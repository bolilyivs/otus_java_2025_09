package ru.otus.processor;

import java.time.LocalDateTime;
import ru.otus.model.Message;

public class ProcessorExceptionThrower implements Processor {

    @Override
    public Message process(Message message) {

        if (LocalDateTime.now().getSecond() % 2 == 0) {
            throw new IllegalStateException("Чётная секунда!");
        }

        return message;
    }
}
