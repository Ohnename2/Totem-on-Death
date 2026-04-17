package ohne.name.GUI;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import ohne.name.TotemOnDeath;

public class TotemCustomMenuType {
    public static final ExtendedMenuType<CustomChest, Integer> CUSTOM_TOTEM_CHEST = new ExtendedMenuType<>(CustomChest::new, ByteBufCodecs.INT);
    public static final DataComponentType<Boolean> IS_FROM_CUSTOM_CHEST = DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build();

    public static void initialize() {
        Registry.register(BuiltInRegistries.MENU,  Identifier.fromNamespaceAndPath(TotemOnDeath.MOD_ID, "custom_chest"), CUSTOM_TOTEM_CHEST);
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(TotemOnDeath.MOD_ID, "is_from_custom_chest"), IS_FROM_CUSTOM_CHEST);
    }
}
