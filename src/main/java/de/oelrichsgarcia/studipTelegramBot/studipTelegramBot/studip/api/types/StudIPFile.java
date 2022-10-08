package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.types;

import java.util.Date;

/**
 * The type Stud ip file.
 */
public class StudIPFile extends StudIPObject {

    private long fileSize = 0;

    /**
     * Instantiates a new Stud ip file.
     *
     * @param id     the id
     * @param parent the parent
     */
    public StudIPFile(String id, StudIPObject parent) {
        super(id, parent);
    }

    /**
     * Instantiates a new Stud ip file.
     *
     * @param id      the id
     * @param name    the name
     * @param created the created
     * @param updated the updated
     * @param parent  the parent
     */
    public StudIPFile(String id, String name, Date created, Date updated, StudIPObject parent) {
        super(id, name, created, updated, parent);
    }

    /**
     * Gets file size.
     *
     * @return the file size
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * Sets file size.
     *
     * @param fileSize the file size
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
