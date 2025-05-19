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
import javax.sound.sampled.*;

public class FenetrePrincipale extends JFrame {
    private static final int LARGEUR_FENETRE = 600;
    private static final int HAUTEUR_FENETRE = 400;

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
        conteneurJeu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panneauPrincipal = new JPanel();
        panneauPrincipal.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
        panneauPrincipal.setBackground(COULEUR_FOND);

        for (Tube tube : jeu.getTubes()) {
            ModernTubePanel panel = new ModernTubePanel(tube);
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        boolean moved = false;
                        if (controleur != null) {
                            int before = tube.getBoules().size();
                            controleur.selectionnerTube(tube);
                            int after = tube.getBoules().size();
                            moved = (before != after);
                        }
                        if (moved) playSatisfyingMoveSound();
                        if (jeu.estTermine()) {
                            JOptionPane.showMessageDialog(FenetrePrincipale.this, "🎉 VICTOIRE ! 🎉", "Victoire", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        boolean moved = false;
                        if (controleur != null) {
                            int before = tube.getBoules().size();
                            controleur.deplacerBillesDeMemeCouleur(tube);
                            int after = tube.getBoules().size();
                            moved = (before != after);
                        }
                        if (moved) playSatisfyingMoveSound();
                        if (jeu.estTermine()) {
                            JOptionPane.showMessageDialog(FenetrePrincipale.this, "🎉 VICTOIRE ! 🎉", "Victoire", JOptionPane.INFORMATION_MESSAGE);
                        }
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
                tubeSelectionne = null; // Ajouté : désélectionner tout tube après recommencer
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
                        boolean moved = false;
                        if (controleur != null) {
                            int before = tube.getBoules().size();
                            controleur.selectionnerTube(tube);
                            int after = tube.getBoules().size();
                            moved = (before != after);
                        }
                        if (moved) playSatisfyingMoveSound();
                        if (jeu.estTermine()) {
                            JOptionPane.showMessageDialog(FenetrePrincipale.this, "🎉 VICTOIRE ! 🎉", "Victoire", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        boolean moved = false;
                        if (controleur != null) {
                            int before = tube.getBoules().size();
                            controleur.deplacerBillesDeMemeCouleur(tube);
                            int after = tube.getBoules().size();
                            moved = (before != after);
                        }
                        if (moved) playSatisfyingMoveSound();
                        if (jeu.estTermine()) {
                            JOptionPane.showMessageDialog(FenetrePrincipale.this, "🎉 VICTOIRE ! 🎉", "Victoire", JOptionPane.INFORMATION_MESSAGE);
                        }
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

    // Effet sonore pour deplacement
    private void playSatisfyingMoveSound() {
        try {
            float sampleRate = 44100;
            int ms = 120;
            int len = (int) (sampleRate * ms / 1000);
            byte[] buf = new byte[len];
            double freq = 1047; // C6, glassy
            double overtone1 = 1568; // G6
            double overtone2 = 2093; // C7
            for (int i = 0; i < len; i++) {
                double t = i / sampleRate;
                double env = Math.exp(-3 * t); // envelope for creamy effect
                double val = Math.sin(2 * Math.PI * freq * t)
                           + 0.5 * Math.sin(2 * Math.PI * overtone1 * t)
                           + 0.3 * Math.sin(2 * Math.PI * overtone2 * t);
                buf[i] = (byte) (env * val * 127);
            }
            AudioFormat af = new AudioFormat(sampleRate, 8, 1, true, false);
            Clip clip = AudioSystem.getClip();
            clip.open(af, buf, 0, buf.length);
            clip.start();
        } catch (Exception ex) {
            // fallback
            Toolkit.getDefaultToolkit().beep();
        }
    }

    // Classe pour tube panel moderne
    private class ModernTubePanel extends JPanel {
        private Tube tube;
        private boolean isHover = false;
        private boolean isSelected = false;

        public ModernTubePanel(Tube tube) {
            this.tube = tube;
            setPreferredSize(new Dimension(48, 180)); // tubes plus petits
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
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 10, 10);
            }

            // Contour du tube avec gradient
            GradientPaint gradient = new GradientPaint(
                    0, 0, isSelected ? COULEUR_ACCENT : COULEUR_SURFACE,
                    0, getHeight(), isSelected ? COULEUR_ACCENT.darker() : COULEUR_SURFACE.darker()
            );
            g2d.setPaint(gradient);
            g2d.fillRoundRect(6, 10, getWidth() - 12, getHeight() - 20, 8, 8);

            // Bordure brillante
            if (isSelected) {
                g2d.setColor(COULEUR_ACCENT);
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(6, 10, getWidth() - 12, getHeight() - 20, 8, 8);
            } else {
                g2d.setColor(isHover ? COULEUR_BORDURE.brighter() : COULEUR_BORDURE);
                g2d.setStroke(new BasicStroke(1f));
                g2d.drawRoundRect(6, 10, getWidth() - 12, getHeight() - 20, 8, 8);
            }

            dessinerBoulesModernes(g2d);
            g2d.dispose();
        }

        private void dessinerBoulesModernes(Graphics2D g2d) {
            java.util.List<Boule> boules = tube.getBoules();
            int nbBoules = tube.getTailleMax();
            int espaceTotal = getHeight() - 20 - 10; // 20: bas, 10: haut
            int diametre, espace;
            if (nbBoules > 1) {
                diametre = Math.min(32, (espaceTotal - (nbBoules - 1) * 3) / nbBoules);
                espace = (espaceTotal - diametre * nbBoules) / (nbBoules - 1);
            } else {
                diametre = Math.min(32, espaceTotal);
                espace = 0;
            }
            int x = (getWidth() - diametre) / 2;
            int yStart = getHeight() - 20 - diametre;
            for (int i = 0; i < boules.size(); i++) {
                Boule boule = boules.get(i);
                int y = yStart - (i * (diametre + espace));

                // Ombre de la boule
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillOval(x + 1, y + 1, diametre, diametre);

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
                g2d.fillOval(x + diametre / 6, y + diametre / 6, diametre / 3, diametre / 3);

                // Contour subtil
                g2d.setColor(couleurBoule.darker().darker());
                g2d.setStroke(new BasicStroke(1f));
                g2d.drawOval(x, y, diametre, diametre);
            }
        }

        private Color getBouleSurLaquelleOnClique(int x, int y) {
            int diametre = 32; // Billes plus petites
            int espace = 3; // Espace entre les billes plus petit
            int xBoule = (getWidth() - diametre) / 2;
            int yStart = getHeight() - 20; // Position de départ ajustée
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
            Jeu jeu = new Jeu(4, 6); // 4 couleurs/tubes remplis, 2 tubes vides ajoutés automatiquement
            FenetrePrincipale fenetre = new FenetrePrincipale(jeu);
            Controleur controleur = new Controleur(jeu, fenetre);
            fenetre.setControleur(controleur);
            fenetre.setVisible(true);
        });
    }
}