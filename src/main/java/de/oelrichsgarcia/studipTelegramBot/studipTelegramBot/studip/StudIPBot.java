package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip;

import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.RestAPI;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.download.DownloadException;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.request.RequestException;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.request.RequestResponse;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.types.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Objects;

import static de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.download.GoogleDriveDownloader.driveFunctions.*;
import static de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.utils.Debugger.Sout;

/**
 * The type Stud ip bot.
 */
public class StudIPBot {

    private final Uni uni;
    private final User user;
    private final RestAPI restapi;
    private boolean loggedIn;

    private FileUpdateSummary updateSummary;

    /**
     * Instantiates a new Stud ip bot.
     *
     * @param uni  the uni
     * @param user the user
     */
    public StudIPBot(Uni uni, User user) {
        this.uni = uni;
        this.user = user;
        restapi = new RestAPI(uni.getApi(), user.getCredentials());
        this.loggedIn = false;
        this.updateSummary = new FileUpdateSummary();
    }

    /**
     * Read studip config.
     *
     * @throws IOException      the io exception
     * @throws RequestException the request exception
     */
    public void readStudipConfig() throws IOException, RequestException {
        Sout("Trying to read StudIP-Config with API-EndPoint at " + restapi.getEndpoint().toString());
        RequestResponse response = restapi.fetchSettings();
        if (response.getResponseCode() == 200) {
            //Set Userinformations
            JSONObject json = new JSONObject(response.getResponseMessage());
            uni.setName(json.getString("UNI_NAME_CLEAN"));
        }
    }


    /**
     * Login.
     *
     * @throws IOException      the io exception
     * @throws RequestException the api exception
     * @throws LoginException   the login exception
     */
    public void login() throws IOException, RequestException, LoginException {
        System.out.println("Trying to Login to " + uni.getName() + " with API-EndPoint at " + restapi.getEndpoint().toString());
        RequestResponse response = restapi.login();

        //Store Userinformations when login was successfull
        if (response.getResponseCode() == 200) {
            //Set Userinformations
            JSONObject json = new JSONObject(response.getResponseMessage());
            user.setUserName(json.getJSONObject("name").getString("username"));
            user.setName(json.getJSONObject("name").getString("formatted"));
            user.setUserId(json.getString("user_id"));
            loggedIn = true;
            Sout("Logged in successfully as " + user.getName());
        }
    }


    /**
     * Fetch modules array list.
     *
     * @return the array list
     * @throws IOException          the io exception
     * @throws NotLoggedInException the not logged in exception
     * @throws RequestException     the api exception
     */
    public void fetchModules() throws IOException, NotLoggedInException, RequestException {
        System.out.println("Fetching Courses");
        //Store Userinformations when login was successfull
        if (loggedIn) {
            RequestResponse response = restapi.fetchUserModules(user.getUserId());
            //Set Userinformations

            user.setKurse(ApiResponseParser.parseCourses(response));
        } else throw new NotLoggedInException();
    }

    /**
     * Gets current modules.
     *
     * @return the current modules
     * @throws RequestException     the api exception
     * @throws IOException          the io exception
     * @throws NotLoggedInException the not logged in exception
     */
    public ArrayList<Course> getCurrentModules() throws RequestException, IOException, NotLoggedInException {
        if (!user.getKurse().isEmpty()) {
            if (uni.getSemesters() == null || uni.getSemesters().isEmpty()) {
                fetchSemesters();
            }
            Semester currentSemester = null;
            for (Semester semester : uni.getSemesters()) {
                Long date = new Date().getTime();
                //date = 1656932718L * 1000;
                if (semester.getBegin().getTime() < date && semester.getEnd().getTime() > date) {
                    currentSemester = semester;
                    break;
                }
            }
            if (currentSemester != null) {
                Sout("Current Semester: " + currentSemester.getTitle());
                ArrayList<Course> courses = new ArrayList<>();
                Semester finalCurrentSemester = currentSemester;
                user.getKurse().forEach(c -> {
                            try {
                                //If StartSemester of Course is this Date or before
                                if (uni.getSemesters().stream().filter(s -> Objects.equals(s.getId(), c.getStartSemester())).findFirst().get().getBegin().getTime() <= finalCurrentSemester.getEnd().getTime()) {
                                    if (uni.getSemesters().stream().filter(s -> Objects.equals(s.getId(), c.getEndSemester())).findFirst().get().getEnd().getTime() >= finalCurrentSemester.getBegin().getTime()) {
                                        courses.add(c);
                                    }
                                }
                            } catch (NullPointerException | NoSuchElementException ignored) {
                            }
                        }
                );
                return courses;
            } else return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    private void fetchSemesters() throws RequestException, IOException, NotLoggedInException {
        if (loggedIn) {
            RequestResponse response = restapi.fetchSemesters();
            uni.setSemesters(ApiResponseParser.parseSemesters(response));
        } else throw new NotLoggedInException();
    }

    /**
     * Fetch news for course.
     *
     * @param course the course
     * @throws IOException          the io exception
     * @throws RequestException     the request exception
     * @throws NotLoggedInException the not logged in exception
     */
    public void fetchNewsForCourse(Course course) throws IOException, RequestException, NotLoggedInException {
        if (loggedIn) {
            RequestResponse response = restapi.fetchCourseNews(course.getId());
            course.setNews(ApiResponseParser.parseAllNews(response));
        } else throw new NotLoggedInException();
    }

    /**
     * Fetch files for course.
     *
     * @param course the course
     * @throws IOException          the io exception
     * @throws RequestException     the request exception
     * @throws NotLoggedInException the not logged in exception
     */
    public void fetchFileStructureForCourse(Course course) throws IOException, RequestException, NotLoggedInException {
        if (loggedIn) {
            RequestResponse response = restapi.fetchCoursesTopFolder(course.getId());
            course.setRootFolder(ApiResponseParser.parseTopFolder(response));
            fetchSubFoldersAndFiles(course.getRootFolder());
        } else throw new NotLoggedInException();
    }

    private void fetchSubFoldersAndFiles(StudIPFolder folder) throws MalformedURLException, RequestException {
        //Make API call
        RequestResponse response = restapi.fetchSubFolders(folder.getId());
        JSONObject collection = null;
        JSONArray folders = new JSONArray();
        //Try to Parse the Response Collection
        try {
            collection = new JSONObject(response.getResponseMessage()).getJSONObject("collection");
            JSONArray finalFolders = folders;
            JSONObject finalCollection = collection;
            collection.keySet().forEach(k -> finalFolders.put(finalCollection.get(k)));
        } catch (JSONException e) {
            folders = new JSONObject(response.getResponseMessage()).getJSONArray("collection");
        }
        //Return if no elements are present
        if (folders.isEmpty()) {
            return;
        }

        //Parse every Folder and Subfolder and add all elements to the Root Folder
        ApiResponseParser.parseFolders(folders, folder).forEach(f -> {
            try {
                if (f.isReadable()) {
                    fetchFilesForFolder(f);
                    //Some Recursivenes
                    fetchSubFoldersAndFiles(f);
                }
                folder.addChild(f);
            } catch (MalformedURLException | RequestException e) {
                throw new RuntimeException(e);
            }

        });

    }

    private void fetchFilesForFolder(StudIPFolder folder) throws MalformedURLException, RequestException {
        RequestResponse response = restapi.fetchFolderFiles(folder.getId());
        ApiResponseParser.parseFiles(new JSONObject(response.getResponseMessage()).getJSONArray("collection"), folder).forEach(folder::addChild);
    }

    /**
     * Download files and create folders.
     *
     * @param objects           the objects
     * @param basePath          the base path
     * @param useGoogleDrive    the use google drive
     * @param drive_root_folder the drive root folder
     */
    public void downloadFilesAndCreateFolders(ArrayList<StudIPObject> objects, Path basePath, boolean useGoogleDrive, String drive_root_folder) {
        checkAndCreateFolder(basePath);
        for (StudIPObject object : objects) {
            //If it's a Folder
            if (object instanceof StudIPFolder) {
                try {
                    downloadFilesAndCreateFolders(((StudIPFolder) object).getChilds(), Paths.get(basePath + "/" + object.getName() + "/"), useGoogleDrive, drive_root_folder);
                } catch (Exception e) {
                    System.err.println("Could not get content of " + object.getName());
                }
            } else {
                //If it's a FIle
                File file = new File(basePath + "/" + object.getName());

                String fileSize = "";
                StudIPFile studIPFile = (StudIPFile) object;
                //If it's not downloadable continue
                if (!studIPFile.isDownloadable()) continue;
                if (studIPFile.getFileSize() > 1000000) fileSize = studIPFile.getFileSize() / 1000000 + " MB";
                else if (studIPFile.getFileSize() > 1000) fileSize = studIPFile.getFileSize() / 1000 + " KB";

                if (file.exists()) {
                    //Check if online version is newer then download
                    if (object.getUpdated().getTime() > file.lastModified()) {
                        Sout("Updating outdated file " + basePath + "/" + object.getName() + " " + fileSize);
                        downloadFile((StudIPFile) object, basePath);
                        if(useGoogleDrive) {
                            uploadToGoogleDrive((StudIPFile) object, basePath, drive_root_folder);
                        }
                        updateSummary.addUpdatedFile((StudIPFile) object);

                    }
                } else {
                    Sout("Downloading new file " + basePath + "/" + object.getName() + " " + fileSize);
                    downloadFile((StudIPFile) object, basePath);
                    if(useGoogleDrive) {
                        uploadToGoogleDrive((StudIPFile) object, basePath, drive_root_folder);
                    }
                    updateSummary.addNewFile((StudIPFile) object);
                }
            }

        }

    }

    /**
     * Reset update summary.
     */
    public void resetUpdateSummary() {
        updateSummary = new FileUpdateSummary();
    }

    /**
     * Get update summary file update summary.
     *
     * @return the file update summary
     */
    public FileUpdateSummary getUpdateSummary() {
        return updateSummary;
    }

    private void downloadFile(StudIPFile file, Path path) {
        try {
            restapi.downloadFile(file, path);
        } catch (DownloadException | MalformedURLException e) {
            Sout("Failed to download " + path + "/" + file.getName());
        }
    }

    private void uploadToGoogleDrive(StudIPFile file, Path path, String drive_root_folder) {
        try {
            String fullPath = path.toString();
            String currentParent = drive_root_folder;
            String datei_ordnerDriveID = "";

            // Create folder structure in Google Drive
            while (!fullPath.equals("")) {
                StringBuilder subfolderTmp = new StringBuilder();
                for (char ch : fullPath.toCharArray()) {

                    fullPath = fullPath.substring(1);
                    if (ch == '\\') {
                        break;
                    } else {
                        subfolderTmp.append(ch);
                    }
                }

                if (!subfolderTmp.toString().equals("data") && !subfolderTmp.toString().equals("Files")) {
                    datei_ordnerDriveID = driveFolderExist(currentParent, subfolderTmp.toString());
                    currentParent = datei_ordnerDriveID;
                }

            }


            String filepath = path.toString();
            filepath = filepath.replaceAll("[.$|:\"<>?]", "");

            //hier muss in Drive die Datei gelöscht und neu hochgeladen werden
            String fileID = driveFileExist(datei_ordnerDriveID, file.getName(), filepath + "/" + file.getName(), false);
            if (!fileID.equals("")) { //datei existiert
                deleteDriveFile(fileID);
            }

            //Prüfen ob Datei in Drive existiert
            driveFileExist(datei_ordnerDriveID, file.getName(), filepath + "/" + file.getName(), true);


        } catch (Exception e) {
            Sout("Failed to upload " + path + "/" + file.getName());
        }
    }

    private void checkAndCreateFolder(Path path) {
        File base = new File(path.toUri());
        if (!base.exists()) {
            if (!base.mkdirs()) {
                Sout("Failed to create Directory " + path);
            }
        }
    }

    /**
     * Gets uni.
     *
     * @return the uni
     */
    public Uni getUni() {
        return uni;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Is logged in boolean.
     *
     * @return the boolean
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }
}
