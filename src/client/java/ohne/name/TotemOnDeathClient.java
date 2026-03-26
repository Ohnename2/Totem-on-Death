package ohne.name;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import ohne.name.networking.Status;

public class TotemOnDeathClient implements ClientModInitializer {
	public static boolean IsDead =  false;
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(Status.ID, (payload, context) -> {
			IsDead = payload.IsDead();
		});
		ClientPlayConnectionEvents.DISCONNECT.register((client, server) -> {
			IsDead = false;
		});
	}
}