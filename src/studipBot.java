import org.json.JSONArray;
import org.json.JSONObject;
import studip.api.Credentials;
import studip.api.RestAPI;
import studip.api.request.RequestResponse;
import studip.api.types.Uni;
import studip.api.types.User;
import utils.Settings;
import utils.WebClient;
import utils.telegramBot;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;

import static utils.Debugger.Sout;
import static utils.Debugger.writeerror;

/**
 * The type Hauptklasse.
 */
public class studipBot {

    /**
     * The constant currentUni.
     */
    static Uni currentUni;     //Aktuelle Uni

    /**
     * The constant currentUser.
     */
    static User currentUser;
    static RestAPI restapi;
    /**
     * The constant webClient.
     */
    WebClient webClient;

    /**
     * The constant programSettings.
     */
    private static final Settings programSettings = new Settings(new File("config.xml"), false);
    private static final Settings modullist = new Settings(new File("modullist.stud"), false);
    private static boolean TESTING = false;
    private static telegramBot TelegramBot;
    private static int telegramChatId = 0;
    private static final File DownloadPath = new File("StudIP/Files/");

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) throws IOException {

        try {
            //Uni erstellen mit nötigen Links
            currentUni = new Uni(
                    "Carl von Ossietzky Universität Oldenburg",
                    new URL("https://elearning.uni-oldenburg.de/api.php/")
            );
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
                    TelegramBot = new telegramBot(token);
                    programSettings.setProperty("telegram.defaultChatId", "" + configureDefaultTelegramChatId());
                    programSettings.saveProperties();

                    Sout("Your default Telegram Chat-ID is now set to: " + programSettings.getProperty("telegram.defaultChatId"));
                    System.exit(0);
                }
            }

        }
        //Try to read Modulelist
        if (modullist.loadProperties()) {
            Sout("Modullist: Successfully loadet Modullist stored in \"" + modullist.getFile().getName() + "\"");
        } else
            Sout("Modullist: Failed loading Modullist stored in \"" + modullist.getFile().getName() + "\"");

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
            restapi = new RestAPI(currentUni.getApi(), new Credentials(programSettings.getProperty("login_username"), programSettings.getProperty("login_password")));
            System.out.println("INFO -> Trying to Login to " + currentUni.getName() + " with API-EndPoint at " + currentUni.getApi().toString());
            RequestResponse response = restapi.login();

            //Store Userinformations when login was successfull
            if (response.getResonseCode() == 200) {
                //Set Userinformations
                JSONObject json = new JSONObject(response.getResonesMessage());
                currentUser = new User();
                currentUser.setUserName(json.getJSONObject("name").getString("username"));
                currentUser.setName(json.getJSONObject("name").getString("formatted"));
                Sout("INFO -> Logged in successfully as " + currentUser.getName());
            }
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
            restapi = new RestAPI(currentUni.getApi(), new Credentials(programSettings.getProperty("login_username"), programSettings.getProperty("login_password")));
            RequestResponse response = restapi.login();

            //Store Userinformations when login was successfull
            if (response.getResonseCode() == 200) {
                //Set Userinformations
                JSONObject json = new JSONObject(response.getResonesMessage());
                currentUser.setUserName(json.getJSONObject("name").getString("username"));
                currentUser.setName(json.getJSONObject("name").getString("formatted"));
                Sout("INFO -> Erfolgreich angemeldet als " + currentUser.getName());
            }
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

}