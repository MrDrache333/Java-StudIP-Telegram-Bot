package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.config;

/**
 * The type Course config.
 */
public class CourseConfig {

    private String name;
    private String folderName;
    private boolean downloadFiles = true;
    private boolean sendNews = true;
    private TelegramConfig telegramConfig;

    /**
     * Instantiates a new Course config.
     */
    public CourseConfig() {
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
     * Gets folder name.
     *
     * @return the folder name
     */
    public String getFolderName() {
        return folderName;
    }

    /**
     * Sets folder name.
     *
     * @param folderName the folder name
     */
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    /**
     * Is download files boolean.
     *
     * @return the boolean
     */
    public boolean isDownloadFiles() {
        return downloadFiles;
    }

    /**
     * Sets download files.
     *
     * @param downloadFiles the download files
     */
    public void setDownloadFiles(boolean downloadFiles) {
        this.downloadFiles = downloadFiles;
    }

    /**
     * Is send news boolean.
     *
     * @return the boolean
     */
    public boolean isSendNews() {
        return sendNews;
    }

    /**
     * Sets send news.
     *
     * @param sendNews to send news
     */
    public void setSendNews(boolean sendNews) {
        this.sendNews = sendNews;
    }

    /**
     * Gets telegram config.
     *
     * @return the telegram config
     */
    public TelegramConfig getTelegramConfig() {
        return telegramConfig;
    }

    /**
     * Sets telegram config.
     *
     * @param telegramConfig the telegram config
     */
    public void setTelegramConfig(TelegramConfig telegramConfig) {
        this.telegramConfig = telegramConfig;
    }
}
