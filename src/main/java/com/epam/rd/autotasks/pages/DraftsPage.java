package com.epam.rd.autotasks.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class DraftsPage extends BasePage {

    public DraftsPage(WebDriver driver) {
        super(driver);
    }

    public ComposeEmailComponent openDraft(String subject) {
        WebElement subjectElement = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[contains(text(), '" + subject + "')]")
        ));

        clickWithJs(subjectElement);

        return new ComposeEmailComponent(driver);
    }

    public boolean isDraftPresent(String subject) {
        driver.navigate().refresh();

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[normalize-space(text())='" + subject + "']")));
        } catch (Exception e) {}

        List<WebElement> drafts = driver.findElements(By.xpath("//*[normalize-space(text())='" + subject + "']"));
        return !drafts.isEmpty() && drafts.get(0).isDisplayed();
    }
}