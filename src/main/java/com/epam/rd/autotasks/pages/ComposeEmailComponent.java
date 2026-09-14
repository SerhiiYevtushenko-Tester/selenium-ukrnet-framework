package com.epam.rd.autotasks.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ComposeEmailComponent extends BasePage {

    private final By subjectInputLocator = By.xpath("//label[contains(text(), 'Тема')]//following::input[1] | //input[@name='subject']");
    private final By recipientLocator = By.xpath("//input[@id='compose-to'] | //input[contains(@placeholder, 'Кому')]");

    public ComposeEmailComponent(WebDriver driver) {
        super(driver);
    }

    public void fillEmailDetails(String to, String subject, String body) {
        WebElement toInput = wait.until(ExpectedConditions.elementToBeClickable(recipientLocator));
        toInput.clear();
        toInput.sendKeys(to, Keys.ENTER);

        WebElement subjectInput = wait.until(ExpectedConditions.elementToBeClickable(subjectInputLocator));
        subjectInput.clear();
        subjectInput.sendKeys(subject);

        fillEmailBody(body);
    }

    private void fillEmailBody(String body) {
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
            return;
        }

        try {
            WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//iframe[contains(@id, 'mce') or contains(@class, 'tox')]")
            ));
            driver.switchTo().frame(iframe);
            js.executeScript("document.body.innerHTML = '<p>' + arguments[0] + '</p>';", body);
        } catch (Exception e) {
            WebElement fallbackBody = driver.findElement(By.cssSelector("div[contenteditable='true']"));
            fallbackBody.sendKeys(body);
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    public void saveAsDraft() {
        try {
            WebElement closeBtn = driver.findElement(By.cssSelector("div.compose button.close, button[aria-label='Close']"));
            clickWithJs(closeBtn);
        } catch (Exception e) {
            try {
                WebElement draftsLink = driver.findElement(By.cssSelector("a[href*='drafts']"));
                clickWithJs(draftsLink);
            } catch (Exception ignored) {}
        }

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class, 'compose')]")));
        } catch (Exception ignored) {}
    }

    public void sendEmail() {
        try {
            WebElement sendButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@data-name='send'] | //button[contains(@class, 'send')] | //button[contains(@class, 'primary')] | //div[contains(@class, 'compose')]//button[@type='submit']")
            ));
            clickWithJs(sendButton);
        } catch (Exception e) {
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
    }

    public String getRecipientsValue() {
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

        throw new RuntimeException("Could not retrieve recipient value from the compose form!");
    }

    public String getSubjectValue() {
        WebElement subjectInput = wait.until(ExpectedConditions.presenceOfElementLocated(subjectInputLocator));
        return subjectInput.getAttribute("value");
    }

    public String getBodyValue() {
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