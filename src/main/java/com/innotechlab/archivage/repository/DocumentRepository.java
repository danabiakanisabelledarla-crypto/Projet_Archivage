package com.innotechlab.archivage.repository;

import com.innotechlab.archivage.entity.Document;
import com.innotechlab.archivage.entity.Document.Statut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByStatut(Statut statut);

    List<Document> findByCategorieId(Long categorieId);

    List<Document> findByUtilisateurId(Long utilisateurId);

    @Query("SELECT d FROM Document d WHERE " +
           "LOWER(d.titre) LIKE LOWER(CONCAT('%', :mot, '%')) " +
           "OR LOWER(d.description) LIKE LOWER(CONCAT('%', :mot, '%'))")
    List<Document> rechercherParMotCle(String mot);

    long countByStatut(Statut statut);
}