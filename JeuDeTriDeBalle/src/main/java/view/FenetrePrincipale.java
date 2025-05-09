package view;

import controler.Controleur;
import model.Jeu;
import model.Tube;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import model.Boule;

public class FenetrePrincipale extends JFrame {
    private static final int LARGEUR_FENETRE = 800;
    private static final int HAUTEUR_FENETRE = 600;

    private JPanel panneauPrincipal;
    private JButton btnRecommencer;
    private JLabel lblCoupsRestants;

    private Jeu jeu;
    private Controleur controleur;

   // Constructeur sans contrôleur
    public FenetrePrincipale(Jeu jeu) {
        this.jeu = jeu;
        this.controleur = null; // Initialisé temporairement
        initialiserFenetre();
        ajouterComposants();
        configurerEvenements();
    }

    // Setter pour le contrôleur
    public void setControleur(Controleur controleur) {
        this.controleur = controleur;
    }


    private void initialiserFenetre() {
        setTitle("Jeu de Tri de Balle");
        setSize(LARGEUR_FENETRE, HAUTEUR_FENETRE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    }

    private void ajouterComposants() {
        panneauPrincipal = new JPanel();
        panneauPrincipal.setLayout(new GridLayout(1, jeu.getTubes().size()));
        add(panneauPrincipal, BorderLayout.CENTER);

        for (Tube tube : jeu.getTubes()) {
            panneauPrincipal.add(new TubePanel(tube));
        }

        JPanel panneauBas = new JPanel();
        panneauBas.setLayout(new FlowLayout());

        btnRecommencer = new JButton("Recommencer");
        lblCoupsRestants = new JLabel("Coups restants: " + jeu.getCoupsRestants());

        panneauBas.add(btnRecommencer);
        panneauBas.add(lblCoupsRestants);
        add(panneauBas, BorderLayout.SOUTH);
    }

    private void configurerEvenements() {
        btnRecommencer.addActionListener(e -> {
            controleur.recommencerPartie();
            repaint();
        });
    }

    public void mettreAJourCoups(int coups) {
        lblCoupsRestants.setText("Coups restants: " + coups);
    }

    private class TubePanel extends JPanel {
        private Tube tube;

        public TubePanel(Tube tube) {
            this.tube = tube;
            setPreferredSize(new Dimension(100, 300));
            setBorder(BorderFactory.createLineBorder(Color.BLACK));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    controleur.selectionnerTube(tube);
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            dessinerBoules(g);
        }

        private void dessinerBoules(Graphics g) {
            int diametre = 50;
            int espace = 10;
            int y = 0;

            for (Boule boule : tube.getBoules()) {
                g.setColor(boule.getCouleur());
                g.fillOval(espace, y, diametre, diametre);
                y += diametre + espace;
            }
        }
    }

   public static void main(String[] args) {
    // 1. Modèle
    Jeu jeu = new Jeu(5, 5);

    // 2. Vue
    FenetrePrincipale vue = new FenetrePrincipale(jeu);

    // 3. Contrôleur
    Controleur controleur = new Controleur(jeu, vue);

    // 4. Relier la vue au contrôleur
    vue.setControleur(controleur);

    // 5. Afficher
    vue.setVisible(true);
}
}