package io.github.kongzhongtitian.ExURA;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DragonEggMillBlockEntity extends BlockEntity {
    private int cooldown = 0;
    private int lastDangonEgg = 0; // 改为实例变量，每个水车独立

    public DragonEggMillBlockEntity(BlockPos pos, BlockState state) {
        super(ExURABlockEntity.DRAGON_EGG_MILL_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DragonEggMillBlockEntity entity) {
        if (level.isClientSide) return;

        entity.cooldown++;
        if (entity.cooldown >= 40) {
            entity.cooldown = 0;

            // 检测四个方向的方块
            int currentDangonEgg = countDangonEggBlocks(level, pos);

            ExURA.LOGGER.debug("dangonEgg_wall {} gets {} pieces of dangonEgg", pos, currentDangonEgg);

            // 只有当水方块数量发生变化时才更新
            if (entity.lastDangonEgg != currentDangonEgg) {
                GlobalVars globals = GlobalVars.getInstance();
                int dangonEggDifference = currentDangonEgg - entity.lastDangonEgg;
                globals.increase("all_gp", dangonEggDifference * 500);

                // 更新当前水车的 lastDangonEgg 值
                entity.lastDangonEgg = currentDangonEgg;
                entity.setChanged(); // 标记需要保存数据
            }
        }
    }

    /**
     * 计算周围的水方块数量
     */
    private static int countDangonEggBlocks(Level level, BlockPos pos) {
        int dangonEggCount = 0;

        // 检查四个水平方向
        BlockPos[] checkPositions = {
                pos.above() // 南
        };

        for (BlockPos checkPos : checkPositions) {
            if (level.getBlockState(checkPos).is(Blocks.DRAGON_EGG)) {
                dangonEggCount++;
            }
        }

        return dangonEggCount;
    }

    /**
     * 保存数据到NBT
     */
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.cooldown = tag.getInt("Cooldown");
        this.lastDangonEgg = tag.getInt("LastDangonEgg");
    }

    /**
     * 从NBT加载数据
     */
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Cooldown", this.cooldown);
        tag.putInt("LastDangonEgg", this.lastDangonEgg);
    }

    /**
     * 获取当前水方块数量（用于调试或其他用途）
     */
    public int getCurrentDangonEggCount() {
        if (level == null) return 0;
        return countDangonEggBlocks(level, getBlockPos());
    }

    /**
     * 获取上次记录的水方块数量
     */
    public int getLastDangonEggCount() {
        return this.lastDangonEgg;
    }

    /**
     * 手动更新水方块计数（如果需要）
     */
    public void updateDangonEggCount() {
        if (level == null || level.isClientSide()) return;

        int currentDangonEgg = countDangonEggBlocks(level, getBlockPos());
        if (this.lastDangonEgg != currentDangonEgg) {
            GlobalVars globals = GlobalVars.getInstance();
            int dangonEggDifference = currentDangonEgg - this.lastDangonEgg;
            globals.increase("all_GP", dangonEggDifference * 4);

            this.lastDangonEgg = currentDangonEgg;
            this.setChanged();
        }
    }
}