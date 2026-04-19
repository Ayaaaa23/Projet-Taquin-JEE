package com.puzzle.taquin.repository;

import com.puzzle.taquin.model.Joueur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoueurRepository extends JpaRepository<Joueur, Long> {
    Joueur findByNom(String nom);
}