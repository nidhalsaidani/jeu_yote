// Project: Pion
// Updated: 19/05/2025
// Modified by: Nidhal Saïdani

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class StockPion extends JPanel {
    private Case[][] monStockPion;
    private CouleurPion couleur;

    public StockPion(CouleurPion couleur) {
        this.couleur = couleur;

        // Disposition en 4 lignes x 3 colonnes
        setLayout(new GridLayout(4, 3));
        setPreferredSize(new Dimension(180, 240));
        setBackground(Color.white);
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.black),
                "Pions " + (couleur == CouleurPion.blanc ? "Blancs" : "Noirs"),
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                (couleur == CouleurPion.blanc ? Color.gray : Color.darkGray)
        ));

        monStockPion = new Case[4][3];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                monStockPion[i][j] = new Case(Color.white, i, j, 1);
                monStockPion[i][j].setPion(new Pion(couleur)); // Initialise avec un pion
                add(monStockPion[i][j]);
            }
        }
    }

    public void reinitialiserStock() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                monStockPion[i][j].setPion(new Pion(couleur));
            }
        }
    }

    public int getNbPions() {
    int count = 0;
    for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 3; j++) {
            if (monStockPion[i][j].getPion() != null) {
                count++;
            }
        }
    }
    return count;
}
}
