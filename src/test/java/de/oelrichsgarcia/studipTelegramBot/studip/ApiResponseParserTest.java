package de.oelrichsgarcia.studipTelegramBot.studip;

import de.oelrichsgarcia.studipTelegramBot.studip.api.request.RequestResponse;
import de.oelrichsgarcia.studipTelegramBot.studip.api.types.Course;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * ApiResponseParser Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Sep 28, 2022</pre>
 */
public class ApiResponseParserTest {

    private static RequestResponse emptyResponse;
    //Course
    private static RequestResponse courseResponse;
    //Semester
    private static RequestResponse semesterResponse;
    //News
    private static RequestResponse newsResponse;

    @BeforeAll
    public static void before() throws Exception {
        emptyResponse = new RequestResponse(200, new JSONObject().put("pagination", new JSONObject()).put("collection", new JSONArray()).toString());
        courseResponse = new RequestResponse(200, emptyResponse.getResponseMessage());
        semesterResponse = new RequestResponse(200, emptyResponse.getResponseMessage());
        newsResponse = new RequestResponse(200, emptyResponse.getResponseMessage());
    }

    /**
     * Method: parseCourse(RequestResponse response)
     */
    @Test
    public void testParseCourse() throws Exception {
        ArrayList<Course> courses = ApiResponseParser.parseCourse(emptyResponse);
        assertEquals(courses, new ArrayList<>());

        courses = ApiResponseParser.parseCourse(courseResponse);
//TODO: Test goes here...

    }

    /**
     * Method: parseSemesters(RequestResponse response)
     */
    @Test
    public void testParseSemesters() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: parseNews(RequestResponse response)
     */
    @Test
    public void testParseNews() throws Exception {
//TODO: Test goes here... 
    }


} 
