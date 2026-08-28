package io.github.kongzhongtitian.ExURA;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WaterMillBlockEntity extends BlockEntity {
    private int cooldown = 0;
    private int lastWater = 0; // 改为实例变量，每个水车独立

    public WaterMillBlockEntity(BlockPos pos, BlockState state) {
        super(ExURABlockEntity.WATER_MILL_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, WaterMillBlockEntity entity) {
        if (level.isClientSide) return;

        entity.cooldown++;
        if (entity.cooldown >= 40) {
            entity.cooldown = 0;

            // 检测四个方向的方块
            int currentWater = countWaterBlocks(level, pos);

            ExURA.LOGGER.debug("water_wall {} gets {} pieces of water", pos, currentWater);

            // 只有当水方块数量发生变化时才更新
            if (entity.lastWater != currentWater) {
                GlobalVars globals = GlobalVars.getInstance();
                int waterDifference = currentWater - entity.lastWater;
                globals.increase("all_gp", waterDifference * 1);

                // 更新当前水车的 lastWater 值
                entity.lastWater = currentWater;
                entity.setChanged(); // 标记需要保存数据
            }
        }
    }

    /**
     * 计算周围的水方块数量
     */
    private static int countWaterBlocks(Level level, BlockPos pos) {
        int waterCount = 0;

        // 检查四个水平方向
        BlockPos[] checkPositions = {
                pos.east(),  // 东
                pos.west(),  // 西
                pos.north(), // 北
                pos.south()  // 南
        };

        for (BlockPos checkPos : checkPositions) {
            if (level.getBlockState(checkPos).is(Blocks.WATER)) {
                waterCount++;
            }
        }

        return waterCount;
    }

    /**
     * 保存数据到NBT
     */
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.cooldown = tag.getInt("Cooldown");
        this.lastWater = tag.getInt("LastWater");
    }

    /**
     * 从NBT加载数据
     */
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Cooldown", this.cooldown);
        tag.putInt("LastWater", this.lastWater);
    }

    /**
     * 获取当前水方块数量（用于调试或其他用途）
     */
    public int getCurrentWaterCount() {
        if (level == null) return 0;
        return countWaterBlocks(level, getBlockPos());
    }

    /**
     * 获取上次记录的水方块数量
     */
    public int getLastWaterCount() {
        return this.lastWater;
    }

    /**
     * 手动更新水方块计数（如果需要）
     */
    public void updateWaterCount() {
        if (level == null || level.isClientSide()) return;

        int currentWater = countWaterBlocks(level, getBlockPos());
        if (this.lastWater != currentWater) {
            GlobalVars globals = GlobalVars.getInstance();
            int waterDifference = currentWater - this.lastWater;
            globals.increase("all_GP", waterDifference * 1);

            this.lastWater = currentWater;
            this.setChanged();
        }
    }
}