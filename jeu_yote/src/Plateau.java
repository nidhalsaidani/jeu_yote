

import javax.swing.*;
import java.awt.*;

import static java.lang.String.valueOf;

public class Plateau extends JPanel {

    private Case[][] monPlateau;

    public Plateau() {
        setLayout(new GridLayout(6, 5));
        setPreferredSize(new Dimension(600, 600));
        setBackground(Color.white);

        monPlateau = new Case[6][5];

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                monPlateau[i][j] = new Case(Color.white, i, j, 0);
                add(monPlateau[i][j]);
            }
        }
    }

    public boolean isIn(Case c) {
        for (int i = 0; i < monPlateau.length; i++) {
            for (int j = 0; j < monPlateau[i].length; j++) {
                if (monPlateau[i][j] == c) return true;
            }
        }
        return false;
    }


    public int coupValide(Case dep, Case arr) {
    if (isIn(dep) && isIn(arr)) {
        if (dep.getPion() != null && dep.getPion().getCouleur() == Yote.joueur) {

            // Récupérer la dernière case de départ du joueur
            Case derniereCase = (Yote.joueur == CouleurPion.blanc) ? Yote.derniereCaseBlanc : Yote.derniereCaseNoir;

            // Si on veut revenir à la dernière case, c'est interdit sauf si c'est une prise
            if (arr == derniereCase) {
                // Autoriser uniquement si c'est une prise
                if (!(verifieDeplacementHorPrise(dep, arr) || verifieDeplacementVertPrise(dep, arr))) {
                    return 0; // Interdit pour déplacement simple
                }
            }

            if (verifieDeplacementHor(dep, arr) || verifieDeplacementVert(dep, arr)) {
                return 1; // déplacement simple valide
            } else if (verifieDeplacementHorPrise(dep, arr) || verifieDeplacementVertPrise(dep, arr)) {
                return 2; // prise valide
            }
        }
    }
    return 0; // coup invalide
}


    public boolean verifieDeplacementVertPrise(Case dep, Case arr) {
        int xDep = dep.getAbscisse();
        int yDep = dep.getOrdonnee();
        int xArr = arr.getAbscisse();
        int yArr = arr.getOrdonnee();

        if (Math.abs(xDep - xArr) == 2 && yDep == yArr) {
            int xInter = (xDep + xArr) / 2;
            Case inter = monPlateau[xInter][yDep];
            return inter.isOccupe()
                    && inter.getPion().getCouleur() != dep.getPion().getCouleur()
                    && !arr.isOccupe();
        }
        return false;
    }

    public boolean verifieDeplacementHorPrise(Case dep, Case arr) {
        int xDep = dep.getAbscisse();
        int yDep = dep.getOrdonnee();
        int xArr = arr.getAbscisse();
        int yArr = arr.getOrdonnee();

        if (Math.abs(yDep - yArr) == 2 && xDep == xArr) {
            int yInter = (yDep + yArr) / 2;
            Case inter = monPlateau[xDep][yInter];
            return inter.isOccupe()
                    && inter.getPion().getCouleur() != dep.getPion().getCouleur()
                    && !arr.isOccupe();
        }
        return false;
    }

    public boolean verifieDeplacementVert(Case dep, Case arr) {
        int xDep = dep.getAbscisse();
        int yDep = dep.getOrdonnee();
        int xArr = arr.getAbscisse();
        int yArr = arr.getOrdonnee();

        return Math.abs(xDep - xArr) == 1 && yDep == yArr && !arr.isOccupe();
    }

    public boolean verifieDeplacementHor(Case dep, Case arr) {
        int xDep = dep.getAbscisse();
        int yDep = dep.getOrdonnee();
        int xArr = arr.getAbscisse();
        int yArr = arr.getOrdonnee();

        return Math.abs(yDep - yArr) == 1 && xDep == xArr && !arr.isOccupe();
    }

    public Case casePrise(Case dep, Case arr) {
        int xDep = dep.getAbscisse();
        int yDep = dep.getOrdonnee();
        int xArr = arr.getAbscisse();
        int yArr = arr.getOrdonnee();

        if (Math.abs(xDep - xArr) == 2 && yDep == yArr) {
            return monPlateau[(xDep + xArr) / 2][yDep];
        } else if (Math.abs(yDep - yArr) == 2 && xDep == xArr) {
            return monPlateau[xDep][(yDep + yArr) / 2];
        }
        return null;
    }

    public void jouerCoupPrise(Case dep, Case arr) {
        Case cPrise = casePrise(dep, arr);
        if (cPrise != null) {
            cPrise.setPion(null);
        }
        arr.setPion(dep.getPion());
        dep.setPion(null);
        Yote.caseDep.setBorder(Yote.empty);

        // Mémorisation
        if (Yote.joueur == CouleurPion.blanc) {
            Yote.derniereCaseBlanc = dep;
        } else {
            Yote.derniereCaseNoir = dep;
        }

        Yote.etat = 2;  // attente de sélection de pion à prendre
    }


    public void jouerCoup(Case dep, Case arr) {
        arr.setPion(dep.getPion());
        dep.setPion(null);
        Yote.caseDep.setBorder(Yote.empty);

        // Mémorisation
        if (Yote.joueur == CouleurPion.blanc) {
            Yote.derniereCaseBlanc = dep;  // <- On retient la case d'où le joueur est parti
        } else {
            Yote.derniereCaseNoir = dep;
        }

        // Changement de joueur
        Yote.joueur = (Yote.joueur == CouleurPion.blanc) ? CouleurPion.noir : CouleurPion.blanc;
        Yote.etat = 0;
    }




    public void annulerCoup(Case dep, Case arr) {
        if (arr.getPion() != null) {
            dep.setPion(arr.getPion());
            arr.setPion(null);
        }
    }

    public boolean joueurPeutJouer(CouleurPion couleur) {
        boolean aAuMoinsUnPionSurPlateau = false;

        for (int i = 0; i < monPlateau.length; i++) {
            for (int j = 0; j < monPlateau[i].length; j++) {
                Case c = monPlateau[i][j];

                if (c.isOccupe() && c.getPion().getCouleur() == couleur) {
                    aAuMoinsUnPionSurPlateau = true;

                    Case derniereCase = (couleur == CouleurPion.blanc) ? Yote.derniereCaseBlanc : Yote.derniereCaseNoir;

                    int x = c.getAbscisse();
                    int y = c.getOrdonnee();

                    // Déplacements simples (interdiction de revenir sur la dernière case)
                    if (x > 0 && !monPlateau[x - 1][y].isOccupe() && monPlateau[x - 1][y] != derniereCase) return true;
                    if (x < monPlateau.length - 1 && !monPlateau[x + 1][y].isOccupe() && monPlateau[x + 1][y] != derniereCase) return true;
                    if (y > 0 && !monPlateau[x][y - 1].isOccupe() && monPlateau[x][y - 1] != derniereCase) return true;
                    if (y < monPlateau[0].length - 1 && !monPlateau[x][y + 1].isOccupe() && monPlateau[x][y + 1] != derniereCase) return true;

                    // Prises possibles (autorisé même vers la case précédente)
                    if (x > 1 && verifieDeplacementVertPrise(c, monPlateau[x - 2][y])) return true;
                    if (x < monPlateau.length - 2 && verifieDeplacementVertPrise(c, monPlateau[x + 2][y])) return true;
                    if (y > 1 && verifieDeplacementHorPrise(c, monPlateau[x][y - 2])) return true;
                    if (y < monPlateau[0].length - 2 && verifieDeplacementHorPrise(c, monPlateau[x][y + 2])) return true;
                }
            }
        }

        // Si pas de pion sur le plateau, autoriser jeu si stock disponible
        if (!aAuMoinsUnPionSurPlateau) {
            if (couleur == CouleurPion.blanc) return Yote.stockBlanc.getNbPions() > 0;
            if (couleur == CouleurPion.noir) return Yote.stockNoir.getNbPions() > 0;
        }

        return false;
    }

    


    public void setMonPlateau(Case[][] monPlateau) {
        this.monPlateau = monPlateau;
    }


    public void reinitialiser() {
        for (int i = 0; i < monPlateau.length; i++) {
            for (int j = 0; j < monPlateau[i].length; j++) {
                monPlateau[i][j].setPion(null);
                monPlateau[i][j].setBackground(Color.white);
                monPlateau[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
            }
        }

        Yote.nbPionBlanc = 12;
        Yote.nbPionNoir = 12;
        Yote.joueur = CouleurPion.blanc;
        Yote.etat = 0;

        Yote.stockBlanc.reinitialiserStock();
        Yote.stockNoir.reinitialiserStock();

        Yote.scoreBlanc.setText("Score Joueur Blanc \n" + valueOf(Yote.nbPionBlanc));
        Yote.scoreNoir.setText("Score Joueur Noir \n" + valueOf(Yote.nbPionNoir));

        Yote.fenetrePrincipale.repaint();
        Yote.fenetrePrincipale.validate();
    }

}
