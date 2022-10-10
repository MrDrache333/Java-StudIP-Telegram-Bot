package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.config;

public class GoogleDriveConfig {

    private boolean enabled;
    private String rootDirectory;

    /**
     * Instantiates a new Google Drive config.
     */
    public GoogleDriveConfig() {
    }

    /**
     * Gets drive root folder.
     *
     * @return the drive root folder
     */
    public String getRootDirectory() {
        return rootDirectory;
    }

    /**
     * Sets drive root folder string
     *
     * @param rootDirectory the folder id
     */
    public void setRootDirectory(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    /**
     * Check if Google Drive shall be used
     *
     * @return true or false
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets if Google Drive shall be used
     *
     * @param enabled boolean
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
