package net.yukkuricraft.tenko.imgmap.youtubeproc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouTubeAPIRegex {

	private static final Logger LOGGER = Logger.getLogger("ImgMap-YouTubeAPI");
	private static final Pattern URL_PATTERN = Pattern.compile(".url=(.*?)[&|,]", Pattern.DOTALL);

	public static List<String> getDirectLinks(String youtubeID){
		String url = "http://youtube.com/get_video_info?hl=en&video_id=" + youtubeID;
		String videoInfo;

		try{
			HttpURLConnection httpConnection = (HttpURLConnection)new URL(url).openConnection();
			httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:34.0) Gecko/20100101 Firefox/34.0 Waterfox/34.0");

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int i;
			InputStream stream = httpConnection.getInputStream();

			while((i = stream.read()) != -1){
				baos.write(i);
			}

			videoInfo = new String(baos.toByteArray(), StandardCharsets.UTF_8);
		} catch (IOException e){
			LOGGER.log(Level.SEVERE, "Encountered I/O error!", e);
			return Collections.EMPTY_LIST;
		}

		Matcher urlMatcher;
		List<String> directLinks = new ArrayList<String>();
		String[] httpQueries = videoInfo.split("&");
		String link;
		for(String query : httpQueries){
			if(query.startsWith("url_encoded_fmt_stream_map=")){
				query = URLDecoder.decode(query);
				urlMatcher = URL_PATTERN.matcher(query);
				while(urlMatcher.find()){
					link = urlMatcher.group(0);
					// We need the ratebypass=yes param to allow us to download faster.
					// YT limits speeds to something like 15fps.
					if(link.contains("ratebypass")){
						directLinks.add(URLDecoder.decode(link.substring(5, link.length() - 1)));
					}
				}
				break;
			}
		}

		return directLinks;
	}

}