package studip.api;

import org.json.JSONObject;
import studip.api.request.*;
import studip.api.types.Credentials;
import studip.api.types.StudIPFile;

import java.io.IOException;
import java.net.MalformedURLException;
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
    public RequestResponse login() throws IOException, APIException {
        RequestResponse response = new AuthenticatedAPIRequest(new URL(endpoint.toString() + "user"), credentials).getResponse();
        if (response.getResponseCode() != 200)
            throw new LoginException(response.getResponseCode(), response.getResponseMessage());
        return response;
    }

    public RequestResponse fetchUserModules(String user_id) throws IOException, APIException {
        RequestResponse response = new AuthenticatedAPIRequest(new URL(endpoint.toString() + "user/" + user_id + "/courses"), credentials).getResponse();
        if (response.getResponseCode() != 200) {
            throw new RequestException(response.getResponseCode(), response.getResponseMessage());
        } else {
            return handlePagination(response);
        }
    }

    public RequestResponse fetchSemesters() throws IOException, APIException {
        RequestResponse response = new AuthenticatedAPIRequest(new URL(endpoint.toString() + "semesters"), credentials).getResponse();
        if (response.getResponseCode() != 200) {
            throw new RequestException(response.getResponseCode(), response.getResponseMessage());
        } else {
            return handlePagination(response);
        }
    }

    public RequestResponse fetchCourseNews(String course_id) throws IOException, APIException {
        RequestResponse response = new AuthenticatedAPIRequest(new URL(endpoint.toString() + "course/" + course_id + "/news"), credentials).getResponse();
        if (response.getResponseCode() != 200) {
            throw new RequestException(response.getResponseCode(), response.getResponseMessage());
        } else {
            return handlePagination(response);
        }
    }

    private RequestResponse handlePagination(RequestResponse response) throws MalformedURLException, APIException {
        JSONObject currentJson = new JSONObject(response.getResponseMessage());
        int total = currentJson.getJSONObject("pagination").getInt("total");
        int siteLimit = currentJson.getJSONObject("pagination").getInt("limit");
        JSONObject collection = currentJson.getJSONObject("collection");
        for (int i = 1; total > siteLimit * i; i++) {

            RequestResponse nextResponse = new AuthenticatedAPIRequest(new URL(endpoint.getProtocol() + "://" + endpoint.getHost() + currentJson.getJSONObject("pagination").getJSONObject("links").getString("next")), credentials).getResponse();
            if (response.getResponseCode() != 200) {
                throw new RequestException(response.getResponseCode(), response.getResponseMessage());
            }
            JSONObject nextJson = new JSONObject(nextResponse.getResponseMessage());
            JSONObject nextCollection = nextJson.getJSONObject("collection");
            nextCollection.keySet().forEach(k -> collection.put(k, nextCollection.get(k)));
            currentJson = nextJson;
        }

        currentJson.put("collection", collection);
        return new RequestResponse(200, currentJson.toString());
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

