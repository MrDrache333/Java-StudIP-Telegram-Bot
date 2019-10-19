package objects;

import java.net.URL;

/**
 * The type Uni.
 */
public class Uni {

    private URL HomePage;       //Link zur Offiziellen HomePage
    private URL LoginPage;      //Link zum Login
    private URL ForgotPage;     //Link zur Passwort vergessen Seite
    private URL CoursesPage;    //Link zu den Kursen
    private URL ProfilePage;    //Link zu dem Studenten Profil
    private URL FilesPage;      //Link zu den Dateien
    private URL FilesDetailsPage;   //Link zu den Details einer Datei
    private URL FilesDownloadLink;  //Link zum herunterladen von Dateien
    private URL AllFilesDownloadLink;   //Link zum herunterladen aller neuen Dateien
    private String Name;        //Name
    private String TownName;    //Stadt
    private String Type;        //Universitaet oder Fachhochschule


    /**
     * Instantiates a new Uni.
     *
     * @param name                 the name
     * @param townName             the town name
     * @param type                 the type
     * @param homePage             the home page
     * @param loginPage            the login page
     * @param forgotPage           the forgot page
     * @param profilePage          the profile page
     * @param coursesPage          the courses page
     * @param filesPage            the files page
     * @param fileDetailsPage      the file details page
     * @param filesDownloadLink    the files download link
     * @param allFilesDownloadLink the all files download link
     */
    public Uni(String name, String townName, String type, URL homePage, URL loginPage, URL forgotPage, URL profilePage, URL coursesPage, URL filesPage, URL fileDetailsPage, URL filesDownloadLink, URL allFilesDownloadLink) {
        this.Name = name;
        this.TownName = townName;
        this.HomePage = homePage;
        this.ProfilePage = profilePage;
        this.CoursesPage = coursesPage;
        this.LoginPage = loginPage;
        this.ForgotPage = forgotPage;
        this.FilesPage = filesPage;
        this.FilesDetailsPage = fileDetailsPage;
        this.Type = type;
        this.FilesDownloadLink = filesDownloadLink;
        this.AllFilesDownloadLink = allFilesDownloadLink;
    }

    /**
     * Gets all files download link.
     *
     * @return the all files download link
     */
    public URL getAllFilesDownloadLink() {
        return AllFilesDownloadLink;
    }

    /**
     * Sets all files download link.
     *
     * @param allFilesDownloadLink the all files download link
     */
    public void setAllFilesDownloadLink(URL allFilesDownloadLink) {
        AllFilesDownloadLink = allFilesDownloadLink;
    }

    /**
     * Gets files download link.
     *
     * @return the files download link
     */
    public URL getFilesDownloadLink() {
        return FilesDownloadLink;
    }

    /**
     * Sets files download link.
     *
     * @param filesDownloadLink the files download link
     */
    public void setFilesDownloadLink(URL filesDownloadLink) {
        FilesDownloadLink = filesDownloadLink;
    }

    /**
     * Gets files page.
     *
     * @return the files page
     */
    public URL getFilesPage() {
        return FilesPage;
    }

    /**
     * Sets files page.
     *
     * @param filesPage the files page
     */
    public void setFilesPage(URL filesPage) {
        FilesPage = filesPage;
    }

    /**
     * Gets home page.
     *
     * @return the home page
     */
    public URL getHomePage() {
        return HomePage;
    }

    /**
     * Sets home page.
     *
     * @param homePage the home page
     */
    public void setHomePage(URL homePage) {
        HomePage = homePage;
    }

    /**
     * Gets login page.
     *
     * @return the login page
     */
    public URL getLoginPage() {
        return LoginPage;
    }

    /**
     * Sets login page.
     *
     * @param loginPage the login page
     */
    public void setLoginPage(URL loginPage) {
        LoginPage = loginPage;
    }

    /**
     * Gets courses page.
     *
     * @return the courses page
     */
    public URL getCoursesPage() {
        return CoursesPage;
    }

    /**
     * Sets courses page.
     *
     * @param coursesPage the courses page
     */
    public void setCoursesPage(URL coursesPage) {
        CoursesPage = coursesPage;
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
     * Gets town name.
     *
     * @return the town name
     */
    public String getTownName() {
        return TownName;
    }

    /**
     * Sets town name.
     *
     * @param townName the town name
     */
    public void setTownName(String townName) {
        TownName = townName;
    }

    /**
     * Gets forgot page.
     *
     * @return the forgot page
     */
    public URL getForgotPage() {
        return ForgotPage;
    }

    /**
     * Sets forgot page.
     *
     * @param forgotPage the forgot page
     */
    public void setForgotPage(URL forgotPage) {
        ForgotPage = forgotPage;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return Type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        Type = type;
    }

    /**
     * Gets profile page.
     *
     * @return the profile page
     */
    public URL getProfilePage() {
        return ProfilePage;
    }

    /**
     * Sets profile page.
     *
     * @param profilePage the profile page
     */
    public void setProfilePage(URL profilePage) {
        ProfilePage = profilePage;
    }

    /**
     * Gets FilesDetailsPage.
     *
     * @return Value of FilesDetailsPage.
     */
    public URL getFilesDetailsPage() {
        return FilesDetailsPage;
    }

    /**
     * Sets new FilesDetailsPage.
     *
     * @param FilesDetailsPage New value of FilesDetailsPage.
     */
    public void setFilesDetailsPage(URL FilesDetailsPage) {
        this.FilesDetailsPage = FilesDetailsPage;
    }
}
