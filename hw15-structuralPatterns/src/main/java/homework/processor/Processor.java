package homework.processor;

import homework.model.Message;

@SuppressWarnings("java:S1135")
public interface Processor {

    Message process(Message message);
}
