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
  ConcurrentHashMap<String, String> emojiMappings = new ConcurrentHashMap<String, String>(); //https://www.spigotmc.org/threads/concurrentmodificationexception.430268/#post-3759420
  String delimiter = ",", mappingFilename = "shortcodes.txt";
  File mappingFile = new File(getDataFolder() + File.separator + mappingFilename);
  BufferedReader br = null;
	
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

        try { //https://www.geeksforgeeks.org/reading-text-file-into-java-hashmap/
          br = new BufferedReader(new FileReader(mappingFile));
          String line = null;

          while ((line = br.readLine()) != null) {
              String[] lineRead = line.split(delimiter);
              if (!lineRead[0].equals("") && !lineRead[1].equals(""))
                emojiMappings.put(lineRead[0], lineRead[1]);
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
    }
	public void onDisable() {
		
	}
    //ekstra
	String surrounding = getConfig().getString("surrounding");
	int maxpage = 109/getConfig().getInt("pagesperpage")+1;
	
    //Hent config og emoji kode
	//Miscellaneous Symbols
	String black_sun_with_rays= ChatColor.translateAlternateColorCodes('&', getConfig().getString("black_sun_with_rays")) + ("\u2600") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_black_sun_with_rays"));
	String cloud= ChatColor.translateAlternateColorCodes('&', getConfig().getString("cloud")) + ("\u2601") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_cloud"));
	String umbrella= ChatColor.translateAlternateColorCodes('&', getConfig().getString("umbrella")) + ("\u2602") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_umbrella"));
	String snowman= ChatColor.translateAlternateColorCodes('&', getConfig().getString("snowman")) + ("\u2603") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_snowman"));
	String comet= ChatColor.translateAlternateColorCodes('&', getConfig().getString("comet")) + ("\u2604") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_comet"));
	String black_star= ChatColor.translateAlternateColorCodes('&', getConfig().getString("black_star")) + ("\u2605") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_black_star"));
	String white_star= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_star")) + ("\u2606") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_star"));
	String lightning= ChatColor.translateAlternateColorCodes('&', getConfig().getString("lightning")) + ("\u2607") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_lightning"));
	String thunderstorm= ChatColor.translateAlternateColorCodes('&', getConfig().getString("thunderstorm")) + ("\u2608") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_thunderstorm"));
	String sun= ChatColor.translateAlternateColorCodes('&', getConfig().getString("sun")) + ("\u2609") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_sun"));
	String ascending_node= ChatColor.translateAlternateColorCodes('&', getConfig().getString("ascending_node")) + ("\u260A") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_ascending_node"));
	String descending_node= ChatColor.translateAlternateColorCodes('&', getConfig().getString("descending_node")) + ("\u260B") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_descending_node"));
	String conjunction= ChatColor.translateAlternateColorCodes('&', getConfig().getString("conjunction")) + ("\u260C") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_conjunction"));
	String opposition= ChatColor.translateAlternateColorCodes('&', getConfig().getString("opposition")) + ("\u260D") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_opposition"));
	String black_telephone= ChatColor.translateAlternateColorCodes('&', getConfig().getString("black_telephone")) + ("\u260E") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_black_telephone"));
	String white_telephone= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_telephone")) + ("\u260F") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_telephone"));
	String ballot_box= ChatColor.translateAlternateColorCodes('&', getConfig().getString("ballot_box")) + ("\u2610") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_ballot_box"));
	String ballot_box_with_check= ChatColor.translateAlternateColorCodes('&', getConfig().getString("ballot_box_with_check")) + ("\u2611") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_ballot_box_with_check"));
	String ballot_box_with_x= ChatColor.translateAlternateColorCodes('&', getConfig().getString("ballot_box_with_x")) + ("\u2612") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_ballot_box_with_x"));
	String saltire= ChatColor.translateAlternateColorCodes('&', getConfig().getString("saltire")) + ("\u2613") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_saltire"));
	String reversed_rotated_floral_heart_bullet= ChatColor.translateAlternateColorCodes('&', getConfig().getString("reversed_rotated_floral_heart_bullet")) + ("\u2619") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_reversed_rotated_floral_heart_bullet"));
	String black_left_pointing_index= ChatColor.translateAlternateColorCodes('&', getConfig().getString("black_left_pointing_index")) + ("\u261A") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_black_left_pointing_index"));
	String black_right_pointing_index= ChatColor.translateAlternateColorCodes('&', getConfig().getString("black_right_pointing_index")) + ("\u261B") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_black_right_pointing_index"));
	String white_left_pointing_index= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_left_pointing_index")) + ("\u261C") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_left_pointing_index"));
	String white_up_pointing_index= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_up_pointing_index")) + ("\u261D") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_up_pointing_index"));
	String white_right_pointing_index= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_right_pointing_index")) + ("\u261E") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_right_pointing_index"));
	String white_down_pointing_index= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_down_pointing_index")) + ("\u261F") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_down_pointing_index"));
	String skull_and_crossbones= ChatColor.translateAlternateColorCodes('&', getConfig().getString("skull_and_crossbones")) + ("\u2620") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_skull_and_crossbones"));
	String caution_sign= ChatColor.translateAlternateColorCodes('&', getConfig().getString("caution_sign")) + ("\u2621") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_caution_sign"));
	String radioactive_sign= ChatColor.translateAlternateColorCodes('&', getConfig().getString("radioactive_sign")) + ("\u2622") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_radioactive_sign"));
	String biohazard_sign= ChatColor.translateAlternateColorCodes('&', getConfig().getString("biohazard_sign")) + ("\u2623") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_biohazard_sign"));
	String caduceus= ChatColor.translateAlternateColorCodes('&', getConfig().getString("caduceus")) + ("\u2624") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_caduceus"));
	String ankh= ChatColor.translateAlternateColorCodes('&', getConfig().getString("ankh")) + ("\u2625") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_ankh"));
	String orthodox_cross= ChatColor.translateAlternateColorCodes('&', getConfig().getString("orthodox_cross")) + ("\u2626") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_orthodox_cross"));
	String chi_rho= ChatColor.translateAlternateColorCodes('&', getConfig().getString("chi_rho")) + ("\u2627") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_chi_rho"));
	String cross_of_lorraine= ChatColor.translateAlternateColorCodes('&', getConfig().getString("cross_of_lorraine")) + ("\u2628") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_cross_of_lorraine"));
	String cross_of_jerusalem= ChatColor.translateAlternateColorCodes('&', getConfig().getString("cross_of_jerusalem")) + ("\u2629") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_cross_of_jerusalem"));
	String star_and_crescent= ChatColor.translateAlternateColorCodes('&', getConfig().getString("star_and_crescent")) + ("\u262A") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_star_and_crescent"));
	String farsi_symbol= ChatColor.translateAlternateColorCodes('&', getConfig().getString("farsi_symbol")) + ("\u262B") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_farsi_symbol"));
	String adi_shakti= ChatColor.translateAlternateColorCodes('&', getConfig().getString("adi_shakti")) + ("\u262C") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_adi_shakti"));
	String hammer_and_sickle= ChatColor.translateAlternateColorCodes('&', getConfig().getString("hammer_and_sickle")) + ("\u262D") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_hammer_and_sickle"));
	String peace_symbol= ChatColor.translateAlternateColorCodes('&', getConfig().getString("peace_symbol")) + ("\u262E") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_peace_symbol"));
	String yin_yang= ChatColor.translateAlternateColorCodes('&', getConfig().getString("yin_yang")) + ("\u262F") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_yin_yang"));
	String trigram_for_heaven= ChatColor.translateAlternateColorCodes('&', getConfig().getString("trigram_for_heaven")) + ("\u2630") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_trigram_for_heaven"));
	String trigram_for_lake= ChatColor.translateAlternateColorCodes('&', getConfig().getString("trigram_for_lake")) + ("\u2631") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_trigram_for_lake"));
	String trigram_for_fire= ChatColor.translateAlternateColorCodes('&', getConfig().getString("trigram_for_fire")) + ("\u2632") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_trigram_for_fire"));
	String trigram_for_thunder= ChatColor.translateAlternateColorCodes('&', getConfig().getString("trigram_for_thunder")) + ("\u2633") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_trigram_for_thunder"));
	String trigram_for_wind= ChatColor.translateAlternateColorCodes('&', getConfig().getString("trigram_for_wind")) + ("\u2634") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_trigram_for_wind"));
	String trigram_for_water= ChatColor.translateAlternateColorCodes('&', getConfig().getString("trigram_for_water")) + ("\u2635") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_trigram_for_water"));
	String trigram_for_mountain= ChatColor.translateAlternateColorCodes('&', getConfig().getString("trigram_for_mountain")) + ("\u2636") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_trigram_for_mountain"));
	String trigram_for_earth= ChatColor.translateAlternateColorCodes('&', getConfig().getString("trigram_for_earth")) + ("\u2637") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_trigram_for_earth"));
	String wheel_of_dharma= ChatColor.translateAlternateColorCodes('&', getConfig().getString("wheel_of_dharma")) + ("\u2638") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_wheel_of_dharma"));
	String white_frowning_face= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_frowning_face")) + ("\u2639") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_frowning_face"));
	String white_smiling_face= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_smiling_face")) + ("\u263A") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_smiling_face"));
	String black_smiling_face= ChatColor.translateAlternateColorCodes('&', getConfig().getString("black_smiling_face")) + ("\u263B") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_black_smiling_face"));
	String white_sun_with_rays= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_sun_with_rays")) + ("\u263C") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_sun_with_rays"));
	String first_quarter_moon= ChatColor.translateAlternateColorCodes('&', getConfig().getString("first_quarter_moon")) + ("\u263D") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_first_quarter_moon"));
	String last_quarter_moon= ChatColor.translateAlternateColorCodes('&', getConfig().getString("last_quarter_moon")) + ("\u263E") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_last_quarter_moon"));
	String mercury= ChatColor.translateAlternateColorCodes('&', getConfig().getString("mercury")) + ("\u263F") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_mercury"));
	String female_sign= ChatColor.translateAlternateColorCodes('&', getConfig().getString("female_sign")) + ("\u2640") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_female_sign"));
	String earth= ChatColor.translateAlternateColorCodes('&', getConfig().getString("earth")) + ("\u2641") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_earth"));
	String male_sign= ChatColor.translateAlternateColorCodes('&', getConfig().getString("male_sign")) + ("\u2642") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_male_sign"));
	String jupiter= ChatColor.translateAlternateColorCodes('&', getConfig().getString("jupiter")) + ("\u2643") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_jupiter"));
	String saturn= ChatColor.translateAlternateColorCodes('&', getConfig().getString("saturn")) + ("\u2644") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_saturn"));
	String uranus= ChatColor.translateAlternateColorCodes('&', getConfig().getString("uranus")) + ("\u2645") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_uranus"));
	String neptune= ChatColor.translateAlternateColorCodes('&', getConfig().getString("neptune")) + ("\u2646") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_neptune"));
	String pluto= ChatColor.translateAlternateColorCodes('&', getConfig().getString("pluto")) + ("\u2647") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_pluto"));
	String aries= ChatColor.translateAlternateColorCodes('&', getConfig().getString("aries")) + ("\u2648") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_aries"));
	String taurus= ChatColor.translateAlternateColorCodes('&', getConfig().getString("taurus")) + ("\u2649") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_taurus"));
	String gemini= ChatColor.translateAlternateColorCodes('&', getConfig().getString("gemini")) + ("\u264A") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_gemini"));
	String cancer= ChatColor.translateAlternateColorCodes('&', getConfig().getString("cancer")) + ("\u264B") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_cancer"));
	String leo= ChatColor.translateAlternateColorCodes('&', getConfig().getString("leo")) + ("\u264C") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_leo"));
	String virgo= ChatColor.translateAlternateColorCodes('&', getConfig().getString("virgo")) + ("\u264D") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_virgo"));
	String libra= ChatColor.translateAlternateColorCodes('&', getConfig().getString("libra")) + ("\u264E") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_libra"));
	String scorpius= ChatColor.translateAlternateColorCodes('&', getConfig().getString("scorpius")) + ("\u264F") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_scorpius"));
	String sagittarius= ChatColor.translateAlternateColorCodes('&', getConfig().getString("sagittarius")) + ("\u2650") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_sagittarius"));
	String capricorn= ChatColor.translateAlternateColorCodes('&', getConfig().getString("capricorn")) + ("\u2651") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_capricorn"));
	String aquarius= ChatColor.translateAlternateColorCodes('&', getConfig().getString("aquarius")) + ("\u2652") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_aquarius"));
	String pisces= ChatColor.translateAlternateColorCodes('&', getConfig().getString("pisces")) + ("\u2653") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_pisces"));
	String white_chess_king= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_chess_king")) + ("\u2654") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_chess_king"));
	String white_chess_queen= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_chess_queen")) + ("\u2655") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_chess_queen"));
	String white_chess_rook= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_chess_rook")) + ("\u2656") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_chess_rook"));
	String white_chess_bishop= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_chess_bishop")) + ("\u2657") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_chess_bishop"));
	String white_chess_knight= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_chess_knight")) + ("\u2658") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_chess_knight"));
	String white_chess_pawn= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_chess_pawn")) + ("\u2659") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_chess_pawn"));
	String black_chess_king= ChatColor.translateAlternateColorCodes('&', getConfig().getString("black_chess_king")) + ("\u265A") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_black_chess_king"));
	String black_chess_queen= ChatColor.translateAlternateColorCodes('&', getConfig().getString("black_chess_queen")) + ("\u265B") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_black_chess_queen"));
	String black_chess_rook= ChatColor.translateAlternateColorCodes('&', getConfig().getString("black_chess_rook")) + ("\u265C") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_black_chess_rook"));
	String black_chess_bishop= ChatColor.translateAlternateColorCodes('&', getConfig().getString("black_chess_bishop")) + ("\u265D") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_black_chess_bishop"));
	String black_chess_knight= ChatColor.translateAlternateColorCodes('&', getConfig().getString("black_chess_knight")) + ("\u265E") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_black_chess_knight"));
	String black_chess_pawn= ChatColor.translateAlternateColorCodes('&', getConfig().getString("black_chess_pawn")) + ("\u265F") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_black_chess_pawn"));
	String black_spade_suit= ChatColor.translateAlternateColorCodes('&', getConfig().getString("black_spade_suit")) + ("\u2660") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_black_spade_suit"));
	String white_heart_suit= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_heart_suit")) + ("\u2661") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_heart_suit"));
	String white_diamond_suit= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_diamond_suit")) + ("\u2662") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_diamond_suit"));
	String black_club_suit= ChatColor.translateAlternateColorCodes('&', getConfig().getString("black_club_suit")) + ("\u2663") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_black_club_suit"));
	String white_spade_suit= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_spade_suit")) + ("\u2664") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_spade_suit"));
	String black_heart_suit= ChatColor.translateAlternateColorCodes('&', getConfig().getString("black_heart_suit")) + ("\u2665") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_black_heart_suit"));
	String black_diamond_suit= ChatColor.translateAlternateColorCodes('&', getConfig().getString("black_diamond_suit")) + ("\u2666") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_black_diamond_suit"));
	String white_club_suit= ChatColor.translateAlternateColorCodes('&', getConfig().getString("white_club_suit")) + ("\u2667") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_white_club_suit"));
	String hot_springs= ChatColor.translateAlternateColorCodes('&', getConfig().getString("hot_springs")) + ("\u2668") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_hot_springs"));
	String quarter_note= ChatColor.translateAlternateColorCodes('&', getConfig().getString("quarter_note")) + ("\u2669") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_quarter_note"));
	String eighth_note= ChatColor.translateAlternateColorCodes('&', getConfig().getString("eighth_note")) + ("\u266A") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_eighth_note"));
	String beamed_eighth_notes= ChatColor.translateAlternateColorCodes('&', getConfig().getString("beamed_eighth_notes")) + ("\u266B") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_beamed_eighth_notes"));
	String beamed_sixteenth_notes= ChatColor.translateAlternateColorCodes('&', getConfig().getString("beamed_sixteenth_notes")) + ("\u266C") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_beamed_sixteenth_notes"));
	String music_flat_sign= ChatColor.translateAlternateColorCodes('&', getConfig().getString("music_flat_sign")) + ("\u266D") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_music_flat_sign"));
	String music_natural_sign= ChatColor.translateAlternateColorCodes('&', getConfig().getString("music_natural_sign")) + ("\u266E") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_music_natural_sign"));
	String music_sharp_sign= ChatColor.translateAlternateColorCodes('&', getConfig().getString("music_sharp_sign")) + ("\u266F") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_music_sharp_sign"));
	String west_syriac_cross= ChatColor.translateAlternateColorCodes('&', getConfig().getString("west_syriac_cross")) + ("\u2670") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_west_syriac_cross"));
	String east_syriac_cross= ChatColor.translateAlternateColorCodes('&', getConfig().getString("east_syriac_cross")) + ("\u2671") + ChatColor.translateAlternateColorCodes('&', getConfig().getString("suffix_east_syriac_cross"));
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
     String regex_string = "(?<=\\" + surrounding.trim() + ")[a-zA-Z0-9-_]+(?=\\"+ surrounding.trim() + ")";
     Pattern msg_pattern = Pattern.compile(regex_string); //https://stackoverflow.com/a/237068
     Matcher msg_matcher = msg_pattern.matcher(msg);
     String shortcode_string = "", emoji_string = "";

     // Check if msg contains pattern for shortcode
    while (msg_matcher.find()) {
       // If pattern matches, get emoji for shortcode
       shortcode_string = msg_matcher.group(0).toLowerCase(Locale.ENGLISH); //https://stackoverflow.com/a/11063161
       emoji_string = emojiMappings.get(shortcode_string);

       if (emoji_string != null)
        msg = msg.replace(surrounding + shortcode_string + surrounding + "", emoji_string + "");
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
          msg = msg.replace(":)", white_smiling_face);
          msg = msg.replace(":(", white_frowning_face);
          msg = msg.replace("<3", black_heart_suit);
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
     if(p.hasPermission("emoji.sign")) {
      if(getConfig().getBoolean("sign") == true){
        for (int i = 0; i < 4; i++) {
        	
        	//Miscellaneous Symbols
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "black_sun_with_rays" + surrounding)) { e.setLine(i, black_sun_with_rays); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "cloud" + surrounding)) { e.setLine(i, cloud); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "umbrella" + surrounding)) { e.setLine(i, umbrella); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "snowman" + surrounding)) { e.setLine(i, snowman); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "comet" + surrounding)) { e.setLine(i, comet); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "black_star" + surrounding)) { e.setLine(i, black_star); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "white_star" + surrounding)) { e.setLine(i, white_star); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "lightning" + surrounding)) { e.setLine(i, lightning); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "thunderstorm" + surrounding)) { e.setLine(i, thunderstorm); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "sun" + surrounding)) { e.setLine(i, sun); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "ascending_node" + surrounding)) { e.setLine(i, ascending_node); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "descending_node" + surrounding)) { e.setLine(i, descending_node); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "conjunction" + surrounding)) { e.setLine(i, conjunction); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "opposition" + surrounding)) { e.setLine(i, opposition); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "black_telephone" + surrounding)) { e.setLine(i, black_telephone); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "white_telephone" + surrounding)) { e.setLine(i, white_telephone); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "ballot_box" + surrounding)) { e.setLine(i, ballot_box); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "ballot_box_with_check" + surrounding)) { e.setLine(i, ballot_box_with_check); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "ballot_box_with_x" + surrounding)) { e.setLine(i, ballot_box_with_x); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "saltire" + surrounding)) { e.setLine(i, saltire); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "reversed_rotated_floral_heart_bullet" + surrounding)) { e.setLine(i, reversed_rotated_floral_heart_bullet); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "black_left_pointing_index" + surrounding)) { e.setLine(i, black_left_pointing_index); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "black_right_pointing_index" + surrounding)) { e.setLine(i, black_right_pointing_index); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "white_left_pointing_index" + surrounding)) { e.setLine(i, white_left_pointing_index); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "white_up_pointing_index" + surrounding)) { e.setLine(i, white_up_pointing_index); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "white_right_pointing_index" + surrounding)) { e.setLine(i, white_right_pointing_index); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "white_down_pointing_index" + surrounding)) { e.setLine(i, white_down_pointing_index); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "skull_and_crossbones" + surrounding)) { e.setLine(i, skull_and_crossbones); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "caution_sign" + surrounding)) { e.setLine(i, caution_sign); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "radioactive_sign" + surrounding)) { e.setLine(i, radioactive_sign); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "biohazard_sign" + surrounding)) { e.setLine(i, biohazard_sign); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "caduceus" + surrounding)) { e.setLine(i, caduceus); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "ankh" + surrounding)) { e.setLine(i, ankh); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "orthodox_cross" + surrounding)) { e.setLine(i, orthodox_cross); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "chi_rho" + surrounding)) { e.setLine(i, chi_rho); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "cross_of_lorraine" + surrounding)) { e.setLine(i, cross_of_lorraine); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "cross_of_jerusalem" + surrounding)) { e.setLine(i, cross_of_jerusalem); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "star_and_crescent" + surrounding)) { e.setLine(i, star_and_crescent); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "farsi_symbol" + surrounding)) { e.setLine(i, farsi_symbol); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "adi_shakti" + surrounding)) { e.setLine(i, adi_shakti); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "hammer_and_sickle" + surrounding)) { e.setLine(i, hammer_and_sickle); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "peace_symbol" + surrounding)) { e.setLine(i, peace_symbol); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "yin_yang" + surrounding)) { e.setLine(i, yin_yang); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "trigram_for_heaven" + surrounding)) { e.setLine(i, trigram_for_heaven); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "trigram_for_lake" + surrounding)) { e.setLine(i, trigram_for_lake); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "trigram_for_fire" + surrounding)) { e.setLine(i, trigram_for_fire); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "trigram_for_thunder" + surrounding)) { e.setLine(i, trigram_for_thunder); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "trigram_for_wind" + surrounding)) { e.setLine(i, trigram_for_wind); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "trigram_for_water" + surrounding)) { e.setLine(i, trigram_for_water); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "trigram_for_mountain" + surrounding)) { e.setLine(i, trigram_for_mountain); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "trigram_for_earth" + surrounding)) { e.setLine(i, trigram_for_earth); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "wheel_of_dharma" + surrounding)) { e.setLine(i, wheel_of_dharma); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "white_frowning_face" + surrounding)) { e.setLine(i, white_frowning_face); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "white_smiling_face" + surrounding)) { e.setLine(i, white_smiling_face); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "black_smiling_face" + surrounding)) { e.setLine(i, black_smiling_face); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "white_sun_with_rays" + surrounding)) { e.setLine(i, white_sun_with_rays); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "first_quarter_moon" + surrounding)) { e.setLine(i, first_quarter_moon); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "last_quarter_moon" + surrounding)) { e.setLine(i, last_quarter_moon); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "mercury" + surrounding)) { e.setLine(i, mercury); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "female_sign" + surrounding)) { e.setLine(i, female_sign); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "earth" + surrounding)) { e.setLine(i, earth); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "male_sign" + surrounding)) { e.setLine(i, male_sign); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "jupiter" + surrounding)) { e.setLine(i, jupiter); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "saturn" + surrounding)) { e.setLine(i, saturn); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "uranus" + surrounding)) { e.setLine(i, uranus); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "neptune" + surrounding)) { e.setLine(i, neptune); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "pluto" + surrounding)) { e.setLine(i, pluto); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "aries" + surrounding)) { e.setLine(i, aries); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "taurus" + surrounding)) { e.setLine(i, taurus); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "gemini" + surrounding)) { e.setLine(i, gemini); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "cancer" + surrounding)) { e.setLine(i, cancer); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "leo" + surrounding)) { e.setLine(i, leo); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "virgo" + surrounding)) { e.setLine(i, virgo); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "libra" + surrounding)) { e.setLine(i, libra); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "scorpius" + surrounding)) { e.setLine(i, scorpius); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "sagittarius" + surrounding)) { e.setLine(i, sagittarius); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "capricorn" + surrounding)) { e.setLine(i, capricorn); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "aquarius" + surrounding)) { e.setLine(i, aquarius); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "pisces" + surrounding)) { e.setLine(i, pisces); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "white_chess_king" + surrounding)) { e.setLine(i, white_chess_king); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "white_chess_queen" + surrounding)) { e.setLine(i, white_chess_queen); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "white_chess_rook" + surrounding)) { e.setLine(i, white_chess_rook); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "white_chess_bishop" + surrounding)) { e.setLine(i, white_chess_bishop); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "white_chess_knight" + surrounding)) { e.setLine(i, white_chess_knight); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "white_chess_pawn" + surrounding)) { e.setLine(i, white_chess_pawn); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "black_chess_king" + surrounding)) { e.setLine(i, black_chess_king); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "black_chess_queen" + surrounding)) { e.setLine(i, black_chess_queen); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "black_chess_rook" + surrounding)) { e.setLine(i, black_chess_rook); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "black_chess_bishop" + surrounding)) { e.setLine(i, black_chess_bishop); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "black_chess_knight" + surrounding)) { e.setLine(i, black_chess_knight); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "black_chess_pawn" + surrounding)) { e.setLine(i, black_chess_pawn); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "black_spade_suit" + surrounding)) { e.setLine(i, black_spade_suit); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "white_heart_suit" + surrounding)) { e.setLine(i, white_heart_suit); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "white_diamond_suit" + surrounding)) { e.setLine(i, white_diamond_suit); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "black_club_suit" + surrounding)) { e.setLine(i, black_club_suit); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "white_spade_suit" + surrounding)) { e.setLine(i, white_spade_suit); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "black_heart_suit" + surrounding)) { e.setLine(i, black_heart_suit); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "black_diamond_suit" + surrounding)) { e.setLine(i, black_diamond_suit); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "white_club_suit" + surrounding)) { e.setLine(i, white_club_suit); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "hot_springs" + surrounding)) { e.setLine(i, hot_springs); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "quarter_note" + surrounding)) { e.setLine(i, quarter_note); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "eighth_note" + surrounding)) { e.setLine(i, eighth_note); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "beamed_eighth_notes" + surrounding)) { e.setLine(i, beamed_eighth_notes); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "beamed_sixteenth_notes" + surrounding)) { e.setLine(i, beamed_sixteenth_notes); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "music_flat_sign" + surrounding)) { e.setLine(i, music_flat_sign); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "music_natural_sign" + surrounding)) { e.setLine(i, music_natural_sign); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "music_sharp_sign" + surrounding)) { e.setLine(i, music_sharp_sign); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "west_syriac_cross" + surrounding)) { e.setLine(i, west_syriac_cross); }
        	if(e.getLine(i).equalsIgnoreCase(surrounding + "east_syriac_cross" + surrounding)) { e.setLine(i, east_syriac_cross); }
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
        lines.add(surrounding + "black_sun_with_rays" + surrounding + " = " + black_sun_with_rays + "\n");
        lines.add(surrounding + "cloud" + surrounding + " = " + cloud + "\n");
        lines.add(surrounding + "umbrella" + surrounding + " = " + umbrella + "\n");
        lines.add(surrounding + "snowman" + surrounding + " = " + snowman + "\n");
        lines.add(surrounding + "comet" + surrounding + " = " + comet + "\n");
        lines.add(surrounding + "black_star" + surrounding + " = " + black_star + "\n");
        lines.add(surrounding + "white_star" + surrounding + " = " + white_star + "\n");
        lines.add(surrounding + "lightning" + surrounding + " = " + lightning + "\n");
        lines.add(surrounding + "thunderstorm" + surrounding + " = " + thunderstorm + "\n");
        lines.add(surrounding + "sun" + surrounding + " = " + sun + "\n");
        lines.add(surrounding + "ascending_node" + surrounding + " = " + ascending_node + "\n");
        lines.add(surrounding + "descending_node" + surrounding + " = " + descending_node + "\n");
        lines.add(surrounding + "conjunction" + surrounding + " = " + conjunction + "\n");
        lines.add(surrounding + "opposition" + surrounding + " = " + opposition + "\n");
        lines.add(surrounding + "black_telephone" + surrounding + " = " + black_telephone + "\n");
        lines.add(surrounding + "white_telephone" + surrounding + " = " + white_telephone + "\n");
        lines.add(surrounding + "ballot_box" + surrounding + " = " + ballot_box + "\n");
        lines.add(surrounding + "ballot_box_with_check" + surrounding + " = " + ballot_box_with_check + "\n");
        lines.add(surrounding + "ballot_box_with_x" + surrounding + " = " + ballot_box_with_x + "\n");
        lines.add(surrounding + "saltire" + surrounding + " = " + saltire + "\n");
        lines.add(surrounding + "reversed_rotated_floral_heart_bullet" + surrounding + " = " + reversed_rotated_floral_heart_bullet + "\n");
        lines.add(surrounding + "black_left_pointing_index" + surrounding + " = " + black_left_pointing_index + "\n");
        lines.add(surrounding + "black_right_pointing_index" + surrounding + " = " + black_right_pointing_index + "\n");
        lines.add(surrounding + "white_left_pointing_index" + surrounding + " = " + white_left_pointing_index + "\n");
        lines.add(surrounding + "white_up_pointing_index" + surrounding + " = " + white_up_pointing_index + "\n");
        lines.add(surrounding + "white_right_pointing_index" + surrounding + " = " + white_right_pointing_index + "\n");
        lines.add(surrounding + "white_down_pointing_index" + surrounding + " = " + white_down_pointing_index + "\n");
        lines.add(surrounding + "skull_and_crossbones" + surrounding + " = " + skull_and_crossbones + "\n");
        lines.add(surrounding + "caution_sign" + surrounding + " = " + caution_sign + "\n");
        lines.add(surrounding + "radioactive_sign" + surrounding + " = " + radioactive_sign + "\n");
        lines.add(surrounding + "biohazard_sign" + surrounding + " = " + biohazard_sign + "\n");
        lines.add(surrounding + "caduceus" + surrounding + " = " + caduceus + "\n");
        lines.add(surrounding + "ankh" + surrounding + " = " + ankh + "\n");
        lines.add(surrounding + "orthodox_cross" + surrounding + " = " + orthodox_cross + "\n");
        lines.add(surrounding + "chi_rho" + surrounding + " = " + chi_rho + "\n");
        lines.add(surrounding + "cross_of_lorraine" + surrounding + " = " + cross_of_lorraine + "\n");
        lines.add(surrounding + "cross_of_jerusalem" + surrounding + " = " + cross_of_jerusalem + "\n");
        lines.add(surrounding + "star_and_crescent" + surrounding + " = " + star_and_crescent + "\n");
        lines.add(surrounding + "farsi_symbol" + surrounding + " = " + farsi_symbol + "\n");
        lines.add(surrounding + "adi_shakti" + surrounding + " = " + adi_shakti + "\n");
        lines.add(surrounding + "hammer_and_sickle" + surrounding + " = " + hammer_and_sickle + "\n");
        lines.add(surrounding + "peace_symbol" + surrounding + " = " + peace_symbol + "\n");
        lines.add(surrounding + "yin_yang" + surrounding + " = " + yin_yang + "\n");
        lines.add(surrounding + "trigram_for_heaven" + surrounding + " = " + trigram_for_heaven + "\n");
        lines.add(surrounding + "trigram_for_lake" + surrounding + " = " + trigram_for_lake + "\n");
        lines.add(surrounding + "trigram_for_fire" + surrounding + " = " + trigram_for_fire + "\n");
        lines.add(surrounding + "trigram_for_thunder" + surrounding + " = " + trigram_for_thunder + "\n");
        lines.add(surrounding + "trigram_for_wind" + surrounding + " = " + trigram_for_wind + "\n");
        lines.add(surrounding + "trigram_for_water" + surrounding + " = " + trigram_for_water + "\n");
        lines.add(surrounding + "trigram_for_mountain" + surrounding + " = " + trigram_for_mountain + "\n");
        lines.add(surrounding + "trigram_for_earth" + surrounding + " = " + trigram_for_earth + "\n");
        lines.add(surrounding + "wheel_of_dharma" + surrounding + " = " + wheel_of_dharma + "\n");
        lines.add(surrounding + "white_frowning_face" + surrounding + " = " + white_frowning_face + "\n");
        lines.add(surrounding + "white_smiling_face" + surrounding + " = " + white_smiling_face + "\n");
        lines.add(surrounding + "black_smiling_face" + surrounding + " = " + black_smiling_face + "\n");
        lines.add(surrounding + "white_sun_with_rays" + surrounding + " = " + white_sun_with_rays + "\n");
        lines.add(surrounding + "first_quarter_moon" + surrounding + " = " + first_quarter_moon + "\n");
        lines.add(surrounding + "last_quarter_moon" + surrounding + " = " + last_quarter_moon + "\n");
        lines.add(surrounding + "mercury" + surrounding + " = " + mercury + "\n");
        lines.add(surrounding + "female_sign" + surrounding + " = " + female_sign + "\n");
        lines.add(surrounding + "earth" + surrounding + " = " + earth + "\n");
        lines.add(surrounding + "male_sign" + surrounding + " = " + male_sign + "\n");
        lines.add(surrounding + "jupiter" + surrounding + " = " + jupiter + "\n");
        lines.add(surrounding + "saturn" + surrounding + " = " + saturn + "\n");
        lines.add(surrounding + "uranus" + surrounding + " = " + uranus + "\n");
        lines.add(surrounding + "neptune" + surrounding + " = " + neptune + "\n");
        lines.add(surrounding + "pluto" + surrounding + " = " + pluto + "\n");
        lines.add(surrounding + "aries" + surrounding + " = " + aries + "\n");
        lines.add(surrounding + "taurus" + surrounding + " = " + taurus + "\n");
        lines.add(surrounding + "gemini" + surrounding + " = " + gemini + "\n");
        lines.add(surrounding + "cancer" + surrounding + " = " + cancer + "\n");
        lines.add(surrounding + "leo" + surrounding + " = " + leo + "\n");
        lines.add(surrounding + "virgo" + surrounding + " = " + virgo + "\n");
        lines.add(surrounding + "libra" + surrounding + " = " + libra + "\n");
        lines.add(surrounding + "scorpius" + surrounding + " = " + scorpius + "\n");
        lines.add(surrounding + "sagittarius" + surrounding + " = " + sagittarius + "\n");
        lines.add(surrounding + "capricorn" + surrounding + " = " + capricorn + "\n");
        lines.add(surrounding + "aquarius" + surrounding + " = " + aquarius + "\n");
        lines.add(surrounding + "pisces" + surrounding + " = " + pisces + "\n");
        lines.add(surrounding + "white_chess_king" + surrounding + " = " + white_chess_king + "\n");
        lines.add(surrounding + "white_chess_queen" + surrounding + " = " + white_chess_queen + "\n");
        lines.add(surrounding + "white_chess_rook" + surrounding + " = " + white_chess_rook + "\n");
        lines.add(surrounding + "white_chess_bishop" + surrounding + " = " + white_chess_bishop + "\n");
        lines.add(surrounding + "white_chess_knight" + surrounding + " = " + white_chess_knight + "\n");
        lines.add(surrounding + "white_chess_pawn" + surrounding + " = " + white_chess_pawn + "\n");
        lines.add(surrounding + "black_chess_king" + surrounding + " = " + black_chess_king + "\n");
        lines.add(surrounding + "black_chess_queen" + surrounding + " = " + black_chess_queen + "\n");
        lines.add(surrounding + "black_chess_rook" + surrounding + " = " + black_chess_rook + "\n");
        lines.add(surrounding + "black_chess_bishop" + surrounding + " = " + black_chess_bishop + "\n");
        lines.add(surrounding + "black_chess_knight" + surrounding + " = " + black_chess_knight + "\n");
        lines.add(surrounding + "black_chess_pawn" + surrounding + " = " + black_chess_pawn + "\n");
        lines.add(surrounding + "black_spade_suit" + surrounding + " = " + black_spade_suit + "\n");
        lines.add(surrounding + "white_heart_suit" + surrounding + " = " + white_heart_suit + "\n");
        lines.add(surrounding + "white_diamond_suit" + surrounding + " = " + white_diamond_suit + "\n");
        lines.add(surrounding + "black_club_suit" + surrounding + " = " + black_club_suit + "\n");
        lines.add(surrounding + "white_spade_suit" + surrounding + " = " + white_spade_suit + "\n");
        lines.add(surrounding + "black_heart_suit" + surrounding + " = " + black_heart_suit + "\n");
        lines.add(surrounding + "black_diamond_suit" + surrounding + " = " + black_diamond_suit + "\n");
        lines.add(surrounding + "white_club_suit" + surrounding + " = " + white_club_suit + "\n");
        lines.add(surrounding + "hot_springs" + surrounding + " = " + hot_springs + "\n");
        lines.add(surrounding + "quarter_note" + surrounding + " = " + quarter_note + "\n");
        lines.add(surrounding + "eighth_note" + surrounding + " = " + eighth_note + "\n");
        lines.add(surrounding + "beamed_eighth_notes" + surrounding + " = " + beamed_eighth_notes + "\n");
        lines.add(surrounding + "beamed_sixteenth_notes" + surrounding + " = " + beamed_sixteenth_notes + "\n");
        lines.add(surrounding + "music_flat_sign" + surrounding + " = " + music_flat_sign + "\n");
        lines.add(surrounding + "music_natural_sign" + surrounding + " = " + music_natural_sign + "\n");
        lines.add(surrounding + "music_sharp_sign" + surrounding + " = " + music_sharp_sign + "\n");
        lines.add(surrounding + "west_syriac_cross" + surrounding + " = " + west_syriac_cross + "\n");
        lines.add(surrounding + "east_syriac_cross" + surrounding + " = " + east_syriac_cross + "\n");
        //Extra
        lines.add("%white_draughts_man% = "+ white_draughts_man + "\n");
        lines.add("%white_draughts_king% = "+ white_draughts_king + "\n");
        lines.add("%black_draughts_man% = "+ black_draughts_man + "\n");
        lines.add("%black_draughts_king% = "+ black_draughts_king + "\n");

        List<List<String>> partition = Lists.partition(lines, getConfig().getInt("pagesperpage"));
        if (partition.size() >= page) {
          List<String> pageList = partition.get(page - 1);
          return pageList;
        }
        
        return null;
      }
      
    
}