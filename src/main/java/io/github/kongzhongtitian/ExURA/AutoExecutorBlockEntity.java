package io.github.kongzhongtitian.ExURA;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

public class AutoExecutorBlockEntity extends BlockEntity {
    private static final int COOLDOWN_TICKS = 20; // 1秒操作一次
    private int cooldown = 0;

    public AutoExecutorBlockEntity(BlockPos pos, BlockState state) {
        super(ExURABlockEntity.AUTO_EXECUTOR_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AutoExecutorBlockEntity entity) {
        if (level.isClientSide()) return;

        entity.cooldown--;
        if (entity.cooldown > 0) return;
        entity.cooldown = COOLDOWN_TICKS;

        Direction facing = state.getValue(AutoExecutor.FACING);
        boolean powered = level.hasNeighborSignal(pos);

        if (powered) {
            entity.breakBlock(level, pos, facing);
        } else {
            entity.placeBlock(level, pos, facing);
        }
    }

    // ---------- 破坏模式 ----------
    private void breakBlock(Level level, BlockPos pos, Direction facing) {
        BlockPos targetPos = pos.relative(facing);
        BlockState targetState = level.getBlockState(targetPos);
        if (targetState.isAir()) return;

        ServerLevel serverLevel = (ServerLevel) level;
        Player fakePlayer = FakePlayerFactory.getMinecraft(serverLevel);
        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DIAMOND_PICKAXE));

        // 获取钻石镐对应的掉落物
        List<ItemStack> drops = Block.getDrops(targetState, serverLevel, targetPos,
                level.getBlockEntity(targetPos), fakePlayer, fakePlayer.getMainHandItem());

        // 移除方块（不掉落物品）
        level.destroyBlock(targetPos, false);

        // 将掉落物存入附近容器
        for (ItemStack stack : drops) {
            insertIntoNearbyContainers(level, pos, stack);
        }
    }

    // ---------- 放置模式 ----------
    private void placeBlock(Level level, BlockPos pos, Direction facing) {
        BlockPos targetPos = pos.relative(facing);
        if (!level.getBlockState(targetPos).isAir()) return;

        ItemStack stack = extractPlaceableItem(level, pos);
        if (stack.isEmpty()) return;

        // 使用假玩家放置方块
        ServerLevel serverLevel = (ServerLevel) level;
        Player fakePlayer = FakePlayerFactory.getMinecraft(serverLevel);
        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, stack);
        fakePlayer.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, facing.toYRot(), 0);

        BlockHitResult hitResult = new BlockHitResult(
                Vec3.atCenterOf(targetPos),
                facing.getOpposite(),
                targetPos,
                false
        );

        // 尝试放置
        if (stack.getItem() instanceof BlockItem blockItem) {
            blockItem.place(new net.minecraft.world.item.context.DirectionalPlaceContext(
                    level, targetPos, facing, stack, facing.getOpposite()
            ));
            // 如果放置失败（stack 仍有物品），放回附近容器，避免物品丢失
            if (!stack.isEmpty()) {
                insertIntoNearbyContainers(level, pos, stack);
            }
        }
    }

    // ---------- 物品交互 ----------
    private ItemStack extractPlaceableItem(Level level, BlockPos pos) {
        for (BlockPos nearby : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            BlockEntity be = level.getBlockEntity(nearby);
            if (be == null) continue;

            IItemHandler handler = be.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
            if (handler == null) continue;

            for (int slot = 0; slot < handler.getSlots(); slot++) {
                ItemStack stack = handler.getStackInSlot(slot);
                if (stack.getItem() instanceof BlockItem) {
                    // 模拟提取1个
                    ItemStack extracted = handler.extractItem(slot, 1, true);
                    if (!extracted.isEmpty()) {
                        return handler.extractItem(slot, 1, false);
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    private void insertIntoNearbyContainers(Level level, BlockPos pos, ItemStack stack) {
        if (stack.isEmpty()) return;

        for (BlockPos nearby : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            BlockEntity be = level.getBlockEntity(nearby);
            if (be == null) continue;

            IItemHandler handler = be.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
            if (handler == null) continue;

            stack = ItemHandlerHelper.insertItemStacked(handler, stack, false);
            if (stack.isEmpty()) return;
        }
    }
}
