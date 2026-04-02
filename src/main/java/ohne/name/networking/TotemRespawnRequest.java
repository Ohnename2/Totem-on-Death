package ohne.name.networking;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import ohne.name.TotemOnDeath;
import java.util.UUID;

public record TotemRespawnRequest(UUID uuid)implements CustomPacketPayload {
    public static final Identifier STATUS_UPDATE_ID = Identifier.fromNamespaceAndPath(TotemOnDeath.MOD_ID, "totem_respawn_request");
    public static final CustomPacketPayload.Type<TotemRespawnRequest> ID = new CustomPacketPayload.Type<>(STATUS_UPDATE_ID);
    public static final StreamCodec<FriendlyByteBuf, TotemRespawnRequest> CODEC = StreamCodec.composite(UUIDUtil.STREAM_CODEC, TotemRespawnRequest::uuid, TotemRespawnRequest::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
