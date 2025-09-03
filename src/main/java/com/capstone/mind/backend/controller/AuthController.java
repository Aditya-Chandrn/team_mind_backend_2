//package com.capstone.mind.backend.controller;
//
///*import com.example.auth.dto.SignupRequest;
//import com.example.auth.dto.LoginRequest;
//import com.example.auth.dto.ApiResponse;
//import com.example.auth.service.AuthService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    private final AuthService authService;
//
//    public AuthController(AuthService authService) {
//        this.authService = authService;
//    }
//
//    @PostMapping("/signup")
//    public ResponseEntity<ApiResponse> signup(@RequestBody SignupRequest request) {
//        String message = authService.signup(
//                request.getFirstName(),
//                request.getLastName(),
//                request.getEmail(),
//                request.getPassword()
//        );
//        return ResponseEntity.ok(new ApiResponse(message));
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
//        String token = authService.login(request.getEmail(), request.getPassword());
//        return ResponseEntity.ok(new ApiResponse(token));
//    }
//}*/
//
//
//import com.capstone.mind.backend.services.AuthService_Arpita;
//import com.capstone.mind.backend.util.TokenStore;
//import com.capstone.mind.backend.util.JwtUtil;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//    private final AuthService_Arpita authService;
//    private final TokenStore tokenStore;
//    private final JwtUtil jwtUtil;
//
//    public AuthController(AuthService_Arpita authService, TokenStore tokenStore, JwtUtil jwtUtil) {
//        this.authService = authService;
//        this.tokenStore = tokenStore;
//        this.jwtUtil = jwtUtil;
//    }
//
//    @PostMapping("/signup")
//    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
//        try {
//            String firstName = body.get("firstName");
//            String lastName = body.get("lastName");
//            String email = body.get("email");
//            String encryptedPassword = body.get("password");
//
//            String message = authService.signup(firstName, lastName, email, encryptedPassword);
//            return ResponseEntity.ok(Map.of("message", message));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
//        try {
//            String email = body.get("email");
//            String encryptedPassword = body.get("password");
//
//            String token = authService.login(email, encryptedPassword);
//            return ResponseEntity.ok(Map.of("token", token));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    @GetMapping("/auth")
//    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
//        }
//
//        String token = authHeader.substring(7);
//        try {
//            String email = jwtUtil.validateToken(token);
//            if (tokenStore.validateToken(token)) {
//                return ResponseEntity.ok(Map.of("email", email));
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalidated");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
//        }
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
//        }
//
//        String token = authHeader.substring(7);
//        tokenStore.removeToken(token);
//        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
//    }
//}


