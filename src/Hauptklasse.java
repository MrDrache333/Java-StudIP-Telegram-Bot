import com.gargoylesoftware.htmlunit.WebClient;
import javafx.stage.Stage;
import objects.*;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Settings;
import utils.WebHook;
import utils.telegramBot;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
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
    private static final Settings programSettings = new Settings(new File("config.xml"), false);
    private static final Settings filelist = new Settings(new File("filelist.stud"), false);
    private static final Settings modullist = new Settings(new File("modullist.stud"), false);
    /**
     * The constant programStorage.
     */
    public static HashMap<String, String> programStorage = new HashMap<>();
    /**
     * The constant TESTING.
     */
    private static boolean TESTING = false;
    private static telegramBot TelegramBot;
    private static int telegramChatId = 0;
    private static final File DownloadPath = new File("StudIP/Files/");

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

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

        if (args.length > 0) {
            //Wenn das Programm getestet wird den Testmodus einschalten
            if (args[0].equals("TEST")) {

                System.out.println("TESTMODUS");
                TESTING = true;
            } else if (args[0].equals("INIT")) {
                init();
            } else if (args[0].equals("configure") && args.length == 2) {
                switch (args[1]) {
                    case "telegramDefaultChatId": {
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
                        TelegramBot = new telegramBot(token);
                        programSettings.setProperty("telegram.defaultChatId", "" + configureDefaultTelegramChatId());
                        programSettings.saveProperties();

                        Sout("Your default Telegram Chat-ID is now set to: " + programSettings.getProperty("telegram.defaultChatId"));
                        System.exit(0);
                        break;
                    }
                }
            }

        }
        //Try to read the filelist
        if (filelist.loadProperties()) {
            utils.Debugger.Sout("Filelist: Successfully loadet filelist stored in \"" + filelist.getFile().getName() + "\"");
        } else
            utils.Debugger.Sout("Filelist: Failed loading filelist stored in \"" + filelist.getFile().getName() + "\"");

        //Try to read Modulelist
        if (modullist.loadProperties()) {
            utils.Debugger.Sout("Modullist: Successfully loadet Modullist stored in \"" + modullist.getFile().getName() + "\"");
        } else
            utils.Debugger.Sout("Modullist: Failed loading Modullist stored in \"" + modullist.getFile().getName() + "\"");

        //Load the Programsettings or create if missing
        if (programSettings.loadProperties() && !programSettings.getProperty("login_username").equals("") && !programSettings.getProperty("login_password").equals("") && !programSettings.getProperty("telegram.token").equals("")) {
            Sout("Settings: Info: Successfully loaded Settings stored in \"" + programSettings.getFile().getName() + "\"");

            //Create a new Telegram_bot
            TelegramBot = new telegramBot(programSettings.getProperty("telegram.token"));

            //Read Default ChatId
            try {
                telegramChatId = Integer.parseInt(programSettings.getProperty("telegram.defaultChatId"));
            } catch (Exception e) {
                Sout("FEHLER -> Die Standard-Telegram-ChatId konnte nicht gelesen werden. Konfiguriere diesen Bot neu mit dem Startparameter \"INIT\"");
                System.exit(404);
            }

            login(programSettings.getProperty("login_username"), programSettings.getProperty("login_password"));
        } else {
            //Reset the config file, if something is corrupt
            programSettings.resetProperties();
            init();
        }
    }

    //Configure the Telegram Bot
    private static void init() {
        Scanner in = new Scanner(System.in);
        Sout("Gebe deinen Benutzernamen der Uni ein:");
        String user;
        while ((user = in.nextLine()).equals("")) {
            Sout("Der Benutzername darf nicht leer sein:");
        }
        programSettings.setProperty("login_username", user);

        //Passwort
        Sout("Gebe dein Passwort für " + user + " ein:");
        String pass;
        while ((pass = in.nextLine()).equals("")) {
            Sout("Das Passwort darf nicht leer sein:");
        }
        programSettings.setProperty("login_password", pass);

        //Try to login
        try {
            //Login
            currentUser = login.EinLoggen(currentUni.getLoginPage(), user, pass);
        } catch (Exception e) {
            Sout("Falsche Logindaten");
            e.printStackTrace();
        }
        Sout("Erfolgreich angemeldet!");

        //Telegram Token
        Sout("Gebe dein API-Token deines Telegram Bots ein:");
        String token;
        while ((token = in.nextLine()).equals("")) {
            Sout("Der Token darf nicht leer sein:");
        }
        TelegramBot = new telegramBot(token);
        programSettings.setProperty("telegram.token", token);

        long chatId = configureDefaultTelegramChatId();
        programSettings.setProperty("telegram.defaultChatId", "" + chatId);

        if (programSettings.saveProperties()) {
            Sout("Settings: Info: Successfully stored Settings at \"" + programSettings.getFile().getName() + "\"");
        } else
            writeerror(new Exception("Settings: Info: Failed store Settings at \"" + programSettings.getFile().getName() + "\""));
        System.exit(0);
    }

    private static long configureDefaultTelegramChatId() {
        //Telegram Default ChatId
        Sout("Füge den Bot als Admin in deiner Gruppe hinzu und schreibe ihn an, oder direkt. Schreibe Ihm folgende Indentifikations-ID:");
        int rand = Math.abs(new Random().nextInt());
        Sout("\"" + rand + "\"");
        Sout("Warte auf Nachricht...(Abbruch: Strg+C)");
        int chatId = 0;
        while (chatId == 0) {
            try {
                String response = telegramBot.getUpdates();
                //System.out.println(response);
                JSONObject json = new JSONObject(response);
                JSONArray messages = json.getJSONArray("result");
                for (int i = 0; i < messages.length(); i++) {
                    JSONObject message = messages.getJSONObject(i);
                    String id = message.getJSONObject("message").getString("text");
                    if (id.equals("" + rand)) {
                        chatId = message.getJSONObject("message").getJSONObject("chat").getInt("id");
                        telegramBot.sendMessage(chatId, "Configuration Successfull!\nYour ChatId is: " + chatId, telegramBot.parseMode.TEXT, true);
                        return chatId;
                    }
                }

            } catch (Exception e) {
                chatId = 0;
            }
            if (chatId == 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        }
        return chatId;
    }

    //Lgoin, Data fetching and Telegram Push
    private static void login(String user, String pass) {

        currentUser = null;

        //Login
        try {
            //Try to login
            currentUser = login.EinLoggen(currentUni.getLoginPage(), user, pass);
            currentUser = login.filterInfos(currentUser);
        } catch (Exception e) {
            System.out.println("Falsche Logindaten");
            e.printStackTrace();
        }
        if (currentUser == null || currentUser.getName().equals("")) {
            System.out.println("Fehler beim Login!");
            System.exit(0);
        }
        //At this Point the User is successfully loged in
        //TODO

        int sendMessages = 0;
        //Kurse nach Updates untersuchen
        for (Kurs kurs : currentUser.getKurse()) {
            String kursname = kurs.getName();
            if (kursname.contains("(")) kursname = kursname.substring(0, kursname.indexOf("("));


            //Check if course is Blacklisted
            boolean black;

            try {
                black = Boolean.parseBoolean(modullist.getProperty(kurs.getID() + ".blacklisted"));
            } catch (Exception e) {
                black = false;
                modullist.setProperty(kurs.getID() + ".blacklisted", "" + black);
            }

            //Load the specific sendType
            int sendtype;
            try {
                sendtype = Integer.parseInt(modullist.getProperty(kurs.getID() + ".sendType"));
                if (sendtype < 0 | sendtype > 3) sendtype = 1;   //Set Default sendType Value if invalid
            } catch (Exception e) {
                sendtype = 1;
                modullist.setProperty(kurs.getID() + ".sendType", "1");
            }

            //Load the specific ChatId for Telegram
            int telegramChatId = 0;
            try {
                telegramChatId = Integer.parseInt(modullist.getProperty(kurs.getID() + ".telegramChatId"));
            } catch (Exception e) {
                modullist.setProperty(kurs.getID() + ".telegramChatId", "0");
            } finally {
                if (telegramChatId == 0) {
                    telegramChatId = Hauptklasse.telegramChatId;
                }
            }
            if (TESTING) telegramChatId = Hauptklasse.telegramChatId;

            //Load the specific WebHook
            WebHook webhook = null;
            try {
                webhook = new WebHook(new URL(modullist.getProperty(kurs.getID() + ".webhook")));
            } catch (Exception e) {
                modullist.setProperty(kurs.getID() + ".webhook", "");
            }
            if (webhook == null) sendtype = 1;

            if (!black) {
                System.out.println("Update Modul: " + kurs.getID() + " | " + kursname);
                //Get FIle Infos for the Course
                ArrayList<StudIPFile> Files = kurs.fetchFileInfos(webClient, currentUni.getFilesPage(), currentUni.getFilesDetailsPage(), currentUni.getFilesDownloadLink());
                String newfiles = "";
                String updatedfiles = "";
                for (StudIPFile file : Files) {
                    File studipfile = new File(DownloadPath.getPath() + "/" + kursname.replace(" ", "_") + "/" + file.getPath() + file.getName());
                    //Check, if the File is up to Date
                    boolean aktuell = studipfile.exists() && (file.getLastChanged() == null || studipfile.lastModified() >= file.getLastChanged().getTime());
                    //Check, if the file needs to be downloaded again, do it and add the Message to the Message-List
                    if (!studipfile.exists() || !aktuell) {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yyyy");

                        if (studipfile.exists()) {
                            Sout("Datei " + file.getName() + " ist nicht mehr aktuell. Datei: " + sdf.format(studipfile.lastModified()) + " / StudIP: " + sdf.format(file.getLastChanged().getTime()) + "Aktualisiere...");
                            studipfile.delete();
                            utils.htmlcrawler.DownloadFile(webClient, file.getLink(), new File(DownloadPath.getPath() + "/" + kursname.replace(" ", "_") + "/" + file.getPath() + file.getName()));
                            if (studipfile.exists())
                                updatedfiles += "[" + file.getName() + "](" + file.getLink() + ")\n";
                        } else {
                            utils.htmlcrawler.DownloadFile(webClient, file.getLink(), new File(DownloadPath.getPath() + "/" + kursname.replace(" ", "_") + "/" + file.getPath() + file.getName()));
                            if (studipfile.exists())
                                newfiles += "[" + file.getName() + "](" + file.getLink() + ")\n";
                        }


                    }
                }
                //Check, if any Messages need to be send to Telegram
                if (!newfiles.equals("")) {
                    newfiles = "\n*Neue Dateien verfügbar*\n" + newfiles + "\n[Alle neuen Dateien herunterladen](" + currentUni.getAllFilesDownloadLink() + "?cid=" + kurs.getID() + ")";
                }
                if (!updatedfiles.equals("")) {
                    updatedfiles = "\n*Aktualisierte Dateien*\n" + updatedfiles;
                }
                if (!newfiles.equals("") || !updatedfiles.equals("")) {
                    String header = "📄 _" + kursname + "_ 📄";
                    switch (sendtype) {
                        case 1: {
                            telegramBot.sendMessage(telegramChatId, header + updatedfiles + newfiles, telegramBot.parseMode.MARKDOWN, !TESTING);
                            break;
                        }
                        case 2: {
                            webhook.httpGet(header + updatedfiles + newfiles, !TESTING);
                            break;
                        }
                        case 3: {
                            telegramBot.sendMessage(telegramChatId, header + updatedfiles + newfiles, telegramBot.parseMode.MARKDOWN, !TESTING);
                            webhook.httpGet(header + updatedfiles + newfiles, !TESTING);
                            break;
                        }
                    }

                }

                //Check if the Course has any new News to push
                if (kurs.isHasNewNews()) {
                    //Neue News abrufen
                    ArrayList<News> newNews = kurs.getNews(webClient, "https://elearning.uni-oldenburg.de/dispatch.php/course/overview?cid=", !TESTING);

                    //Because there are potential large Posts, post any News Message as a unique Message to Telegram
                    for (News news : newNews) {
                        switch (sendtype) {
                            case 1: {
                                telegramBot.sendMessage(telegramChatId, "📰 _" + kursname + "_ 📰\n*" + news.getTitle() + "*\n" + news.getText(), telegramBot.parseMode.MARKDOWN, true);
                                break;
                            }
                            case 2: {
                                webhook.httpGet("📰 _" + kursname + "_ 📰\n*" + news.getTitle() + "*\n" + news.getText(), !TESTING);
                                break;
                            }
                            case 3: {
                                telegramBot.sendMessage(telegramChatId, "📰 _" + kursname + "_ 📰\n*" + news.getTitle() + "*\n" + news.getText(), telegramBot.parseMode.MARKDOWN, true);
                                webhook.httpGet("📰 _" + kursname + "_ 📰\n*" + news.getTitle() + "*\n" + news.getText(), !TESTING);
                                break;
                            }
                        }
                        sendMessages++;
                    }
                }

            } else {
                Sout("Modul " + kursname + " | " + kurs.getID() + " übersprungen!");
            }
        }
        modullist.saveProperties();
        if (sendMessages == 0) System.out.println("Keine neuen Nachrichten!");

    }


}
