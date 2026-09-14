package com.epam.rd.autotasks.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class InboxPage extends BasePage {

    public InboxPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoginSuccessful() {
        try {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//a[contains(@href, 'inbox')] | //span[contains(text(), 'Вхідні')] | //button[normalize-space()='Написати листа']")
            ));
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public ComposeEmailComponent clickCompose() {
        WebElement composeBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[normalize-space()='Написати листа'] | //a[normalize-space()='Написати листа']")
        ));
        clickWithJs(composeBtn);
        return new ComposeEmailComponent(driver);
    }

    public DraftsPage goToDrafts() {
        WebElement draftsFolder = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a[href*='draft']")
        ));
        clickWithJs(draftsFolder);
        return new DraftsPage(driver);
    }

    public SentPage goToSent() {
        WebElement sentFolder = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a[href*='sent']")
        ));
        clickWithJs(sentFolder);
        return new SentPage(driver);
    }

    public LoginPage logout() {
        driver.manage().deleteAllCookies();
        driver.get("https://accounts.ukr.net/login");
        return new LoginPage(driver);
    }
}