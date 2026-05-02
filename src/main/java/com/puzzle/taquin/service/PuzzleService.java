package com.puzzle.taquin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PuzzleService {

    public int[][] genererGrilleMelangee(int taille) {
        int[][] grille = grilleSolution(taille);
        Random rand = new Random();
        int videLig = taille - 1;
        int videCol = taille - 1;
        int nbMouvements = 200 + rand.nextInt(100);
        for (int i = 0; i < nbMouvements; i++) {
            List<int[]> voisins = getVoisins(videLig, videCol, taille);
            int[] choix = voisins.get(rand.nextInt(voisins.size()));
            int temp = grille[choix[0]][choix[1]];
            grille[choix[0]][choix[1]] = grille[videLig][videCol];
            grille[videLig][videCol] = temp;
            videLig = choix[0];
            videCol = choix[1];
        }
        return grille;
    }

    private int[][] grilleSolution(int taille) {
    int[][] grille = new int[taille][taille];
    for (int i = 0; i < taille; i++) {
        for (int j = 0; j < taille; j++) {
            // Au lieu de i*taille + j, on utilise j*taille + i
            grille[i][j] = j * taille + i;
        }
    }
    return grille;
}

    private List<int[]> getVoisins(int lig, int col, int taille) {
        List<int[]> voisins = new ArrayList<>();
        if (lig > 0) voisins.add(new int[]{lig-1, col});
        if (lig < taille-1) voisins.add(new int[]{lig+1, col});
        if (col > 0) voisins.add(new int[]{lig, col-1});
        if (col < taille-1) voisins.add(new int[]{lig, col+1});
        return voisins;
    }

    public boolean estMouvementValide(int[][] grille, int ligClique, int colClique, int taille) {
        int videLig = -1, videCol = -1;
        int valeurVide = taille * taille - 1;
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (grille[i][j] == valeurVide) {
                    videLig = i;
                    videCol = j;
                    break;
                }
            }
        }
        return (Math.abs(ligClique - videLig) + Math.abs(colClique - videCol)) == 1;
    }

    public void deplacer(int[][] grille, int ligClique, int colClique, int taille) {
        int videLig = -1, videCol = -1;
        int valeurVide = taille * taille - 1;
        for (int i = 0; i < taille; i++) {
            for (int j = 0; j < taille; j++) {
                if (grille[i][j] == valeurVide) {
                    videLig = i;
                    videCol = j;
                    break;
                }
            }
        }
        int temp = grille[ligClique][colClique];
        grille[ligClique][colClique] = grille[videLig][videCol];
        grille[videLig][videCol] = temp;
    }

    public boolean estGagnee(int[][] grille, int taille) {
    for (int i = 0; i < taille; i++) {
        for (int j = 0; j < taille; j++) {
            // Compare avec la même transposition
            if (grille[i][j] != j * taille + i) {
                return false;
            }
        }
    }
    return true;
}

    public String grilleToJson(int[][] grille) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(grille);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public int[][] jsonToGrille(String json, int taille) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, int[][].class);
        } catch (JsonProcessingException e) {
            return genererGrilleMelangee(taille);
        }
    }
}