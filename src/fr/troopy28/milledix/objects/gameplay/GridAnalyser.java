package fr.troopy28.milledix.objects.gameplay;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.troopy28.milledix.objects.form.MDForm;
import fr.troopy28.milledix.objects.sound.Musician;
import fr.troopy28.milledix.objects.sound.SoundType;
import fr.troopy28.milledix.utils.ChatUtils;
/**
 * 
 * @author troopy28
 *
 */
public class GridAnalyser {

	private MDPlayer lastPayer;
	private MDForm form;
	private final String mMName;
	private boolean isLine;

	private static ArrayList<Block> blockToRemove;

	/**
	 * Constructeur permettant l'analyse avec un MDPlayer. La forme récupérée est donc celle possédée par ledit
	 * MDPlayer. L'analyse gérée par l'objet généré avec ce constructeur pourra donner des points.
	 * @param lastPayer
	 */
	public GridAnalyser(MDPlayer lastPayer){
		this.lastPayer = lastPayer;
		this.form = this.lastPayer.getForm();
		this.mMName = "MDMap"; //@AmauryPi Présent en dur, certes, mais pour initialiser la constante mMName
		this.isLine = false;
		blockToRemove = new ArrayList<Block>();
	}

	/**
	 * Constructeur permettant l'analyse sans passer par un MDPlayer. L'objet effectuera donc une analyse sans points.
	 * @param form Prend en paramètre la forme à tester.
	 */
	public GridAnalyser(MDForm form){
		this.form = form;
		this.mMName = "MDMap"; //Idem (cf ligne 38)
		this.isLine = false;
		blockToRemove = new ArrayList<Block>();
	}

	/**
	 * Analyse la grille de jeu. Vérifie la présence de lignes, de colonnes, et dans le cas où l'une d'entre 
	 * elles serait trouvée, supprime les blocs de ces lignes tout en donnant au joueur le nombre de points
	 * qui lui est dû.
	 * Dans le cas d'une ligne, le son correspondant à la ligne est joué pour tous les joueurs. Dans le cas 
	 * où il n'y a pas de ligne, le son correspondant au placement de bloc est joué pour tous les joueurs.
	 */
	public void analyseGrid(){
		checkLines();
		checkColumns();

		if(this.isLine){ //Il y a une ligne
			deleteBlocks();
			if(lastPayer != null)
				this.lastPayer.sendMessage(ChatUtils.getGamePrefix() + ChatColor.WHITE + "Vous avez un score de " + ChatColor.AQUA + this.lastPayer.getScore());		
			Musician musician = new Musician(SoundType.LINE);
			musician.playSoundForAllPlayers();			
		}
		else{ //Il n'y a pas de ligne
			Musician musician = new Musician(SoundType.PLACE);
			musician.playSoundForAllPlayers();
		}
	}

	/**
	 * Vérifie si la forme est plaçable sur la grille.
	 * Pour le déterminer, l'algorithme teste si il peut placer la forme sur chaque bloc de la grille.
	 * Si aucun bloc ne l'a permit, il en déduit que la forme ne peut être placée depuis aucun bloc et ne peut pas pas être placée.
	 * @return Renvoie true si la forme peut être placée. Renvoie false si elle ne peut pas l'être.
	 */
	public boolean checkCanPlace(){
		for(int j = 0; j < 10; j++){
			for(int i = 0; i < 10; i++){
				if(form.checkForm(new Location(Bukkit.getWorld(mMName), j, 4, i))){ //Si on peut placer... On met la hauteur à 4 (au Y de la location) car on soustrait 1 dans la fonction checkForm().
					return true; //On renvoie true
				}
			}
		}
		return false; //On renvoie false si on n'a pu placer la forme nul part
	}

	/**
	 * Vérifie pour chaque ligne de la grille si elle est remplie complètement de couleur, une ligne
	 * pouvant être composée de deux couleurs à la fois (sauf le noir, représentant l'absence de bloc).
	 * Pour chaque ligne trouvée, la fonction va placer tous les blocs de ladite ligne dans la liste des
	 * blocs à supprimer.
	 */
	@SuppressWarnings("deprecation")
	private void checkLines(){
		//On parcours les lignes verticales
		for(int i = 0; i < 10; i++){
			ArrayList<Block> lineBlock = new ArrayList<Block>();
			//Puis chaque bloc de chaque ligne verticale
			for(int j = 0; j < 10; j++){
				//Si le bloc est vert ou rouge ET qu'il s'agit de laine, on ajoute le bloc testé à lineBlock.
				if(new Location(Bukkit.getWorld(mMName), j, 3, i).getBlock().getData() == DyeColor.GREEN.getData() || new Location(Bukkit.getWorld(mMName), j, 3, i).getBlock().getData() == DyeColor.RED.getData()){
					lineBlock.add(new Location(Bukkit.getWorld(mMName), j, 3, i).getBlock());
				}else{ //Si on a rencontré un bloc qui n'est pas du vert ou du rouge, on efface le tableau lineBlock pour retester une autre ligne, donc on sort du for.
					lineBlock.clear();
					break;
				}				
			}
			/* Si il y a 10 blocs dans le tableau, c'est qu'une ligne a été
			 * trouvée, une ligne étant composée de 10 blocs. On va donc 
			 * ajouter les blocs de la ligne à blockToRemove. On ne les
			 * supprime pas afin de ne pas empêcher une éventuelle ligne
			 * croisée : 
			 * 
			 * 		x o o o 
			 * 		o
			 * 		o
			 * 		o
			 * 
			 * On efface ensuite le tableau lineBlock, les blocs étant maintenant
			 * dans la liste des blocs à supprimer.
			 */
			if(lineBlock.size() == 10){ 
				this.isLine = true;
				for(Block block : lineBlock){
					blockToRemove.add(block);
				}				
			}
			lineBlock.clear();

		}
	}
	
	/**
	 * Vérifie pour chaque colonne de la grille si elle est remplie complètement de couleur, une colonne
	 * pouvant être composée de deux couleurs à la fois (sauf le noir, représentant l'absence de bloc).
	 * Pour chaque colonne trouvée, la fonction va placer tous les blocs de ladite colonne dans la liste des
	 * blocs à supprimer.
	 */
	@SuppressWarnings("deprecation")
	private void checkColumns(){

		/*
		 * L'algorithme étant très similaire à celui de vérification des lignes, je ne le recommente pas.
		 * La seule différence réside en l'inversion des coordonnées i et j dans la location.
		 */
		for(int i = 0; i < 10; i++){

			ArrayList<Block> lineBlock = new ArrayList<Block>();

			for(int j = 0; j < 10; j++){
				if(new Location(Bukkit.getWorld(mMName), i, 3, j).getBlock().getData() == DyeColor.GREEN.getData() || new Location(Bukkit.getWorld(mMName), i, 3, j).getBlock().getData() == DyeColor.RED.getData()){
					lineBlock.add(new Location(Bukkit.getWorld(mMName), i, 3, j).getBlock());
				}else{ //On a rencontré un bloc qui n'est pas du vert ou du rouge
					lineBlock.clear();
					break;
				}				
			}
			if(lineBlock.size() == 10){
				this.isLine = true;
				for(Block block : lineBlock){
					blockToRemove.add(block);
				}				
			}
			lineBlock.clear();
		}
	}

	/**
	 * Supprime l'intégralité des blocs contenus dans la liste des blocs à supprimer. Pour chaque bloc 
	 * supprimé, elle ajoute un point au joueur passé en paramètre dans le constructeur.
	 */
	@SuppressWarnings("deprecation")
	private void deleteBlocks(){
		for(Block b : blockToRemove){		
			b.setTypeIdAndData(35, DyeColor.BLACK.getData(), true); 
			if(lastPayer != null)			
				this.lastPayer.increaseScore(1);
		}
	}

	/**
	 * Cette fonction est une sorte de ramasse miette au niveau d'éventuels blocs de clignotement bugués.
	 * Pour chaque bloc de fer présent sur la liste lors de la vérification, elle va le remplacer par un
	 * bloc de laine noire, afin d'effacer sa présence.
	 * Cette fonction permet de récupérer un éventuel bug non géré pouvant survenir durant la partie.
	 */
	@SuppressWarnings("deprecation")
	public void killBadIron(){
		for(int j = 0; j < 10; j++){		
			for(int i = 0; i < 10; i++){
				if(new Location(Bukkit.getWorld(mMName), j, 3, i).getBlock().getType() == Material.IRON_BLOCK)
					new Location(Bukkit.getWorld(mMName), j, 3, i).getBlock().setTypeIdAndData(Material.WOOL.getId(), DyeColor.BLACK.getData(), true);
			}

		}
	}


}
