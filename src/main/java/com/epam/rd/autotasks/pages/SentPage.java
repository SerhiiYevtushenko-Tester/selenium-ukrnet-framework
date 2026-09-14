package com.epam.rd.autotasks.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SentPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(SentPage.class);

    public SentPage(WebDriver driver) {
        super(driver);
    }

    public boolean isMailPresent(String subject) {
        logger.debug("Checking presence of sent email with subject: {}", subject);
        try {
            WebElement sentItem = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[contains(text(), '" + subject + "') or contains(@title, '" + subject + "')] | //span[contains(text(), '" + subject + "')]")
            ));
            boolean isDisplayed = sentItem.isDisplayed();
            logger.info("Sent email presence status for '{}': {}", subject, isDisplayed);
            return isDisplayed;
        } catch (Exception e) {
            logger.debug("Sent email with subject '{}' was not found or not displayed", subject);
            return false;
        }
    }
}