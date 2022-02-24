package com.halcon.gaming.boardgamelibrary.cucumber

import static org.awaitility.Awaitility.*

import java.nio.file.Paths
import java.time.Duration

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class WebStepDefinitions {
    WebDriver webDriver

    private final ChromeDriverManager chromeDriverManager = new ChromeDriverManager(Paths.get("build", "chromedriver"))

    @Before
    public void startWebDriver() {
        chromeDriverManager.installChromeDriver()

        ChromeOptions options = new ChromeOptions()
        options.addArguments("--headless")
        options.addArguments("--no-sandbox")
        webDriver = new ChromeDriver(options)
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10))
        webDriver.manage().window().maximize()
    }

    @After
    public void stopWebDriver() {
        webDriver.close()
    }

    @When("we direct the browser to {string}")
    public void we_direct_the_browser_to(String url) {
        webDriver.get(url)
    }

    @Then("we will see an element with an id of {string}")
    public void we_will_see_an_element_with_an_id_of(String id) {
        await().atMost(Duration.ofSeconds(5)).until {
            webDriver.findElements(By.id(id)).size() == 1
        }
    }
}
