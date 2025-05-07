/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Surface
 */
public class Jeu {
    private ArrayList<Tube> tubes;

    public Jeu(int nbCouleurs, int nbTubesVides) {
        tubes = new ArrayList<>();

        // Créer les tubes
        ArrayList<Boule> toutesLesBoules = new ArrayList<>();
        String[] couleurs = {"Rouge", "Bleu", "Vert", "Jaune", "Orange", "Violet"};

        for (int i = 0; i < nbCouleurs; i++) {
            for (int j = 0; j < 4; j++) {
                toutesLesBoules.add(new Boule(couleurs[i]));
            }
        }

        Collections.shuffle(toutesLesBoules); // Mélanger les boules

        // Remplir les tubes avec les boules
        int index = 0;
        for (int i = 0; i < nbCouleurs; i++) {
            Tube t = new Tube();
            for (int j = 0; j < 4; j++) {
                t.ajouterBoule(toutesLesBoules.get(index++));
            }
            tubes.add(t);
        }

        // Ajouter les tubes vides
        for (int i = 0; i < nbTubesVides; i++) {
            tubes.add(new Tube());
        }
    }

    public boolean deplacer(int from, int to) {
        if (from < 0 || to < 0 || from >= tubes.size() || to >= tubes.size()) return false;

        Tube source = tubes.get(from);
        Tube destination = tubes.get(to);

        if (source.estVide()) return false;

        Boule b = source.sommet();
        if (destination.peutRecevoir(b)) {
            destination.ajouterBoule(source.retirerBoule());
            return true;
        }
        return false;
    }

    public boolean estGagne() {
        for (Tube t : tubes) {
            if (!t.estVide()) {
                String couleur = null;
                for (Boule b : t.getBoules()) {
                    if (couleur == null) couleur = b.getCouleur();
                    else if (!b.getCouleur().equals(couleur)) return false;
                }
                if (t.getBoules().size() < 4) return false;
            }
        }
        return true;
    }

    public ArrayList<Tube> getTubes() {
        return tubes;
    }

    public void afficher() {
        System.out.println("\n--- État des tubes ---");
        for (int i = 0; i < tubes.size(); i++) {
            System.out.println("Tube " + i + ": " + tubes.get(i));
        }
    }
    
}
