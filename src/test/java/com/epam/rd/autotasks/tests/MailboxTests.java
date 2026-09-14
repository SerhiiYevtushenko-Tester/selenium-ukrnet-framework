package com.epam.rd.autotasks.tests;

import com.epam.rd.autotasks.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MailboxTests extends BaseTest {
    private InboxPage inboxPage;
    private DraftsPage draftsPage;
    private SentPage sentPage;
    private ComposeEmailComponent composeMail;

    private final String uniqueId = String.valueOf(System.currentTimeMillis());
    private final String subject = "Thoughts on The Way of Kings " + uniqueId;
    private final String body = "I highly recommend starting The Way of Kings!";

    @Test(priority = 1)
    public void testCreateDraft() {
        LoginPage loginPage = new LoginPage(driver);
        inboxPage = loginPage.login(email, password);

        Assert.assertTrue(inboxPage.isLoginSuccessful(), "Login failed!");

        composeMail = inboxPage.clickCompose();
        composeMail.fillEmailDetails(recipient, subject, body);
        composeMail.saveAsDraft();

        draftsPage = inboxPage.goToDrafts();
        Assert.assertTrue(draftsPage.isDraftPresent(subject), "Draft was not found in Drafts folder!");

        composeMail = draftsPage.openDraft(subject);

        Assert.assertEquals(composeMail.getRecipientsValue(), recipient, "Draft recipient does not match!");
        Assert.assertEquals(composeMail.getSubjectValue(), subject, "Draft subject does not match!");
        Assert.assertEquals(composeMail.getBodyValue(), body, "Draft body does not match!");

        composeMail.saveAsDraft();
    }

    @Test(priority = 2, dependsOnMethods = "testCreateDraft")
    public void testSendDraft() {
        draftsPage = inboxPage.goToDrafts();
        composeMail = draftsPage.openDraft(subject);

        Assert.assertEquals(composeMail.getSubjectValue(), subject, "Subject mismatch before sending!");

        composeMail.sendEmail();

        draftsPage = inboxPage.goToDrafts();
        Assert.assertFalse(draftsPage.isDraftPresent(subject), "Mail is still present in Drafts after sending!");
    }

    @Test(priority = 3, dependsOnMethods = "testSendDraft")
    public void testVerifySentAndLogout() {
        sentPage = inboxPage.goToSent();
        Assert.assertTrue(sentPage.isMailPresent(subject), "Mail is not present in Sent folder!");

        LoginPage loginPage = inboxPage.logout();
        Assert.assertTrue(loginPage.isLoginScreenDisplayed(), "Logout was not successful, login form is not visible!");
    }
}