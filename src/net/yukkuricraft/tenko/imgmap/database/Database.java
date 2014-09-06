package net.yukkuricraft.tenko.imgmap.database;

import net.yukkuricraft.tenko.imgmap.ImgMap;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

// TODO: Come up with a better name.
public class Database {

	private File yamlFile = new File(ImgMap.getInstance().getDataFolder(), "maps.yml");
	private YamlConfiguration yaml;

	public Database(){
		if(!yamlFile.exists()){
			try{
				yamlFile.createNewFile();
			} catch (IOException e){
				e.printStackTrace();
			}
		}

		yaml = YamlConfiguration.loadConfiguration(yamlFile);
	}

	public void updateMapImage(short id, String url){
		yaml.set(id + ".image", url);
		saveYaml();
	}

	public void removeMap(short id){
		yaml.set(String.valueOf(id), null);
		saveYaml();
	}

	public void loadYaml(){
		yaml.get
	}

	public void saveYaml(){
		try{
			yaml.save(yamlFile);
		} catch (IOException e){
			e.printStackTrace();
		}
	}

}
