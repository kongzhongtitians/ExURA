package io.github.kongzhongtitian.ExURA;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SolarPanelBlockEntity extends BlockEntity {
    private int cooldown = 0;
    private long lastFire = 0; // 改为实例变量，每个水车独立

    public SolarPanelBlockEntity(BlockPos pos, BlockState state) {
        super(ExURABlockEntity.SOLAR_PANEL_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SolarPanelBlockEntity entity) {
        if (level.isClientSide) return;

        entity.cooldown++;
        if (entity.cooldown >= 40) {
            entity.cooldown = 0;

            long tick = level.getGameTime();

            ExURA.LOGGER.debug("sun {} gets {} pieces of tick", pos, tick);

            // 只有当水方块数量发生变化时才更新
            if (entity.lastFire != tick && entity.lastFire != 12000) {
                if (entity.lastFire % 24000 < 12000 && tick % 24000 > 12000){
                    GlobalVars globals = GlobalVars.getInstance();
                    globals.decrease("all_gp",1);
                } else if (entity.lastFire % 24000 > 12000 && tick % 24000 < 12000) {
                    GlobalVars globals = GlobalVars.getInstance();
                    globals.increase("all_gp",1);
                } else if (tick % 24000 < 12000){
                    GlobalVars globals = GlobalVars.getInstance();
                    globals.increase("all_gp",1);
                }

                // 更新当前水车的 lastFire 值
                entity.lastFire = tick;
                entity.setChanged(); // 标记需要保存数据
            }
        }
    }

    /**
     * 计算周围的水方块数量
     */
    private static int countFireBlocks(Level level, BlockPos pos) {
        int fireCount = 0;

        // 检查四个水平方向
        BlockPos[] checkPositions = {
                pos.below() // 南
        };

        for (BlockPos checkPos : checkPositions) {
            if (level.getBlockState(checkPos).is(Blocks.FIRE)) {
                fireCount++;
            }
        }

        return fireCount;
    }

    /**
     * 保存数据到NBT
     */
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.cooldown = tag.getInt("Cooldown");
        this.lastFire = tag.getInt("LastFire");
    }

    /**
     * 从NBT加载数据
     */
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Cooldown", this.cooldown);
        tag.putLong("LastFire", this.lastFire);
    }

    /**
     * 获取当前水方块数量（用于调试或其他用途）
     */
    public int getCurrentFireCount() {
        if (level == null) return 0;
        return countFireBlocks(level, getBlockPos());
    }

    //

    /**
     * 手动更新水方块计数（如果需要）
     */

}