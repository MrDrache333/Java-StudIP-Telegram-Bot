package de.oelrichsgarcia.studipTelegramBot;

import de.oelrichsgarcia.studipTelegramBot.telegram.TelegramBot;
import de.oelrichsgarcia.studipTelegramBot.telegram.api.TelegramApi;
import de.oelrichsgarcia.studipTelegramBot.utils.Settings;
import studip.LoginException;
import studip.NotLoggedInException;
import studip.StudIPBot;
import studip.api.request.RequestException;
import studip.api.types.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import static de.oelrichsgarcia.studipTelegramBot.utils.Debugger.Sout;
import static de.oelrichsgarcia.studipTelegramBot.utils.Debugger.writeerror;

/**
 * The type Hauptklasse.
 */
public class main {

    /**
     * The constant programSettings.
     */
    //TODO Config als YAML speichern
    private static final Settings programSettings = new Settings(new File("config.xml"));
    private static final Settings modullist = new Settings(new File("modullist.stud"));
    //TODO Pfad in Config speichern
    private static final File DownloadPath = new File("StudIP/Files/");
    /**
     * The constant currentUni.
     */
    static Uni currentUni;     //Aktuelle Uni
    /**
     * The constant currentUser.
     */
    static User currentUser;
    private static TelegramBot telegramBot;
    private static int telegramChatId = 0;


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException, NotLoggedInException, RequestException {
        //Create new Uni
        //TODO Uni aus Config laden
        //TODO Uni namen von Api abrufen?
        currentUni = new Uni(
                "Carl von Ossietzky Universität Oldenburg",
                new URL("https://elearning.uni-oldenburg.de/api.php/")
        );

        //Handle Input Parameters
        handleArgs(args);

        //Try to read Module list
        if (modullist.loadProperties()) {
            Sout("Modullist: Successfully loaded Modullist stored in \"" + modullist.getFile().getName() + "\"");
        } else
            Sout("Modullist: Failed loading Modullist stored in \"" + modullist.getFile().getName() + "\"");

        //Load the Program settings or create if missing
        if (programSettings.loadProperties() && !programSettings.getProperty("login_username").equals("") && !programSettings.getProperty("login_password").equals("") && !programSettings.getProperty("telegram.token").equals("")) {
            Sout("Settings: Info: Successfully loaded Settings stored in \"" + programSettings.getFile().getName() + "\"");

            //Create a new Telegram_bot
            telegramBot = new TelegramBot(programSettings.getProperty("telegram.token"));

            //Read Default ChatId
            try {
                telegramChatId = Integer.parseInt(programSettings.getProperty("telegram.defaultChatId"));
            } catch (Exception e) {
                Sout("FEHLER -> Die Standard-Telegram-ChatId konnte nicht gelesen werden. Konfiguriere diesen Bot neu mit dem Startparameter \"INIT\"");
                System.exit(404);
            }

            //Parse latest fetch
            Date lastFetch = new Date(0);
            try {
                lastFetch = new Date(Long.parseLong(programSettings.getProperty("last-fetch")));
            } catch (Exception e) {
                Sout("INFO -> Cloud not found or parse last fetch. Initial run: Fetching everything now...");

            }

            currentUser = new User();
            currentUser.setCredentials(new Credentials(programSettings.getProperty("login_username"), programSettings.getProperty("login_password")));

            StudIPBot studIPBot = new StudIPBot(currentUni, currentUser);

            try {
                studIPBot.login();
            } catch (RequestException | LoginException e) {
                Sout("ERROR -> Error during Login\n" + e.getMessage());
                System.exit(1);
            }

            try {
                studIPBot.fetchModules();
            } catch (RequestException e) {
                Sout("ERROR -> Error fetching courses\n" + e.getErrorMessage());
                System.exit(e.getErrorCode());
            }

            //Check News and send new
            ArrayList<Course> currentCourses = studIPBot.getCurrentModules();
            for (Course course : currentCourses) {
                studIPBot.fetchNewsForCourse(course);
                //Check whether new news are available or not
                if (course.getNews() != null && !course.getNews().isEmpty()) {
                    ArrayList<News> news = course.getNews();
                    Date finalLastFetch = lastFetch;
                    news.forEach(n -> {

                        //Is News newer than last fetch?
                        if (n.getDate().getTime() > finalLastFetch.getTime()) {
                            try {
                                telegramBot.sendMessage(telegramChatId, "\uD83D\uDCF0_" + course.getName() + "_\uD83D\uDCF0\n*" + n.getTopic() + "*\n" + n.getText(), TelegramApi.parseMode.MARKDOWN);
                            } catch (de.oelrichsgarcia.studipTelegramBot.telegram.api.RequestException e) {
                                //If sending Message failed, try to send short Message with link to Original news
                                try {
                                    telegramBot.sendMessage(telegramChatId, "\uD83D\uDCF0_" + course.getName() + "_\uD83D\uDCF0\n*" + n.getTopic() + "*\n" + "_Die Nachricht kann nicht in einer Telegramnachricht angezeigt werden._\n[Öffne StudIP](" + currentUni.getApi().getProtocol() + "://" + currentUni.getApi().getHost() + "/dispatch.php/course/overview?cid=" + course.getID(), TelegramApi.parseMode.MARKDOWN);
                                } catch (de.oelrichsgarcia.studipTelegramBot.telegram.api.RequestException ex) {
                                    //When both failed, log error
                                    Sout("ERROR -> Course: " + course.getID() + " News: " + n.getId() + " Errorcode: " + ex.getErrorCode() + " Message: " + ex.getErrorMessage());
                                }
                            }
                        }
                    });
                }
            }

            //TODO Nach fertigstellung aktuelles Datum speichern
            programSettings.setProperty("last-fetch", String.valueOf(new Date().getTime()));
            programSettings.saveProperties();

        } else {
            //Reset the config file, if something is corrupt
            programSettings.resetProperties();
            initializeTelegram();
        }
    }

    private static void handleArgs(String[] args) throws IOException {
        if (args.length > 0) {
            //Wenn das Programm getestet wird den Testmodus einschalten
            if (args[0].equals("TEST")) {

                System.out.println("TESTMODUS");
            } else if (args[0].equals("INIT")) {
                initializeTelegram();
            } else if (args[0].equals("configure") && args.length == 2) {
                if ("telegramDefaultChatId".equals(args[1])) {
                    Sout("--- Configuration of the default Telegram-Chat-ID ---");
                    //Create a new Telegram_bot
                    if (!programSettings.loadProperties()) {
                        Sout("Reconfigure Bot using \"INIT\" Parameter");
                        System.exit(0);
                    }
                    final String token = programSettings.getProperty("telegram.token");
                    if (token.equals("")) {
                        Sout("Failed to load Telegram Token");
                        Sout("Reconfigure Bot using \"INIT\" Parameter");
                        System.exit(0);

                    }
                    telegramBot = new TelegramBot(token);
                    programSettings.setProperty("telegram.defaultChatId", "" + telegramBot.configureDefaultTelegramChatId());
                    programSettings.saveProperties();

                    Sout("Your default Telegram Chat-ID is now set to: " + programSettings.getProperty("telegram.defaultChatId"));
                    System.exit(0);
                }
            }

        }
    }

    //Configure the Telegram Bot
    private static void initializeTelegram() throws IOException {
        Scanner in = new Scanner(System.in);
        Sout("Gebe deinen Benutzernamen der Uni ein:");
        String user;
        while ((user = in.nextLine()).equals("")) {
            Sout("Der Benutzername darf nicht leer sein:");
        }
        programSettings.setProperty("login_username", user);

        //Password
        Sout("Gebe dein Passwort für " + user + " ein:");
        String pass;
        while ((pass = in.nextLine()).equals("")) {
            Sout("Das Passwort darf nicht leer sein:");
        }
        programSettings.setProperty("login_password", pass);

        //Try to log in
        StudIPBot studIPBot = new StudIPBot(currentUni, currentUser);
        try {
            studIPBot.login();
        } catch (RequestException | LoginException e) {
            Sout("ERROR -> Error during Login\n" + e.getMessage());
            System.exit(1);
        }

        //Telegram Token
        Sout("Gebe dein API-Token deines Telegram Bots ein:");
        String token;
        while ((token = in.nextLine()).equals("")) {
            Sout("Der Token darf nicht leer sein:");
        }
        telegramBot = new TelegramBot(token);
        programSettings.setProperty("telegram.token", token);

        long chatId = telegramBot.configureDefaultTelegramChatId();
        programSettings.setProperty("telegram.defaultChatId", "" + chatId);

        if (programSettings.saveProperties()) {
            Sout("Settings: Info: Successfully stored Settings at \"" + programSettings.getFile().getName() + "\"");
        } else
            writeerror(new Exception("Settings: Info: Failed store Settings at \"" + programSettings.getFile().getName() + "\""));
        System.exit(0);
    }

}