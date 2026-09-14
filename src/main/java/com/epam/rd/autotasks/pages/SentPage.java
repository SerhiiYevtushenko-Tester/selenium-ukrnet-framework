package com.epam.rd.autotasks.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SentPage extends BasePage {

    public SentPage(WebDriver driver) {
        super(driver);
    }

    public boolean isMailPresent(String subject) {
        try {
            WebElement sentItem = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[contains(text(), '" + subject + "') or contains(@title, '" + subject + "')] | //span[contains(text(), '" + subject + "')]")
            ));
            return sentItem.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}