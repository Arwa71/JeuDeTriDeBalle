package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jeu {
    private List<Tube> tubes;
    private int coupsRestants;
    private boolean victoire;

    public Jeu(int nombreTubes, int boulesParTube) {
        tubes = new ArrayList<>();
        genererTubes(nombreTubes, boulesParTube);
        coupsRestants = 10; // Exemple de coups restants
        victoire = false;
    }

    private void genererTubes(int nombreTubes, int boulesParTube) {
        Random rand = new Random();
        List<Color> couleurs = List.of(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW);

        for (int i = 0; i < nombreTubes; i++) {
            Tube tube = new Tube();
            for (int j = 0; j < boulesParTube; j++) {
                Color couleur = couleurs.get(rand.nextInt(couleurs.size()));
                tube.ajouterBoule(new Boule(couleur));
            }
            tubes.add(tube);
        }
    }

    public boolean deplacerBoule(Tube source, Tube destination) {
        Boule boule = source.retirerBoule();
        if (destination.ajouterBoule(boule)) {
            coupsRestants--;
            verifierVictoire();
            return true;
        }
        source.ajouterBoule(boule); // Annuler le déplacement invalide
        return false;
    }

    private void verifierVictoire() {
        for (Tube tube : tubes) {
            if (!tube.estTrie()) {
                victoire = false;
                return;
            }
        }
        victoire = true;
    }

    public List<Tube> getTubes() {
        return tubes;
    }

    public int getCoupsRestants() {
        return coupsRestants;
    }

    public boolean estVictoire() {
        return victoire;
    }

    public void reinitialiser() {
        genererTubes(tubes.size(), tubes.get(0).getBoules().size());
        coupsRestants = 3;
        victoire = false;
    }
}
