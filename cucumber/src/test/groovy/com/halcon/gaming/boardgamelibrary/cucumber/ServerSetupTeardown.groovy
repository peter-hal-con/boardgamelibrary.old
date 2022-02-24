package com.halcon.gaming.boardgamelibrary.cucumber;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.AfterAll
import io.cucumber.java.Before
import io.cucumber.java.BeforeAll

public class ServerSetupTeardown {
    private static ProcessManager serverProcessManager = new ProcessManager("./gradlew -Dgrails.env=test server:bootRun", new File(".."), {
        try {
            String url = "http://localhost:8080/"
            int rc = new URL(url).openConnection().getResponseCode()
            return rc == 200 || rc == 404
        } catch(IOException e) {
            return false
        }
    })

    @BeforeAll
    public static void startServer() {
        serverProcessManager.start()
    }

    @AfterAll
    public static void stopServer() {
        serverProcessManager.stop()
    }

    @Before
    public void resetServer() {
        assertEquals(200, new URL("http://localhost:8080/testOnly/reset").openConnection().responseCode)
    }
}
