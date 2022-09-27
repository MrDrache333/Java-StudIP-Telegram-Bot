import org.json.JSONException;
import org.json.JSONObject;
import studip.api.RestAPI;
import studip.api.request.APIException;
import studip.api.request.RequestResponse;
import studip.api.types.Course;
import studip.api.types.Semester;
import studip.api.types.Uni;
import studip.api.types.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

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
    public void login() throws IOException, APIException {
        System.out.println("INFO -> Trying to Login to " + uni.getName() + " with API-EndPoint at " + uni.getApi().toString());
        RequestResponse response = restapi.login();

        //Store Userinformations when login was successfull
        if (response.getResponseCode() == 200) {
            //Set Userinformations
            JSONObject json = new JSONObject(response.getResponseMessage());
            user.setUserName(json.getJSONObject("name").getString("username"));
            user.setName(json.getJSONObject("name").getString("formatted"));
            user.setUserId(json.getString("user_id"));
            loggedIn = true;
            Sout("INFO -> Logged in successfully as " + user.getName());
        }
    }


    public ArrayList<Course> fetchModules() throws IOException, APIException {
        System.out.println("INFO -> Trying to Fetch Courses");
        //Store Userinformations when login was successfull
        if (loggedIn) {
            RequestResponse response = restapi.fetchUserModules(user.getUserId());
            //Set Userinformations
            JSONObject json = new JSONObject(response.getResponseMessage());

            ArrayList<Course> courses = new ArrayList<>();
            JSONObject collection = json.getJSONObject("collection");
            collection.keySet().forEach(k -> {
                JSONObject courseJson = collection.getJSONObject(k);
                Course course = new Course(courseJson.getString("title"));
                course.setID(courseJson.getString("course_id"));
                course.setStartSemester(courseJson.getString("start_semester").substring(courseJson.getString("start_semester").lastIndexOf("/") + 1));
                try {
                    course.setEndSemester(courseJson.getString("end_semester").substring(courseJson.getString("end_semester").lastIndexOf("/") + 1));
                } catch (JSONException e) {
                    course.setEndSemester("");
                }
                courses.add(course);
            });
            return courses;
        }
        return null;
    }

    public ArrayList<Course> getCurrentModules() throws APIException, IOException {
        System.out.println("INFO -> Trying to filter current courses");
        if (!user.getKurse().isEmpty()) {
            if (uni.getSemesters().isEmpty()) {
                uni.setSemesters(fetchSemesters());
            }
            Semester currentSemester = null;
            for (Semester semester : uni.getSemesters()) {
                if (semester.getBegin().getTime() < new Date().getTime() && semester.getEnd().getTime() > new Date().getTime()) {
                    currentSemester = semester;
                    break;
                }
            }
            if (currentSemester != null) {

            }

        }
        return null;
    }

    private ArrayList<Semester> fetchSemesters() throws APIException, IOException {
        if (loggedIn) {
            RequestResponse response = restapi.fetchSemesters();

            JSONObject json = new JSONObject(response.getResponseMessage());
            ArrayList<Semester> semesters = new ArrayList<>();
            JSONObject collection = json.getJSONObject("collection");
            collection.keySet().forEach(s -> {
                JSONObject semesterJson = collection.getJSONObject(s);
                Semester semester = new Semester();
                semester.setId(semesterJson.getString("id"));
                semester.setTitle(semesterJson.getString("title"));
                semester.setToken(semesterJson.getString("token"));
                semester.setBegin(new Date(semesterJson.getLong("begin")));
                semester.setEnd(new Date(semesterJson.getLong("end")));

                semesters.add(semester);
            });
            return semesters;
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
