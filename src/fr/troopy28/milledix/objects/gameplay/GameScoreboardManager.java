package fr.troopy28.milledix.objects.gameplay;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import fr.troopy28.milledix.Main;

public class GameScoreboardManager {

	
	//Scoreboard en général
	private ScoreboardManager manager;
	private Scoreboard board;
	private Objective objective;
	
	//Scores du scoreboard. Sert à les afficher
	private Score redScore;
	private Score greenScore;
	private Score timeScore;
	private Score turnTimeScore;

	
	public GameScoreboardManager(){
		this.manager = Bukkit.getScoreboardManager();
		this.board = manager.getNewScoreboard();
		this.objective = board.registerNewObjective("Scores", "dummy");
		this.objective.setDisplayName(ChatColor.BOLD + "" + ChatColor.GOLD + "MilleDix!");
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

	}
	
	/**
	 * Actualise le scoreboard afin d'afficher les informations correctes en ce
	 * qui concerne les points, le temps de partie, le temps du tour etc.
	 * 
	 * @param timeRemaining Temps restant avant la fin de la PARTIE.
	 * @param turnTime Temps restant avant la fin du TOUR.
	 */
	public void updateScoreboard(int timeRemaining, int turnTime){
		
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
		timeScore.setScore(timeRemaining);
		
		turnTimeScore = objective.getScore(ChatColor.AQUA + "Temps tour:");
		turnTimeScore.setScore(turnTime);
		
		
		//On envoie le scoreboard à tous les joueurs
		for(Player pls : Bukkit.getOnlinePlayers()){
			pls.setScoreboard(board);
		}
	}

	
}
