package com.university.auth.security;

import com.university.auth.service.JwtService;
import com.university.common.enums.Role;
import com.university.common.repository.UserRepository;
import io.grpc.*;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.EnumSet;
import java.util.Set;

@GrpcGlobalServerInterceptor
public class JwtServerInterceptor implements ServerInterceptor {

    private static final Metadata.Key<String> AUTHORIZATION_KEY = 
            Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public JwtServerInterceptor(JwtService jwtService, UserDetailsService userDetailsService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        
        String fullToken = headers.get(AUTHORIZATION_KEY);
        
        // Skip security for public methods (Login, Register, Reflection, Health)
        String methodName = call.getMethodDescriptor().getFullMethodName().toLowerCase();
        if (methodName.contains("login") ||
            methodName.contains("register") ||
            methodName.contains("validatetoken") ||
            methodName.contains("reflection") ||
            methodName.contains("health")) {
            return next.startCall(call, headers);
        }

        if (fullToken == null || !fullToken.startsWith("Bearer ")) {
            call.close(Status.UNAUTHENTICATED.withDescription("Authorization token is missing"), new Metadata());
            return new ServerCall.Listener<ReqT>() {};
        }

        String token = fullToken.substring(7);
        try {
            if (jwtService.validateToken(token)) {
                String username = jwtService.getUsernameFromToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                Role role = userRepository.findByEmail(username)
                        .orElseThrow(() -> new IllegalArgumentException("User not found for token"))
                        .getRole();

                Set<Role> allowedRoles = allowedRolesFor(methodName);
                if (!allowedRoles.contains(role) && role != Role.ADMIN) {
                    call.close(Status.PERMISSION_DENIED.withDescription(
                            "Role " + role + " is not allowed to call " + call.getMethodDescriptor().getFullMethodName()), new Metadata());
                    return new ServerCall.Listener<ReqT>() {};
                }

                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return next.startCall(call, headers);
            } else {
                call.close(Status.UNAUTHENTICATED.withDescription("Token validation failed in JwtService"), new Metadata());
                return new ServerCall.Listener<ReqT>() {};
            }
        } catch (Exception e) {
            call.close(Status.UNAUTHENTICATED.withDescription("Auth Error: " + e.getMessage()), new Metadata());
            return new ServerCall.Listener<ReqT>() {};
        }
    }

    private Set<Role> allowedRolesFor(String methodName) {
        if (methodName.contains("accesscontrolservice")) {
            return EnumSet.of(Role.ADMIN);
        }

        if (methodName.contains("authservice/refreshtoken")
                || methodName.contains("authservice/logout")) {
            return EnumSet.allOf(Role.class);
        }

        if (methodName.contains("ingestservice")
                || methodName.contains("documentservice")) {
            return EnumSet.of(Role.FACULTY, Role.RESEARCHER);
        }

        if (methodName.contains("rankingservice")
                || methodName.contains("analyticsservice")
                || methodName.contains("predictionservice")) {
            return EnumSet.of(Role.FACULTY, Role.RESEARCHER);
        }

        if (methodName.contains("queryoptimizationservice")
                || methodName.contains("searchservice")
                || methodName.contains("filterservice")
                || methodName.contains("personalizationservice")
                || methodName.contains("notificationservice")) {
            return EnumSet.of(Role.STUDENT, Role.FACULTY, Role.RESEARCHER);
        }

        return EnumSet.allOf(Role.class);
    }
}
