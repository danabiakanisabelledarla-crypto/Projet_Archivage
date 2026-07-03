package com.innotechlab.archivage.controller;

import com.innotechlab.archivage.entity.Categorie;
import com.innotechlab.archivage.service.CategorieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategorieController {

    private final CategorieService categorieService;

    @GetMapping
    public ResponseEntity<List<Categorie>> listerToutes() {
        return ResponseEntity.ok(categorieService.listerToutes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categorie> trouverParId(@PathVariable Long id) {
        return ResponseEntity.ok(categorieService.trouverParId(id));
    }

    @PostMapping
    public ResponseEntity<Categorie> creer(@RequestBody Categorie categorie) {
        return ResponseEntity.ok(categorieService.creer(categorie));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categorie> modifier(@PathVariable Long id,
                                               @RequestBody Categorie data) {
        return ResponseEntity.ok(categorieService.modifier(id, data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimer(@PathVariable Long id) {
        categorieService.supprimer(id);
        return ResponseEntity.ok(Map.of("message", "Catégorie supprimée"));
    }
}