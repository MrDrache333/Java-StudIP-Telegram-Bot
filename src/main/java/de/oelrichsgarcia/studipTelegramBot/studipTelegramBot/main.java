package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot;

import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.config.Config;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.config.YAMLConfigHandler;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.LoginException;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.NotLoggedInException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.utils.Debugger.Sout;

/**
 * The type Hauptklasse.
 */
public class main {
    private static StudipTelegramBot studipTelegramBot;


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException, InitializationException, LoginException, de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.request.RequestException, NotLoggedInException {

        Date startTime = new Date();


        if (!new File(YAMLConfigHandler.configPath.toUri()).exists()) {
            YAMLConfigHandler.createNewConfig(new Config(), YAMLConfigHandler.configPath);
        }
        YAMLConfigHandler configHandler = YAMLConfigHandler.getInstance();
        Config config = configHandler.loadConfig();

        studipTelegramBot = new StudipTelegramBot(config);
        studipTelegramBot.initialize();

        studipTelegramBot.update();

        Sout("INFO -> Finished! Time spend: " + new SimpleDateFormat("ss").format(new Date().getTime() - startTime.getTime()) + " Seconds");
        configHandler.dumpConfig();

    }
}