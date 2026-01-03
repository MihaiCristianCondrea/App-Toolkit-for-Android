package com.d4rk.android.libs.apptoolkit.core.utils.extensions

import com.d4rk.android.libs.apptoolkit.core.utils.extensions.string.extractChangesForVersion
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.string.faqCatalogUrl
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.string.toToken
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StringExtensionsTest {

    @Test
    fun `extractChangesForVersion returns matching section`() {
        val markdown = """
            # 2.0.0
            - Added feature
            - Fixed bug
            # 1.9.0
            - Previous changes
        """.trimIndent()

        val result = markdown.extractChangesForVersion("2.0.0")

        assertEquals(
            """
            # 2.0.0
            - Added feature
            - Fixed bug
            """.trimIndent(),
            result
        )
    }

    @Test
    fun `extractChangesForVersion returns empty when version header is missing`() {
        val markdown = """
            # 1.0.0
            - Existing change
        """.trimIndent()

        val result = markdown.extractChangesForVersion("2.0.0")

        assertEquals("", result)
    }

    @Test
    fun `extractChangesForVersion returns empty for blank content`() {
        val markdown = ""

        val result = markdown.extractChangesForVersion("1.0.0")

        assertEquals("", result)
    }

    @Test
    fun `decodeBase64OrEmpty returns decoded value`() {
        val encoded = "Z2l0aHViLXRva2Vu"

        val decoded = encoded.toToken()

        assertEquals("github-token", decoded)
    }

    @Test
    fun `decodeBase64OrEmpty returns empty on invalid input`() {
        val decoded = "not-base64%%%".toToken()

        assertEquals("", decoded)
    }

    @Test
    fun `faqCatalogUrl builds environment specific url`() {
        val baseUrl = "https://d4rk.dev/help"

        assertEquals(
            "$baseUrl/debug/catalog.json",
            baseUrl.faqCatalogUrl(isDebugBuild = true)
        )
        assertEquals(
            "$baseUrl/release/catalog.json",
            baseUrl.faqCatalogUrl(isDebugBuild = false)
        )
    }
}
