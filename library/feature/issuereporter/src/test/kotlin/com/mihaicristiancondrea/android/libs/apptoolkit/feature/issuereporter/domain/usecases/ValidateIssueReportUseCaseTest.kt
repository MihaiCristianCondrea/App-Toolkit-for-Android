/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.usecases

import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportFieldError
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.issuereporter.domain.models.IssueReportValidation
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ValidateIssueReportUseCaseTest {

    private val validateReport = ValidateIssueReportUseCase()

    private fun validate(
        title: String = VALID_TITLE,
        description: String = VALID_DESCRIPTION,
        email: String = VALID_EMAIL,
    ): IssueReportValidation = validateReport(title = title, description = description, email = email)

    @Test
    fun `a real report passes`() {
        val validation = validate()

        assertTrue(validation.isValid)
        assertEquals(IssueReportValidation(), validation)
    }

    /** The shape of the garbage reports filed as #714 and #715: a greeting and a mistyped email. */
    @Test
    fun `a short unrelated report is refused on every field`() {
        val validation = validate(
            title = "Hello",
            description = "Good app",
            email = "achrafachrafachref 123@gmail.com",
        )

        assertFalse(validation.isValid)
        assertEquals(IssueReportFieldError.TooShort(minimumLength = 10), validation.titleError)
        assertEquals(IssueReportFieldError.TooShort(minimumLength = 40), validation.descriptionError)
        assertEquals(IssueReportFieldError.InvalidEmail, validation.emailError)
    }

    @Test
    fun `an empty or whitespace report is missing every field`() {
        val validation = validate(title = "   ", description = "\n\t ", email = " ")

        assertEquals(IssueReportFieldError.Missing, validation.titleError)
        assertEquals(IssueReportFieldError.Missing, validation.descriptionError)
        assertEquals(IssueReportFieldError.Missing, validation.emailError)
    }

    @Test
    fun `the email is required`() {
        assertEquals(IssueReportFieldError.Missing, validate(email = "").emailError)
    }

    @Test
    fun `malformed emails are refused`() {
        listOf(
            "achrafachrafachref 123@gmail.com",
            "user@localhost",
            "user@example.c",
            "user..name@example.com",
            ".user@example.com",
            "@example.com",
            "user@",
            "user@-example.com",
            "user@example..com",
            "user@@example.com",
        ).forEach { email ->
            assertEquals(IssueReportFieldError.InvalidEmail, validate(email = email).emailError, email)
        }
    }

    @Test
    fun `ordinary and international emails are accepted`() {
        listOf(
            "me@example.com",
            "first.last+tag@sub.example.co.uk",
            "ion_popescu@exemplu.ro",
            "  padded@example.com  ",
            "用户@例子.广告",
        ).forEach { email ->
            assertNull(validate(email = email).emailError, email)
        }
    }

    @Test
    fun `reports in other scripts pass on the same terms`() {
        listOf(
            Triple(
                "應用程式開啟設定時閃退",
                "每次我從主畫面開啟設定頁面時，應用程式都會立即關閉。重新安裝後問題仍然存在，請協助檢查這個錯誤。",
                "user@example.com",
            ),
            Triple(
                "Приложение падает при запуске",
                "Каждый раз при открытии настроек приложение закрывается без сообщения об ошибке.",
                "user@example.com",
            ),
            Triple(
                "التطبيق يتوقف عند فتح الإعدادات",
                "في كل مرة أفتح فيها صفحة الإعدادات يغلق التطبيق فجأة دون أي رسالة خطأ.",
                "user@example.com",
            ),
            Triple(
                "सेटिंग खोलने पर ऐप बंद हो जाता है",
                "जब भी मैं सेटिंग पेज खोलता हूं, ऐप बिना किसी त्रुटि संदेश के तुरंत बंद हो जाता है।",
                "user@example.com",
            ),
        ).forEach { (title, description, email) ->
            assertTrue(validate(title = title, description = description, email = email).isValid, title)
        }
    }

    @Test
    fun `lengths are counted after trimming`() {
        assertEquals(
            IssueReportFieldError.TooShort(minimumLength = 10),
            validate(title = "     Crash     ").titleError,
        )
        assertNull(validate(title = "   Crash on launch   ").titleError)
    }

    @Test
    fun `text over the limits is too long`() {
        assertEquals(
            IssueReportFieldError.TooLong(maximumLength = 120),
            validate(title = "Crash when opening ".repeat(7)).titleError,
        )
        assertEquals(
            IssueReportFieldError.TooLong(maximumLength = 5_000),
            validate(description = VALID_DESCRIPTION.repeat(100)).descriptionError,
        )
    }

    @Test
    fun `punctuation and emoji alone are not text`() {
        assertEquals(IssueReportFieldError.TooFewLetters, validate(description = "!".repeat(50)).descriptionError)
        assertEquals(IssueReportFieldError.TooFewLetters, validate(description = "🐛🔥💥😡").descriptionError)
        assertEquals(IssueReportFieldError.TooFewLetters, validate(title = "??????????????").titleError)
    }

    @Test
    fun `long enough text needs enough letters`() {
        assertEquals(IssueReportFieldError.TooFewLetters, validate(title = "!!! Bug !!!").titleError)
        assertEquals(
            IssueReportFieldError.TooFewLetters,
            validate(description = "Bug!!! ......... ????????? !!!!!!!!! ......... 404").descriptionError,
        )
    }

    @Test
    fun `extremely repetitive text is refused`() {
        listOf(
            "a".repeat(48),
            "test test test test test test test test test",
            "qwerty qwerty qwerty qwerty qwerty qwerty qwerty",
            "asdasdasdasdasdasdasdasdasdasdasdasdasdasdas",
        ).forEach { description ->
            assertEquals(
                IssueReportFieldError.Repetitive,
                validate(description = description).descriptionError,
                description,
            )
        }
        assertEquals(IssueReportFieldError.Repetitive, validate(title = "aaaaaaaaaaaa").titleError)
    }

    @Test
    fun `a stack trace that repeats its frames is not repetitive`() {
        val stackTrace = "java.lang.StackOverflowError\n" + "    at com.example.App.loop(App.kt:12)\n".repeat(20)

        assertNull(validate(description = stackTrace).descriptionError)
    }

    @Test
    fun `a link with nothing around it is refused`() {
        assertEquals(
            IssueReportFieldError.LinkOnly,
            validate(description = "https://example.com/some/very/long/path/to/a/page").descriptionError,
        )
        assertEquals(IssueReportFieldError.LinkOnly, validate(title = "see www.example.com/a").titleError)
    }

    @Test
    fun `a link inside a description is fine`() {
        val description = "The settings page crashes, see the screenshot at https://example.com/shot.png for details."

        assertNull(validate(description = description).descriptionError)
    }

    @Test
    fun `a description repeating the title is refused`() {
        val title = "The app crashes whenever I open the settings page"

        assertEquals(
            IssueReportFieldError.SameAsTitle,
            validate(
                title = title,
                description = "  the app crashes whenever I open   the SETTINGS page ",
            ).descriptionError,
        )
    }

    private companion object {
        const val VALID_TITLE: String = "Crash when opening settings"
        const val VALID_DESCRIPTION: String =
            "The app closes as soon as I open the settings page from the drawer, every time."
        const val VALID_EMAIL: String = "me@example.com"
    }
}
