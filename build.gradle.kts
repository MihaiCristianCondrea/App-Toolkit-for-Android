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

plugins {
    alias(notation = libs.plugins.android.application) apply false
    alias(notation = libs.plugins.android.library) apply false
    alias(notation = libs.plugins.kotlin.compose) apply false
    alias(notation = libs.plugins.kotlin.serialization) apply false
    alias(notation = libs.plugins.google.mobile.services) apply false
    alias(notation = libs.plugins.firebase.crashlytics) apply false
    alias(notation = libs.plugins.firebase.performance) apply false
    alias(notation = libs.plugins.about.libraries) apply true
    alias(notation = libs.plugins.mannodermaus.android.junit5) apply false
}

buildscript {
    dependencies {
        constraints {
            classpath("org.jdom:jdom2:2.0.6.1") {
                because("Mitigates CVE-2021-33813 (XXE) from AGP transitive dependency chain")
            }
            classpath("io.netty:netty-handler:4.1.129.Final") {
                because("Mitigates known Netty handler vulnerabilities (native crash and SNI allocation DoS)")
            }
            classpath("io.netty:netty-codec-http2:4.1.129.Final") {
                because("Mitigates known HTTP/2 vulnerabilities, including Rapid Reset and MadeYouReset")
            }
            classpath("io.netty:netty-codec:4.1.129.Final") {
                because("Mitigates Netty decoder DoS vulnerabilities (including CVE-2025-58057)")
            }
            classpath("io.netty:netty-codec-http:4.1.129.Final") {
                because("Mitigates CRLF injection/request smuggling vulnerability (CVE-2025-67735)")
            }
            classpath("org.bitbucket.b_c:jose4j:0.9.6") {
                because("Mitigates DoS via compressed JWE content (CVE-2024-29371)")
            }
        }
    }
}
