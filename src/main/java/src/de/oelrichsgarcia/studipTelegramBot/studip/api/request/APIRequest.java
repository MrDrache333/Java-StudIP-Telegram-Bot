package de.oelrichsgarcia.studipTelegramBot.studip.api.request;

import java.io.IOException;

/**
 * The interface Api request.
 */
interface APIRequest {

    /**
     * Gets response.
     *
     * @return the response
     * @throws IOException  the io exception
     * @throws APIException the api exception
     */
    RequestResponse getResponse() throws IOException, APIException;
}