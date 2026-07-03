package com.innotechlab.archivage.controller;

import com.innotechlab.archivage.entity.Document;
import com.innotechlab.archivage.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping
    public ResponseEntity<List<Document>> listerActifs() {
        return ResponseEntity.ok(documentService.listerActifs());
    }

    @GetMapping("/recherche")
    public ResponseEntity<List<Document>> rechercher(@RequestParam String mot) {
        return ResponseEntity.ok(documentService.rechercher(mot));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> trouverParId(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.trouverParId(id));
    }

    @PostMapping
    public ResponseEntity<?> deposer(
            @RequestParam("fichier") MultipartFile fichier,
            @RequestParam("titre") String titre,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "categorieId", required = false) Long categorieId,
            Authentication authentication) {
        try {
            Document doc = documentService.deposer(
                fichier, titre, description, categorieId,
                authentication.getName()
            );
            return ResponseEntity.ok(doc);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("erreur", "Erreur lors du dépôt du fichier"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimer(@PathVariable Long id,
                                        Authentication authentication) {
        documentService.supprimer(id, authentication.getName());
        return ResponseEntity.ok(Map.of("message", "Document supprimé"));
    }

    @GetMapping("/{id}/telecharger")
    public ResponseEntity<Resource> telecharger(@PathVariable Long id)
            throws MalformedURLException {
        Path chemin = documentService.obtenirCheminFichier(id);
        Resource resource = new UrlResource(chemin.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + chemin.getFileName() + "\"")
                .body(resource);
    }

    @GetMapping("/statistiques")
    public ResponseEntity<?> statistiques() {
        return ResponseEntity.ok(Map.of(
            "actifs",   documentService.compterParStatut(Document.Statut.ACTIF),
            "archives", documentService.compterParStatut(Document.Statut.ARCHIVE),
            "supprimes",documentService.compterParStatut(Document.Statut.SUPPRIME)
        ));
    }
}