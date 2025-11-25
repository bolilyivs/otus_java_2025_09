package ru.otus.listener.homework;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

public class HistoryListener implements Listener, HistoryReader {

    Map<Long, Message> store = new ConcurrentHashMap<>();

    @Override
    public void onUpdated(Message msg) {
        store.put(msg.getId(), msg.toBuilder().field13(copy(msg.getField13())).build());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.of(store.getOrDefault(id, null));
    }

    private ObjectForMessage copy(ObjectForMessage field) {
        ObjectForMessage newField = new ObjectForMessage();
        newField.setData(new ArrayList<>(field.getData()));
        return newField;
    }
}
