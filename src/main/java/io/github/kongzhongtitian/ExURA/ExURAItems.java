package io.github.kongzhongtitian.ExURA;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ExURAItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ExURA.MODID);

    public static final RegistryObject<Item> ENDER_SHARD = registerSimpleItem("ender_shard");
    public static final RegistryObject<Item> REDSTONE_CRYSTAL = registerSimpleItem("redstone_crystal");
    public static final RegistryObject<Item> REDSTONE_GEAR = registerSimpleItem("redstone_gear");
    public static final RegistryObject<Item> UPGRADE_BASE = registerSimpleItem("upgrade_base");
    public static final RegistryObject<Item> DYE_POWDER_LUNAR = registerSimpleItem("dye_powder_lunar");
    public static final RegistryObject<Item> DEMON_INGOT = registerSimpleItem("demon_ingot");
    public static final RegistryObject<Item> EVIL_DROP = registerSimpleItem("evil_drop");
    public static final RegistryObject<Item> EYE_REDSTONE = registerSimpleItem("eye_redstone");
    public static final RegistryObject<Item> RED_COAL = registerSimpleItem("red_coal");
    public static final RegistryObject<Item> MOON_STONE = registerSimpleItem("moon_stone");
    public static final RegistryObject<Item> ENCHANTED_INGOT = registerSimpleItem("enchanted_ingot");
    public static final RegistryObject<Item> UNSTABLE_INGOT_OUTLINE = registerSimpleItem("unstable_ingot_outline");
    public static final RegistryObject<Item> UNSTABLE_NUGGET_OUTLINE = registerSimpleItem("unstable_nugget_outline");
    public static final RegistryObject<Item> EVIL_INFUSED_INGOT = registerSimpleItem("evil_infused_ingot");
    public static final RegistryObject<Item> RAINBOW_GENERATOR_BOTTOM_TOP = registerSimpleItem("rainbow_generator_bottom_top");
    public static final RegistryObject<Item> RAINBOW_GENERATOR_BOTTOM = registerSimpleItem("rainbow_generator_bottom");
    public static final RegistryObject<Item> UPGRADE_SPEED = ITEMS.register("upgrade_speed",
            () -> new Item(new Item.Properties().stacksTo(4)));
    public static final RegistryObject<Item> UPGRADE_SPEED_ENCHANTED = ITEMS.register("upgrade_speed_enchanted",
            () -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> UPGRADE_SPEED_SUPER = registerSimpleItem("upgrade_speed_super");
    public static final RegistryObject<Item> UPGRADE_STACK = ITEMS.register("upgrade_stack",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MAGICAL_APPLE = registerSimpleFood("magical_apple",10,1.1F);
    public static final RegistryObject<Item> OPINIUMA = registerSimpleItem("opiniuma");
    public static final RegistryObject<Item> OPINIUMB = registerSimpleItem("opiniumb");
    public static final RegistryObject<Item> OPINIUMC = registerSimpleItem("opiniumc");
    public static final RegistryObject<Item> OPINIUMD = registerSimpleItem("opiniumd");
    public static final RegistryObject<Item> OPINIUME = registerSimpleItem("opiniume");
    public static final RegistryObject<Item> OPINIUMF = registerSimpleItem("opiniumf");
    public static final RegistryObject<Item> OPINIUMG = registerSimpleItem("opiniumg");
    public static final RegistryObject<Item> OPINIUMH = registerSimpleItem("opiniumh");
    public static final RegistryObject<Item> OPINIUMI = registerSimpleItem("opiniumi");

    public static final RegistryObject<Item> GLASS_CUTTER = ITEMS.register("glass_cutter",
            () -> new Item(new Item.Properties().stacksTo(1).durability(512)));

    public static RegistryObject<Item> registerSimpleItem(String itemName){
        return ITEMS.register(itemName , ()-> new Item(new Item.Properties()));
    }
    public static RegistryObject<Item> registerSimpleFood(String itemName,int nutrition,float saturation){
        final FoodProperties FOOD_API = (new FoodProperties.Builder())
                .nutrition(nutrition)
                .saturationMod(saturation)
                .build();
        return ITEMS.register(itemName , ()-> new Item(new Item.Properties().food(FOOD_API)));
    }
}
