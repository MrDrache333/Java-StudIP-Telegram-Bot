package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.types;

import java.util.Date;

/**
 * The type Object.
 */
public abstract class StudIPObject {

    private String id;
    private String name;
    private Date created;
    private Date updated;
    private StudIPObject parent;

    private boolean readable;

    /**
     * Instantiates a new Stud ip object.
     */
    public StudIPObject() {
    }

    /**
     * Instantiates a new Object.
     *
     * @param id     the id
     * @param parent the parent
     */
    public StudIPObject(String id, StudIPObject parent) {
        this.id = id;
        this.parent = parent;
    }

    /**
     * Instantiates a new Object.
     *
     * @param id       the id
     * @param name     the name
     * @param created  the created
     * @param updated  the updated
     * @param parent   the parent
     * @param readable the readable
     */
    public StudIPObject(String id, String name, Date created, Date updated, StudIPObject parent, Boolean readable) {
        this.id = id;
        this.name = name;
        this.created = created;
        this.updated = updated;
        this.parent = parent;
        this.readable = readable;
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
     * Gets created.
     *
     * @return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Sets created.
     *
     * @param created the created
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * Gets updated.
     *
     * @return the updated
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * Sets updated.
     *
     * @param updated the updated
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    /**
     * Gets parent.
     *
     * @return the parent
     */
    public StudIPObject getParent() {
        return parent;
    }

    /**
     * Sets parent.
     *
     * @param parent the parent
     */
    public void setParent(StudIPObject parent) {
        this.parent = parent;
    }

    /**
     * Is readable boolean.
     *
     * @return the boolean
     */
    public boolean isReadable() {
        return readable;
    }

    /**
     * Sets readable.
     *
     * @param readable the readable
     */
    public void setReadable(boolean readable) {
        this.readable = readable;
    }
}
