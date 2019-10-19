package objects;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * The type Stud ip data set.
 */
public class StudIP_DataSet {

    private HtmlPage Login;
    private HtmlPage Modules;
    private HtmlPage Profil;
    private HtmlPage MainPage;


    /**
     * Instantiates a new Stud ip data set.
     */
    public StudIP_DataSet() {

    }

    /**
     * Gets login.
     *
     * @return the login
     */
    public HtmlPage getLogin() {
        return Login;
    }

    /**
     * Sets login.
     *
     * @param login the login
     */
    public void setLogin(HtmlPage login) {
        Login = login;
    }

    /**
     * Gets modules.
     *
     * @return the modules
     */
    public HtmlPage getModules() {
        return Modules;
    }

    /**
     * Sets modules.
     *
     * @param modules the modules
     */
    public void setModules(HtmlPage modules) {
        Modules = modules;
    }

    /**
     * Gets profil.
     *
     * @return the profil
     */
    public HtmlPage getProfil() {
        return Profil;
    }

    /**
     * Sets profil.
     *
     * @param profil the profil
     */
    public void setProfil(HtmlPage profil) {
        Profil = profil;
    }

    /**
     * Gets main page.
     *
     * @return the main page
     */
    public HtmlPage getMainPage() {
        return MainPage;
    }

    /**
     * Sets main page.
     *
     * @param mainPage the main page
     */
    public void setMainPage(HtmlPage mainPage) {
        MainPage = mainPage;
    }
}
