package com.epam.rd.autotasks.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    @FindBy(css = "input[name='login']")
    private WebElement loginInput;

    @FindBy(name = "password")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement submitButton;

    private final By loginScreenLocator = By.xpath("//input[@name='login' or @name='username'] | //form//input[1]");

    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public InboxPage login(String username, String password) {
        wait.until(ExpectedConditions.visibilityOf(loginInput));
        loginInput.clear();
        loginInput.sendKeys(username);

        wait.until(ExpectedConditions.visibilityOf(passwordInput));
        passwordInput.clear();
        passwordInput.sendKeys(password);

        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButton.click();

        return new InboxPage(driver);
    }

    public boolean isLoginScreenDisplayed() {
        try {
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(loginScreenLocator));
            return input.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}