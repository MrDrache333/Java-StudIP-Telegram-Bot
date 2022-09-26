import studip.api.RestAPI;
import studip.api.types.Credentials;
import studip.api.types.Uni;
import studip.api.types.User;
import utils.Settings;
import utils.WebClient;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import static utils.Debugger.Sout;
import static utils.Debugger.writeerror;

/**
 * The type Hauptklasse.
 */
public class main {

    /**
     * The constant programSettings.
     */
    private static final Settings programSettings = new Settings(new File("config.xml"), false);
    private static final Settings modullist = new Settings(new File("modullist.stud"), false);
    private static final File DownloadPath = new File("StudIP/Files/");
    /**
     * The constant currentUni.
     */
    static Uni currentUni;     //Aktuelle Uni
    /**
     * The constant currentUser.
     */
    static User currentUser;
    static RestAPI restapi;
    private static boolean TESTING = false;
    private static TelegramBot telegramBot;
    private static int telegramChatId = 0;
    /**
     * The constant webClient.
     */
    WebClient webClient;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) throws IOException {
        //Uni erstellen mit nötigen Links
        currentUni = new Uni(
                "Carl von Ossietzky Universität Oldenburg",
                new URL("https://elearning.uni-oldenburg.de/api.php/")
        );

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
                    telegramBot = new TelegramBot(token);
                    programSettings.setProperty("telegram.defaultChatId", "" + telegramBot.configureDefaultTelegramChatId());
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
            telegramBot = new TelegramBot(programSettings.getProperty("telegram.token"));

            //Read Default ChatId
            try {
                telegramChatId = Integer.parseInt(programSettings.getProperty("telegram.defaultChatId"));
            } catch (Exception e) {
                Sout("FEHLER -> Die Standard-Telegram-ChatId konnte nicht gelesen werden. Konfiguriere diesen Bot neu mit dem Startparameter \"INIT\"");
                System.exit(404);
            }

            currentUser.setCredentials(new Credentials(programSettings.getProperty("login_username"), programSettings.getProperty("login_password")));

            StudIPBot studIPBot = new StudIPBot(currentUni, currentUser);

            studIPBot.login();

        } else {
            //Reset the config file, if something is corrupt
            programSettings.resetProperties();
            init();
        }
    }

    //Configure the Telegram Bot
    private static void init() throws IOException {
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
        StudIPBot studIPBot = new StudIPBot(currentUni, currentUser);
        studIPBot.login();

        if (!studIPBot.isLoggedIn()) {
            Sout("ERROR -> Error during Login");
            System.exit(401);
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