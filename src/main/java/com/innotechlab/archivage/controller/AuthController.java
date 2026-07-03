package com.innotechlab.archivage.controller;

import com.innotechlab.archivage.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/connexion")
    public ResponseEntity<?> connexion(@RequestBody Map<String, String> body) {
        try {
            String token = authService.connecter(
                body.get("email"),
                body.get("motPasse")
            );
            return ResponseEntity.ok(Map.of(
                "token", token,
                "message", "Connexion réussie"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur", e.getMessage()));
        }
    }

    @PostMapping("/changer-mot-passe")
    public ResponseEntity<?> changerMotPasse(@RequestBody Map<String, String> body,
                                              @RequestAttribute("email") String email) {
        try {
            authService.changerMotPasse(
                email,
                body.get("ancienMotPasse"),
                body.get("nouveauMotPasse")
            );
            return ResponseEntity.ok(Map.of("message", "Mot de passe modifié avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erreur", e.getMessage()));
        }
    }
}