package studip;

public class NotLoggedInException extends AbstractBotException {


    public NotLoggedInException() {
        super("YOu have to login first");
    }
}
