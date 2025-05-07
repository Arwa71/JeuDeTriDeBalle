/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controler;

import model.Jeu;
import model.Tube;

/**
 *
 * @author Surface
 */


public class Controleur {
    private Jeu jeu;

    public Controleur(Jeu jeu) {
        this.jeu = jeu;
    }

    public void recommencerPartie() {
        // À implémenter plus tard si nécessaire
        System.out.println("Recommencer la partie !");
    }

    public void selectionnerTube(Tube tube) {
        // Ici, tu gères la logique de sélection/déplacement
        System.out.println("Tube sélectionné !");
    }
}
