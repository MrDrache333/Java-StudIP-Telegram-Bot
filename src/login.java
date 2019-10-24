import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.concurrent.Task;
import objects.Kurs;
import objects.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import utils.htmlcrawler;

import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Level;

import static utils.Debugger.writeerror;

/**
 * The type Login.
 */
public class login {

    /**
     * The constant task.
     */
    public static Task task;

    /**
     * Ein loggen user.
     *
     * @param url      the url
     * @param UserID   the user id
     * @param Passwort the passwort
     * @return the user
     */
    public static User EinLoggen(URL url, String UserID, String Passwort) {

        final User[] currentUser = {null};

        System.out.println("EinLoggen:");
        Long startDate = new Date().getTime();
        Hauptklasse.webClient = new htmlcrawler().getWebClient();

        //Deaktivate Warnings from WebClient
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

        try {
            //Loginseite abrufen
            System.out.println("\tTrying to Login to Account: " + UserID + " of " + Hauptklasse.currentUni.getName() + " " + Hauptklasse.currentUni.getType() + " " + Hauptklasse.currentUni.getTownName());
            HtmlPage page = Hauptklasse.webClient.getPage(url);

            //Login Formular herausfiltern und mit logindaten ausfüllen
            HtmlForm form = page.getForms().get(0);
            form.getInputByName("loginname").setValueAttribute(UserID);
            HtmlInput passWordInput = form.getInputByName("password");
            passWordInput.setValueAttribute(Passwort);

            //Auf den Loginbutton klicken
            page = form.getButtonByName("login").click();

            //login abgeschlossen verifizieren durch Nachricht im footer
            HtmlDivision footer = page.getHtmlElementById("footer");
            System.out.println("\t" + footer.getFirstChild().asText());
            String user = footer.getFirstChild().asText().replace("Sie sind angemeldet als ", "").replace("You are signed in as ", "");
            user = user.substring(0, user.indexOf('(') - 1);

            currentUser[0] = new User(UserID, Passwort);
            currentUser[0].setName(user);
            currentUser[0].getStudIP_Data().setMainPage(page);
            //System.out.println("\t\t- Got MainPage for " + user);
            //currentUser[0].getStudIP_Data().setProfil(Hauptklasse.webClient.getPage(Hauptklasse.currentUni.getProfilePage()));

            //System.out.println("\t\t- Got Profile for " + user);
            currentUser[0].getStudIP_Data().setModules(Hauptklasse.webClient.getPage(Hauptklasse.currentUni.getCoursesPage()));

            System.out.println("\t\t- Got Courses for " + user);

            System.out.println("\tFetched user data successfully.");
            System.out.println("\tTime used: " + formatTime(new Date().getTime() - startDate));

            //PB und Namen raussuchen (hier nicht notwendig)
            /*
            Document Profil = Jsoup.parse(currentUser[0].getStudIP_Data().getProfil().asXml());
            String linkToPB = Profil.select("div.text-center").select("img").get(0).attributes().get("src");
            String Name = Profil.select("div.sidebar-widget-header").get(0).text();
            System.out.println("\tStudent: " + Name);
            currentUser[0].setName(Name);
            currentUser[0].setProfilePictureLink(linkToPB);
            */


        } catch (UnknownHostException e) {
            System.out.println("\tFEHLER - Keine Internetverbindung!");
            Hauptklasse.currentUser = new User("", "");
        } catch (Exception e) {
            writeerror(e);
            Hauptklasse.currentUser = new User("", "");
        } finally {
            Hauptklasse.webClient.close();
        }
        return currentUser[0];
    }

    /**
     * Filter infos user.
     *
     * @param user the user
     * @return the user
     */
    public static User filterInfos(User user) {
        if (user != null && user.getStudIP_Data().getModules() != null) {
            //Kurse raussuchen
            //System.out.println("\tKurse");
            //Kursseite mit Jsoup paresen und auswerten
            Document Kurse = Jsoup.parse(user.getStudIP_Data().getModules().asXml());
            //Tabelle mit den Kursen auswählen
            Elements Tables = Kurse.select("table");
            Element Table = null;
            for (Element table : Tables) {
                if (table.attributes().get("class").contains("mycourses")) {
                    Table = table;
                }
            }
            if (Table != null)
                for (Element tableRow : Table.select("tr")) {
                    if (tableRow.select("td").size() == 7) {
                        Elements rowElements = tableRow.select("td");

                        Kurs tempKurs;

                        //Name des Kurses
                        Element courseName = rowElements.get(1).select("img").first();
                        tempKurs = new Kurs(courseName.attributes().get("title"));

                        //Nummer des Kurses
                        try {
                            Node courseNr = rowElements.get(2).childNode(0);
                            tempKurs.setStudIpNr(courseNr.toString());
                        } catch (Exception e) {
                            tempKurs.setStudIpNr("");
                        }

                        //Neue Forumseinträge?
                        try {
                            Node courseForumInfo = rowElements.get(5).childNode(1).childNode(1);
                            boolean hasNewPosts = courseForumInfo.attributes().get("class").contains("icon-role-attention");
                            tempKurs.setHasNewPosts(hasNewPosts);
                        } catch (Exception e) {
                            tempKurs.setHasNewPosts(false);
                        }

                        //Neue Dateien? Wie viele?
                        try {
                            Node courseFilesInfo = rowElements.get(5).childNode(5);
                            String filesLink = courseFilesInfo.attributes().get("href");
                            tempKurs.setFiles(new URL(filesLink));
                            courseFilesInfo = courseFilesInfo.childNode(1);
                            int filecount = Integer.parseInt(courseFilesInfo.attributes().get("title").substring(0, courseFilesInfo.attributes().get("title").indexOf(' ')));
                            tempKurs.setFileCount(filecount);
                            boolean hasNewFiles = courseFilesInfo.attributes().get("class").contains("icon-role-attention");
                            tempKurs.setHasNewFiles(hasNewFiles);

                        } catch (Exception e) {
                            tempKurs.setHasNewFiles(false);
                            tempKurs.setFileCount(0);
                        }

                        //Kurs ID
                        try {
                            Node courseFilesInfo = rowElements.get(5).childNode(5);
                            String filesLink = "";
                            String ID = "";
                            if (courseName.toString().contains("?d=")) {
                                filesLink = courseName.toString();
                                ID = filesLink.substring(filesLink.lastIndexOf(" course-"));
                                ID = ID.substring(ID.indexOf(" course-") + 8);
                                ID = ID.substring(0, ID.indexOf("\""));
                            } else {
                                filesLink = courseFilesInfo.attributes().get("href");
                                ID = filesLink.substring(filesLink.indexOf("auswahl="));
                                ID = ID.substring(ID.indexOf("=") + 1, ID.indexOf("&"));
                            }
                            tempKurs.setID(ID);
                        } catch (Exception e) {

                        }

                        //Neue Ankuendigungen?
                        try {
                            Node courseNewsInfo = rowElements.get(5).childNode(7).childNode(1);
                            boolean hasNewNews = courseNewsInfo.attributes().get("class").contains("icon-role-attention");
                            tempKurs.setHasNewNews(hasNewNews);
                        } catch (Exception e) {
                            tempKurs.setHasNewNews(false);
                        }

                        user.getKurse().add(tempKurs);
                    }
                }
        }
        return user;
    }

    /**
     * Format time string.
     *
     * @param time the time
     * @return the string
     */
//Retrun the Formatted time(1000 <=> 1 sek.) like 00:00:00
    static String formatTime(long time) {
        time /= 1000;
        long sec = 0, min = 0, hour = 0;
        while (time >= 3600) {
            hour++;
            time -= 3600;
        }
        while (time >= 60) {
            min++;
            time -= 60;
        }
        sec = time;
        return "" + (hour < 10 ? (hour != 0 ? ("0" + hour) : "00") : hour) + ":" + (min < 10 ? (min != 0 ? ("0" + min) : "00") : min) + ":" + (sec < 10 ? (sec != 0 ? ("0" + sec) : "00") : sec);
    }
}