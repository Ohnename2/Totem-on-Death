package ohne.name.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class InitializePackets {
    public InitializePackets() {
        PayloadTypeRegistry.clientboundPlay().register(Status.ID, Status.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(TotemRespawnRequest.ID, TotemRespawnRequest.CODEC);
    }
}