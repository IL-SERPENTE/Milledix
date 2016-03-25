package fr.troopy28.milledix.objects.gameplay;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.Dye;
import org.bukkit.scoreboard.Scoreboard;

import fr.troopy28.milledix.Main;
import fr.troopy28.milledix.MilleDix;
import fr.troopy28.milledix.objects.form.FormType;
import fr.troopy28.milledix.objects.form.MDForm;
import fr.troopy28.milledix.utils.ChatUtils;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle.EnumTitleAction;
import net.samagames.api.games.GamePlayer;
/**
 * 
 * @author troopy28
 *
 */
public class MDPlayer extends GamePlayer{

	private Player bPlayer;
	private MDForm form;
	private int score;
	private ChatColor color;
	private boolean isPlaying;
	private boolean canHighlight;
	
	
	/**
	 * <h2>Constructeur</h2>.
	 * Construit l'objet MDPlayer à partir des paramètres spécifiés. 
	 * @param p Joueur Bukkit que gèrera le MDPlayer
	 * @param isGreen Permet de stocker la couleur 
	 * @param form Forme de type MDForm que le joueur aura à placer.
	 */
	public MDPlayer(Player p){	
		super(p);
	}
	
	public void setup(Player p, ChatColor color, MDForm form){
		this.bPlayer = p;
		this.color = color;
		this.score = 0;
		this.form = form;
		this.isPlaying = false;
		this.canHighlight = true;
		preparePlayer();		
	}
	
	/*Fonctions internes /  privées*/	
	/**
	 * Donne au joueur les boutons d'interaction.
	 */
	private void giveHighlightButton(){
		
		Dye d = new Dye();	
        d.setColor(DyeColor.BLUE);
        
        ItemStack startGLButton = d.toItemStack();	      
        startGLButton.setAmount(1);
        
        ItemMeta startGLButtonMeta = startGLButton.getItemMeta();
        startGLButtonMeta.setDisplayName(ChatColor.GREEN + "Faire clignoter la forme");
        startGLButton.setItemMeta(startGLButtonMeta);
        
		this.bPlayer.getInventory().setItem(8, startGLButton);
		
		Dye placeDye = new Dye();	
		placeDye.setColor(DyeColor.GREEN);
        
		ItemStack placeDyeButton = placeDye.toItemStack();     
		placeDyeButton.setAmount(1);	  		
		
		ItemMeta placeDyeButtonMeta = placeDyeButton.getItemMeta();
		placeDyeButtonMeta.setDisplayName(ChatColor.BLUE + "Placer la forme");
		placeDyeButton.setItemMeta(placeDyeButtonMeta);
        
		this.bPlayer.getInventory().setItem(0, placeDyeButton);
		this.bPlayer.updateInventory();
	}
	
	/**
	 * Donne au joueur son armure de couleur.
	 */
	private void preparePlayer(){
				
		//Armure de la couleur définie
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		
		if(this.color == ChatColor.GREEN){ //Si le joueur est vert
	        LeatherArmorMeta metaHelmet = (LeatherArmorMeta) helmet.getItemMeta();
	        metaHelmet.setColor(Color.GREEN);
	        metaHelmet.setDisplayName(ChatColor.GREEN + "Vous êtes vert");
	        helmet.setItemMeta(metaHelmet);
			
	        LeatherArmorMeta metaBoots = (LeatherArmorMeta) boots.getItemMeta();
	        metaBoots.setColor(Color.GREEN);
	        metaBoots.setDisplayName(ChatColor.GREEN + "Vert");
	        boots.setItemMeta(metaBoots);
	        
	        LeatherArmorMeta metaChestplate = (LeatherArmorMeta) chestplate.getItemMeta();
	        metaChestplate.setColor(Color.GREEN);
	        metaChestplate.setDisplayName(ChatColor.GREEN + "Toujours vert");
	        chestplate.setItemMeta(metaChestplate);
	        
	        LeatherArmorMeta metaLeggings = (LeatherArmorMeta) leggings.getItemMeta();
	        metaLeggings.setColor(Color.GREEN);
	        metaLeggings.setDisplayName(ChatColor.GREEN + "Et encore vert !");
	        leggings.setItemMeta(metaLeggings);
	        
		}
		/* Actuellement, il n'y a que deux couleurs. Un simple else peut donc permettre
		 * de faire une vérification correcte de sa "rougitude".
		 * DONC : Sinon si il est rouge
		 */
		else{ 
			helmet = new ItemStack(Material.LEATHER_HELMET, 1);
	        LeatherArmorMeta metaHelmet = (LeatherArmorMeta) helmet.getItemMeta();
	        metaHelmet.setColor(Color.RED);
	        metaHelmet.setDisplayName(ChatColor.RED + "Vous êtes rouge");
	        helmet.setItemMeta(metaHelmet);
			
			boots = new ItemStack(Material.LEATHER_BOOTS, 1);
	        LeatherArmorMeta metaBoots = (LeatherArmorMeta) boots.getItemMeta();
	        metaBoots.setColor(Color.RED);
	        metaBoots.setDisplayName(ChatColor.RED + "Rouge");
	        boots.setItemMeta(metaBoots);
	        
			chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
	        LeatherArmorMeta metaChestplate = (LeatherArmorMeta) chestplate.getItemMeta();
	        metaChestplate.setColor(Color.RED);
	        metaChestplate.setDisplayName(ChatColor.RED + "Toujours rouge");
	        chestplate.setItemMeta(metaChestplate);
	        
			
	        LeatherArmorMeta metaLeggings = (LeatherArmorMeta) leggings.getItemMeta();
	        metaLeggings.setColor(Color.RED);
	        metaLeggings.setDisplayName(ChatColor.RED + "Et encore rouge !");
	        leggings.setItemMeta(metaLeggings);
	        
		}
		
        this.bPlayer.getInventory().setHelmet(helmet);
        this.bPlayer.getInventory().setChestplate(chestplate);
        this.bPlayer.getInventory().setLeggings(leggings);
        this.bPlayer.getInventory().setBoots(boots);
		
	}
		
	
	/*Fonctions utiles*/
	
	/**
	 * Supprime tous les items et toutes les pièces d'armure du joueur.
	 */
	public void setupWaitingInventory(){
		this.bPlayer.getInventory().clear();
		this.bPlayer.getInventory().setChestplate(null);
		this.bPlayer.getInventory().setLeggings(null);
		this.bPlayer.getInventory().setHelmet(null);
		this.bPlayer.getInventory().setBoots(null);
	}
	
	/**
	 * Envoie le scoreboard au joueur.
	 * @param board Scoreboard à envoyer.
	 */
	public void sendScoreboard(Scoreboard board){
		this.bPlayer.setScoreboard(board);
	}
	
	/**
	 * Donne une fore aléatoire au joueur.
	 */
	public void giveRandomForm(){
		this.form = new MDForm(FormType.RANDOM, this.color);
	}
	
	/**
	 * Démarre le tour du joueur. Efface son inventaire, lui donne une forme
	 * aléatoire, les boutons d'interaction et le définit comme étant en train
	 * de joueur. On lui envoie un title le notifiant que c'est désormais son
	 * tour, et on actualise son inventaire. <br>
	 * On vérifie ensuite que le joueur peut bien placer sa forme, et si ce n'est pas le cas, 
	 * on termine la partie en lui décrémentant 100 points.
	 */
	public void startPlaying(){
		this.bPlayer.getInventory().clear();
		giveRandomForm();
		giveHighlightButton();
		this.isPlaying = true;
		sendTitle(ChatColor.AQUA + "A vous de jouer !", "Placer votre forme sur la grille !", 3);
		this.bPlayer.updateInventory();
		
		GridAnalyser ga = new GridAnalyser(this);
		if(!(ga.checkCanPlace())){ //Si il est impossible pour un joueur de poser sa forme, le joueur perd et la partie se termine
			Bukkit.broadcastMessage(ChatUtils.getGamePrefix() + ChatColor.WHITE + "Le joueur " + ChatColor.AQUA + this.bPlayer.getName() + ChatColor.WHITE + " n'a pas pu poser sa forme ! La partie est terminée !");
			MilleDix.getGameInstance().endGame(this, true); //True -> il n'a pas pu poser sa forme

		}		
	}
	
	/**
	 * Arrête le tour du joueur : efface l'inventaire, efface les tableaux de la forme, la 
	 * définit sur null, et actualise l'inventaire.
	 */
	public void stopPlaying(){
		this.isPlaying = false;
		this.bPlayer.getInventory().clear();
		if(this.form != null)
			this.form.clearAllArrayLists();
		this.form = null;
		this.bPlayer.updateInventory();
	}
	
	/**
	 * Vérifie si la forme peut être placée. Si oui, elle est placée.
	 * @return Renvoie <b>true</b> si la forme peut a pu être placée, 
	 * et <b>false</b> si elle n'a pas pu l'être.
	 */
	public boolean placeForm(){
		if(this.canHighlight && form.checkForm(this.bPlayer)){		
				form.placeForm(this.bPlayer);
				GridAnalyser ga = new GridAnalyser(this);
				ga.analyseGrid();
				return true;			
		}
		return false;
	}
	
	/**
	 * Fait clignoter la forme. Vérifie si la forme est plaçable et que le cooldown
	 * de placement est bon. Si c'est le cas, fait clignoter la forme et lance un 
	 * cooldown de non-placement de 25 ticks durant lesquels il sera impossible de
	 * faire clignoter ou placer la forme.
	 * Sinon, on reçoit un message nous indiquant la raison pour laquelle on ne peut
	 * pas placer la forme.
	 */
	public void highlightForm(){
		boolean canPlace = form.checkHighlight(this.bPlayer.getLocation()); //On utilise un booléen afin de ne pas avoir à refaire la boucle de la fonction "checkForm" plusieurs fois
		if(this.canHighlight && canPlace){
			form.highlightForm(this.bPlayer);
			this.canHighlight = false;
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable(){
				public void run() {					
					canHighlight = true;
				}
			}, 25);
		}
		else if(this.canHighlight){ //Si il peut tout de même la placer, c'est que le temps n'est pas terminé
			sendTitle(ChatColor.RED + "Restez sur la grille !", "Vous ne pouvez afficher que sur la grille !", 2);
		}
		else if(canPlace){
			sendTitle(ChatColor.AQUA + "Mais pas si vite !", "Attendez un peu avant de refaire clignoter !", 2);
		}
	}
	
	/**
	 * Envoie un title NMS au joueur. Les arguments <i>title</i> et <i>subTitle</i> acceptent 
	 * les couleurs.
	 * @param title Gros titre du title.
	 * @param subTitle Sous-titre du title.
	 * @param timeSeconds Temps en secondes durant lequel le title va rester affiché à l'écran
	 * du joueur.
	 */
	public void sendTitle(String title, String subTitle, int timeSeconds){
		IChatBaseComponent titleComp = ChatSerializer.a("{\"text\": \""+title+"\"}");
		IChatBaseComponent subTitleComp = ChatSerializer.a("{\"text\": \""+subTitle+"\"}");
		
		PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleComp);
		PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subTitleComp);
		
		((CraftPlayer) this.bPlayer).getHandle().playerConnection.sendPacket(titlePacket);	
		((CraftPlayer) this.bPlayer).getHandle().playerConnection.sendPacket(subtitlePacket);	
		
		PacketPlayOutTitle timePacket = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, 20, timeSeconds*20, 20);
		((CraftPlayer) this.bPlayer).getHandle().playerConnection.sendPacket(timePacket);
	}

	/**
	 * Envoie un message au joueur dans le chat
	 * @param message
	 */
	public void sendMessage(String message){
		this.bPlayer.sendMessage(message);
	}
	
	/**
	 * Augmente le score de la valeur passée en paramètre.
	 * @param val Valeur qui sera ajoutée au score.
	 */
	public void increaseScore(int val){
		this.score+=val;
	}
	
	/**
	 * Réduit le score de la valeur passée en paramètre.
	 * @param val Valeur qui sera soustraite au score.
	 */
	public void decreaseScore(int val){
		this.score -= val;
	}
		
	/*Getters et setters*/
	
	/**
	 * @return Renvoie le Player Bukkit représenté par cet objet MDPlayer.
	 */
	public Player getbPlayer() {
		return bPlayer;
	}

	/**
	 * Permet de définir le Player Bukkit de ce MDplayer.
	 * @param bPlayer Nouveau joueur que ce MDPlayer va représenter.
	 */
	public void setbPlayer(Player bPlayer) {
		this.bPlayer = bPlayer;
	}

	/**
	 * @return Renvoie la forme possédée par le joueur.
	 */
	public MDForm getForm() {
		return form;
	}

	/**
	 * Définit la forme qui sera possédée par le joueur
	 * @param form Forme qui sera possédée.
	 */
	public void setForm(MDForm form) {
		this.form = form;
	}

	/**
	 * @return Renvoie le score du joueur.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Définit le score du joueur.
	 * @param score Nouveau score du joueur.
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @return Renvoie la couleur du joueur.
	 */
	public ChatColor getColor() {
		return this.color;
	}

	/**
	 * Définit la couleur du joueur.
	 * @param color Nouvelle couleur du joueur.
	 */
	public void setColor(ChatColor color) {
		this.color = color;
	}
	
	/**
	 * @return Renvoie <b>true</b> si le joueur joue. Renvoie <b>false</b> sinon.
	 */
	public boolean isPlaying() {
		return isPlaying;
	}

	/**
	 * Définit si le joueur est en train de jouer ou non.
	 * @param isPlaying Valeur : est-il en train de jouer ?
	 */
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

	/**
	 * @return Informe de si le joueur peut faire clignoter sa forme ou non <br>
	 * Renvoie <b>true</b> si le joueur peut faire clignoter sa forme. Renvoie <b>false</b> sinon.
	 */
	public boolean canHighlight() {
		return canHighlight;
	}

	/**
	 * Définit si le joueur peut faire clignoter sa forme.
	 * @param canHighlight Booléen définissant s'il peut faire clignoter sa forme. <br>
	 * Mettre <b>true</b> si le joueur peut faire clignoter sa forme. Mettre <b>false</b> sinon.
	 */
	public void setHighlight(boolean canHighlight) {
		this.canHighlight = canHighlight;
	}

	
	
	
}
