package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip;

import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.request.RequestResponse;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.types.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public static ArrayList<Course> parseCourses(RequestResponse response) {
        if (response == null || response.getResponseMessage() == null) return new ArrayList<>();
        JSONObject json = new JSONObject(response.getResponseMessage());

        ArrayList<Course> courses = new ArrayList<>();
        JSONObject collection;
        try {
            collection = json.getJSONObject("collection");
        } catch (JSONException e) {
            return new ArrayList<>();
        }
        collection.keySet().forEach(k -> courses.add(parseCourse(collection.getJSONObject(k))));
        return courses;
    }

    private static Course parseCourse(JSONObject courseJson) {
        Course course = new Course(courseJson.getString("title"));
        course.setId(courseJson.getString("course_id"));
        course.setStartSemester(courseJson.getString("start_semester").substring(courseJson.getString("start_semester").lastIndexOf("/") + 1));
        try {
            course.setEndSemester(courseJson.getString("end_semester").substring(courseJson.getString("end_semester").lastIndexOf("/") + 1));
        } catch (JSONException e) {
            course.setEndSemester("");
        }
        return course;
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
            semesters.add(parseSemester(semesterJson));
        });
        return semesters;
    }

    private static Semester parseSemester(JSONObject semesterJson) {
        Semester semester = new Semester();
        semester.setId(semesterJson.getString("id"));
        semester.setTitle(semesterJson.getString("title"));
        semester.setToken(semesterJson.getString("token"));
        semester.setBegin(new Date(semesterJson.getLong("begin") * 1000));
        semester.setEnd(new Date(semesterJson.getLong("end") * 1000));
        return semester;
    }

    /**
     * Parse news array list.
     *
     * @param response the response
     * @return the array list
     */
    public static ArrayList<News> parseAllNews(RequestResponse response) {
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
            news.add(parseNews(newsJson));
        });
        return news;
    }

    private static News parseNews(JSONObject newsJson) {
        News news = new News();
        news.setId(newsJson.getString("news_id"));
        news.setTopic(newsJson.getString("topic"));
        news.setHtml(newsJson.getString("body_html"));
        news.setDate(new Date(newsJson.getLong("date") * 1000));
        news.setAuthor_id(newsJson.getString("user_id"));
        return news;
    }

    /**
     * Parse top folder stud ip folder.
     *
     * @param response the response
     * @return the stud ip folder
     */
    public static StudIPFolder parseTopFolder(RequestResponse response) {
        if (response == null || response.getResponseMessage() == null) return null;
        JSONObject json = new JSONObject(response.getResponseMessage());

        if (!json.isEmpty()) {
            StudIPFolder studIPRootFolder = new StudIPFolder(json.getString("id"), null);
            studIPRootFolder.setName("Files");
            studIPRootFolder.setCreated(new Date(json.getLong("mkdate") * 1000));
            studIPRootFolder.setUpdated(new Date(json.getLong("chdate") * 1000));
            JSONArray jsonFiles = new JSONArray(json.getJSONArray("file_refs"));

            parseFiles(jsonFiles, studIPRootFolder).forEach(studIPRootFolder::addChild);
            return studIPRootFolder;
        }

        return null;
    }

    /**
     * Parse files array list.
     *
     * @param jsonFiles the json files
     * @param parent    the parent
     * @return the array list
     */
    public static ArrayList<StudIPFile> parseFiles(JSONArray jsonFiles, StudIPFolder parent) {
        ArrayList<StudIPFile> files = new ArrayList<>();
        if (!jsonFiles.isEmpty()) {
            jsonFiles.forEach(f -> files.add(parseFile((JSONObject) f, parent)));
        }
        return files;
    }

    /**
     * Parse folders array list.
     *
     * @param jsonFolders the json folders
     * @param parent      the parent
     * @return the array list
     */
    public static ArrayList<StudIPFolder> parseFolders(JSONArray jsonFolders, StudIPFolder parent) {
        ArrayList<StudIPFolder> folders = new ArrayList<>();
        if (!jsonFolders.isEmpty()) {
            jsonFolders.forEach(f -> folders.add(parseFolder((JSONObject) f, parent)));
        }
        return folders;
    }

    private static StudIPFolder parseFolder(JSONObject json, StudIPFolder parent) {
        StudIPFolder studIPFolder = new StudIPFolder();
        studIPFolder.setParent(parent);
        studIPFolder.setReadable(json.getBoolean("is_readable"));
        if (!studIPFolder.isReadable()) return studIPFolder;

        studIPFolder.setId(json.getString("id"));
        studIPFolder.setName(json.getString("name"));
        studIPFolder.setCreated(new Date(json.getLong("mkdate") * 1000));
        studIPFolder.setUpdated(new Date(json.getLong("chdate") * 1000));
        return studIPFolder;
    }

    private static StudIPFile parseFile(JSONObject json, StudIPFolder parent) {
        StudIPFile studIPFile = new StudIPFile();
        studIPFile.setParent(parent);
        studIPFile.setReadable(json.getBoolean("is_readable"));
        studIPFile.setDownloadable(json.getBoolean("is_downloadable"));
        if (!studIPFile.isReadable()) return studIPFile;
        studIPFile.setId(json.getString("id"));
        studIPFile.setName(json.getString("name"));
        studIPFile.setCreated(new Date(json.getLong("mkdate") * 1000));
        studIPFile.setUpdated(new Date(json.getLong("chdate") * 1000));
        studIPFile.setFileSize(json.getLong("size"));
        return studIPFile;
    }
}
