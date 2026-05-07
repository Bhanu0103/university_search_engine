package com.university.search.grpc;

import com.university.document.entity.DocumentEntity;
import com.university.grpc.AdvancedSearchRequest;
import com.university.grpc.DocumentResponse;
import com.university.grpc.FilterRequest;
import com.university.grpc.PaginationRequest;
import com.university.grpc.SearchRequest;
import com.university.grpc.SearchResponse;
import com.university.grpc.SearchDetailResponse;
import com.university.grpc.SearchIdRequest;
import com.university.grpc.SearchServiceGrpc;
import com.university.search.service.SearchService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class SearchServiceImpl extends SearchServiceGrpc.SearchServiceImplBase {
    
    private final SearchService service;

    public SearchServiceImpl(SearchService service) {
        this.service = service;
    }

    @Override
    public void executeSearch(SearchRequest request, StreamObserver<SearchResponse> responseObserver) {
        try {
            List<DocumentEntity> entities = service.search(request.getQuery());
            sendSearchResponse(entities, responseObserver);
        } catch (Exception e) {
            System.err.println("Search failed: " + e.getMessage());
            e.printStackTrace();
            responseObserver.onNext(SearchResponse.newBuilder().build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void advancedSearch(AdvancedSearchRequest request, StreamObserver<SearchResponse> responseObserver) {
        try {
            sendSearchResponse(service.advancedSearch(request.getQuery(), request.getFieldsList()), responseObserver);
        } catch (Exception e) {
            sendEmptySearchResponse(responseObserver, e);
        }
    }

    @Override
    public void filterSearchResults(FilterRequest request, StreamObserver<SearchResponse> responseObserver) {
        try {
            sendSearchResponse(
                    service.filterSearchResults(request.getQuery(), request.getDepartment(), request.getLocation()),
                    responseObserver);
        } catch (Exception e) {
            sendEmptySearchResponse(responseObserver, e);
        }
    }

    @Override
    public void paginateResults(PaginationRequest request, StreamObserver<SearchResponse> responseObserver) {
        try {
            sendSearchResponse(service.paginateResults(request.getQuery(), request.getPage(), request.getSize()), responseObserver);
        } catch (Exception e) {
            sendEmptySearchResponse(responseObserver, e);
        }
    }

    @Override
    public void getSearchById(SearchIdRequest request, StreamObserver<SearchDetailResponse> responseObserver) {
        try {
            SearchDetailResponse.Builder response = SearchDetailResponse.newBuilder();
            service.getSearchById(request.getId()).map(this::toDocumentResponse).ifPresent(response::setDocument);
            responseObserver.onNext(response.build());
        } catch (Exception e) {
            System.err.println("Get search by id failed: " + e.getMessage());
            responseObserver.onNext(SearchDetailResponse.newBuilder().build());
        }
        responseObserver.onCompleted();
    }

    private void sendSearchResponse(List<DocumentEntity> entities, StreamObserver<SearchResponse> responseObserver) {
        List<DocumentResponse> results = entities.stream()
                .map(this::toDocumentResponse)
                .collect(Collectors.toList());

        responseObserver.onNext(SearchResponse.newBuilder()
                .addAllResults(results)
                .setTotalHits(results.size())
                .build());
        responseObserver.onCompleted();
    }

    private void sendEmptySearchResponse(StreamObserver<SearchResponse> responseObserver, Exception e) {
        System.err.println("Search endpoint failed: " + e.getMessage());
        responseObserver.onNext(SearchResponse.newBuilder().build());
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
