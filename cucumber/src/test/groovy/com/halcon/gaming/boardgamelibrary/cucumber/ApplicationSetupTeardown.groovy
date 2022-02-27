package com.halcon.gaming.boardgamelibrary.cucumber;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.AfterAll
import io.cucumber.java.Before
import io.cucumber.java.BeforeAll

public class ApplicationSetupTeardown {
    private static int responseCode(String path) {
        return new URL("http://localhost:8080${path}").openConnection().responseCode
    }

    private static ProcessManager processManager = new ProcessManager("java -Dgrails.env=test -jar build/server-0.1.jar", new File(".."), {
        try {
            int rc = responseCode("/")
            return rc == 200
        } catch(IOException e) {
            return false
        }
    })

    @BeforeAll
    public static void startServer() {
        processManager.start()
    }

    @AfterAll
    public static void stopServer() {
        processManager.stop()
    }

    @Before
    public void resetServer() {
        assertEquals(200, responseCode("/testOnly/reset"))
    }
}
