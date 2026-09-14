package com.epam.rd.autotasks.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import java.io.InputStream;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class BaseTest {
    protected WebDriver driver;
    protected String email;
    protected String password;
    protected String recipient;

    {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("secret.properties")) {
            if (input != null) {
                props.load(input);
                email = props.getProperty("mail.email");
                password = props.getProperty("mail.password");
                recipient = props.getProperty("mail.recipient");
            } else {
                throw new RuntimeException("secret.properties not found in resources folder!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read configuration files: " + e.getMessage(), e);
        }
    }

    @BeforeClass
    public void setUp() {
        ChromeOptions options = new ChromeOptions();

        java.util.Map<String, Object> prefs = new java.util.HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);

        driver = new ChromeDriver(options);

        // Experiment with implicit wait combined with explicit waits for better test stability
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

        driver.manage().window().maximize();
        driver.get("https://accounts.ukr.net/login");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}