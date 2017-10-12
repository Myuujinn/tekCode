package com.myujinn.tekcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class SourceFinder {

    final static Logger LOGGER = LoggerFactory.getLogger(SourceFinder.class);

    static boolean isDirectory(String path) {
        return new File(path).isDirectory();
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        return "";
    }

    static List<Path> findSourceFiles(Path sourcePath) {
        List<Path> pathList = new ArrayList<>();

        final File folder = sourcePath.toFile();
        final File[] filesInFolder = folder.listFiles();

        if (filesInFolder == null) {
            LOGGER.error("Couldn't list folder " + sourcePath.toString());
            return new ArrayList<>();  //returning empty list to not trip out pathList.addAll in case of recursion
        }

        for (File file : filesInFolder) {
            if (file.isDirectory() && '.' != file.getName().charAt(0)) {
                pathList.addAll(findSourceFiles(file.toPath()));
            }
            else if (file.isFile()) {
                if ("c".equals(getFileExtension(file)) || "h".equals(getFileExtension(file)))
                    pathList.add(file.toPath());
                else if (!"".equals(getFileExtension(file)))
                    MistakePrinter.minor("O2 -- " + file.getName() + " is not a source file in your project.");
            }
        }

        return pathList;
    }
}
