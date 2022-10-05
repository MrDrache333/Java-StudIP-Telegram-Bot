package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.types;

import java.util.ArrayList;

/**
 * The type Kurs.
 */
public class Course {

    private String name;
    private String id;
    private String startSemester;
    private String endSemester;

    private ArrayList<News> news;

    private StudIPFolder rootStudIPFolder;


    /**
     * Instantiates a new Kurs.
     *
     * @param name the name
     */
    public Course(String name) {
        this.name = name;
    }

    //Getter und Setter

    /**
     * Gets root folder.
     *
     * @return the root folder
     */
    public StudIPFolder getRootFolder() {
        return rootStudIPFolder;
    }

    public void setRootFolder(StudIPFolder rootStudIPFolder) {
        this.rootStudIPFolder = rootStudIPFolder;
    }

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
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
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

}

