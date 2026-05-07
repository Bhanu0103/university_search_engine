package com.university.filter.grpc;

import com.university.document.entity.DocumentEntity;
import com.university.filter.service.FilterService;
import com.university.grpc.CombineFilterRequest;
import com.university.grpc.DocumentResponse;
import com.university.grpc.FacetRequest;
import com.university.grpc.FacetResponse;
import com.university.grpc.FilterRequest;
import com.university.grpc.FilterResponse;
import com.university.grpc.FilterServiceGrpc;
import com.university.grpc.RefineRequest;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class FilterServiceImpl extends FilterServiceGrpc.FilterServiceImplBase {
    
    private final FilterService service;

    public FilterServiceImpl(FilterService service) {
        this.service = service;
    }

    @Override
    public void applyFilters(FilterRequest request, StreamObserver<FilterResponse> responseObserver) {
        try {
            sendFilterResponse(
                    service.applyFilters(request.getQuery(), request.getDepartment(), request.getLocation()),
                    responseObserver);
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onNext(FilterResponse.newBuilder().build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getFacetCounts(FacetRequest request, StreamObserver<FacetResponse> responseObserver) {
        try {
            responseObserver.onNext(FacetResponse.newBuilder()
                    .putAllCounts(service.getFacetCounts(request.getQuery(), request.getFacetFieldsList()))
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onNext(FacetResponse.newBuilder().build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void refineSearch(RefineRequest request, StreamObserver<FilterResponse> responseObserver) {
        try {
            sendFilterResponse(service.refineSearch(request.getQuery(), request.getRefineToken()), responseObserver);
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onNext(FilterResponse.newBuilder().build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void combineFilters(CombineFilterRequest request, StreamObserver<FilterResponse> responseObserver) {
        try {
            sendFilterResponse(service.combineFilters(request.getFilterIdsList()), responseObserver);
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onNext(FilterResponse.newBuilder().build());
            responseObserver.onCompleted();
        }
    }

    private void sendFilterResponse(List<DocumentEntity> entities, StreamObserver<FilterResponse> responseObserver) {
        List<DocumentResponse> results = entities.stream()
                .map(this::toDocumentResponse)
                .collect(Collectors.toList());

        responseObserver.onNext(FilterResponse.newBuilder()
                .addAllResults(results)
                .setTotalHits(results.size())
                .build());
        responseObserver.onCompleted();
    }

    private DocumentResponse toDocumentResponse(DocumentEntity entity) {
        return DocumentResponse.newBuilder()
                .setId(entity.getId() != null ? entity.getId().toString() : "")
                .setTitle(entity.getTitle() != null ? entity.getTitle() : "")
                .setUniversity(entity.getUniversityName() != null ? entity.getUniversityName() : "")
                .setContent(entity.getContent() != null ? entity.getContent() : "")
                .setLocation(entity.getLocation() != null ? entity.getLocation() : "")
                .setRanking(entity.getRanking() != null ? entity.getRanking() : 0.0)
                .setDepartment(entity.getDepartment() != null ? entity.getDepartment() : "")
                .build();
    }
}
