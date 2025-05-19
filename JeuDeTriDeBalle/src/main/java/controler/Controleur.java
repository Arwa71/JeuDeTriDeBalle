package controler;

import model.Boule;
import model.Jeu;
import model.Tube;
import view.FenetrePrincipale;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Jeu;
import model.Tube;
import view.FenetrePrincipale;

public class Controleur {
    private Jeu jeu;
    private FenetrePrincipale vue;

    private Tube tubeSelectionne = null;

    public Controleur(Jeu jeu, FenetrePrincipale vue) {
        this.jeu = jeu;
        this.vue = vue;
    }

    public void selectionnerTube(Tube tube) {
        if (tubeSelectionne == null) {
            tubeSelectionne = tube; // Premier clic : sélectionne la source
        } else {
            // Deuxième clic : tente de déplacer
            if (jeu.deplacerBoule(tubeSelectionne, tube)) {
                tubeSelectionne = null; // Réinitialise la sélection

                vue.rechargerTubes(); // Rafraîchit l'interface
            } else {
                tubeSelectionne = null; // Annule la sélection si échec
            }
        }
    }

    public void deplacerBillesDeMemeCouleur(Tube tube) {
        if (tubeSelectionne == null) {
            tubeSelectionne = tube; // Premier clic : sélectionne la source
        } else {
            List<Boule> boulesSource = tube.getBoules();
            if (!boulesSource.isEmpty()) {
                Color couleur = boulesSource.get(boulesSource.size() - 1).getCouleur();

                // Déplacer toutes les billes de cette couleur
                if (jeu.deplacerToutesLesBillesDeMemeCouleur(tube, tubeSelectionne, couleur)) {
                    tubeSelectionne = null;
                    vue.rechargerTubes();
                } else {
                    tubeSelectionne = null; // Annule la sélection si échec
                }
            }
        }
    }

    public void recommencerPartie() {
        jeu.reinitialiser();
        vue.rechargerTubes();
    }
}