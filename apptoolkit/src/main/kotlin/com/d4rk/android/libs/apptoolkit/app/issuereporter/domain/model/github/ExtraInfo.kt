package com.d4rk.android.libs.apptoolkit.app.issuereporter.domain.model.github

class ExtraInfo {
    private val extraInfo: MutableMap<String, String> = LinkedHashMap()

    fun isEmpty(): Boolean = extraInfo.isEmpty()

    fun toMarkdown(): String {
        if (extraInfo.isEmpty()) return ""
        val output = StringBuilder()
        output.append(
            "Extra info:\n" +
                    "---\n" +
                    "<table>\n"
        )
        for (key in extraInfo.keys) {
            output.append("<tr><td>")
                .append(key)
                .append("</td><td>")
                .append(extraInfo[key])
                .append("</td></tr>\n")
        }
        output.append("</table>\n")
        return output.toString()
    }
}
