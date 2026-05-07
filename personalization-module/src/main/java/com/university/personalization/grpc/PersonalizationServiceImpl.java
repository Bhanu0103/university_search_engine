package com.university.personalization.grpc;

import com.university.document.entity.DocumentEntity;
import com.university.grpc.DocumentResponse;
import com.university.grpc.PersonalizedResultRequest;
import com.university.grpc.RecommendationResponse;
import com.university.grpc.PersonalizationServiceGrpc;
import com.university.grpc.RecommendationRequest;
import com.university.grpc.StatusResponse;
import com.university.grpc.TrackEventRequest;
import com.university.grpc.UserProfileRequest;
import com.university.personalization.service.PersonalizationService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class PersonalizationServiceImpl extends PersonalizationServiceGrpc.PersonalizationServiceImplBase {
    
    private final PersonalizationService service;

    public PersonalizationServiceImpl(PersonalizationService service) {
        this.service = service;
    }

    @Override
    public void getPersonalizedResults(PersonalizedResultRequest request, StreamObserver<RecommendationResponse> responseObserver) {
        try {
            List<DocumentEntity> entities = service.getPersonalizedResults(request.getUserId(), request.getQuery());
            
            // 4. Map to response
            List<DocumentResponse> results = entities.stream()
                    .map(entity -> DocumentResponse.newBuilder()
                            .setId(entity.getId() != null ? entity.getId().toString() : "")
                            .setTitle(entity.getTitle() != null ? entity.getTitle() : "")
                            .setUniversity(entity.getUniversityName() != null ? entity.getUniversityName() : "")
                            .setContent(entity.getContent() != null ? entity.getContent() : "")
                            .setLocation(entity.getLocation() != null ? entity.getLocation() : "")
                            .setRanking(entity.getRanking() != null ? entity.getRanking() : 0.0)
                            .setDepartment(entity.getDepartment() != null ? entity.getDepartment() : "")
                            .build())
                    .collect(Collectors.toList());

            responseObserver.onNext(RecommendationResponse.newBuilder()
                    .addAllResults(results)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onNext(RecommendationResponse.newBuilder().build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void trackUserSearch(TrackEventRequest request, StreamObserver<StatusResponse> responseObserver) {
        sendStatus(responseObserver, () -> service.trackUserSearch(request.getUserId(), request.getEventJson()));
    }

    @Override
    public void buildUserProfile(UserProfileRequest request, StreamObserver<StatusResponse> responseObserver) {
        sendStatus(responseObserver, () -> service.buildUserProfile(request.getUserId()));
    }

    @Override
    public void recommendContent(RecommendationRequest request, StreamObserver<RecommendationResponse> responseObserver) {
        try {
            responseObserver.onNext(RecommendationResponse.newBuilder()
                    .addAllResults(toDocumentResponses(service.recommendContent(request.getUserId())))
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onNext(RecommendationResponse.newBuilder().build());
        }
        responseObserver.onCompleted();
    }

    private List<DocumentResponse> toDocumentResponses(List<DocumentEntity> entities) {
        return entities.stream()
                .map(entity -> DocumentResponse.newBuilder()
                        .setId(entity.getId() != null ? entity.getId().toString() : "")
                        .setTitle(entity.getTitle() != null ? entity.getTitle() : "")
                        .setUniversity(entity.getUniversityName() != null ? entity.getUniversityName() : "")
                        .setContent(entity.getContent() != null ? entity.getContent() : "")
                        .setLocation(entity.getLocation() != null ? entity.getLocation() : "")
                        .setRanking(entity.getRanking() != null ? entity.getRanking() : 0.0)
                        .setDepartment(entity.getDepartment() != null ? entity.getDepartment() : "")
                        .build())
                .collect(Collectors.toList());
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
