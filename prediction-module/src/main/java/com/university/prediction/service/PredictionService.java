package com.university.prediction.service;

import com.university.prediction.repository.PredictionRepository;
import com.university.prediction.dto.PredictRecord;
import com.university.prediction.dto.PredictionResultDto;
import com.university.common.repository.UserRepository;
import com.university.accesscontrol.service.AccesscontrolService;
import org.springframework.stereotype.Service;

@Service
public class PredictionService {
    private final PredictionRepository predictionRepository;
    private final UserRepository userRepository;
    private final AccesscontrolService accessControlService;

    public PredictionService(PredictionRepository predictionRepository,
                             UserRepository userRepository,
                             AccesscontrolService accessControlService) {
        this.predictionRepository = predictionRepository;
        this.userRepository = userRepository;
        this.accessControlService = accessControlService;
    }

    /**
     * Simple prediction stub – only users with ADMIN role are allowed.
     */
    public PredictionResultDto predict(PredictRecord record) {
        var userOpt = userRepository.findById(Long.valueOf(record.userId()));
        if (userOpt.isEmpty() || !userOpt.get().getRole().name().equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("Access denied: only ADMIN can invoke predictions");
        }
        // In a real system you would run a model here. We'll just echo back the data.
        System.out.println("Predicting for user " + record.userId() + ": " + record.data());
        return new PredictionResultDto("Prediction successful for " + record.data());
    }
}

//import com.university.prediction.repository.PredictionRepository;
//import com.university.prediction.entity.PredictionEntity;
//import com.university.prediction.dto.*;
//import org.springframework.stereotype.Service;
//@Service
//public class PredictionService {
//    private final PredictionRepository repository;
//    public PredictionService(PredictionRepository repository) { this.repository = repository; }
//        public void predict(PredictRecord record) { System.out.println("Predicting " + record.data()); }
//}
