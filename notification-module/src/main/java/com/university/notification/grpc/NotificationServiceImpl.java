package com.university.notification.grpc;

import com.university.grpc.AlertRequest;
import com.university.grpc.LogRequest;
import com.university.grpc.NewContentRequest;
import com.university.grpc.NotificationListRequest;
import com.university.grpc.NotificationListResponse;
import com.university.grpc.StatusResponse;
import com.university.notification.service.NotificationService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class NotificationServiceImpl extends com.university.grpc.NotificationServiceGrpc.NotificationServiceImplBase {
    private final NotificationService service;

    public NotificationServiceImpl(NotificationService service) {
        this.service = service;
    }

    @Override
    public void logSearchEvent(LogRequest request, StreamObserver<StatusResponse> responseObserver) {
        sendStatus(responseObserver, service.logSearchEvent(request.getUserId(), request.getQuery()));
    }

    @Override
    public void sendSearchAlert(AlertRequest request, StreamObserver<StatusResponse> responseObserver) {
        sendStatus(responseObserver, service.sendSearchAlert(request.getUserId(), request.getAlertMessage()));
    }

    @Override
    public void notifyNewContent(NewContentRequest request, StreamObserver<StatusResponse> responseObserver) {
        sendStatus(responseObserver, service.notifyNewContent(request.getDocumentId()));
    }

    @Override
    public void getNotifications(NotificationListRequest request, StreamObserver<NotificationListResponse> responseObserver) {
        responseObserver.onNext(NotificationListResponse.newBuilder()
                .addAllNotifications(service.getNotifications(request.getUserId()))
                .build());
        responseObserver.onCompleted();
    }

    private void sendStatus(StreamObserver<StatusResponse> responseObserver, String message) {
        responseObserver.onNext(StatusResponse.newBuilder()
                .setSuccess(true)
                .setMessage(message)
                .build());
        responseObserver.onCompleted();
    }
}
