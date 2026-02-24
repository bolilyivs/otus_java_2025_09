package ru.otus.protobuf;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerMain {

    private static final int SERVER_PORT = 8190;
    private static final int SLEEP_MS = 2000;
    private static final int START_ITERATE = 1;
    private static final int START_INCREMENT = 1;

    @SneakyThrows
    public static void main(String[] args) {
        Server server = ServerBuilder.forPort(SERVER_PORT)
                .addService(new RemoteServiceImpl())
                .build();
        server.start();
        log.info("Server started, listening on {}", SERVER_PORT);
        server.awaitTermination();
    }

    private static class RemoteServiceImpl extends RemoteMessageServiceGrpc.RemoteMessageServiceImplBase {
        @Override
        @SneakyThrows
        public void send(
                RemoteMessageServiceOuterClass.MessageRequest request,
                StreamObserver<RemoteMessageServiceOuterClass.MessageResponse> responseObserver) {
            Stream.iterate(START_ITERATE, i -> i + START_INCREMENT)
                    .limit(request.getLastValue())
                    .forEach(value -> sendResponse(request.getFirstValue() + value, responseObserver));
            responseObserver.onCompleted();
        }

        @SneakyThrows
        private void sendResponse(
                long value, StreamObserver<RemoteMessageServiceOuterClass.MessageResponse> responseObserver) {
            responseObserver.onNext(createResposne(value));
            log.info("Server send {}", value);
            Thread.sleep(SLEEP_MS);
        }

        private RemoteMessageServiceOuterClass.MessageResponse createResposne(long value) {
            return RemoteMessageServiceOuterClass.MessageResponse.newBuilder()
                    .setValue(value)
                    .build();
        }
    }
}
