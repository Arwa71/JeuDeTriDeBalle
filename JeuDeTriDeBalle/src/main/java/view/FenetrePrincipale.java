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
import java.util.List;
import java.util.Random;
import javax.sound.sampled.*;

public class FenetrePrincipale extends JFrame {
    private static final int LARGEUR_FENETRE = 700;
    private static final int HAUTEUR_FENETRE = 500;

    // Palette de couleurs premium
    private static final Color COULEUR_FOND = new Color(10, 11, 13);
    private static final Color COULEUR_SURFACE = new Color(19, 22, 26);
    private static final Color COULEUR_ACCENT = new Color(88, 166, 255);
    private static final Color COULEUR_ACCENT_HOVER = new Color(108, 176, 255);
    private static final Color COULEUR_TEXTE = new Color(240, 243, 249);
    private static final Color COULEUR_BORDURE = new Color(48, 54, 61);
    private static final Color COULEUR_SUCCESS = new Color(46, 204, 113);
    private static final Color COULEUR_GOLD = new Color(255, 215, 0);

    private JPanel panneauPrincipal;
    private ModernButton btnRecommencer;
    private JLabel lblTitre;
    private JLabel lblStatut;
    private JLabel lblBestScore;
    private JPanel leaderboardPanel;
    private Jeu jeu;
    private Controleur controleur;
    private Tube tubeSelectionne;
    private Timer pulseTimer;
    private float pulseAlpha = 0.0f;
    private String pseudoUtilisateur = null;
    private JLabel lblUser;

    public FenetrePrincipale(Jeu jeu) {
        this.jeu = jeu;
        this.controleur = null;
        this.tubeSelectionne = null;
        demanderPseudo();
        initialiserFenetre();
        ajouterComposants();
        configurerEvenements();
        demarrerAnimations();
    }

    private void demanderPseudo() {
        while (pseudoUtilisateur == null || pseudoUtilisateur.trim().isEmpty()) {
            pseudoUtilisateur = JOptionPane.showInputDialog(
                this,
                "Entrez votre pseudo pour enregistrer votre score :",
                "Pseudo requis",
                JOptionPane.QUESTION_MESSAGE
            );
            if (pseudoUtilisateur == null) {
                System.exit(0);
            }
            pseudoUtilisateur = pseudoUtilisateur.trim();
        }
        if (lblUser != null) {
            lblUser.setText("Joueur : " + pseudoUtilisateur);
        }
    }

    public void setControleur(Controleur controleur) {
        this.controleur = controleur;
    }

    private void initialiserFenetre() {
        setTitle("Ball Sort Puzzle - Premium Edition");
        setSize(LARGEUR_FENETRE, HAUTEUR_FENETRE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        getContentPane().setBackground(COULEUR_FOND);

        try {
            setIconImage(createPremiumIcon());
        } catch (Exception e) {
            // Icône par défaut si erreur
        }
    }

    private Image createPremiumIcon() { 
        int size = 48;
        BufferedImage icon = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gradient = new GradientPaint(0, 0, COULEUR_ACCENT, size, size, COULEUR_ACCENT.darker());
        g2d.setPaint(gradient);
        g2d.fillRoundRect(4, 4, size - 8, size - 8, 12, 12);

        g2d.setColor(new Color(255, 255, 255, 60));
        g2d.fillRoundRect(6, 6, size - 12, (size - 12) / 2, 10, 10);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawRoundRect(12, 16, 8, 20, 3, 3);
        g2d.drawRoundRect(28, 12, 8, 24, 3, 3);

        g2d.setColor(COULEUR_GOLD);
        g2d.fillOval(14, 30, 4, 4);
        g2d.setColor(COULEUR_SUCCESS);
        g2d.fillOval(30, 28, 4, 4);

        g2d.dispose();
        return icon;
    }

    private void ajouterComposants() {
        ajouterEnTetePremium();
        ajouterZoneJeu();
        ajouterPanneauControles();
    }

    private void ajouterEnTetePremium() {
        JPanel panneauEnTete = new JPanel(new BorderLayout());
        panneauEnTete.setBackground(COULEUR_SURFACE);
        panneauEnTete.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, COULEUR_ACCENT),
                BorderFactory.createEmptyBorder(25, 40, 25, 40)
        ));

        lblTitre = new JLabel("Ball Sort Puzzle") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                        0, 0, COULEUR_ACCENT.brighter(),
                        getWidth(), 0, COULEUR_GOLD
                );
                g2d.setPaint(gradient);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2;
                g2d.drawString(getText(), x, y);
                g2d.dispose();
            }
        };
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitre.setHorizontalAlignment(SwingConstants.CENTER);

        lblStatut = new JLabel("Prêt à jouer !");
        lblStatut.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblStatut.setForeground(COULEUR_TEXTE.darker());
        lblStatut.setHorizontalAlignment(SwingConstants.CENTER);

        lblBestScore = new JLabel();
        lblBestScore.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblBestScore.setForeground(COULEUR_GOLD);
        lblBestScore.setHorizontalAlignment(SwingConstants.CENTER);

        leaderboardPanel = new JPanel();
        leaderboardPanel.setBackground(COULEUR_SURFACE);
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));

        JPanel conteneurTitre = new JPanel(new BorderLayout());
        conteneurTitre.setBackground(COULEUR_SURFACE);
        conteneurTitre.add(lblTitre, BorderLayout.CENTER);
        conteneurTitre.add(lblStatut, BorderLayout.SOUTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(COULEUR_SURFACE);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(lblBestScore);
        infoPanel.add(leaderboardPanel);

        lblUser = new JLabel("Joueur : " + pseudoUtilisateur);
        lblUser.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblUser.setForeground(COULEUR_TEXTE);
        infoPanel.add(lblUser);

        panneauEnTete.add(conteneurTitre, BorderLayout.CENTER);
        panneauEnTete.add(infoPanel, BorderLayout.EAST);

        add(panneauEnTete, BorderLayout.NORTH);

        // Load scores from DB
        rafraichirScores();
    }

    // Call this method whenever you want to refresh the best score/leaderboard
    private void rafraichirScores() {
        try {
            List<String[]> topScores = util.ScoreDAO.getTopScores(10);
            if (!topScores.isEmpty()) {
                String[] best = topScores.get(0);
                lblBestScore.setText("Meilleur score : " + best[0] + " coups par " + best[1] + " (" + best[2] + ")");
            } else {
                lblBestScore.setText("Aucun score enregistré.");
            }
            // Leaderboard (top 10)
            leaderboardPanel.removeAll();
            JLabel lb = new JLabel("Top 10 :");
            lb.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lb.setForeground(COULEUR_ACCENT);
            leaderboardPanel.add(lb);
            for (int i = 0; i < topScores.size(); i++) {
                String[] s = topScores.get(i);
                JLabel l = new JLabel((i+1) + ". " + s[1] + " - " + s[0] + " coups (" + s[2] + ")");
                l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                l.setForeground(COULEUR_TEXTE);
                leaderboardPanel.add(l);
            }
            leaderboardPanel.revalidate();
            leaderboardPanel.repaint();
        } catch (Exception e) {
            lblBestScore.setText("Erreur chargement scores.");
        }
    }

    private void ajouterZoneJeu() {
        JPanel conteneurJeu = new JPanel(new BorderLayout());
        conteneurJeu.setBackground(COULEUR_FOND);
        conteneurJeu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panneauPrincipal = new JPanel();
        panneauPrincipal.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 0));
        panneauPrincipal.setBackground(COULEUR_FOND);

        for (Tube tube : jeu.getTubes()) {
            PremiumTubePanel panel = new PremiumTubePanel(tube);
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    boolean moved = false;
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (controleur != null) {
                            int before = tube.getBoules().size();
                            controleur.selectionnerTube(tube);
                            int after = tube.getBoules().size();
                            moved = (before != after);
                            if (moved) jeu.decrementerCoups();
                        }
                        if (moved) {
                            playSatisfyingMoveSound();
                            ajouterEffetParticules(panel);
                        }
                        if (jeu.estTermine()) {
                            afficherVictoirePremium();
                        }
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        if (controleur != null) {
                            int before = tube.getBoules().size();
                            controleur.deplacerBillesDeMemeCouleur(tube);
                            int after = tube.getBoules().size();
                            moved = (before != after);
                            if (moved) jeu.decrementerCoups();
                        }
                        if (moved) {
                            playPowerMoveSound();
                            ajouterEffetEclair(panel);
                        }
                        if (jeu.estTermine()) {
                            afficherVictoirePremium();
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
                    lblStatut.setText("Prêt à jouer !");
                }
            });
            panneauPrincipal.add(panel);
        }

        JScrollPane scrollPane = new JScrollPane(panneauPrincipal);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(COULEUR_FOND);

        // Style premium pour la scrollbar
        scrollPane.getHorizontalScrollBar().setUI(new PremiumScrollBarUI());

        conteneurJeu.add(scrollPane, BorderLayout.CENTER);
        add(conteneurJeu, BorderLayout.CENTER);
    }

    private void ajouterPanneauControles() {
        JPanel panneauBas = new JPanel(new FlowLayout());
        panneauBas.setBackground(COULEUR_SURFACE);
        panneauBas.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, COULEUR_ACCENT),
                BorderFactory.createEmptyBorder(25, 40, 25, 40)
        ));

        btnRecommencer = new ModernButton("Nouvelle Partie");
        btnRecommencer.setPreferredSize(new Dimension(180, 45));
        panneauBas.add(btnRecommencer);

        add(panneauBas, BorderLayout.SOUTH);
    }

    private void configurerEvenements() {
        btnRecommencer.addActionListener(e -> {
            if (controleur != null) {
                controleur.recommencerPartie();
                tubeSelectionne = null;
                lblStatut.setText("Nouvelle partie commencée !");
                playRestartSound();
            }
        });
    }

    private void demarrerAnimations() {
        pulseTimer = new Timer(50, e -> {
            pulseAlpha += 0.05f;
            if (pulseAlpha > 1.0f) pulseAlpha = 0.0f;
            repaint();
        });
        pulseTimer.start();
    }

    public void setTubeSelectionne(Tube tube) {
        this.tubeSelectionne = tube;
        repaint();
    }

    public void rechargerTubes() {
        panneauPrincipal.removeAll();
        for (Tube tube : jeu.getTubes()) {
            PremiumTubePanel panel = new PremiumTubePanel(tube);
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    boolean moved = false;
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (controleur != null) {
                            int before = tube.getBoules().size();
                            controleur.selectionnerTube(tube);
                            int after = tube.getBoules().size();
                            moved = (before != after);
                            if (moved) jeu.decrementerCoups();
                        }
                        if (moved) {
                            playSatisfyingMoveSound();
                            ajouterEffetParticules(panel);
                        }
                        if (jeu.estTermine()) {
                            afficherVictoirePremium();
                        }
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        if (controleur != null) {
                            int before = tube.getBoules().size();
                            controleur.deplacerBillesDeMemeCouleur(tube);
                            int after = tube.getBoules().size();
                            moved = (before != after);
                            if (moved) jeu.decrementerCoups();
                        }
                        if (moved) {
                            playPowerMoveSound();
                            ajouterEffetEclair(panel);
                        }
                        if (jeu.estTermine()) {
                            afficherVictoirePremium();
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
                    lblStatut.setText("Prêt à jouer !");
                }
            });
            panneauPrincipal.add(panel);
        }
        panneauPrincipal.revalidate();
        panneauPrincipal.repaint();
    }

    // Message de victoire premium avec animations
    private void afficherVictoirePremium() {
        SwingUtilities.invokeLater(() -> {
            playVictorySound();
            int coups = calculerCoupsUtilises();
            try {
                util.ScoreDAO.saveScore(pseudoUtilisateur, coups);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement du score : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }

            // Dialogue personnalisé premium
            JDialog dialogueVictoire = new JDialog(this, "Victoire !", true);
            dialogueVictoire.setSize(400, 300);
            dialogueVictoire.setLocationRelativeTo(this);
            dialogueVictoire.setUndecorated(true);
            dialogueVictoire.getRootPane().setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COULEUR_GOLD, 3),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));

            // Panneau principal du dialogue
            JPanel panneauDialogue = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Fond avec dégradé premium
                    GradientPaint gradient = new GradientPaint(
                            0, 0, COULEUR_SURFACE,
                            0, getHeight(), COULEUR_FOND
                    );
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());

                    // Particules dorées animées
                    Random rand = new Random(System.currentTimeMillis() / 100);
                    g2d.setColor(new Color(255, 215, 0, 100));
                    for (int i = 0; i < 20; i++) {
                        int x = rand.nextInt(getWidth());
                        int y = rand.nextInt(getHeight());
                        int size = 3 + rand.nextInt(4);
                        g2d.fillOval(x, y, size, size);
                    }

                    g2d.dispose();
                }
            };
            panneauDialogue.setBackground(COULEUR_SURFACE);

            // Titre de victoire avec emoji et style
            JLabel lblVictoire = new JLabel("🎉 VICTOIRE ! 🎉") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Effet de brillance
                    GradientPaint gradient = new GradientPaint(
                            0, 0, COULEUR_GOLD,
                            getWidth(), getHeight(), COULEUR_ACCENT
                    );
                    g2d.setPaint(gradient);
                    g2d.setFont(getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(getText())) / 2;
                    int y = (getHeight() + fm.getAscent()) / 2;
                    g2d.drawString(getText(), x, y);

                    g2d.dispose();
                }
            };
            lblVictoire.setFont(new Font("Segoe UI", Font.BOLD, 36));
            lblVictoire.setHorizontalAlignment(SwingConstants.CENTER);

            // Message de félicitations sans emoji
            JLabel lblMessage = new JLabel("<html><center>Félicitations !<br>Vous avez brillamment résolu le puzzle !<br><br>Performance exceptionnelle !</center></html>");
            lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lblMessage.setForeground(COULEUR_TEXTE);
            lblMessage.setHorizontalAlignment(SwingConstants.CENTER);

            // Boutons premium sans emoji
            JPanel panneauBoutons = new JPanel(new FlowLayout());
            panneauBoutons.setOpaque(false);

            ModernButton btnNouvellePartie = new ModernButton("Nouvelle Partie");
            btnNouvellePartie.setPreferredSize(new Dimension(150, 40));
            btnNouvellePartie.addActionListener(e -> {
                dialogueVictoire.dispose();
                demanderPseudo(); // Ask for pseudo again for new game
                if (controleur != null) {
                    controleur.recommencerPartie();
                    tubeSelectionne = null;
                }
                rafraichirScores();
            });

            ModernButton btnFermer = new ModernButton("Fermer");
            btnFermer.setPreferredSize(new Dimension(100, 40));
            btnFermer.addActionListener(e -> {
                dialogueVictoire.dispose();
                rafraichirScores();
            });

            panneauBoutons.add(btnNouvellePartie);
            panneauBoutons.add(btnFermer);

            // Assemblage du dialogue
            panneauDialogue.add(lblVictoire, BorderLayout.NORTH);
            panneauDialogue.add(lblMessage, BorderLayout.CENTER);
            panneauDialogue.add(panneauBoutons, BorderLayout.SOUTH);

            dialogueVictoire.add(panneauDialogue);

            // Animation d'entrée
            Timer animationTimer = new Timer(50, null);
            final int[] compteur = {0};
            animationTimer.addActionListener(e -> {
                compteur[0]++;
                if (compteur[0] < 20) {
                    panneauDialogue.repaint();
                } else {
                    animationTimer.stop();
                }
            });
            animationTimer.start();

            dialogueVictoire.setVisible(true);
        });
    }

    // Helper to calculate the number of moves (coups) used
    private int calculerCoupsUtilises() {
        // If you have a coups counter in Jeu, use it here.
        // Otherwise, you can implement your own logic.
        // For now, let's assume 100 - jeu.getCoupsRestants()
        return 100 - jeu.getCoupsRestants();
    }

    // Sons premium
    private void playSatisfyingMoveSound() {
        try {
            float sampleRate = 44100;
            int ms = 150;
            int len = (int) (sampleRate * ms / 1000);
            byte[] buf = new byte[len];

            // Son cristallin avec harmoniques
            for (int i = 0; i < len; i++) {
                double t = i / sampleRate;
                double env = Math.exp(-4 * t);
                double val = Math.sin(2 * Math.PI * 1200 * t) * 0.6
                        + Math.sin(2 * Math.PI * 1800 * t) * 0.3
                        + Math.sin(2 * Math.PI * 2400 * t) * 0.1;
                buf[i] = (byte) (env * val * 100);
            }

            AudioFormat af = new AudioFormat(sampleRate, 8, 1, true, false);
            Clip clip = AudioSystem.getClip();
            clip.open(af, buf, 0, buf.length);
            clip.start();
        } catch (Exception ex) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void playPowerMoveSound() {
        try {
            float sampleRate = 44100;
            int ms = 200;
            int len = (int) (sampleRate * ms / 1000);
            byte[] buf = new byte[len];

            // Son puissant avec sweep ascendant
            for (int i = 0; i < len; i++) {
                double t = i / sampleRate;
                double env = Math.exp(-2 * t);
                double freq = 800 + (400 * t); // Montée en fréquence
                double val = Math.sin(2 * Math.PI * freq * t) * 0.8
                        + Math.sin(2 * Math.PI * freq * 1.5 * t) * 0.2;
                buf[i] = (byte) (env * val * 120);
            }

            AudioFormat af = new AudioFormat(sampleRate, 8, 1, true, false);
            Clip clip = AudioSystem.getClip();
            clip.open(af, buf, 0, buf.length);
            clip.start();
        } catch (Exception ex) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void playVictorySound() {
        try {
            float sampleRate = 44100;
            int ms = 800;
            int len = (int) (sampleRate * ms / 1000);
            byte[] buf = new byte[len];

            // Mélodie de victoire complexe
            double[] notes = {523.25, 659.25, 783.99, 1046.50}; // Do, Mi, Sol, Do
            for (int i = 0; i < len; i++) {
                double t = i / sampleRate;
                double env = Math.exp(-1 * t);
                int noteIndex = (int) (t * 4) % notes.length;
                double val = Math.sin(2 * Math.PI * notes[noteIndex] * t) * 0.7
                        + Math.sin(2 * Math.PI * notes[noteIndex] * 2 * t) * 0.2
                        + Math.sin(2 * Math.PI * notes[noteIndex] * 0.5 * t) * 0.1;
                buf[i] = (byte) (env * val * 127);
            }

            AudioFormat af = new AudioFormat(sampleRate, 8, 1, true, false);
            Clip clip = AudioSystem.getClip();
            clip.open(af, buf, 0, buf.length);
            clip.start();
        } catch (Exception ex) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void playRestartSound() {
        try {
            float sampleRate = 44100;
            int ms = 100;
            int len = (int) (sampleRate * ms / 1000);
            byte[] buf = new byte[len];

            // Son de remise à zéro doux
            for (int i = 0; i < len; i++) {
                double t = i / sampleRate;
                double env = 1.0 - t;
                double val = Math.sin(2 * Math.PI * 440 * t) * 0.5;
                buf[i] = (byte) (env * val * 80);
            }

            AudioFormat af = new AudioFormat(sampleRate, 8, 1, true, false);
            Clip clip = AudioSystem.getClip();
            clip.open(af, buf, 0, buf.length);
            clip.start();
        } catch (Exception ex) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    // Effets visuels
    private void ajouterEffetParticules(JComponent composant) {
        Timer effet = new Timer(50, null);
        final int[] compteur = {0};
        effet.addActionListener(e -> {
            compteur[0]++;
            composant.repaint();
            if (compteur[0] > 10) effet.stop();
        });
        effet.start();
    }

    private void ajouterEffetEclair(JComponent composant) {
        Color transparent = new Color(0, 0, 0, 0);
        Timer effet = new Timer(100, null);
        final int[] compteur = {0};
        effet.addActionListener(e -> {
            compteur[0]++;
            composant.setBorder(BorderFactory.createLineBorder(
                    compteur[0] % 2 == 0 ? COULEUR_GOLD : transparent, 2
            ));
            if (compteur[0] > 6) {
                effet.stop();
                composant.setBorder(null);
            }
        });
        effet.start();
    }

    // Classe pour tube panel premium
    private class PremiumTubePanel extends JPanel {
        private Tube tube;
        private boolean isHover = false;
        private boolean isSelected = false;

        public PremiumTubePanel(Tube tube) {
            this.tube = tube;
            setPreferredSize(new Dimension(55, 200));
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

            // Ombre portée premium
            if (isHover || isSelected) {
                g2d.setColor(new Color(0, 0, 0, 80));
                g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 12, 12);
            }

            // Fond du tube avec dégradé premium
            GradientPaint gradient;
            if (isSelected) {
                gradient = new GradientPaint(
                        0, 0, COULEUR_ACCENT.brighter(),
                        0, getHeight(), COULEUR_ACCENT.darker()
                );
            } else {
                gradient = new GradientPaint(
                        0, 0, COULEUR_SURFACE.brighter(),
                        0, getHeight(), COULEUR_SURFACE.darker()
                );
            }
            g2d.setPaint(gradient);
            g2d.fillRoundRect(8, 15, getWidth() - 16, getHeight() - 30, 10, 10);

            // Reflet métallique
            g2d.setColor(new Color(255, 255, 255, isHover ? 40 : 20));
            g2d.fillRoundRect(10, 17, (getWidth() - 20) / 3, getHeight() - 34, 8, 8);

            // Bordure premium
            if (isSelected) {
                g2d.setColor(COULEUR_GOLD);
                g2d.setStroke(new BasicStroke(2.5f));
            } else {
                g2d.setColor(isHover ? COULEUR_BORDURE.brighter() : COULEUR_BORDURE);
                g2d.setStroke(new BasicStroke(1.5f));
            }
            g2d.drawRoundRect(8, 15, getWidth() - 16, getHeight() - 30, 10, 10);

            dessinerBoulesPremium(g2d);
            g2d.dispose();
        }

        private void dessinerBoulesPremium(Graphics2D g2d) {
            java.util.List<Boule> boules = tube.getBoules();
            int nbBoules = tube.getTailleMax();
            int espaceTotal = getHeight() - 30 - 15;
            int diametre, espace;

            if (nbBoules > 1) {
                diametre = Math.min(35, (espaceTotal - (nbBoules - 1) * 4) / nbBoules);
                espace = (espaceTotal - diametre * nbBoules) / Math.max(1, nbBoules - 1);
            } else {
                diametre = Math.min(35, espaceTotal);
                espace = 0;
            }

            int x = (getWidth() - diametre) / 2;
            int yStart = getHeight() - 20 - diametre;

            for (int i = 0; i < boules.size(); i++) {
                Boule boule = boules.get(i);
                int y = yStart - (i * (diametre + espace));

                // Ombre premium
                g2d.setColor(new Color(0, 0, 0, 60));
                g2d.fillOval(x + 2, y + 2, diametre, diametre);

                // Gradient premium pour effet 3D
                Color couleurBoule = boule.getCouleur();
                GradientPaint gradientBoule = new GradientPaint(
                        x, y, couleurBoule.brighter().brighter(),
                        x + diametre, y + diametre, couleurBoule.darker()
                );
                g2d.setPaint(gradientBoule);
                g2d.fillOval(x, y, diametre, diametre);

                // Reflet brillant premium
                g2d.setColor(new Color(255, 255, 255, 120));
                g2d.fillOval(x + diametre / 5, y + diametre / 5, diametre / 2, diametre / 2);

                // Petit reflet secondaire
                g2d.setColor(new Color(255, 255, 255, 80));
                g2d.fillOval(x + (3 * diametre) / 4, y + (3 * diametre) / 4, diametre / 6, diametre / 6);

                // Contour métallique
                g2d.setColor(couleurBoule.darker().darker());
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawOval(x, y, diametre, diametre);

                // Anneau intérieur brillant
                g2d.setColor(couleurBoule.brighter());
                g2d.setStroke(new BasicStroke(0.8f));
                g2d.drawOval(x + 2, y + 2, diametre - 4, diametre - 4);
            }
        }
    }

    // Classe pour bouton moderne premium
    private class ModernButton extends JButton {
        private boolean isHovered = false;
        private boolean isPressed = false;

        public ModernButton(String text) {
            super(text);
            setFont(new Font("Segoe UI", Font.BOLD, 15));
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

                @Override
                public void mousePressed(MouseEvent e) {
                    isPressed = true;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    isPressed = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Ombre portée
            if (!isPressed) {
                g2d.setColor(new Color(0, 0, 0, 40));
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 12, 12);
            }

            // Fond du bouton avec gradient premium
            GradientPaint gradient;
            if (isPressed) {
                gradient = new GradientPaint(
                        0, 0, COULEUR_ACCENT.darker(),
                        0, getHeight(), COULEUR_ACCENT.darker().darker()
                );
            } else if (isHovered) {
                gradient = new GradientPaint(
                        0, 0, COULEUR_ACCENT_HOVER.brighter(),
                        0, getHeight(), COULEUR_ACCENT_HOVER.darker()
                );
            } else {
                gradient = new GradientPaint(
                        0, 0, COULEUR_ACCENT,
                        0, getHeight(), COULEUR_ACCENT.darker()
                );
            }

            g2d.setPaint(gradient);
            int offset = isPressed ? 2 : 0;
            g2d.fillRoundRect(offset, offset, getWidth() - offset, getHeight() - offset, 12, 12);

            // Reflet premium sur le dessus
            if (!isPressed) {
                g2d.setColor(new Color(255, 255, 255, isHovered ? 40 : 25));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight() / 2, 12, 12);
            }

            // Bordure brillante
            g2d.setColor(isHovered ? COULEUR_GOLD : COULEUR_ACCENT.brighter());
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(offset, offset, getWidth() - offset - 1, getHeight() - offset - 1, 12, 12);

            g2d.dispose();
            super.paintComponent(g);
        }
    }

    // Style premium pour scrollbar
    private class PremiumScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = COULEUR_ACCENT;
            this.thumbDarkShadowColor = COULEUR_ACCENT.darker();
            this.thumbHighlightColor = COULEUR_ACCENT.brighter();
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

            // Gradient pour le thumb
            GradientPaint gradient = new GradientPaint(
                    thumbBounds.x, thumbBounds.y, COULEUR_ACCENT.brighter(),
                    thumbBounds.x, thumbBounds.y + thumbBounds.height, COULEUR_ACCENT.darker()
            );
            g2d.setPaint(gradient);
            g2d.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 15, 15);

            // Reflet
            g2d.setColor(new Color(255, 255, 255, 60));
            g2d.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height / 2, 13, 13);

            g2d.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(trackColor);
            g2d.fillRoundRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height, 8, 8);
            g2d.dispose();
        }
    }

    @Override
    public void dispose() {
        if (pulseTimer != null) {
            pulseTimer.stop();
        }
        super.dispose();
    }

    public static void main(String[] args) {
        // Activer le look premium
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.application.name", "Ball Sort Puzzle Premium");

        // Améliorer le rendu graphique
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {


            Jeu jeu = new Jeu(4, 6); // 4 couleurs/tubes remplis, 2 tubes vides ajoutés automatiquement
            FenetrePrincipale fenetre = new FenetrePrincipale(jeu);
            Controleur controleur = new Controleur(jeu, fenetre);
            fenetre.setControleur(controleur);
            fenetre.setVisible(true);
        });
    }
}