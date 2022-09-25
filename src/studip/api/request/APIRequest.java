package studip.api.request;

import java.io.IOException;

interface APIRequest {

    RequestResponse getResponse() throws IOException;
}
