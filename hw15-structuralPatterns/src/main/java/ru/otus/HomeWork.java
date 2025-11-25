package ru.otus;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.*;

public class HomeWork {

    private static final Logger logger = LoggerFactory.getLogger(Demo.class);

    /*
    Реализовать to do:
      + 1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
      + 2. Сделать процессор, который поменяет местами значения field11 и field12
      +/? 3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяьться во время выполнения.
            Тест - важная часть задания
            Обязательно посмотрите пример к паттерну Мементо!
      + 4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
         Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
         Для него уже есть тест, убедитесь, что тест проходит
    */

    public static void main(String[] args) {
        var processors = List.of(
                new ProcessorConcatFields(),
                new LoggerProcessor(new ProcessorUpperField10()),
                new LoggerProcessor(new ProcessorReplacerFields()),
                new LoggerProcessor(new ProcessorExceptionThrower()));

        var complexProcessor = new ComplexProcessor(processors, ex -> {
            logger.error(ex.getLocalizedMessage());
        });
        var listenerPrinter = new ListenerPrinterConsole();
        var historyListener = new HistoryListener();
        complexProcessor.addListener(listenerPrinter);
        complexProcessor.addListener(historyListener);

        var field13 = new ObjectForMessage();
        field13.setData(List.of("13"));

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(field13)
                .build();

        var result = complexProcessor.handle(message);
        logger.info("result:{}", result);

        complexProcessor.removeListener(listenerPrinter);
    }
}
