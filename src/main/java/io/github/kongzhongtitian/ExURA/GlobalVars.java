package io.github.kongzhongtitian.ExURA;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.world.level.storage.LevelResource;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Collections;

public class GlobalVars {
    private static volatile GlobalVars instance;
    private final Map<String, AtomicInteger> variables = new ConcurrentHashMap<>();
    private File saveFile;
    private boolean initialized = false;

    // 私有构造函数，不立即初始化
    private GlobalVars() {
        ExURA.LOGGER.info("GlobalVars 实例已创建，等待服务器初始化...");
    }

    // 单例模式
    public static GlobalVars getInstance() {
        if (instance == null) {
            synchronized (GlobalVars.class) {
                if (instance == null) {
                    instance = new GlobalVars();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化方法，应该在服务器启动后调用
     */
    public void initialize() {
        if (initialized) {
            return;
        }

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            this.saveFile = getSaveFile(server);
            loadFromFile();
            initialized = true;
            ExURA.LOGGER.info("GlobalVars 已成功初始化");
        } else {
            ExURA.LOGGER.warn("GlobalVars 初始化失败：服务器未运行");
        }
    }

    @Nullable
    private File getSaveFile(MinecraftServer server) {
        try {
            Path worldPath = server.getWorldPath(LevelResource.ROOT);
            return worldPath.resolve("exura_global_vars.dat").toFile();
        } catch (Exception e) {
            ExURA.LOGGER.error("获取保存文件路径失败", e);
            return null;
        }
    }

    private void loadFromFile() {
        if (saveFile == null || !saveFile.exists()) {
            ExURA.LOGGER.info("未找到保存文件，使用默认值");
            return;
        }

        try (FileInputStream fis = new FileInputStream(saveFile)) {
            CompoundTag tag = NbtIo.readCompressed(fis);
            CompoundTag varsTag = tag.getCompound("variables");

            variables.clear();
            for (String key : varsTag.getAllKeys()) {
                variables.put(key, new AtomicInteger(varsTag.getInt(key)));
            }

            ExURA.LOGGER.info("从文件加载全局变量: " + variables.size() + " 个");
        } catch (IOException e) {
            ExURA.LOGGER.error("加载全局变量文件失败", e);
        }
    }

    private void saveToFile() {
        if (saveFile == null || !initialized) {
            // 如果未初始化，则不保存
            return;
        }

        try {
            if (!saveFile.getParentFile().exists()) {
                saveFile.getParentFile().mkdirs();
            }

            CompoundTag tag = new CompoundTag();
            CompoundTag varsTag = new CompoundTag();

            for (Map.Entry<String, AtomicInteger> entry : variables.entrySet()) {
                varsTag.putInt(entry.getKey(), entry.getValue().get());
            }

            tag.put("variables", varsTag);

            try (FileOutputStream fos = new FileOutputStream(saveFile)) {
                NbtIo.writeCompressed(tag, fos);
            }

            ExURA.LOGGER.debug("保存全局变量到文件: " + variables.size() + " 个");
        } catch (IOException e) {
            ExURA.LOGGER.error("保存全局变量文件失败", e);
        }
    }

    /**
     * 安全检查方法
     */
    private void checkInitialized() {
        if (!initialized) {
            // 尝试初始化
            initialize();
            if (!initialized) {
                ExURA.LOGGER.warn("GlobalVars 尚未初始化，操作可能不会持久化");
            }
        }
    }

    /**
     * 为指定变量增加数值（线程安全）
     */
    public int increase(String varName, int amount) {
        checkInitialized();

        AtomicInteger variable = getVariable(varName);
        int oldValue = variable.get();
        int newValue = variable.addAndGet(amount);

        ExURA.LOGGER.info("+_ [" + varName + "]: " + oldValue + " + " + amount + " = " + newValue);

        // 异步保存到文件
        if (initialized) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                server.execute(this::saveToFile);
            }
        }

        return newValue;
    }

    public int decrease(String varName, int amount) {
        return increase(varName, -amount);
    }

    /**
     * 获取指定变量的数值
     */
    public int getValue(String varName) {
        checkInitialized();
        AtomicInteger variable = getVariable(varName);
        int value = variable.get();
        ExURA.LOGGER.info("get_ [" + varName + "]: " + value);
        return value;
    }

    /**
     * 设置指定变量的数值
     */
    public int setValue(String varName, int value) {
        checkInitialized();

        AtomicInteger variable = getVariable(varName);
        int oldValue = variable.getAndSet(value);

        ExURA.LOGGER.info("set_ [" + varName + "]: " + oldValue + " -> " + value);

        if (initialized) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                server.execute(this::saveToFile);
            }
        }

        return oldValue;
    }

    /**
     * 手动保存数据
     */
    public void save() {
        if (initialized) {
            saveToFile();
        }
    }

    /**
     * 重新加载数据
     */
    public void reload() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            this.saveFile = getSaveFile(server);
            loadFromFile();
        }
    }

    /**
     * 内部方法：获取或创建变量（线程安全）
     */
    private AtomicInteger getVariable(String varName) {
        return variables.computeIfAbsent(varName, k -> {
            ExURA.LOGGER.info("newvar: " + varName + " (初始值: 0)");
            return new AtomicInteger(0);
        });
    }

    /**
     * 检查是否已初始化
     */
    public boolean isInitialized() {
        return initialized;
    }
}
