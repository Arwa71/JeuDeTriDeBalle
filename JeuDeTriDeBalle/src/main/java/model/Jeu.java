package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Jeu {
    private List<Tube> tubes;
    private int coupsRestants;
    private int nombreTubes;
    private int boulesParTube;
    private int nombreTubesVides = 2; // Nombre de tubes vides à ajouter

    public Jeu(int nombreTubesRemplis, int boulesParTube) {
        this.nombreTubes = nombreTubesRemplis;
        this.boulesParTube = boulesParTube;
        tubes = new ArrayList<>();
        genererTubes(nombreTubes, boulesParTube);
        coupsRestants = 100; // Ou une autre valeur
    }

    private void genererTubes(int nombreTubesRemplis, int boulesParTube) {
        tubes.clear();
        Random rand = new Random();
        // Utiliser autant de couleurs que de tubes remplis
        List<Color> couleursDisponibles = List.of(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.PINK);
        int nbCouleurs = Math.min(nombreTubesRemplis, couleursDisponibles.size());
        List<Color> couleurs = couleursDisponibles.subList(0, nbCouleurs);

        List<Boule> toutesLesBilles = new ArrayList<>();
        // Pour chaque couleur, créer exactement boulesParTube boules
        for (Color couleur : couleurs) {
            for (int i = 0; i < boulesParTube; i++) {
                toutesLesBilles.add(new Boule(couleur));
            }
        }
        Collections.shuffle(toutesLesBilles);

        // Créer les tubes remplis (un par couleur)
        for (int i = 0; i < nbCouleurs; i++) {
            Tube tube = new Tube(boulesParTube);
            for (int j = 0; j < boulesParTube; j++) {
                tube.ajouterBoule(toutesLesBilles.remove(0));
            }
            tubes.add(tube);
        }
        // Ajouter les tubes vides
        for (int i = 0; i < nombreTubesVides; i++) {
            tubes.add(new Tube(boulesParTube));
        }
    }

    public boolean deplacerBoule(Tube source, Tube dest) {
        if (source.estVide() || dest.estPlein() || source == dest) {
            return false; // Impossible de déplacer si source vide, dest plein ou même tube
        }
        Boule boule = source.getDerniereBoule();
        // Vérifier la couleur du dessus du tube destination
        if (!dest.estVide()) {
            Boule topDest = dest.getDerniereBoule();
            if (!topDest.getCouleur().equals(boule.getCouleur())) {
                return false;
            }
        }
        // Déplacer la boule
        boule = source.retirerDerniereBoule();
        dest.ajouterBoule(boule);
        return true;
    }

    public boolean deplacerToutesLesBillesDeMemeCouleur(Tube source, Tube dest, Color couleur) {
        return source.deplacerToutesLesBillesDeMemeCouleur(dest, couleur);
    }

    public void reinitialiser() {
        genererTubes(nombreTubes, boulesParTube);
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

    // Vérifie si le jeu est terminé : tous les tubes non vides sont pleins et uniformes
    public boolean estTermine() {
        for (Tube tube : tubes) {
            if (!tube.estVide() && !tube.estUniformeEtRempli()) {
                return false;
            }
        }
        return true;
    }
}