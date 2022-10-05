package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot;

import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.config.Config;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.config.CourseConfig;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.config.TelegramConfig;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.LoginException;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.NotLoggedInException;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.StudIPBot;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.request.RequestException;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.types.*;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.telegram.TelegramBot;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.telegram.api.TelegramApi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.utils.Debugger.Sout;

/**
 * The type Studip telegram bot.
 */
public class StudipTelegramBot {

    private final Config config;

    private Uni uni;
    private User user;

    private StudIPBot studIPBot;

    /**
     * Instantiates a new Studip telegram bot.
     *
     * @param config the config
     */
    public StudipTelegramBot(Config config) {
        this.config = config;
    }

    /**
     * Initialize.
     *
     * @throws InitializationException the initialization exception
     * @throws MalformedURLException   the malformed url exception
     */
    public void initialize() throws InitializationException, MalformedURLException {

        if (config.getApi_endpoint() == null)
            System.out.println("No api_endPoint Present! Please add it to your config.");
        if (config.getApi_username() == null)
            System.out.println("No api_username Present! Please add it to your config.");
        if (config.getApi_password() == null)
            System.out.println("No api_password Present! Please add it to your config.");

        Optional<TelegramConfig> telegramConfig = Optional.ofNullable(config.getTelegramConfig());
        if (!telegramConfig.isPresent()) {
            System.out.println("No telegramConfig Present! Please add it to your config.");
        } else {
            //TODO
        }

        if (config.getApi_endpoint() == null || config.getApi_username() == null || config.getApi_password() == null || !telegramConfig.isPresent()) {
            throw new InitializationException();
        }

        uni = new Uni(new URL(config.getApi_endpoint()));
        user = new User();
        user.setCredentials(new Credentials(config.getApi_username(), config.getApi_password()));
        studIPBot = new StudIPBot(uni, user);

    }

    /**
     * Update.
     *
     * @throws LoginException   the login exception
     * @throws IOException      the io exception
     * @throws RequestException the request exception
     */
    public void update() throws LoginException, IOException, RequestException, NotLoggedInException {

        studIPBot.readStudipConfig();
        studIPBot.login();
        studIPBot.fetchModules();

        //Check News and send new
        Sout("INFO -> Fetching News for current courses");
        ArrayList<Course> currentCourses = studIPBot.getCurrentModules();
        for (Course course : currentCourses) {
            studIPBot.fetchNewsForCourse(course);
            //Check whether new news are available or not
            if (course.getNews() != null && !course.getNews().isEmpty()) {
                ArrayList<News> news = course.getNews();
                Date finalLastFetch = new Date(config.getStartTime());
                news.forEach(n -> {

                    //Try  to load specific configuaration for course
                    int telegramChatId = config.getTelegramConfig().getChatId();
                    String telegramToken = config.getTelegramConfig().getToken();
                    Optional<CourseConfig> courseConfig = Optional.ofNullable(config.getCourseConfigs().get(course.getId()));
                    if (courseConfig.isPresent()) {
                        Optional<TelegramConfig> telegramConfig = Optional.ofNullable(courseConfig.get().getTelegramConfig());
                        if (telegramConfig.isPresent()) {
                            if (telegramConfig.get().getChatId() != 0) {
                                telegramChatId = telegramConfig.get().getChatId();
                            }
                            if (telegramConfig.get().getToken() != null) {
                                telegramToken = telegramConfig.get().getToken();
                            }
                        }
                    }

                    TelegramBot telegramBot = new TelegramBot(telegramToken);

                    //Is News newer than last fetch?
                    if (n.getDate().getTime() > finalLastFetch.getTime()) {
                        try {
                            telegramBot.sendMessage(telegramChatId, "\uD83D\uDCF0_" + course.getName() + "_\uD83D\uDCF0\n*" + n.getTopic() + "*\n" + n.getText(), TelegramApi.parseMode.MARKDOWN);
                        } catch (
                                de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.telegram.api.RequestException e) {
                            //If sending Message failed, try to send short Message with link to Original news
                            try {
                                telegramBot.sendMessage(telegramChatId, "\uD83D\uDCF0_" + course.getName() + "_\uD83D\uDCF0\n*" + n.getTopic() + "*\n" + "_Die Nachricht kann nicht in einer Telegramnachricht angezeigt werden._\n[Öffne StudIP](" + uni.getApi().getProtocol() + "://" + uni.getApi().getHost() + "/dispatch.php/course/overview?cid=" + course.getId(), TelegramApi.parseMode.MARKDOWN);
                            } catch (
                                    de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.telegram.api.RequestException ex) {
                                //When both failed, log error
                                Sout("ERROR -> Course: " + course.getId() + " News: " + n.getId() + " Errorcode: " + ex.getErrorCode() + " Message: " + ex.getErrorMessage());
                            }
                        }
                    }
                });
            }
        }


        //Check Files and download new
        Sout("INFO -> Fetching Filestructure for current courses");
        for (Course course : currentCourses) {
            studIPBot.fetchFilesForCourse(course);
        }


        config.setStartTime(new Date().getTime());
    }
}
