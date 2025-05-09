package controler;

import model.Jeu;
import model.Tube;
import view.FenetrePrincipale;

import java.sql.*;
import java.util.List;

public class Controleur {
    private Jeu jeu;
    private FenetrePrincipale vue;

    public Controleur(Jeu jeu, FenetrePrincipale vue) {
        this.jeu = jeu;
        this.vue = vue;
    }

    public void selectionnerTube(Tube tube) {
        if (jeu.estVictoire()) return; // Ne rien faire si la partie est gagnée

        // Logique de déplacement (exemple simple)
        // À améliorer avec une sélection de deux tubes consécutifs
        Tube tubeSource = tube;
        Tube tubeDestination = rechercherTubeVide();

        if (tubeDestination != null) {
            jeu.deplacerBoule(tubeSource, tubeDestination);
            vue.mettreAJourCoups(jeu.getCoupsRestants());
            vue.repaint();
        }
    }

    private Tube rechercherTubeVide() {
        for (Tube t : jeu.getTubes()) {
            if (t.estVide()) return t;
        }
        return null;
    }

    public void sauvegarderScore(String pseudo, int coups) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ball_sort", 
                "root", 
                "")) {
            String sql = "INSERT INTO scores (pseudo, coups) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, pseudo);
            stmt.setInt(2, coups);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void recommencerPartie() {
        jeu.reinitialiser();
        vue.mettreAJourCoups(jeu.getCoupsRestants());
        vue.repaint();
    }
}