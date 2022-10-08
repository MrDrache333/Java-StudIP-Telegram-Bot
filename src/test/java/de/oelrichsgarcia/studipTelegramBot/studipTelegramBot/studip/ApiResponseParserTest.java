package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip;

import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.request.RequestResponse;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.types.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseParserTest {

    @Test
    void parseCourses() {
        JSONObject course1 = new JSONObject();
        course1.put("title", "test");
        course1.put("course_id", "1234");
        course1.put("start_semester", "/api.php/123");
        course1.put("end_semester", "/api.php/124");

        JSONObject course2 = new JSONObject();
        course2.put("title", "test2");
        course2.put("course_id", "12345");
        course2.put("start_semester", "/api.php/1234");
        course2.put("end_semester", "/api.php/1235");

        JSONObject collection = new JSONObject();
        collection.put(course1.get("course_id").toString(), course1);
        collection.put(course2.get("course_id").toString(), course2);

        JSONObject response = new JSONObject();
        response.put("collection", collection);

        ArrayList<Course> parsedCourses = ApiResponseParser.parseCourses(new RequestResponse(200, response.toString()));
        assertEquals(2, parsedCourses.size());
        //Course 1
        assertEquals("test", parsedCourses.get(0).getName());
        assertEquals("1234", parsedCourses.get(0).getId());
        assertEquals("123", parsedCourses.get(0).getStartSemester());
        assertEquals("124", parsedCourses.get(0).getEndSemester());
        //Course 2
        assertEquals("test2", parsedCourses.get(1).getName());
        assertEquals("12345", parsedCourses.get(1).getId());
        assertEquals("1234", parsedCourses.get(1).getStartSemester());
        assertEquals("1235", parsedCourses.get(1).getEndSemester());

        //Empty Response
        response = new JSONObject();
        response.put("collection", new JSONArray());

        parsedCourses = ApiResponseParser.parseCourses(new RequestResponse(200, response.toString()));
        assertEquals(0, parsedCourses.size());

    }

    @Test
    void parseSemesters() {
        JSONObject semester1 = new JSONObject();
        semester1.put("title", "test");
        semester1.put("id", "1234");
        semester1.put("token", "12345");
        semester1.put("begin", 123);
        semester1.put("end", 124);

        JSONObject semester2 = new JSONObject();
        semester2.put("title", "test2");
        semester2.put("id", "1235");
        semester2.put("token", "12346");
        semester2.put("begin", 124);
        semester2.put("end", 125);

        JSONObject collection = new JSONObject();
        collection.put(semester1.get("id").toString(), semester1);
        collection.put(semester2.get("id").toString(), semester2);

        JSONObject response = new JSONObject();
        response.put("collection", collection);

        ArrayList<Semester> parseSemesters = ApiResponseParser.parseSemesters(new RequestResponse(200, response.toString()));
        assertEquals(2, parseSemesters.size());
        //Semester 1
        assertEquals("test", parseSemesters.get(1).getTitle());
        assertEquals("1234", parseSemesters.get(1).getId());
        assertEquals("12345", parseSemesters.get(1).getToken());
        assertEquals(new Date(123000), parseSemesters.get(1).getBegin());
        assertEquals(new Date(124000), parseSemesters.get(1).getEnd());
        //Semester 2
        assertEquals("test2", parseSemesters.get(0).getTitle());
        assertEquals("1235", parseSemesters.get(0).getId());
        assertEquals("12346", parseSemesters.get(0).getToken());
        assertEquals(new Date(124000), parseSemesters.get(0).getBegin());
        assertEquals(new Date(125000), parseSemesters.get(0).getEnd());

        //Empty Response
        response = new JSONObject();
        response.put("collection", new JSONArray());

        parseSemesters = ApiResponseParser.parseSemesters(new RequestResponse(200, response.toString()));
        assertEquals(0, parseSemesters.size());
    }

    @Test
    void parseNews() {
        JSONObject news1 = new JSONObject();
        news1.put("topic", "test");
        news1.put("news_id", "1234");
        news1.put("body_html", "12345");
        news1.put("date", 123);
        news1.put("user_id", "124");

        JSONObject news2 = new JSONObject();
        news2.put("topic", "test2");
        news2.put("news_id", "1235");
        news2.put("body_html", "12346");
        news2.put("date", 124);
        news2.put("user_id", "125");

        JSONObject collection = new JSONObject();
        collection.put(news1.get("news_id").toString(), news1);
        collection.put(news2.get("news_id").toString(), news2);

        JSONObject response = new JSONObject();
        response.put("collection", collection);

        ArrayList<News> allNews = ApiResponseParser.parseAllNews(new RequestResponse(200, response.toString()));
        assertEquals(2, allNews.size());
        //Semester 1
        assertEquals("test", allNews.get(1).getTopic());
        assertEquals("1234", allNews.get(1).getId());
        assertEquals("12345", allNews.get(1).getText());
        assertEquals(new Date(123000), allNews.get(1).getDate());
        assertEquals("124", allNews.get(1).getAuthor_id());
        //Semester 2
        assertEquals("test2", allNews.get(0).getTopic());
        assertEquals("1235", allNews.get(0).getId());
        assertEquals("12346", allNews.get(0).getText());
        assertEquals(new Date(124000), allNews.get(0).getDate());
        assertEquals("125", allNews.get(0).getAuthor_id());

        //Empty Response
        response = new JSONObject();
        response.put("collection", new JSONArray());

        allNews = ApiResponseParser.parseAllNews(new RequestResponse(200, response.toString()));
        assertEquals(0, allNews.size());
    }

    @Test
    void parseTopFolder() {

        JSONObject jsonFolder = new JSONObject();
        jsonFolder.put("id", "123");
        jsonFolder.put("mkdate", 123);
        jsonFolder.put("chdate", 124);
        jsonFolder.put("file_refs", new JSONArray());

        StudIPFolder parsedFolder = ApiResponseParser.parseTopFolder(new RequestResponse(200, jsonFolder.toString()));
        assertEquals("123", parsedFolder.getId());
        assertEquals("Files", parsedFolder.getName());
        assertEquals(new Date(123000), parsedFolder.getCreated());
        assertEquals(new Date(124000), parsedFolder.getUpdated());
        assertEquals(0, parsedFolder.getChilds().size());


        assertNull(ApiResponseParser.parseTopFolder(null));
        assertNull(ApiResponseParser.parseTopFolder(new RequestResponse(200, "{}")));
    }

    @Test
    void parseFiles() {

        JSONObject file1 = new JSONObject();
        file1.put("id", "123");
        file1.put("name", "test");
        file1.put("mkdate", 123);
        file1.put("chdate", 124);
        file1.put("size", 125);
        file1.put("is_readable", "true");
        file1.put("is_downloadable", "false");

        JSONObject file2 = new JSONObject();
        file2.put("id", "124");
        file2.put("name", "test2");
        file2.put("mkdate", 124);
        file2.put("chdate", 125);
        file2.put("size", 126);
        file2.put("is_readable", "true");
        file2.put("is_downloadable", "true");

        JSONObject file3 = new JSONObject();
        file3.put("is_readable", "false");
        file3.put("is_downloadable", "false");

        JSONArray files = new JSONArray();
        files.put(file1);
        files.put(file2);
        files.put(file3);

        ArrayList<StudIPFile> parsedFiles = ApiResponseParser.parseFiles(files, new StudIPFolder());
        assertEquals("123", parsedFiles.get(0).getId());
        assertEquals("test", parsedFiles.get(0).getName());
        assertEquals(new Date(123000), parsedFiles.get(0).getCreated());
        assertEquals(new Date(124000), parsedFiles.get(0).getUpdated());
        assertEquals(125, parsedFiles.get(0).getFileSize());
        assertTrue(parsedFiles.get(0).isReadable());
        assertFalse(parsedFiles.get(0).isDownloadable());

        assertEquals("124", parsedFiles.get(1).getId());
        assertEquals("test2", parsedFiles.get(1).getName());
        assertEquals(new Date(124000), parsedFiles.get(1).getCreated());
        assertEquals(new Date(125000), parsedFiles.get(1).getUpdated());
        assertEquals(126, parsedFiles.get(1).getFileSize());
        assertTrue(parsedFiles.get(1).isReadable());
        assertTrue(parsedFiles.get(1).isDownloadable());

        assertFalse(parsedFiles.get(2).isReadable());
        assertFalse(parsedFiles.get(2).isDownloadable());

    }

    @Test
    void parseFolders() {
        JSONObject folder1 = new JSONObject();
        folder1.put("id", "123");
        folder1.put("name", "test");
        folder1.put("mkdate", 123);
        folder1.put("chdate", 124);
        folder1.put("is_readable", "true");

        JSONObject folder2 = new JSONObject();
        folder2.put("id", "124");
        folder2.put("name", "test2");
        folder2.put("mkdate", 124);
        folder2.put("chdate", 125);
        folder2.put("is_readable", "true");

        JSONObject folder3 = new JSONObject();
        folder3.put("is_readable", "false");

        JSONArray folders = new JSONArray();
        folders.put(folder1);
        folders.put(folder2);
        folders.put(folder3);

        ArrayList<StudIPFolder> parsedFolders = ApiResponseParser.parseFolders(folders, new StudIPFolder());
        assertEquals("123", parsedFolders.get(0).getId());
        assertEquals("test", parsedFolders.get(0).getName());
        assertEquals(new Date(123000), parsedFolders.get(0).getCreated());
        assertEquals(new Date(124000), parsedFolders.get(0).getUpdated());
        assertTrue(parsedFolders.get(0).isReadable());

        assertEquals("124", parsedFolders.get(1).getId());
        assertEquals("test2", parsedFolders.get(1).getName());
        assertEquals(new Date(124000), parsedFolders.get(1).getCreated());
        assertEquals(new Date(125000), parsedFolders.get(1).getUpdated());
        assertTrue(parsedFolders.get(1).isReadable());

        assertFalse(parsedFolders.get(2).isReadable());
    }
}