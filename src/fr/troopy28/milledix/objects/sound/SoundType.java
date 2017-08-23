package fr.troopy28.milledix.objects.sound;

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
