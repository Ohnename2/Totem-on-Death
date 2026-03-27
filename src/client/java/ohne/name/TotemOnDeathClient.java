package ohne.name;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.entity.player.Player;
import ohne.name.networking.Status;

import java.util.List;
import java.util.UUID;

public class TotemOnDeathClient implements ClientModInitializer {
	public static List<UUID> DeadPlayers =  null;
	public static UUID LocalPlayerUuid = null;
	public static boolean IsDead = false;
	@Override
	public void onInitializeClient() {
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			LocalPlayerUuid = client.getGameProfile().id();
		});
		ClientPlayConnectionEvents.DISCONNECT.register((client, server) -> {
			DeadPlayers = null;
			setIsDead();
		});
		ClientPlayNetworking.registerGlobalReceiver(Status.ID, (payload, context) -> {
			DeadPlayers = payload.DeadPlayers();
			setIsDead();
		});
	}
	public static void setIsDead() {
		for(UUID uuid : DeadPlayers) {
			if(uuid.equals(LocalPlayerUuid)) {
				IsDead = true;
				return;
			}
			IsDead = false;
		}
	}
}