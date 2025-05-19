// Updated by Nidhal Saïdani 13/05/2025

/*
 *  La classe Fenetre qui étend la classe JFrame. Elle est composée de deux boutons (annuler et
 reinitialiser). Elle permet d'a cher le plateau et les deux stocks de pions. Elle a chera
 aussi le nombre de pions restants pour chaque joueur. Elle possède un constructeur et la
 méthode ajouterComposants qui ajoute ses attributs à la fenêtre a chée.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.String.valueOf;


public class Fenetre extends JFrame {

  public static JButton boutonAnnuler = new JButton("Annuler");
  public static JButton boutonReinit = new JButton("Reinitialiser");

  public Fenetre(String name) {
    super(name);
    this.setSize(2500, 2500);
    }

  public void ajouterComposants(final Container pane) {

    FlowLayout structureBouton = new FlowLayout(FlowLayout.CENTER);
    FlowLayout structurePlateau = new FlowLayout(FlowLayout.CENTER);
    FlowLayout structureScore = new FlowLayout(FlowLayout.CENTER);

    JPanel fondBouton = new JPanel();
    fondBouton.setLayout(structureBouton);
    fondBouton.add(boutonAnnuler);
    fondBouton.add(boutonReinit);

    //Définition de l'action du bouton Annuler
    boutonAnnuler.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent event){
        Yote.unPlateau.annulerCoup(Yote.caseDep, Yote.caseArr);
      }
    });
    //Définition de l'action du bouton Réinitialiser
    boutonReinit.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent event){
        Yote.unPlateau.reinitialiser();
      }
    });
    pane.add(fondBouton, BorderLayout.NORTH);

    JPanel fondPlateau = new JPanel();
    fondPlateau.setLayout(structurePlateau);

    Yote.unPlateau = new Plateau();
    Yote.stockBlanc = new StockPion(CouleurPion.blanc);
    Yote.stockNoir = new StockPion(CouleurPion.noir);
    fondPlateau.add(Yote.stockBlanc);
    fondPlateau.add(Yote.unPlateau);
    fondPlateau.add(Yote.stockNoir);
    pane.add(fondPlateau, BorderLayout.CENTER);

    JPanel score =  new JPanel();
    score.setLayout(structureScore);
    Yote.scoreBlanc = new JLabel("Score Joueur Blanc \n "+ valueOf(Yote.nbPionBlanc));
    Yote.scoreNoir = new JLabel("Score Joueur Noir \n" + valueOf(Yote.nbPionNoir));
    score.add(Yote.scoreBlanc);
    score.add(Yote.scoreNoir);
    pane.add(score, BorderLayout.SOUTH);
    }

}

