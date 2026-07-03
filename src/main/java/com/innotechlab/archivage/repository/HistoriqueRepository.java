package com.innotechlab.archivage.repository;

import com.innotechlab.archivage.entity.Historique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoriqueRepository extends JpaRepository<Historique, Long> {
    List<Historique> findTop20ByOrderByDateActionDesc();
    List<Historique> findByUtilisateurId(Long utilisateurId);
}