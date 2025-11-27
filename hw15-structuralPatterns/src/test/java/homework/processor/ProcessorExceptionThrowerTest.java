package homework.processor;

import homework.model.Message;
import homework.processor.provider.DateTimeProvider;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
