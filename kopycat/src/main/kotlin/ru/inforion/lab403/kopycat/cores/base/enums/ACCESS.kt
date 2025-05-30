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
package ru.inforion.lab403.kopycat.cores.base.enums


/**
 * {RU}
 * Перечисление, описывает доступ элементу как на чтение, так и на запись.
 * Могут быть назначены следующие действия на попытку доступа:
 * [GRANT] - разрешить доступ
 * [IGNORE] - игнорировать доступ
 * [BREAK] - вызывать остановку дебагера при доступе
 * [ERROR] - вызывать исключение при доступе
 * {RU}
 */
const val GRANT = 0
const val IGNORE = 1
const val BREAK = 3
const val ERROR = 4

enum class ACCESS(val read: Int, val write: Int) {
    /**
     * READ         / WRITE
     */
    R_W(GRANT, GRANT),

    /**
     * READ         / WRITE IGNORED
     */
    R_I(GRANT, IGNORE),

    /**
     * READ         / WRITE BREAK
     */
    R_B(GRANT, BREAK),

    /**
     * READ         / WRITE ERROR
     */
    R_E(GRANT, ERROR),

    /**
     * READ IGNORED / WRITE
     */
    I_W(IGNORE, GRANT),

    /**
     * READ IGNORED / WRITE IGNORED
     */
    I_I(IGNORE, IGNORE),

    /**
     * READ IGNORED / WRITE BREAK
     */
    I_B(IGNORE, BREAK),

    /**
     * READ IGNORED / WRITE ERROR
     */
    I_E(IGNORE, ERROR),

    /**
     * READ BREAK   / WRITE
     */
    B_W(BREAK, GRANT),

    /**
     * READ BREAK   / WRITE IGNORED
     */
    B_I(BREAK, IGNORE),

    /**
     * READ BREAK   / WRITE BREAK
     */
    B_B(BREAK, BREAK),

    /**
     * READ BREAK   / WRITE ERROR
     */
    B_E(BREAK, ERROR),

    /**
     * READ ERROR   / WRITE
     */
    E_W(ERROR, GRANT),

    /**
     * READ ERROR   / WRITE IGNORED
     */
    E_I(ERROR, IGNORE),

    /**
     * READ ERROR   / WRITE BREAK
     */
    E_B(ERROR, BREAK),

    /**
     * READ ERROR   / WRITE ERROR
     */
    E_E(ERROR, ERROR),
}