package io.github.kongzhongtitian.ExURA;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FireMillBlockEntity extends BlockEntity {
    private int cooldown = 0;
    private int lastFire = 0; // 改为实例变量，每个水车独立

    public FireMillBlockEntity(BlockPos pos, BlockState state) {
        super(ExURABlockEntity.FIRE_MILL_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FireMillBlockEntity entity) {
        if (level.isClientSide) return;

        entity.cooldown++;
        if (entity.cooldown >= 40) {
            entity.cooldown = 0;

            // 检测四个方向的方块
            int currentFire = countFireBlocks(level, pos);

            ExURA.LOGGER.debug("fire_wall {} gets {} pieces of fire", pos, currentFire);

            // 只有当水方块数量发生变化时才更新
            if (entity.lastFire != currentFire) {
                GlobalVars globals = GlobalVars.getInstance();
                int fireDifference = currentFire - entity.lastFire;
                globals.increase("all_gp", fireDifference * 4);

                // 更新当前水车的 lastFire 值
                entity.lastFire = currentFire;
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
        tag.putInt("LastFire", this.lastFire);
    }

    /**
     * 获取当前水方块数量（用于调试或其他用途）
     */
    public int getCurrentFireCount() {
        if (level == null) return 0;
        return countFireBlocks(level, getBlockPos());
    }

    /**
     * 获取上次记录的水方块数量
     */
    public int getLastFireCount() {
        return this.lastFire;
    }

    /**
     * 手动更新水方块计数（如果需要）
     */
    public void updateFireCount() {
        if (level == null || level.isClientSide()) return;

        int currentFire = countFireBlocks(level, getBlockPos());
        if (this.lastFire != currentFire) {
            GlobalVars globals = GlobalVars.getInstance();
            int fireDifference = currentFire - this.lastFire;
            globals.increase("all_GP", fireDifference * 4);

            this.lastFire = currentFire;
            this.setChanged();
        }
    }
}