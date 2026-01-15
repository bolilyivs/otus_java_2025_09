package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyCache<K, V> implements HwCache<K, V> {

    private static final Logger log = LoggerFactory.getLogger(MyCache.class);

    Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyAll(key, value, Action.PUT.name());
    }

    @Override
    public void remove(K key) {
        V value = cache.remove(key);
        notifyAll(key, value, Action.REMOVE.name());
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        notifyAll(key, value, Action.GET.name());
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyAll(K key, V value, String action) {
        listeners.forEach(listener -> {
            try {
                listener.notify(key, value, action);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        });
    }

    @RequiredArgsConstructor
    public enum Action {
        PUT,
        REMOVE,
        GET;
    }
}
