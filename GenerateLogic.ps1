$modulesLogic = @{
    "ingest" = @{
        Dtos = @{
            "DocumentDto" = "public record DocumentDto(String title, String content, String author, String type) {}"
            "IngestResultDto" = "public record IngestResultDto(boolean success, String documentId, String message) {}"
        }
        ServiceMethods = @"
    // Business Rule: Incremental indexing, Deduplication, Schema validation
    public IngestResultDto ingestDocument(DocumentDto dto) {
        if (!validateSchema(dto)) {
            return new IngestResultDto(false, null, `"Invalid Schema`");
        }
        if (isDuplicate(dto)) {
            return new IngestResultDto(false, null, `"Duplicate Document`");
        }
        // Save to DB
        IngestEntity entity = new IngestEntity();
        entity.setName(dto.title());
        repository.save(entity);
        return new IngestResultDto(true, entity.getId().toString(), `"Indexed successfully`");
    }
    
    private boolean validateSchema(DocumentDto dto) {
        return dto.title() != null && !dto.title().isEmpty();
    }
    
    private boolean isDuplicate(DocumentDto dto) {
        // Mock deduplication check
        return false;
    }
"@
    }
    "search" = @{
        Dtos = @{
            "SearchQueryDto" = "import java.util.List;`npublic record SearchQueryDto(String query, List<String> fields, int page, int size) {}"
            "SearchResultDto" = "public record SearchResultDto(String documentId, double relevanceScore, String snippet) {}"
        }
        ServiceMethods = @"
    // Business Rule: Full-text search, multi-field, pagination
    public java.util.List<SearchResultDto> executeFullTextSearch(SearchQueryDto query) {
        // Mocking PostgreSQL Full-Text Search execution
        System.out.println(`"Executing search for: `" + query.query());
        
        // Return paginated dummy results
        return java.util.List.of(
            new SearchResultDto(`"doc-1`", 0.95, `"Sample snippet matching `" + query.query())
        );
    }
"@
    }
    "ranking" = @{
        Dtos = @{
            "RankingContextDto" = "public record RankingContextDto(String documentId, double tfIdfScore, double popularity, double recency) {}"
        }
        ServiceMethods = @"
    // Business Rule: Relevance Score = w1 * TF-IDF + w2 * Popularity + w3 * Recency
    public double calculateRelevanceScore(RankingContextDto context) {
        double w1 = 0.6; // TF-IDF weight
        double w2 = 0.3; // Popularity weight
        double w3 = 0.1; // Freshness/Recency weight
        
        double finalScore = (w1 * context.tfIdfScore()) + (w2 * context.popularity()) + (w3 * context.recency());
        return finalScore;
    }
    
    public double applyUserPersonalizationBoost(double baseScore, String userId) {
        // User-specific ranking logic
        return baseScore * 1.1; // 10% boost for personalized content
    }
"@
    }
    "filter" = @{
        Dtos = @{
            "FilterCriteriaDto" = "import java.util.Map;`npublic record FilterCriteriaDto(Map<String, String> dynamicFilters) {}"
            "FacetResultDto" = "import java.util.Map;`npublic record FacetResultDto(String field, Map<String, Integer> counts) {}"
        }
        ServiceMethods = @"
    // Business Rule: Dynamic filtering, Multi-level facets
    public java.util.List<String> applyDynamicFilters(FilterCriteriaDto criteria) {
        System.out.println(`"Applying filters: `" + criteria.dynamicFilters());
        return java.util.List.of(`"filtered-doc-1`", `"filtered-doc-2`");
    }
    
    public FacetResultDto generateFacets(String field) {
        return new FacetResultDto(field, java.util.Map.of(`"Science`", 120, `"Arts`", 85));
    }
"@
    }
    "personalization" = @{
        Dtos = @{
            "UserProfileDto" = "import java.util.List;`npublic record UserProfileDto(String userId, String role, List<String> searchHistory) {}"
        }
        ServiceMethods = @"
    // Business Rule: Based on search history, Role-based personalization
    public void trackSearchHistory(String userId, String query) {
        System.out.println(`"Tracking user `" + userId + `" searched for: `" + query);
    }
    
    public java.util.List<String> getRoleBasedRecommendations(UserProfileDto profile) {
        if (`"FACULTY`".equals(profile.role())) {
            return java.util.List.of(`"Research Grant Templates`", `"Tenure Guidelines`");
        }
        return java.util.List.of(`"Course Catalog`", `"Student Life`");
    }
"@
    }
    "document" = @{
        Dtos = @{
            "DocumentMetadataDto" = "import java.util.List;`npublic record DocumentMetadataDto(String id, String category, List<String> tags) {}"
        }
        ServiceMethods = @"
    // Business Rule: Metadata validation, Tagging consistency
    public boolean validateAndCategorize(DocumentMetadataDto metadata) {
        if (metadata.tags() == null || metadata.tags().isEmpty()) {
            throw new IllegalArgumentException(`"Document must have at least one tag`");
        }
        System.out.println(`"Categorizing document `" + metadata.id() + `" into `" + metadata.category());
        return true;
    }
"@
    }
    "access-control" = @{
        Dtos = @{
            "AccessPolicyDto" = "import java.util.List;`npublic record AccessPolicyDto(String role, List<String> allowedResources) {}"
            "AccessRequestDto" = "public record AccessRequestDto(String userId, String role, String resourceId) {}"
        }
        ServiceMethods = @"
    // Business Rule: Role-based access, Restricted documents
    public boolean isAccessAllowed(AccessRequestDto request, AccessPolicyDto policy) {
        System.out.println(`"Validating access for user `" + request.userId() + `" on `" + request.resourceId());
        if (`"ADMIN`".equals(request.role())) {
            return true;
        }
        return policy.allowedResources().contains(request.resourceId());
    }
"@
    }
    "query-optimization" = @{
        Dtos = @{
            "QueryExpansionDto" = "import java.util.List;`npublic record QueryExpansionDto(String original, List<String> expandedTerms) {}"
        }
        ServiceMethods = @"
    // Business Rule: Query expansion, Synonym mapping, Typo correction
    public QueryExpansionDto optimizeQuery(String rawQuery) {
        String corrected = correctTypo(rawQuery);
        java.util.List<String> synonyms = mapSynonyms(corrected);
        return new QueryExpansionDto(rawQuery, synonyms);
    }
    
    private String correctTypo(String query) {
        // Mock typo correction
        return query.replace(`"univrsity`", `"university`");
    }
    
    private java.util.List<String> mapSynonyms(String query) {
        if (query.contains(`"AI`")) return java.util.List.of(`"AI`", `"Artificial Intelligence`", `"Machine Learning`");
        return java.util.List.of(query);
    }
"@
    }
    "notification" = @{
        Dtos = @{
            "NotificationEventDto" = "public record NotificationEventDto(String targetUserId, String eventType, String message) {}"
        }
        ServiceMethods = @"
    // Business Rule: Event tracking, Alert for new relevant content
    public void dispatchAlert(NotificationEventDto event) {
        // Here we would push to Kafka or WebSocket
        System.out.println(`"Sending alert to `" + event.targetUserId() + `": `" + event.message());
    }
"@
    }
    "abuse-detection" = @{
        Dtos = @{
            "AnomalyReportDto" = "public record AnomalyReportDto(String ipAddress, String userId, int requestRate, boolean isBot) {}"
        }
        ServiceMethods = @"
    // Business Rule: Query rate limiting, Bot detection
    public boolean evaluateTraffic(AnomalyReportDto report) {
        if (report.requestRate() > 1000) {
            System.err.println(`"Rate limit exceeded for IP: `" + report.ipAddress());
            return true; // is abusive
        }
        if (report.isBot()) {
            System.err.println(`"Bot traffic detected for User: `" + report.userId());
            return true;
        }
        return false;
    }
"@
    }
    "auth" = @{
        Dtos = @{
            "LoginDto" = "public record LoginDto(String username, String password) {}"
            "JwtTokenDto" = "public record JwtTokenDto(String accessToken, String refreshToken, long expiresIn) {}"
        }
        ServiceMethods = @"
    // Business Rule: OAuth token validation, Role-based RPC access
    public JwtTokenDto authenticateUser(LoginDto login) {
        // Mock Spring Security OAuth2 Authentication
        if (`"admin`".equals(login.username()) && `"password`".equals(login.password())) {
            return new JwtTokenDto(`"mock-jwt-access-token`", `"mock-refresh-token`", 3600);
        }
        throw new RuntimeException(`"Invalid Credentials`");
    }
    
    public boolean validateToken(String token) {
        return token != null && token.startsWith(`"mock-jwt`");
    }
"@
    }
    "analytics" = @{
        Dtos = @{
            "MetricDto" = "public record MetricDto(String metricName, double value, String timestamp) {}"
        }
        ServiceMethods = @"
    // Business Rule: Aggregation queries, Time-based analytics
    public void recordMetric(MetricDto metric) {
        System.out.println(`"Recording Metric: `" + metric.metricName() + `" = `" + metric.value());
        // Save to timeseries DB / PostgreSQL
    }
    
    public java.util.List<MetricDto> aggregateMetrics(String timeframe) {
        return java.util.List.of(
            new MetricDto(`"TotalSearches`", 5432.0, timeframe)
        );
    }
"@
    }
    "prediction" = @{
        Dtos = @{
            "TrendForecastDto" = "public record TrendForecastDto(String topic, double probability, String timeframe) {}"
        }
        ServiceMethods = @"
    // Business Rule: Historical data analysis, Trend prediction
    public TrendForecastDto forecastTopic(String topic) {
        // Mock ML model analysis
        double calculatedProbability = 0.85; 
        System.out.println(`"Forecasting trend for topic: `" + topic);
        return new TrendForecastDto(topic, calculatedProbability, `"Next 6 Months`");
    }
"@
    }
}

foreach ($modName in $modulesLogic.Keys) {
    $modConfig = $modulesLogic[$modName]
    $pkgName = $modName.Replace("-", "")
    $basePath = "$modName-module/src/main/java/com/university/$pkgName"
    $classNamePrefix = (Get-Culture).TextInfo.ToTitleCase($pkgName)
    
    # Ensure DTO directory exists
    New-Item -ItemType Directory -Force -Path "$basePath/dto" | Out-Null
    
    # 1. Generate Java 25 Record DTOs
    foreach ($dtoName in $modConfig.Dtos.Keys) {
        $dtoBody = $modConfig.Dtos[$dtoName]
        
        # Check if DTO needs imports
        if ($dtoBody.StartsWith("import")) {
            $parts = $dtoBody -split "`n", 2
            $imports = $parts[0]
            $body = $parts[1]
            $dtoContent = @"
package com.university.$pkgName.dto;

$imports

$body
"@
        } else {
            $dtoContent = @"
package com.university.$pkgName.dto;

$dtoBody
"@
        }
        Set-Content -Path "$basePath/dto/${dtoName}.java" -Value $dtoContent
    }

    # 2. Update Service with Business Logic and DTO usage
    $serviceContent = @"
package com.university.$pkgName.service;

import com.university.$pkgName.repository.${classNamePrefix}Repository;
import com.university.$pkgName.entity.${classNamePrefix}Entity;
import com.university.$pkgName.dto.*;
import org.springframework.stereotype.Service;

@Service
public class ${classNamePrefix}Service {
    private final ${classNamePrefix}Repository repository;

    public ${classNamePrefix}Service(${classNamePrefix}Repository repository) {
        this.repository = repository;
    }
    
$($modConfig.ServiceMethods)
}
"@
    Set-Content -Path "$basePath/service/${classNamePrefix}Service.java" -Value $serviceContent

    Write-Host "Injected Business Logic & DTO Records into: $modName"
}
