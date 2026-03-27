package ohne.name.networking;

import io.netty.buffer.ByteBuf;
import io.netty.util.collection.ByteObjectMap;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import ohne.name.TotemOnDeath;
import java.util.List;
import java.util.UUID;

public record Status(List<UUID> DeadPlayers) implements CustomPacketPayload {
    public static final Identifier STATUS_UPDATE_ID = Identifier.fromNamespaceAndPath(TotemOnDeath.MOD_ID, "status_update");
    public static final CustomPacketPayload.Type<Status> ID = new CustomPacketPayload.Type<>(STATUS_UPDATE_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, Status> CODEC = StreamCodec.composite(UUIDUtil.STREAM_CODEC.apply(ByteBufCodecs.list()), Status::DeadPlayers, Status::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
