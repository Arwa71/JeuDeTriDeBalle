/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Stack;

/**
 *
 * @author Surface
 */
public class Tube {
    private Stack<Boule> boules;
    private final int capacite = 4;

    public Tube() {
        boules = new Stack<>();
    }

    public boolean estVide() {
        return boules.isEmpty();
    }

    public boolean estPlein() {
        return boules.size() == capacite;
    }

    public Boule sommet() {
        if (!estVide()) {
            return boules.peek();
        }
        return null;
    }

    public boolean peutRecevoir(Boule b) {
        return estVide() || (!estPlein() && sommet().getCouleur().equals(b.getCouleur()));
    }

    public boolean ajouterBoule(Boule b) {
        if (peutRecevoir(b)) {
            boules.push(b);
            return true;
        }
        return false;
    }

    public Boule retirerBoule() {
        if (!estVide()) {
            return boules.pop();
        }
        return null;
    }

    public Stack<Boule> getBoules() {
        return boules;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("| ");
        for (int i = boules.size() - 1; i >= 0; i--) {
            sb.append(boules.get(i).getCouleur()).append(" | ");
        }
        return sb.toString();
    }
    
}
