package de.oelrichsgarcia.studipTelegramBot.studip.api.types;

import java.util.ArrayList;

/**
 * The type User.
 */
public class User {

    /*
        TODO Eingenschaften hinzufuegen:
             -  Profilaufrufe
             -  Rang
             -  Punkte
             -  Matrikelnummer
             -  Motto

     */

    private String Name;        //Name des Benutzers
    private String userName;
    private Credentials credentials;
    private ArrayList<Course> Kurse;

    private String userId;


    /**
     * Instantiates a new User.
     */
    public User() {
        this.Kurse = new ArrayList<>();
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets credentials.
     *
     * @return the credentials
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * Sets credentials.
     *
     * @param credentials the credentials
     */
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets user name.
     *
     * @param userName the user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
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
     * Gets kurse.
     *
     * @return the kurse
     */
    public ArrayList<Course> getKurse() {
        return Kurse;
    }

    /**
     * Sets kurse.
     *
     * @param kurse the kurse
     */
    public void setKurse(ArrayList<Course> kurse) {
        Kurse = kurse;
    }
}
