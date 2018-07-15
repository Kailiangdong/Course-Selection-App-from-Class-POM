package backend;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public class HTTPClient {

    private String ipAddress;

    public HTTPClient(String ipAddress) {
        this.ipAddress = ipAddress;
    }


    public enum HTTPRequestType {
        GET, POST
    }

    public HTTPAnswer httpRequest(HTTPRequestType httpRequestType, String payload) {

        try {

            CloseableHttpClient httpClient;
            CloseableHttpResponse httpResponse;

            if (httpRequestType == HTTPRequestType.GET) {
                HttpGet httpGet = new HttpGet(new URIBuilder()
                        .setScheme("http")
                        .setHost(ipAddress)
                        .setPort(1995)
                        .setPath("/")
                        .build());
                httpClient = HttpClients.createDefault();
                httpResponse = httpClient.execute(httpGet);
            } else {
                HttpPost httpPost = new HttpPost(new URIBuilder()
                        .setScheme("http")
                        .setHost(ipAddress)
                        .setPort(1995)
                        .setPath("/")
                        .build());
                httpPost.setEntity(new StringEntity(payload,
                        ContentType.create("text/plain", "utf-8")));
                httpClient = HttpClients.createDefault();
                httpResponse = httpClient.execute(httpPost);

            }

            int httpResponseStatusCode = httpResponse
                    .getStatusLine()
                    .getStatusCode();
            if (httpResponseStatusCode != 200) {

                return new HTTPAnswer(false,
                        "HTTP Response Status Code: " + httpResponseStatusCode);

            }

            InputStream httpResponseBodyStream = httpResponse
                    .getEntity()
                    .getContent();
            String httpResponseBody = IOUtils.toString(httpResponseBodyStream,
                    "utf-8");

            httpResponseBodyStream.close();
            httpResponse.close();
            httpClient.close();

            return new HTTPAnswer(true,
                    httpResponseBody);

        } catch (IOException | URISyntaxException e) {

            return new HTTPAnswer(false,
                    e.toString());

        }
    }

}

