package eu.byncing.bridge.driver.event.player;

import eu.byncing.bridge.driver.event.IEvent;
import eu.byncing.bridge.driver.player.IBridgePlayer;

public class PlayerTitleEvent implements IEvent {

    private final IBridgePlayer player;
    private final String title, subtitle;
    private final int fadeIn, stay, fadeOut;


    public PlayerTitleEvent(IBridgePlayer player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.player = player;
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public IBridgePlayer getPlayer() {
        return player;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }
}