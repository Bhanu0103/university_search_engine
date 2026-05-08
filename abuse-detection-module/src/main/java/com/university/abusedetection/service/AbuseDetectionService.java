package com.university.abusedetection.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AbuseDetectionService {
    private static final Logger logger = LoggerFactory.getLogger(AbuseDetectionService.class);
    private final List<String> flaggedUsers = new ArrayList<>();

    public boolean isAbusive(String query) {
        // Simple Rule: If query is longer than 50 chars, mark as suspicious/abusive
        if (query != null && query.length() > 50) {
            logger.warn("Abuse detected: query is too long ({} chars)", query.length());
            return true;
        }
        return false;
    }

    public AbuseDecision detectSearchAbuse(String userId, String ipAddress) {
        if (isBlank(userId)) {
            return new AbuseDecision(true, "Missing user id");
        }
        if (isBlank(ipAddress)) {
            return new AbuseDecision(true, "Missing IP address");
        }
        if (ipAddress.startsWith("0.") || ipAddress.startsWith("127.")) {
            return new AbuseDecision(true, "Suspicious or local-only IP address");
        }
        return new AbuseDecision(false, "No abuse detected for user " + userId + " from IP " + ipAddress);
    }

    public String analyzeQueryPatterns(String timeRange) {
        String range = isBlank(timeRange) ? "all" : timeRange.trim();
        return "{\"timeRange\":\"" + escape(range) + "\","
                + "\"rule\":\"query_length_and_identity_checks\","
                + "\"flaggedUsers\":" + flaggedUsers.size() + ","
                + "\"summary\":\"Abuse checks are active for missing identity, suspicious IP, and long search queries.\"}";
    }

    public String flagSuspiciousUser(String userId, String reason) {
        String safeUserId = isBlank(userId) ? "unknown" : userId.trim();
        String safeReason = isBlank(reason) ? "No reason provided" : reason.trim();
        String report = "Flagged suspicious user " + safeUserId + ": " + safeReason;
        flaggedUsers.add(report);
        logger.warn("{}", report);
        return report;
    }

    public List<String> getFraudReports(String startDate, String endDate) {
        if (flaggedUsers.isEmpty()) {
            return List.of("No fraud reports found from " + defaultText(startDate, "beginning")
                    + " to " + defaultText(endDate, "now"));
        }
        return List.copyOf(flaggedUsers);
    }

    private String defaultText(String value, String fallback) {
        return isBlank(value) ? fallback : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public record AbuseDecision(boolean abusive, String reason) {}
}
