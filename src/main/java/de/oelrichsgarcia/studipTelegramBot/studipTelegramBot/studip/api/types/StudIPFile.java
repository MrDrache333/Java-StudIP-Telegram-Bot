package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.types;

/**
 * The type Stud ip file.
 */
public class StudIPFile extends StudIPObject {

    private long fileSize = 0;

    private boolean downloadable;

    /**
     * Instantiates a new Stud ip file.
     */
    public StudIPFile() {
        super();
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

    /**
     * Is downloadable boolean.
     *
     * @return the boolean
     */
    public boolean isDownloadable() {
        return downloadable;
    }

    /**
     * Sets downloadable.
     *
     * @param downloadable the downloadable
     */
    public void setDownloadable(boolean downloadable) {
        this.downloadable = downloadable;
    }
}
