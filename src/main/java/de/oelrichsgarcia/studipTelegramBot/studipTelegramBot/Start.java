package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot;

import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.config.Config;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.config.YAMLConfigHandler;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.LoginException;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.NotLoggedInException;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.download.StandardDownloader;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.request.AuthenticatedAPIRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.utils.Debugger.Sout;

/**
 * The type Hauptklasse.
 */
public class Start {


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

        StudipTelegramBot studipTelegramBot = new StudipTelegramBot(config);
        studipTelegramBot.initialize();

        studipTelegramBot.update();

        Sout("Finished! Time spend: " + new SimpleDateFormat("ssss").format(new Date().getTime() - startTime.getTime()) + " Seconds");
        Sout("API-Requests made: " + AuthenticatedAPIRequest.requests);
        Sout("Downloads made: " + StandardDownloader.getDownloadCount());
        configHandler.dumpConfig(config, YAMLConfigHandler.configPath);

    }
}