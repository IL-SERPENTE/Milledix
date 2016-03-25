package fr.troopy28.milledix.objects.assistant;

import org.bukkit.craftbukkit.libs.jline.internal.Log;

import fr.troopy28.milledix.Main;
import net.samagames.api.SamaGamesAPI;
/**
 * @author troopy28
 */
public class JSONReader {

	//private JSONObject jsonObject;
	//private JSONParser jsonParser;
	private long timeTurn;
	private long timeGame;
	private long pointLimit;
	
	//Localisation du fichier JSON de config.
	//private static final String CONFIG_FILE_NAME = "game.json";

	/**
	 * <B>Constructeur :</b> Initialise les objets de lecture du JSON, les variables de points / temps et <i>lit la configuration</i> afin d'actualiser les variables de l'objet JSON reader créé.
	 */
	public JSONReader(){
		//jsonObject = new JSONObject();
		//jsonParser = new JSONParser();
		this.timeTurn = 0;
		this.timeGame = 0;
		this.pointLimit = 0;
		readConfig();
	}
	
	/**
	 * Lit la configuration JSON afin d'actualiser les variables de l'objet JSON reader créé.
	 */
	public void readConfig(){
		try {
			
			/*FileReader reader = new FileReader(CONFIG_FILE_NAME);
			jsonObject = (JSONObject) jsonParser.parse(reader);					
			timeGame =  (Long) jsonObject.get("timeGame");
			timeTurn =  (Long) jsonObject.get("timeTurn");
			pointLimit = (Long) jsonObject.get("pointLimit");
			 */		
			timeGame =  (Long) SamaGamesAPI.get().getGameManager().getGameProperties().getConfigs().get("timeGame").getAsLong();
			timeTurn =  (Long) SamaGamesAPI.get().getGameManager().getGameProperties().getConfigs().get("timeTurn").getAsLong();
			pointLimit = (Long) SamaGamesAPI.get().getGameManager().getGameProperties().getConfigs().get("pointLimit").getAsLong();
			
			/*	La configuration est récupérée sous forme de Long.
			*	Toutefois, le scoreboard prend en paramètre un int.
			*	Le test suivant détermine si la conversion du long vers le int aura un problème.
			*	Si oui, on met la valeur maximale acceptable par un int comme valeur.
			*/
			if(timeTurn >= Integer.MAX_VALUE){
				timeTurn = (long) Integer.MAX_VALUE-1;
			}
			if(timeGame >= Integer.MAX_VALUE){
				timeGame = (long) Integer.MAX_VALUE-1;
			}
			if(pointLimit >= Integer.MAX_VALUE){
				pointLimit = (long) Integer.MAX_VALUE-1;
			}
			
			Main.getInstance().getLogger().info("------Configuration recuperee------");
			Main.getInstance().getLogger().info("Temps d'un tour > " + timeTurn);
			Main.getInstance().getLogger().info("Temps d'une partie > " + timeGame);
			Main.getInstance().getLogger().info("Limite de points > " + pointLimit);
			Main.getInstance().getLogger().info("-----------------------------------");
		}
		catch(Exception e){
			Main.getInstance().getLogger().info("Une erreur est survenue lors de la lecture de la configuration.");
			Log.trace(e);
			setBaseConfig();
		}
	}
	
	/**
	 * Définit les valeurs de la configuration sur les valeurs par défaut, soit -> temps de la partie = 600 ; temps du tour = 31
	 */
	private void setBaseConfig(){
		timeTurn = 50;
		timeGame = 600;
		pointLimit = 150;
		Main.getInstance().getLogger().info("Afin de ne pas perturber le jeu, la configuration d'urgence a ete recuperee : ");
		Main.getInstance().getLogger().info("Temps d'un tour > " + timeTurn);
		Main.getInstance().getLogger().info("Temps d'une partie > " + timeGame);
		Main.getInstance().getLogger().info("Limite de points > " + pointLimit);
	}
	
	/**
	 * Renvoie le temps que doit durer un tour en secondes selon la configuration
	 * @return
	 */
	public long getTimeTurn(){
		return this.timeTurn;
	}
	
	/**
	 * Renvoie le temps que doit durer la partie en secondes selon la configuration
	 * @return
	 */
	public long getTimeGame(){
		return this.timeGame;
	}
	
	/**
	 * Renvoie le nombre de points entrainant une fin de partie selon la configuration
	 * @return
	 */
	public long getPointLimit(){
		return this.pointLimit;
	}
	
}
