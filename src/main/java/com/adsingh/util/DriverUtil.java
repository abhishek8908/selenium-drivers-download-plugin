package com.adsingh.util;

import com.adsingh.driver.logger.Logger;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.Log;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class DriverUtil extends Logger {

public static Log log= new Logger().getLog();

    public DriverUtil() {
    }

    public static void download(String driverName, String targetDirectory, String version) throws IOException, ConfigurationException {
        String sourceURL = getSourceUrl(driverName);
        String fileName = getFileNameFromUrl(sourceURL);
        String toFile = targetDirectory + File.separator + fileName;
        FileUtils.copyURLToFile(new URL(sourceURL), new File(toFile), 10000, 10000);
        unzipFile(targetDirectory + File.separator + fileName, targetDirectory);
        changeFileName(targetDirectory + File.separator + driverName, targetDirectory + File.separator + driverName + "-" + version);
        cleanDir(targetDirectory);
    }

    private static void unzipFile(String source, String destinationPath) throws IOException {
        if (source.contains(".zip")) {
            Archiver archiver = ArchiverFactory.createArchiver("zip");
            archiver.extract(new File(source), new File(destinationPath));
        } else {
            Archiver archiver = ArchiverFactory.createArchiver("tar", "gz");
            archiver.extract(new File(source), new File(destinationPath));
        }
        log.info("*****Decompressing file "+source);

    }

    private static void changeFileName(String fileName, String fileOut) throws IOException {
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

    private static String getFileNameFromUrl(String url) {
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


    private static void cleanDir(String dir) {
        File folder = new File(dir);
        Arrays.stream(folder.listFiles((f, p) -> p.endsWith(".zip"))).forEach(File::delete);
        Arrays.stream(folder.listFiles((f, p) -> p.endsWith(".tar.gz"))).forEach(File::delete);
        log.info("***** Cleaning Dir:"+dir);
    }


    private static String readProperty(String propertyName) throws ConfigurationException {
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


    private static String getSourceUrl(String browser) throws ConfigurationException {
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

    @Test
    public static void newDownload() throws IOException, ConfigurationException {

        String fromFile = "https://github.com/mozilla/geckodriver/releases/download/v0.20.1/geckodriver-v0.20.1-win32.zip";
        String toFile = "D:/Driver/geckodriver-v0.20.1-win32.zip";
        //connectionTimeout, readTimeout = 10 seconds
        FileUtils.copyURLToFile(new URL(fromFile), new File(toFile), 10000, 10000);


    }

    @Test
    public void testSystemProperty() throws ConfigurationException {
        System.setProperty("ver", "2.39");
        System.setProperty("os", "linux64");
        System.setProperty("ext", "zip");
        System.out.println(readProperty("chromedriver.download.url"));
    }

    @Test
    public void donloadTest() throws IOException, URISyntaxException, ConfigurationException {
        System.setProperty("ver", "2.39");
        System.setProperty("os", "win32");
        System.setProperty("ext", "zip");
        download("chromedriver", "D:\\Driver", "2.39");
    }

    @Test
    public void urlTest() throws IOException, URISyntaxException {

        getFileNameFromUrl("https://chromedriver.storage.googleapis.com/2.39/chromedriver_win32.zip");

    }

    @Test
    public void fileRename() throws IOException, URISyntaxException {

        changeFileName("D:\\Driver\\chromedriver.exe", "D:\\Driver\\chromedriver-" + "2.38" + ".exe");

    }

    @Test
    public void testCleanDir() throws IOException, URISyntaxException {

        cleanDir("D:\\Driver");

    }

    @Test
    public void testDriverExists() throws IOException, URISyntaxException {

        System.out.println(checkDriverVersionExists("chromedriver", "2.38", "D:/Driver"));

    }

    @Test
    public void testProperty() throws IOException, URISyntaxException, ConfigurationException {

        System.out.println(readProperty("chrome.download.url"));
        System.getProperty("os.name");

    }

}
