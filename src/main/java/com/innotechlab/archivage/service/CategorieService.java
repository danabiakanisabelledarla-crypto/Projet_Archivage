package com.innotechlab.archivage.service;

import com.innotechlab.archivage.entity.Categorie;
import com.innotechlab.archivage.repository.CategorieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategorieService {

    private final CategorieRepository categorieRepository;

    public List<Categorie> listerToutes() {
        return categorieRepository.findAll();
    }

    public Categorie trouverParId(Long id) {
        return categorieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categorie introuvable"));
    }

    public Categorie creer(Categorie categorie) {
        if (categorieRepository.existsByNom(categorie.getNom())) {
            throw new RuntimeException("Cette categorie existe deja");
        }
        return categorieRepository.save(categorie);
    }

    public Categorie modifier(Long id, Categorie data) {
        Categorie categorie = trouverParId(id);
        categorie.setNom(data.getNom());
        categorie.setDescription(data.getDescription());
        return categorieRepository.save(categorie);
    }

    public void supprimer(Long id) {
        categorieRepository.deleteById(id);
    }
}