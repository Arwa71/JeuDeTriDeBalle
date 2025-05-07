/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author Surface
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Jeu;
import controler.Controleur;
import model.Boule;
import model.Tube;

public class FenetrePrincipale extends JFrame {
    private static final int LARGEUR_FENETRE = 800;
    private static final int HAUTEUR_FENETRE = 600;
    
    private JPanel panneauPrincipale;
    private JButton btnRecommencer;
    private JLabel lblCoupsRestants;
    
    // Liens avec le modèle et le contrôleur
    private Jeu jeu; // Classe du modèle
    private Controleur controleur; // Classe du contrôleur

    public FenetrePrincipale(Jeu jeu, Controleur controleur) {
        this.jeu = jeu;
        this.controleur = controleur;
        initialiserFenetre();
        ajouterComposants();
        configurerEvenements();
    }

    private void initialiserFenetre() {
        setTitle("Jeu de Tri de Balle");
        setSize(LARGEUR_FENETRE, HAUTEUR_FENETRE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Layout principal
        setLocationRelativeTo(null); // Centrer la fenêtre
    }

    private void ajouterComposants() {
        // Panneau principal pour les tubes
        panneauPrincipale = new JPanel();
        panneauPrincipale.setLayout(new GridLayout(1, jeu.getNombreTubes())); // 1 ligne, autant de tubes que nécessaire
        add(panneauPrincipale, BorderLayout.CENTER);

        // Ajouter les tubes
        for (Tube tube : jeu.getTubes()) {
            panneauPrincipale.add(new TubePanel(tube));
        }

        // Panneau pour les boutons et informations
        JPanel panneauBas = new JPanel();
        panneauBas.setLayout(new FlowLayout());

        btnRecommencer = new JButton("Recommencer");
        lblCoupsRestants = new JLabel("Coups restants: " + jeu.getCoupsRestants());

        panneauBas.add(btnRecommencer);
        panneauBas.add(lblCoupsRestants);
        add(panneauBas, BorderLayout.SOUTH);
    }

    private void configurerEvenements() {
        btnRecommencer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controleur.recommencerPartie();
                // Mettre à jour l'affichage
                repaint();
            }
        });
    }

    // Méthode pour mettre à jour l'affichage des coups restants
    public void mettreAJourCoups(int coupsRestants) {
        lblCoupsRestants.setText("Coups restants: " + coupsRestants);
    }

    // Classe interne pour représenter un tube graphique
    private class TubePanel extends JPanel {
        private Tube tube;

        public TubePanel(Tube tube) {
            this.tube = tube;
            setPreferredSize(new Dimension(100, 300)); // Largeur et hauteur du tube
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            // Gestion du clic sur le tube (ex. pour déplacer une boule)
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    controleur.selectionnerTube(tube); // Notifier le contrôleur
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            dessinerBoules(g);
        }

        private void dessinerBoules(Graphics g) {
    int diametreBoule = 50;
    int espace = 10;
    int y = 0;

    for (Boule boule : tube.getBoules()) {
        String couleurNom = boule.getCouleur();
        Color couleur;

        switch (couleurNom.toLowerCase()) {
            case "red":
                couleur = Color.RED;
                break;
            case "green":
                couleur = Color.GREEN;
                break;
            case "blue":
                couleur = Color.BLUE;
                break;
            case "yellow":
                couleur = Color.YELLOW;
                break;
            case "orange":
                couleur = Color.ORANGE;
                break;
            case "pink":
                couleur = Color.PINK;
                break;
            case "cyan":
                couleur = Color.CYAN;
                break;
            case "magenta":
                couleur = Color.MAGENTA;
                break;
            case "gray":
                couleur = Color.GRAY;
                break;
            default:
                couleur = Color.BLACK; // Couleur par défaut si inconnue
        }

        g.setColor(couleur);
        g.fillOval(espace, y, diametreBoule, diametreBoule);
        y += diametreBoule + espace;
    }
}
    }

    public static void main(String[] args) {
        // Exemple d'initialisation (à adapter avec le modèle réel)
        Jeu jeu = new Jeu(5,3); // Exemple avec 5 tubes
        Controleur controleur = new Controleur(jeu);
        new FenetrePrincipale(jeu, controleur).setVisible(true);
    }
}
