package io.github.kongzhongtitian.ExURA;

import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExURA.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GlobalVarsEventHandler {

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        // 服务器完全启动后再初始化GlobalVars
        ExURA.LOGGER.info("服务器已启动，正在初始化GlobalVars...");
        GlobalVars.getInstance().initialize();
        // 从配置文件同步作弊模式
        CheatModeManager.syncFromFile();
    }

    @SubscribeEvent
    public static void onWorldSave(LevelEvent.Save event) {
        // 世界保存时也保存全局变量
        if (!event.getLevel().isClientSide()) {
            GlobalVars.getInstance().save();
        }
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        // 服务器停止时保存数据
        GlobalVars.getInstance().save();
        ExURA.LOGGER.info("服务器已停止，GlobalVars数据已保存");
    }
}
