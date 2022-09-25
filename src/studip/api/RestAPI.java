package studip.api;

import studip.api.request.AuthenticatedAPIRequest;
import studip.api.request.RequestResponse;
import studip.api.types.StudIPFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * The type Rest.
 */
public class RestAPI {

    private Credentials credentials;
    private URL endpoint;

    /**
     * Instantiates a new Rest.
     */
    public RestAPI() {

    }

    /**
     * Instantiates a new Rest.
     *
     * @param endPoint    the end point
     * @param credentials the credentials
     */
    public RestAPI(URL endPoint, Credentials credentials) {
        this.endpoint = endPoint;
        this.credentials = credentials;
    }

    /**
     * Login.
     *
     * @return the boolean
     */
    public RequestResponse login() throws IOException {
        return new AuthenticatedAPIRequest(new URL(endpoint.toString() + "user"), credentials).getResponse();
    }

    /**
     * Gets endpoint.
     *
     * @return the endpoint
     */
    public URL getEndpoint() {
        return endpoint;
    }

    /**
     * Sets endpoint.
     *
     * @param endpoint the endpoint
     */
    public void setEndpoint(URL endpoint) {
        this.endpoint = endpoint;
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

    /**
     * Gets all files.
     *
     * @param id the id
     * @return the all files
     */
    public ArrayList<StudIPFile> getAllFiles(String id) {
        //TODO
        return null;
    }
}

