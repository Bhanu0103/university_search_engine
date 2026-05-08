package com.university.ingest.grpc;

import com.university.grpc.DeleteRequest;
import com.university.grpc.IngestRequest;
import com.university.grpc.IngestServiceGrpc;
import com.university.grpc.StatusResponse;
import com.university.grpc.UpdateRequest;
import com.university.ingest.dto.IngestRequestRecord;
import com.university.ingest.service.IngestService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class IngestServiceImpl extends IngestServiceGrpc.IngestServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(IngestServiceImpl.class);

    private final IngestService service;

    public IngestServiceImpl(IngestService service) {
        this.service = service;
    }

    @Override
    public void ingestDocuments(IngestRequest request, StreamObserver<StatusResponse> responseObserver) {
        try {
            logger.debug("Ingesting document title={} university={}", request.getTitle(), request.getUniversity());

            IngestRequestRecord record = new IngestRequestRecord(
                    request.getTitle(),
                    request.getUniversity(),
                    request.getContent(),
                    request.getLocation(),
                    request.getRanking(),
                    request.getDepartment()
            );
            
            String msg = service.ingest(record);
            
            responseObserver.onNext(StatusResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage(msg)
                    .build());
        } catch (Exception e) {
            logger.warn("Document ingestion failed", e);
            responseObserver.onNext(StatusResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Ingestion failed: " + e.getMessage())
                    .build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void indexCourses(IngestRequest request, StreamObserver<StatusResponse> responseObserver) {
        handleIngest(request, responseObserver, record -> service.indexCourses(record));
    }

    @Override
    public void indexFacultyProfiles(IngestRequest request, StreamObserver<StatusResponse> responseObserver) {
        handleIngest(request, responseObserver, record -> service.indexFacultyProfiles(record));
    }

    @Override
    public void indexResearchPapers(IngestRequest request, StreamObserver<StatusResponse> responseObserver) {
        handleIngest(request, responseObserver, record -> service.indexResearchPapers(record));
    }

    @Override
    public void updateIndex(UpdateRequest request, StreamObserver<StatusResponse> responseObserver) {
        try {
            responseObserver.onNext(StatusResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage(service.updateIndex(request.getId(), request.getUpdatedJson()))
                    .build());
        } catch (Exception e) {
            logger.warn("Index update failed for id {}", request.getId(), e);
            responseObserver.onNext(StatusResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Index update failed: " + e.getMessage())
                    .build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void deleteFromIndex(DeleteRequest request, StreamObserver<StatusResponse> responseObserver) {
        try {
            responseObserver.onNext(StatusResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage(service.deleteFromIndex(request.getId()))
                    .build());
        } catch (Exception e) {
            logger.warn("Index delete failed for id {}", request.getId(), e);
            responseObserver.onNext(StatusResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Index delete failed: " + e.getMessage())
                    .build());
        }
        responseObserver.onCompleted();
    }

    private void handleIngest(IngestRequest request, StreamObserver<StatusResponse> responseObserver, IngestOperation operation) {
        try {
            responseObserver.onNext(StatusResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage(operation.apply(toRecord(request)))
                    .build());
        } catch (Exception e) {
            logger.warn("Ingest operation failed for title={}", request.getTitle(), e);
            responseObserver.onNext(StatusResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Ingestion failed: " + e.getMessage())
                    .build());
        }
        responseObserver.onCompleted();
    }

    private IngestRequestRecord toRecord(IngestRequest request) {
        return new IngestRequestRecord(
                request.getTitle(),
                request.getUniversity(),
                request.getContent(),
                request.getLocation(),
                request.getRanking(),
                request.getDepartment()
        );
    }

    @FunctionalInterface
    private interface IngestOperation {
        String apply(IngestRequestRecord record);
    }
}
