package objects;

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
    private String Username;    //Anmeldename in Stud.IP
    private String Password;    //Passwort fuer Stud.IP
    private String profilePictureLink;  //Link zum Profilbild

    private int profileViews;       //Profilaufrufe
    private String studipRang;      //Rangname in StudIP
    private long profilePoints;     //Forumspunkte in STUDIP
    private int matrikelnummer;     //Matrikelnummer
    private String motto;           //Aktuelles Motto

    private StudIP_DataSet StudIP_Data;

    private ArrayList<Kurs> Kurse;


    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param password the password
     */
    public User(String username, String password) {
        this.Password = password;
        this.Username = username;
        this.StudIP_Data = new StudIP_DataSet();
        this.Kurse = new ArrayList<>();
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
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return Username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        Username = username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return Password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        Password = password;
    }

    /**
     * Gets stud ip data.
     *
     * @return the stud ip data
     */
    public StudIP_DataSet getStudIP_Data() {
        return StudIP_Data;
    }

    /**
     * Sets stud ip data.
     *
     * @param studIP_Data the stud ip data
     */
    public void setStudIP_Data(StudIP_DataSet studIP_Data) {
        StudIP_Data = studIP_Data;
    }

    /**
     * Gets profile picture link.
     *
     * @return the profile picture link
     */
    public String getProfilePictureLink() {
        return profilePictureLink;
    }

    /**
     * Sets profile picture link.
     *
     * @param profilePictureLink the profile picture link
     */
    public void setProfilePictureLink(String profilePictureLink) {
        this.profilePictureLink = profilePictureLink;
    }

    /**
     * Gets kurse.
     *
     * @return the kurse
     */
    public ArrayList<Kurs> getKurse() {
        return Kurse;
    }

    /**
     * Sets kurse.
     *
     * @param kurse the kurse
     */
    public void setKurse(ArrayList<Kurs> kurse) {
        Kurse = kurse;
    }
}
