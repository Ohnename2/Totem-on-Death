package ohne.name;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import ohne.name.GUI.CustomChestGui;
import ohne.name.GUI.TotemCustomMenuType;
import ohne.name.networking.ItemRemoveAmount;
import ohne.name.networking.Status;
import ohne.name.networking.TotemRespawnRequest;

import java.util.List;
import java.util.UUID;

public class TotemOnDeathClient implements ClientModInitializer {
	public static List<UUID> DeadPlayers =  null;
	public static UUID LocalPlayerUuid = null;
	public static boolean IsDead = false;
	public static boolean souldShowDeathUI = false;
	public static int ItemsToDestroy = -1;
	@Override
	public void onInitializeClient() {
		MenuScreens.register(TotemCustomMenuType.CUSTOM_TOTEM_CHEST, CustomChestGui::new);
		BlockRenderLayerMap.putBlock(TotemBlocks.RESPAWNER, ChunkSectionLayer.TRANSLUCENT);


		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			LocalPlayerUuid = client.getGameProfile().id();
		});
		ClientPlayConnectionEvents.DISCONNECT.register((client, server) -> {
			DeadPlayers = null;
			LocalPlayerUuid = null;
			setIsDead();
		});
		ClientPlayNetworking.registerGlobalReceiver(Status.ID, (payload, context) -> {
			souldShowDeathUI = payload.showDeathUI();
			DeadPlayers = payload.DeadPlayers();
			setIsDead();
		});
		ClientPlayNetworking.registerGlobalReceiver(ItemRemoveAmount.ID, (payload, context) -> {
			ItemsToDestroy = payload.amount();
		});
	}
	public static void setIsDead() {
		if(LocalPlayerUuid == null)	{
			IsDead = false;
			return;
		}
		for(UUID uuid : DeadPlayers) {
			if(uuid.equals(LocalPlayerUuid)) {
				IsDead = true;
				return;
			}
			IsDead = false;
		}
	}
}