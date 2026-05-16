package ohne.name;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerItemRespawnCount {
    private static final Map<UUID, Integer> dataMap = new HashMap<>();

    public static void set(UUID uuid, int value) {
        dataMap.put(uuid, value);
    }

    public static int get(UUID uuid) {
        System.out.println(dataMap.getOrDefault(uuid, 0));
        return dataMap.getOrDefault(uuid, 0);
    }

    public static void remove(UUID uuid) {
        dataMap.remove(uuid);
    }
}
