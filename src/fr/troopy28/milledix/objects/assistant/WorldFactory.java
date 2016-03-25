package fr.troopy28.milledix.objects.assistant;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import fr.troopy28.milledix.Main;

/**
 * @author troopy28
 */
public class WorldFactory {

	private WorldCreator mapMaker;
	private String mMName;
	
	/**
	 * @param mMName Le nom de la map sera défini par ce paramètre.
	 */
	public WorldFactory(String mMName){
		this.mMName = mMName;
		this.mapMaker = new WorldCreator(mMName);
	}
	
	/**
	 * Génère le monde s'il n'existe pas déjà.
	 * Le monde est plat, sans structures, et dans un monde normal
	 * (overworld). Efface ensuite les entités sauf les joueurs, et
	 * construit la grille de jeu.
	 */
	public void generateWorld(){
		
		if(Bukkit.getWorld(mMName) == null){
	        mapMaker.environment(Environment.NORMAL);
	        mapMaker.generateStructures(false);
	        mapMaker.type(WorldType.FLAT);
	        mapMaker.createWorld();
	        buildGrid();
		}
		
       
        try{
        	cleanWorld();      	 
        } catch(Exception e){
        	Main.getInstance().getLogger().info("=====================");
        	Main.getInstance().getLogger().info("/!\\ UNE ERREUR EST SURVENUE LORS DE LA PREPARATION DE LA MAP /!\\");
        	Log.trace(e);
        	Main.getInstance().getLogger().info("=====================");
        }
		return;   
	}
	
	/**
	 * Supprime les entités du monde définit dans le constructeur.
	 * Met le temps du monde sur 1000, et charge le chunk 0-0 afin
	 * que les joueurs puissent apparaître.
	 */
	public void cleanWorld(){
		for(Entity ent : Bukkit.getWorld(mMName).getEntities()){
			if(ent.getType() != EntityType.PLAYER)
				ent.remove();
		}
				
		Bukkit.getWorld(mMName).getEntities().clear();		
		Bukkit.getWorld(mMName).setTime(1000);
		Bukkit.getWorld(mMName).loadChunk(0, 0);
	}
	
	/**
	 * Construit la grille de jeu en replissant les coordonnées 0-0 
	 * à 9-9 de blocs de laine noire.
	 */
	@SuppressWarnings("deprecation")
	public void buildGrid(){				
		for(int j = 0; j < 10; j++){		
			for(int i = 0; i < 10; i++){
				Bukkit.getWorld(mMName).getBlockAt(new Location(Bukkit.getWorld(mMName), j, 3, i)).setTypeIdAndData(35, DyeColor.BLACK.getData(), true);
			}			
		}		
	}
}
