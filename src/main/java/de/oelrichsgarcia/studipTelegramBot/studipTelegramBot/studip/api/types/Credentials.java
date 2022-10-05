package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.types;

/**
 * The type Credentials.
 */
public class Credentials {

    private String username;
    private String password;

    /**
     * Instantiates a new Credentials.
     */
    public Credentials() {

    }

    /**
     * Instantiates a new Credentials.
     *
     * @param username the username
     * @param password the password
     */
    public Credentials(String username, String password) {
        this.password = password;
        this.username = username;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
