package com.github.abhishek8908.util;

import com.github.abhishek8908.driver.logger.Logger;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.Log;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class DriverUtil extends Logger {

    private static Log log = new Logger().getLog();

    public DriverUtil() {
    }

    public static void download(String driverName, String targetDirectory, String version) throws IOException, ConfigurationException {
        String sourceURL = getSourceUrl(driverName);
        String fileName = getFileNameFromUrl(sourceURL);
        //     checkDriverDirExists(targetDirectory);
        String toFile = targetDirectory + File.separator + fileName;
        FileUtils.copyURLToFile(new URL(sourceURL), new File(toFile), 10000, 10000);
        unzipFile(targetDirectory + File.separator + fileName, targetDirectory);
        changeFileName(targetDirectory + File.separator + driverName, targetDirectory + File.separator + driverName + "-" + version);
        cleanDir(targetDirectory);
    }

    public static void unzipFile(String source, String destinationPath) throws IOException {
        if (source.contains(".zip")) {
            Archiver archiver = ArchiverFactory.createArchiver("zip");
            archiver.extract(new File(source), new File(destinationPath));
        } else {
            Archiver archiver = ArchiverFactory.createArchiver("tar", "gz");
            archiver.extract(new File(source), new File(destinationPath));
        }
        log.info("*****Decompressing file "+source);

    }

    public static void changeFileName(String fileName, String fileOut) throws IOException {
        String os = System.getProperty("os");
        String ext = "";
        if (os.toLowerCase().contains("win")) {
            ext = ".exe";
        }
        FileUtils.moveFile(
                FileUtils.getFile(fileName + ext),
                FileUtils.getFile(fileOut + "-" + os + ext));
        log.info("***** File Name :"+fileName+"changed to"+fileOut);

    }

    public static String getFileNameFromUrl(String url) {
        String[] newUrl = url.split("/");
        log.info("***** File Name :"+newUrl[newUrl.length - 1]);
        return newUrl[newUrl.length - 1];


    }

    public static boolean checkDriverVersionExists(String driverName, String version, String dir) throws IOException {
        boolean fileExists = false;
        String os = System.getProperty("os");

        final DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir), driverName + "-" + version + "-" + os + "{,.*}");
        for (final Path entry : stream) {
            if (!entry.toString().isEmpty()) {
                fileExists = true;
            }
        }
        return fileExists;
    }


    public static void cleanDir(String dir) {
        File folder = new File(dir);
        Arrays.stream(folder.listFiles((f, p) -> p.endsWith(".zip"))).forEach(File::delete);
        Arrays.stream(folder.listFiles((f, p) -> p.endsWith(".tar.gz"))).forEach(File::delete);
        log.info("***** Cleaning Dir:"+dir);
    }


    public static String readProperty(String propertyName) throws ConfigurationException {
        String resourcePath = "";
        try {
            resourcePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        } catch (NullPointerException e) {
            System.out.println(resourcePath + e.getMessage());
        }
        Configuration config = new PropertiesConfiguration(resourcePath + "driver.properties");
        Configuration extConfig = ((PropertiesConfiguration) config).interpolatedConfiguration();
        return extConfig.getProperty(propertyName).toString();

    }


    public static String getSourceUrl(String browser) throws ConfigurationException {
        if (browser.toLowerCase().contains("chromedriver")) {
            return readProperty("chromedriver.download.url");
        } else if (browser.toLowerCase().contains("geckodriver")) {
            return readProperty("geckodriver.download.url");
        } else if (browser.toLowerCase().contains("safaridriver")) {
            return readProperty("safaridriver.download.url");
        } else {
            return readProperty("edgedriver.download.url");
        }
    }

    public static String getExtensionOfDriver(String os) {

        if (os.contains("win")) {
            return ".exe";
        } else {
            return "";
        }

    }

    public static void checkDriverDirExists(String dirName) throws IOException {

        File directory = new File(dirName);
        if (!directory.exists()) {
            log.info("Driver directory does not exists !! Creating...");
            FileUtils.forceMkdir(directory);
        }

    }


}
