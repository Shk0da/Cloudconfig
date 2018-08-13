package com.github.shk0da.cloudconfig.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class FileUtil {

    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

    private FileUtil() {
    }

    /**
     * Recursively extract all files from the path
     *
     * @param path the path to the directory
     * @return {@link List} files
     */
    public static List<File> extractFilesFromFolder(String path) {
        File folder = new File(path);
        List<File> files = new ArrayList<>();

        if (!folder.exists() || !folder.isDirectory()) {
            log.debug("[{}] - isn't folder", path);
            return files;
        }

        if (folder.listFiles() == null) {
            log.debug("[{}] - is empty", path);
            return files;
        }

        Arrays.asList(Objects.requireNonNull(folder.listFiles())).forEach(file -> {
            if (!file.isDirectory()) {
                files.add(file);
            } else {
                try {
                    files.addAll(extractFilesFromFolder(file.getPath()));
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });

        return files;
    }

    /**
     * Checks the existence of the directory and if it is not present, creates
     *
     * @param path to the directory
     */
    public static void checkDirAndMake(String path) {
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            if (!dir.mkdirs()) {
                log.error("Failed mkdir [{}]", dir);
            }
        }
    }
}
