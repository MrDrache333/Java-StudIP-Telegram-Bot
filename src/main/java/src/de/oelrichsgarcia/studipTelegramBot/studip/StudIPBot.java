package de.oelrichsgarcia.studipTelegramBot.studip;

import de.oelrichsgarcia.studipTelegramBot.studip.api.RestAPI;
import de.oelrichsgarcia.studipTelegramBot.studip.api.request.RequestException;
import de.oelrichsgarcia.studipTelegramBot.studip.api.request.RequestResponse;
import de.oelrichsgarcia.studipTelegramBot.studip.api.types.Course;
import de.oelrichsgarcia.studipTelegramBot.studip.api.types.Semester;
import de.oelrichsgarcia.studipTelegramBot.studip.api.types.Uni;
import de.oelrichsgarcia.studipTelegramBot.studip.api.types.User;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Objects;

import static de.oelrichsgarcia.studipTelegramBot.utils.Debugger.Sout;

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
     * @throws IOException      the io exception
     * @throws RequestException the api exception
     * @throws LoginException   the login exception
     */
    public void login() throws IOException, RequestException, LoginException {
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


    /**
     * Fetch modules array list.
     *
     * @return the array list
     * @throws IOException          the io exception
     * @throws NotLoggedInException the not logged in exception
     * @throws RequestException     the api exception
     */
    public void fetchModules() throws IOException, NotLoggedInException, RequestException {
        System.out.println("INFO -> Trying to Fetch Courses");
        //Store Userinformations when login was successfull
        if (loggedIn) {
            RequestResponse response = restapi.fetchUserModules(user.getUserId());
            //Set Userinformations

            user.setKurse(ApiResponseParser.parseCourse(response));
        } else throw new NotLoggedInException();
    }

    /**
     * Gets current modules.
     *
     * @return the current modules
     * @throws RequestException     the api exception
     * @throws IOException          the io exception
     * @throws NotLoggedInException the not logged in exception
     */
    public ArrayList<Course> getCurrentModules() throws RequestException, IOException, NotLoggedInException {
        System.out.println("INFO -> Trying to filter current courses");
        if (!user.getKurse().isEmpty()) {
            if (uni.getSemesters() == null || uni.getSemesters().isEmpty()) {
                fetchSemesters();
            }
            Semester currentSemester = null;
            for (Semester semester : uni.getSemesters()) {
                if (semester.getBegin().getTime() < new Date().getTime() && semester.getEnd().getTime() > new Date().getTime()) {
                    currentSemester = semester;
                    break;
                }
            }
            if (currentSemester != null) {
                ArrayList<Course> courses = new ArrayList<>();
                user.getKurse().forEach(c -> {
                            try {
                                //If StartSemester of Course is this Date or before
                                if (uni.getSemesters().stream().filter(s -> Objects.equals(s.getId(), c.getStartSemester())).findFirst().get().getBegin().getTime() <= new Date().getTime()) {
                                    if (uni.getSemesters().stream().filter(s -> Objects.equals(s.getId(), c.getEndSemester())).findFirst().get().getEnd().getTime() >= new Date().getTime()) {
                                        courses.add(c);
                                    }
                                }
                            } catch (NullPointerException | NoSuchElementException ignored) {
                            }
                        }
                );
                return courses;
            } else return null;
        }
        return null;
    }

    private void fetchSemesters() throws RequestException, IOException, NotLoggedInException {
        if (loggedIn) {
            RequestResponse response = restapi.fetchSemesters();
            uni.setSemesters(ApiResponseParser.parseSemesters(response));
        } else throw new NotLoggedInException();
    }

    /**
     * Fetch news for course.
     *
     * @param course the course
     * @throws IOException          the io exception
     * @throws RequestException     the request exception
     * @throws NotLoggedInException the not logged in exception
     */
    public void fetchNewsForCourse(Course course) throws IOException, RequestException, NotLoggedInException {
        if (loggedIn) {
            RequestResponse response = restapi.fetchCourseNews(course.getID());
            course.setNews(ApiResponseParser.parseNews(response));
        } else throw new NotLoggedInException();
    }

    /**
     * Gets uni.
     *
     * @return the uni
     */
    public Uni getUni() {
        return uni;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
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
