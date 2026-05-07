package com.university.abusedetection.service;

import org.springframework.stereotype.Service;

@Service
public class AbuseDetectionService {

    public boolean isAbusive(String query) {
        // Simple Rule: If query is longer than 50 chars, mark as suspicious/abusive
        if (query != null && query.length() > 50) {
            System.err.println("🚨 ABUSE DETECTED: Query is too long! (" + query.length() + " chars)");
            return true;
        }
        return false;
    }
}
