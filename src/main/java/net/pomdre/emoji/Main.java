package net.pomdre.emoji;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;
import org.bstats.bukkit.Metrics;

public class Main extends JavaPlugin implements Listener {
	FileConfiguration config = this.getConfig();

  //https://www.spigotmc.org/threads/concurrentmodificationexception.430268/#post-3759420
  ConcurrentHashMap<String, String> emojiMappings = new ConcurrentHashMap<String, String>();
  String mappingDelimiter = ",", mappingFilename = "shortcodes.txt";
  File mappingFile = new File(getDataFolder() + File.separator + mappingFilename);
  BufferedReader br = null;
  Integer mappingCount = 0;
	
	@Override
    public void onEnable() {
		saveDefaultConfig();
		reloadConfig();
        
        
        getServer().getPluginManager().registerEvents(this,this);
        
        //bStats
        if(getConfig().getBoolean("metrics") == true){
          int pluginId = 61432; // <-- Replace with the id of your plugin!
          Metrics metrics = new Metrics(this, pluginId);
        }
        
        //Update
        Logger logger = this.getLogger();
        
        new UpdateChecker(this, 61432).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {

            } else {
                logger.info("There is a new update available. \n Download it now: \n https://www.spigotmc.org/resources/emoji%E2%9D%A4.61432/");
            }
        });

        logger.info("Loading shortcodes into memory...");

        if (!mappingFile.exists())
          saveResource(mappingFilename, false); //https://bukkit.org/threads/copy-file-from-resources.102015/#post-1345427
        emojiMappings.clear();
        mappingCount = 0;

        //Miscellaneous Symbols
        try { //https://www.geeksforgeeks.org/reading-text-file-into-java-hashmap/
          br = new BufferedReader(new FileReader(mappingFile));
          String line = null;

          while ((line = br.readLine()) != null) {
              String[] lineRead = line.split(mappingDelimiter);
              if (!lineRead[0].equals("") && !lineRead[1].equals("")) {
                String[] colorCode = {"&r", "&r"};

                if (!(getConfig().getString(lineRead[0]) == null) && !(getConfig().getString("suffix_" + lineRead[0]) == null)) { //https://bukkit.org/threads/check-config-for-null-string.366097/#post-3137224
                  colorCode = new String[]{getConfig().getString(lineRead[0]), getConfig().getString("suffix_" + lineRead[0])}; //https://stackoverflow.com/a/20992528

                  emojiMappings.put(lineRead[0], ChatColor.translateAlternateColorCodes('&', colorCode[0]) + lineRead[1] + ChatColor.translateAlternateColorCodes('&', colorCode[1]));
                } else {
                  emojiMappings.put(lineRead[0], lineRead[1]);
                }

                mappingCount++;
              }
          }
          logger.info("Shortcodes successfully loaded!");
        }
        catch (Exception e) { //https://stackoverflow.com/a/6823021
          logger.severe("Failed to parse shortcodes mapping!");
            
          StringWriter writer = new StringWriter();
          PrintWriter printWriter = new PrintWriter(writer);
          e.printStackTrace(printWriter);
          printWriter.flush();
          getLogger().log(Level.SEVERE, writer.toString());
        } finally {
            if (br != null) {
                try {
                    br.close();
                }
                catch (Exception e) {};
            }
        }
        // Uncomment if allowing /emoji list to be dynamic
        //maxpage = mappingCount/getConfig().getInt("pagesperpage")+1; //dynamic
    }
	public void onDisable() {
		
	}
    //ekstra
	String surrounding = getConfig().getString("surrounding");
	int maxpage = 109/getConfig().getInt("pagesperpage")+1;
  

  //Regex Preparation, https://stackoverflow.com/a/1454936 and https://stackoverflow.com/a/5887729
  String regex_string = "(?<=\\" + surrounding.trim() + ")[a-zA-Z0-9-_]+(?=\\"+ surrounding.trim() + ")";  
	
    //Hent config og emoji kode


  //Extra
	String white_draughts_man= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_draughts_man")) + ("\u26C0") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_draughts_man"));
	String white_draughts_king= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_draughts_king")) + ("\u26C1") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_draughts_king"));
	String black_draughts_man= ChatColor.translateAlternateColorCodes('&', getConfig().getString("black_draughts_man")) + ("\u26C2") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_black_draughts_man"));
	String black_draughts_king= ChatColor.translateAlternateColorCodes('&', getConfig().getString("black_draughts_king")) + ("\u26C3") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_black_draughts_king"));

    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
    if(p.hasPermission("emoji.chat")) {
    	if(getConfig().getBoolean("chat") == true){
     String msg = event.getMessage();
 
     //Miscellaneous Symbols
     // Regex Preparation, https://stackoverflow.com/a/1454936 and https://stackoverflow.com/a/5887729
     Pattern msg_pattern = Pattern.compile(regex_string); //https://stackoverflow.com/a/237068
     Matcher msg_matcher = msg_pattern.matcher(msg);
     String shortcode_string = "", emoji_string = "";

     // Check if msg contains pattern for shortcode
    while (msg_matcher.find()) {
       // If pattern matches, get emoji for shortcode
       shortcode_string = msg_matcher.group(0).toLowerCase(Locale.ENGLISH); //https://stackoverflow.com/a/11063161
       emoji_string = emojiMappings.get(shortcode_string);

       if (emoji_string != null)
        msg = msg.replace(surrounding + shortcode_string + surrounding, emoji_string);
     }

     //Extra
     msg = msg.replace("%white_draughts_man%", white_draughts_man);
     msg = msg.replace("%white_draughts_king%", white_draughts_king);
     msg = msg.replace("%black_draughts_man%", black_draughts_man);
     msg = msg.replace("%black_draughts_king%", black_draughts_king);
     
     event.setMessage(msg);
     
    }
   }
}
    
    @EventHandler
    public void onPlayerChatShourtcuts(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
   
     String msg = event.getMessage();
     
     //shortcuts
     if(p.hasPermission("emoji.shortcuts")) { 
      if(getConfig().getBoolean("shortcuts") == true){
        try { msg = msg.replace(":)", emojiMappings.get("white_smiling_face")); }
        catch (Exception e) { getLogger().severe("No shortcode defined for \"white_smiling_face\"!"); }
        
        try { msg = msg.replace(":(", emojiMappings.get("white_frowning_face")); }
        catch (Exception e) { getLogger().severe("No shortcode defined for \"white_frowning_face\"!"); }
        
        try { msg = msg.replace("<3", emojiMappings.get("black_heart_suit")); }
        catch (Exception e) { getLogger().severe("No shortcode defined for \"black_heart_suit\"!"); }
    	}else{
    	//gjør ingenting
    	} 
          
     event.setMessage(msg);
     }
}
   
     
    
    //sign
    @EventHandler
    public void onSignChange(SignChangeEvent e) {
     Player p = e.getPlayer();

     // Regex Preparation, https://stackoverflow.com/a/1454936 and https://stackoverflow.com/a/5887729
     Pattern sign_pattern = Pattern.compile(regex_string); //https://stackoverflow.com/a/237068
     Matcher sign_matcher = null;
     String shortcode_string = "", emoji_string = "";

     if(p.hasPermission("emoji.sign")) {
      if(getConfig().getBoolean("sign") == true){
        for (int i = 0; i < 4; i++) {
        	
        	//Miscellaneous Symbols
          
          sign_matcher = sign_pattern.matcher(e.getLine(i));
          // Check if msg contains pattern for shortcode
          while (sign_matcher.find()) {
            // If pattern matches, get emoji for shortcode
            shortcode_string = sign_matcher.group(0).toLowerCase(Locale.ENGLISH); //https://stackoverflow.com/a/11063161
            emoji_string = emojiMappings.get(shortcode_string);

            if (emoji_string != null) {
              if (e.getLine(i).equalsIgnoreCase(surrounding + shortcode_string + surrounding))
                e.setLine(i, emoji_string);
            } 
          }

        	//Extra
        	if(e.getLine(i).equalsIgnoreCase("%white_draughts_man%")) { e.setLine(i, white_draughts_man); }
        	if(e.getLine(i).equalsIgnoreCase("%white_draughts_king%")) { e.setLine(i, white_draughts_king); }
        	if(e.getLine(i).equalsIgnoreCase("%black_draughts_man%")) { e.setLine(i, black_draughts_man); }
        	if(e.getLine(i).equalsIgnoreCase("%black_draughts_king%")) { e.setLine(i, black_draughts_king); }

        	}
        }
     }
  }
 

    //commands
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("emoji")) {
          if (sender instanceof Player) {
            Player player = (Player) sender;
            
            if (args.length < 1) {
              player.sendMessage(ChatColor.RED + "You can use: /emoji help or /emoji list");
              return true;
              
            } else if (args.length > 0 && args[0].equalsIgnoreCase("list")) {
              if (!(args.length > 1)) {
            	  sender.sendMessage("You must select a number between 1 to " + maxpage);
                return true;
              }
                
              if (!args[1].matches("\\d+")) {
            	  sender.sendMessage("You must select a number between 1 to " + maxpage);
                return true;
              }
              
              int pageNumber = Integer.parseInt(args[1]);
              if (player.hasPermission("emoji.list")) {
                if (pageNumber == 0) {
                  sender.sendMessage("You must select a number between 1 to " + maxpage);
                  return true;
                }

                String message = ChatColor.GOLD + "[Emoji] " + "\n"
                        + "Macro list:" + "\n"
                        + "Page: " + pageNumber + " of " + maxpage + "\n"
                        + ChatColor.RESET;
                List<String> page = getEmojiPage(pageNumber);
                if (page != null) {
                  for (String s : page) {
                    message = message + s;
                  }
                  sender.sendMessage(message);
                  return true;
                }
                sender.sendMessage("This number is too big! \nYou must select a number between 1 to " + maxpage);
              return true;
              }
              sender.sendMessage("You do not have have permission to use this command.");
              return true;
            }
            
      	  else if(args.length > 0 && args[0].equalsIgnoreCase("help")){   
         	 if (player.hasPermission("emoji.help")) { 
	            	  player.sendMessage(
		         		         ChatColor.GOLD + "[Emoji] " + "\n"
		         		        		 		+ "HELP:" + "\n"
		         		        		 		+ "/emoji list -List all macros" + "\n"
		         		        		 		+ "/emoji help -Displays this page" + "\n"
		         		            			  );
         	  
               return true;
              
           }
        }
        	  
          }
          sender.sendMessage("This is not a command!");
          return true;
        }
        return false;
      }

      public List<String> getEmojiPage(int page) {

        List<String> lines = new ArrayList<>();

        //Miscellaneous Symbols
        //Note: This hasn't received a proper rewrite like the others to help maintain the "backward compatibility"
        
        lines.add(surrounding + "black_sun_with_rays" + surrounding + " = " + emojiMappings.get("black_sun_with_rays") + "\n");
        lines.add(surrounding + "cloud" + surrounding + " = " + emojiMappings.get("cloud") + "\n");
        lines.add(surrounding + "umbrella" + surrounding + " = " + emojiMappings.get("umbrella") + "\n");
        lines.add(surrounding + "snowman" + surrounding + " = " + emojiMappings.get("snowman") + "\n");
        lines.add(surrounding + "comet" + surrounding + " = " + emojiMappings.get("comet") + "\n");
        lines.add(surrounding + "black_star" + surrounding + " = " + emojiMappings.get("black_star") + "\n");
        lines.add(surrounding + "white_star" + surrounding + " = " + emojiMappings.get("white_star") + "\n");
        lines.add(surrounding + "lightning" + surrounding + " = " + emojiMappings.get("lightning") + "\n");
        lines.add(surrounding + "thunderstorm" + surrounding + " = " + emojiMappings.get("thunderstorm") + "\n");
        lines.add(surrounding + "sun" + surrounding + " = " + emojiMappings.get("sun") + "\n");
        lines.add(surrounding + "ascending_node" + surrounding + " = " + emojiMappings.get("ascending_node") + "\n");
        lines.add(surrounding + "descending_node" + surrounding + " = " + emojiMappings.get("descending_node") + "\n");
        lines.add(surrounding + "conjunction" + surrounding + " = " + emojiMappings.get("conjunction") + "\n");
        lines.add(surrounding + "opposition" + surrounding + " = " + emojiMappings.get("opposition") + "\n");
        lines.add(surrounding + "black_telephone" + surrounding + " = " + emojiMappings.get("black_telephone") + "\n");
        lines.add(surrounding + "white_telephone" + surrounding + " = " + emojiMappings.get("white_telephone") + "\n");
        lines.add(surrounding + "ballot_box" + surrounding + " = " + emojiMappings.get("ballot_box") + "\n");
        lines.add(surrounding + "ballot_box_with_check" + surrounding + " = " + emojiMappings.get("ballot_box_with_check") + "\n");
        lines.add(surrounding + "ballot_box_with_x" + surrounding + " = " + emojiMappings.get("ballot_box_with_x") + "\n");
        lines.add(surrounding + "saltire" + surrounding + " = " + emojiMappings.get("saltire") + "\n");
        lines.add(surrounding + "reversed_rotated_floral_heart_bullet" + surrounding + " = " + emojiMappings.get("reversed_rotated_floral_heart_bullet") + "\n");
        lines.add(surrounding + "black_left_pointing_index" + surrounding + " = " + emojiMappings.get("black_left_pointing_index") + "\n");
        lines.add(surrounding + "black_right_pointing_index" + surrounding + " = " + emojiMappings.get("black_right_pointing_index") + "\n");
        lines.add(surrounding + "white_left_pointing_index" + surrounding + " = " + emojiMappings.get("white_left_pointing_index") + "\n");
        lines.add(surrounding + "white_up_pointing_index" + surrounding + " = " + emojiMappings.get("white_up_pointing_index") + "\n");
        lines.add(surrounding + "white_right_pointing_index" + surrounding + " = " + emojiMappings.get("white_right_pointing_index") + "\n");
        lines.add(surrounding + "white_down_pointing_index" + surrounding + " = " + emojiMappings.get("white_down_pointing_index") + "\n");
        lines.add(surrounding + "skull_and_crossbones" + surrounding + " = " + emojiMappings.get("skull_and_crossbones") + "\n");
        lines.add(surrounding + "caution_sign" + surrounding + " = " + emojiMappings.get("caution_sign") + "\n");
        lines.add(surrounding + "radioactive_sign" + surrounding + " = " + emojiMappings.get("radioactive_sign") + "\n");
        lines.add(surrounding + "biohazard_sign" + surrounding + " = " + emojiMappings.get("biohazard_sign") + "\n");
        lines.add(surrounding + "caduceus" + surrounding + " = " + emojiMappings.get("caduceus") + "\n");
        lines.add(surrounding + "ankh" + surrounding + " = " + emojiMappings.get("ankh") + "\n");
        lines.add(surrounding + "orthodox_cross" + surrounding + " = " + emojiMappings.get("orthodox_cross") + "\n");
        lines.add(surrounding + "chi_rho" + surrounding + " = " + emojiMappings.get("chi_rho") + "\n");
        lines.add(surrounding + "cross_of_lorraine" + surrounding + " = " + emojiMappings.get("cross_of_lorraine") + "\n");
        lines.add(surrounding + "cross_of_jerusalem" + surrounding + " = " + emojiMappings.get("cross_of_jerusalem") + "\n");
        lines.add(surrounding + "star_and_crescent" + surrounding + " = " + emojiMappings.get("star_and_crescent") + "\n");
        lines.add(surrounding + "farsi_symbol" + surrounding + " = " + emojiMappings.get("farsi_symbol") + "\n");
        lines.add(surrounding + "adi_shakti" + surrounding + " = " + emojiMappings.get("adi_shakti") + "\n");
        lines.add(surrounding + "hammer_and_sickle" + surrounding + " = " + emojiMappings.get("hammer_and_sickle") + "\n");
        lines.add(surrounding + "peace_symbol" + surrounding + " = " + emojiMappings.get("peace_symbol") + "\n");
        lines.add(surrounding + "yin_yang" + surrounding + " = " + emojiMappings.get("yin_yang") + "\n");
        lines.add(surrounding + "trigram_for_heaven" + surrounding + " = " + emojiMappings.get("trigram_for_heaven") + "\n");
        lines.add(surrounding + "trigram_for_lake" + surrounding + " = " + emojiMappings.get("trigram_for_lake") + "\n");
        lines.add(surrounding + "trigram_for_fire" + surrounding + " = " + emojiMappings.get("trigram_for_fire") + "\n");
        lines.add(surrounding + "trigram_for_thunder" + surrounding + " = " + emojiMappings.get("trigram_for_thunder") + "\n");
        lines.add(surrounding + "trigram_for_wind" + surrounding + " = " + emojiMappings.get("trigram_for_wind") + "\n");
        lines.add(surrounding + "trigram_for_water" + surrounding + " = " + emojiMappings.get("trigram_for_water") + "\n");
        lines.add(surrounding + "trigram_for_mountain" + surrounding + " = " + emojiMappings.get("trigram_for_mountain") + "\n");
        lines.add(surrounding + "trigram_for_earth" + surrounding + " = " + emojiMappings.get("trigram_for_earth") + "\n");
        lines.add(surrounding + "wheel_of_dharma" + surrounding + " = " + emojiMappings.get("wheel_of_dharma") + "\n");
        lines.add(surrounding + "white_frowning_face" + surrounding + " = " + emojiMappings.get("white_frowning_face") + "\n");
        lines.add(surrounding + "white_smiling_face" + surrounding + " = " + emojiMappings.get("white_smiling_face") + "\n");
        lines.add(surrounding + "black_smiling_face" + surrounding + " = " + emojiMappings.get("black_smiling_face") + "\n");
        lines.add(surrounding + "white_sun_with_rays" + surrounding + " = " + emojiMappings.get("white_sun_with_rays") + "\n");
        lines.add(surrounding + "first_quarter_moon" + surrounding + " = " + emojiMappings.get("first_quarter_moon") + "\n");
        lines.add(surrounding + "last_quarter_moon" + surrounding + " = " + emojiMappings.get("last_quarter_moon") + "\n");
        lines.add(surrounding + "mercury" + surrounding + " = " + emojiMappings.get("mercury") + "\n");
        lines.add(surrounding + "female_sign" + surrounding + " = " + emojiMappings.get("female_sign") + "\n");
        lines.add(surrounding + "earth" + surrounding + " = " + emojiMappings.get("earth") + "\n");
        lines.add(surrounding + "male_sign" + surrounding + " = " + emojiMappings.get("male_sign") + "\n");
        lines.add(surrounding + "jupiter" + surrounding + " = " + emojiMappings.get("jupiter") + "\n");
        lines.add(surrounding + "saturn" + surrounding + " = " + emojiMappings.get("saturn") + "\n");
        lines.add(surrounding + "uranus" + surrounding + " = " + emojiMappings.get("uranus") + "\n");
        lines.add(surrounding + "neptune" + surrounding + " = " + emojiMappings.get("neptune") + "\n");
        lines.add(surrounding + "pluto" + surrounding + " = " + emojiMappings.get("pluto") + "\n");
        lines.add(surrounding + "aries" + surrounding + " = " + emojiMappings.get("aries") + "\n");
        lines.add(surrounding + "taurus" + surrounding + " = " + emojiMappings.get("taurus") + "\n");
        lines.add(surrounding + "gemini" + surrounding + " = " + emojiMappings.get("gemini") + "\n");
        lines.add(surrounding + "cancer" + surrounding + " = " + emojiMappings.get("cancer") + "\n");
        lines.add(surrounding + "leo" + surrounding + " = " + emojiMappings.get("leo") + "\n");
        lines.add(surrounding + "virgo" + surrounding + " = " + emojiMappings.get("virgo") + "\n");
        lines.add(surrounding + "libra" + surrounding + " = " + emojiMappings.get("libra") + "\n");
        lines.add(surrounding + "scorpius" + surrounding + " = " + emojiMappings.get("scorpius") + "\n");
        lines.add(surrounding + "sagittarius" + surrounding + " = " + emojiMappings.get("sagittarius") + "\n");
        lines.add(surrounding + "capricorn" + surrounding + " = " + emojiMappings.get("capricorn") + "\n");
        lines.add(surrounding + "aquarius" + surrounding + " = " + emojiMappings.get("aquarius") + "\n");
        lines.add(surrounding + "pisces" + surrounding + " = " + emojiMappings.get("pisces") + "\n");
        lines.add(surrounding + "white_chess_king" + surrounding + " = " + emojiMappings.get("white_chess_king") + "\n");
        lines.add(surrounding + "white_chess_queen" + surrounding + " = " + emojiMappings.get("white_chess_queen") + "\n");
        lines.add(surrounding + "white_chess_rook" + surrounding + " = " + emojiMappings.get("white_chess_rook") + "\n");
        lines.add(surrounding + "white_chess_bishop" + surrounding + " = " + emojiMappings.get("white_chess_bishop") + "\n");
        lines.add(surrounding + "white_chess_knight" + surrounding + " = " + emojiMappings.get("white_chess_knight") + "\n");
        lines.add(surrounding + "white_chess_pawn" + surrounding + " = " + emojiMappings.get("white_chess_pawn") + "\n");
        lines.add(surrounding + "black_chess_king" + surrounding + " = " + emojiMappings.get("black_chess_king") + "\n");
        lines.add(surrounding + "black_chess_queen" + surrounding + " = " + emojiMappings.get("black_chess_queen") + "\n");
        lines.add(surrounding + "black_chess_rook" + surrounding + " = " + emojiMappings.get("black_chess_rook") + "\n");
        lines.add(surrounding + "black_chess_bishop" + surrounding + " = " + emojiMappings.get("black_chess_bishop") + "\n");
        lines.add(surrounding + "black_chess_knight" + surrounding + " = " + emojiMappings.get("black_chess_knight") + "\n");
        lines.add(surrounding + "black_chess_pawn" + surrounding + " = " + emojiMappings.get("black_chess_pawn") + "\n");
        lines.add(surrounding + "black_spade_suit" + surrounding + " = " + emojiMappings.get("black_spade_suit") + "\n");
        lines.add(surrounding + "white_heart_suit" + surrounding + " = " + emojiMappings.get("white_heart_suit") + "\n");
        lines.add(surrounding + "white_diamond_suit" + surrounding + " = " + emojiMappings.get("white_diamond_suit") + "\n");
        lines.add(surrounding + "black_club_suit" + surrounding + " = " + emojiMappings.get("black_club_suit") + "\n");
        lines.add(surrounding + "white_spade_suit" + surrounding + " = " + emojiMappings.get("white_spade_suit") + "\n");
        lines.add(surrounding + "black_heart_suit" + surrounding + " = " + emojiMappings.get("black_heart_suit") + "\n");
        lines.add(surrounding + "black_diamond_suit" + surrounding + " = " + emojiMappings.get("black_diamond_suit") + "\n");
        lines.add(surrounding + "white_club_suit" + surrounding + " = " + emojiMappings.get("white_club_suit") + "\n");
        lines.add(surrounding + "hot_springs" + surrounding + " = " + emojiMappings.get("hot_springs") + "\n");
        lines.add(surrounding + "quarter_note" + surrounding + " = " + emojiMappings.get("quarter_note") + "\n");
        lines.add(surrounding + "eighth_note" + surrounding + " = " + emojiMappings.get("eighth_note") + "\n");
        lines.add(surrounding + "beamed_eighth_notes" + surrounding + " = " + emojiMappings.get("beamed_eighth_notes") + "\n");
        lines.add(surrounding + "beamed_sixteenth_notes" + surrounding + " = " + emojiMappings.get("beamed_sixteenth_notes") + "\n");
        lines.add(surrounding + "music_flat_sign" + surrounding + " = " + emojiMappings.get("music_flat_sign") + "\n");
        lines.add(surrounding + "music_natural_sign" + surrounding + " = " + emojiMappings.get("music_natural_sign") + "\n");
        lines.add(surrounding + "music_sharp_sign" + surrounding + " = " + emojiMappings.get("music_sharp_sign") + "\n");
        lines.add(surrounding + "west_syriac_cross" + surrounding + " = " + emojiMappings.get("west_syriac_cross") + "\n");
        lines.add(surrounding + "east_syriac_cross" + surrounding + " = " + emojiMappings.get("east_syriac_cross") + "\n");
        
        // However, should Pomdre want to make this plugin truly dynamic, the above would just need to be
        // removed, and the below three lines uncommented. Everything else is otherwise dynamic except
        // for the "extras" which have been carefully left as-is.
        /*
        for (Map.Entry<String, String> entry : emojiMappings.entrySet()) { //https://stackoverflow.com/a/1066607
          lines.add(surrounding + entry.getKey() + surrounding + " = " + entry.getValue() + "\n");
        }
        */

        //Extra
        lines.add("%white_draughts_man% = "+ white_draughts_man + "\n");
        lines.add("%white_draughts_king% = "+ white_draughts_king + "\n");
        lines.add("%black_draughts_man% = "+ black_draughts_man + "\n");
        lines.add("%black_draughts_king% = "+ black_draughts_king + "\n");

        /* //Drop this into shortcodes.txt if the above are not to be left simply as Extra
          white_draughts_man,⛀
          white_draughts_king,⛁
          black_draughts_man,⛂
          black_draughts_king,⛃
        */

        List<List<String>> partition = Lists.partition(lines, getConfig().getInt("pagesperpage"));
        if (partition.size() >= page) {
          List<String> pageList = partition.get(page - 1);
          return pageList;
        }
        
        return null;
      }
      
    
}