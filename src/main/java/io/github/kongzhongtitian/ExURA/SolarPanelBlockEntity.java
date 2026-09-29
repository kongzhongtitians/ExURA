package io.github.kongzhongtitian.ExURA;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SolarPanelBlockEntity extends BlockEntity {
    private int cooldown = 0;
    private boolean hasBonus = false; // 当前是否正在贡献 1 GP（白天）

    public SolarPanelBlockEntity(BlockPos pos, BlockState state) {
        super(ExURABlockEntity.SOLAR_PANEL_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SolarPanelBlockEntity entity) {
        if (level.isClientSide) return;

        entity.cooldown++;
        if (entity.cooldown >= 40) {
            entity.cooldown = 0;

            boolean isDaytime = level.getGameTime() % 24000 < 12000;

            GlobalVars globals = GlobalVars.getInstance();

            if (isDaytime && !entity.hasBonus) {
                // 进入白天，发放 1 GP
                globals.increase("all_gp", 1);
                entity.hasBonus = true;
                entity.setChanged();
            } else if (!isDaytime && entity.hasBonus) {
                // 进入晚上，收回 1 GP
                globals.decrease("all_gp", 1);
                entity.hasBonus = false;
                entity.setChanged();
            }
        }
    }

    /** 当前是否正在贡献 GP（用于破坏时扣除） */
    public boolean hasBonus() {
        return this.hasBonus;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.cooldown = tag.getInt("Cooldown");
        this.hasBonus = tag.getBoolean("HasBonus");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Cooldown", this.cooldown);
        tag.putBoolean("HasBonus", this.hasBonus);
    }
}
