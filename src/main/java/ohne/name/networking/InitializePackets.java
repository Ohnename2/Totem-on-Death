package ohne.name.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class InitializePackets {
    public InitializePackets() {
        PayloadTypeRegistry.playS2C().register(Status.ID, Status.CODEC);
        PayloadTypeRegistry.playC2S().register(TotemRespawnRequest.ID, TotemRespawnRequest.CODEC);
    }
}