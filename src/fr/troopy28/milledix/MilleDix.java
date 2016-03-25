package fr.troopy28.milledix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import fr.troopy28.milledix.objects.assistant.JSONReader;
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
	
	//Scoreboard en général
	private ScoreboardManager manager;
	private Scoreboard board;
	private Objective objective;
	
	//Scores du scoreboard. Sert à les afficher
	private Score redScore;
	private Score greenScore;
	private Score timeScore;
	private Score turnTimeScore;
	
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
		
		jsr = new JSONReader();
		this.manager = Bukkit.getScoreboardManager();
		this.board = manager.getNewScoreboard();
		this.objective = board.registerNewObjective("Scores", "dummy");
		this.objective.setDisplayName(ChatColor.BOLD + "" + ChatColor.GOLD + "MilleDix!");
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
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
		manageTime();
		Main.getInstance().setGameState(GameState.GAME);
		greenTurn();
	    super.startGame();
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
		
		p.stopPlaying();//Il arrête de jouer
		turnTask.cancel(); //On éteint son compteur de tour
		if(checkPointLimit(p))//On vérifie s'il dépasse ou a au moins la limite de points. Si oui, on sort pour ne pas relancer de tour
			return;
		for(String key : Main.getInstance().getGamePlayers().keySet()){ //On parcours le HashMap afin de trouver le joueur qui joue
			if(Main.getInstance().get(key).getbPlayer().getName() != p.getbPlayer().getName())
				Main.getInstance().get(key).startPlaying(); //On récupère le joueur qui n'est pas celui qui vient de jouer		
		}

		startTurnTime(); //On redémarre le compteur de tour pour le nouveau joueur
	}
	
	/**
	 * Actualise le scoreboard afin d'afficher les informations correctes en ce
	 * qui concerne les points, le temps de partie, le temps du tour etc.
	 */
	public void updateScoreboard(){
		
		int rs = Main.getInstance().get(Main.getInstance().getRedUUID()).getScore(); //Score du rouge
		int gs = Main.getInstance().get(Main.getInstance().getGreenUUID()).getScore();; //Score du vert
		
		String rName = Main.getInstance().get(Main.getInstance().getRedUUID()).getbPlayer().getName(); //Pseudo du rouge
		String gName = Main.getInstance().get(Main.getInstance().getGreenUUID()).getbPlayer().getName(); //Pseudo du vert
		
		if(rName.length() >= 12){
			rName = rName.substring(0, 10) + ".";
		}
		if(gName.length() >= 12){
			gName = gName.substring(0, 10) + ".";
		}

		/* On parcours le HashMap afin de trouver les joueurs verts et rouges, puis
		 * on récupère leurs scores et leur pseudo par l'intermédiaire du HashMap,
		 * et on les stocke dans rs, gs, rName, et gName.
		 */
	
		//On définit les score puis on les affiche
		
		redScore = objective.getScore(ChatColor.RED + rName + " : ");
		redScore.setScore(rs);
		
		greenScore = objective.getScore(ChatColor.GREEN + gName + " : ");		
		greenScore.setScore(gs);
	
		timeScore = objective.getScore(ChatColor.AQUA + "Temps restant:");
		timeScore.setScore((int)timeRemaining);
		
		turnTimeScore = objective.getScore(ChatColor.AQUA + "Temps tour:");
		turnTimeScore.setScore((int)turnTime);
		
		
		//On envoie le scoreboard à tous les joueurs
		for(Player pls : Bukkit.getOnlinePlayers()){
			pls.setScoreboard(board);
		}
		
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
				updateScoreboard();
				timeRemaining--;
				Bukkit.getWorld("MDMap").setTime(1000);
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
		Terminator terminator = new Terminator(this);
		terminator.endGame(p, causeCannotPlace);	
	}

	
	
	

	
}
