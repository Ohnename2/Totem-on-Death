package ohne.name;

import net.fabricmc.api.ModInitializer;
import ohne.name.commands.RespawnCommand;
import ohne.name.networking.InitializePackets;
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
		new InitializePackets();
		new TotemEventHandle();
		RespawnCommand.initialize();
		LOGGER.info("Totem-on-Death has been initialized");
	}
}