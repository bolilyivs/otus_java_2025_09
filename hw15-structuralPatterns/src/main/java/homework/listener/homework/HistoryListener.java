package homework.listener.homework;

import homework.listener.Listener;
import homework.model.Message;
import homework.model.ObjectForMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> store = new ConcurrentHashMap<>();

    @Override
    public void onUpdated(Message msg) {
        store.put(msg.getId(), msg.toBuilder().field13(copy(msg.getField13())).build());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.of(store.getOrDefault(id, null));
    }

    private ObjectForMessage copy(ObjectForMessage field) {
        List<String> data =
                Optional.ofNullable(field).map(ObjectForMessage::getData).orElse(new ArrayList<>());
        ObjectForMessage newField = new ObjectForMessage();
        newField.setData(new ArrayList<>(data));
        return newField;
    }
}
