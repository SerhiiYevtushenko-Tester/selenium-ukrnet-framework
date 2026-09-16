# Selenium UI Automation Framework (Ukr.net Mailbox)

Automated UI testing framework for Ukr.net mail service built with **Java**, **Selenium WebDriver**, **TestNG**, and **Log4j2**, following the **Page Object Model (POM)** pattern with component-based architecture and CI/CD integration.

---

## 🛠 Tech Stack & Tools
* **Language:** Java 26
* **Automation Tool:** Selenium WebDriver 4.23.0
* **Test Framework:** TestNG 7.10.2
* **Logging:** Log4j2 (Console + Daily Rolling File appenders)
* **Build Tool:** Maven (Surefire plugin for suite management)
* **CI/CD:** Jenkins (`Jenkinsfile`)

---

## 📂 Project Structure
* `src/main/java/.../pages` — Page Objects and Components (`BasePage`, `LoginPage`, `InboxPage`, `DraftsPage`, `SentPage`, `ComposeEmailComponent`)
* `src/main/java/.../models` — Business Objects (`User`, `EmailMessage`)
* `src/main/java/.../driver` — Thread-safe `DriverManager` with `ThreadLocal` support
* `src/main/java/.../utils` — Configuration provider (`ConfigProvider`) supporting multi-environment properties
* `src/test/java/.../listeners` — `TestListener` for automatic failure screenshot capture and logging
* `src/test/resources/` — TestNG XML suites (`smoke.xml`, `regression.xml`) and environment properties (`qa.properties`, `dev.properties`)

---

## ⚙️ Configuration
Before running tests, ensure you have a `secret.properties` file in `src/main/resources/` with your credentials (ignored by Git):
```properties
mail.email=your_email@ukr.net
mail.password=your_password
test.recipient=recipient_email@ukr.net
```

---

## ⚠️ Important Note (UI Language)
The Ukr.net interface language must be set to Ukrainian.
The tests rely on Ukrainian element locators and text selectors (e.g., folder names and buttons). If your account uses a different language, please switch it to Ukrainian in your mailbox settings before running the tests.

---

## 🚀 How to Run Tests

You can execute tests via Maven from the command line with flexible parameters for browser, environment, and test suite.

### Available Parameters:
* `-Dbrowser` — target browser (`chrome`, `firefox`, `edge`). Default: `chrome`
* `-Denv` — test environment (`qa`, `dev`). Default: `qa`
* `-DsuiteXmlFile` — path to TestNG XML suite. Default: `src/test/resources/regression.xml`

### Execution Examples:

1. **Run full Regression suite (default configuration):**
   ```bash
   mvn clean test
   ```

2. **Run Smoke suite on Chrome with QA environment:**
   ```bash
   mvn clean test -Dbrowser=chrome -Denv=qa -DsuiteXmlFile=src/test/resources/smoke.xml
   ```

3. **Run Regression suite on Firefox with DEV environment:**
   ```bash
   mvn clean test -Dbrowser=firefox -Denv=dev -DsuiteXmlFile=src/test/resources/regression.xml
   ```

---

## 📊 Reporting & CI/CD
* **Logs:** Execution logs are automatically generated in `logs/automation.log` with daily rollover.
* **Screenshots on Failure:** If a test fails, `TestListener` captures a screenshot and saves it to `target/screenshots/` with a detailed error log.
* **Jenkins Pipeline:** The repository includes a `Jenkinsfile` supporting parameterized builds (`BROWSER`, `ENV`, `SUITE`), TestNG trend report publishing, and archiving of failure screenshots and logs as build artifacts.