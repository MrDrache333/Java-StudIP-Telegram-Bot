package utils;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The type Htmlcrawler.
 */
public class htmlcrawler {

    private WebClient webClient;

    /**
     * Instantiates a new Htmlcrawler.
     */
    public htmlcrawler() {

        //Alle nicht nötigen Features des WebClients abschalten um die Geschwindigkeit zu erhöhen
        this.webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);

        this.webClient.setIncorrectnessListener((arg0, arg1) -> {
        });
        this.webClient.setCssErrorHandler(new SilentCssErrorHandler());
        this.webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {

            @Override
            public void timeoutError(HtmlPage arg0, long arg1, long arg2) {
            }

            @Override
            public void scriptException(HtmlPage arg0, ScriptException arg1) {
            }

            @Override
            public void malformedScriptURL(HtmlPage arg0, String arg1, MalformedURLException arg2) {
            }

            @Override
            public void loadScriptError(HtmlPage arg0, URL arg1, Exception arg2) {
            }
        });
        this.webClient.setHTMLParserListener(new HTMLParserListener() {
            @Override
            public void error(String s, URL url, String s1, int i, int i1, String s2) {
            }

            @Override
            public void warning(String s, URL url, String s1, int i, int i1, String s2) {
            }
        });

        this.webClient.getOptions().setJavaScriptEnabled(false);
        this.webClient.getOptions().setDownloadImages(false);
        this.webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        this.webClient.getOptions().setThrowExceptionOnScriptError(false);
        this.webClient.getOptions().setCssEnabled(false);
        this.webClient.getOptions().setAppletEnabled(false);
        this.webClient.getOptions().setTimeout(10000);
    }

    /**
     * Download file.
     *
     * @param webClient the web client
     * @param url       the url
     * @param output    the output
     */
    public static void DownloadFile(WebClient webClient, URL url, File output) {
        webClient.addWebWindowListener(new WebWindowListener() {

            public void webWindowOpened(WebWindowEvent event) {
            }

            public void webWindowContentChanged(WebWindowEvent event) {
                Page page = event.getNewPage();
                //System.out.println(page.getUrl());
                String Path = output.getPath().substring(0, output.getPath().lastIndexOf("/") + 1);
                if (!new File(Path).exists()) new File(Path).mkdirs();
                FileOutputStream fos = null;
                InputStream is = null;
                if (page != null && page instanceof UnexpectedPage) {
                    try {
                        fos = new FileOutputStream(output);
                        UnexpectedPage uPage = (UnexpectedPage) page;
                        is = uPage.getInputStream();
                        IOUtils.copy(is, fos);

                        webClient.removeWebWindowListener(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (fos != null)
                                fos.close();
                            if (is != null)
                                is.close();
                            if (output.exists()) {
                                System.out.println("Datei " + output.getPath() + " erfolgreich heruntergeladen!");
                            } else
                                System.err.println("Fehler beim Herunterladen der Datei " + output.getName() + " nach " + output.getPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("Fehler beim speichern der Datei " + output.getName());
                }
            }

            public void webWindowClosed(WebWindowEvent event) {
            }
        });
        try {
            webClient.getPage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets web client.
     *
     * @return the web client
     */
    public WebClient getWebClient() {
        return webClient;
    }
}
