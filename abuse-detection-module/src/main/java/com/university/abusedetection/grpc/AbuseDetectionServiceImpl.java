package com.university.abusedetection.grpc;

import com.university.abusedetection.service.AbuseDetectionService;
import com.university.grpc.AbuseCheckRequest;
import com.university.grpc.AbuseCheckResponse;
import com.university.grpc.AbuseDetectionServiceGrpc;
import com.university.grpc.FlagRequest;
import com.university.grpc.PatternRequest;
import com.university.grpc.PatternResponse;
import com.university.grpc.ReportRequest;
import com.university.grpc.ReportResponse;
import com.university.grpc.StatusResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class AbuseDetectionServiceImpl extends AbuseDetectionServiceGrpc.AbuseDetectionServiceImplBase {
    
    private final AbuseDetectionService service;

    public AbuseDetectionServiceImpl(AbuseDetectionService service) {
        this.service = service;
    }

    @Override
    public void detectSearchAbuse(AbuseCheckRequest request, StreamObserver<AbuseCheckResponse> responseObserver) {
        responseObserver.onNext(AbuseCheckResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void analyzeQueryPatterns(PatternRequest request, StreamObserver<PatternResponse> responseObserver) {
        responseObserver.onNext(PatternResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void flagSuspiciousUsers(FlagRequest request, StreamObserver<StatusResponse> responseObserver) {
        responseObserver.onNext(StatusResponse.newBuilder().setSuccess(true).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getFraudReports(ReportRequest request, StreamObserver<ReportResponse> responseObserver) {
        responseObserver.onNext(ReportResponse.newBuilder().build());
        responseObserver.onCompleted();
    }
}
