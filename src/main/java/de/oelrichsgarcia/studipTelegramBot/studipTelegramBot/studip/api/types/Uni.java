package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.types;

import java.net.URL;
import java.util.ArrayList;

/**
 * The type Uni.
 */
public class Uni {

    private URL api;
    private String name;

    private ArrayList<Semester> semesters;

    /**
     * Instantiates a new Uni.
     *
     * @param name the name
     * @param api  the api
     */
    public Uni(String name, URL api) {
        this.name = name;
        this.api = api;
    }

    /**
     * Instantiates a new Uni.
     *
     * @param api the api
     */
    public Uni(URL api) {
        this.api = api;
        this.name = "";
    }

    /**
     * Gets semesters.
     *
     * @return the semesters
     */
    public ArrayList<Semester> getSemesters() {
        return semesters;
    }

    /**
     * Sets semesters.
     *
     * @param semesters the semesters
     */
    public void setSemesters(ArrayList<Semester> semesters) {
        this.semesters = semesters;
    }

    /**
     * Gets api.
     *
     * @return the api
     */
    public URL getApi() {
        return api;
    }

    /**
     * Sets api.
     *
     * @param api the api
     */
    public void setApi(URL api) {
        this.api = api;
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
}
