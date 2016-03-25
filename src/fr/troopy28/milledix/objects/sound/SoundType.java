package fr.troopy28.milledix.objects.sound;
/**
 * 
 * @author troopy28
 * Définit les types de son jouables par l'objet
 * Musician.
 */
public enum SoundType {
	/**
	 * Son devant être joué lorsque le joueur essaie de faire
	 * un placement impossible, qui n'est pas accepté par la
	 * logique du jeu.
	 */
	IMPOSSIBLE,
	/**
	 * Son devant être joué lorsque le placement d'un bloc 
	 * abouti au remplissage complet d'une ou plusieurs 
	 * ligne(s) de la grille.
	 */
	LINE,
	/**
	 * Son devant être joué lorsque le placement d'un bloc
	 * est possible mais qu'il n'abouti à rien de particulier.
	 */
	PLACE;
	
}
