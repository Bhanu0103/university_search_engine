package com.university.ranking.grpc;
import com.university.ranking.service.RankingService;
import net.devh.boot.grpc.server.service.GrpcService;
@GrpcService
public class RankingServiceImpl extends com.university.grpc.RankingServiceGrpc.RankingServiceImplBase {
    private final RankingService service;
    public RankingServiceImpl(RankingService service) { this.service = service; }
        @Override
    public void calculateRelevanceScore(com.university.grpc.RankingRequest request, io.grpc.stub.StreamObserver<com.university.grpc.RankingResponse> responseObserver) {
        responseObserver.onNext(com.university.grpc.RankingResponse.newBuilder()
                .putAllScores(service.calculateScores(java.util.List.of(request.getDocumentId())))
                .build());
        responseObserver.onCompleted();
    }
    @Override
    public void boostResults(com.university.grpc.BoostRequest request, io.grpc.stub.StreamObserver<com.university.grpc.RankingResponse> responseObserver) {
        responseObserver.onNext(com.university.grpc.RankingResponse.newBuilder()
                .putAllScores(service.boostResults(request.getDocumentIdsList(), request.getBoostFactor()))
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void applyRankingModel(com.university.grpc.RankingModelRequest request, io.grpc.stub.StreamObserver<com.university.grpc.RankingResponse> responseObserver) {
        responseObserver.onNext(com.university.grpc.RankingResponse.newBuilder()
                .putAllScores(service.applyRankingModel(request.getModelName()))
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void personalizeRanking(com.university.grpc.PersonalizeRankingRequest request, io.grpc.stub.StreamObserver<com.university.grpc.RankingResponse> responseObserver) {
        responseObserver.onNext(com.university.grpc.RankingResponse.newBuilder()
                .putAllScores(service.personalizeRanking(request.getUserId(), request.getDocumentIdsList()))
                .build());
        responseObserver.onCompleted();
    }
}
