package ohne.name.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import ohne.name.TotemOnDeath;

public record ItemRemoveAmount(int amount) implements CustomPacketPayload {
    public static final Identifier STATUS_UPDATE_ID = Identifier.fromNamespaceAndPath(TotemOnDeath.MOD_ID, "item_remove_amount");
    public static final Type<ItemRemoveAmount> ID = new Type<>(STATUS_UPDATE_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemRemoveAmount> CODEC = StreamCodec.composite(ByteBufCodecs.INT, ItemRemoveAmount::amount, ItemRemoveAmount::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
