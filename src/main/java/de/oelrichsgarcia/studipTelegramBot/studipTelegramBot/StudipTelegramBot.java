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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
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
        }

        if (config.getApi_endpoint() == null || config.getApi_username() == null || config.getApi_password() == null || !telegramConfig.isPresent()) {
            throw new InitializationException();
        }

        uni = new Uni(new URL(config.getApi_endpoint()));
        user = new User();
        user.setCredentials(new Credentials(config.getApi_username(), config.getApi_password()));
        studIPBot = new StudIPBot(uni, user);

    }

    private TelegramBot getTelegramBotForCourse(Course course) {
        String telegramToken = config.getTelegramConfig().getToken();
        Optional<CourseConfig> courseConfig = Optional.ofNullable(config.getCourseConfigs().get(course.getId()));
        if (courseConfig.isPresent()) {
            Optional<TelegramConfig> telegramConfig = Optional.ofNullable(courseConfig.get().getTelegramConfig());
            if (telegramConfig.isPresent()) {
                if (telegramConfig.get().getToken() != null) {
                    telegramToken = telegramConfig.get().getToken();
                }
            }
        }
        return new TelegramBot(telegramToken);
    }

    private int getTelegramChatForCourse(Course course) {
        int telegramChatId = config.getTelegramConfig().getChatId();
        Optional<CourseConfig> courseConfig = Optional.ofNullable(config.getCourseConfigs().get(course.getId()));
        if (courseConfig.isPresent()) {
            Optional<TelegramConfig> telegramConfig = Optional.ofNullable(courseConfig.get().getTelegramConfig());
            if (telegramConfig.isPresent()) {
                if (telegramConfig.get().getChatId() != 0) {
                    telegramChatId = telegramConfig.get().getChatId();
                }

            }
        }
        return telegramChatId;
    }

    private Boolean isCourseBlacklisted(Course course) {
        return config.getBlacklist().contains(course.getId());
    }

    /**
     * Update.
     *
     * @throws LoginException       the login exception
     * @throws IOException          the io exception
     * @throws RequestException     the request exception
     * @throws NotLoggedInException the not logged in exception
     */
    public void update() throws LoginException, IOException, RequestException, NotLoggedInException {

        studIPBot.readStudipConfig();
        studIPBot.login();
        studIPBot.fetchModules();

        ArrayList<Course> currentCourses = studIPBot.getCurrentModules();

        //Check News and send new
        for (Course course : currentCourses) {
            //Skip if course is on Blacklist
            if (isCourseBlacklisted(course)) {
                Sout("Skipping " + course.getName());
                continue;
            } else
                Sout("Updating " + course.getName());

            //Apply Custom settings
            Optional<CourseConfig> courseConfig = Optional.ofNullable(config.getCourseConfigs().get(course.getId()));
            boolean downloadFiles = true;
            String downloadpath = course.getName();
            boolean sendNews = true;
            if (courseConfig.isPresent()) {
                CourseConfig currentCourseConfig = courseConfig.get();
                if (currentCourseConfig.getName() != null) {
                    course.setName(currentCourseConfig.getName());
                }
                if (currentCourseConfig.getFolderName() != null) {
                    downloadpath = currentCourseConfig.getFolderName();
                }
                downloadFiles = currentCourseConfig.isDownloadFiles();
                sendNews = currentCourseConfig.isSendNews();
            } else {
                //Create new config
                createNewCourseConfig(course);
            }

            //Fetch and handle news of course
            if (sendNews) {
                studIPBot.fetchNewsForCourse(course);
                filterAndSendNews(course);
            }

            //Fetch and handle files of course
            if (downloadFiles) {
                studIPBot.fetchFileStructureForCourse(course);
                createFoldersAndDownloadFiles(course, downloadpath);
            }
        }

        config.setStartTime(new Date().getTime());
    }

    private void createNewCourseConfig(Course course) {
        //Create new custom settings in config for each course
        Map<String, CourseConfig> newCourseConfigs = config.getCourseConfigs();
        CourseConfig newCourseConfig = new CourseConfig();
        newCourseConfig.setName(course.getName());
        newCourseConfigs.put(course.getId(), newCourseConfig);
        config.setCourseConfigs(newCourseConfigs);
    }

    private void createFoldersAndDownloadFiles(Course course, String downloadpath) {
        //if (!course.getRootFolder().getChilds().isEmpty())
        studIPBot.downloadFilesAndCreateFolders(course.getRootFolder().getChilds(), Paths.get("data/" + config.getDownloadFolder() + "/" + downloadpath + "/"));

    }

    private void filterAndSendNews(Course course) {
        //Check whether new news are available or not
        if (course.getNews() != null && !course.getNews().isEmpty()) {
            ArrayList<News> news = course.getNews();
            Date finalLastFetch = new Date(config.getStartTime());

            TelegramBot telegramBot = getTelegramBotForCourse(course);
            int telegramChatId = getTelegramChatForCourse(course);

            news.forEach(n -> {

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
}
