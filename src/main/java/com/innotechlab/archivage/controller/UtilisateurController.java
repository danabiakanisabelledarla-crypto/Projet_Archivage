package com.innotechlab.archivage.controller;

import com.innotechlab.archivage.entity.Utilisateur;
import com.innotechlab.archivage.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @GetMapping
    public ResponseEntity<List<Utilisateur>> listerTous() {
        return ResponseEntity.ok(utilisateurService.listerTous());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> trouverParId(@PathVariable Long id) {
        return ResponseEntity.ok(utilisateurService.trouverParId(id));
    }

    @PostMapping
    public ResponseEntity<Utilisateur> creer(@RequestBody Utilisateur utilisateur) {
        return ResponseEntity.ok(utilisateurService.creer(utilisateur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> modifier(@PathVariable Long id,
                                                 @RequestBody Utilisateur data) {
        return ResponseEntity.ok(utilisateurService.modifier(id, data));
    }

    @PutMapping("/{id}/activer")
    public ResponseEntity<?> activerDesactiver(@PathVariable Long id) {
        utilisateurService.activerDesactiver(id);
        return ResponseEntity.ok(Map.of("message", "Statut modifié avec succès"));
    }
}