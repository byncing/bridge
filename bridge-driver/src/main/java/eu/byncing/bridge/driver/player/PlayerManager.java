package eu.byncing.bridge.driver.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerManager {

    private final List<IBridgePlayer> players = new ArrayList<>();

    public IBridgePlayer getPlayer(UUID uniqueId) {
        return players.stream().filter(player -> player.getUniqueId().equals(uniqueId)).findFirst().orElse(null);
    }

    public IBridgePlayer getPlayer(String name) {
        return players.stream().filter(player -> player.getName().equals(name)).findFirst().orElse(null);
    }

    public List<IBridgePlayer> getPlayers() {
        return players;
    }
}