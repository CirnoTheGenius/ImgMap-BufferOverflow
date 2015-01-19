package net.yukkuricraft.tenko.imgmap.youtubeproc;


import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Deprecated
/**
 * This is a dead API; there's no point in using it since it's inherently prone to bugs, glitches, and nurupos.
 */
public class YouTubeAPI {

	private static final Logger LOGGER = Logger.getLogger("ImgMap-YouTubeAPI");

	public static void main(String[] args){
		for(int i = 0; i < 1000; i++)
			getMP4Video("p1GHd4oVC-w");
	}

	public static void getMP4Video(String id){
		String url = "http://youtube.com/get_video_info?hl=en&video_id=" + id;

		try{
			HttpURLConnection httpConnection = (HttpURLConnection)new URL(url).openConnection();
			httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:34.0) Gecko/20100101 Firefox/34.0 Waterfox/34.0");
			InputStreamReader stream = new InputStreamReader(httpConnection.getInputStream(), "UTF-8");
			String videoInfo = new String(IOUtils.toByteArray(httpConnection.getInputStream()), StandardCharsets.UTF_8);
			stream.close();

			// For some reason, YouTube encodes it twice?
			// Oh, I see why. URLs within URLs is a pain.
			String[] querySplitted = videoInfo.split("&");
			Map<Integer, Map<String, String>> itagList = new HashMap<Integer, Map<String, String>>();

			Iterator<String> streamMap;
			Map<String, String> associatedData;

			String a, b, c, d;
			String[] aData, bData, cData, dData;
			int itag;

			for(String child : querySplitted){
				if(child.startsWith("url_encoded_fmt_stream_map=")){
					if(child.contains("%3D")){
						child = child.replace("%3D", "=");
					}

					if(child.contains("%26")){
						child = child.replace("%26", "&");
					}

					// This is actually a comma, but whatever.
					if(child.contains("%2C")){
						child = child.replace("%2C", "&");
					}

					streamMap = Arrays.asList(URLDecoder.decode(child, "UTF-8").split("&")).iterator();
					streamMap.next(); // Skip the first line since it's always the url_encoded_fmt_stream_map stuff.

					for(int index = 0; index < 4; index++){
						a = streamMap.next();
						b = streamMap.next();
						c = streamMap.next();
						d = streamMap.next();

						associatedData = new HashMap<String, String>();
						itag = -1;

						if(a.startsWith("itag=")){
							itag = Integer.valueOf(a.substring(5));
							bData = b.split("=");
							cData = c.split("=");
							dData = d.split("=");
							associatedData.put(bData[0], bData[1]);
							associatedData.put(cData[0], cData[1]);
							associatedData.put(dData[0], dData[1]);
						} else if(b.startsWith("itag=")){
							itag = Integer.valueOf(b.substring(5));
							aData = a.split("=");
							cData = c.split("=");
							dData = d.split("=");
							associatedData.put(aData[0], aData[1]);
							associatedData.put(cData[0], cData[1]);
							associatedData.put(dData[0], dData[1]);
						} else if(c.startsWith("itag=")){
							itag = Integer.valueOf(c.substring(5));
							aData = a.split("=");
							bData = b.split("=");
							dData = d.split("=");
							associatedData.put(aData[0], aData[1]);
							associatedData.put(bData[0], bData[1]);
							associatedData.put(dData[0], dData[1]);
						} else if(d.startsWith("itag=")){
							itag = Integer.valueOf(d.substring(5));
							aData = a.split("=");
							bData = b.split("=");
							cData = c.split("=");
							associatedData.put(aData[0], aData[1]);
							associatedData.put(bData[0], bData[1]);
							associatedData.put(cData[0], cData[1]);
						}

						if(itag != -1){
							itagList.put(itag, associatedData);
						}
					}
					break;
				}
			}


			int choice = 0;

			for(Map.Entry<Integer, Map<String, String>> e : itagList.entrySet()){

				//if the current set contain data description for the small quality video
				if(e.getValue().containsKey("url"))
					choice = e.getKey();

			}

			System.out.println("choice: " + choice);

			//System.out.println ( URLDecoder.decode ( itagList.get ( choice ).get ( "url" ) ) );
		} catch (MalformedURLException e) {
			LOGGER.log(Level.SEVERE, "Failed to create a URL from the given ID. Is it even valid?", e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Encountered I/O error!", e);
		}
	}


}