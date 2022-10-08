package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * The type Web hook.
 */
public class WebHook {

    private URL LINK;

    /**
     * Instantiates a new Web hook.
     *
     * @param link the link
     */
    public WebHook(URL link) {
        this.LINK = link;
    }

    //Konvertiert die Eingabe eines InputStreams in einen String und gibt ihn zurück
    private static String convertStreamToString(InputStream is)
            throws IOException {
            /*
             * To convert the InputStream to String we use the
             * Reader.read(char[] buffer) method. We iterate until the
    35.         * Reader return -1 which means there's no more data to
    36.         * read. We use the StringWriter class to produce the string.
    37.         */
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, StandardCharsets.UTF_8));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }

    public String httpGet(String data, boolean send) {
        //Nachricht senden, wenn sie gesendet werden soll
        if (send) {
            int trys = 1;
            do {
                try {

                    String jsonBody = "{" +
                            "\"content\" : \"" + data.replace("\n", "\\n") + "\"" +
                            "}";
                    HttpURLConnection conn = (HttpURLConnection) LINK.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestMethod("POST");

                    OutputStream os = conn.getOutputStream();
                    os.write(jsonBody.getBytes());
                    os.close();

                    String result = conn.getResponseCode() + " " + conn.getResponseMessage();

                    conn.disconnect();
                    Debugger.Sout("New Discord Push: " + jsonBody);
                    Debugger.Sout("\tResponse: " + result);
                    //if (!result.contains("Congratulations"))Debugger.Sout("Failed to Push Informations to IFTTT!");
                    conn.disconnect();
                    trys = 0;
                    return result;
                } catch (Exception e) {
                    if (trys > 2) {
                        System.err.println("Failed to connect. Down?");
                        System.err.println(e.getMessage());
                    }
                    trys++;
                }
            } while (trys > 0 && trys < 4);
        }


        return null;
    }

    /**
     * Gets LINK.
     *
     * @return Value of LINK.
     */
    public URL getLINK() {
        return LINK;
    }

    /**
     * Sets new LINK.
     *
     * @param LINK New value of LINK.
     */
    public void setLINK(URL LINK) {
        this.LINK = LINK;
    }
}
