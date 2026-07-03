package com.innotechlab.archivage.service;

import com.innotechlab.archivage.entity.Utilisateur;
import com.innotechlab.archivage.repository.UtilisateurRepository;
import com.innotechlab.archivage.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public String connecter(String email, String motPasse) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect"));

        if (!utilisateur.getActif()) {
            throw new RuntimeException("Compte desactive");
        }

        if (!passwordEncoder.matches(motPasse, utilisateur.getMotPasse())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        return jwtUtils.genererToken(utilisateur.getEmail(), utilisateur.getRole().name());
    }

    public void changerMotPasse(String email, String ancienMotPasse, String nouveauMotPasse) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!passwordEncoder.matches(ancienMotPasse, utilisateur.getMotPasse())) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }

        utilisateur.setMotPasse(passwordEncoder.encode(nouveauMotPasse));
        utilisateurRepository.save(utilisateur);
    }
}