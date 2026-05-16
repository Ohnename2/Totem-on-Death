package ohne.name.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class InitializePackets {
    public InitializePackets() {
        PayloadTypeRegistry.playS2C().register(Status.ID, Status.CODEC);
        PayloadTypeRegistry.playS2C().register(ItemRemoveAmount.ID, ItemRemoveAmount.CODEC);
        PayloadTypeRegistry.playC2S().register(TotemRespawnRequest.ID, TotemRespawnRequest.CODEC);
    }
}