import org.json.JSONObject;
import studip.api.RestAPI;
import studip.api.request.RequestResponse;
import studip.api.types.Kurs;
import studip.api.types.Uni;
import studip.api.types.User;

import java.io.IOException;
import java.util.ArrayList;

import static utils.Debugger.Sout;

/**
 * The type Stud ip bot.
 */
public class StudIPBot {

    private final Uni uni;
    private final User user;
    private final RestAPI restapi;
    private boolean loggedIn;

    /**
     * Instantiates a new Stud ip bot.
     *
     * @param uni  the uni
     * @param user the user
     */
    public StudIPBot(Uni uni, User user) {
        this.uni = uni;
        this.user = user;
        restapi = new RestAPI(uni.getApi(), user.getCredentials());
        this.loggedIn = false;
    }


    /**
     * Login.
     *
     * @throws IOException the io exception
     */
    public void login() throws IOException {
        System.out.println("INFO -> Trying to Login to " + uni.getName() + " with API-EndPoint at " + uni.getApi().toString());
        RequestResponse response = restapi.login();

        //Store Userinformations when login was successfull
        if (response.getResonseCode() == 200) {
            //Set Userinformations
            JSONObject json = new JSONObject(response.getResonesMessage());
            user.setUserName(json.getJSONObject("name").getString("username"));
            user.setName(json.getJSONObject("name").getString("formatted"));
            loggedIn = true;
            Sout("INFO -> Logged in successfully as " + user.getName());
        }
    }


    public ArrayList<Kurs> fetchCourses() throws IOException {
        System.out.println("INFO -> Trying to Fetch Courses");
        RequestResponse response = restapi.login();

        //Store Userinformations when login was successfull
        if (response.getResonseCode() == 200) {
            //Set Userinformations
            JSONObject json = new JSONObject(response.getResonesMessage());
            user.setUserName(json.getJSONObject("name").getString("username"));
            user.setName(json.getJSONObject("name").getString("formatted"));
            loggedIn = true;
            Sout("INFO -> Logged in successfully as " + user.getName());
        }
        return null;
    }

    /**
     * Is logged in boolean.
     *
     * @return the boolean
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }
}
