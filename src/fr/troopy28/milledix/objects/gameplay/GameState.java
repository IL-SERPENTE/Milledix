package fr.troopy28.milledix.objects.gameplay;

/**
 * @author troopy28
 * Définit les différents états de jeu possibles.
 */
public enum GameState {
	/**
	 * Etat de jeu durant du démarrage du serveur au début de la partie.
	 */
	PRE_GAME,
	/**
	 * Etat de jeu durant lequel se déroule la partie. Seul état de jeu 
	 * durant lequel les joueurs peuvent jouer.
	 */
	GAME,
	/**
	 * Etat se déroulant après la partie, durant l'interval de temps avant
	 * l'éjection des joueurs par le serveur.
	 */
	END_GAME;	
}
