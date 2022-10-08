package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api;

import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.LoginException;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.download.DownloadException;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.download.Downloader;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.download.StandardDownloader;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.request.AuthenticatedAPIRequest;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.request.RequestException;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.request.RequestResponse;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.types.Credentials;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.types.StudIPFile;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

/**
 * The type Rest.
 */
public class RestAPI {

    private Credentials credentials;
    private URL endpoint;

    private final Downloader downloader;

    /**
     * Instantiates a new Rest.
     *
     * @param endPoint    the end point
     * @param credentials the credentials
     */
    public RestAPI(URL endPoint, Credentials credentials) {
        if (!endPoint.getPath().endsWith("/")) {
            try {
                endPoint = new URL(endPoint.getProtocol() + "://" + endPoint.getHost() + endPoint.getPath() + "/");
            } catch (MalformedURLException ignored) {
            }
        }
        this.downloader = new StandardDownloader(credentials);
        this.endpoint = endPoint;
        this.credentials = credentials;
    }

    public RequestResponse fetchSettings() throws IOException, RequestException {
        RequestResponse response = new AuthenticatedAPIRequest(new URL(endpoint.toString() + "studip/settings"), credentials).getResponse();
        if (response.getResponseCode() != 200)
            throw new RequestException(response.getResponseCode(), response.getResponseMessage());
        return response;
    }

    /**
     * Login.
     *
     * @return the boolean
     * @throws IOException      the io exception
     * @throws RequestException the request exception
     * @throws LoginException   the api exception
     */
    public RequestResponse login() throws IOException, RequestException, LoginException {
        RequestResponse response = new AuthenticatedAPIRequest(new URL(endpoint.toString() + "user"), credentials).getResponse();
        if (response.getResponseCode() != 200)
            throw new LoginException(response.getResponseMessage());
        return response;
    }

    /**
     * Fetch user modules request response.
     *
     * @param user_id the user id
     * @return the request response
     * @throws IOException      the io exception
     * @throws RequestException the api exception
     */
    public RequestResponse fetchUserModules(String user_id) throws IOException, RequestException {
        RequestResponse response = new AuthenticatedAPIRequest(new URL(endpoint.toString() + "user/" + user_id + "/courses"), credentials).getResponse();
        if (response.getResponseCode() != 200) {
            throw new RequestException(response.getResponseCode(), response.getResponseMessage());
        } else {
            return handlePagination(response);
        }
    }

    /**
     * Fetch semesters request response.
     *
     * @return the request response
     * @throws IOException      the io exception
     * @throws RequestException the api exception
     */
    public RequestResponse fetchSemesters() throws IOException, RequestException {
        RequestResponse response = new AuthenticatedAPIRequest(new URL(endpoint.toString() + "semesters"), credentials).getResponse();
        if (response.getResponseCode() != 200) {
            throw new RequestException(response.getResponseCode(), response.getResponseMessage());
        } else {
            return handlePagination(response);
        }
    }

    /**
     * Fetch course news request response.
     *
     * @param course_id the course id
     * @return the request response
     * @throws IOException      the io exception
     * @throws RequestException the api exception
     */
    public RequestResponse fetchCourseNews(String course_id) throws IOException, RequestException {
        RequestResponse response = new AuthenticatedAPIRequest(new URL(endpoint.toString() + "course/" + course_id + "/news"), credentials).getResponse();
        if (response.getResponseCode() != 200) {
            throw new RequestException(response.getResponseCode(), response.getResponseMessage());
        } else {
            return handlePagination(response);
        }
    }

    /**
     * Fetch courses top folder request response.
     *
     * @param course_id the course id
     * @return the request response
     * @throws IOException      the io exception
     * @throws RequestException the request exception
     */
    public RequestResponse fetchCoursesTopFolder(String course_id) throws IOException, RequestException {
        RequestResponse response = new AuthenticatedAPIRequest(new URL(endpoint.toString() + "course/" + course_id + "/top_folder"), credentials).getResponse();
        if (response.getResponseCode() != 200) {
            throw new RequestException(response.getResponseCode(), response.getResponseMessage());
        } else {
            return response;
        }
    }

    /**
     * Fetch folder files request response.
     *
     * @param folder_id the folder id
     * @return the request response
     * @throws MalformedURLException the malformed url exception
     * @throws RequestException      the request exception
     */
    public RequestResponse fetchFolderFiles(String folder_id) throws MalformedURLException, RequestException {
        RequestResponse response = new AuthenticatedAPIRequest(new URL(endpoint.toString() + "folder/" + folder_id + "/files"), credentials).getResponse();
        if (response.getResponseCode() != 200) {
            throw new RequestException(response.getResponseCode(), response.getResponseMessage());
        } else {
            return handlePagination(response);
        }
    }

    /**
     * Fetch sub folders request response.
     *
     * @param folder_id the folder id
     * @return the request response
     * @throws MalformedURLException the malformed url exception
     * @throws RequestException      the request exception
     */
    public RequestResponse fetchSubFolders(String folder_id) throws MalformedURLException, RequestException {
        RequestResponse response = new AuthenticatedAPIRequest(new URL(endpoint.toString() + "folder/" + folder_id + "/subfolders"), credentials).getResponse();
        if (response.getResponseCode() != 200) {
            throw new RequestException(response.getResponseCode(), response.getResponseMessage());
        } else {
            return handlePagination(response);
        }
    }

    public void downloadFile(StudIPFile file, Path path) throws DownloadException, MalformedURLException {
        downloader.downloadFile(new URL(endpoint.toString() + "file/" + file.getId() + "/download"), file, path);
    }

    private RequestResponse handlePagination(RequestResponse response) throws MalformedURLException, RequestException {
        JSONObject currentJson = new JSONObject(response.getResponseMessage());
        int total = currentJson.getJSONObject("pagination").getInt("total");
        int siteLimit = currentJson.getJSONObject("pagination").getInt("limit");
        JSONObject collection;
        try {
            collection = currentJson.getJSONObject("collection");
        } catch (JSONException e) {
            return new RequestResponse(200, currentJson.toString());
        }
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


}

