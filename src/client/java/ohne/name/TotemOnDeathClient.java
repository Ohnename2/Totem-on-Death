package ohne.name;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screens.MenuScreens;
import ohne.name.GUI.CustomChestGui;
import ohne.name.GUI.TotemCustomMenuType;
import ohne.name.networking.Status;
import java.util.List;
import java.util.UUID;

public class TotemOnDeathClient implements ClientModInitializer {
	public static List<UUID> DeadPlayers =  null;
	public static UUID LocalPlayerUuid = null;
	public static boolean IsDead = false;
	public static boolean souldShowDeathUI = false;
	@Override
	public void onInitializeClient() {
		MenuScreens.register(TotemCustomMenuType.CUSTOM_TOTEM_CHEST, CustomChestGui::new);


		BlockRenderLayerMap.putBlock(TotemBlocks.RESPAWNER, ChunkSectionLayer.TRANSLUCENT);

		MenuScreens.register(TotemCustomMenuType.CUSTOM_TOTEM_CHEST, CustomChestGui::new);


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