package com.epam.rd.autotasks.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class InboxPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(InboxPage.class);

    public InboxPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoginSuccessful() {
        logger.debug("Checking if login was successful");
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//a[contains(@href, 'inbox')] | //span[contains(text(), 'Вхідні')] | //button[normalize-space()='Написати листа']")
            ));
            boolean displayed = element.isDisplayed();
            logger.info("Login success status check: {}", displayed);
            return displayed;
        } catch (Exception e) {
            logger.warn("Login success elements not found", e);
            return false;
        }
    }

    public ComposeEmailComponent clickCompose() {
        logger.info("Clicking compose button");
        WebElement composeBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[normalize-space()='Написати листа'] | //a[normalize-space()='Написати листа']")
        ));
        clickWithJs(composeBtn);
        return new ComposeEmailComponent(driver);
    }

    public DraftsPage goToDrafts() {
        logger.info("Navigating to Drafts folder");
        WebElement draftsFolder = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a[href*='draft']")
        ));
        clickWithJs(draftsFolder);
        return new DraftsPage(driver);
    }

    public SentPage goToSent() {
        logger.info("Navigating to Sent folder");
        WebElement sentFolder = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a[href*='sent']")
        ));
        clickWithJs(sentFolder);
        return new SentPage(driver);
    }

    public LoginPage logout() {
        logger.info("Performing user logout");
        driver.manage().deleteAllCookies();
        driver.get("https://accounts.ukr.net/login");
        return new LoginPage(driver);
    }
}