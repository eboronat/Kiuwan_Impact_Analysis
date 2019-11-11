import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


public class ImpactAnalysis {
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		
		if (args.length != 4) {
			System.out.println("Program must have 4 arguments: ImpactAnalysis <appName> <componentName> <typeOfComponent> <navigationDirection>");
			return;
		}

        HttpClient httpclient = HttpClientBuilder.create().build();
        //For making subsequent requests token X-CSRF-TOKEN (taken from obtained cookie from first request) must be set inside header, or ignore cookies like here.
        RequestConfig params = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();

        HttpPost post = new HttpPost("https://www.kiuwan.com/saas/rest/v1/arch/impact/searchTargets");
        post.setConfig(params);

        String name = "username";
		String password = "password";
		String authString = name + ":" + password;
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
        post.addHeader("Authorization", "Basic " + authStringEnc);
        post.addHeader("Content-Type", "application/json");

		// add JSON object 
		//(see https://www.kiuwan.com/docs/display/K5/Architecture#Architecture-%C2%AB%C2%BBListimpactedcomponents for JSON Object structure)
        String json = "{"
        		+ "\"applicationName\":\""+args[0]+"\","
        		+ "\"sourceFilter\":{"
        		+ "\"contains\":\""+args[1]+"\","
        		+ "\"types\":\""+args[2]+"\""
        		+ "},"
        		+ "\"targetFilter\":"
        		+ "{\"types\":\"Class\"},"
        		+ "\"navigationFilter\":{"
        		+ "\"direction\":\""+args[3]+"\","
        		+ "\"depth\":2"
        		+ "},"
        		+ "\"pagination\":{"
        		+ "\"page\":-1,"
        		+ "\"count\":500"
        		+ "}"
        		+ "}";

        StringEntity stringEntity = new StringEntity(json);
   		post.setEntity(stringEntity);
        		
        System.out.println("Executing request " + post.getRequestLine());

        HttpResponse response = httpclient.execute(post);
        StatusLine status = response.getStatusLine();
        System.out.println("----------------------------------------");
        System.out.println(status);
        
        if (status.getStatusCode() >= 200 && status.getStatusCode() < 300) {
	        System.out.println("----------------------------------------");
	        
	        String json_response = EntityUtils.toString(response.getEntity());
	        JSONObject jsonObj = new JSONObject(json_response);
	        JSONArray jsonArr = jsonObj.getJSONArray("data");
	        for (int i = 0; i < jsonArr.length(); i++) {
	            System.out.println("Component Impacted Name: "+jsonArr.getJSONObject(i).get("name") 
	            		+ " (Type: " + jsonArr.getJSONObject(i).get("type") + ")");
	        }
        }
	}
}