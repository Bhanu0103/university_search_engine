$modules = @{
    "ingest" = @{
        Service = "IngestService"
        GrpcBase = "IngestServiceImplBase"
        Methods = @("public void ingestDocuments(com.university.grpc.IngestRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }",
                    "public void indexCourses(com.university.grpc.IngestRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }",
                    "public void indexFacultyProfiles(com.university.grpc.IngestRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }",
                    "public void indexResearchPapers(com.university.grpc.IngestRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }",
                    "public void updateIndex(com.university.grpc.UpdateRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }",
                    "public void deleteFromIndex(com.university.grpc.DeleteRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }")
    }
    "search" = @{
        Service = "SearchService"
        GrpcBase = "SearchServiceImplBase"
        Methods = @("public void executeSearch(com.university.grpc.SearchRequest request, io.grpc.stub.StreamObserver<com.university.grpc.SearchResponse> responseObserver) { responseObserver.onNext(com.university.grpc.SearchResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void advancedSearch(com.university.grpc.AdvancedSearchRequest request, io.grpc.stub.StreamObserver<com.university.grpc.SearchResponse> responseObserver) { responseObserver.onNext(com.university.grpc.SearchResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void filterSearchResults(com.university.grpc.FilterRequest request, io.grpc.stub.StreamObserver<com.university.grpc.SearchResponse> responseObserver) { responseObserver.onNext(com.university.grpc.SearchResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void paginateResults(com.university.grpc.PaginationRequest request, io.grpc.stub.StreamObserver<com.university.grpc.SearchResponse> responseObserver) { responseObserver.onNext(com.university.grpc.SearchResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void getSearchById(com.university.grpc.SearchIdRequest request, io.grpc.stub.StreamObserver<com.university.grpc.SearchDetailResponse> responseObserver) { responseObserver.onNext(com.university.grpc.SearchDetailResponse.newBuilder().build()); responseObserver.onCompleted(); }")
    }
    "ranking" = @{
        Service = "RankingService"
        GrpcBase = "RankingServiceImplBase"
        Methods = @("public void calculateRelevanceScore(com.university.grpc.RankingRequest request, io.grpc.stub.StreamObserver<com.university.grpc.RankingResponse> responseObserver) { responseObserver.onNext(com.university.grpc.RankingResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void boostResults(com.university.grpc.BoostRequest request, io.grpc.stub.StreamObserver<com.university.grpc.RankingResponse> responseObserver) { responseObserver.onNext(com.university.grpc.RankingResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void applyRankingModel(com.university.grpc.RankingModelRequest request, io.grpc.stub.StreamObserver<com.university.grpc.RankingResponse> responseObserver) { responseObserver.onNext(com.university.grpc.RankingResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void personalizeRanking(com.university.grpc.PersonalizeRankingRequest request, io.grpc.stub.StreamObserver<com.university.grpc.RankingResponse> responseObserver) { responseObserver.onNext(com.university.grpc.RankingResponse.newBuilder().build()); responseObserver.onCompleted(); }")
    }
    "filter" = @{
        Service = "FilterService"
        GrpcBase = "FilterServiceImplBase"
        Methods = @("public void applyFilters(com.university.grpc.FilterRequest request, io.grpc.stub.StreamObserver<com.university.grpc.FilterResponse> responseObserver) { responseObserver.onNext(com.university.grpc.FilterResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void getFacetCounts(com.university.grpc.FacetRequest request, io.grpc.stub.StreamObserver<com.university.grpc.FacetResponse> responseObserver) { responseObserver.onNext(com.university.grpc.FacetResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void refineSearch(com.university.grpc.RefineRequest request, io.grpc.stub.StreamObserver<com.university.grpc.FilterResponse> responseObserver) { responseObserver.onNext(com.university.grpc.FilterResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void combineFilters(com.university.grpc.CombineFilterRequest request, io.grpc.stub.StreamObserver<com.university.grpc.FilterResponse> responseObserver) { responseObserver.onNext(com.university.grpc.FilterResponse.newBuilder().build()); responseObserver.onCompleted(); }")
    }
    "personalization" = @{
        Service = "PersonalizationService"
        GrpcBase = "PersonalizationServiceImplBase"
        Methods = @("public void trackUserSearch(com.university.grpc.TrackEventRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }",
                    "public void buildUserProfile(com.university.grpc.UserProfileRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }",
                    "public void recommendContent(com.university.grpc.RecommendationRequest request, io.grpc.stub.StreamObserver<com.university.grpc.RecommendationResponse> responseObserver) { responseObserver.onNext(com.university.grpc.RecommendationResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void getPersonalizedResults(com.university.grpc.PersonalizedResultRequest request, io.grpc.stub.StreamObserver<com.university.grpc.RecommendationResponse> responseObserver) { responseObserver.onNext(com.university.grpc.RecommendationResponse.newBuilder().build()); responseObserver.onCompleted(); }")
    }
    "document" = @{
        Service = "DocumentService"
        GrpcBase = "DocumentServiceImplBase"
        Methods = @("public void uploadDocument(com.university.grpc.DocumentUploadRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }",
                    "public void updateMetadata(com.university.grpc.MetadataUpdateRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }",
                    "public void tagDocument(com.university.grpc.TagRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }",
                    "public void classifyDocument(com.university.grpc.ClassifyRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }",
                    "public void getDocumentDetails(com.university.grpc.DocumentIdRequest request, io.grpc.stub.StreamObserver<com.university.grpc.DocumentDataResponse> responseObserver) { responseObserver.onNext(com.university.grpc.DocumentDataResponse.newBuilder().build()); responseObserver.onCompleted(); }")
    }
    "access-control" = @{
        Service = "AccessControlService"
        GrpcBase = "AccessControlServiceImplBase"
        Methods = @("public void defineAccessPolicy(com.university.grpc.PolicyRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }",
                    "public void assignPermissions(com.university.grpc.PermissionRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }",
                    "public void validateAccess(com.university.grpc.AccessValidationRequest request, io.grpc.stub.StreamObserver<com.university.grpc.AccessValidationResponse> responseObserver) { responseObserver.onNext(com.university.grpc.AccessValidationResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void revokeAccess(com.university.grpc.RevokeRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }")
    }
    "query-optimization" = @{
        Service = "QueryOptimizationService"
        GrpcBase = "QueryOptimizationServiceImplBase"
        Methods = @("public void suggestQuery(com.university.grpc.SuggestionRequest request, io.grpc.stub.StreamObserver<com.university.grpc.SuggestionResponse> responseObserver) { responseObserver.onNext(com.university.grpc.SuggestionResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void autoComplete(com.university.grpc.AutoCompleteRequest request, io.grpc.stub.StreamObserver<com.university.grpc.AutoCompleteResponse> responseObserver) { responseObserver.onNext(com.university.grpc.AutoCompleteResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void correctSpelling(com.university.grpc.SpellingRequest request, io.grpc.stub.StreamObserver<com.university.grpc.SpellingResponse> responseObserver) { responseObserver.onNext(com.university.grpc.SpellingResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void expandQuery(com.university.grpc.ExpansionRequest request, io.grpc.stub.StreamObserver<com.university.grpc.ExpansionResponse> responseObserver) { responseObserver.onNext(com.university.grpc.ExpansionResponse.newBuilder().build()); responseObserver.onCompleted(); }")
    }
    "notification" = @{
        Service = "NotificationService"
        GrpcBase = "NotificationServiceImplBase"
        Methods = @("public void logSearchEvent(com.university.grpc.LogRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }",
                    "public void sendSearchAlert(com.university.grpc.AlertRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }",
                    "public void notifyNewContent(com.university.grpc.NewContentRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }",
                    "public void getNotifications(com.university.grpc.NotificationListRequest request, io.grpc.stub.StreamObserver<com.university.grpc.NotificationListResponse> responseObserver) { responseObserver.onNext(com.university.grpc.NotificationListResponse.newBuilder().build()); responseObserver.onCompleted(); }")
    }
    "abuse-detection" = @{
        Service = "AbuseDetectionService"
        GrpcBase = "AbuseDetectionServiceImplBase"
        Methods = @("public void detectSearchAbuse(com.university.grpc.AbuseCheckRequest request, io.grpc.stub.StreamObserver<com.university.grpc.AbuseCheckResponse> responseObserver) { responseObserver.onNext(com.university.grpc.AbuseCheckResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void analyzeQueryPatterns(com.university.grpc.PatternRequest request, io.grpc.stub.StreamObserver<com.university.grpc.PatternResponse> responseObserver) { responseObserver.onNext(com.university.grpc.PatternResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void flagSuspiciousUsers(com.university.grpc.FlagRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }",
                    "public void getFraudReports(com.university.grpc.ReportRequest request, io.grpc.stub.StreamObserver<com.university.grpc.ReportResponse> responseObserver) { responseObserver.onNext(com.university.grpc.ReportResponse.newBuilder().build()); responseObserver.onCompleted(); }")
    }
    "auth" = @{
        Service = "AuthService"
        GrpcBase = "AuthServiceImplBase"
        Methods = @("public void login(com.university.grpc.LoginRequest request, io.grpc.stub.StreamObserver<com.university.grpc.TokenResponse> responseObserver) { responseObserver.onNext(com.university.grpc.TokenResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void validateToken(com.university.grpc.TokenValidationRequest request, io.grpc.stub.StreamObserver<com.university.grpc.ValidationResponse> responseObserver) { responseObserver.onNext(com.university.grpc.ValidationResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void refreshToken(com.university.grpc.RefreshTokenRequest request, io.grpc.stub.StreamObserver<com.university.grpc.TokenResponse> responseObserver) { responseObserver.onNext(com.university.grpc.TokenResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void logout(com.university.grpc.LogoutRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }")
    }
    "analytics" = @{
        Service = "AnalyticsService"
        GrpcBase = "AnalyticsServiceImplBase"
        Methods = @("public void getSearchAnalytics(com.university.grpc.AnalyticsRequest request, io.grpc.stub.StreamObserver<com.university.grpc.AnalyticsResponse> responseObserver) { responseObserver.onNext(com.university.grpc.AnalyticsResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void getPopularQueries(com.university.grpc.PopularityRequest request, io.grpc.stub.StreamObserver<com.university.grpc.PopularityResponse> responseObserver) { responseObserver.onNext(com.university.grpc.PopularityResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void getUserEngagement(com.university.grpc.EngagementRequest request, io.grpc.stub.StreamObserver<com.university.grpc.EngagementResponse> responseObserver) { responseObserver.onNext(com.university.grpc.EngagementResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void getContentPerformance(com.university.grpc.PerformanceRequest request, io.grpc.stub.StreamObserver<com.university.grpc.PerformanceResponse> responseObserver) { responseObserver.onNext(com.university.grpc.PerformanceResponse.newBuilder().build()); responseObserver.onCompleted(); }")
    }
    "prediction" = @{
        Service = "PredictionService"
        GrpcBase = "PredictionServiceImplBase"
        Methods = @("public void forecastSearchTrends(com.university.grpc.ForecastRequest request, io.grpc.stub.StreamObserver<com.university.grpc.ForecastResponse> responseObserver) { responseObserver.onNext(com.university.grpc.ForecastResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void recommendContentOptimization(com.university.grpc.OptimizationRequest request, io.grpc.stub.StreamObserver<com.university.grpc.OptimizationResponse> responseObserver) { responseObserver.onNext(com.university.grpc.OptimizationResponse.newBuilder().build()); responseObserver.onCompleted(); }",
                    "public void analyzeUsagePatterns(com.university.grpc.UsagePatternRequest request, io.grpc.stub.StreamObserver<com.university.grpc.UsagePatternResponse> responseObserver) { responseObserver.onNext(com.university.grpc.UsagePatternResponse.newBuilder().build()); responseObserver.onCompleted(); }")
    }
}

foreach ($modName in $modules.Keys) {
    $modConfig = $modules[$modName]
    $pkgName = $modName.Replace("-", "")
    $basePath = "$modName-module/src/main/java/com/university/$pkgName"
    
    # Capitalize the first letter of the package name for classes
    $classNamePrefix = (Get-Culture).TextInfo.ToTitleCase($pkgName)
    
    # Create Dirs
    New-Item -ItemType Directory -Force -Path "$basePath/grpc" | Out-Null
    New-Item -ItemType Directory -Force -Path "$basePath/service" | Out-Null
    New-Item -ItemType Directory -Force -Path "$basePath/repository" | Out-Null
    New-Item -ItemType Directory -Force -Path "$basePath/entity" | Out-Null

    # 1. Entity
    $entityContent = @"
package com.university.$pkgName.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = `"${pkgName}_data`")
public class ${classNamePrefix}Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
"@
    Set-Content -Path "$basePath/entity/${classNamePrefix}Entity.java" -Value $entityContent

    # 2. Repository
    $repoContent = @"
package com.university.$pkgName.repository;

import com.university.$pkgName.entity.${classNamePrefix}Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ${classNamePrefix}Repository extends JpaRepository<${classNamePrefix}Entity, Long> {
}
"@
    Set-Content -Path "$basePath/repository/${classNamePrefix}Repository.java" -Value $repoContent

    # 3. Service
    $serviceContent = @"
package com.university.$pkgName.service;

import com.university.$pkgName.repository.${classNamePrefix}Repository;
import org.springframework.stereotype.Service;


@Service
public class ${classNamePrefix}Service {
    private final ${classNamePrefix}Repository repository;

    public ${classNamePrefix}Service(${classNamePrefix}Repository repository) {
        this.repository = repository;
    }
    
    // Core business logic goes here
}
"@
    Set-Content -Path "$basePath/service/${classNamePrefix}Service.java" -Value $serviceContent

    # 4. GrpcService
    $methodsContent = ""
    foreach ($m in $modConfig.Methods) {
        $methodsContent += "    @Override`n    $m`n`n"
    }
    
    $grpcContent = @"
package com.university.$pkgName.grpc;

import com.university.$pkgName.service.${classNamePrefix}Service;
import com.university.grpc.$($modConfig.Service)Grpc;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class $($modConfig.Service)Impl extends $($modConfig.Service)Grpc.$($modConfig.GrpcBase) {
    
    private final ${classNamePrefix}Service service;

    public $($modConfig.Service)Impl(${classNamePrefix}Service service) {
        this.service = service;
    }

$methodsContent
}
"@
    Set-Content -Path "$basePath/grpc/$($modConfig.Service)Impl.java" -Value $grpcContent

    Write-Host "Generated code for module: $modName"
}
