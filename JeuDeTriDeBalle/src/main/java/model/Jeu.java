package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Jeu {
    private List<Tube> tubes;
    private int coupsRestants;

    public Jeu(int nombreTubes, int boulesParTube) {
        tubes = new ArrayList<>();
        genererTubes(nombreTubes, boulesParTube);
        coupsRestants = 100; // Ou une autre valeur
    }

    private void genererTubes(int nombreTubes, int boulesParTube) {
        Random rand = new Random();
        List<Color> couleurs = List.of(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW);

        List<Boule> toutesLesBilles = new ArrayList<>();
        for (int i = 0; i < nombreTubes * boulesParTube; i++) {
            Boule boule = new Boule(couleurs.get(rand.nextInt(couleurs.size())));
            toutesLesBilles.add(boule);
        }
        Collections.shuffle(toutesLesBilles);

        for (int i = 0; i < nombreTubes; i++) {
            Tube tube = new Tube();
            for (int j = 0; j < boulesParTube; j++) {
                tube.ajouterBoule(toutesLesBilles.remove(0));
            }
            tubes.add(tube);
        }
    }

    public boolean deplacerBoule(Tube source, Tube dest) {
        if (source.estVide()) {
            return false; // Impossible de déplacer si source vide
        }
        Boule boule = source.retirerDerniereBoule();
        dest.ajouterBoule(boule);
        return true;
    }

    public boolean deplacerToutesLesBillesDeMemeCouleur(Tube source, Tube dest, Color couleur) {
        return source.deplacerToutesLesBillesDeMemeCouleur(dest, couleur);
    }

    public void reinitialiser() {
        tubes.clear();
        genererTubes(tubes.size(), 6); // Génère 6 billes par tube
        coupsRestants = 100; // Réinitialise les coups restants
    }

    public List<Tube> getTubes() {
        return tubes;
    }

    public int getCoupsRestants() {
        return coupsRestants;
    }

    public void decrementerCoups() {
        coupsRestants--;
    }
}