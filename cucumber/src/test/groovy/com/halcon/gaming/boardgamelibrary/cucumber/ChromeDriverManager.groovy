package com.halcon.gaming.boardgamelibrary.cucumber

import static org.junit.jupiter.api.Assertions.*

import java.nio.file.Path

class ChromeDriverManager {
    private final Path chromeDriverDirectory
    private final boolean log
    private final String chromeVersion

    public ChromeDriverManager(Path chromeDriverDirectory, boolean log = false) {
        this.chromeDriverDirectory = chromeDriverDirectory.toAbsolutePath()
        this.log = log
        if(log) println("Chrome Driver Directory ${this.chromeDriverDirectory}")
        if(log) println("Determining chrome version")
        this.chromeVersion = executeCommand("google-chrome --version").tokenize(" ")[2]
        if(log) println("Chrome version: ${chromeVersion}")
    }

    private String versionTrim(String version, int count) {
        return version.tokenize(".").subList(0, count).join(".")
    }

    private String executeCommand(String cmdline) {
        StringBuilder sout = new StringBuilder()
        StringBuilder serr = new StringBuilder()
        if(log) println("Executing: ${cmdline}")
        Process proc = cmdline.execute()
        proc.waitForProcessOutput(sout, serr)
        int exitValue = proc.exitValue()
        if(log) println("Exit value: ${exitValue}")
        assertEquals(0, exitValue)
        if(log) println("Stdout: ${sout.toString().trim()}")
        return sout
    }

    private boolean isChromeDriverPresent() {
        File chromeDriver = chromeDriverDirectory.resolve("chromedriver").toFile()
        return chromeDriver.exists()
    }

    private String getChromeDriverVersion() {
        String chromeDriverVersion
        if(log) println("Determining chrome driver version")
        if(chromeDriverPresent) {
            if(log) println("Chrome driver present")
            chromeDriverVersion = executeCommand("${chromeDriverDirectory}/chromedriver --version").tokenize(" ")[1]
            if(log) println("Chrome driver version: ${chromeDriverVersion}")
        } else {
            if(log) println("Chrome driver absent")
            chromeDriverVersion = executeCommand("curl https://chromedriver.storage.googleapis.com/LATEST_RELEASE_${versionTrim(chromeVersion, 3)}").trim()
        }
        if(log) println("Chrome driver version: ${chromeDriverVersion}")
        return chromeDriverVersion
    }

    private void removeExistingChromeDriver() {
        if(log) println("Removing existing chrome driver")
        chromeDriverDirectory.deleteDir()
        chromeDriverDirectory.toFile().mkdirs()
    }

    private void fetchChromeDriverZipFile() {
        if(log) println("Fetching new chrome driver zip file")
        executeCommand("wget -N -P ${chromeDriverDirectory} https://chromedriver.storage.googleapis.com/${chromeDriverVersion}/chromedriver_linux64.zip")
    }

    private void unzipChromeDriverZipFile() {
        if(log) println("Unzipping new chrome driver zip file")
        executeCommand("unzip -d ${chromeDriverDirectory} ${chromeDriverDirectory}/chromedriver_linux64.zip")
    }

    public void installChromeDriver() {
        if(versionTrim(chromeVersion, 3) != versionTrim(chromeDriverVersion, 3)) {
            removeExistingChromeDriver()
        }

        if(!chromeDriverPresent) {
            fetchChromeDriverZipFile()
            unzipChromeDriverZipFile()
        }

        System.setProperty("webdriver.chrome.driver", chromeDriverDirectory.resolve("chromedriver").toFile().toString())
    }
}
