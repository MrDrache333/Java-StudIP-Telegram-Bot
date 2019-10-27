package objects;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static utils.Debugger.writeerror;

/**
 * The type Kurs.
 */
public class Kurs {

    private String Name;        //Name des Kurses
    private String ID;          //ID in Stud.IP
    private String shortID;     //Kennung des Kurses z.B. mat955
    private String StudIpNr;    //Nummer in StudIP z.B. 2.0.18a

    private boolean hasNewFiles;    //New Files in Course
    private boolean hasNewPosts;    //New Posts in Forum
    private boolean hasNewNews;     //Neue Ankuendigungen

    private URL Files;          //Link to Files
    private int fileCount;      //Anzahl der Dateien
    private URL Forum;          //Link to the Forum


    /**
     * Instantiates a new Kurs.
     *
     * @param name the name
     */
    public Kurs(String name) {
        this.Name = name;
    }

    /**
     * Fetch file infos array list.
     *
     * @param webClient        the web client
     * @param FilesPage        the files page
     * @param FileDetailsPage  the file details page
     * @param FileDownloadLink the file download link
     * @return the array list
     */
    public ArrayList<StudIPFile> fetchFileInfos(WebClient webClient, URL FilesPage, URL FileDetailsPage, URL FileDownloadLink) {
        if (webClient != null && FilesPage != null) {
            ArrayList<StudIPFile> StudIPFiles = new ArrayList<>();
            try {
                //Seite mit Dateien des Kurses laden
                HtmlPage filespage = webClient.getPage(FilesPage + "?cid=" + ID);
                Document FilePageContent = Jsoup.parse(filespage.asXml());
                //Dateieinträge in Tabelle finden
                Elements Files = FilePageContent.getElementsByAttributeValueStarting("id", "fileref_");
                for (Element File : Files) {
                    try {
                        //Prüfen auf Alter
                        boolean neu = false;
                        try {
                            neu = File.attr("class").equals("new");
                        } catch (Exception e) {
                        }

                        //Dateiname auslesen
                        String FileName = "";
                        try {

                            FileName = File.getElementsByAttributeValueContaining("href", "file/details").get(0).text();
                            if (FileName.contains("[")) FileName = FileName.replace("[", "");
                            if (FileName.contains("]")) FileName = FileName.replace("]", "");
                            if (FileName.contains("(")) FileName = FileName.replace("(", "");
                            if (FileName.contains(")")) FileName = FileName.replace(")", "");
                        } catch (Exception e) {
                        }


                        //Downloadlink auslesen
                        Element FileLink = File.getElementsByAttributeValueStarting("href", FileDownloadLink.toString()).get(0);
                        String BaseUrl = FileLink.attr("href");
                        String id = BaseUrl.substring(BaseUrl.indexOf("file_id=") + 8);
                        id = id.substring(0, id.indexOf("&"));
                        String fileName = BaseUrl.substring(BaseUrl.indexOf("file_name=") + 10);
                        StudIPFile file = new StudIPFile(id, FileName.equals("") ? fileName : FileName, new URL(BaseUrl));
                        file.setNew(neu);

                        //Speicherort und Änderungsdatum auslesen
                        try {

                            //Speicherort
                            HtmlPage detailpage = webClient.getPage(FileDetailsPage + id + "/1?cid=" + ID);
                            Document DetailPage = Jsoup.parse(detailpage.asXml());
                            Elements folders = DetailPage.getElementById("preview_container").getElementsByAttribute("href");
                            String path = "";
                            for (int i = 1; i < folders.size(); i++) {
                                Element Folder = folders.get(i);
                                String folder = Folder.text();
                                if (folder.charAt(0) == ' ') folder = folder.substring(1);
                                if (folder.charAt(folder.length() - 1) == ' ')
                                    folder = folder.substring(0, folder.length() - 2);
                                if (folder.contains("/")) folder = folder.replace("/", "-");
                                path += folder + "/";
                            }
                            file.setPath(path);

                            //Änderungsdatum auslesen
                            Elements table = DetailPage.getElementsByTag("tr");
                            for (int i = 0; i < table.size(); i++) {
                                Element tr = table.get(i);
                                if (tr.text().toLowerCase().contains("geändert") || tr.text().toLowerCase().contains("changed")) {
                                    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                                    Date date = df.parse(tr.getElementsByTag("td").get(1).text());
                                    file.setLastChanged(date);
                                }
                            }
                        } catch (Exception e) {
                            writeerror(e);
                        }

                        StudIPFiles.add(file);
                    } catch (Exception ignored) {
                        //Fails if "File" is a Checkbox
                    }

                }
                return StudIPFiles;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    /**
     * Has updates boolean.
     *
     * @return the boolean
     */
    public boolean hasUpdates() {
        return hasNewFiles || hasNewNews || hasNewPosts;
    }

    /**
     * Get news array list.
     *
     * @param webClient  the web client
     * @param url        the url
     * @param markAsRead the mark as read
     * @return the array list
     */
//Neue News abrufen
    public ArrayList<News> getNews(WebClient webClient, String url, boolean markAsRead) {
        ArrayList<News> newNews = new ArrayList<>();
        try {
            //Versuchen die Newsseite abzurufen
            HtmlPage page = webClient.getPage(new URL(url + ID));
            Document Content = Jsoup.parse(page.asXml());
            //System.out.println(page.asText());
            //Neue News filtern
            Elements articles = Content.select(".studip .new"); //.new
            for (Element article : articles) {
                //News auslesen und als gelesen markieren
                News news = new News(article.select("h1").text(), article.select(".formatted-content").text(), article.getElementsByAttribute("id").get(0).attr("id"));
                news.setHtml(article.select(".formatted-content").html());
                if (markAsRead)
                    webClient.getPage(url + ID + "&contentbox_type=news&contentbox_open=" + news.getID() + "#" + news.getID());
                newNews.add(news);

                //System.out.println(article.select("h1").text() + " | " + article.select(".formatted-content").text());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newNews;
    }

    //Getter und Setter

    /**
     * Is has new news boolean.
     *
     * @return the boolean
     */
    public boolean isHasNewNews() {
        return hasNewNews;
    }

    /**
     * Sets has new news.
     *
     * @param hasNewNews the has new news
     */
    public void setHasNewNews(boolean hasNewNews) {
        this.hasNewNews = hasNewNews;
    }

    /**
     * Gets stud ip nr.
     *
     * @return the stud ip nr
     */
    public String getStudIpNr() {
        return StudIpNr;
    }

    /**
     * Sets stud ip nr.
     *
     * @param studIpNr the stud ip nr
     */
    public void setStudIpNr(String studIpNr) {
        StudIpNr = studIpNr;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return Name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        Name = name;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getID() {
        return ID;
    }

    /**
     * Sets id.
     *
     * @param ID the id
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * Gets short id.
     *
     * @return the short id
     */
    public String getShortID() {
        return shortID;
    }

    /**
     * Sets short id.
     *
     * @param shortID the short id
     */
    public void setShortID(String shortID) {
        this.shortID = shortID;
    }

    /**
     * Is has new files boolean.
     *
     * @return the boolean
     */
    public boolean isHasNewFiles() {
        return hasNewFiles;
    }

    /**
     * Sets has new files.
     *
     * @param hasNewFiles the has new files
     */
    public void setHasNewFiles(boolean hasNewFiles) {
        this.hasNewFiles = hasNewFiles;
    }

    /**
     * Is has new posts boolean.
     *
     * @return the boolean
     */
    public boolean isHasNewPosts() {
        return hasNewPosts;
    }

    /**
     * Sets has new posts.
     *
     * @param hasNewPosts the has new posts
     */
    public void setHasNewPosts(boolean hasNewPosts) {
        this.hasNewPosts = hasNewPosts;
    }

    /**
     * Gets files.
     *
     * @return the files
     */
    public URL getFiles() {
        return Files;
    }

    /**
     * Sets files.
     *
     * @param files the files
     */
    public void setFiles(URL files) {
        Files = files;
    }

    /**
     * Gets forum.
     *
     * @return the forum
     */
    public URL getForum() {
        return Forum;
    }

    /**
     * Sets forum.
     *
     * @param forum the forum
     */
    public void setForum(URL forum) {
        Forum = forum;
    }

    /**
     * Gets file count.
     *
     * @return the file count
     */
    public int getFileCount() {
        return fileCount;
    }

    /**
     * Sets file count.
     *
     * @param fileCount the file count
     */
    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }
}

