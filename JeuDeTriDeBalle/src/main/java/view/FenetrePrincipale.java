package view;

import controler.Controleur;
import model.Jeu;
import model.Tube;
import model.Boule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class FenetrePrincipale extends JFrame {
    private static final int LARGEUR_FENETRE = 1000;
    private static final int HAUTEUR_FENETRE = 700;

    // Couleurs du thème moderne
    private static final Color COULEUR_FOND = new Color(13, 17, 23);
    private static final Color COULEUR_SURFACE = new Color(22, 27, 34);
    private static final Color COULEUR_ACCENT = new Color(56, 211, 255);
    private static final Color COULEUR_ACCENT_HOVER = new Color(79, 172, 254);
    private static final Color COULEUR_TEXTE = new Color(201, 209, 217);
    private static final Color COULEUR_BORDURE = new Color(48, 54, 61);

    private JPanel panneauPrincipal;
    private ModernButton btnRecommencer;
    private JLabel lblTitre;
    private Jeu jeu;
    private Controleur controleur;
    private Tube tubeSelectionne;

    public FenetrePrincipale(Jeu jeu) {
        this.jeu = jeu;
        this.controleur = null;
        this.tubeSelectionne = null;
        initialiserFenetre();
        ajouterComposants();
        configurerEvenements();
    }

    public void setControleur(Controleur controleur) {
        this.controleur = controleur;
    }

    private void initialiserFenetre() {
        setTitle("Ball Sort Puzzle - Modern UI");
        setSize(LARGEUR_FENETRE, HAUTEUR_FENETRE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        getContentPane().setBackground(COULEUR_FOND);

        // Icône moderne pour la fenêtre
        try {
            setIconImage(createModernIcon());
        } catch (Exception e) {
            // Icône par défaut si erreur
        }
    }

    private Image createModernIcon() {
        int size = 32;
        BufferedImage icon = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fond circulaire
        g2d.setColor(COULEUR_ACCENT);
        g2d.fillOval(2, 2, size - 4, size - 4);

        // Formes géométriques modernes
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawRoundRect(8, 12, 6, 12, 2, 2);
        g2d.drawRoundRect(18, 8, 6, 16, 2, 2);

        g2d.dispose();
        return icon;
    }

    private void ajouterComposants() {
        // En-tête moderne
        ajouterEnTete();
        // Zone de jeu principale
        ajouterZoneJeu();
        // Panneau de contrôles en bas
        ajouterPanneauControles();
    }

    private void ajouterEnTete() {
        JPanel panneauEnTete = new JPanel(new BorderLayout());
        panneauEnTete.setBackground(COULEUR_SURFACE);
        panneauEnTete.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COULEUR_BORDURE),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        // Titre avec style moderne
        lblTitre = new JLabel("Ball Sort Puzzle");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitre.setForeground(COULEUR_TEXTE);
        panneauEnTete.add(lblTitre, BorderLayout.WEST);

        add(panneauEnTete, BorderLayout.NORTH);
    }

    private void ajouterZoneJeu() {
        JPanel conteneurJeu = new JPanel(new BorderLayout());
        conteneurJeu.setBackground(COULEUR_FOND);
        conteneurJeu.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        panneauPrincipal = new JPanel();
        panneauPrincipal.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panneauPrincipal.setBackground(COULEUR_FOND);

        for (Tube tube : jeu.getTubes()) {
            ModernTubePanel panel = new ModernTubePanel(tube);
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        controleur.selectionnerTube(tube);
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        controleur.deplacerBillesDeMemeCouleur(tube);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    panel.setHover(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    panel.setHover(false);
                }
            });
            panneauPrincipal.add(panel);
        }

        JScrollPane scrollPane = new JScrollPane(panneauPrincipal);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(COULEUR_FOND);

        // Style moderne pour la scrollbar
        scrollPane.getHorizontalScrollBar().setUI(new ModernScrollBarUI());

        conteneurJeu.add(scrollPane, BorderLayout.CENTER);
        add(conteneurJeu, BorderLayout.CENTER);
    }

    private void ajouterPanneauControles() {
        JPanel panneauBas = new JPanel(new FlowLayout());
        panneauBas.setBackground(COULEUR_SURFACE);
        panneauBas.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, COULEUR_BORDURE),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        btnRecommencer = new ModernButton("Recommencer");
        btnRecommencer.setPreferredSize(new Dimension(150, 40));
        panneauBas.add(btnRecommencer);

        add(panneauBas, BorderLayout.SOUTH);
    }

    private void configurerEvenements() {
        btnRecommencer.addActionListener(e -> {
            if (controleur != null) {
                controleur.recommencerPartie();
            }
        });
    }

    public void setTubeSelectionne(Tube tube) {
        this.tubeSelectionne = tube;
        repaint();
    }

    public void rechargerTubes() {
        panneauPrincipal.removeAll();
        for (Tube tube : jeu.getTubes()) {
            ModernTubePanel panel = new ModernTubePanel(tube);
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        controleur.selectionnerTube(tube);
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        controleur.deplacerBillesDeMemeCouleur(tube);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    panel.setHover(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    panel.setHover(false);
                }
            });
            panneauPrincipal.add(panel);
        }
        panneauPrincipal.revalidate();
        panneauPrincipal.repaint();
    }

    // Classe pour tube panel moderne
    private class ModernTubePanel extends JPanel {
        private Tube tube;
        private boolean isHover = false;
        private boolean isSelected = false;

        public ModernTubePanel(Tube tube) {
            this.tube = tube;
            setPreferredSize(new Dimension(100, 600)); // Tubes plus grands
            setBackground(COULEUR_FOND);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        public void setHover(boolean hover) {
            this.isHover = hover;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            isSelected = (tube == tubeSelectionne);

            // Effet d'ombre portée
            if (isHover || isSelected) {
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 15, 15);
            }

            // Contour du tube avec gradient
            GradientPaint gradient = new GradientPaint(
                    0, 0, isSelected ? COULEUR_ACCENT : COULEUR_SURFACE,
                    0, getHeight(), isSelected ? COULEUR_ACCENT.darker() : COULEUR_SURFACE.darker()
            );
            g2d.setPaint(gradient);
            g2d.fillRoundRect(10, 20, getWidth() - 20, getHeight() - 40, 12, 12);

            // Bordure brillante
            if (isSelected) {
                g2d.setColor(COULEUR_ACCENT);
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(10, 20, getWidth() - 20, getHeight() - 40, 12, 12);
            } else {
                g2d.setColor(isHover ? COULEUR_BORDURE.brighter() : COULEUR_BORDURE);
                g2d.setStroke(new BasicStroke(1f));
                g2d.drawRoundRect(10, 20, getWidth() - 20, getHeight() - 40, 12, 12);
            }

            // Dessiner les boules avec effet 3D
            dessinerBoulesModernes(g2d);
            g2d.dispose();
        }

        private void dessinerBoulesModernes(Graphics2D g2d) {
            int diametre = 60; // Billes plus grandes
            int espace = 10; // Espace entre les billes plus grand
            int x = (getWidth() - diametre) / 2;
            int yStart = getHeight() - 40; // Position de départ ajustée
            java.util.List<Boule> boules = tube.getBoules();
            for (int i = 0; i < boules.size(); i++) {
                Boule boule = boules.get(i);
                int y = yStart - (i * (diametre + espace));

                // Ombre de la boule
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillOval(x + 2, y + 2, diametre, diametre);

                // Gradient pour effet 3D
                Color couleurBoule = boule.getCouleur();
                GradientPaint gradientBoule = new GradientPaint(
                        x, y, couleurBoule.brighter(),
                        x + diametre, y + diametre, couleurBoule.darker()
                );
                g2d.setPaint(gradientBoule);
                g2d.fillOval(x, y, diametre, diametre);

                // Reflet brillant
                g2d.setColor(new Color(255, 255, 255, 80));
                g2d.fillOval(x + 10, y + 10, diametre / 3, diametre / 3);

                // Contour subtil
                g2d.setColor(couleurBoule.darker().darker());
                g2d.setStroke(new BasicStroke(1f));
                g2d.drawOval(x, y, diametre, diametre);
            }
        }

        private Color getBouleSurLaquelleOnClique(int x, int y) {
            int diametre = 60; // Billes plus grandes
            int espace = 10; // Espace entre les billes plus grand
            int xBoule = (getWidth() - diametre) / 2;
            int yStart = getHeight() - 40; // Position de départ ajustée
            java.util.List<Boule> boules = tube.getBoules();
            for (int i = 0; i < boules.size(); i++) {
                int yBoule = yStart - (i * (diametre + espace));
                if (x >= xBoule && x <= xBoule + diametre &&
                        y >= yBoule && y <= yBoule + diametre) {
                    return boules.get(i).getCouleur();
                }
            }
            return null;
        }
    }

    // Classe pour bouton moderne
    private class ModernButton extends JButton {
        private boolean isHovered = false;

        public ModernButton(String text) {
            super(text);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(Color.WHITE);
            setBorder(null);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Fond du bouton avec gradient
            GradientPaint gradient = new GradientPaint(
                    0, 0, isHovered ? COULEUR_ACCENT_HOVER : COULEUR_ACCENT,
                    0, getHeight(), isHovered ? COULEUR_ACCENT_HOVER.darker() : COULEUR_ACCENT.darker()
            );
            g2d.setPaint(gradient);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

            // Effet de surbrillance
            if (isHovered) {
                g2d.setColor(new Color(255, 255, 255, 20));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            }

            g2d.dispose();
            super.paintComponent(g);
        }
    }

    // Style moderne pour scrollbar
    private class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = COULEUR_BORDURE;
            this.thumbDarkShadowColor = COULEUR_BORDURE.darker();
            this.thumbHighlightColor = COULEUR_BORDURE.brighter();
            this.trackColor = COULEUR_SURFACE;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(thumbColor);
            g2d.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
            g2d.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(trackColor);
            g2d.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            g2d.dispose();
        }
    }

    public static void main(String[] args) {
        // Activer le look moderne
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.application.name", "Ball Sort Puzzle");

        SwingUtilities.invokeLater(() -> {
            Jeu jeu = new Jeu(5, 6); // Exemple : 5 tubes, 6 billes chacun
            FenetrePrincipale fenetre = new FenetrePrincipale(jeu);
            Controleur controleur = new Controleur(jeu, fenetre);
            fenetre.setControleur(controleur);
            fenetre.setVisible(true);
        });
    }
}