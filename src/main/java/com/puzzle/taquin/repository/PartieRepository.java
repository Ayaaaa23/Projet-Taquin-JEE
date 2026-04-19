package com.puzzle.taquin.repository;

import com.puzzle.taquin.model.Partie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PartieRepository extends JpaRepository<Partie, Long> {
    List<Partie> findByJoueurIdAndTermineeFalse(Long joueurId);
}