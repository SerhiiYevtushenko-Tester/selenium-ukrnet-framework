# Selenium Mailbox Automation Framework

Automated UI testing framework for the Ukr.net mail service
built using Java, Selenium WebDriver, TestNG, and the Page Object Model (POM).

## Tech Stack & Dependencies

* **Language**: Java (JDK 17+, compatible with JDK 26)
* **Automation Tool**: Selenium WebDriver (v4.23.0)
* **Testing Framework**: TestNG (v7.10.2)
* **Logging**: Apache Log4j2 (v2.23.1)
* **Build Tool**: Maven

## Key Features & Architecture

1. Page Object Model (POM) & Component-Based Structure:
   Separation of pages (`LoginPage`, `InboxPage`, `DraftsPage`, `SentPage`)
   and components (`ComposeEmailComponent`).
2. Cross-Browser Support: Dynamic driver initialization in `BaseTest`
   supporting Chrome, Firefox, and Edge via execution parameters.
3. Environment Management: Multi-environment configuration support
   (`qa.properties`, `dev.properties`) handled via `ConfigProvider`.
4. Advanced Logging (Log4j2): Detailed execution logs output
   to console and daily rotating log files (`logs/automation.log`).
5. Robust Failure Management: Custom `TestListener` that captures
   screenshots (`target/screenshots/`) automatically on test failure.
6. Robust Wait Strategies: Precise `WebDriverWait` combined with
   resilient JavaScript fallbacks for dynamic elements.

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
mail.recipient=your_recipient@ukr.net
```

*(Note: `secret.properties` is ignored by Git).*

## Running Tests

Execute tests via Maven terminal:
```bash
mvn clean test 
```

### Advanced Execution Parameters (VM Options)

* Specify Browser: `-Dbrowser=firefox` or `-Dbrowser=edge` (defaults to chrome)
* Specify Environment: `-Denv=dev` (defaults to qa)

Example command:
```bash
mvn test -Dbrowser=chrome -Denv=qa
```