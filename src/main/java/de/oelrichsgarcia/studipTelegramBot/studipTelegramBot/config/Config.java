package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Config.
 */
public class Config {

    private String api_endpoint;
    private String api_username;
    private String api_password;
    private boolean use_google_drive;
    private String drive_root_folder;
    private String downloadFolder = "Files";
    private Long startTime = 0L;
    private Map<String, CourseConfig> courseConfigs = new HashMap<>();
    private TelegramConfig telegramConfig = new TelegramConfig();
    private List<String> blacklist = new ArrayList<>();

    /**
     * Instantiates a new Config.
     */
    public Config() {
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public Long getStartTime() {
        return startTime;
    }

    /**
     * Sets start time.
     *
     * @param startTime the start time
     */
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets api endpoint.
     *
     * @return the api endpoint
     */
    public String getApi_endpoint() {
        return api_endpoint;
    }

    /**
     * Sets api endpoint.
     *
     * @param api_endpoint the api endpoint
     */
    public void setApi_endpoint(String api_endpoint) {
        this.api_endpoint = api_endpoint;
    }

    /**
     * Gets api username.
     *
     * @return the api username
     */
    public String getApi_username() {
        return api_username;
    }

    /**
     * Sets api username.
     *
     * @param api_username the api username
     */
    public void setApi_username(String api_username) {
        this.api_username = api_username;
    }

    /**
     * Gets api drive root folder.
     *
     * @return the drive root folder
     */
    public String getDrive_root_folder() {
        return drive_root_folder;
    }

    /**
     * Sets drive root folder string
     *
     * @param drive_root_folder the folder id
     */
    public void setDrive_root_folder(String drive_root_folder) {
        this.drive_root_folder = drive_root_folder;
    }

    /**
     * Check if google drive shall be used
     *
     * @return true or false
     */
    public boolean isUse_google_drive() {
        return use_google_drive;
    }

    /**
     * Sets if google drive shall be used
     *
     * @param use_google_drive boolean
     */
    public void setUse_google_drive(boolean use_google_drive) {
        this.use_google_drive = use_google_drive;
    }

    /**
     * Gets api password.
     *
     * @return the api password
     */
    public String getApi_password() {
        return api_password;
    }

    /**
     * Sets api password.
     *
     * @param api_password the api password
     */
    public void setApi_password(String api_password) {
        this.api_password = api_password;
    }

    /**
     * Gets download folder.
     *
     * @return the download folder
     */
    public String getDownloadFolder() {
        return downloadFolder;
    }

    /**
     * Sets download folder.
     *
     * @param downloadFolder the download folder
     */
    public void setDownloadFolder(String downloadFolder) {
        this.downloadFolder = downloadFolder;
    }

    /**
     * Gets course configs.
     *
     * @return the course configs
     */
    public Map<String, CourseConfig> getCourseConfigs() {
        return courseConfigs;
    }

    /**
     * Sets course configs.
     *
     * @param courseConfigs the course configs
     */
    public void setCourseConfigs(Map<String, CourseConfig> courseConfigs) {
        this.courseConfigs = courseConfigs;
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

    /**
     * Gets black listed.
     *
     * @return the black listed
     */
    public List<String> getBlacklist() {
        return blacklist;
    }

    /**
     * Sets black listed.
     *
     * @param blacklist the black listed
     */
    public void setBlacklist(List<String> blacklist) {
        this.blacklist = blacklist;
    }
}
