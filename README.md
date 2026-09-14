# Selenium Mailbox Automation Task

Automated UI tests for Ukr.net mailbox functionality implemented using Java, Selenium WebDriver, TestNG, and the Page Object Model (POM) design pattern.

## Prerequisites

* **Java Development Kit (JDK)**: Version 17 or higher.
* **Maven**: For dependency management and building the project.
* **Browser**: Google Chrome installed.

## Important Note (UI Language)
⚠️ **The Ukr.net interface language must be set to Ukrainian.**
The tests rely on Ukrainian element locators and text selectors (e.g., folder names and buttons). If your account uses a different language, please switch it to Ukrainian in your mailbox settings before running the tests.

## Configuration & Security

To protect sensitive credentials, authentication data is excluded from version control.

1. Go to the `src/main/resources` folder.
2. Create a new file named `secret.properties` (based on the provided `secret.properties.example` template).
3. Fill in your actual Ukr.net credentials and recipient email inside `secret.properties`:

```properties
mail.email=your_actual_username  
mail.password=your_actual_password  
mail.recipient=recipient_email@ukr.net  
```

*(Note: `secret.properties` is ignored by Git to prevent exposing credentials).*

## Wait Strategy

This project combines implicit and explicit waits to ensure stability while handling Ukr.net's dynamic UI:

- **Implicit Wait:** A baseline timeout (e.g., 3 seconds) configured in `BaseTest.java` to act as a global safety net for element searches.
- **Explicit Wait:** Used extensively across Page Objects (`WebDriverWait` with `ExpectedConditions`) to handle dynamic components, such as waiting for the TinyMCE editor to initialize, inputs to become clickable, or elements to disappear.

## Running Tests

You can run the tests via Maven from your terminal:

```bash
mvn clean test 
```

Or directly run `MailboxTests.java` via your IDE (IntelliJ IDEA).