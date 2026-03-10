package ohne.name;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import java.util.function.Function;

public class TotemBlocks {
    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register((itemGroup) -> {
            itemGroup.accept(TotemBlocks.RESPAWNER.asItem());
            itemGroup.accept(TotemBlocks.CREATIVE_RESPAWNER.asItem());
        });
    }

    public static final Block CREATIVE_RESPAWNER = register(
            "creative_respawner",
            properties -> new TotemRespawner(properties, false),
            BlockBehaviour.Properties.of().sound(SoundType.CREAKING_HEART).friction(0.999F).jumpFactor(0F).mapColor(MapColor.SNOW).strength(-1.0F, 3600000.0F),
            true
    );

    public static final Block RESPAWNER = register(
            "respawner",
            properties -> new TotemRespawner(properties, true),
            BlockBehaviour.Properties.of().sound(SoundType.CREAKING_HEART).friction(0.999F).jumpFactor(0F).mapColor(MapColor.SNOW).strength(50f, 1200f).randomTicks(),
            true
    );

        private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
            // Create a registry key for the block
            ResourceKey<Block> blockKey = keyOfBlock(name);
            // Create the block instance
            Block block = blockFactory.apply(settings.setId(blockKey));

            // Sometimes, you may not want to register an item for the block.
            // Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`
            if (shouldRegisterItem) {
                // Items need to be registered with a different type of registry key, but the ID
                // can be the same.
                ResourceKey<Item> itemKey = keyOfItem(name);

                BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
                Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
            }

            return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
        }

        private static ResourceKey<Block> keyOfBlock(String name) {
            return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(TotemOnDeath.MOD_ID, name));
        }

        private static ResourceKey<Item> keyOfItem(String name) {
            return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(TotemOnDeath.MOD_ID, name));
        }
}
