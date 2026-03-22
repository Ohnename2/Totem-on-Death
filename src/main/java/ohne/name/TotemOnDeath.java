package ohne.name;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TotemOnDeath implements ModInitializer {
	public static final String MOD_ID = "totem-on-death";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		TotemConfigHandle.loadConfig();
		TotemBlocks.initialize();
		TotemPlayerHandle.initialize();
		new TotemEventHandle();
		LOGGER.info("Totem-on-Death has been initialized");
	}
}