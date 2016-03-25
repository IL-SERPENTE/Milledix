package fr.troopy28.milledix.utils;

import org.bukkit.ChatColor;
/**
 * @author troopy28
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