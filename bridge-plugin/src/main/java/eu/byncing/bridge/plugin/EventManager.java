package eu.byncing.bridge.plugin;

import eu.byncing.bridge.driver.BridgeDriver;
import eu.byncing.bridge.driver.IBridgeDriver;
import eu.byncing.bridge.driver.event.BridgeHandler;
import eu.byncing.bridge.driver.event.IBridgeListener;
import eu.byncing.bridge.driver.event.IEvent;
import eu.byncing.bridge.driver.event.IEventManager;
import eu.byncing.bridge.driver.service.IBridgeService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventManager implements IEventManager {

    private final List<IBridgeListener> listeners = new ArrayList<>();

    private IBridgeService service;

    public void setService(IBridgeService service) {
        this.service = service;
    }

    @Override
    public void register(IBridgeListener... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));
    }

    @Override
    public void unregister(IBridgeListener... listeners) {
        this.listeners.removeAll(Arrays.asList(listeners));
    }

    @Override
    public void call(IBridgeListener listener, IEvent event) {
        try {
            IBridgeDriver instance = BridgeDriver.getInstance();
            for (Method method : listener.getClass().getMethods()) {
                BridgeHandler handler = method.getAnnotation(BridgeHandler.class);
                if (handler == null) return;
                Parameter[] parameters = method.getParameters();
                if (Arrays.stream(parameters).anyMatch(parameter -> parameter.getType().equals(event.getClass()))) {
                    if (handler.internal()) {
                        if (service == null) {
                            method.invoke(listener, event);
                            return;
                        }
                        if (service.equals(instance.getInternalService())) method.invoke(listener, event);
                        return;
                    }
                    method.invoke(listener, event);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void call(IEvent event) {
        listeners.forEach(listener -> call(listener, event));
    }

    @Override
    public List<IBridgeListener> getListeners() {
        return listeners;
    }
}