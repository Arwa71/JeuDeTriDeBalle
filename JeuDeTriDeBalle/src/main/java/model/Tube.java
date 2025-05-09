/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Tube {
    private List<Boule> boules;
    private boolean estVide;

    public Tube() {
        this.boules = new ArrayList<>();
        this.estVide = true;
    }

    public boolean ajouterBoule(Boule boule) {
        if (estVide || (boules.size() > 0 && boule.getCouleur().equals(boules.get(0).getCouleur()))) {
            boules.add(boule);
            estVide = false;
            return true;
        }
        return false;
    }

    public Boule retirerBoule() {
        if (!estVide) {
            return boules.remove(boules.size() - 1);
        }
        return null;
    }

    public boolean estPlein() {
        return boules.size() == 10; // Exemple : 4 boules par tube
    }

    public boolean estTrie() {
        if (boules.isEmpty()) return true;
        Color couleur = boules.get(0).getCouleur();
        for (Boule b : boules) {
            if (!b.getCouleur().equals(couleur)) return false;
        }
        return true;
    }

    public List<Boule> getBoules() {
        return boules;
    }

    public boolean estVide() {
        return estVide;
    }
}