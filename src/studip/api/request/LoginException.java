package studip.api.request;

public class LoginException extends APIException {

    public LoginException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }
}
