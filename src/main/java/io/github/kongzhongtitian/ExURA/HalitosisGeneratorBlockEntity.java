package io.github.kongzhongtitian.ExURA;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class HalitosisGeneratorBlockEntity extends BlockEntity implements MenuProvider {
    // 槽位定义
    public static final int FUEL_SLOT = 0;
    public static final int ENERGY_CAPACITY = 10000; // 100k FE
    public static final int ENERGY_TRANSFER_RATE = 1000; // 1k FE/t
    public static final int FUEL_BURN_TIME = 0;
    public static final int ENERGY_STORED = 1;
    public static final int MAX_BURN_TIME = 2;
    public static final int MAX_ENERGY = 3;

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            // 只有燃料可以放入
            return stack.is(Items.DRAGON_BREATH);
        }
    };

    private final EnergyStorage energyStorage = new EnergyStorage(ENERGY_CAPACITY, ENERGY_TRANSFER_RATE, ENERGY_TRANSFER_RATE, 0);

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    // 数据同步
    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case FUEL_BURN_TIME -> burnTime;
                case ENERGY_STORED -> energyStorage.getEnergyStored();
                case MAX_BURN_TIME -> currentBurnTime;
                case MAX_ENERGY -> energyStorage.getMaxEnergyStored();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case FUEL_BURN_TIME -> burnTime = value;
                case ENERGY_STORED -> energyStorage.receiveEnergy(value, false);
                case MAX_BURN_TIME -> currentBurnTime = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    // 燃料燃烧相关
    private int burnTime = 0;
    private int currentBurnTime = 0;
    private int energyGenerationRate = 40; // FE/tick

    public HalitosisGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ExURABlockEntity.HALITOSIS_GENERATOR_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.exura.halitosis_generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new HalitosisGeneratorMenu(containerId, inventory, this, this.data);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) return;

        // 处理燃料燃烧
        if (isBurning()) {
            burnTime--;

            // 生成能量
            if (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
                energyStorage.receiveEnergy(energyGenerationRate, false);
                setChanged();
            }

            // 更新方块状态
            if (!state.getValue(HalitosisGenerator.ACTIVE)) {
                level.setBlock(pos, state.setValue(HalitosisGenerator.ACTIVE, true), 3);
            }

            // 检查燃料是否烧完
            if (burnTime <= 0) {
                stopBurning();
                level.setBlock(pos, state.setValue(HalitosisGenerator.ACTIVE, false), 3);
            }
        } else {
            // 尝试从燃料槽获取燃料
            ItemStack fuelStack = itemHandler.getStackInSlot(FUEL_SLOT);
            if (!fuelStack.isEmpty()) {
                int burnTimeValue = 12000;
                if (burnTimeValue > 0) {
                    startBurning(fuelStack, burnTimeValue);
                    setChanged();

                    // 更新方块状态
                    if (!state.getValue(HalitosisGenerator.ACTIVE)) {
                        level.setBlock(pos, state.setValue(HalitosisGenerator.ACTIVE, true), 3);
                    }
                }
            } else {
                // 没有燃料，确保方块状态正确
                if (state.getValue(HalitosisGenerator.ACTIVE)) {
                    level.setBlock(pos, state.setValue(HalitosisGenerator.ACTIVE, false), 3);
                }
            }
        }

        // 向相邻方块输出能量
        transferEnergyToNeighbors(level, pos);

        // 保存数据
        setChanged();
    }

    private boolean isBurning() {
        return burnTime > 0;
    }

    private void startBurning(ItemStack fuelStack, int burnTimeValue) {
        this.burnTime = burnTimeValue;
        this.currentBurnTime = burnTimeValue;

        // 消耗燃料
        fuelStack.shrink(1);

        ExURA.LOGGER.debug("开始燃烧燃料，燃烧时间: {} ticks", burnTimeValue);
    }

    private void stopBurning() {
        this.burnTime = 0;
        this.currentBurnTime = 0;

        ExURA.LOGGER.debug("燃料燃烧完毕");
    }

    private void transferEnergyToNeighbors(Level level, BlockPos pos) {
        if (energyStorage.getEnergyStored() <= 0) return;

        for (Direction direction : Direction.values()) {
            BlockEntity neighborEntity = level.getBlockEntity(pos.relative(direction));
            if (neighborEntity != null) {
                neighborEntity.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).ifPresent(neighborEnergy -> {
                    if (neighborEnergy.canReceive()) {
                        int energyToTransfer = Math.min(energyStorage.getEnergyStored(), ENERGY_TRANSFER_RATE);
                        int energyTransferred = neighborEnergy.receiveEnergy(energyToTransfer, false);
                        energyStorage.extractEnergy(energyTransferred, false);

                        if (energyTransferred > 0) {
                            ExURA.LOGGER.debug("向 {} 方向传输 {} FE", direction, energyTransferred);
                        }
                    }
                });
            }
        }
    }

    // Capability 处理
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> energyStorage);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    // NBT 数据持久化
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.put("inventory", itemHandler.serializeNBT());

        CompoundTag energyTag = new CompoundTag();
        energyTag.putInt("energy", energyStorage.getEnergyStored());
        tag.put("energy", energyTag);

        tag.putInt("burnTime", burnTime);
        tag.putInt("currentBurnTime", currentBurnTime);

        ExURA.LOGGER.debug("保存发电机数据，能量: {} FE", energyStorage.getEnergyStored());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (tag.contains("inventory")) {
            itemHandler.deserializeNBT(tag.getCompound("inventory"));
        }

        if (tag.contains("energy")) {
            CompoundTag energyTag = tag.getCompound("energy");
            int energy = energyTag.getInt("energy");
            energyStorage.receiveEnergy(energy, false);
        }

        burnTime = tag.getInt("burnTime");
        currentBurnTime = tag.getInt("currentBurnTime");

        ExURA.LOGGER.debug("加载发电机数据，能量: {} FE", energyStorage.getEnergyStored());
    }

    // 获取方法
    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public int getCurrentBurnTime() {
        return currentBurnTime;
    }

    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    public int getMaxEnergyStored() {
        return energyStorage.getMaxEnergyStored();
    }

    // 调试方法
    public void printStatus() {
        ExURA.LOGGER.info("发电机状态:");
        ExURA.LOGGER.info("  能量: {}/{} FE", getEnergyStored(), getMaxEnergyStored());
        ExURA.LOGGER.info("  燃烧时间: {}/{} ticks", burnTime, currentBurnTime);
        ExURA.LOGGER.info("  燃料槽: {}", itemHandler.getStackInSlot(FUEL_SLOT));
        ExURA.LOGGER.info("  活跃: {}", level != null ?
                level.getBlockState(getBlockPos()).getValue(HalitosisGenerator.ACTIVE) : "未知");
    }

    public boolean stillValid(Player player) {
        if (this.level == null || this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        }

        // 检查玩家距离（8格内）
        return player.distanceToSqr(
                (double)this.worldPosition.getX() + 0.5D,
                (double)this.worldPosition.getY() + 0.5D,
                (double)this.worldPosition.getZ() + 0.5D
        ) <= 64.0D;
    }

    public static boolean isFuel(ItemStack stack) {
        return stack.is(Items.DRAGON_BREATH);
    }

}