/*
 *
 * This file is part of Kopycat emulator software.
 *
 * Copyright (C) 2023 INFORION, LLC
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Non-free licenses may also be purchased from INFORION, LLC,
 * for users who do not want their programs protected by the GPL.
 * Contact us for details kopycat@inforion.ru
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package ru.inforion.lab403.gradle.kodegen.tokens

import ru.inforion.lab403.gradle.kodegen.Config
import ru.inforion.lab403.gradle.kodegen.abstracts.CompositeToken

class KPackage(
        config: Config,
        val name: String
) : CompositeToken(config, false) {

    fun obj(name: String, init: KObject.() -> Unit) = addToken(KObject(config, name), init)

    override fun render(builder: StringBuilder, indent: String) = builder.apply {
        append("${indent}package $name\n")
        super.render(builder, indent)
    }
}