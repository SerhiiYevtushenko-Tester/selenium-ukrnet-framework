package com.epam.rd.autotasks.pages;

import com.epam.rd.autotasks.models.EmailMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ComposeEmailComponent extends BasePage {
    private static final Logger logger = LogManager.getLogger(ComposeEmailComponent.class);

    private final By subjectInputLocator = By.xpath("//label[contains(text(), 'Тема')]//following::input[1] | //input[@name='subject']");
    private final By recipientLocator = By.xpath("//input[@id='compose-to'] | //input[contains(@placeholder, 'Кому')]");

    public ComposeEmailComponent(WebDriver driver) {
        super(driver);
    }

    public void fillEmailDetails(EmailMessage message) {
        logger.info("Filling email details. Recipient: {}, Subject: {}", message.getTo(), message.getSubject());
        WebElement toInput = wait.until(ExpectedConditions.elementToBeClickable(recipientLocator));
        toInput.clear();
        toInput.sendKeys(message.getTo(), Keys.ENTER);
        logger.debug("Recipient input populated");

        WebElement subjectInput = wait.until(ExpectedConditions.elementToBeClickable(subjectInputLocator));
        subjectInput.clear();
        subjectInput.sendKeys(message.getSubject());
        logger.debug("Subject input populated");

        fillEmailBody(message.getBody());
    }

    private void fillEmailBody(String body) {
        logger.debug("Filling email body content");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        wait.until(driverInstance -> js.executeScript("return typeof tinymce !== 'undefined'").equals(true));

        Boolean isTinymceSet = (Boolean) js.executeScript(
                "if (typeof tinymce !== 'undefined' && tinymce.activeEditor) {" +
                        "    tinymce.activeEditor.setContent(arguments[0]);" +
                        "    tinymce.activeEditor.save();" +
                        "    return true;" +
                        "} return false;",
                body
        );

        if (Boolean.TRUE.equals(isTinymceSet)) {
            logger.debug("Email body populated via TinyMCE editor");
            return;
        }

        try {
            logger.debug("TinyMCE not active, attempting iframe fallback for body");
            WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//iframe[contains(@id, 'mce') or contains(@class, 'tox')]")
            ));
            driver.switchTo().frame(iframe);
            js.executeScript("document.body.innerHTML = '<p>' + arguments[0] + '</p>';", body);
            logger.debug("Email body populated via iframe");
        } catch (Exception e) {
            logger.debug("Iframe not found, using contenteditable fallback for body");
            WebElement fallbackBody = driver.findElement(By.cssSelector("div[contenteditable='true']"));
            fallbackBody.sendKeys(body);
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    public void saveAsDraft() {
        logger.info("Saving email as draft");
        try {
            WebElement closeBtn = driver.findElement(By.cssSelector("div.compose button.close, button[aria-label='Close']"));
            clickWithJs(closeBtn);
            logger.debug("Draft close button clicked");
        } catch (Exception e) {
            try {
                WebElement draftsLink = driver.findElement(By.cssSelector("a[href*='drafts']"));
                clickWithJs(draftsLink);
                logger.debug("Drafts link clicked as fallback");
            } catch (Exception ignored) {}
        }

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'compose')]")));
            logger.debug("Compose window closed/hidden");
        } catch (Exception ignored) {}
    }

    public void sendEmail() {
        logger.info("Sending email");
        try {
            WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@data-name='send'] | //button[contains(@class, 'send')] | //button[contains(@class, 'primary')] | //div[contains(@class, 'compose')]//button[@type='submit']")
            ));
            clickWithJs(sendButton);
            logger.debug("Send button clicked via standard locator");
        } catch (Exception e) {
            logger.warn("Standard send button click failed, executing JS fallback", e);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "let buttons = Array.from(document.querySelectorAll('button'));" +
                            "let sendBtn = buttons.find(b => b.textContent.includes('Надіслати') || b.getAttribute('data-name') === 'send' || b.className.includes('send') || b.className.includes('primary') || b.type === 'submit');" +
                            "if (sendBtn) { sendBtn.click(); }" +
                            "else { throw new Error('Send button not found via JS fallback'); }"
            );
        }

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'compose')]")));
        } catch (Exception ignored) {}

        driver.navigate().refresh();
        logger.debug("Page refreshed after sending email");
    }

    public String getRecipientsValue() {
        logger.debug("Retrieving recipient value from compose form");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String recipientValue = (String) js.executeScript(
                "let input = document.getElementById('compose-to');" +
                        "if (input && input.value && input.value.trim() !== '') return input.value.trim();" +
                        "let elements = document.querySelectorAll('body *');" +
                        "for (let el of elements) {" +
                        "    let text = el.textContent ? el.textContent.trim() : '';" +
                        "    if (text.includes('@') && text.length < 50 && !text.includes(' ') && el.children.length === 0) {" +
                        "        return text;" +
                        "    }" +
                        "}" +
                        "return '';"
        );

        if (recipientValue != null && !recipientValue.isEmpty()) {
            return recipientValue;
        }

        logger.error("Could not retrieve recipient value from the compose form!");
        throw new RuntimeException("Could not retrieve recipient value from the compose form!");
    }

    public String getSubjectValue() {
        logger.debug("Retrieving subject value");
        WebElement subjectInput = wait.until(ExpectedConditions.presenceOfElementLocated(subjectInputLocator));
        return subjectInput.getAttribute("value");
    }

    public String getBodyValue() {
        logger.debug("Retrieving email body value");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String content = (String) js.executeScript(
                "if (typeof tinymce !== 'undefined' && tinymce.activeEditor) {" +
                        "    return tinymce.activeEditor.getContent({format : 'text'});" +
                        "}" +
                        "let editor = document.querySelector('div[contenteditable=\"true\"]');" +
                        "return editor ? editor.innerText : '';"
        );
        return content != null ? content.trim() : "";
    }
}