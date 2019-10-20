import com.gargoylesoftware.htmlunit.WebClient;
import javafx.stage.Stage;
import objects.*;
import utils.Password;
import utils.Settings;
import utils.telegramBot;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static utils.Debugger.Sout;
import static utils.Debugger.writeerror;

/**
 * The type Hauptklasse.
 */
public class Hauptklasse {

    /**
     * The constant currentUni.
     */
    static Uni currentUni;     //Aktuelle Uni
    /**
     * The constant studIPHomePage.
     */
    static URL studIPHomePage;

    /**
     * The constant currentUser.
     */
    static User currentUser;
    /**
     * The constant webClient.
     */
    static WebClient webClient;
    /**
     * The constant stage.
     */
    public static Stage stage;

    /**
     * The constant programSettings.
     */
    private static Settings programSettings = new Settings(new File("config.xml"), true);
    private static Settings blacklist = new Settings(new File("blacklist.stud"), false);
    private static Settings filelist = new Settings(new File("filelist.stud"), false);
    /**
     * The constant programStorage.
     */
    public static HashMap<String, String> programStorage = new HashMap<>();
    /**
     * The constant TESTING.
     */
    private static boolean TESTING = false;
    private static telegramBot TelegramBot;
    private static String telegramChatId = "";
    private static File DownloadPath = new File("StudIP/Files/");

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        if (args.length > 0) {
            //Wenn das Programm getestet wird den Testmodus einschalten
            if (args[0].equals("TEST")) {
                System.out.println("TESTMODUS");
                TESTING = true;
                //TODO Ändere diese in deine Private ChatID
                telegramChatId = "895714744";    //Privat
            } else if (args[0].equals("INIT")) {
                Scanner in = new Scanner(System.in);
                Sout("Gebe deinen Benutzernamen der Uni ein:");
                String user;
                while ((user = in.nextLine()).equals("")) {
                    Sout("Der Benutzername darf nicht leer sein:");
                }
                Sout("Gebe dein Passwort für " + user + " ein:");
                String pass;
                while ((pass = in.nextLine()).equals("")) {
                    Sout("Das Passwort darf nicht leer sein:");
                }

                //Versuchen einzuloggen
                Sout("Einloggen...");
                try {
                    //Einloggen
                    currentUser = login.EinLoggen(currentUni.getLoginPage(), user, pass);
                } catch (Exception e) {
                    Sout("Falsche Logindaten");
                    e.printStackTrace();
                }
                Sout("Erfolgreich angemeldet!");
                programSettings.setProperty("login_username", user);
                programSettings.setProperty("login_password", user);
                programSettings.saveProperties();

            }

        } else {
            //TODO Ändere diese in deine Gruppen ChatID
            telegramChatId = "-396426700";    //Gruppe
        }


        try {
            //Uni erstellen mit nötigen Links
            currentUni = new Uni(
                    "Carl von Ossietzky",
                    "Oldenburg",
                    "Universität",
                    new URL("https://uol.de/"),
                    new URL("https://elearning.uni-oldenburg.de/index.php?cancel_login=1"),
                    new URL("https://pw.uni-oldenburg.de/IDMProv/jsps/pwdmgt/ForgotPassword.jsp"),
                    new URL("https://elearning.uni-oldenburg.de/dispatch.php/profile"),
                    new URL("https://elearning.uni-oldenburg.de/dispatch.php/my_courses"),
                    new URL("https://elearning.uni-oldenburg.de/dispatch.php/course/files/flat"),
                    new URL("https://elearning.uni-oldenburg.de/dispatch.php/file/details/"),
                    new URL("https://elearning.uni-oldenburg.de/sendfile.php"),
                    new URL("https://elearning.uni-oldenburg.de/dispatch.php/course/files/newest_files")
            );
            studIPHomePage = new URL("https://www.studip.de");
        } catch (Exception ignored) {

        }

        if (filelist.loadProperties()) {
            utils.Debugger.Sout("Filelist: Successfully loadet filelist stored in \"" + filelist.getFile().getName() + "\"");
        } else
            utils.Debugger.Sout("Filelist: Failed loading filelist stored in \"" + filelist.getFile().getName() + "\"");
        if (blacklist.loadProperties()) {
            utils.Debugger.Sout("Blacklist: Successfully loadet filelist stored in \"" + blacklist.getFile().getName() + "\"");
        } else
            utils.Debugger.Sout("Blacklist: Failed loading filelist stored in \"" + blacklist.getFile().getName() + "\"");

        //Programmeinstellungen laden und ggf. anlegen
        if (programSettings.loadProperties()) {
            Sout("Settings: Info: Successfully loaded Settings stored in \"" + programSettings.getFile().getName() + "\"");

            //Neuen Telegram Bot erstellen
            TelegramBot = new telegramBot(programSettings.getProperty("telegram.token"));

            login(programSettings.getProperty("login_username"), programSettings.getProperty("login_password"));
        } else {
            //Bei Fehler die Programmeinstellungen zurücksetzen und neu erstellen
            programSettings.resetProperties();
            programSettings.addProperty("login_username", "");
            programSettings.addProperty("login_password", "");
            programSettings.addProperty("telegram.token", "");
            if (programSettings.saveProperties()) {
                Sout("Settings: Info: Successfully stored Settings at \"" + programSettings.getFile().getName() + "\"");
            } else
                writeerror(new Exception("Settings: Info: Failed store Settings at \"" + programSettings.getFile().getName() + "\""));
        }
    }

    //Funktion zum anmeldenm abrufen der Daten und weiterleitung an Telegram
    private static void login(String user, String pass) {

        currentUser = null;

        //Versuchen einzuloggen
        try {
            //Einloggen
            currentUser = login.EinLoggen(currentUni.getLoginPage(), user, Password.lock(pass));
            currentUser = login.filterInfos(currentUser);
        } catch (Exception e) {
            System.out.println("Falsche Logindaten");
            e.printStackTrace();
        }
        if (currentUser == null || currentUser.getName().equals("")) {
            System.out.println("Fehler beim Login!");
            System.exit(0);
        }
        //Login erfolgreich und Daten empfangen

        int sendMessages = 0;
        //Kurse nach Updates untersuchen
        for (Kurs kurs : currentUser.getKurse()) {
            String kursname = kurs.getName();
            if (kursname.contains("(")) kursname = kursname.substring(0, kursname.indexOf("("));

            boolean black = !blacklist.getProperty(kurs.getID()).equals("");

            System.out.println("Update Modul: " + kurs.getID() + " | " + kursname);

            if (black) {
                ArrayList<StudIPFile> Files = kurs.fetchFileInfos(webClient, currentUni.getFilesPage(), currentUni.getFilesDetailsPage(), currentUni.getFilesDownloadLink());
                String post = "";
                for (StudIPFile file : Files) {
                    if (!new File(DownloadPath.getPath() + "/" + kursname.replace(" ", "_") + "/" + file.getPath() + file.getName()).exists()) {
                        //System.out.println("\t" + file.getName() + " is new!");
                        post += "[" + file.getName() + "](" + file.getLink() + ")\n";
                        utils.htmlcrawler.DownloadFile(webClient, file.getLink(), new File(DownloadPath.getPath() + "/" + kursname.replace(" ", "_") + "/" + file.getPath() + file.getName()));

                    }
                }
                if (!post.equals("")) {
                    post = "📄 _" + kursname + "_ 📄\n*Neue Dateien verfügbar*\n" + post + "\n[Alle neuen Dateien herunterladen](" + currentUni.getAllFilesDownloadLink() + "?cid=" + kurs.getID() + ")";
                    telegramBot.sendMessage(telegramChatId, post, !TESTING);
                }
            }
            //Wenn es Updates gibt an Telegram weiterleiten
            if (kurs.isHasNewNews()) {
                System.out.println("\t\t- " + kurs.getName() + "\tFiles: " + kurs.getFileCount() + "\tNewFiles: " + (kurs.isHasNewFiles() ? "Ja" : "Nein") + "\tNewPosts: " + (kurs.isHasNewPosts() ? "Ja" : "Nein") + "\tNewNews: " + (kurs.isHasNewNews() ? "Ja" : "Nein"));

                //Neue News abrufen
                ArrayList<News> newNews = kurs.getNews(webClient, "https://elearning.uni-oldenburg.de/dispatch.php/course/overview?cid=", !TESTING);

                for (News news : newNews) {
                    //Neue News an Telegram senden
                    //System.out.println("\t" + news.getTitle() + "\n\t\t" + news.getText());
                    telegramBot.sendMessage(telegramChatId, "📰 _" + kursname + "_ 📰\n*" + news.getTitle() + "*\n" + news.getText(), !TESTING);
                    sendMessages++;
                }
            }
        }
        if (sendMessages == 0) System.out.println("Keine neuen Nachrichten!");

    }


}
