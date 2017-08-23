package fr.troopy28.milledix.objects.gameplay;

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
