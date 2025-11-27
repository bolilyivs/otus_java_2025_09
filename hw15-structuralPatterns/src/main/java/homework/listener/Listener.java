package homework.listener;

import homework.model.Message;

@SuppressWarnings("java:S1135")
public interface Listener {
    void onUpdated(Message msg);
}
