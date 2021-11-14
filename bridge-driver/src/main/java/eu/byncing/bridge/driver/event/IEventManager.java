package eu.byncing.bridge.driver.event;

import java.util.List;

public interface IEventManager {

    void register(IBridgeListener... listeners);

    void unregister(IBridgeListener... listeners);

    void call(IBridgeListener listener, IEvent event);

    void call(IEvent event);

    List<IBridgeListener> getListeners();
}