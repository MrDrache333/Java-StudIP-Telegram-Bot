package objects;

import java.net.URL;

/**
 * The type Stud ip file.
 */
public class StudIPFile {

    private String Name;
    private String Path;
    private String ID;
    private boolean isNew;
    private URL Link;


    /**
     * Instantiates a new Stud ip file.
     *
     * @param id   the id
     * @param name the name
     * @param link the link
     */
    public StudIPFile(String id, String name, URL link) {
        this.Name = name;
        this.ID = id;
        this.Link = link;
        this.isNew = false;
        this.Path = "";
    }

    /**
     * Gets link.
     *
     * @return the link
     */
    public URL getLink() {
        return Link;
    }

    /**
     * Sets link.
     *
     * @param link the link
     */
    public void setLink(URL link) {
        Link = link;
    }

    /**
     * Is new boolean.
     *
     * @return the boolean
     */
    public boolean isNew() {
        return isNew;
    }

    /**
     * Sets new.
     *
     * @param aNew the a new
     */
    public void setNew(boolean aNew) {
        isNew = aNew;
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
     * Gets Path.
     *
     * @return Value of Path.
     */
    public String getPath() {
        return Path;
    }

    /**
     * Sets new Path.
     *
     * @param Path New value of Path.
     */
    public void setPath(String Path) {
        this.Path = Path;
    }

    /**
     * Gets isNew.
     *
     * @return Value of isNew.
     */
    public boolean isIsNew() {
        return isNew;
    }

    /**
     * Sets new isNew.
     *
     * @param isNew New value of isNew.
     */
    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }
}