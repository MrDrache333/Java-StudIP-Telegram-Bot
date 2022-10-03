package src.de.oelrichsgarcia.studipTelegramBot.studip.api.types;

import java.util.ArrayList;

/**
 * The type Kurs.
 */
public class Course {

    private String Name;        //Name des Kurses
    private String ID;          //ID in Stud.IP
    private String shortID;     //Kennung des Kurses z.B. mat955
    private String StudIpNr;    //Nummer in StudIP z.B. 2.0.18a
    private String startSemester;
    private String endSemester;

    private ArrayList<News> news;


    /**
     * Instantiates a new Kurs.
     *
     * @param name the name
     */
    public Course(String name) {
        this.Name = name;
    }

    //Getter und Setter


    /**
     * Gets news.
     *
     * @return the news
     */
    public ArrayList<News> getNews() {
        return news;
    }

    /**
     * Sets news.
     *
     * @param news the news
     */
    public void setNews(ArrayList<News> news) {
        this.news = news;
    }

    /**
     * Gets start semester.
     *
     * @return the start semester
     */
    public String getStartSemester() {
        return startSemester;
    }

    /**
     * Sets start semester.
     *
     * @param startSemester the start semester
     */
    public void setStartSemester(String startSemester) {
        this.startSemester = startSemester;
    }

    /**
     * Gets end semester.
     *
     * @return the end semester
     */
    public String getEndSemester() {
        return endSemester;
    }

    /**
     * Sets end semester.
     *
     * @param endSemester the end semester
     */
    public void setEndSemester(String endSemester) {
        this.endSemester = endSemester;
    }

    /**
     * Gets stud ip nr.
     *
     * @return the stud ip nr
     */
    public String getStudIpNr() {
        return StudIpNr;
    }

    /**
     * Sets stud ip nr.
     *
     * @param studIpNr the stud ip nr
     */
    public void setStudIpNr(String studIpNr) {
        StudIpNr = studIpNr;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return Name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        Name = name;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getID() {
        return ID;
    }

    /**
     * Sets id.
     *
     * @param ID the id
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * Gets short id.
     *
     * @return the short id
     */
    public String getShortID() {
        return shortID;
    }

    /**
     * Sets short id.
     *
     * @param shortID the short id
     */
    public void setShortID(String shortID) {
        this.shortID = shortID;
    }
}

