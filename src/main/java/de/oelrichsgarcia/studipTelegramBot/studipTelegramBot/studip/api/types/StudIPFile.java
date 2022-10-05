package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.types;

import java.util.Date;

public class StudIPFile extends StudIPObject {
    public StudIPFile(String id, StudIPObject parent) {
        super(id, parent);
    }

    public StudIPFile(String id, String name, Date created, Date updated, StudIPObject parent) {
        super(id, name, created, updated, parent);
    }
}
