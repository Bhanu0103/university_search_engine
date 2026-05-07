package com.university.accesscontrol.grpc;

import com.university.accesscontrol.dto.AccessRequestDto;
import com.university.accesscontrol.dto.PolicyRecord;
import com.university.accesscontrol.service.AccesscontrolService;
import com.university.grpc.AccessValidationRequest;
import com.university.grpc.AccessValidationResponse;
import com.university.grpc.PermissionRequest;
import com.university.grpc.PolicyRequest;
import com.university.grpc.RevokeRequest;
import com.university.grpc.StatusResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import java.util.List;

@GrpcService
public class AccessControlServiceImpl extends com.university.grpc.AccessControlServiceGrpc.AccessControlServiceImplBase {
    private final AccesscontrolService service;

    public AccessControlServiceImpl(AccesscontrolService service) {
        this.service = service;
    }

    @Override
    public void defineAccessPolicy(PolicyRequest request, StreamObserver<StatusResponse> responseObserver) {
        try {
            String role = readString(request.getPolicyJson(), "role");
            List<String> resources = readArray(request.getPolicyJson(), "resources");
            if (resources.isEmpty()) {
                String resource = readString(request.getPolicyJson(), "resource");
                resources = resource.isBlank() ? List.of() : List.of(resource);
            }

            List<String> roles = role.isBlank() ? readArray(request.getPolicyJson(), "roles") : List.of(role);
            if (roles.isEmpty()) {
                throw new IllegalArgumentException("Policy must include role or roles");
            }
            for (String policyRole : roles) {
                service.defineAccessPolicy(new PolicyRecord(policyRole, resources));
            }
            sendStatus(responseObserver, true, "Access policy stored for roles: " + String.join(",", roles));
        } catch (Exception e) {
            sendStatus(responseObserver, false, e.getMessage());
        }
    }

    @Override
    public void assignPermissions(PermissionRequest request, StreamObserver<StatusResponse> responseObserver) {
        try {
            if (request.getRolesList().isEmpty()) {
                throw new IllegalArgumentException("At least one role is required");
            }
            service.assignPermissions(new AccessRequestDto(request.getUserId(), request.getRoles(0), ""));
            sendStatus(responseObserver, true, "Assigned role " + request.getRoles(0) + " to user " + request.getUserId());
        } catch (Exception e) {
            sendStatus(responseObserver, false, e.getMessage());
        }
    }

    @Override
    public void validateAccess(AccessValidationRequest request, StreamObserver<AccessValidationResponse> responseObserver) {
        boolean allowed = service.validateAccess(new AccessRequestDto(request.getUserId(), "", request.getResourceId()));
        responseObserver.onNext(AccessValidationResponse.newBuilder().setIsAllowed(allowed).build());
        responseObserver.onCompleted();
    }

    @Override
    public void revokeAccess(RevokeRequest request, StreamObserver<StatusResponse> responseObserver) {
        try {
            service.revokeAccess(new AccessRequestDto(request.getUserId(), request.getRole(), ""));
            sendStatus(responseObserver, true, "Revoked role for user " + request.getUserId());
        } catch (Exception e) {
            sendStatus(responseObserver, false, e.getMessage());
        }
    }

    private void sendStatus(StreamObserver<StatusResponse> responseObserver, boolean success, String message) {
        responseObserver.onNext(StatusResponse.newBuilder().setSuccess(success).setMessage(message).build());
        responseObserver.onCompleted();
    }

    private String readString(String json, String field) {
        java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("\"" + java.util.regex.Pattern.quote(field) + "\"\\s*:\\s*\"([^\"]*)\"")
                .matcher(json == null ? "" : json);
        return matcher.find() ? matcher.group(1) : "";
    }

    private List<String> readArray(String json, String field) {
        java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("\"" + java.util.regex.Pattern.quote(field) + "\"\\s*:\\s*\\[(.*?)]")
                .matcher(json == null ? "" : json);
        if (!matcher.find()) {
            return List.of();
        }
        return java.util.Arrays.stream(matcher.group(1).split(","))
                .map(value -> value.replace("\"", "").trim())
                .filter(value -> !value.isBlank())
                .toList();
    }
}
