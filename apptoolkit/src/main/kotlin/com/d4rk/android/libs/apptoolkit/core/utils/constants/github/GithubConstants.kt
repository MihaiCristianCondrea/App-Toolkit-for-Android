package com.d4rk.android.libs.apptoolkit.core.utils.constants.github

object GithubConstants {
    const val GITHUB_USER: String = "MihaiCristianCondrea"
    const val GITHUB_BASE: String = "https://github.com/$GITHUB_USER/"
    const val GITHUB_ISSUES_SUFFIX: String = "/issues/new"
    const val GITHUB_RAW: String = "https://raw.githubusercontent.com/$GITHUB_USER"
    const val GITHUB_PAGES: String = "https://mihaicristiancondrea.github.io"
    fun githubChangelog(repository: String): String =
        "$GITHUB_RAW/$repository/refs/heads/master/CHANGELOG.md"
}
