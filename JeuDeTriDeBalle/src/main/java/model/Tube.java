/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Tube {
    private List<Boule> billes;

    public Tube() {
        this.billes = new ArrayList<>();
    }

    // Ajoute une bille sans vérifier la capacité
    public void ajouterBoule(Boule boule) {
        billes.add(boule);
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

    // Vérifie si le tube est vide
    public boolean estVide() {
        return billes.isEmpty();
    }

    // Méthode ajoutée pour déplacer toutes les billes de même couleur
    public boolean deplacerToutesLesBillesDeMemeCouleur(Tube dest, Color couleur) {
        List<Boule> billesADeplacer = new ArrayList<>();

        // Collecter toutes les billes de la couleur spécifiée dans le tube source
        for (int i = billes.size() - 1; i >= 0; i--) {
            Boule boule = billes.get(i);
            if (boule.getCouleur().equals(couleur)) {
                billesADeplacer.add(boule);
            } else {
                break; // Arrête dès qu'on trouve une bille d'une autre couleur
            }
        }

        // Retirer les billes du tube source
        billes.removeAll(billesADeplacer);

        // Ajouter les billes au tube destination
        dest.getBoules().addAll(billesADeplacer);

        return !billesADeplacer.isEmpty(); // Vrai si au moins une bille a été déplacée
    }

    public int getTailleMax() {
        return  billes.size() ;
    }
}