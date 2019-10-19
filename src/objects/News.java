package objects;

/**
 * The type News.
 */
public class News {

    private String Title;
    private String Text;
    private String ID;

    /**
     * Instantiates a new News.
     *
     * @param tilte the tilte
     * @param text  the text
     * @param id    the id
     */
    public News(String tilte, String text, String id) {
        this.Title = tilte;
        this.Text = text;
        this.ID = id;
    }

    /**
     * Gets Title.
     *
     * @return Value of Title.
     */
    public String getTitle() {
        return Title;
    }

    /**
     * Sets new Title.
     *
     * @param Title New value of Title.
     */
    public void setTitle(String Title) {
        this.Title = Title;
    }

    /**
     * Gets Text.
     *
     * @return Value of Text.
     */
    public String getText() {
        return Text;
    }

    /**
     * Sets new Text.
     *
     * @param Text New value of Text.
     */
    public void setText(String Text) {
        this.Text = Text;
    }

    /**
     * Gets ID.
     *
     * @return Value of ID.
     */
    public String getID() {
        return ID;
    }

    /**
     * Sets new ID.
     *
     * @param ID New value of ID.
     */
    public void setID(String ID) {
        this.ID = ID;
    }
}
