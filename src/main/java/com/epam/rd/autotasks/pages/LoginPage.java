package com.epam.rd.autotasks.pages;

import com.epam.rd.autotasks.models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(LoginPage.class);

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

    public InboxPage login(User user) {
        logger.info("Attempting to log in with user: {}", user.getEmail());

        wait.until(ExpectedConditions.visibilityOf(loginInput));
        loginInput.clear();
        loginInput.sendKeys(user.getEmail());
        logger.debug("Login input filled");

        wait.until(ExpectedConditions.visibilityOf(passwordInput));
        passwordInput.clear();
        passwordInput.sendKeys(user.getPassword());
        logger.debug("Password input filled");

        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButton.click();
        logger.info("Submit button clicked, proceeding to Inbox");

        return new InboxPage(driver);
    }

    public boolean isLoginScreenDisplayed() {
        logger.debug("Checking if login screen is displayed");
        try {
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(loginScreenLocator));
            boolean displayed = input.isDisplayed();
            logger.debug("Login screen display status: {}", displayed);
            return displayed;
        } catch (Exception e) {
            logger.debug("Login screen is not displayed");
            return false;
        }
    }
}