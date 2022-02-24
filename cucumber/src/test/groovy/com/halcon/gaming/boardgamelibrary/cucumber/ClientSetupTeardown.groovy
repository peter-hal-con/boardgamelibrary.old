package com.halcon.gaming.boardgamelibrary.cucumber;

import io.cucumber.java.AfterAll
import io.cucumber.java.BeforeAll

public class ClientSetupTeardown {
    private static ProcessManager clientProcessManager = new ProcessManager("./gradlew client:bootRun", new File(".."), {
        try {
            String url = "http://localhost:3000/"
            int rc = new URL(url).openConnection().getResponseCode()
            return rc == 200
        } catch(IOException e) {
            return false
        }
    })

    @BeforeAll
    public static void startClient() {
        clientProcessManager.start()
    }

    @AfterAll
    public static void stopClient() {
        clientProcessManager.stop()
    }
}
