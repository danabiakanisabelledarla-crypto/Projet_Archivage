package com.innotechlab.archivage.service;

import com.innotechlab.archivage.entity.Document;
import com.innotechlab.archivage.entity.Document.Statut;
import com.innotechlab.archivage.entity.Historique;
import com.innotechlab.archivage.entity.Utilisateur;
import com.innotechlab.archivage.repository.DocumentRepository;
import com.innotechlab.archivage.repository.HistoriqueRepository;
import com.innotechlab.archivage.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final HistoriqueRepository historiqueRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public List<Document> listerActifs() {
        return documentRepository.findByStatut(Statut.ACTIF);
    }

    public List<Document> rechercher(String mot) {
        return documentRepository.rechercherParMotCle(mot);
    }

    public Document trouverParId(Long id) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document introuvable"));
        doc.setNbConsultations(doc.getNbConsultations() + 1);
        return documentRepository.save(doc);
    }

    public Document deposer(MultipartFile fichier, String titre,
                            String description, Long categorieId,
                            String emailUtilisateur) throws IOException {

        Utilisateur utilisateur = utilisateurRepository.findByEmail(emailUtilisateur)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Path dossier = Paths.get(uploadDir);
        if (!Files.exists(dossier)) Files.createDirectories(dossier);

        String nomUnique = UUID.randomUUID() + "_" + fichier.getOriginalFilename();
        Path chemin = dossier.resolve(nomUnique);
        Files.copy(fichier.getInputStream(), chemin, StandardCopyOption.REPLACE_EXISTING);

        Document doc = new Document();
        doc.setTitre(titre);
        doc.setDescription(description);
        doc.setNomFichier(fichier.getOriginalFilename());
        doc.setTypeFichier(fichier.getContentType());
        doc.setTailleFichier(fichier.getSize());
        doc.setCheminStockage(nomUnique);
        doc.setDateDepot(LocalDateTime.now());
        doc.setUtilisateur(utilisateur);
        doc.setStatut(Statut.ACTIF);
        doc.setNbConsultations(0);
        doc.setNbTelechargements(0);

        Document saved = documentRepository.save(doc);

        enregistrerHistorique("DEPOT", utilisateur, saved, "Depot du document : " + titre);

        return saved;
    }

    public void supprimer(Long id, String emailUtilisateur) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document introuvable"));
        Utilisateur utilisateur = utilisateurRepository.findByEmail(emailUtilisateur)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        doc.setStatut(Statut.SUPPRIME);
        documentRepository.save(doc);
        enregistrerHistorique("SUPPRESSION", utilisateur, doc,
                "Suppression du document : " + doc.getTitre());
    }

    public Path obtenirCheminFichier(Long id) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document introuvable"));
        doc.setNbTelechargements(doc.getNbTelechargements() + 1);
        documentRepository.save(doc);
        return Paths.get(uploadDir).resolve(doc.getCheminStockage());
    }

    private void enregistrerHistorique(String action, Utilisateur utilisateur,
                                        Document document, String details) {
        Historique h = new Historique();
        h.setAction(action);
        h.setDateAction(LocalDateTime.now());
        h.setDetails(details);
        h.setUtilisateur(utilisateur);
        h.setDocument(document);
        historiqueRepository.save(h);
    }

    public long compterParStatut(Statut statut) {
        return documentRepository.countByStatut(statut);
    }
}