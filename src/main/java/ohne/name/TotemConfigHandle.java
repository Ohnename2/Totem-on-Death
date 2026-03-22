package ohne.name;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class TotemConfigHandle {
    public static TotemConfigData CONFIG = new TotemConfigData();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("Totem-on-death.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void loadConfig() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                CONFIG = GSON.fromJson(reader, TotemConfigData.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            saveConfig();
        }
    }

    public static void saveConfig() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(CONFIG, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}