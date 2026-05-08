# JetBrains SDET Task 1

UI automation test project built with:

- Kotlin
- JUnit 5
- Gradle
- [IntelliJ IDE Starter](https://github.com/JetBrains/intellij-ide-starter)
- [IntelliJ Driver SDK](https://github.com/JetBrains/intellij-community/tree/master/platform/remote-driver)

Following the style of [`UiTestWithDriver`](https://github.com/JetBrains/intellij-ide-starter/blob/master/intellij.tools.ide.starter.examples/testSrc/com/intellij/ide/starter/examples/driver/UiTestWithDriver.kt) example

## Test Scenario

The automated UI test [`ChangelistsUiTest`](testSrc/ChangelistsUiTest.kt) performs the following steps:

1. Download and start IntelliJ IDEA Ultimate
2. Open a publicly available repository [`for-ui-test`](https://github.com/ChickenHin/for-ui-test) from GitHub
3. Open **Settings** via keyboard shortcut (`Ctrl+Alt+S` / `⌘,`)
4. Navigate to **Version Control → Changelists**
5. Check the **Create changelists automatically** checkbox
6. Assert the checkbox is selected
7. Click the **OK** button to close the dialog and save the change

## Locate UI Components
The test uses [IntelliJ Driver SDK](https://github.com/JetBrains/intellij-community/tree/master/platform/remote-driver) component wrappers and locators to interact with the IDE UI

### Main IDE Window
`ideFrame` locates the main IntelliJ IDEA window and ensures all subsequent UI operations are performed within that window context

### Open Settings
The Settings dialog is opened directly with the keyboard shortcut `keyboard { hotKey(...) }`

The shortcut differs between operating systems, so the test handles both macOS and Windows/Linux using `SystemInfo`:

- macOS: `Cmd + ,`
- Windows/Linux: `Ctrl + Alt + S`

### Setting Dialog
`settingsDialog` locates the IntelliJ Settings dialog using the Driver SDK Settings dialog wrapper.

This provides Settings-specific methods:

- `openTreeSettingsSection(...)`
    - Locates the Settings category tree on the left side of the dialog
    - Navigates to a specific path
- `content { ... }`
    - Locates the right-hand content panel for the currently selected Settings section

### Checkbox
The checkbox is located inside the Settings content panel using an XPath query based on visible text

### Button
`button(text: String)` locates button with its visible text of "OK"

## Prerequisites
- JDK 21
- Gradle 8+
- macOS / Windows

## Running Tests
### From the command line

```bash
./gradlew clean test
```

### From IntelliJ IDEA

1. Open the project in IntelliJ IDEA.
2. Wait for Gradle to import the project and resolve dependencies.
3. Open `ChangelistsUiTest.kt` and click the gutter icon next to the `@Test` method.

## CI
GitHub Actions workflow is configured to run UI tests automatically on:
- macOS
- Windows

after `pushes` to the main branch.
