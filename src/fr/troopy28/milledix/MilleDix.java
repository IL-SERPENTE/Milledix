package fr.troopy28.milledix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.scheduler.BukkitTask;

import fr.troopy28.milledix.objects.assistant.JSONReader;
import fr.troopy28.milledix.objects.gameplay.GameScoreboardManager;
import fr.troopy28.milledix.objects.gameplay.GameState;
import fr.troopy28.milledix.objects.gameplay.GridAnalyser;
import fr.troopy28.milledix.objects.gameplay.MDPlayer;
import fr.troopy28.milledix.objects.gameplay.Terminator;
import fr.troopy28.milledix.utils.ChatUtils;
import net.samagames.api.games.Game;

/**
 * @author troopy28
 * Classe chargée de gérer le déroulement du jeu. Elle implémente
 * la plupart des fonctions logiques permettant l'avancement du jeu
 * au niveau du plugin.
 */
public class MilleDix extends Game<MDPlayer>{

	private static MilleDix instance;
	
	//Scoreboard
	private GameScoreboardManager scoreboardManager;
	
	//Tâches
	private BukkitTask task;
	private BukkitTask turnTask;

	//Config
	private JSONReader jsr;
	private long turnTime;
	private long timeRemaining;
	private long pointLimit;
	
	
	/**
	 * <h2>Constructeur</h2> Crée un objet de lecture de config JSON. Définit les objets nécessaires à la 
	 * gestion du scoreboard. Récupère ensuite la configuration.
	 */
	public MilleDix(String gameCodeName, String gameName, String gameDescription, Class<MDPlayer> gamePlayerClass) {
		
		super(gameCodeName, gameName, gameDescription, gamePlayerClass);
		scoreboardManager = new GameScoreboardManager();
		jsr = new JSONReader();
		instance = this;
		getConfig();
	}

	/**
	 * @return Retourne l'instance du jeu.
	 */
	public static MilleDix getGameInstance(){
		return instance;
	}
	
	/**
	 * Démarre la partie : lance le compte à rebours de la partie (temps total), définit l'instance sur GameState,
	 * et lance le tour du joueur vert, qui est le joueur censé commencer.
	 */
	@Override
	public void startGame()
	{
	    super.startGame();
		manageTime();
		Main.getInstance().setGameState(GameState.GAME);
		greenTurn();
	}	
	
	/**
	 * Récupère la configuration : stocke la limite de points dans pointLimit,
	 * stocke le temps de tour dans turnTime, et stocke le temps de la partie
	 * dans timeRemaining.
	 */
	private void getConfig(){
		try{
			this.pointLimit = jsr.getPointLimit();
			this.turnTime = jsr.getTimeTurn();
			this.timeRemaining = jsr.getTimeGame();
		}
		catch(Exception e){			
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Erreur dans getConfig, MilleDix.");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "JSR -> " + jsr);
			Log.trace(e);
		}
	}
	
	/**
	 * Démarre le compte à rebours du tour : redéfinit la variable du temps restant
	 * sur le temps donné dans la configuration, et lance un scheduler qui décrémente
	 * ce temps chaque seconde. Si le temps vaut zéro, on enlève 5 points au joueur et
	 * on arrête son tour afin de lancer le suivant.
	 */
	private void startTurnTime(){
		this.turnTime = jsr.getTimeTurn();
		turnTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable(){
			public void run() {
				turnTime--;
				if(turnTime <= 0){
					getPlayingPlayer().decreaseScore(5);
					switchTurns(getPlayingPlayer());
				}
			}
		}, 20, 20);
	}
	
	
	/**
	 * Démarre le tour du joueur vert.
	 * Par précaution, met fin au tour du joueur rouge même s'il n'est pas censé jouer,
	 * puis démarre le tour du joueur vert, et lance le compteur de tour.
	 */
	public void greenTurn(){
		Main.getInstance().getGamePlayers().get(Main.getInstance().getRedUUID()).stopPlaying();
		Main.getInstance().getGamePlayers().get(Main.getInstance().getGreenUUID()).startPlaying();		
		startTurnTime();
	}
	
	/**
	 * Arrête le tour du joueur jouant actuellement et démarre le tour du nouveau joueur. La fonction 
	 * commence par supprimer tout bloc de fer éventuellement bugué de la grille, puis arrête le tour 
	 * du joueur actuel, et éteint le compteur du tour. Vérifie ensuite les points du joueur afin de
	 * vérifier s'il a gagné.
	 * @param p Prend en paramètre le joueur dont le tour se termine et qui sera donc sujet à tous ces
	 * tests et actions. 
	 */
	public void switchTurns(MDPlayer p){
		
		if(Main.getInstance().getGameState() != GameState.GAME)
			return;

		//On supprime des éventuels blocs bugués
		GridAnalyser ga = new GridAnalyser(p);	
		ga.killBadIron();
		
		p.stopPlaying(); //Il arrête de jouer
		turnTask.cancel(); //On éteint son compteur de tour
		if(checkPointLimit(p)) //On vérifie s'il dépasse ou a au moins la limite de points. Si oui, on sort pour ne pas relancer de tour
			return;
		for(String key : Main.getInstance().getGamePlayers().keySet()){ //On parcours le HashMap afin de trouver le joueur qui joue
			if(Main.getInstance().get(key).getbPlayer().getName() != p.getbPlayer().getName())
				Main.getInstance().get(key).startPlaying(); //On récupère le joueur qui n'est pas celui qui vient de jouer		
		}

		startTurnTime(); //On redémarre le compteur de tour pour le nouveau joueur
	}
	

	/**
	 * Fonction chargée du temps : compte à rebours du temps de partie et non pas du temps de tour.
	 */
	private void manageTime(){
		task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable(){

			public void run() {
				if(timeRemaining <= 0){ //Une fois le temps écoulé, on arrête le compte à rebours, puis la partie en récupérant le perdant.
					task.cancel();
					endGame(getLoser(Main.getInstance().getGamePlayers().get(Main.getInstance().getGreenUUID()), Main.getInstance().getGamePlayers().get(Main.getInstance().getRedUUID())), false);
				}
				//A chaque tour, on actualise le compteur, on décrémente le temps, et on remet le jour.
				scoreboardManager.updateScoreboard((int) timeRemaining, (int) turnTime);
				timeRemaining--;
				Bukkit.getWorld("MDMap").setTime(1000);
				Bukkit.getWorld("MDMap").setStorm(false);
			}
			
		}, 20, 20);
	}

	/**
	 * @param rP Joueur rouge
	 * @param gP Joueur vert
	 * @return Retourne le gagnat, c'est-à-dire le joueur ayant le plus de points des deux. Dans le cas d'une égalité, renvoie null.
	 */
	public MDPlayer getWinner(MDPlayer rP, MDPlayer gP){
		if(rP.getScore() > gP.getScore()){ //Si le rouge a plus de points que le vert, on renvoie le rouge
			return rP;
		}
		else if(rP.getScore() < gP.getScore()){ //Si le rouge a moins de points que le vert, on renvoie le vert
			return gP;
		}
		else{
			return null;
		}
	}
	
	/**
	 * @param rP Joueur rouge
	 * @param gP Joueur vert
	 * @return Retourne le perdant, c'est-à-dire le joueur ayant le moins de points des deux. Dans le cas d'une égalité, renvoie null.
	 */
	private MDPlayer getLoser(MDPlayer rP,MDPlayer gP){
		if(rP.getScore() < gP.getScore()){ //Si le rouge a moins de points que le vert, on renvoie le rouge
			return rP;
		}
		if(rP.getScore() > gP.getScore()){ //Si le rouge a plus de points que le vert, on renvoie le vert
			return gP;
		}
		else{
			return null;
		}
	}
	
	/**
	 * @return Renvoie le joueur actuellement en train de jouer.
	 */
	private MDPlayer getPlayingPlayer(){
		MDPlayer ret = null;
		/* 
		 * On parcours le HashMap afin de trouver le joueur qui joue
		 * Le parcours est court : uniquement deux valeurs
		 */
		for(String key : Main.getInstance().getGamePlayers().keySet()){ 
			if(Main.getInstance().get(key).isPlaying())
				ret = Main.getInstance().get(key);
		}
 
		return ret;
	}
	
	/**
	 * Vérifie si le joueur a au moins la limite de points. Si oui, la fonction termine la partie. 
	 * @param p Joueur dont le nombre de points va être vérifié.
	 */
	private boolean checkPointLimit(MDPlayer p){
		if(p.getScore() >= this.pointLimit){
			Main.getInstance().setGameState(GameState.END_GAME);
			Bukkit.broadcastMessage(ChatUtils.getGamePrefix() + "Le joueur " + p.getbPlayer().getName() + " a atteint la limite de score !");
			endGame(p, false);
			return true;
		}
		return false;
	}
	
	/**
	 * Termine la partie. Définit l'état de jeu sur END_GAME, enlève 100 points au dernier joueur s'il n'a pas pu 
	 * placer sa forme (causeCannotPlace), affiche le nouveau scoreboard, donne l'inventaire d'attente à tous les
	 * joueurs, arrête toutes les tâches puis affiche le message adéquat de fin de partie en fonction des paramètres
	 * passés. <br>
	 * Nettoie ensuite le serveur en appelant la fonction cleanServer().
	 * @param p Joueur <u><i><b>PERDANT</b></i></u>. Il s'agit du joueur qui perdra 100 points si causeCannotPlace
	 * vaut true. 
	 * @param causeCannotPlace Booléen définissant si la fonction est appelée car il a été impossible pour le joueur
	 * perdant de placer sa forme. Mettre <b>true</b> si c'est le cas, sinon mettre <b>false</b>. Dans le cas où le
	 * booléen vaut <b>true</b>, le joueur passé en paramètre perdra 100 points.
	 */
	public void endGame(MDPlayer p, boolean causeCannotPlace){ //Recoit en paramètre le PERDANT SI causeCannotPlace = true
		this.timeRemaining = 0;
		this.turnTime = 0;
		Terminator terminator = new Terminator(this);
		terminator.endGame(p, causeCannotPlace);	
		
	}

	/**
	 * @return Renvoie le scoreboardManager de l'instance du jeu. Le scoreboardManager est utilisé pour gérer le 
	 * scoreboard : <br>
	 * l'actualiser, changer les informations relatives au score, au temps...
	 */
	public GameScoreboardManager getGameScoreboardManager(){
		return this.scoreboardManager;
	}
	
	/**
	 * Définit le scoreboardManager sur le nouveau GameScoreboardManager passé en paramètre.
	 * @param newGameScoreboardManager Nouveau GameScoreboardManager qui remplacera l'actuel.
	 */
	public void setGameScoreboardManager(GameScoreboardManager newGameScoreboardManager){
		this.scoreboardManager = newGameScoreboardManager;
	}

	
}
