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
package ru.inforion.lab403.kopycat.cores.mips.instructions.cpu.mips16.arith

import ru.inforion.lab403.common.extensions.ushr
import ru.inforion.lab403.kopycat.cores.mips.instructions.AMipsInstruction16
import ru.inforion.lab403.kopycat.cores.mips.operands.MipsRegister
import ru.inforion.lab403.kopycat.modules.cores.MipsCore

/**
 * Created by shiftdj on 22.06.2021.
 */

// srl rx, ry, sa
class srl(
        core: MipsCore,
        data: ULong,
        val rx: MipsRegister,
        val ry: MipsRegister,
        val sa: Int) : AMipsInstruction16(core, data, Type.VOID, rx, ry) {

    override val mnem = "srl"

    override fun execute() {
        val s = if (sa == 0) 8 else sa
        val temp = ry.value(core) ushr s
        rx.value(core, temp)
    }
}