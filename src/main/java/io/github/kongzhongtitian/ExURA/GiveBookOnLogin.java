package io.github.kongzhongtitian.ExURA;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.patchouli.api.PatchouliAPI;

public class GiveBookOnLogin {
        @SubscribeEvent
        public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
                if (event.getEntity().level().isClientSide) return;
                var player = event.getEntity();

                var data = player.getPersistentData();
                var key = "klux_has_book";  // 标记玩家是否已获得手册

                if (!data.getBoolean(key)) {
                        // 构造 Patchouli 手册 ItemStack
                        ItemStack book = PatchouliAPI.get().getBookStack(
                                new ResourceLocation(ExURA.MODID, "exura_book")
                        );
                        player.getInventory().add(book);
                        data.putBoolean(key, true);
                }
        }
}