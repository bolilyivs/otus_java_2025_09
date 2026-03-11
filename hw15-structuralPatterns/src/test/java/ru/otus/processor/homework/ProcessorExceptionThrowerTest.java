package ru.otus.processor.homework;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.processor.provider.DateTimeProvider;

class ProcessorExceptionThrowerTest {

    private final Message testMsg = new Message.Builder(1L).field1("test").build();

    @Test
    void processEvenTest() {
        DateTimeProvider dateTimeProvider = Mockito.mock(DateTimeProvider.class);
        Mockito.when(dateTimeProvider.getDate()).thenReturn(LocalDateTime.now().withSecond(2));

        Processor processor = new ProcessorExceptionThrower(dateTimeProvider);
        Assertions.assertThrowsExactly(IllegalStateException.class, () -> processor.process(testMsg));
    }

    @Test
    void processOddTest() {
        DateTimeProvider dateTimeProvider = Mockito.mock(DateTimeProvider.class);
        Mockito.when(dateTimeProvider.getDate()).thenReturn(LocalDateTime.now().withSecond(1));

        Processor processor = new ProcessorExceptionThrower(dateTimeProvider);
        Assertions.assertDoesNotThrow(() -> processor.process(testMsg));
    }
}
