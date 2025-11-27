package homework.handler;

import homework.listener.Listener;
import homework.model.Message;

public interface Handler {
    Message handle(Message msg);

    void addListener(Listener listener);

    void removeListener(Listener listener);
}
