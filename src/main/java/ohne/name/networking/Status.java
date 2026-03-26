package ohne.name.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import ohne.name.TotemOnDeath;

public record Status(boolean IsDead) implements CustomPacketPayload{
    public static final Identifier STATUS_UPDATE_ID = Identifier.fromNamespaceAndPath(TotemOnDeath.MOD_ID, "status_update");
    public static final CustomPacketPayload.Type<Status> ID = new CustomPacketPayload.Type<>(STATUS_UPDATE_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, Status> CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, Status::IsDead, Status::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
