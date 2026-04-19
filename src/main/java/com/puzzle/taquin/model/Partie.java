package com.puzzle.taquin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Partie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int niveau;
    private int score;
    @Column(columnDefinition = "TEXT")
    private String grilleJson;
    private boolean terminee;
    private LocalDateTime dateSauvegarde;
    
    @ManyToOne
    @JoinColumn(name = "joueur_id")
    private Joueur joueur;
    
    public Partie() {}
    
    // Getters et Setters 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getNiveau() { return niveau; }
    public void setNiveau(int niveau) { this.niveau = niveau; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public String getGrilleJson() { return grilleJson; }
    public void setGrilleJson(String grilleJson) { this.grilleJson = grilleJson; }
    public boolean isTerminee() { return terminee; }
    public void setTerminee(boolean terminee) { this.terminee = terminee; }
    public LocalDateTime getDateSauvegarde() { return dateSauvegarde; }
    public void setDateSauvegarde(LocalDateTime dateSauvegarde) { this.dateSauvegarde = dateSauvegarde; }
    public Joueur getJoueur() { return joueur; }
    public void setJoueur(Joueur joueur) { this.joueur = joueur; }
}