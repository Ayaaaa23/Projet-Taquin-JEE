package com.puzzle.taquin.controller;

import com.puzzle.taquin.model.Joueur;
import com.puzzle.taquin.model.Partie;
import com.puzzle.taquin.repository.JoueurRepository;
import com.puzzle.taquin.repository.PartieRepository;
import com.puzzle.taquin.service.PuzzleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class PuzzleController {

    @Autowired
    private PuzzleService puzzleService;
    @Autowired
    private JoueurRepository joueurRepository;
    @Autowired
    private PartieRepository partieRepository;

    @GetMapping("/")
    public String accueil() {
        return "accueil";
    }

    @PostMapping("/connexion")
    public String connexion(@RequestParam String nom, HttpSession session) {
        Joueur joueur = joueurRepository.findByNom(nom);
        if (joueur == null) {
            joueur = new Joueur(nom);
            joueurRepository.save(joueur);
        }
        session.setAttribute("joueurId", joueur.getId());
        return "redirect:/menu";
    }

    @GetMapping("/menu")
    public String menu(HttpSession session, Model model) {
        Long joueurId = (Long) session.getAttribute("joueurId");
        if (joueurId == null) return "redirect:/";
        model.addAttribute("parties", partieRepository.findByJoueurIdAndTermineeFalse(joueurId));
        return "menu";
    }

    @GetMapping("/nouvelle-partie")
    public String nouvellePartie(@RequestParam int niveau, HttpSession session) {
        Long joueurId = (Long) session.getAttribute("joueurId");
        if (joueurId == null) return "redirect:/";
        Joueur joueur = joueurRepository.findById(joueurId).orElseThrow();

        int taille = niveau == 1 ? 3 : (niveau == 2 ? 4 : 5);
        int[][] grille = puzzleService.genererGrilleMelangee(taille);
        Partie partie = new Partie();
        partie.setNiveau(niveau);
        partie.setScore(0);
        partie.setGrilleJson(puzzleService.grilleToJson(grille));
        partie.setTerminee(false);
        partie.setDateSauvegarde(LocalDateTime.now());
        partie.setJoueur(joueur);
        partieRepository.save(partie);
        return "redirect:/jeu?partieId=" + partie.getId();
    }

    @GetMapping("/jeu")
    public String afficherJeu(@RequestParam Long partieId, HttpSession session, Model model) {
        Long joueurId = (Long) session.getAttribute("joueurId");
        if (joueurId == null) return "redirect:/";
        Partie partie = partieRepository.findById(partieId).orElseThrow();
        if (!partie.getJoueur().getId().equals(joueurId)) return "redirect:/menu";

        int taille = partie.getNiveau() == 1 ? 3 : (partie.getNiveau() == 2 ? 4 : 5);
        int[][] grille = puzzleService.jsonToGrille(partie.getGrilleJson(), taille);
        model.addAttribute("partie", partie);
        model.addAttribute("grille", grille);
        model.addAttribute("taille", taille);
        model.addAttribute("videIndex", taille * taille - 1);
        return "jeu";
    }

    @PostMapping("/deplacer")
    public String deplacer(@RequestParam Long partieId,
                           @RequestParam int row,
                           @RequestParam int col,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        Long joueurId = (Long) session.getAttribute("joueurId");
        if (joueurId == null) return "redirect:/";
        Partie partie = partieRepository.findById(partieId).orElseThrow();
        if (partie.isTerminee()) return "redirect:/menu";

        int taille = partie.getNiveau() == 1 ? 3 : (partie.getNiveau() == 2 ? 4 : 5);
        int[][] grille = puzzleService.jsonToGrille(partie.getGrilleJson(), taille);

        if (puzzleService.estMouvementValide(grille, row, col, taille)) {
            puzzleService.deplacer(grille, row, col, taille);
            partie.setScore(partie.getScore() + 1);
            partie.setGrilleJson(puzzleService.grilleToJson(grille));
            if (puzzleService.estGagnee(grille, taille)) {
                partie.setTerminee(true);
                redirectAttributes.addFlashAttribute("message", "Félicitations ! Vous avez gagné en " + partie.getScore() + " mouvements !");
            }
            partie.setDateSauvegarde(LocalDateTime.now());
            partieRepository.save(partie);
        }
        return "redirect:/jeu?partieId=" + partieId;
    }

    @PostMapping("/sauvegarder")
public String sauvegarder(@RequestParam Long partieId, HttpSession session) {
    Long joueurId = (Long) session.getAttribute("joueurId");
    if (joueurId == null) return "redirect:/";
    Partie partie = partieRepository.findById(partieId).orElseThrow();
    if (!partie.getJoueur().getId().equals(joueurId)) return "redirect:/menu";
    
    // Met à jour la date de sauvegarde (et force la sauvegarde en base)
    partie.setDateSauvegarde(LocalDateTime.now());
    partieRepository.save(partie);
    
    return "redirect:/jeu?partieId=" + partieId;
}
    @PostMapping("/rejouer")
public String rejouer(@RequestParam Long partieId, HttpSession session) {
    Long joueurId = (Long) session.getAttribute("joueurId");
    if (joueurId == null) return "redirect:/";
    
    Partie partie = partieRepository.findById(partieId).orElseThrow();
    if (!partie.getJoueur().getId().equals(joueurId)) return "redirect:/menu";
    
    // pour Réinitialiser la grille (mélanger)
    int taille = partie.getNiveau() == 1 ? 3 : (partie.getNiveau() == 2 ? 4 : 5);
    int[][] nouvelleGrille = puzzleService.genererGrilleMelangee(taille);
    partie.setGrilleJson(puzzleService.grilleToJson(nouvelleGrille));
    partie.setScore(0);
    partie.setTerminee(false);
    partie.setDateSauvegarde(LocalDateTime.now());
    partieRepository.save(partie);
    
    return "redirect:/jeu?partieId=" + partieId;
}
}