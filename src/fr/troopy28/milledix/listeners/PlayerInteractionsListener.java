package fr.troopy28.milledix.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.troopy28.milledix.Main;
import fr.troopy28.milledix.MilleDix;
import fr.troopy28.milledix.objects.gameplay.GameState;
import fr.troopy28.milledix.objects.gameplay.MDPlayer;
import fr.troopy28.milledix.objects.sound.Musician;
import fr.troopy28.milledix.objects.sound.SoundType;

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
public class PlayerInteractionsListener implements Listener {

	//Constantes définissant les noms des boutons
	private static final String HIGHLIHT_FORM_ITEM = ChatColor.GREEN + "Faire clignoter la forme";
	private static final String PLACE_FORM_ITEM = ChatColor.BLUE + "Placer la forme";
	
	/**
	 * Gère d'interaction des joueurs avec leurs items de jeu. Cette fonction est donc chargée de 
	 * faire la liaison entre la logique du jeu et le joueur en lui même.
	 * @param e Evénement d'interaction.
	 */
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
	
		MDPlayer p = null;
		boolean found = false;
		e.setCancelled(true);	//Quoi qu'il advienne, on annule l'événement

		if(Main.getInstance().getGameState() != GameState.GAME)
			return;
	
		
		//On vérifie si on trouve le joueur, si oui, on continue et on définit le joueur p sur le bon item du HashMap
		if(Main.getInstance().get(e.getPlayer().getUniqueId().toString()) != null) {
			found = true;
			p = Main.getInstance().get(e.getPlayer().getUniqueId().toString());
		}
		
		//On vérifie si l'action est bien un clic droit et que le joueur a bien été trouvé ci-dessus
		if(!found || e.getAction().equals(Action.LEFT_CLICK_AIR) ||  e.getAction().equals(Action.LEFT_CLICK_BLOCK))//Si on n'a pas trouvé le joueur, il s'agit d'un spectateur ou autre joueur ne devant pas interagir dans la partie. On annule donc l'événement.		
			return;		

		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && p.isPlaying()){ //Si c'est un clic d'interaction et que c'est son tour, on gère l'interaction
			manageAction(p);			
		}				
	}	
	
	/**
	 * Gère les actions à effectuer côté plugin (clignotement, placement etc.)
	 * @param p Joueur qui sera affecté par les actions.
	 */
	@SuppressWarnings("deprecation") //TODO CHANGE ACQUISITION ITEM METHOD
	public void manageAction(MDPlayer p){
		if(p.getbPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(HIGHLIHT_FORM_ITEM) && p.canHighlight()){ //Si le joueur veut faire clignoter, on fait clignoter
			p.highlightForm(); //On fait clignoter
		}	
		else if(p.getbPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(PLACE_FORM_ITEM) && p.placeForm()){ //Si il veut placer la forme, on place la forme			
			MilleDix.getGameInstance().switchTurns(p);			
		}
		/* Sinon on lui dit clairement qu'il ne le peut pas.
		 * Noter que le sinon ne s'applique pas dans la cas supposé où le joueur dont ce
		 * n'est pas le tour effectue un clic droit, car on vérifie si le MDPlayer p est 
		 * en train de joueur à la ligne 42.
		 */
		else{ 
			p.sendTitle(ChatColor.RED + "Tututut !", ChatColor.AQUA + "Vous ne pouvez pas poser cela ici !", 3);
			Musician musician = new Musician(SoundType.IMPOSSIBLE);
			musician.playSoundForPlayer(p.getbPlayer());;
		}
	}
}