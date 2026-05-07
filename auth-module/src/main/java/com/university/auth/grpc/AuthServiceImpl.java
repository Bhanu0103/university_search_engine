package com.university.auth.grpc;

import com.university.auth.dto.AuthRecord;
import com.university.auth.dto.RegisterRecord;
import com.university.auth.service.AuthService;
import com.university.common.enums.Role;
import com.university.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {

    private final AuthService service;

    public AuthServiceImpl(AuthService service) {
        this.service = service;
    }

    @Override
    public void login(LoginRequest request, StreamObserver<TokenResponse> responseObserver) {
        // Now using getEmail() from the proto request
        String token = service.authenticate(new AuthRecord(request.getEmail(), request.getPassword()));
        responseObserver.onNext(TokenResponse.newBuilder().setAccessToken(token).build());
        responseObserver.onCompleted();
    }

    @Override
    public void register(RegisterRequest request, StreamObserver<StatusResponse> responseObserver) {
        try {
            Role role = Role.valueOf(request.getRole().toUpperCase());
            service.register(new RegisterRecord(request.getUsername(), request.getPassword(), request.getEmail(), role));
            responseObserver.onNext(StatusResponse.newBuilder().setSuccess(true).setMessage("User registered").build());
        } catch (Exception e) {
            responseObserver.onNext(StatusResponse.newBuilder().setSuccess(false).setMessage(e.getMessage()).build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void validateToken(TokenValidationRequest request, StreamObserver<ValidationResponse> responseObserver) {
        boolean valid = service.validate(request.getToken());
        ValidationResponse.Builder response = ValidationResponse.newBuilder().setIsValid(valid);
        try {
            if (valid) {
            var user = service.getUserFromToken(request.getToken());
            response.setUserId(String.valueOf(user.getId()));
            response.setRole(user.getRole().name());
            }
        } catch (Exception e) {
            response.setIsValid(false);
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void refreshToken(RefreshTokenRequest request, StreamObserver<TokenResponse> responseObserver) {
        responseObserver.onNext(TokenResponse.newBuilder().setAccessToken("refreshed-jwt-token").build());
        responseObserver.onCompleted();
    }

    @Override
    public void logout(LogoutRequest request, StreamObserver<StatusResponse> responseObserver) {
        responseObserver.onNext(StatusResponse.newBuilder().setSuccess(true).setMessage("Logged out").build());
        responseObserver.onCompleted();
    }
}
