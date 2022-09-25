package studip.api.types;

import java.net.URL;

/**
 * The type Uni.
 */
public class Uni {

    private URL api;
    private String Name;

    /**
     * Instantiates a new Uni.
     *
     * @param name the name
     * @param api  the api
     */
    public Uni(String name, URL api) {
        this.Name = name;
        this.api = api;
    }

    /**
     * Gets api.
     *
     * @return the api
     */
    public URL getApi() {
        return api;
    }

    /**
     * Sets api.
     *
     * @param api the api
     */
    public void setApi(URL api) {
        this.api = api;
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
}
