import com.intellij.driver.sdk.ui.components.UiComponent.Companion.waitFound
import com.intellij.driver.sdk.ui.components.common.ideFrame
import com.intellij.driver.sdk.ui.components.elements.button
import com.intellij.driver.sdk.ui.components.elements.checkBox
import com.intellij.driver.sdk.ui.components.settings.settingsDialog
import com.intellij.driver.sdk.ui.should
import com.intellij.driver.sdk.waitForIndicators
import com.intellij.ide.starter.driver.engine.runIdeWithDriver
import com.intellij.ide.starter.junit5.hyphenateWithClass
import com.intellij.ide.starter.models.TestCase
import com.intellij.ide.starter.project.GitHubProject
import com.intellij.ide.starter.runner.CurrentTestMethod
import com.intellij.ide.starter.runner.Starter
import com.intellij.ide.starter.sdk.JdkDownloaderFacade.jdk21
import com.intellij.ide.starter.ide.IdeProductProvider
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.awt.event.KeyEvent
import kotlin.time.Duration.Companion.minutes
import com.intellij.openapi.util.SystemInfo
import kotlin.time.Duration.Companion.seconds

class ChangelistsUiTest {
    /**
     * Opens Settings via a keyboard shortcut, navigates to Version Control -> Changelists in the Settings dialog.
     * Enables the "Create changelists automatically" checkbox and saves changes.
     */
    @Test
    fun enableCreateChangelistsAutomatically() {

        val testContext = Starter
            .newContext(
                CurrentTestMethod.hyphenateWithClass(),
                TestCase(
                    IdeProductProvider.IU,
                    GitHubProject.fromGithub(
                        branchName = "main",
                        repoRelativeUrl = "ChickenHin/for-ui-test.git",
                        commitHash = "6f2ef284fdee409aa68de27aefbdd8f5cb1d3f43"
                    )
                )
            )
            .setupSdk(jdk21.toSdk())
            .setLicense(System.getenv("LICENSE_KEY"))
            .prepareProjectCleanImport()

        testContext.runIdeWithDriver().useDriverAndCloseIde {

            ideFrame {
                waitForIndicators(5.minutes)
                // Open Settings via keyboard shortcut (Cmd + , in MacOS; Ctrl + Alt + S in Windows/Linux)
                keyboard {
                    when {
                        SystemInfo.isMac -> {
                            hotKey(KeyEvent.VK_META, KeyEvent.VK_COMMA)
                        }

                        SystemInfo.isWindows || SystemInfo.isLinux -> {
                            hotKey(KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_S)
                        }
                    }
                }

                settingsDialog {
                    settingsTree.waitFound()
                    // In Settings, navigate to Version Control -> Changelists
                    openTreeSettingsSection("Version Control", "Changelists")
                    // In Changelists, ensure checkbox "Create changelists automatically" is selected
                    content {
                        val checkbox = checkBox(
                            "//div[contains(@visible_text, 'Create changelists automatically')]"
                        )
                        checkbox.waitFound()
                        checkbox.check()

                        assertTrue(checkbox.isSelected()) {
                            "\"Create changelists automatically\" checkbox is not selected"
                        }
                    }
                    // After selecting checkbox, click OK to save changes
                    button("OK").click()
                }
            }
        }
    }
}