package ru.otus.processor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.handler.ComplexProcessor;
import ru.otus.handler.Handler;
import ru.otus.model.Message;

class ProcessorExceptionThrowerTest {

    @Test
    void processTest() {
        List<Processor> processors =
                List.of(new ProcessorExceptionThrower(() -> LocalDateTime.now().withSecond(2)));
        List<Exception> exceptions = new ArrayList<>();
        Handler handler = new ComplexProcessor(processors, exceptions::add);
        handler.handle(new Message.Builder(1L).field1("test").build());

        Assertions.assertFalse(exceptions.isEmpty());
    }
}
