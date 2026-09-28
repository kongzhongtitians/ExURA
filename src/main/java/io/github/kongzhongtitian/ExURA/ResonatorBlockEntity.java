package io.github.kongzhongtitian.ExURA;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ResonatorBlockEntity extends DTBaseProcessingBlockEntity implements MenuProvider {
    public static final int INPUT_SLOT_1 = 0;
    public static final int INPUT_SLOT_2 = 1;
    public static final int OUTPUT_SLOT = 2;

    public ResonatorBlockEntity(BlockPos pos, BlockState state) {
        super(ExURABlockEntity.RESONATOR_BLOCK_ENTITY.get(), pos, state);
        this.setItemStackHandler(5);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.exura.resonator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ResonatorMenu(containerId, inventory, this, this.data);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        Optional<ResonatorRecipe> recipe = getRecipe(ExURARecipe.RESONATOR_RECIPE.get());

        if (recipe.isPresent() && hasRecipe(ExURARecipe.RESONATOR_RECIPE.get())) {
            this.maxProgress = recipe.get().getProcessingTime() > 0 ?
                    recipe.get().getProcessingTime() : 20;
            this.data.set(1, this.maxProgress);

            GlobalVars globals = GlobalVars.getInstance();

            // 开始新一次加工前检查 GP 是否足够，并预扣费用
            if (progress == 0) {
                if (globals.getValue("used_gp") + 8 > globals.getValue("all_gp")) {
                    ExURA.LOGGER.info("no");
                    return;
                }
                globals.increase("used_gp", 8);
                ExURA.LOGGER.info("yes");
            }

            progress++;
            this.data.set(0, this.progress);
            setChanged();

            if (progress >= maxProgress) {
                craftItem(recipe.get());
                ExURA.LOGGER.info("ok-1");
                resetProgress();
                ExURA.LOGGER.info("ok-2");
                globals.decrease("used_gp",8);
                ExURA.LOGGER.info("ok-3");
            }
        } else {
            resetProgress();
        }
    }

    private <T extends InputOutputRecipe> void craftItem(T recipe) {
        RecipeOutputUtil.consumeInputs(recipe, itemStackHandler, List.of(INPUT_SLOT_1, INPUT_SLOT_2));
        RecipeOutputUtil.produceOutputs(recipe.getOutputs(), itemStackHandler, List.of(2));
    }

    private void resetProgress() {
        progress = 0;
        maxProgress = 20;
        this.data.set(0, progress);
        this.data.set(1, maxProgress);
        setChanged();
    }

    private <T extends InputOutputRecipe> boolean hasRecipe(RecipeType<T> recipeType) {
        if (recipeType == null) return false;

        Optional<List<ItemStack>> outputs = RecipeOutputUtil.getOutputs(recipeType, getSlotsItemStack(), level);
        if (outputs.isEmpty()) return false;

        return RecipeOutputUtil.canFitOutputs(outputs.get(), getSlotsOutputItemStack());
    }

    private <T extends InputOutputRecipe> Optional<T> getRecipe(RecipeType<T> recipeType) {
        if (level == null) return Optional.empty();
        return RecipeOutputUtil.getRecipe(recipeType, getSlotsItemStack(), level);
    }

    protected List<ItemStack> getSlotsItemStack() {
        return List.of(
                itemStackHandler.getStackInSlot(INPUT_SLOT_1),
                itemStackHandler.getStackInSlot(INPUT_SLOT_2)
        );
    }

    protected List<ItemStack> getSlotsOutputItemStack() {
        return List.of(
                itemStackHandler.getStackInSlot(OUTPUT_SLOT)
        );
    }
}
