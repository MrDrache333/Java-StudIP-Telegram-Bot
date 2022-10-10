package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.download.GoogleDriveDownloader;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.util.DateTime;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class driveFunctions {
    /**
     * Creates a new file in Google Drive
     *
     * @param googleFolderIdParent id of parent folder
     * @param contentType          content type of new file
     * @param customFileName       name of new file
     * @param uploadStreamContent  content of file
     * @return the created file
     * @throws IOException
     */
    private static com.google.api.services.drive.model.File _createGoogleFile(String googleFolderIdParent, String contentType, //
                                                                              String customFileName, AbstractInputStreamContent uploadStreamContent) throws IOException {

        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName(customFileName);

        List<String> parents = Arrays.asList(googleFolderIdParent);
        fileMetadata.setParents(parents);
        //
        Drive driveService = GoogleDriveUtils.getDriveService();

        com.google.api.services.drive.model.File file = driveService.files().create(fileMetadata, uploadStreamContent)
                .setFields("id, webContentLink, webViewLink, parents").execute();

        return file;
    }

    /**
     * Creates a new file in Google Drive
     *
     * @param googleFolderIdParent id of parent folder
     * @param contentType          content type of new file
     * @param customFileName       name of new file
     * @param uploadFile           content of file
     * @return uploaded file
     * @throws IOException
     */
    public static com.google.api.services.drive.model.File createGoogleFile(String googleFolderIdParent, String contentType, //
                                                                            String customFileName, java.io.File uploadFile) throws IOException {

        //
        AbstractInputStreamContent uploadStreamContent = new FileContent(contentType, uploadFile);
        //
        return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }

    /**
     * Deletes a file
     *
     * @param fileID id of the file
     * @throws IOException
     */
    public static void deleteDriveFile(String fileID) throws IOException {
        Drive driveService = GoogleDriveUtils.getDriveService();

        try {
            System.err.println("The file " + fileID + " will be deleted because it is no longer up to date.");
            driveService.files().delete(fileID).execute();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }

    /**
     * Returns the subfolders of a specific folder
     *
     * @param googleFolderIdParent parent folder id
     * @return list with subfolder ids
     * @throws IOException
     */
    public static final List<com.google.api.services.drive.model.File> getGoogleSubFolders(String googleFolderIdParent) throws IOException {
        Drive driveService = GoogleDriveUtils.getDriveService();

        String pageToken = null;
        List<com.google.api.services.drive.model.File> list = new ArrayList<com.google.api.services.drive.model.File>();

        String query = null;
        if (googleFolderIdParent == null) {
            query = " mimeType = 'application/vnd.google-apps.folder' " //
                    + " and 'root' in parents";
        } else {
            query = " mimeType = 'application/vnd.google-apps.folder' " //
                    + " and '" + googleFolderIdParent + "' in parents";
        }

        do {
            FileList result = driveService.files().list().setQ(query).setSpaces("drive") //
                    // Fields will be assigned values: id, name, createdTime
                    .setFields("nextPageToken, files(id, name, createdTime)")//
                    .setPageToken(pageToken).execute();
            for (com.google.api.services.drive.model.File file : result.getFiles()) {
                list.add(file);
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        //
        return list;
    }

    /**
     * Returns the files within a folder
     *
     * @param googleFolderIdParent parent folder id
     * @return list with files
     * @throws IOException
     */
    public static final List<com.google.api.services.drive.model.File> getGoogleSubFolderFiles(String googleFolderIdParent) throws IOException {
        Drive driveService = GoogleDriveUtils.getDriveService();

        String pageToken = null;
        List<com.google.api.services.drive.model.File> list = new ArrayList<com.google.api.services.drive.model.File>();

        String query = null;
        query = " mimeType != 'application/vnd.google-apps.folder' " + " and '" + googleFolderIdParent + "' in parents";

        do {
            FileList result = driveService.files().list().setQ(query).setFields("nextPageToken, files(id, name, createdTime, mimeType)").execute();
            for (com.google.api.services.drive.model.File file : result.getFiles()) {
                list.add(file);
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        //
        return list;
    }

    /**
     * Creates a Google Drive folder
     *
     * @param folderIdParent id of parent folder
     * @param folderName     name of the folder
     * @return File object with id & name fields will be assigned values
     * @throws IOException
     */
    public static final com.google.api.services.drive.model.File createGoogleFolder(String folderIdParent, String folderName) throws IOException {

        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();

        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        if (folderIdParent != null) {
            List<String> parents = Arrays.asList(folderIdParent);

            fileMetadata.setParents(parents);
        }
        Drive driveService = GoogleDriveUtils.getDriveService();

        // Create a Folder.
        // Returns File object with id & name fields will be assigned values
        com.google.api.services.drive.model.File file = driveService.files().create(fileMetadata).setFields("id, name").execute();

        return file;
    }

    /**
     * Checks if a specific folder exists within Google Drive
     *
     * @param folderIdParent id of parent folder
     * @param folderName     name of the folder
     * @return driveFolderID
     * @throws IOException
     */
    public static String driveFolderExist(String folderIdParent, String folderName) throws IOException {

        List<com.google.api.services.drive.model.File> googleFolders = getGoogleSubFolders(folderIdParent);


        boolean driveFolderExist = false;
        String driveFolderID = "";
        //prüfen ob im übergebenen verzeichnes der zu überprüfende ordner liegt
        for (com.google.api.services.drive.model.File folder : googleFolders) {
            // System.out.println("Folder ID: " + folder.getId() + " --- Name: " + folder.getName());
            if (folder.getName().equals(folderName)) {
                //System.out.println("5. Der Drive Ordner: '" + folderName + "' existiert");
                driveFolderExist = true;
                driveFolderID = folder.getId();
                break;
            }
        }
        //Ordner anlegen wenn nicht existent
        if (!driveFolderExist && !folderName.equals("")) {
            com.google.api.services.drive.model.File driveFolder = createGoogleFolder(folderIdParent, folderName);
            driveFolderID = driveFolder.getId();
            System.err.println("The Drive folder '" + folderName + "' does not exist (Creating Drive folder : '" + driveFolder.getName() + "')");
        }
        //System.out.print(" - ID: " + driveFolderID);

        if (driveFolderID.equals("")) {
            driveFolderID = folderIdParent;
        }
        return driveFolderID;
    }

    /**
     * Checks if the specific file already exists and/or creates it, if flag is set
     *
     * @param folderIdParent id of parent folder
     * @param fileName       name of new file
     * @param filePath       path of the file
     * @param upload         if the file shall be uploaded
     * @return fileID, to proof success
     * @throws IOException
     */
    public static String driveFileExist(String folderIdParent, String fileName, String filePath, boolean upload) throws IOException {
        List<com.google.api.services.drive.model.File> googleFolderFiles = getGoogleSubFolderFiles(folderIdParent);

        //System.out.print("\n\t Filename: " + fileName + " Path: " + filePath);
        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        //System.out.print(" (" + mimeType + ")");

        boolean fileExist = false;
        DateTime createdTime;
        String fileID = "";
        for (com.google.api.services.drive.model.File driveFile : googleFolderFiles) {
            // System.out.println("Datei: " + driveFile.getName() + " --- Erstellt: " + driveFile.getCreatedTime());
            if (driveFile.getName().equals(fileName)) {
                fileExist = true;
                fileID = driveFile.getId();
                createdTime = driveFile.getCreatedTime();
                //System.out.print(" (existiert: " + createdTime + ")\n");
                break;
            }
        }

        if (!fileExist && upload == true) {
            java.io.File uploadFile = new java.io.File(filePath);
            System.out.println("\n\t Filename: " + fileName + " Path: " + filePath + " (upload)");
            // Create Google File:
            com.google.api.services.drive.model.File googleFile = createGoogleFile(folderIdParent, mimeType, fileName, uploadFile);
            //System.out.println(" : " + googleFile.getName() + " --- Erstellt: " + googleFile.getCreatedTime());
        }
        return fileID;
    }
}
