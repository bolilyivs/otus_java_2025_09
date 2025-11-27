package homework.processor;

import homework.model.Message;
import homework.processor.provider.DateTimeProvider;

public class ProcessorExceptionThrower implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public ProcessorExceptionThrower(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (dateTimeProvider.getDate().getSecond() % 2 == 0) {
            throw new IllegalStateException("Чётная секунда!");
        }

        return message;
    }
}
