package src.de.oelrichsgarcia.studipTelegramBot.studip;

import org.json.JSONException;
import org.json.JSONObject;
import src.de.oelrichsgarcia.studipTelegramBot.studip.api.request.RequestResponse;
import src.de.oelrichsgarcia.studipTelegramBot.studip.api.types.Course;
import src.de.oelrichsgarcia.studipTelegramBot.studip.api.types.News;
import src.de.oelrichsgarcia.studipTelegramBot.studip.api.types.Semester;

import java.util.ArrayList;
import java.util.Date;

/**
 * The type Api response parser.
 */
public class ApiResponseParser {

    /**
     * Parse course array list.
     *
     * @param response the response
     * @return the array list
     */
    public static ArrayList<Course> parseCourse(RequestResponse response) {
        if (response == null || response.getResponseMessage() == null) return new ArrayList<>();
        JSONObject json = new JSONObject(response.getResponseMessage());

        ArrayList<Course> courses = new ArrayList<>();
        JSONObject collection;
        try {
            collection = json.getJSONObject("collection");
        } catch (JSONException e) {
            return new ArrayList<>();
        }
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

    /**
     * Parse semesters array list.
     *
     * @param response the response
     * @return the array list
     */
    static ArrayList<Semester> parseSemesters(RequestResponse response) {
        if (response == null || response.getResponseMessage() == null) return new ArrayList<>();
        JSONObject json = new JSONObject(response.getResponseMessage());
        ArrayList<Semester> semesters = new ArrayList<>();
        JSONObject collection;
        try {
            collection = json.getJSONObject("collection");
        } catch (JSONException e) {
            return new ArrayList<>();
        }
        collection.keySet().forEach(s -> {
            JSONObject semesterJson = collection.getJSONObject(s);
            Semester semester = new Semester();
            semester.setId(semesterJson.getString("id"));
            semester.setTitle(semesterJson.getString("title"));
            semester.setToken(semesterJson.getString("token"));
            semester.setBegin(new Date(semesterJson.getLong("begin") * 1000));
            semester.setEnd(new Date(semesterJson.getLong("end") * 1000));

            semesters.add(semester);
        });
        return semesters;
    }

    /**
     * Parse news array list.
     *
     * @param response the response
     * @return the array list
     */
    public static ArrayList<News> parseNews(RequestResponse response) {
        if (response == null || response.getResponseMessage() == null) return new ArrayList<>();
        JSONObject json = new JSONObject(response.getResponseMessage());
        ArrayList<News> news = new ArrayList<>();
        JSONObject collection;
        try {
            collection = json.getJSONObject("collection");
        } catch (JSONException e) {
            return new ArrayList<>();
        }
        collection.keySet().forEach(c -> {
            JSONObject newsJson = collection.getJSONObject(c);
            News news1 = new News();
            news1.setId(newsJson.getString("news_id"));
            news1.setTopic(newsJson.getString("topic"));
            news1.setHtml(newsJson.getString("body_html"));
            news1.setDate(new Date(newsJson.getLong("date") * 1000));
            news1.setAuthor_id(newsJson.getString("user_id"));
            news.add(news1);
        });
        return news;
    }
}
