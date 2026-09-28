package io.github.kongzhongtitian.ExURA;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ExURABlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ExURA.MODID);

    // ===================== 属性模板 =====================
    // 参考原版数值：石头 1.5/6.0，圆石 2.0/6.0，铁/金/钻石块 5.0/6.0，
    // 黑曜石 50/1200，木头 2.0/3.0，泥土/沙 0.5/0.5，下界岩 0.4/0.4。
    // strength(a, b) 中 a = 硬度（挖掘时间），b = 爆炸抗性。

    /** 魔法木：用斧挖掘 */
    private static final BlockBehaviour.Properties MAGICAL_WOOD_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .sound(SoundType.WOOD)
            .strength(2.0F, 3.0F)
            .requiresCorrectToolForDrops();

    /** 压缩圆石：逐级硬化 */
    private static final BlockBehaviour.Properties COMPRESSED_COBBLESTONE_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .sound(SoundType.STONE)
            .strength(3.0F, 12.0F)
            .requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties DOUBLE_COMPRESSED_COBBLESTONE_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .sound(SoundType.STONE)
            .strength(4.5F, 18.0F)
            .requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties TRIPLE_COMPRESSED_COBBLESTONE_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .sound(SoundType.STONE)
            .strength(6.0F, 24.0F)
            .requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties QUADRUPLE_COMPRESSED_COBBLESTONE_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .sound(SoundType.STONE)
            .strength(8.0F, 36.0F)
            .requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties QUINTUPLE_COMPRESSED_COBBLESTONE_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .sound(SoundType.STONE)
            .strength(10.0F, 48.0F)
            .requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties SEXTUPLE_COMPRESSED_COBBLESTONE_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .sound(SoundType.STONE)
            .strength(15.0F, 72.0F)
            .requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties SEPTUPLE_COMPRESSED_COBBLESTONE_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .sound(SoundType.STONE)
            .strength(20.0F, 96.0F)
            .requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties OCTUPLE_COMPRESSED_COBBLESTONE_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .sound(SoundType.STONE)
            .strength(30.0F, 1200.0F)
            .requiresCorrectToolForDrops();

    /** 压缩下界岩 */
    private static final BlockBehaviour.Properties COMPRESSED_NETHERRACK_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.NETHER)
            .sound(SoundType.NETHERRACK)
            .strength(1.0F, 4.0F)
            .requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties DOUBLE_COMPRESSED_NETHERRACK_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.NETHER)
            .sound(SoundType.NETHERRACK)
            .strength(2.0F, 8.0F)
            .requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties TRIPLE_COMPRESSED_NETHERRACK_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.NETHER)
            .sound(SoundType.NETHERRACK)
            .strength(3.5F, 16.0F)
            .requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties QUADRUPLE_COMPRESSED_NETHERRACK_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.NETHER)
            .sound(SoundType.NETHERRACK)
            .strength(5.0F, 24.0F)
            .requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties QUINTUPLE_COMPRESSED_NETHERRACK_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.NETHER)
            .sound(SoundType.NETHERRACK)
            .strength(7.0F, 32.0F)
            .requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties SEXTUPLE_COMPRESSED_NETHERRACK_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.NETHER)
            .sound(SoundType.NETHERRACK)
            .strength(10.0F, 48.0F)
            .requiresCorrectToolForDrops();

    /** 压缩沙 */
    private static final BlockBehaviour.Properties COMPRESSED_SAND_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.SAND)
            .sound(SoundType.SAND)
            .strength(1.0F, 2.0F);
    private static final BlockBehaviour.Properties DOUBLE_COMPRESSED_SAND_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.SAND)
            .sound(SoundType.SAND)
            .strength(2.0F, 4.0F);

    /** 压缩沙砾 */
    private static final BlockBehaviour.Properties COMPRESSED_GRAVEL_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .sound(SoundType.GRAVEL)
            .strength(1.0F, 2.0F);
    private static final BlockBehaviour.Properties DOUBLE_COMPRESSED_GRAVEL_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .sound(SoundType.GRAVEL)
            .strength(2.0F, 4.0F);

    /** 压缩泥土 */
    private static final BlockBehaviour.Properties COMPRESSED_DIRT_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.DIRT)
            .sound(SoundType.GRAVEL)
            .strength(1.0F, 2.0F);
    private static final BlockBehaviour.Properties DOUBLE_COMPRESSED_DIRT_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.DIRT)
            .sound(SoundType.GRAVEL)
            .strength(2.0F, 4.0F);
    private static final BlockBehaviour.Properties TRIPLE_COMPRESSED_DIRT_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.DIRT)
            .sound(SoundType.GRAVEL)
            .strength(3.0F, 6.0F);
    private static final BlockBehaviour.Properties QUADRUPLE_COMPRESSED_DIRT_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.DIRT)
            .sound(SoundType.GRAVEL)
            .strength(4.5F, 9.0F);

    /** 装饰石头 */
    private static final BlockBehaviour.Properties STONEBURNT_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .sound(SoundType.STONE)
            .strength(1.5F, 6.0F)
            .requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties POLISHED_STONE_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .sound(SoundType.STONE)
            .strength(2.0F, 6.0F)
            .requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties CROSSED_STONE_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .sound(SoundType.STONE)
            .strength(2.0F, 6.0F)
            .requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties TRUCHET_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .sound(SoundType.STONE)
            .strength(2.0F, 6.0F)
            .requiresCorrectToolForDrops();
    private static final BlockBehaviour.Properties BORDER_STONE_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .sound(SoundType.STONE)
            .strength(3.0F, 12.0F)
            .requiresCorrectToolForDrops();

    /** 基岩类（可挖掘，但非常坚硬） */
    private static final BlockBehaviour.Properties BEDROCK_LIKE_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .sound(SoundType.DEEPSLATE)
            .strength(30.0F, 1200.0F)
            .requiresCorrectToolForDrops();

    /** 机器外壳 */
    private static final BlockBehaviour.Properties MACHINE_BLOCK_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .sound(SoundType.METAL)
            .strength(5.0F, 6.0F)
            .requiresCorrectToolForDrops();

    /** 基础机器（磨坊、面板、谐振器） */
    private static final BlockBehaviour.Properties BASIC_MACHINE_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .sound(SoundType.METAL)
            .strength(3.5F, 6.0F)
            .requiresCorrectToolForDrops();

    /** 高级机器（高级发电机、自动执行器） */
    private static final BlockBehaviour.Properties ADVANCED_MACHINE_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .sound(SoundType.METAL)
            .strength(5.0F, 12.0F)
            .requiresCorrectToolForDrops();

    /** 石制鼓 */
    private static final BlockBehaviour.Properties STONE_DRUM_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .sound(SoundType.STONE)
            .strength(3.5F, 6.0F)
            .requiresCorrectToolForDrops();

    /** 铁制鼓 */
    private static final BlockBehaviour.Properties IRON_DRUM_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .sound(SoundType.METAL)
            .strength(5.0F, 6.0F)
            .requiresCorrectToolForDrops();

    /** 强化大型鼓 */
    private static final BlockBehaviour.Properties REINFORCED_DRUM_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .sound(SoundType.METAL)
            .strength(7.0F, 12.0F)
            .requiresCorrectToolForDrops();

    /** 恶魔巨型鼓 */
    private static final BlockBehaviour.Properties DEMONIC_DRUM_PROPERTIES = BlockBehaviour.Properties.of()
            .mapColor(MapColor.NETHER)
            .sound(SoundType.METAL)
            .strength(20.0F, 60.0F)
            .requiresCorrectToolForDrops();

    // ===================== 方块注册 =====================

    public static final RegistryObject<Block> MAGICAL_WOOD = registerBlock("magical_wood",
            () -> new Block(MAGICAL_WOOD_PROPERTIES));

    public static final RegistryObject<Block> COMPRESSED_COBBLESTONE = registerBlock("compressed_cobblestone",
            () -> new Block(COMPRESSED_COBBLESTONE_PROPERTIES));

    public static final RegistryObject<Block> DOUBLE_COMPRESSED_COBBLESTONE = registerBlock("double_compressed_cobblestone",
            () -> new Block(DOUBLE_COMPRESSED_COBBLESTONE_PROPERTIES));

    public static final RegistryObject<Block> TRIPLE_COMPRESSED_COBBLESTONE = registerBlock("triple_compressed_cobblestone",
            () -> new Block(TRIPLE_COMPRESSED_COBBLESTONE_PROPERTIES));

    public static final RegistryObject<Block> QUADRUPLE_COMPRESSED_COBBLESTONE = registerBlock("quadruple_compressed_cobblestone",
            () -> new Block(QUADRUPLE_COMPRESSED_COBBLESTONE_PROPERTIES));

    public static final RegistryObject<Block> QUINTUPLE_COMPRESSED_COBBLESTONE = registerBlock("quintuple_compressed_cobblestone",
            () -> new Block(QUINTUPLE_COMPRESSED_COBBLESTONE_PROPERTIES));

    public static final RegistryObject<Block> SEXTUPLE_COMPRESSED_COBBLESTONE = registerBlock("sextuple_compressed_cobblestone",
            () -> new Block(SEXTUPLE_COMPRESSED_COBBLESTONE_PROPERTIES));

    public static final RegistryObject<Block> SEPTUPLE_COMPRESSED_COBBLESTONE = registerBlock("septuple_compressed_cobblestone",
            () -> new Block(SEPTUPLE_COMPRESSED_COBBLESTONE_PROPERTIES));

    public static final RegistryObject<Block> OCTUPLE_COMPRESSED_COBBLESTONE = registerBlock("octuple_compressed_cobblestone",
            () -> new Block(OCTUPLE_COMPRESSED_COBBLESTONE_PROPERTIES));

    public static final RegistryObject<Block> COMPRESSED_NETHERRACK = registerBlock("compressed_netherrack",
            () -> new Block(COMPRESSED_NETHERRACK_PROPERTIES));

    public static final RegistryObject<Block> DOUBLE_COMPRESSED_NETHERRACK = registerBlock("double_compressed_netherrack",
            () -> new Block(DOUBLE_COMPRESSED_NETHERRACK_PROPERTIES));

    public static final RegistryObject<Block> TRIPLE_COMPRESSED_NETHERRACK = registerBlock("triple_compressed_netherrack",
            () -> new Block(TRIPLE_COMPRESSED_NETHERRACK_PROPERTIES));

    public static final RegistryObject<Block> QUADRUPLE_COMPRESSED_NETHERRACK = registerBlock("quadruple_compressed_netherrack",
            () -> new Block(QUADRUPLE_COMPRESSED_NETHERRACK_PROPERTIES));

    public static final RegistryObject<Block> QUINTUPLE_COMPRESSED_NETHERRACK = registerBlock("quintuple_compressed_netherrack",
            () -> new Block(QUINTUPLE_COMPRESSED_NETHERRACK_PROPERTIES));

    public static final RegistryObject<Block> SEXTUPLE_COMPRESSED_NETHERRACK = registerBlock("sextuple_compressed_netherrack",
            () -> new Block(SEXTUPLE_COMPRESSED_NETHERRACK_PROPERTIES));

    public static final RegistryObject<Block> COMPRESSED_SAND = registerBlock("compressed_sand",
            () -> new Block(COMPRESSED_SAND_PROPERTIES));

    public static final RegistryObject<Block> DOUBLE_COMPRESSED_SAND = registerBlock("double_compressed_sand",
            () -> new Block(DOUBLE_COMPRESSED_SAND_PROPERTIES));

    public static final RegistryObject<Block> COMPRESSED_GRAVEL = registerBlock("compressed_gravel",
            () -> new Block(COMPRESSED_GRAVEL_PROPERTIES));

    public static final RegistryObject<Block> DOUBLE_COMPRESSED_GRAVEL = registerBlock("double_compressed_gravel",
            () -> new Block(DOUBLE_COMPRESSED_GRAVEL_PROPERTIES));

    public static final RegistryObject<Block> COMPRESSED_DIRT = registerBlock("compressed_dirt",
            () -> new Block(COMPRESSED_DIRT_PROPERTIES));

    public static final RegistryObject<Block> DOUBLE_COMPRESSED_DIRT = registerBlock("double_compressed_dirt",
            () -> new Block(DOUBLE_COMPRESSED_DIRT_PROPERTIES));

    public static final RegistryObject<Block> TRIPLE_COMPRESSED_DIRT = registerBlock("triple_compressed_dirt",
            () -> new Block(TRIPLE_COMPRESSED_DIRT_PROPERTIES));

    public static final RegistryObject<Block> QUADRUPLE_COMPRESSED_DIRT = registerBlock("quadruple_compressed_dirt",
            () -> new Block(QUADRUPLE_COMPRESSED_DIRT_PROPERTIES));

    public static final RegistryObject<Block> STONEBURNT = registerBlock("stoneburnt",
            () -> new Block(STONEBURNT_PROPERTIES));

    public static final RegistryObject<Block> POLISHED_STONE = registerBlock("polished_stone",
            () -> new Block(POLISHED_STONE_PROPERTIES));

    public static final RegistryObject<Block> CROSSED_STONE = registerBlock("crossed_stone",
            () -> new Block(CROSSED_STONE_PROPERTIES));

    public static final RegistryObject<Block> TRUCHET = registerBlock("truchet",
            () -> new Block(TRUCHET_PROPERTIES));

    public static final RegistryObject<Block> BORDER_STONE = registerBlock("border_stone",
            () -> new Block(BORDER_STONE_PROPERTIES));

    public static final RegistryObject<Block> BEDROCK_COBBLESTONE = registerBlock("bedrock_cobblestone",
            () -> new Block(BEDROCK_LIKE_PROPERTIES));

    public static final RegistryObject<Block> BEDROCK_SLABS = registerBlock("bedrock_slabs",
            () -> new Block(BEDROCK_LIKE_PROPERTIES));

    public static final RegistryObject<Block> BEDROCK_BRICKS = registerBlock("bedrock_bricks",
            () -> new Block(BEDROCK_LIKE_PROPERTIES));

    public static final RegistryObject<Block> MACHINE_BLOCK = registerBlock("machine_block",
            () -> new Block(MACHINE_BLOCK_PROPERTIES));

    public static final RegistryObject<Block> WATER_MILL = registerBlock("water_mill",
            () -> new WaterMill(BASIC_MACHINE_PROPERTIES));

    public static final RegistryObject<Block> RESONATOR = registerBlock("resonator",
            () -> new Resonator(BASIC_MACHINE_PROPERTIES));

    public static final RegistryObject<Block> FIRE_MILL = registerBlock("fire_mill",
            () -> new FireMill(BASIC_MACHINE_PROPERTIES));

    public static final RegistryObject<Block> DRAGON_EGG_MILL = registerBlock("dragon_egg_mill",
            () -> new DragonEggMill(ADVANCED_MACHINE_PROPERTIES));

    public static final RegistryObject<Block> SOLAR_PANEL = registerBlock("solar_panel",
            () -> new SolarPanel(BASIC_MACHINE_PROPERTIES));

    public static final RegistryObject<Block> LUNAR_PANEL = registerBlock("lunar_panel",
            () -> new LunarPanel(BASIC_MACHINE_PROPERTIES));

    public static final RegistryObject<Block> LAVA_MILL = registerBlock("lava_mill",
            () -> new LavaMill(BASIC_MACHINE_PROPERTIES));

    public static final RegistryObject<Block> FURNACE_GENERATOR = registerBlock("furnace_generator",
            () -> new FurnaceGenerator(BASIC_MACHINE_PROPERTIES));

    public static final RegistryObject<Block> SURVIVAL_GENERATOR = registerBlock("survival_generator",
            () -> new SurvivalGenerator(BASIC_MACHINE_PROPERTIES));

    public static final RegistryObject<Block> OVERCLOCKED_GENERATOR = registerBlock("overclocked_generator",
            () -> new OverclockedGenerator(ADVANCED_MACHINE_PROPERTIES));

    public static final RegistryObject<Block> DEATH_GENERATOR = registerBlock("death_generator",
            () -> new DeathGenerator(ADVANCED_MACHINE_PROPERTIES));

    public static final RegistryObject<Block> EXPLOSIVE_GENERATOR = registerBlock("explosive_generator",
            () -> new ExplosiveGenerator(ADVANCED_MACHINE_PROPERTIES));

    public static final RegistryObject<Block> ENDER_GENERATOR = registerBlock("ender_generator",
            () -> new EnderGenerator(ADVANCED_MACHINE_PROPERTIES));

    public static final RegistryObject<Block> HALITOSIS_GENERATOR = registerBlock("halitosis_generator",
            () -> new HalitosisGenerator(ADVANCED_MACHINE_PROPERTIES));

    public static final RegistryObject<Block> NETHERSTAR_GENERATOR = registerBlock("netherstar_generator",
            () -> new NetherstarGenerator(ADVANCED_MACHINE_PROPERTIES));

    public static final RegistryObject<Block> FROSTY_GENERATOR = registerBlock("frosty_generator",
            () -> new FrostyGenerator(ADVANCED_MACHINE_PROPERTIES));

    public static final RegistryObject<Block> PINK_GENERATOR = registerBlock("pink_generator",
            () -> new PinkGenerator(ADVANCED_MACHINE_PROPERTIES));

    public static final RegistryObject<Block> AUTO_EXECUTOR = registerBlock("auto_executor",
            () -> new AutoExecutor(ADVANCED_MACHINE_PROPERTIES));

    public static final RegistryObject<Block> STONE_DRUM = registerBlock("stone_drum",
            () -> new StoneDrum(STONE_DRUM_PROPERTIES));

    public static final RegistryObject<Block> IRON_DRUM = registerBlock("iron_drum",
            () -> new IronDrum(IRON_DRUM_PROPERTIES));

    public static final RegistryObject<Block> REINFORCED_LARGE_DRUM = registerBlock("reinforced_large_drum",
            () -> new ReinforcedLargeDrum(REINFORCED_DRUM_PROPERTIES));

    public static final RegistryObject<Block> DEMONICALLY_GARGANTUAN_DRUM = registerBlock("demonically_gargantuan_drum",
            () -> new DemonicallyGargantuanDrum(DEMONIC_DRUM_PROPERTIES));

    public static RegistryObject<Block> registerSimpleBlock(String name, BlockBehaviour.Properties properties) {
        RegistryObject<Block> block = BLOCKS.register(name, () -> new Block(properties));
        ExURAItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    public static RegistryObject<Block> registerSimpleBlock(String name, BlockBehaviour.Properties properties, Item.Properties itemProperties) {
        RegistryObject<Block> block = BLOCKS.register(name, () -> new Block(properties));
        ExURAItems.ITEMS.register(name, () -> new BlockItem(block.get(), itemProperties));
        return block;
    }

    public static RegistryObject<Block> registerBlock(String name, Supplier<Block> blockSupplier) {
        RegistryObject<Block> block = BLOCKS.register(name, blockSupplier);
        ExURAItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }
}
