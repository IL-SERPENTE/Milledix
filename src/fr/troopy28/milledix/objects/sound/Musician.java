package fr.troopy28.milledix.objects.sound;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import fr.troopy28.milledix.Main;

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
