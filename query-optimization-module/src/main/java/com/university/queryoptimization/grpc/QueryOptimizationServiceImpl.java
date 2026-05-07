package com.university.queryoptimization.grpc;

import com.university.grpc.AutoCompleteRequest;
import com.university.grpc.AutoCompleteResponse;
import com.university.grpc.ExpansionRequest;
import com.university.grpc.ExpansionResponse;
import com.university.grpc.QueryOptimizationServiceGrpc;
import com.university.grpc.SpellingRequest;
import com.university.grpc.SpellingResponse;
import com.university.grpc.SuggestionRequest;
import com.university.grpc.SuggestionResponse;
import com.university.queryoptimization.service.QueryOptimizationService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import java.util.List;

@GrpcService
public class QueryOptimizationServiceImpl extends QueryOptimizationServiceGrpc.QueryOptimizationServiceImplBase {
    
    private final QueryOptimizationService service;

    public QueryOptimizationServiceImpl(QueryOptimizationService service) {
        this.service = service;
    }

    @Override
    public void suggestQuery(SuggestionRequest request, StreamObserver<SuggestionResponse> responseObserver) {
        try {
            List<String> suggestions = service.getSuggestions(request.getPartialQuery());
            
            SuggestionResponse response = SuggestionResponse.newBuilder()
                    .addAllSuggestions(suggestions)
                    .build();

            responseObserver.onNext(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        responseObserver.onCompleted();
    }

    @Override
    public void autoComplete(AutoCompleteRequest request, StreamObserver<AutoCompleteResponse> responseObserver) {
        responseObserver.onNext(AutoCompleteResponse.newBuilder()
                .addAllCompletions(service.autoComplete(request.getPrefix()))
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void correctSpelling(SpellingRequest request, StreamObserver<SpellingResponse> responseObserver) {
        responseObserver.onNext(SpellingResponse.newBuilder()
                .setCorrectedQuery(service.correctSpelling(request.getWrongQuery()))
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void expandQuery(ExpansionRequest request, StreamObserver<ExpansionResponse> responseObserver) {
        responseObserver.onNext(ExpansionResponse.newBuilder()
                .addAllExpandedQueries(service.expandQuery(request.getBaseQuery()))
                .build());
        responseObserver.onCompleted();
    }
}
