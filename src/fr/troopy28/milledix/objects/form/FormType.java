package fr.troopy28.milledix.objects.form;

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
public enum FormType {
	
	TOP_LEFT_CORNER, 
	TOP_RIGHT_CORNER, 
	BOT_LEFT_CORNER, 
	BOT_RIGHT_CORNER, 
		
	VERTICAL_LINE_2,
	VERTICAL_LINE_3,
	VERTICAL_LINE_4, 
		
	HORIZONTAL_LINE_2,
	HORIZONTAL_LINE_3,
	HORIZONTAL_LINE_4,
		
	CUBE_1,
	CUBE_2,
	CUBE_3,
	
	RANDOM;
	
	
	/**
	 * Constructeur privé. Sert uniquement à respecter la POO et faire plaisir
	 * à SonarLint par la même occasion.
	 */
	private FormType(){
		
	}
	
	/**
	 * @param y Prend en paramètre un entier compris de préférence entre 0 et 12 inclus.
	 * Tout nombre strictement supérieur à 11 retournera un type de forme RANDOM.
	 * @return Renvoie le type de forme attribué au nombre passé en paramètre.
	 * <br> 0 ->	VERTICAL_LINE_4
	 * <br> 1 ->	BOT_LEFT_CORNER
	 * <br> 2 ->	BOT_RIGHT_CORNER
	 * <br> 3 ->	CUBE_1
	 * <br> 4 ->	CUBE_2
	 * <br> 5 ->	CUBE_3
	 * <br> 6 ->	TOP_LEFT_CORNER
	 * <br> 7 -> 	HORIZONTAL_LINE_2
	 * <br> 8 -> 	HORIZONTAL_LINE_3
	 * <br> 9 ->	HORIZONTAL_LINE_4
	 * <br> 10 -> 	VERTICAL_LINE_2
	 * <br> 11 -> 	VERTICAL_LINE_3
	 * <br> 12 -> 	TOP_RIGHT_CORNER
	 * <br> Nombre supérieur à 12 -> RANDOM
	 */
	public FormType getFormFromValue(int y){ // NOSONAR => On dépasse la complexité maximale étant donné que l'on teste plus de 12 formes.
		switch(y){
			case 0:
				return FormType.VERTICAL_LINE_4;
			case 1:
				return FormType.BOT_LEFT_CORNER;
			case 2:
				return FormType.BOT_RIGHT_CORNER;
			case 3:
				return FormType.CUBE_1;
			case 4:
				return FormType.CUBE_2;
			case 5:
				return FormType.CUBE_3;
			case 6:
				return FormType.TOP_LEFT_CORNER;
			case 7:
				return FormType.HORIZONTAL_LINE_2;
			case 8:
				return FormType.HORIZONTAL_LINE_3;
			case 9:
				return FormType.HORIZONTAL_LINE_4;
			case 10:
				return FormType.VERTICAL_LINE_2;
			case 11:
				return FormType.VERTICAL_LINE_3;
			case 12:
				return FormType.TOP_RIGHT_CORNER;
			default:
				return FormType.RANDOM; //Si un problème survient ou qu'on a un nombre supérieur à 12, on rappellera la fonction qui refera un tour de switch
		}
	}
	
}
