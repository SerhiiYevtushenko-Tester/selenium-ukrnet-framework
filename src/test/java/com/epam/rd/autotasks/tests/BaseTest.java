package com.epam.rd.autotasks.tests;

import com.epam.rd.autotasks.utils.ConfigProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import java.util.Collections;

public class BaseTest {
    public WebDriver driver;
    protected String email;
    protected String password;
    protected String recipient;

    {
        email = ConfigProvider.getProperty("mail.email");
        password = ConfigProvider.getProperty("mail.password");
        recipient = ConfigProvider.getProperty("test.recipient");

        if (recipient == null) {
            recipient = ConfigProvider.getProperty("mail.recipient");
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    @BeforeClass
    public void setUp() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();

        switch (browser) {
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            case "chrome":
            default:
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
                break;
        }

        driver.manage().window().maximize();
        driver.get(ConfigProvider.getProperty("base.url"));
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}