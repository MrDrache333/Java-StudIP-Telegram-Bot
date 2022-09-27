package studip.api.types;

import java.net.URL;

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

    private boolean hasNewFiles;    //New Files in Course
    private boolean hasNewPosts;    //New Posts in Forum
    private boolean hasNewNews;     //Neue Ankuendigungen

    private URL Files;          //Link to Files
    private int fileCount;      //Anzahl der Dateien
    private URL Forum;          //Link to the Forum


    /**
     * Instantiates a new Kurs.
     *
     * @param name the name
     */
    public Course(String name) {
        this.Name = name;
    }


    /**
     * Has updates boolean.
     *
     * @return the boolean
     */
    public boolean hasUpdates() {
        return hasNewFiles || hasNewNews || hasNewPosts;
    }


    //Getter und Setter


    public String getStartSemester() {
        return startSemester;
    }

    public void setStartSemester(String startSemester) {
        this.startSemester = startSemester;
    }

    public String getEndSemester() {
        return endSemester;
    }

    public void setEndSemester(String endSemester) {
        this.endSemester = endSemester;
    }

    /**
     * Is has new news boolean.
     *
     * @return the boolean
     */
    public boolean isHasNewNews() {
        return hasNewNews;
    }

    /**
     * Sets has new news.
     *
     * @param hasNewNews the has new news
     */
    public void setHasNewNews(boolean hasNewNews) {
        this.hasNewNews = hasNewNews;
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

    /**
     * Is has new files boolean.
     *
     * @return the boolean
     */
    public boolean isHasNewFiles() {
        return hasNewFiles;
    }

    /**
     * Sets has new files.
     *
     * @param hasNewFiles the has new files
     */
    public void setHasNewFiles(boolean hasNewFiles) {
        this.hasNewFiles = hasNewFiles;
    }

    /**
     * Is has new posts boolean.
     *
     * @return the boolean
     */
    public boolean isHasNewPosts() {
        return hasNewPosts;
    }

    /**
     * Sets has new posts.
     *
     * @param hasNewPosts the has new posts
     */
    public void setHasNewPosts(boolean hasNewPosts) {
        this.hasNewPosts = hasNewPosts;
    }

    /**
     * Gets files.
     *
     * @return the files
     */
    public URL getFiles() {
        return Files;
    }

    /**
     * Sets files.
     *
     * @param files the files
     */
    public void setFiles(URL files) {
        Files = files;
    }

    /**
     * Gets forum.
     *
     * @return the forum
     */
    public URL getForum() {
        return Forum;
    }

    /**
     * Sets forum.
     *
     * @param forum the forum
     */
    public void setForum(URL forum) {
        Forum = forum;
    }

    /**
     * Gets file count.
     *
     * @return the file count
     */
    public int getFileCount() {
        return fileCount;
    }

    /**
     * Sets file count.
     *
     * @param fileCount the file count
     */
    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }
}

