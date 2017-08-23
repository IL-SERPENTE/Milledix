package fr.troopy28.milledix.utils;

import org.bukkit.ChatColor;

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
public class ChatUtils {

	//Préfixe de jeu devant être placé devant la plupart des messages.
	private static String gamePrefix = ChatColor.GOLD + "[" + ChatColor.AQUA + "MilleDix!" + ChatColor.GOLD + "] ";

	private ChatUtils(){
		
	}

	/**
	 * @return Renvoie le préfixe de jeu devant être placé devant la plupart des messages. <br>
	 * Le préfixe est le suivant : <b>[MilleDix!]</b> (hors couleur).
	 */
	public static String getGamePrefix(){
		return gamePrefix;
	}

}