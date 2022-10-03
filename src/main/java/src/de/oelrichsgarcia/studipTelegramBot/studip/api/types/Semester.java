package src.de.oelrichsgarcia.studipTelegramBot.studip.api.types;

import java.util.Date;

/**
 * The type Semester.
 */
public class Semester {

    private String id;

    private String title;
    private String token;
    private Date begin;
    private Date end;

    /**
     * Instantiates a new Semester.
     */
    public Semester() {

    }

    /**
     * Instantiates a new Semester.
     *
     * @param id    the id
     * @param title the title
     * @param token the token
     * @param begin the begin
     * @param end   the end
     */
    public Semester(String id, String title, String token, long begin, long end) {
        this.id = id;
        this.title = title;
        this.token = token;
        this.begin = new Date(begin);
        this.end = new Date(end);
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets token.
     *
     * @param token the token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets begin.
     *
     * @return the begin
     */
    public Date getBegin() {
        return begin;
    }

    /**
     * Sets begin.
     *
     * @param begin the begin
     */
    public void setBegin(Date begin) {
        this.begin = begin;
    }

    /**
     * Gets end.
     *
     * @return the end
     */
    public Date getEnd() {
        return end;
    }

    /**
     * Sets end.
     *
     * @param end the end
     */
    public void setEnd(Date end) {
        this.end = end;
    }
}
