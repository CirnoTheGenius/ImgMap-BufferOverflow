package net.yukkuricraft.tenko.imgmap.database;

import net.yukkuricraft.tenko.imgmap.helper.IOHelper;
import net.yukkuricraft.tenko.imgmap.helper.MapHelper;
import net.yukkuricraft.tenko.imgmap.renderer.ImageRenderer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: Come up with a better name.
public class MapDatabase {

	private File yamlFile;
	private Map<String, Map<String, String>> inMemory = new HashMap<String, Map<String, String>>();
	private YamlConfiguration yaml;
	private final Logger logger = Logger.getLogger("ImgMap");

	// Just some test code. If you want to run it, compile it, and run it twice.
	public static void main(String[] arguments){
		File file;
		final MapDatabase base = new MapDatabase(file = new File("maps.yml"));
		System.out.println(file.getAbsolutePath());
		final Random random = new Random();
		for(int i=0; i < Runtime.getRuntime().availableProcessors(); i++){
			final int iCopy = i;
			new Thread(new Runnable() {

				@Override
				public void run() {
					byte[] str = new byte[5];
					for(int i = (iCopy*1000); i < (iCopy*1000)+1000; i++){
						random.nextBytes(str);
						base.updateMapImage((short)i, random.nextBoolean(), random.nextBoolean(), Long.toString(random.nextLong(), 32));
					}
					base.saveYaml();
				}
			}).run();
		}
	}

	public MapDatabase(File yamlFile){
		if(!yamlFile.getName().equalsIgnoreCase("maps.yml")){
			logger.log(Level.WARNING, "Er... We're loading something other than maps.yml. A bit odd.");
			logger.log(Level.WARNING, "File at hand: " + yamlFile);
		}

		this.yamlFile = yamlFile;

		if(!yamlFile.exists()){
			try{
				yamlFile.createNewFile();
			} catch (IOException e){
				e.printStackTrace();
			}
		}

		yaml = YamlConfiguration.loadConfiguration(yamlFile);
		loadYaml();
	}

	public void updateMapImage(short id_, boolean isAnimated, boolean isLocal, String url){
		String id = String.valueOf(id_);
		String animated = String.valueOf(isAnimated);
		String local = String.valueOf(isLocal);
		Map<String, String> data = inMemory.get(id);

		if(data != null){
			data.put("image", url);
			data.put("animated", animated);
			data.put("local", local);
		} else {
			data = new HashMap<>();
			data.put("image", url);
			data.put("animated", animated);
			data.put("local", local);
			inMemory.put(id, data);
		}
	}

	public void removeMap(short id){
		inMemory.remove(String.valueOf(id));
	}

	public void loadYaml(){
		ConfigurationSection section = yaml.getConfigurationSection("maps");
		if(section == null){
			yaml.createSection("maps");
			saveYaml();
			return;
		}

		//I trust that YAML can retain Maps.
		Map<String, Object> onDisk = section.getValues(false);
		for(Map.Entry<String, Object> entry : onDisk.entrySet()){
			try{
				short id = Short.valueOf(entry.getKey());
				MemorySection data = (MemorySection)entry.getValue();

				String imageURL = data.getString("image");
				boolean local = Boolean.valueOf(data.getString("local"));
				boolean animated = Boolean.valueOf(data.getString("animated"));

				if(imageURL == null){
					logger.log(Level.WARNING, "There was a problem while parsing map ID " + id + ": There was no image associated with it.");
					continue; // Invalid data type.
				}

				System.out.println("loaded id " + id + " with values imageURL=" + imageURL + ",local=" + local + ",animated=" + animated);

				MapView view = Bukkit.getMap(id);
				MapHelper.removeRenderers(view);
				Image image = local ? IOHelper.fetchLocalImage(imageURL) : IOHelper.fetchImage(imageURL);
				MapRenderer renderer = animated ? null : new ImageRenderer(image); // Fix for animated images later.
				view.addRenderer(renderer);
			} catch (NumberFormatException | ClassCastException e){
				logger.log(Level.WARNING, "Failed to parse " + entry.getKey(), e);
			} catch (IOException e){
				logger.log(Level.WARNING, "Encountered error while trying to retrieve image on ID " + entry.getKey(), e);
			}
		}
	}

	public void saveYaml(){
		try{
			yaml.createSection("maps", inMemory);
			yaml.save(yamlFile);
		} catch (IOException e){
			e.printStackTrace();
		}
	}

}
