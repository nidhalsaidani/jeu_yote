public class Pion
{
    private int mvt=1;//amplitude mouvement horizontal et vertical autorisé pour un déplacement
    private int mvtPrise=2;//amplitude mouvement horizontal et vertical autorisé pour une prise

    private CouleurPion couleur;//blanc, noir

    public Pion(CouleurPion couleur){
        this.couleur=couleur;
        if (couleur==CouleurPion.blanc){
            mvt=1;
            mvtPrise=2;
        }
        else if (couleur==CouleurPion.noir){
            mvt=1;
            mvtPrise=2;
        }
        else{
            System.out.println("Erreur de couleur");
        }
	
    }
    
    public int getMvt() {
        return mvt;
    }
    public int getMvtPrise() {
        return mvtPrise;

    }

    public CouleurPion getCouleur() {
        return couleur;

    }

    public String toString(){
        return "Pion de couleur " + couleur + " avec mvt = " + mvt + " et mvtPrise = " + mvtPrise;

    }

}
