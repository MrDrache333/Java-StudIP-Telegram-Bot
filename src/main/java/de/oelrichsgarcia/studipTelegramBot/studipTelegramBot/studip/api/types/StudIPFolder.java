package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.types;

import java.util.ArrayList;

/**
 * The type Folder.
 */
public class StudIPFolder extends StudIPObject {

    private final ArrayList<StudIPObject> childs;

    /**
     * Instantiates a new Folder.
     *
     * @param id     the id
     * @param parent the parent
     */
    public StudIPFolder(String id, StudIPObject parent) {
        super(id, parent);
        this.childs = new ArrayList<>();

    }


    public StudIPFolder() {
        super();
        this.childs = new ArrayList<>();
    }

    /**
     * Add child.
     *
     * @param studIPObject the object
     */
    public void addChild(StudIPObject studIPObject) {
        childs.add(studIPObject);
    }

    /**
     * Gets childs.
     *
     * @return the childs
     */
    public ArrayList<StudIPObject> getChilds() {
        return childs;
    }
}
