package src.de.oelrichsgarcia.studipTelegramBot.studip.api.types;

import org.jsoup.Jsoup;

import java.util.Date;

/**
 * The type News.
 */
public class News {

    private String topic;
    private String id;
    private String html;

    private String author_id;

    private Date date;

    /**
     * Instantiates a new News.
     */
    public News() {

    }

    /**
     * Instantiates a new News.
     *
     * @param tilte the tilte
     * @param html  the html
     * @param id    the id
     */
    public News(String tilte, String html, String id) {
        this.topic = tilte;
        this.html = html;
        this.id = id;
    }

    /**
     * Gets author id.
     *
     * @return the author id
     */
    public String getAuthor_id() {
        return author_id;
    }

    /**
     * Sets author id.
     *
     * @param author_id the author id
     */
    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gets html.
     *
     * @return the html
     */
    public String getHtml() {
        return html;
    }

    /**
     * Sets html.
     *
     * @param html the html
     */
    public void setHtml(String html) {
        this.html = html;
    }

    /**
     * Gets Title.
     *
     * @return Value of Title.
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Sets new Title.
     *
     * @param Title New value of Title.
     */
    public void setTopic(String Title) {
        this.topic = Title;
    }

    /**
     * Gets Text.
     *
     * @return Value of Text.
     */
    public String getText() {
        return Jsoup.parse(html).text();
    }

    /**
     * Gets ID.
     *
     * @return Value of ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets new ID.
     *
     * @param id New value of ID.
     */
    public void setId(String id) {
        this.id = id;
    }
}
