package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip;

import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.types.StudIPFile;

import java.util.ArrayList;

/**
 * The type File update summary.
 */
public class FileUpdateSummary {

    private final ArrayList<StudIPFile> updatedFiles;
    private final ArrayList<StudIPFile> newFiles;

    /**
     * Instantiates a new File update summary.
     */
    public FileUpdateSummary() {
        updatedFiles = new ArrayList<>();
        newFiles = new ArrayList<>();
    }

    /**
     * Add updated file.
     *
     * @param file the file
     */
    public void addUpdatedFile(StudIPFile file) {
        updatedFiles.add(file);
    }

    /**
     * Add new file.
     *
     * @param file the file
     */
    public void addNewFile(StudIPFile file) {
        newFiles.add(file);
    }

    /**
     * Gets updated files.
     *
     * @return the updated files
     */
    public ArrayList<StudIPFile> getUpdatedFiles() {
        return updatedFiles;
    }

    /**
     * Gets new files.
     *
     * @return the new files
     */
    public ArrayList<StudIPFile> getNewFiles() {
        return newFiles;
    }

}
