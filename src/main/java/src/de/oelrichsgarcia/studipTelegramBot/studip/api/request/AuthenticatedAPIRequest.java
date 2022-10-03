package src.de.oelrichsgarcia.studipTelegramBot.studip.api.request;

import org.apache.commons.codec.binary.Base64;
import src.de.oelrichsgarcia.studipTelegramBot.studip.api.types.Credentials;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * The type Authenticated api request.
 */
public class AuthenticatedAPIRequest implements APIRequest {

    private Credentials credentials;
    private URL endPoint;

    /**
     * Instantiates a new Authenticated api request.
     *
     * @param endPoint    the end point
     * @param credentials the credentials
     */
    public AuthenticatedAPIRequest(URL endPoint, Credentials credentials) {
        this.endPoint = endPoint;
        this.credentials = credentials;
    }


    @Override
    public RequestResponse getResponse() throws RequestException {
        try {
            HttpsURLConnection con = (HttpsURLConnection) endPoint.openConnection();
            con.setRequestMethod("GET");

            //Auth
            String auth = credentials.getUsername() + ":" + credentials.getPassword();
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
            String authHeaderValue = "Basic " + new String(encodedAuth);
            con.setRequestProperty("Authorization", authHeaderValue);

            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            return new RequestResponse(status, content.toString());
        } catch (IOException e) {
            throw new RequestException(1, e.getMessage());
        }
    }

    /**
     * Gets end point.
     *
     * @return the end point
     */
    public URL getEndPoint() {
        return endPoint;
    }

    /**
     * Sets end point.
     *
     * @param endPoint the end point
     */
    public void setEndPoint(URL endPoint) {
        this.endPoint = endPoint;
    }

    /**
     * Gets credentials.
     *
     * @return the credentials
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * Sets credentials.
     *
     * @param credentials the credentials
     */
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
