package com.epam.rd.autotasks.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class DraftsPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(DraftsPage.class);

    public DraftsPage(WebDriver driver) {
        super(driver);
    }

    public ComposeEmailComponent openDraft(String subject) {
        logger.info("Opening draft with subject: {}", subject);
        WebElement subjectElement = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(), '" + subject + "')]")
        ));

        clickWithJs(subjectElement);

        return new ComposeEmailComponent(driver);
    }

    public boolean isDraftPresent(String subject) {
        logger.debug("Refreshing page and checking presence of draft with subject: {}", subject);
        driver.navigate().refresh();

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + subject + "']")));
        } catch (Exception e) {
            logger.debug("Draft element not immediately found via explicit wait for subject: {}", subject);
        }

        List<WebElement> drafts = driver.findElements(By.xpath("//*[normalize-space(text())='" + subject + "']"));
        boolean isPresent = !drafts.isEmpty() && drafts.get(0).isDisplayed();
        logger.info("Draft present status for '{}': {}", subject, isPresent);
        return isPresent;
    }
}