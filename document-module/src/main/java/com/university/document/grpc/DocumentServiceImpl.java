package com.university.document.grpc;

import com.university.document.dto.DocRecord;
import com.university.document.service.DocumentService;
import com.university.grpc.ClassifyRequest;
import com.university.grpc.DocumentDataResponse;
import com.university.grpc.DocumentIdRequest;
import com.university.grpc.DocumentServiceGrpc;
import com.university.grpc.DocumentUploadRequest;
import com.university.grpc.MetadataUpdateRequest;
import com.university.grpc.StatusResponse;
import com.university.grpc.TagRequest;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class DocumentServiceImpl extends DocumentServiceGrpc.DocumentServiceImplBase {
    private final DocumentService service;

    public DocumentServiceImpl(DocumentService service) {
        this.service = service;
    }

    @Override
    public void uploadDocument(DocumentUploadRequest request, StreamObserver<StatusResponse> responseObserver) {
        sendStatus(responseObserver, () -> service.uploadDocument(request.getContent(), request.getMetadataJson()));
    }

    @Override
    public void updateMetadata(MetadataUpdateRequest request, StreamObserver<StatusResponse> responseObserver) {
        sendStatus(responseObserver, () -> service.updateMetadata(request.getDocumentId(), request.getMetadataJson()));
    }

    @Override
    public void tagDocument(TagRequest request, StreamObserver<StatusResponse> responseObserver) {
        sendStatus(responseObserver, () -> service.tagDocument(request.getDocumentId(), request.getTagsList()));
    }

    @Override
    public void classifyDocument(ClassifyRequest request, StreamObserver<StatusResponse> responseObserver) {
        sendStatus(responseObserver, () -> service.classifyDocument(request.getDocumentId()));
    }

    @Override
    public void getDocumentDetails(DocumentIdRequest request, StreamObserver<DocumentDataResponse> responseObserver) {
        try {
            Long docId = Long.valueOf(request.getDocumentId());
            DocRecord record = service.getDocumentDetails(docId);
            String json = "{\"id\":\"" + record.id() + "\",\"metadata\":\"" + record.metadata() + "\"}";
            responseObserver.onNext(DocumentDataResponse.newBuilder().setDocumentJson(json).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    private void sendStatus(StreamObserver<StatusResponse> responseObserver, StatusOperation operation) {
        try {
            responseObserver.onNext(StatusResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage(operation.run())
                    .build());
        } catch (Exception e) {
            responseObserver.onNext(StatusResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage(e.getMessage())
                    .build());
        }
        responseObserver.onCompleted();
    }

    @FunctionalInterface
    private interface StatusOperation {
        String run();
    }
}
