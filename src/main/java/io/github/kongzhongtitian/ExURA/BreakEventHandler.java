package io.github.kongzhongtitian.ExURA;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "exura", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BreakEventHandler {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState blockState = event.getState();

        // 只在服务端处理
        if (level.isClientSide()) return;

        // 只处理水车方块被破坏的情况
        if (blockState.getBlock() instanceof io.github.kongzhongtitian.ExURA.WaterMill) {
            handleWaterMillBreak(level, pos);
        }

        if (blockState.getBlock() instanceof FireMill) {
            handleFireMillBreak(level, pos);
        }

        if (blockState.getBlock() instanceof DragonEggMill) {
            handleDragonEggMillBreak(level, pos);
        }

        if (blockState.getBlock() instanceof SolarPanel) {
            handleSolarPanelBreak(level, pos);
        }

        if (blockState.getBlock() instanceof LavaMill) {
            handleLavaMillBreak(level, pos);
        }

        if (blockState.getBlock() instanceof LunarPanel) {
            handleLunarPanelBreak(level, pos);
        }
    }

    /**
     * 处理水车破坏
     */
    private static void handleWaterMillBreak(Level level, BlockPos waterMillPos) {
        // 在方块实体被移除前获取数据
        if (level.getBlockEntity(waterMillPos) instanceof io.github.kongzhongtitian.ExURA.WaterMillBlockEntity waterMill) {
            int lastWater = waterMill.getLastWaterCount();
            int gpReduction = lastWater * 1;

            GlobalVars globals = GlobalVars.getInstance();
            globals.decrease("all_gp", gpReduction);

            System.out.println("water will die: " + waterMillPos +
                    ", remove " + lastWater + " pieces of water, - " + gpReduction + " GP");
        }
    }

    private static void handleFireMillBreak(Level level, BlockPos fireMillPos) {
        // 在方块实体被移除前获取数据
        if (level.getBlockEntity(fireMillPos) instanceof FireMillBlockEntity fireMill) {
            int gpReduction = 4;

            GlobalVars globals = GlobalVars.getInstance();
            globals.decrease("all_gp", gpReduction);

            System.out.println("fire will die: " + fireMillPos +
                    ", remove  1  pieces of water, - " + gpReduction + " GP");
        }
    }

    private static void handleDragonEggMillBreak(Level level, BlockPos fireMillPos) {
        // 在方块实体被移除前获取数据
        if (level.getBlockEntity(fireMillPos) instanceof DragonEggMillBlockEntity dragonEggMill) {
            // 检查上方是否有龙蛋
            BlockPos abovePos = fireMillPos.above();
            if (level.getBlockState(abovePos).getBlock() == Blocks.DRAGON_EGG) {
                int gpReduction = 500;

                GlobalVars globals = GlobalVars.getInstance();
                globals.decrease("all_gp", gpReduction);

                System.out.println("d mill die: " + fireMillPos +
                        ", remove 1 pieces of water, - " + gpReduction + " GP");
            }
        }
    }

    private static void handleSolarPanelBreak(Level level, BlockPos fireMillPos) {
        // 在方块实体被移除前获取数据
        if (level.getBlockEntity(fireMillPos) instanceof SolarPanelBlockEntity dragonEggMill) {
            int up_gp;
            if (level.getGameTime() % 24000 < 12000){
                up_gp = 1;
            }else {
                up_gp = 0;
            }

            GlobalVars globals = GlobalVars.getInstance();
            globals.decrease("all_gp", up_gp);

            System.out.println("d mill die: " + fireMillPos +
                    ", remove  1  pieces of water, - " + up_gp + " GP");
        }
    }

    private static void handleLunarPanelBreak(Level level, BlockPos fireMillPos) {
        // 在方块实体被移除前获取数据
        if (level.getBlockEntity(fireMillPos) instanceof LunarPanelBlockEntity dragonEggMill) {
            int up_gp;
            if (level.getGameTime() % 24000 > 12000){
                up_gp = 1;
            }else {
                up_gp = 0;
            }

            GlobalVars globals = GlobalVars.getInstance();
            globals.decrease("all_gp", up_gp);

            System.out.println("d mill die: " + fireMillPos +
                    ", remove  1  pieces of water, - " + up_gp + " GP");
        }
    }

    private static void handleLavaMillBreak(Level level, BlockPos waterMillPos) {
        // 在方块实体被移除前获取数据
        if (level.getBlockEntity(waterMillPos) instanceof io.github.kongzhongtitian.ExURA.LavaMillBlockEntity waterMill) {
            int lastWater = waterMill.getLastWaterCount();
            int gpReduction = lastWater * 2;

            GlobalVars globals = GlobalVars.getInstance();
            globals.decrease("all_gp", gpReduction);

            System.out.println("lava will die: " + waterMillPos +
                    ", remove " + lastWater + " pieces of water, - " + gpReduction + " GP");
        }
    }
}
