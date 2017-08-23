package fr.troopy28.milledix;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.troopy28.milledix.listeners.MustCancelListener;
import fr.troopy28.milledix.listeners.PlayerInteractionsListener;
import fr.troopy28.milledix.listeners.PlayersJoinListener;
import fr.troopy28.milledix.listeners.PlayersQuitListener;
import fr.troopy28.milledix.objects.assistant.WorldFactory;
import fr.troopy28.milledix.objects.gameplay.GameState;
import fr.troopy28.milledix.objects.gameplay.MDPlayer;
import net.samagames.api.SamaGamesAPI;

/*
 * This file is part of Milledix.
 *
 * Milledix is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Milledix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Milledix.  If not, see <http://www.gnu.org/licenses/>.
 */
public class Main extends JavaPlugin{
	
	private static Main instance; //Représente l'instance du plugin
	private GameState gameState; //Stocke l'état de jeu
	private Map<String, MDPlayer> gamePlayers = new HashMap<String, MDPlayer>(); //Stocke les joueurs avec leur UUID comme clé
	/* Les deux variables de UUID permettent d'accéder aux joueurs du HashMap en utilisant la clé par l'intermédiaire d'un "get"
	 * plutôt que de parcourir tout le HashMap avec un EntrySet.
	 */
	private String greenUuid; //UUID du joueur vert
	private String redUuid; //UUID du joueur rouge
	
	private MilleDix myGame;
	private Logger log;	
	
	@Override
	public void onLoad(){
		
	}
	
	/**
	 * Se déclenche au démarrage du plugin.
	 * La fonction génère le monde si il n'existe pas, puis génère galement la grille.
	 * Elle active ensuite les écouteurs d'événements, puis définit l'instance du plugin, l'état de jeu, et l'objet de logique du jeu.
	 */
	@Override
	public void onEnable(){
		//Sur la ligne ci-dessous, SonarLint n'est pas content. J'ai préféré m'en remettre aux compétences humaines d'AmauryPi qui n'a pas trouvé d'utilité à setupStatics() que l'analyse de base de Sonar.		
		instance = this; // NOSONAR
		log  = Bukkit.getLogger();
		
		//On gère le monde
		WorldFactory mapMaker = new WorldFactory("MDMap");
		mapMaker.generateWorld();
		mapMaker.buildGrid();
		//On redéfinit le GameState
		gameState = GameState.PRE_GAME;
		
		//On enregistre les événements
		registerEvents();
		
		myGame = new MilleDix("MilleDix", "MilleDix", "Reprise de l'application SmartPhone MillDix", MDPlayer.class);
		
		//Si le jeu est déjà enregistré, on ne l'enregistre pas à nouveau TODO vérifier l'utilité
		if(SamaGamesAPI.get().getGameManager().getGame() == null){
			SamaGamesAPI.get().getGameManager().registerGame(myGame);	
		}
		else if(SamaGamesAPI.get().getGameManager().getGame().getGameName() != myGame.getGameName()){
			SamaGamesAPI.get().getGameManager().registerGame(myGame);	
		}
	}

	
	/**
	 * Est appelée à l'arrêt du plugin. Il éteint toutes les tâches du plugin, efface la liste des joueurs, redéfinit le délai d'attente sur 11 et redéfini l'état de jeu sur "Fin de partie".
	 * Le délai d'attente est le temps que les joueurs doivent attendre une fois tous les joueurs nécessaires à la partie connectés.
	 */
	@Override
	public void onDisable(){
		Bukkit.getScheduler().cancelTasks(this);
		gamePlayers.clear();
		PlayersJoinListener.setCountDown(11);
		gameState = GameState.END_GAME;
	}
	
	/**
	 * @param pl Enregistre les écouteurs d'événements utiles à la partie.
	 */
	public void registerEvents(){
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayersJoinListener(), this);
		pm.registerEvents(new PlayersQuitListener(), this);
		pm.registerEvents(new PlayerInteractionsListener(), this);
		pm.registerEvents(new MustCancelListener(), this);
	}
	
	/**
	 * @return Renvoie le logger du plugin, afin de pouvoir �crire dans le chat avec un logger.
	 */
	public Logger getInstanceLogger(){
		return log;
	}
	
	/**
	 * @return Renvoie l'instance du plugin.
	 */
	public static Main getInstance(){
		return instance;
	}
	
	/**
	 * @return Retourne l'état de jeu actuel de la partie.
	 */
	public GameState getGameState(){
		return gameState;
	}
	
	/**
	 * Renvoie la Map de tous les joueurs.
	 */
	public Map<String, MDPlayer> getGamePlayers(){
		return gamePlayers;
	}
	
	/**
	 * @param uuid Clé permettant de retrouver le joueur.
	 * @return Retourne le joueur ayant comme UUID la clé passée en paramètre.
	 */
	public MDPlayer get(String uuid){
		return this.gamePlayers.get(uuid);
	}
	
	
	/**
	 * Défini l'état de jeu sur celui passé en paramètre.
	 * @param gs Etat de jeu à définir.
	 */
	public void setGameState(GameState gs){
		gameState = gs;
	}
	
	/**
	 * @return Retourne l'identifiant unique (UUID) du joueur vert
	 */
	public String getGreenUUID(){
		return this.greenUuid;
	}
	
	/**
	 * @return Retourne l'identifiant unique (UUID) du joueur rouge
	 */
	public String getRedUUID(){
		return this.redUuid;
	}
	
	/**
	 * Définit la valeur de l'identifiant unique du joueur vert sur la valeur passée en paramètre
	 * @param greenUUID Nouvel identifiant unique
	 */
	public void setGreenUUID(String greenUUID){
		this.greenUuid = greenUUID;
	}
	
	/**
	 * Définit la valeur de l'identifiant unique du joueur rouge sur la valeur passée en paramètre
	 * @param redUUID Nouvel identifiant unique
	 */
	public void setRedUUID(String redUUID){
		this.redUuid = redUUID;
	}
	
}


