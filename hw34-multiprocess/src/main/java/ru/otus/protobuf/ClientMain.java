package ru.otus.protobuf;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

@Slf4j
public class ClientMain {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final int SLEEP_MS = 1000;

    private static final long FIRST_VALUE = 0L;
    private static final long LAST_VALUE = 30L;
    private static final int VALUE_INIT = 0;
    private static final int VALUE_INC = 1;
    private static final int LOOP_START_VALUE = 0;
    private static final int LOOP_END_VALUE = 50;
    private static final long SERVER_VALUE_RESET = 0L;
    private static final int COUNT_DOWN_LATCH_COUNT = 1;

    private static final AtomicLong serverValue = new AtomicLong(0);

    @SneakyThrows
    public static void main(String[] args) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger("io").setLevel(Level.OFF);

        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        var stub = RemoteMessageServiceGrpc.newStub(channel);
        var latch = new CountDownLatch(COUNT_DOWN_LATCH_COUNT);
        CompletableFuture.runAsync(() -> stub.send(createRequest(FIRST_VALUE, LAST_VALUE), new MessageObserver(latch)));

        long currentValue = VALUE_INIT;
        for (int i = LOOP_START_VALUE; i < LOOP_END_VALUE; i++) {
            currentValue += getServerValue() + VALUE_INC;
            log.info("Current value: {}", currentValue);
            Thread.sleep(SLEEP_MS);
        }

        latch.await();
        channel.shutdown();
    }

    private static RemoteMessageServiceOuterClass.MessageRequest createRequest(long fristValue, long lastValue) {
        return RemoteMessageServiceOuterClass.MessageRequest.newBuilder()
                .setFirstValue(fristValue)
                .setLastValue(lastValue)
                .build();
    }

    private static long getServerValue() {
        return serverValue.getAndSet(SERVER_VALUE_RESET);
    }

    @Slf4j
    @RequiredArgsConstructor
    private static class MessageObserver implements StreamObserver<RemoteMessageServiceOuterClass.MessageResponse> {

        private final CountDownLatch latch;

        @Override
        public void onNext(RemoteMessageServiceOuterClass.MessageResponse messageResponse) {
            long newValue = messageResponse.getValue();
            log.info("new value: {}", newValue);
            serverValue.set(newValue);
        }

        @Override
        public void onError(Throwable throwable) {
            log.error(throwable.getMessage());
        }

        @Override
        public void onCompleted() {
            log.info("Done!");
            latch.countDown();
        }
    }
}
