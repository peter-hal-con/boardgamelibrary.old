package com.halcon.gaming.boardgamelibrary.cucumber

import static org.awaitility.Awaitility.*
import static org.junit.jupiter.api.Assertions.*

import java.nio.file.Paths
import java.time.Duration

import org.openqa.selenium.By
import org.openqa.selenium.SessionNotCreatedException
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.html5.LocalStorage
import org.openqa.selenium.html5.WebStorage
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

import groovy.json.JsonSlurper
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

class WebStepDefinitions {
    WebDriver webDriver = null

    private final ChromeDriverManager chromeDriverManager = new ChromeDriverManager(Paths.get("build", "chromedriver"))
    private final UserRepository userRepository

    WebStepDefinitions(UserRepository userRepository) {
        this.userRepository = userRepository
    }

    @Before
    public void startWebDriver() {
        chromeDriverManager.installChromeDriver()

        ChromeOptions options = new ChromeOptions()
        options.addArguments("--headless")
        options.addArguments("--no-sandbox")
        while(webDriver == null) {
            try {
                webDriver = new ChromeDriver(options)
            } catch(SessionNotCreatedException e) {
                Thread.sleep(5000)
            }
        }
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10))
        webDriver.manage().window().maximize()
    }

    @After
    public void stopWebDriver() {
        webDriver.close()
    }

    @Given("we have directed the browser to {string}")
    @When("we direct the browser to {string}")
    public void we_direct_the_browser_to(String path) {
        def url = "http://localhost:8080${path}"
        if(path.startsWith("/#/")) {
            await().atMost(Duration.ofSeconds(10)).until {
                if(webDriver.currentUrl != url) {
                    webDriver.get(url)
                    return false
                } else {
                    return true
                }
            }
        } else {
            webDriver.get(url)
        }
    }

    @Given("we are logged in as {string}")
    public void we_are_logged_in_as(String username) {
        we_direct_the_browser_to("/#/")
        we_click_the_element_with_id("open_login")
        we_enter_the_value_into_the_element_with_id(username, "username")
        we_enter_the_value_into_the_element_with_id(userRepository.userPassword(username), "password")
        we_click_the_element_with_id("submit_login")
    }

    @When("we click the element with id {string}")
    public void we_click_the_element_with_id(String id) {
        new WebDriverWait(webDriver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.id(id))).click();
    }

    @When("we enter the value {string} into the element with id {string}")
    public void we_enter_the_value_into_the_element_with_id(String value, String id) {
        for(;;) {
            try {
                def element = webDriver.findElement(By.id(id))
                element.clear()
                element.sendKeys(value)
                return
            } catch(StaleElementReferenceException e) {
            }
        }
    }

    @When("we click the element with xpath {string}")
    public void we_click_the_element_with_xpath(String xpath) {
        new WebDriverWait(webDriver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
    }

    @Then("we will see an element with id {string}")
    public void we_will_see_an_element_with_id(String id) {
        await().atMost(Duration.ofSeconds(5)).until {
            webDriver.findElements(By.id(id)).size() == 1
        }
    }

    @Then("we will not see an element with id {string}")
    public void we_will_not_see_an_element_with_id(String id) {
        await().atMost(Duration.ofMinutes(1)).until {
            webDriver.findElements(By.id(id)).empty
        }
    }

    @Then("we will be logged in as {string}")
    public void we_will_be_logged_in_as(String username) {
        LocalStorage localStorage
        await().atMost(Duration.ofSeconds(5)).until {
            localStorage = ((WebStorage)webDriver).getLocalStorage()
            localStorage.getItem("auth") != null
        }
        assertEquals(username, new JsonSlurper().parseText(localStorage.getItem("auth")).username)
    }

    @Then("we will not be logged in")
    public void we_will_not_be_logged_in() {
        await().atMost(Duration.ofSeconds(5)).until {
            LocalStorage localStorage = ((WebStorage)webDriver).getLocalStorage()
            localStorage.getItem("auth") == null
        }
    }

    @Then("the current URL will be {string}")
    public void the_current_url_will_be(String url) {
        await().atMost(Duration.ofSeconds(5)).until {
            webDriver.currentUrl == "http://localhost:8080${url}"
        }
    }

    @Then("the element with id {string} will be disabled")
    public void the_element_with_id_will_be_disabled(String id) {
        assertFalse(webDriver.findElement(By.id(id)).enabled)
    }

    @Then("the element with id {string} will be empty")
    public void the_element_with_id_will_be_empty(String id) {
        await().atMost(Duration.ofSeconds(5)).until {
            "" == webDriver.findElement(By.id(id)).getAttribute("value")
        }
    }

    @Then("the element with id {string} will not be empty")
    public void the_element_with_id_will_not_be_empty(String id) {
        await().atMost(Duration.ofSeconds(5)).until {
            "" != webDriver.findElement(By.id(id)).getAttribute("value")
        }
    }

    @Then("the element with id {string} will be unchecked")
    public void the_element_with_id_will_be_unchecked(String id) {
        await().atMost(Duration.ofSeconds(5)).until {
            !webDriver.findElement(By.id(id)).isSelected()
        }
    }

    @Then("the element with xpath {string} will contain {string}")
    public void the_element_with_xpath_will_contain(String xpath, String content) {
        await().atMost(Duration.ofSeconds(5)).until {
            webDriver.findElement(By.xpath(xpath)).getText() == content
        }
    }

    @Then("the element with xpath {string} will be empty")
    public void the_element_with_xpath_will_be_empty(String xpath) {
        await().atMost(Duration.ofSeconds(5)).until {
            webDriver.findElement(By.xpath(xpath)).getText() == ""
        }
    }

    @Then("we will see an element with xpath {string}")
    public void we_will_see_an_element_with_xpath(String xpath) {
        await().atMost(Duration.ofSeconds(5)).until {
            webDriver.findElements(By.xpath(xpath)).size() == 1
        }
    }

    @Then("the element with id {string} will contain {string}")
    public void the_element_with_id_will_contain(String id, String content) {
        await().atMost(Duration.ofSeconds(5)).until {
            content == webDriver.findElement(By.id(id)).getAttribute("value")
        }
    }

    @Then("the element with id {string} will be checked")
    public void the_element_with_id_will_be_checked(String id) {
        await().atMost(Duration.ofSeconds(5)).until {
            webDriver.findElement(By.id(id)).isSelected()
        }
    }
}
