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
package ru.inforion.lab403.kopycat.veos.ports.obstack

import ru.inforion.lab403.kopycat.veos.api.pointers.BytePointer
import ru.inforion.lab403.kopycat.veos.api.pointers.StructPointer
import ru.inforion.lab403.kopycat.veos.kernel.System

class _obstack_chunk(sys: System, address: ULong) : StructPointer(sys, address) {
    var limit by pointer(0x00)
    private var prevPointer by pointer(0x04)
    val contents get() = BytePointer(sys, 0x08u)

    var prev: _obstack_chunk
        get() = _obstack_chunk(sys, prevPointer)
        set(value) { prevPointer = value.address }
}
