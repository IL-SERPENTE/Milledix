package fr.troopy28.milledix.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.troopy28.milledix.Main;
import fr.troopy28.milledix.MilleDix;
import fr.troopy28.milledix.objects.gameplay.GameState;
import fr.troopy28.milledix.objects.gameplay.MDPlayer;
import fr.troopy28.milledix.utils.ChatUtils;
/**
 * 
 * @author troopy28
 *
 */
public class PlayersQuitListener implements Listener {

	private static final String CHAT_THE_PLAYER = "Le joueur ";
	private static final String CHAT_HAS_QUIT = " a quitté la partie!";


	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		try{
			e.setQuitMessage(null);			
			if(Main.getInstance().getGameState() == GameState.GAME){ //Si on est en PRE_GAME	
				manageGame(e);
			}
			else if(Main.getInstance().getGameState() == GameState.PRE_GAME){
				managePreGame(e);
			}
			else if(Main.getInstance().getGameState() == GameState.END_GAME){				
				manageEndGame(e);
			}
		}catch(Exception ex){			
			Log.trace(ex);
		}
	}

	/**
	 * Gère un déconnexion survenant durant la partie. Si la personne se déconnectant participait au jeu, 
	 * la victoire est donnée à son adversaire. Sinon, si le joueur se déconnectant est un spectateur, on
	 * n'affiche rien dans le chat.
	 * @param e Prend en paramètre l'événement à gérer.
	 */
	private void manageGame(PlayerQuitEvent e){
		if(Main.getInstance().getGamePlayers().size() != 0){ //Si il y a des joueurs
			
			if(Main.getInstance().get(e.getPlayer().getUniqueId().toString()) == null){ //Si on ne trouve pas le joueur
				e.setQuitMessage(null); //Si on ne trouve personne, il ne faut pas notifier
				return;
			}
			
			MDPlayer mdplayer = Main.getInstance().get(e.getPlayer().getUniqueId().toString()); //On récupère le joueur pour éviter de faire trop d'appels à Main
			
			Bukkit.broadcastMessage(ChatUtils.getGamePrefix() + ChatColor.WHITE +  CHAT_THE_PLAYER + ChatColor.AQUA +  e.getPlayer().getName() + ChatColor.WHITE + CHAT_HAS_QUIT);
			Bukkit.broadcastMessage(ChatUtils.getGamePrefix()  + ChatColor.YELLOW + "" + ChatColor.ITALIC + "Ouuuuuuuuh!");
			mdplayer.decreaseScore(50000); // On s'assure que le joueur qui quitte perde!
			MilleDix.getGameInstance().endGame(mdplayer, false); //On termine la partie. On met sur false 			
		}
	}

	/**
	 * Gère une déconnexion durant la phase de "préparation", d'attente.
	 * @param e Prend en paramètre l'événement à gérer.
	 */
	private void managePreGame(PlayerQuitEvent e){
		if(Main.getInstance().getGamePlayers().size() != 0){ //Si il y a des joueurs
			
			if(Main.getInstance().get(e.getPlayer().getUniqueId().toString()) == null){ //Si on ne trouve pas le joueur
				e.setQuitMessage(null);
				return;
			}
			//Sinon, c'est qu'un joueur jouant au jeu a quitté. Par conséquent, on notifie, on le supprime de la liste
			MDPlayer mdplayer = Main.getInstance().get(e.getPlayer().getUniqueId().toString()); //On récupère le joueur pour éviter de faire trop d'appels à Main
			
			Bukkit.broadcastMessage(ChatUtils.getGamePrefix() + ChatColor.WHITE +  CHAT_THE_PLAYER + ChatColor.AQUA +  e.getPlayer().getName() + ChatColor.WHITE + CHAT_HAS_QUIT + ChatColor.GREEN + "(" + (Bukkit.getServer().getOnlinePlayers().size()-1) + "/2)" );								
			Main.getInstance().getGamePlayers().remove(mdplayer.getbPlayer().getUniqueId().toString());
			tellSpectators();
			PlayersJoinListener.setCountDown(11);
			PlayersJoinListener.getTask().cancel();
		}
	}

	/**
	 * Informe les spectateurs qu'ils peuvent effectuer une déconnexion-reconnexion afin de pouvoir 
	 * participer à la partie en tant que joueur.
	 */
	private void tellSpectators(){
		for(Player pls : Bukkit.getOnlinePlayers()){
			if(pls.getGameMode() == GameMode.SPECTATOR)
				pls.sendMessage(ChatUtils.getGamePrefix() + ChatColor.GOLD + "Avis aux spectateurs : " + ChatColor.WHITE + "reconnectez vous afin de venir jouer!");

		}
	}

	/**
	 * Gère une déconnexion survenant à la fin de la partie, une fois que celle-ci est terminée. Si
	 * la personne se déconnectant est un joueur qui participait au jeu, on informe la déconnexion.
	 * Sinon, on fait en sorte que le chat reste vide.
	 * @param p
	 */
	private void manageEndGame(PlayerQuitEvent e){
		if(Main.getInstance().get(e.getPlayer().getUniqueId().toString()) != null){ //Si on trouve le joueur, on notifie
			Bukkit.broadcastMessage(ChatUtils.getGamePrefix() + ChatColor.WHITE +  CHAT_THE_PLAYER + ChatColor.AQUA +  e.getPlayer().getName() + ChatColor.WHITE + CHAT_HAS_QUIT + ChatColor.GREEN + "(" + (Bukkit.getServer().getOnlinePlayers().size()-1) + "/2)" );													
		}
		e.setQuitMessage(null); //Mais dans tous les cas, on efface le message notifiant la déconnexion
	}
	
}
