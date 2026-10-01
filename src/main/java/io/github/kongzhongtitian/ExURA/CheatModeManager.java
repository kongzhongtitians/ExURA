package io.github.kongzhongtitian.ExURA;

import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 管理作弊模式的配置文件开关。
 * 目录：config/exura/cheat/
 * 文件：空文件 "True"（作弊开）或 "False"（作弊关）
 * 启动时从文件同步到 GlobalVars；命令切换时同时改文件和 GlobalVars。
 */
public class CheatModeManager {

    private static final Path CHEAT_DIR =
            FMLPaths.CONFIGDIR.get().resolve("exura").resolve("cheat");
    private static final String TRUE_FILE = "True";
    private static final String FALSE_FILE = "False";

    /**
     * 服务器启动时调用：从文件系统读取作弊状态，同步到 GlobalVars。
     * - 存在 True → cheat_mode = 1
     * - 存在 False → cheat_mode = 0
     * - 其他情况（目录为空或文件名被改）→ 重新生成 False，cheat_mode = 0
     */
    public static void syncFromFile() {
        try {
            Files.createDirectories(CHEAT_DIR);

            Path truePath = CHEAT_DIR.resolve(TRUE_FILE);
            Path falsePath = CHEAT_DIR.resolve(FALSE_FILE);

            if (Files.exists(truePath)) {
                GlobalVars.getInstance().setValue("cheat_mode", 1);
                ExURA.LOGGER.info("[CheatMode] 检测到 True 文件，作弊模式已开启");
            } else if (Files.exists(falsePath)) {
                GlobalVars.getInstance().setValue("cheat_mode", 0);
                ExURA.LOGGER.info("[CheatMode] 检测到 False 文件，作弊模式已关闭");
            } else {
                // 用户把文件改成别的名字了，或者目录是空的 → 重新生成默认 False
                ExURA.LOGGER.info("[CheatMode] 未找到 True/False 文件，重新生成默认 False");
                Files.createFile(falsePath);
                GlobalVars.getInstance().setValue("cheat_mode", 0);
            }
        } catch (IOException e) {
            ExURA.LOGGER.error("[CheatMode] 读取配置目录失败", e);
        }
    }

    /**
     * 切换作弊模式：同时更新 GlobalVars 和文件系统。
     */
    public static void setCheatMode(boolean enabled) {
        // 先更新全局变量
        GlobalVars.getInstance().setValue("cheat_mode", enabled ? 1 : 0);

        // 再更新文件系统
        try {
            Files.createDirectories(CHEAT_DIR);
            // 删掉两个旧文件
            Files.deleteIfExists(CHEAT_DIR.resolve(TRUE_FILE));
            Files.deleteIfExists(CHEAT_DIR.resolve(FALSE_FILE));
            // 创建新的状态文件
            Files.createFile(CHEAT_DIR.resolve(enabled ? TRUE_FILE : FALSE_FILE));
            ExURA.LOGGER.info("[CheatMode] 作弊模式已切换为: " + (enabled ? "True" : "False"));
        } catch (IOException e) {
            ExURA.LOGGER.error("[CheatMode] 更新配置文件失败", e);
        }
    }
}
