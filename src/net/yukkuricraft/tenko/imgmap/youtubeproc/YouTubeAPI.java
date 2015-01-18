package net.yukkuricraft.tenko.imgmap.youtubeproc;


import java.io.ByteArrayOutputStream;
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

public class YouTubeAPI {

	private static final Logger LOGGER = Logger.getLogger("ImgMap-YouTubeAPI");

	public static void main(String[] args){
		getMP4Video("p1GHd4oVC-w");
	}

	public static void getMP4Video(String id){
		String url = "http://youtube.com/get_video_info?hl=en&video_id=" + id;
		try{
			HttpURLConnection httpConnection = (HttpURLConnection)new URL(url).openConnection();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			InputStreamReader stream = new InputStreamReader(httpConnection.getInputStream(), StandardCharsets.UTF_8);
			int i;

			while((i = stream.read()) != -1){
				baos.write(i);
			}

			String videoInfo = new String(baos.toByteArray(), StandardCharsets.UTF_8);
			// For some reason, YouTube encodes it twice?
			// Oh, I see why. URLs within URLs is a pain.
			String[] querySplitted = videoInfo.split("&");
			Map<Integer, Map<String, String>> itagList = new HashMap<Integer, Map<String, String>>();
			for(String child : querySplitted){
				if(child.startsWith("url_encoded_fmt_stream_map=")){
					if(child.contains("%3D")){
						child = child.replace("%3D", "=");
					}

					if(child.contains("%26")){
						child = child.replace("%26", "&");
					}

					if(child.contains("%2C")){
						child = child.replace("%2C", "&");
					}

					System.out.println(child);
					Iterator<String> streamMap = Arrays.asList(URLDecoder.decode(child, "UTF-8").split("&")).iterator();
					streamMap.next(); // Skip the first line since it's always the url_encoded_fmt_stream_map stuff.
					for(int index=0; index < 4; index++){
						String a = streamMap.next();
						String b = streamMap.next();
						String c = streamMap.next();
						String d = streamMap.next();

						Map<String, String> associatedData = new HashMap<String, String>();
						int itag = -1;
						if(a.startsWith("itag=")){
							itag = Integer.valueOf(a.substring(5));
							String[] bData = b.split("=");
							String[] cData = c.split("=");
							String[] dData = d.split("=");
							associatedData.put(bData[0], bData[1]);
							associatedData.put(cData[0], cData[1]);
							associatedData.put(dData[0], dData[1]);
						} else if(b.startsWith("itag=")){
							itag = Integer.valueOf(b.substring(5));
							String[] aData = a.split("=");
							String[] cData = c.split("=");
							String[] dData = d.split("=");
							associatedData.put(aData[0], aData[1]);
							associatedData.put(cData[0], cData[1]);
							associatedData.put(dData[0], dData[1]);
						} else if(c.startsWith("itag=")){
							itag = Integer.valueOf(c.substring(5));
							String[] aData = a.split("=");
							String[] bData = b.split("=");
							String[] dData = d.split("=");
							associatedData.put(aData[0], aData[1]);
							associatedData.put(bData[0], bData[1]);
							associatedData.put(dData[0], dData[1]);
						} else if(d.startsWith("itag=")){
							itag = Integer.valueOf(d.substring(5));
							String[] aData = a.split("=");
							String[] bData = b.split("=");
							String[] cData = c.split("=");
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

			System.out.println(itagList.toString());
			System.out.println(URLDecoder.decode(itagList.get(18).get("url")));
		} catch (MalformedURLException e){
			LOGGER.log(Level.SEVERE, "Failed to create a URL from the given ID. Is it even valid?", e);
		} catch (IOException e){
			LOGGER.log(Level.SEVERE, "Encountered I/O error!", e);
		}
	}


}