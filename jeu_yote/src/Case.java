import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.String.valueOf;

public class Case extends JButton implements ActionListener {

    private int typeCase;  // si typeCase == 0, case du plateau, si typeCase ==1 case du stock

    private ImageIcon imagePion;
    private Pion pion;
    private Color couleurFond;

    private boolean occupe;
    private int abscisse;
    private int ordonnee;

    public Case(Color couleur, int abs, int ord, int typeCase) {
        super();
        this.typeCase = typeCase;
        this.couleurFond = couleur;
        this.setOpaque(true);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setBorderPainted(true);
        this.setBackground(couleur);
        if (typeCase == 0)
            this.setPreferredSize(new Dimension(100, 100));
        else
            this.setPreferredSize(new Dimension(50, 50));

        addActionListener(this);
        this.setFocusable(false);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setRolloverEnabled(false);

        this.abscisse = abs;
        this.ordonnee = ord;
        this.occupe = false;
        this.imagePion = null;
        this.pion = null;
    }

    public Pion getPion() {
        return pion;
    }

    public void setPion(Pion p) {
        this.pion = p;
        if (p != null) {
            String chemin = p.getCouleur() == CouleurPion.blanc ? "/zebre.png" : "/guepard.png";
            java.net.URL imageURL = getClass().getResource(chemin);
            if (imageURL != null) {
                imagePion = new ImageIcon(imageURL);
                Image scaled = imagePion.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(scaled));
            } else {
                setIcon(null);
            }
            setBackground(Color.white);
            occupe = true;
        } else {
            setIcon(null);
            setBackground(couleurFond);
            occupe = false;
        }
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public int getAbscisse() {
        return abscisse;
    }

    public int getTypeCase() {
        return typeCase;
    }

    public int getOrdonnee() {
        return ordonnee;
    }

    public boolean isOccupe() {
        return occupe;
    }

    public void actionPerformed(ActionEvent e) {

        if (Yote.etat == 0) {
            Fenetre.boutonAnnuler.setEnabled(false);
            Yote.caseDep = ((Case) e.getSource());

            if (Yote.caseDep.isOccupe()) {
                if (Yote.caseDep.getPion().getCouleur() == Yote.joueur) {
                    Yote.caseDep.setBorder(Yote.redline);
                    Yote.etat = 1;
                }
            }
        } else if (Yote.etat == 1) {
            Yote.caseArr = ((Case) e.getSource());

            if (Yote.unPlateau.isIn(Yote.caseArr)) {
                if (Yote.caseDep.getTypeCase() == 1) {
                    if (!Yote.caseArr.isOccupe())
                        Yote.unPlateau.jouerCoup(Yote.caseDep, Yote.caseArr);
                    else {
                        if (Yote.caseArr.getPion().getCouleur() == Yote.joueur) {
                            Yote.caseDep.setBorder(Yote.empty);
                            Yote.caseArr.setBorder(Yote.redline);
                            Yote.caseDep = Yote.caseArr;
                            Yote.etat = 1;
                        }
                    }
                } else {
                    int cv = Yote.unPlateau.coupValide(Yote.caseDep, Yote.caseArr);
                    if (cv == 1) {
                        Yote.unPlateau.jouerCoup(Yote.caseDep, Yote.caseArr);
                    } else if (cv == 2) {
                        Yote.unPlateau.jouerCoupPrise(Yote.caseDep, Yote.caseArr);
                        Yote.scoreBlanc.setText("Score Joueur Blanc \n" + valueOf(Yote.nbPionBlanc));
                        Yote.scoreNoir.setText("Score Joueur Noir \n" + valueOf(Yote.nbPionNoir));
                        Yote.etat = 2;
                        return;
                    } else if (cv == 0 && Yote.caseArr.isOccupe()
                            && Yote.caseArr.getPion().getCouleur() == Yote.caseDep.getPion().getCouleur()) {
                        Yote.caseDep.setBorder(Yote.empty);
                        Yote.caseArr.setBorder(Yote.redline);
                        Yote.caseDep = Yote.caseArr;
                        Yote.etat = 1;
                    }
                }
            } else {
                if (Yote.caseArr.isOccupe()
                        && Yote.caseArr.getPion().getCouleur() == Yote.caseDep.getPion().getCouleur()) {
                    Yote.caseDep.setBorder(Yote.empty);
                    Yote.caseArr.setBorder(Yote.redline);
                    Yote.caseDep = Yote.caseArr;
                    Yote.etat = 1;
                }
            }
        } else if (Yote.etat == 2) {
            boolean finPartie = false;
            Yote.casePrise = ((Case) e.getSource());
            CouleurPion couleurSuiv = (Yote.joueur == CouleurPion.blanc) ? CouleurPion.noir : CouleurPion.blanc;

            if (Yote.casePrise.isOccupe() && Yote.casePrise.getPion().getCouleur() == couleurSuiv) {
                Yote.casePrise.setPion(null);
                if (Yote.joueur == CouleurPion.blanc) {
                    Yote.nbPionNoir--;
                    if (Yote.nbPionNoir == 0) finPartie = true;
                } else {
                    Yote.nbPionBlanc--;
                    if (Yote.nbPionBlanc == 0) finPartie = true;
                }

                if (finPartie) {
                    String message = "Partie terminée. Le joueur " + Yote.joueur + " a perdu !";
                    int retour = JOptionPane.showOptionDialog(this, message, "Fin de jeu", 1, 1,
                            new ImageIcon(), new String[]{"Recommencer", "Fermer le jeu"}, "Recommencer");
                    if (retour == 0) {
                        Yote.unPlateau.reinitialiser();
                    } else {
                        Yote.fenetrePrincipale.dispose();
                    }
                    return;
                }

                Yote.scoreBlanc.setText("Score Joueur Blanc \n" + valueOf(Yote.nbPionBlanc));
                Yote.scoreNoir.setText("Score Joueur Noir \n" + valueOf(Yote.nbPionNoir));

                Yote.fenetrePrincipale.repaint();
                Yote.fenetrePrincipale.validate();
                Fenetre.boutonAnnuler.setEnabled(true);

                Yote.joueur = couleurSuiv;
                Yote.etat = 0;

                if (!Yote.unPlateau.joueurPeutJouer(Yote.joueur)) {
                    JOptionPane.showMessageDialog(null,
                            "Le joueur " + (Yote.joueur == CouleurPion.blanc ? "blanc" : "noir") +
                                    " ne peut plus jouer. Il a perdu !");
                    Yote.unPlateau.reinitialiser();
                }
            }
        }
    }

    public String toString() {
        String s = "Case : (" + abscisse + "," + ordonnee + ") - ";
        if (isOccupe()) {
            s += "Pion : " + pion.toString();
        } else {
            s += "Case vide";
        }
        return s;
    }
}
