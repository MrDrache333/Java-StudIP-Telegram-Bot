package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.download;

import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.types.Credentials;
import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.types.StudIPFile;
import org.apache.commons.codec.binary.Base64;

import javax.net.ssl.HttpsURLConnection;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * The type Standard downloader.
 */
public class StandardDownloader implements Downloader {

    private static final int BUFFER_SIZE = 1024000000;
    private static int downloadCount = 0;

    private final Credentials credentials;


    /**
     * Instantiates a new Standard downloader.
     *
     * @param credentials the credentials
     */
    public StandardDownloader(Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * Gets download count.
     *
     * @return the download count
     */
    public static int getDownloadCount() {
        return downloadCount;
    }

    @Override
    public void downloadFile(URL url, StudIPFile file, Path path) throws DownloadException {
        int status = 200;
        try {
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            //Auth
            String auth = credentials.getUsername() + ":" + credentials.getPassword();
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
            String authHeaderValue = "Basic " + new String(encodedAuth);
            con.setRequestProperty("Authorization", authHeaderValue);

            status = con.getResponseCode();
            if (status == HTTP_OK) {

                // opens input stream from the HTTP connection
                InputStream inputStream = con.getInputStream();
                String saveFilePath = path + "/" + file.getName();

                // opens an output stream to save into file
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                int bytesRead;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();
                downloadCount++;
            } else
                throw new DownloadException(status, con.getResponseMessage());
            con.disconnect();

        } catch (IOException e) {
            throw new DownloadException(status, e.getMessage());
        }
    }
}
