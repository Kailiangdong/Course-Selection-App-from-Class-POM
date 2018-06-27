package net.hockeyapp.javafx.simple;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class FeedbackManager {

	private String identifier = null;
    private String urlString;

	private String getURLString() {
		return urlString + "api/2/apps/" + identifier + "/feedback/";
	}
	
	public FeedbackManager(String appIdentifier) {
		urlString = Constants.BASE_URL;
		this.identifier = appIdentifier;
	}
	
	public void stop() {
		
	}
    
    public HashMap<String, String> submitFeedback(String name, String email, String subject, String text) {
        HashMap<String, String> result = new HashMap<String, String>();
        result.put("type", "send");

        HttpURLConnection urlConnection = null;
        try {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("name", name);
            if (email != null) {
            	parameters.put("email", email);
            }
            parameters.put("subject", subject);
            parameters.put("text", text);
            parameters.put("bundle_identifier", Constants.APP_PACKAGE);
            parameters.put("bundle_version", Constants.APP_VERSION);

            urlConnection = new HttpURLConnectionBuilder(getURLString())
                    .setRequestMethod("POST")
                    .writeFormFields(parameters)
                    .build();

            urlConnection.connect();

            result.put("status", String.valueOf(urlConnection.getResponseCode()));
            result.put("response", getStringFromConnection(urlConnection));
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return result;
    }
	
    
    protected static String getStringFromConnection(HttpURLConnection connection) throws IOException {
        InputStream inputStream = new BufferedInputStream(connection.getInputStream());
        String jsonString = convertStreamToString(inputStream);
        inputStream.close();

        return jsonString;
    }

    private static String convertStreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 1024);
        StringBuilder stringBuilder = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
}
