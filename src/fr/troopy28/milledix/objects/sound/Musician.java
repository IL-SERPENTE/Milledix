package fr.troopy28.milledix.objects.sound;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import fr.troopy28.milledix.Main;
/**
 * 
 * @author troopy28
 *
 */
public class Musician {

	private SoundType soundType; //Type de son à jouer
	/**
	 * 
	 * @param soundType Définit le type de son qui sera joué.
	 */
	public Musician(SoundType soundType){
		this.soundType = soundType;
	}
	
	/**
	 * Joue le son passé en argument dans le constructeur à tous
	 * les joueurs connectés y compris les spectateurs.
	 */
	public void playSoundForAllPlayers(){
		for(Player pls : Bukkit.getOnlinePlayers()){
			playSoundForPlayer(pls);
		}
	}
	
	/**
	 * Joue le son défini en commentaire uniquement pour le joueur 
	 * spécifié.
	 * @param p Joueur qui entendra le son.
	 */
	public void playSoundForPlayer(final Player p){
		if(this.soundType == SoundType.IMPOSSIBLE){
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
		}
		else if(this.soundType == SoundType.LINE){			
			for(int x = 0; x < 5; x++){
				int ticks = x*3;
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable(){
					public void run() {
						p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 10, 1);
					}				
				}, ticks);
			}
		}
		else if(this.soundType == SoundType.PLACE){
			p.getWorld().playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 10, 1);
		}
		
	}
	
}
