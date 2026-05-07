$modulesLogic = @{
    "ingest" = @{
        Service = "IngestService"; GrpcBase = "IngestServiceImplBase"
        Dtos = @{ "IngestRequestRecord" = "package com.university.ingest.dto;`n`npublic record IngestRequestRecord(String id, String title, String payload) {}" }
        ServiceMethods = @"
    public String ingest(IngestRequestRecord record) {
        System.out.println(`"Ingesting: `" + record.title());
        IngestEntity entity = new IngestEntity();
        entity.setName(record.title());
        repository.save(entity);
        return `"Indexed `" + record.title();
    }
"@
        GrpcMethods = @"
    @Override
    public void ingestDocuments(com.university.grpc.IngestRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) {
        String msg = service.ingest(new com.university.ingest.dto.IngestRequestRecord(`"1`", `"Doc`", request.getPayloadJson()));
        responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).setMessage(msg).build());
        responseObserver.onCompleted();
    }
    @Override public void indexCourses(com.university.grpc.IngestRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }
    @Override public void indexFacultyProfiles(com.university.grpc.IngestRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }
    @Override public void indexResearchPapers(com.university.grpc.IngestRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }
    @Override public void updateIndex(com.university.grpc.UpdateRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }
    @Override public void deleteFromIndex(com.university.grpc.DeleteRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }
"@
    }
    "search" = @{
        Service = "SearchService"; GrpcBase = "SearchServiceImplBase"
        Dtos = @{ "SearchRecord" = "package com.university.search.dto;`n`npublic record SearchRecord(String query, int page) {}" }
        ServiceMethods = @"
    public java.util.List<String> execute(SearchRecord record) {
        return java.util.List.of(`"Result for `" + record.query());
    }
"@
        GrpcMethods = @"
    @Override
    public void executeSearch(com.university.grpc.SearchRequest request, io.grpc.stub.StreamObserver<com.university.grpc.SearchResponse> responseObserver) {
        java.util.List<String> results = service.execute(new com.university.search.dto.SearchRecord(request.getQuery(), 0));
        responseObserver.onNext(com.university.grpc.SearchResponse.newBuilder().addAllResults(results).build());
        responseObserver.onCompleted();
    }
    @Override public void advancedSearch(com.university.grpc.AdvancedSearchRequest request, io.grpc.stub.StreamObserver<com.university.grpc.SearchResponse> responseObserver) { responseObserver.onNext(com.university.grpc.SearchResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void filterSearchResults(com.university.grpc.FilterRequest request, io.grpc.stub.StreamObserver<com.university.grpc.SearchResponse> responseObserver) { responseObserver.onNext(com.university.grpc.SearchResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void paginateResults(com.university.grpc.PaginationRequest request, io.grpc.stub.StreamObserver<com.university.grpc.SearchResponse> responseObserver) { responseObserver.onNext(com.university.grpc.SearchResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void getSearchById(com.university.grpc.SearchIdRequest request, io.grpc.stub.StreamObserver<com.university.grpc.SearchDetailResponse> responseObserver) { responseObserver.onNext(com.university.grpc.SearchDetailResponse.newBuilder().build()); responseObserver.onCompleted(); }
"@
    }
    "ranking" = @{
        Service = "RankingService"; GrpcBase = "RankingServiceImplBase"
        Dtos = @{ "RankingRecord" = "package com.university.ranking.dto;`n`npublic record RankingRecord(double tfIdf, double popularity) {}" }
        ServiceMethods = @"
    public double score(RankingRecord record) {
        return record.tfIdf() * 0.7 + record.popularity() * 0.3;
    }
"@
        GrpcMethods = @"
    @Override
    public void calculateRelevanceScore(com.university.grpc.RankingRequest request, io.grpc.stub.StreamObserver<com.university.grpc.RankingResponse> responseObserver) {
        double s = service.score(new com.university.ranking.dto.RankingRecord(0.8, 0.5));
        responseObserver.onNext(com.university.grpc.RankingResponse.newBuilder().putScores(request.getDocumentId(), s).build());
        responseObserver.onCompleted();
    }
    @Override public void boostResults(com.university.grpc.BoostRequest request, io.grpc.stub.StreamObserver<com.university.grpc.RankingResponse> responseObserver) { responseObserver.onNext(com.university.grpc.RankingResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void applyRankingModel(com.university.grpc.RankingModelRequest request, io.grpc.stub.StreamObserver<com.university.grpc.RankingResponse> responseObserver) { responseObserver.onNext(com.university.grpc.RankingResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void personalizeRanking(com.university.grpc.PersonalizeRankingRequest request, io.grpc.stub.StreamObserver<com.university.grpc.RankingResponse> responseObserver) { responseObserver.onNext(com.university.grpc.RankingResponse.newBuilder().build()); responseObserver.onCompleted(); }
"@
    }
    "filter" = @{
        Service = "FilterService"; GrpcBase = "FilterServiceImplBase"
        Dtos = @{ "FilterRecord" = "package com.university.filter.dto;`n`npublic record FilterRecord(String field, String value) {}" }
        ServiceMethods = @"
    public void apply(FilterRecord record) { System.out.println(`"Filtering `" + record.field()); }
"@
        GrpcMethods = @"
    @Override public void applyFilters(com.university.grpc.FilterRequest request, io.grpc.stub.StreamObserver<com.university.grpc.FilterResponse> responseObserver) { responseObserver.onNext(com.university.grpc.FilterResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void getFacetCounts(com.university.grpc.FacetRequest request, io.grpc.stub.StreamObserver<com.university.grpc.FacetResponse> responseObserver) { responseObserver.onNext(com.university.grpc.FacetResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void refineSearch(com.university.grpc.RefineRequest request, io.grpc.stub.StreamObserver<com.university.grpc.FilterResponse> responseObserver) { responseObserver.onNext(com.university.grpc.FilterResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void combineFilters(com.university.grpc.CombineFilterRequest request, io.grpc.stub.StreamObserver<com.university.grpc.FilterResponse> responseObserver) { responseObserver.onNext(com.university.grpc.FilterResponse.newBuilder().build()); responseObserver.onCompleted(); }
"@
    }
    "personalization" = @{
        Service = "PersonalizationService"; GrpcBase = "PersonalizationServiceImplBase"
        Dtos = @{ "UserRecord" = "package com.university.personalization.dto;`n`npublic record UserRecord(String userId) {}" }
        ServiceMethods = @"
    public void personalize(UserRecord record) { System.out.println(`"Personalizing for `" + record.userId()); }
"@
        GrpcMethods = @"
    @Override public void trackUserSearch(com.university.grpc.TrackEventRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }
    @Override public void buildUserProfile(com.university.grpc.UserProfileRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }
    @Override public void recommendContent(com.university.grpc.RecommendationRequest request, io.grpc.stub.StreamObserver<com.university.grpc.RecommendationResponse> responseObserver) { responseObserver.onNext(com.university.grpc.RecommendationResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void getPersonalizedResults(com.university.grpc.PersonalizedResultRequest request, io.grpc.stub.StreamObserver<com.university.grpc.RecommendationResponse> responseObserver) { responseObserver.onNext(com.university.grpc.RecommendationResponse.newBuilder().build()); responseObserver.onCompleted(); }
"@
    }
    "document" = @{
        Service = "DocumentService"; GrpcBase = "DocumentServiceImplBase"
        Dtos = @{ "DocRecord" = "package com.university.document.dto;`n`npublic record DocRecord(String id, String metadata) {}" }
        ServiceMethods = @"
    public void manage(DocRecord record) { System.out.println(`"Managing doc `" + record.id()); }
"@
        GrpcMethods = @"
    @Override public void uploadDocument(com.university.grpc.DocumentUploadRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }
    @Override public void updateMetadata(com.university.grpc.MetadataUpdateRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }
    @Override public void tagDocument(com.university.grpc.TagRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }
    @Override public void classifyDocument(com.university.grpc.ClassifyRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }
    @Override public void getDocumentDetails(com.university.grpc.DocumentIdRequest request, io.grpc.stub.StreamObserver<com.university.grpc.DocumentDataResponse> responseObserver) { responseObserver.onNext(com.university.grpc.DocumentDataResponse.newBuilder().build()); responseObserver.onCompleted(); }
"@
    }
    "access-control" = @{
        Service = "AccessControlService"; GrpcBase = "AccessControlServiceImplBase"
        Dtos = @{ "PolicyRecord" = "package com.university.accesscontrol.dto;`n`npublic record PolicyRecord(String role) {}" }
        ServiceMethods = @"
    public boolean check(PolicyRecord record) { return true; }
"@
        GrpcMethods = @"
    @Override public void defineAccessPolicy(com.university.grpc.PolicyRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }
    @Override public void assignPermissions(com.university.grpc.PermissionRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }
    @Override public void validateAccess(com.university.grpc.AccessValidationRequest request, io.grpc.stub.StreamObserver<com.university.grpc.AccessValidationResponse> responseObserver) { responseObserver.onNext(com.university.grpc.AccessValidationResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void revokeAccess(com.university.grpc.RevokeRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }
"@
    }
    "query-optimization" = @{
        Service = "QueryOptimizationService"; GrpcBase = "QueryOptimizationServiceImplBase"
        Dtos = @{ "QueryRecord" = "package com.university.queryoptimization.dto;`n`npublic record QueryRecord(String raw) {}" }
        ServiceMethods = @"
    public String optimize(QueryRecord record) { return record.raw().toLowerCase(); }
"@
        GrpcMethods = @"
    @Override public void suggestQuery(com.university.grpc.SuggestionRequest request, io.grpc.stub.StreamObserver<com.university.grpc.SuggestionResponse> responseObserver) { responseObserver.onNext(com.university.grpc.SuggestionResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void autoComplete(com.university.grpc.AutoCompleteRequest request, io.grpc.stub.StreamObserver<com.university.grpc.AutoCompleteResponse> responseObserver) { responseObserver.onNext(com.university.grpc.AutoCompleteResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void correctSpelling(com.university.grpc.SpellingRequest request, io.grpc.stub.StreamObserver<com.university.grpc.SpellingResponse> responseObserver) { responseObserver.onNext(com.university.grpc.SpellingResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void expandQuery(com.university.grpc.ExpansionRequest request, io.grpc.stub.StreamObserver<com.university.grpc.ExpansionResponse> responseObserver) { responseObserver.onNext(com.university.grpc.ExpansionResponse.newBuilder().build()); responseObserver.onCompleted(); }
"@
    }
    "notification" = @{
        Service = "NotificationService"; GrpcBase = "NotificationServiceImplBase"
        Dtos = @{ "NotifyRecord" = "package com.university.notification.dto;`n`npublic record NotifyRecord(String msg) {}" }
        ServiceMethods = @"
    public void notifyUser(NotifyRecord record) { System.out.println(record.msg()); }
"@
        GrpcMethods = @"
    @Override public void logSearchEvent(com.university.grpc.LogRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }
    @Override public void sendSearchAlert(com.university.grpc.AlertRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }
    @Override public void notifyNewContent(com.university.grpc.NewContentRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }
    @Override public void getNotifications(com.university.grpc.NotificationListRequest request, io.grpc.stub.StreamObserver<com.university.grpc.NotificationListResponse> responseObserver) { responseObserver.onNext(com.university.grpc.NotificationListResponse.newBuilder().build()); responseObserver.onCompleted(); }
"@
    }
    "abuse-detection" = @{
        Service = "AbuseDetectionService"; GrpcBase = "AbuseDetectionServiceImplBase"
        Dtos = @{ "AbuseRecord" = "package com.university.abusedetection.dto;`n`npublic record AbuseRecord(String ip) {}" }
        ServiceMethods = @"
    public boolean isAbusive(AbuseRecord record) { return false; }
"@
        GrpcMethods = @"
    @Override public void detectSearchAbuse(com.university.grpc.AbuseCheckRequest request, io.grpc.stub.StreamObserver<com.university.grpc.AbuseCheckResponse> responseObserver) { responseObserver.onNext(com.university.grpc.AbuseCheckResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void analyzeQueryPatterns(com.university.grpc.PatternRequest request, io.grpc.stub.StreamObserver<com.university.grpc.PatternResponse> responseObserver) { responseObserver.onNext(com.university.grpc.PatternResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void flagSuspiciousUsers(com.university.grpc.FlagRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }
    @Override public void getFraudReports(com.university.grpc.ReportRequest request, io.grpc.stub.StreamObserver<com.university.grpc.ReportResponse> responseObserver) { responseObserver.onNext(com.university.grpc.ReportResponse.newBuilder().build()); responseObserver.onCompleted(); }
"@
    }
    "auth" = @{
        Service = "AuthService"; GrpcBase = "AuthServiceImplBase"
        Dtos = @{ "AuthRecord" = "package com.university.auth.dto;`n`npublic record AuthRecord(String user) {}" }
        ServiceMethods = @"
    public String authenticate(AuthRecord record) { return `"token123`"; }
"@
        GrpcMethods = @"
    @Override public void login(com.university.grpc.LoginRequest request, io.grpc.stub.StreamObserver<com.university.grpc.TokenResponse> responseObserver) { responseObserver.onNext(com.university.grpc.TokenResponse.newBuilder().setAccessToken(service.authenticate(new com.university.auth.dto.AuthRecord(request.getUsername()))).build()); responseObserver.onCompleted(); }
    @Override public void validateToken(com.university.grpc.TokenValidationRequest request, io.grpc.stub.StreamObserver<com.university.grpc.ValidationResponse> responseObserver) { responseObserver.onNext(com.university.grpc.ValidationResponse.newBuilder().setIsValid(true).build()); responseObserver.onCompleted(); }
    @Override public void refreshToken(com.university.grpc.RefreshTokenRequest request, io.grpc.stub.StreamObserver<com.university.grpc.TokenResponse> responseObserver) { responseObserver.onNext(com.university.grpc.TokenResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void logout(com.university.grpc.LogoutRequest request, io.grpc.stub.StreamObserver<com.university.grpc.StatusResponse> responseObserver) { responseObserver.onNext(com.university.grpc.StatusResponse.newBuilder().setSuccess(true).build()); responseObserver.onCompleted(); }
"@
    }
    "analytics" = @{
        Service = "AnalyticsService"; GrpcBase = "AnalyticsServiceImplBase"
        Dtos = @{ "StatRecord" = "package com.university.analytics.dto;`n`npublic record StatRecord(String metric) {}" }
        ServiceMethods = @"
    public void track(StatRecord record) { System.out.println(record.metric()); }
"@
        GrpcMethods = @"
    @Override public void getSearchAnalytics(com.university.grpc.AnalyticsRequest request, io.grpc.stub.StreamObserver<com.university.grpc.AnalyticsResponse> responseObserver) { responseObserver.onNext(com.university.grpc.AnalyticsResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void getPopularQueries(com.university.grpc.PopularityRequest request, io.grpc.stub.StreamObserver<com.university.grpc.PopularityResponse> responseObserver) { responseObserver.onNext(com.university.grpc.PopularityResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void getUserEngagement(com.university.grpc.EngagementRequest request, io.grpc.stub.StreamObserver<com.university.grpc.EngagementResponse> responseObserver) { responseObserver.onNext(com.university.grpc.EngagementResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void getContentPerformance(com.university.grpc.PerformanceRequest request, io.grpc.stub.StreamObserver<com.university.grpc.PerformanceResponse> responseObserver) { responseObserver.onNext(com.university.grpc.PerformanceResponse.newBuilder().build()); responseObserver.onCompleted(); }
"@
    }
    "prediction" = @{
        Service = "PredictionService"; GrpcBase = "PredictionServiceImplBase"
        Dtos = @{ "PredictRecord" = "package com.university.prediction.dto;`n`npublic record PredictRecord(String data) {}" }
        ServiceMethods = @"
    public void predict(PredictRecord record) { System.out.println(`"Predicting `" + record.data()); }
"@
        GrpcMethods = @"
    @Override public void forecastSearchTrends(com.university.grpc.ForecastRequest request, io.grpc.stub.StreamObserver<com.university.grpc.ForecastResponse> responseObserver) { responseObserver.onNext(com.university.grpc.ForecastResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void recommendContentOptimization(com.university.grpc.OptimizationRequest request, io.grpc.stub.StreamObserver<com.university.grpc.OptimizationResponse> responseObserver) { responseObserver.onNext(com.university.grpc.OptimizationResponse.newBuilder().build()); responseObserver.onCompleted(); }
    @Override public void analyzeUsagePatterns(com.university.grpc.UsagePatternRequest request, io.grpc.stub.StreamObserver<com.university.grpc.UsagePatternResponse> responseObserver) { responseObserver.onNext(com.university.grpc.UsagePatternResponse.newBuilder().build()); responseObserver.onCompleted(); }
"@
    }
}

foreach ($modName in $modulesLogic.Keys) {
    $modConfig = $modulesLogic[$modName]
    $pkgName = $modName.Replace("-", "")
    $basePath = "$modName-module/src/main/java/com/university/$pkgName"
    $classNamePrefix = (Get-Culture).TextInfo.ToTitleCase($pkgName)
    
    # 1. DTOs
    New-Item -ItemType Directory -Force -Path "$basePath/dto" | Out-Null
    foreach ($dtoName in $modConfig.Dtos.Keys) {
        Set-Content -Path "$basePath/dto/${dtoName}.java" -Value $modConfig.Dtos[$dtoName]
    }
    
    # 2. Service
    $serviceContent = @"
package com.university.$pkgName.service;
import com.university.$pkgName.repository.${classNamePrefix}Repository;
import com.university.$pkgName.entity.${classNamePrefix}Entity;
import com.university.$pkgName.dto.*;
import org.springframework.stereotype.Service;
@Service
public class ${classNamePrefix}Service {
    private final ${classNamePrefix}Repository repository;
    public ${classNamePrefix}Service(${classNamePrefix}Repository repository) { this.repository = repository; }
    $($modConfig.ServiceMethods)
}
"@
    Set-Content -Path "$basePath/service/${classNamePrefix}Service.java" -Value $serviceContent
    
    # 3. GrpcService
    $grpcContent = @"
package com.university.$pkgName.grpc;
import com.university.$pkgName.service.${classNamePrefix}Service;
import net.devh.boot.grpc.server.service.GrpcService;
@GrpcService
public class $($modConfig.Service)Impl extends com.university.grpc.$($modConfig.Service)Grpc.$($modConfig.GrpcBase) {
    private final ${classNamePrefix}Service service;
    public $($modConfig.Service)Impl(${classNamePrefix}Service service) { this.service = service; }
    $($modConfig.GrpcMethods)
}
"@
    Set-Content -Path "$basePath/grpc/$($modConfig.Service)Impl.java" -Value $grpcContent
    Write-Host "Updated $modName"
}
