package me.yuna.commands.anime;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class AniListRequestTest {
	
	public static final String aniListUrl = "https://graphql.anilist.co";
			
	public static void main(String[] args) throws IOException {
		
		String query = "query  {Media (id: 984, type: ANIME) {id title { romaji english native} description(asHtml: false) coverImage {large} }}";
		System.out.println(httpresponse(aniListUrl, query));

	}

	
	public static String httpresponse(String url, String query) throws IOException{
		
		
		try {
			
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(url);
			request.addHeader("content-type", "application/json");
			request.addHeader("Accept", "application/json");
			request.setEntity(new StringEntity(query));
			HttpResponse response = httpClient.execute(request);
			//return response.getEntity().toString();
			return EntityUtils.toString(response.getEntity(), "UTF-8");
			
		} catch (IOException ex) {
			throw ex;
		}
	}
}
