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

package com.wstxda.toolkit.manager.dns

enum class DnsProvider(val hostname: String) {
    DISABLED(""),
    AUTOMATIC(""),

    GOOGLE("dns.google"),
    CLOUDFLARE("1dot1dot1dot1.cloudflare-dns.com"),

    ADGUARD("dns.adguard-dns.com"),

    OPENDNS("dns.opendns.com"),
    QUAD9("dns.quad9.net"),

    NEXT_DNS("dns.nextdns.io"),
    MULLVAD("adblock.dns.mullvad.net"),
    CONTROLD("p2.freedns.controld.com"),

    LIBREDNS("dot.libredns.gr"),

    CLOUDFLARE_FAMILY("family.cloudflare-dns.com"),

    CUSTOM(""),
}