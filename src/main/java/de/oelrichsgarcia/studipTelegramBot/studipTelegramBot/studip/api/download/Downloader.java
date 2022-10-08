package de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.download;

import de.oelrichsgarcia.studipTelegramBot.studipTelegramBot.studip.api.types.StudIPFile;

import java.net.URL;
import java.nio.file.Path;

public interface Downloader {

    void downloadFile(URL url, StudIPFile file, Path path) throws DownloadException;
}
