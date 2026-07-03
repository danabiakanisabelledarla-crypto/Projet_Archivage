package com.innotechlab.archivage.service;

import com.innotechlab.archivage.entity.Utilisateur;
import com.innotechlab.archivage.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Utilisateur> listerTous() {
        return utilisateurRepository.findAll();
    }

    public Utilisateur trouverParId(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    public Utilisateur creer(Utilisateur utilisateur) {
        if (utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
            throw new RuntimeException("Cet email est deja utilise");
        }
        utilisateur.setMotPasse(passwordEncoder.encode(utilisateur.getMotPasse()));
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur modifier(Long id, Utilisateur data) {
        Utilisateur utilisateur = trouverParId(id);
        utilisateur.setNom(data.getNom());
        utilisateur.setPrenom(data.getPrenom());
        utilisateur.setRole(data.getRole());
        return utilisateurRepository.save(utilisateur);
    }

    public void activerDesactiver(Long id) {
        Utilisateur utilisateur = trouverParId(id);
        utilisateur.setActif(!utilisateur.getActif());
        utilisateurRepository.save(utilisateur);
    }
}