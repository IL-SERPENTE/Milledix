package fr.troopy28.milledix.objects.form;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.troopy28.milledix.Main;
import fr.troopy28.milledix.objects.gameplay.GridAnalyser;
/** 
 * @author troopy28
 */
public class MDForm {
	
	private FormType formType;
	private ChatColor formColor;
	private byte[][] formMap = {{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
	private byte x = 0;
	private int tabSize = 0;
	private MDForm instance;
	private ArrayList<Location> toReplace = new ArrayList<Location>();
	private ArrayList<Material> toReset = new ArrayList<Material>();
	private ArrayList<Byte> colorDatas = new ArrayList<Byte>();
	
	/**
	 * <h2>Constructeur</h2>. Construit l'objet avec les paramètres spécifiés.
	 * @param formType Type de forme à attribuer à cet objet. La forme
	 * affichée au sol est définie par le type de forme.
	 * @param color Couleur de la forme, au format ChatColor.
	 */
	public MDForm(FormType formType, ChatColor color){
		this.formType = formType;
		this.formColor = color;
		buildFormMap();
		this.instance = this;
	}
	
	/**
	 * Construit le tableau formMap selon le type de forme
	 * attribué à l'objet. Si le type de forme est aléatoire ou
	 * non valide, on ré-effectue une sélection du type de forme
	 * et une construction du tableau formMap
	 */
	private void buildFormMap(){   //Il y a 12 types de formes. On dépasse donc forcément la complexité maximale de SonarLint
		
		switch(formType){
		case BOT_LEFT_CORNER:
			buildBotLeft();
			break;
		case BOT_RIGHT_CORNER:
			buildBotRight();
			break;
		case CUBE_1:
			buildCubeOne();
			break;
		case CUBE_2:
			buildCubeTwo();
			break;
		case CUBE_3:
			buildCubeThree();
			break;
		case HORIZONTAL_LINE_2:
			buildHorizontalLineTwo();
			break;
		case HORIZONTAL_LINE_3:
			buildHorizontalLineThree();
			break;
		case HORIZONTAL_LINE_4:
			buildHorizontalLineFour();
			break;
		case RANDOM:
			selectIfNotFound();
			buildFormMap();	
			break;
		case TOP_LEFT_CORNER:
			buildTopLeft();
			break;
		case TOP_RIGHT_CORNER:
			buildTopRight();
			break;
		case VERTICAL_LINE_2:
			buildVerticalLineTwo();
			break;
		case VERTICAL_LINE_3:
			buildVerticalLineThree();
			break;
		case VERTICAL_LINE_4:
			buildVerticalLineFour();
			break;
		default:
			selectIfNotFound();
			buildFormMap();	
			break;
		}
		
	}
	
	/*---------------BUILDING---------------*/
	
	
	/**
	 * Construit le tableau pour le type de forme BOT_LEFT_CORNER
	 */
	private void buildBotLeft(){
		formMap[0][0] = 0; //Coordonnées X
		formMap[0][0] = 0; //Coordonnées Z
		
		formMap[0][1] = 0;
		formMap[1][1] = 1;	
		
		formMap[1][2] = 0;
		formMap[0][2] = 1;	
		this.tabSize = 3;
	}
	
	/**
	 * Construit le tableau pour le type de forme BOT_RIGHT_CORNER
	 */
	private void buildBotRight(){
		formMap[0][0] = 0; //Coordonnées X
		formMap[0][0] = 0; //Coordonnées Z
		
		formMap[0][1] = 0;
		formMap[1][1] = 1;	
		
		formMap[1][2] = 0;
		formMap[0][2] = -1;	
		this.tabSize = 3;
	}
	
	/**
	 * Construit le tableau pour le type de forme CUBE_1
	 */
	private void buildCubeOne(){
		formMap[0][0] = 0; //Coordonnées X
		formMap[0][0] = 0; //Coordonnées Z
	
		this.tabSize = 1;
	}
	
	/**
	 * Construit le tableau pour le type de forme CUBE_2
	 */
	private void buildCubeTwo(){
		formMap[0][0] = 0; //Coordonnées X
		formMap[0][0] = 0; //Coordonnées Z
		
		formMap[0][1] = 0;
		formMap[1][1] = 1;	
		
		formMap[1][2] = 0;
		formMap[0][2] = 1;	
		
		formMap[0][3] = 0;
		formMap[1][3] = -1;
				
		this.tabSize = 4;
	}
	
	/**
	 * Construit le tableau pour le type de forme CUBE_3
	 */
	private void buildCubeThree(){
		formMap[0][0] = 0; //Coordonnées X
		formMap[0][0] = 0; //Coordonnées Z
		
		formMap[0][1] = 0;
		formMap[1][1] = 1;	
		
		formMap[1][2] = 0;
		formMap[0][2] = 1;	
		
		formMap[0][3] = 0;
		formMap[1][3] = -1;
		
		formMap[0][4] = 0;
		formMap[1][4] = -1;
		
		formMap[0][5] = -1;
		formMap[1][5] = 0;
		
		formMap[0][6] = 2;
		formMap[1][6] = 0;
		
		formMap[0][7] = 0;
		formMap[1][7] = 1;

		formMap[0][8] = 0;
		formMap[1][8] = 1;
			
		this.tabSize = 9;
	}
	
	/**
	 * Construit le tableau pour le type de forme HORIZONTAL_LINE_2
	 */
	private void buildHorizontalLineTwo(){
		formMap[0][0] = 0; //Coordonnées X
		formMap[0][0] = 0; //Coordonnées Z
		
		formMap[1][1] = 0;
		formMap[0][1] = 1;	
		
		this.tabSize = 2;
	}
	
	/**
	 * Construit le tableau pour le type de forme HORIZONTAL_LINE_3
	 */
	private void buildHorizontalLineThree(){
		formMap[0][0] = 0; //Coordonnées X
		formMap[0][0] = 0; //Coordonnées Z
		
		formMap[1][1] = 0;
		formMap[0][1] = 1;
		
		formMap[1][2] = 0;
		formMap[0][2] = 1;
		
		this.tabSize = 3;
	}
	
	/**
	 * Construit le tableau pour le type de forme HORIZONTAL_LINE_4
	 */
	private void buildHorizontalLineFour(){
		formMap[0][0] = 0; //Coordonnées X
		formMap[0][0] = 0; //Coordonnées Z
		
		formMap[1][1] = 0;
		formMap[0][1] = 1;
		
		formMap[1][2] = 0;
		formMap[0][2] = 1;
		
		formMap[1][3] = 0;
		formMap[0][3] = 1;
		
		this.tabSize = 4;
	}
	
	/**
	 * Construit le tableau pour le type de forme TOP_LEFT_CORNER 
	 */
	private void buildTopLeft(){
		formMap[0][0] = 0; //Coordonnées X
		formMap[0][0] = 0; //Coordonnées Z
		
		formMap[0][1] = 0;
		formMap[1][1] = 1;	
		
		formMap[1][2] = 0;
		formMap[0][2] = -1;	
		this.tabSize = 3;
	}
	
	/**
	 * Construit le tableau pour le type de forme TOP_RIGHT
	 */
	private void buildTopRight(){
		formMap[0][0] = 0; //Coordonnées X
		formMap[0][0] = 0; //Coordonnées Z
		
		formMap[0][1] = 0;
		formMap[1][1] = -1;	
		
		formMap[1][2] = 0;
		formMap[0][2] = -1;	
		this.tabSize = 3;
	}
	
	/**
	 * Construit le tableau pour le type de forme VERTICAL_LINE_2
	 */
	private void buildVerticalLineTwo(){
		formMap[0][0] = 0; //Coordonnées X
		formMap[0][0] = 0; //Coordonnées Z
		
		formMap[0][1] = 0;
		formMap[1][1] = 1;	
		
		this.tabSize = 2;
	}
	
	/**
	 * Construit le tableau pour le type de forme VERTICAL_LINE_3
	 */
	private void buildVerticalLineThree(){
		formMap[0][0] = 0; //Coordonnées X
		formMap[0][0] = 0; //Coordonnées Z
		
		formMap[0][1] = 0;
		formMap[1][1] = 1;
		
		formMap[0][2] = 0;
		formMap[1][2] = 1;
		
		this.tabSize = 3;
	}
	
	/**
	 * Construit le tableau pour le type de forme VERTICAL_LINE_4
	 */
	private void buildVerticalLineFour(){
		formMap[0][0] = 0; //Coordonnées X
		formMap[0][0] = 0; //Coordonnées Z
		
		formMap[0][1] = 0;
		formMap[1][1] = 1;
		
		formMap[0][2] = 0;
		formMap[1][2] = 1;
		
		formMap[0][3] = 0;
		formMap[1][3] = 1;
		
		this.tabSize = 4;
	}
	
	/**
	 * Choisit un type de forme aléatoire 
	 */
	private void selectIfNotFound(){
		Random rnd = new Random();
		int y = rnd.nextInt(13);
		
		//Pour chaque valeur possible, on attribue une forme
		this.formType = formType.getFormFromValue(y);
	}
	
	/**
	 * Fait clignoter la forme sur la location du joueur.
	 * @param p Joueur dont la location servira de base au clignotement.
	 */
	public void highlightForm(Player p){
		highlightForm(p.getLocation());	
	}
	
	/**
	 * Fait clignoter la forme sur la location loc. Afin de déterminer
	 * quels blocs devront être mis en surbrillance, la fonction lit le tableau
	 * formMap.
	 * @param loc Location qui servira de base au clignotement.
	 */
	@SuppressWarnings("deprecation")
	public void highlightForm(Location loc){
		
		clearAllArrayLists();		
		int locH = loc.getBlock().getY(); //La hauteur où le joueur est placé
		int locMaxH = loc.getWorld().getHighestBlockAt(loc).getY(); //La hauteur du bloc le plus haut sur son XZ
		
		loc.subtract(0, Math.abs((double)locMaxH-(double)locH)+1, 0); //On récupère la distance pour afficher les blocs sur la grille et pas au dessus ou en dessous
		
		boolean firstZeroZero = true;
		
		//On parcourt notre tableau pour tracer la forme
		while(x < tabSize){			
						
			/*
			 * Si formMap[0][x] et formMap[1][x] valent tous deux zéro, c'est que nous
			 * sommes arrivés à un point où il n'y a plus d'évolution dans la gestion
			 * des coordonnées des blocs à faire clignoter. On peut donc s'arrêter là
			 * car les autres vaudront soit tous soit zéro, soit null.
			 */
			if(formMap[0][x] == 0 && formMap[1][x] == 0){ //Si les deux valent 0
				if(firstZeroZero) //Si c'est la première fois...
					firstZeroZero = false;		//On passe			
				else{					
				break;	//Sinon on sort du while
				}
			}
			
			loc.add(formMap[0][x], 0, formMap[1][x]); //On reprend notre location
			
			//On ajoute ici nos blocs qui seront replacés à la fin du clignotement
			toReset.add(loc.getBlock().getType());
			colorDatas.add(loc.getBlock().getData());					
			loc.getBlock().setType(Material.IRON_BLOCK);		
			toReplace.add(loc.getBlock().getLocation());
			
			x++;		
		}
		
		x = 0;
		
		//Et on replace les blocs
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable(){		
			public void run() {				
				for(int i = 0; i < tabSize; i++){
					toReplace.get(i).getBlock().setTypeIdAndData(toReset.get(i).getId(), colorDatas.get(i), true);
				}
				if(!toReplace.isEmpty())
					toReplace.clear();
				if(!toReset.isEmpty())
					toReset.clear();
				if(!colorDatas.isEmpty())
					colorDatas.clear();
			
				GridAnalyser ga = new GridAnalyser(instance);
				ga.killBadIron();				
			}		
		}, 20*1L);
	
	}

	/**
	 * Place la forme sur la location sur joueur.
	 * @param p Joueur dont la location servira de base au placement.
	 */
	public void placeForm(Player p){
		placeForm(p.getLocation());
	}
	
	/**
	 * Placea forme sur la location loc. Afin de déterminer
	 * quels blocs devront être changés, la fonction lit le tableau
	 * formMap.
	 * @param loc Location qui servira de base au placement.
	 */
	@SuppressWarnings("deprecation")
	public void placeForm(Location loc){
			
		int locH = loc.getBlock().getY();
		int locMaxH = loc.getWorld().getHighestBlockAt(loc).getY();
		
		loc.subtract(0, Math.abs((double)locMaxH-(double)locH)+1, 0); //On r�cup�re la distance
			
		boolean firstZeroZero = true;
		
		//On parcourt notre tableau pour tracer la forme
		while(x < tabSize){			

			/*
			 * Si formMap[0][x] et formMap[1][x] valent tous deux zéro, c'est que nous
			 * sommes arrivés à un point où il n'y a plus d'évolution dans la gestion
			 * des coordonnées des blocs à faire clignoter. On peut donc s'arrêter là
			 * car les autres vaudront soit tous soit zéro, soit null.
			 */
			if(formMap[0][x] == 0 && formMap[1][x] == 0){ //Si les deux valent 0
				if(firstZeroZero) //Si c'est la première fois...
					firstZeroZero = false;	//On passe
				else{
					break;	//Sinon on sort
				}
			}
			
			loc.add(formMap[0][x], 0, formMap[1][x]); //On reprend notre location
				
			if(this.formColor == ChatColor.GREEN)			
				loc.getBlock().setTypeIdAndData(35, DyeColor.GREEN.getData(), true);
			
			/* Actuellement, il n'y a que deux couleurs. Un simple else peut donc permettre
			 * de faire une vérification correcte de la "couleur de la forme.
			 */	
			else
				loc.getBlock().setTypeIdAndData(35, DyeColor.RED.getData(), true);
					
			x++;		
		}
		
	}
	
	/**
	 * Vérifie si la forme peut être placée sur la location d'un joueur.
	 * @param p Joueur dont on va regarder la location.
	 * @return Renvoie <b>true</b> si la forme peut être placée et renvoie <b>false</b> sinon.
	 */
	public boolean checkForm(Player p){
		if(checkForm(p.getLocation()))
			return true;		
		return false;			
	}
	
	/**
	 * Vérifie si la forme peut être placée sur une location donnée.
	 * @param loc Location qui va servir de base à la vérification.
	 * @return Renvoie <b>true</b> si la forme peut être placée et renvoie <b>false</b> sinon.
	 */
	@SuppressWarnings("deprecation")
	public boolean checkForm(Location loc){ 
		
		int locH = loc.getBlock().getY(); //La hauteur o� le joueur est plac�
		int locMaxH = loc.getWorld().getHighestBlockAt(loc).getY(); //La hauteur du bloc le plus haut sur son XZ
		
		loc.subtract(0, Math.abs((double)locMaxH-(double)locH)+1, 0); //On r�cup�re la distance
		
		boolean firstZeroZero = true;
		
		//On parcourt notre tableau pour tracer la forme
		while(x < tabSize){						
			//Si on tombe sur un z�ro z�ro mais que ce n'est pas le premier...
			if(formMap[0][x] == 0 && formMap[1][x] == 0){
				if(firstZeroZero) //Si c'est le premier...
					firstZeroZero = false;		//On passe			
				else{					
					break;	//Sinon on sort
				}
			}			
			loc.add(formMap[0][x], 0, formMap[1][x]); //On reprend notre location			
			
			//On ajoute ici nos blocs qui seront replac�s � la fin du clignotement
			toReset.add(loc.getBlock().getType());
			colorDatas.add(loc.getBlock().getData());			
			
			toReplace.add(loc.getBlock().getLocation());			
			x++;		
		}		
		x = 0;
		
		return checkMatAndDatas();

		
	}
	
	/**
	 * Gère la contenance des tableaux de matériaux et de datas des blocs sur
	 * les locations à placer ou faire clignoter.
	 * E
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private boolean checkMatAndDatas(){
		for(Material mat : toReset){
			if(mat != Material.WOOL){
				clearAllArrayLists();
				return false;
			}				
		}
		
		for(Byte b : colorDatas){
			if(b != DyeColor.BLACK.getData()){
				clearAllArrayLists();
				return false;
			}
		}
		//A ce stade, on n'est pas encore reset si aucun bloc de couleur n'a été trouvé
		//On peut donc renvoyer true
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public boolean checkHighlight(Location loc){ 
		
		int locH = loc.getBlock().getY(); //La hauteur o� le joueur est plac�
		int locMaxH = loc.getWorld().getHighestBlockAt(loc).getY(); //La hauteur du bloc le plus haut sur son XZ
		
		loc.subtract(0, Math.abs((double)locMaxH-(double)locH)+1, 0); //On r�cup�re la distance
		
		boolean firstZeroZero = true;
		
		//On parcourt notre tableau pour tracer la forme
		while(x < tabSize){						
			//Si on tombe sur un z�ro z�ro mais que ce n'est pas le premier...
			if(formMap[0][x] == 0 && formMap[1][x] == 0){
				if(firstZeroZero) //Si c'est le premier...
					firstZeroZero = false;		//On passe			
				else{					
					break;	//Sinon on sort
				}
			}			
			loc.add(formMap[0][x], 0, formMap[1][x]); //On reprend notre location			
			
			//On ajoute ici nos blocs qui seront replac�s � la fin du clignotement
			toReset.add(loc.getBlock().getType());
			colorDatas.add(loc.getBlock().getData());			
			
			toReplace.add(loc.getBlock().getLocation());			
			x++;		
		}		
		x = 0;
		
		for(Material mat : toReset){
			if(mat != Material.WOOL){
				clearAllArrayLists();
				return false;
			}				
		}

		//A ce stade, on n'est pas encore reset si aucun bloc autre que de la laine n'a �t� trouv� => sur la grille
		return true; //Donc on peut renvoyer true!
	}
	
	public void clearAllArrayLists(){
		toReplace.clear();
		toReset.clear();
		colorDatas.clear();
	}
	
	/*GETTERS ET SETTERS*/
	
	/**
	 * @return Renvoie le type de forme représenté par cet objet.
	 */
	public FormType getFormType() {
		return formType;
	}
	
	/**
	 * Définit le type de forme sur celui passé en paramètre, et renconstruit le 
	 * tableau formMap.
	 * @param formType Nouveau type de forme qui sera appliqué à cette forme.
	 */
	public void setFormType(FormType formType) {
		this.formType = formType;
		buildFormMap();
	}
	
	/**
	 * @return Renvoie la couleur de la forme
	 */
	public ChatColor getFormColor() {
			return this.formColor;
		}
	
	/**
	 * Définit la couleur de la forme sur la couleur (ChatColor) passée
	 * en paramètre.
	 * @param formColor Nouvelle couleur à appliquer à la forme.
	 */
	public void setFormColor(ChatColor formColor) {
			this.formColor = formColor;
	}
		
			
		
	
}
