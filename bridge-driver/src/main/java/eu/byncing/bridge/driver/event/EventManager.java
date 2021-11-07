package eu.byncing.bridge.driver.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventManager {

    private final List<IBridgeListener> listeners = new ArrayList<>();

    public void register(IBridgeListener... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));
    }

    public void call(IBridgeListener listener, IEvent event) {
        try {
            for (Method method : listener.getClass().getMethods()) {
                BridgeHandler handler = method.getAnnotation(BridgeHandler.class);
                if (handler == null) return;
                Parameter[] parameters = method.getParameters();
                if (Arrays.stream(parameters).anyMatch(parameter -> parameter.getType().equals(event.getClass()))) {
                    method.invoke(listener, event);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void call(IEvent event) {
        listeners.forEach(listener -> call(listener, event));
    }

    public List<IBridgeListener> getListeners() {
        return listeners;
    }
}