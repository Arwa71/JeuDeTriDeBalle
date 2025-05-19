/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Tube {
    private List<Boule> billes;
    private int capaciteMax;

    public Tube(int capaciteMax) {
        this.billes = new ArrayList<>();
        this.capaciteMax = capaciteMax;
    }

    // Pour compatibilité avec l'ancien constructeur
    public Tube() {
        this(6); // Valeur par défaut, à ajuster selon le jeu
    }

    // Ajoute une bille si la capacité n'est pas atteinte
    public boolean ajouterBoule(Boule boule) {
        if (billes.size() < capaciteMax) {
            billes.add(boule);
            return true;
        }
        return false;
    }

    // Retire la dernière bille du tube
    public Boule retirerDerniereBoule() {
        if (billes.isEmpty()) {
            return null;
        }
        return billes.remove(billes.size() - 1);
    }

    // Retourne toutes les billes du tube
    public List<Boule> getBoules() {
        return billes;
    }

    // Retourne la dernière boule (haut du tube)
    public Boule getDerniereBoule() {
        if (billes.isEmpty()) return null;
        return billes.get(billes.size() - 1);
    }

    // Vérifie si le tube est vide
    public boolean estVide() {
        return billes.isEmpty();
    }

    // Vérifie si le tube est plein
    public boolean estPlein() {
        return billes.size() >= capaciteMax;
    }

    // Vérifie si le tube est plein et toutes les billes sont de la même couleur
    public boolean estUniformeEtRempli() {
        if (billes.size() != capaciteMax) return false;
        if (billes.isEmpty()) return false;
        Color couleur = billes.get(0).getCouleur();
        for (Boule b : billes) {
            if (!b.getCouleur().equals(couleur)) return false;
        }
        return true;
    }

    // Déplace toutes les billes du haut de la même couleur d'un coup (pas une par une)
    public boolean deplacerToutesLesBillesDeMemeCouleur(Tube dest, Color couleur) {
        if (this == dest) return false;
        if (dest.estPlein()) return false;

        // Compter combien de billes consécutives du haut sont de la couleur demandée
        int count = 0;
        for (int i = billes.size() - 1; i >= 0; i--) {
            Boule boule = billes.get(i);
            if (boule.getCouleur().equals(couleur)) {
                count++;
            } else {
                break;
            }
        }
        if (count == 0) return false;

        // Vérifier la couleur du dessus du tube destination
        if (!dest.estVide()) {
            Boule topDest = dest.getDerniereBoule();
            if (!topDest.getCouleur().equals(couleur)) {
                return false;
            }
        }
        // Vérifier la capacité
        if (dest.capaciteMax - dest.billes.size() < count) return false;

        // Déplacer toutes les billes d'un coup (en conservant l'ordre)
        List<Boule> aDeplacer = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            aDeplacer.add(0, billes.remove(billes.size() - 1));
        }
        dest.billes.addAll(aDeplacer);
        return true;
    }

    public int getTailleMax() {
        return capaciteMax;
    }
}