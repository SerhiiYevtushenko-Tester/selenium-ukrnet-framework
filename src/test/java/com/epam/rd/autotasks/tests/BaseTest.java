package com.epam.rd.autotasks.tests;

import com.epam.rd.autotasks.driver.DriverManager;
import com.epam.rd.autotasks.utils.ConfigProvider;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseTest {
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
        return DriverManager.getDriver();
    }

    @BeforeClass
    public void setUp() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();

        DriverManager.initDriver(browser);

        getDriver().get(ConfigProvider.getProperty("base.url"));
    }

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }
}