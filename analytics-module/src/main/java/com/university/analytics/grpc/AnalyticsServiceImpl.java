package com.university.analytics.grpc;

import com.university.analytics.service.AnalyticsService;
import com.university.grpc.AnalyticsRequest;
import com.university.grpc.AnalyticsResponse;
import com.university.grpc.AnalyticsServiceGrpc;
import com.university.grpc.EngagementRequest;
import com.university.grpc.EngagementResponse;
import com.university.grpc.PerformanceRequest;
import com.university.grpc.PerformanceResponse;
import com.university.grpc.PopularityRequest;
import com.university.grpc.PopularityResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@GrpcService
public class AnalyticsServiceImpl extends AnalyticsServiceGrpc.AnalyticsServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsServiceImpl.class);
    
    private final AnalyticsService service;

    public AnalyticsServiceImpl(AnalyticsService service) {
        this.service = service;
    }

    @Override
    public void getPopularQueries(PopularityRequest request, StreamObserver<PopularityResponse> responseObserver) {
        try {
            int limit = request.getTopN() > 0 ? request.getTopN() : 5;
            List<String> topQueries = service.getTopTrending(limit);
            
            PopularityResponse response = PopularityResponse.newBuilder()
                    .addAllQueries(topQueries)
                    .build();

            responseObserver.onNext(response);
        } catch (Exception e) {
            logger.warn("Get popular queries failed with topN={}", request.getTopN(), e);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getSearchAnalytics(AnalyticsRequest request, StreamObserver<AnalyticsResponse> responseObserver) {
        responseObserver.onNext(AnalyticsResponse.newBuilder()
                .setMetricsJson(service.getSearchAnalytics(request.getTimeRange()))
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getUserEngagement(EngagementRequest request, StreamObserver<EngagementResponse> responseObserver) {
        responseObserver.onNext(EngagementResponse.newBuilder()
                .setEngagementScore(service.getUserEngagement(request.getUserId()))
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getContentPerformance(PerformanceRequest request, StreamObserver<PerformanceResponse> responseObserver) {
        responseObserver.onNext(PerformanceResponse.newBuilder()
                .setPerformanceMetricsJson(service.getContentPerformance(request.getDocumentId()))
                .build());
        responseObserver.onCompleted();
    }
}
