package com.innotechlab.archivage.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "archive")
public class Archive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_archivage", updatable = false)
    private LocalDateTime dateArchivage;

    @Column(columnDefinition = "TEXT")
    private String motif;

    @Column(name = "date_restauration")
    private LocalDateTime dateRestauration;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false, unique = true)
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archive_par", nullable = false)
    private Utilisateur archivePar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaure_par")
    private Utilisateur restaurePar;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDateArchivage() { return dateArchivage; }
    public void setDateArchivage(LocalDateTime dateArchivage) { this.dateArchivage = dateArchivage; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public LocalDateTime getDateRestauration() { return dateRestauration; }
    public void setDateRestauration(LocalDateTime dateRestauration) { this.dateRestauration = dateRestauration; }

    public Document getDocument() { return document; }
    public void setDocument(Document document) { this.document = document; }

    public Utilisateur getArchivePar() { return archivePar; }
    public void setArchivePar(Utilisateur archivePar) { this.archivePar = archivePar; }

    public Utilisateur getRestaurePar() { return restaurePar; }
    public void setRestaurePar(Utilisateur restaurePar) { this.restaurePar = restaurePar; }
}