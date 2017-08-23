package fr.troopy28.milledix.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

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
public class MustCancelListener implements Listener {

	/**
	 * Lorsque qu'une entité subit des dégâts, on vérifie s'il s'agit d'un joueur. Si non, on 
	 * laisse l'entité subir les dégâts afin que les joueurs puissent se débarasser d'une
	 * entité gênante qui aurait réussi à spawner malgré le filtre au niveau du spawn.
	 * S'il s'agit d'un joueur, on annule l'événement, afin que le joueur ne subisse pas de dégâts,
	 * et on met les ticks de feu à zéro afin que le joueur ne brûle pas pendant X temps.
	 * @param e EntityDamageEvent : Evénement survenant lorsqu'une entité subit des dégâts.
	 */
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player) {
			((Player)e.getEntity()).setFireTicks(0);
			e.setCancelled(true);
		}
	}

	/**
	 * On annule simpement l'événement de nourriture qui change afin que les joueurs restent à leur 
	 * maxiumum de nourriture, définit au moment de la connexion.
	 * @param e FoodLevelChangeEvent : Evénement de changement de niveau de nourriture.
	 */
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e){
		e.setCancelled(true);
	}

	/**
	 * On annule tout drop d'item afin que les joueurs ne se retrouvent pas bloqués. On actualise 
	 * ensuite l'inventaire afin que le joueur n'ait pas de problème d'affichage de l'item droppé.
	 * @param e PlayerDropItemEvent : Evénement de jet d'un item sur le sol.
	 */
	@EventHandler
	public void onItemDropped(PlayerDropItemEvent e){
		e.setCancelled(true);
		e.getPlayer().updateInventory();
	}

	/**
	 * On annule le spawn de toute entité n'étant ni un joueur, ni un armor stand, ni un villageois.
	 * L'armor stand et le villageois sont autorisés pour une éventuelle map où l'armor stand serait
	 * dans la décoration et où le villageois pourrait être un éventuel spectateur de la partie (dans
	 * une arène, par exemple).
	 * @param e CreatureSpawnEvent : Evénement d'apparition d'une entité.
	 */
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e){		
		if(e.getEntityType() != EntityType.PLAYER && e.getEntityType() != EntityType.ARMOR_STAND && e.getEntityType() != EntityType.VILLAGER){ //S'il s'agit d'un monstre ou un animal
			e.setCancelled(true);
		}		
	}	


}	