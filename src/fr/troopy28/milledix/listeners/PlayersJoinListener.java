package fr.troopy28.milledix.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

import fr.troopy28.milledix.Main;
import fr.troopy28.milledix.objects.gameplay.GameState;
import fr.troopy28.milledix.objects.gameplay.MDPlayer;

/**
 * @author troopy28
 */
public class PlayersJoinListener implements Listener {

	//Compteur de temps
	static int countdown = 11;
	//Tâche gérant 
	private static BukkitTask task; 

	/**
	 * Gère la connexion du joueur et les actions à effectuer lors de celle-ci. 
	 * @param e Evénement de connexion.
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){	
	
		//Téléporte le joueur à la bonne location et le prépare
		e.getPlayer().teleport(new Location(Bukkit.getWorld("MDMap"), 0, 7, 0));
		setupPlayer(e.getPlayer());

		//Si on est durant la phase d'attente
		if(Main.getInstance().getGameState() == GameState.PRE_GAME){
			if(Main.getInstance().getGamePlayers().isEmpty()){ //S'il n'y a pas encore de joueur, celui-ci est le premier. On gère donc le premier joueur.
				manageFirstPlayer(e);
			}
			else if(Main.getInstance().getGamePlayers().size() == 1){ //S'il n'y a déjà un joueur, celui-ci est le second. On gère donc le second joueur.
				manageSecondPlayer(e);
			}
		}
		//Sinon, on est dans la phase de jeu ou d'après jeu, dans quel cas les joueurs sont mis en tant que spectateurs.
		else
			setSpectator(e.getPlayer());
	}

	/**
	 * Crée un nouvel objet MDPlayer, et le met dans le HashMap de liste des joueurs en fonction de son UUID.
	 * Affiche un message, envoie un title au joueur et défini le greenUUID dans main sur le UUID du joueur venant de se connecter.
	 * @param e Evénement à gérer.
	 */
	private void manageFirstPlayer(PlayerJoinEvent e){
		MDPlayer mdPlayer = new MDPlayer(e.getPlayer()); //On met le premier vert et on ne lui donne pas de forme
		mdPlayer.setup(e.getPlayer(), ChatColor.GREEN, null);
		Main.getInstance().getGamePlayers().put(e.getPlayer().getUniqueId().toString(), mdPlayer);		
		e.setJoinMessage(null);
		mdPlayer.sendTitle(ChatColor.AQUA + "Bienvenue au MilleDix!", ChatColor.GREEN + "Vous êtes vert!", 3);							
		Main.getInstance().setGreenUUID(e.getPlayer().getUniqueId().toString());
	}
	
	/**
	 * Crée un nouvel objet MDPlayer, et le met dans le HashMap de liste des joueurs en fonction de son UUID.
	 * Affiche un message, envoie un title au joueur et défini le redUUID dans main sur le UUID du joueur venant de se connecter.
	 * @param e Evénement à gérer.
	 */
	private void manageSecondPlayer(PlayerJoinEvent e){
		MDPlayer mdPlayer = new MDPlayer(e.getPlayer()); //On met le second rouge et on ne lui donne pas de forme
		mdPlayer.setup(e.getPlayer(), ChatColor.RED, null);
		Main.getInstance().getGamePlayers().put(e.getPlayer().getUniqueId().toString(), mdPlayer);
		e.setJoinMessage(null);
		mdPlayer.sendTitle(ChatColor.AQUA + "Bienvenue au MilleDix!", ChatColor.RED + "Vous êtes rouge!", 3);
		Main.getInstance().setRedUUID(e.getPlayer().getUniqueId().toString());
	}
	
	/**
	 * Met le joueur passé en paramètre en conditions de spectateur : mode spectateur, il 
	 * est caché pour tous les autres joueurs, est autorisé à voler, et le vol est activé.
	 * @param p : Joueur à mettre en spectateur
	 */
	public void setSpectator(Player p){	
		for(Player pls : Bukkit.getServer().getOnlinePlayers()){
			pls.hidePlayer(p);
		}
		p.setGameMode(GameMode.SPECTATOR);
		p.setAllowFlight(true);
		p.setFlying(true);
	}


	/**
	 * Prépare le joueur passé en paramètre à jouer : il est mit en mode aventure, on l'autorise à 
	 * voler et on lance le vol, on lui met une vitesse de vol à <i>0.1</i>, on lui met sa vie et sa 
	 * nourriture à 20.
	 * @param p : Joueur à préparer.
	 */
	public void setupPlayer(Player p){
		p.getInventory().clear();
		p.setGameMode(GameMode.ADVENTURE);
		p.setAllowFlight(true);
		p.setFlying(true);
		p.setFlySpeed(0.1f);
		p.setHealth(20);
		p.setFoodLevel(20);
	}

	/**
	 * @return Renvoie la valeur actuelle du compte à rebours, soit le nombre de secondes restantes.
	 */
	public static int getCountDown(){
		return countdown;
	}

	/**
	 * 
	 * @param value Valeur à donner au compte à rebours.
	 */
	public static void setCountDown(int value){
		PlayersJoinListener.countdown = value;
	}

	/**
	 * @return Renvoie l'entier représentant la tâche du compte à rebours afin de permettre de l'éteindre
	 * de l'extérieur.
	 */
	public static BukkitTask getTask(){
		return task;
	}

}
