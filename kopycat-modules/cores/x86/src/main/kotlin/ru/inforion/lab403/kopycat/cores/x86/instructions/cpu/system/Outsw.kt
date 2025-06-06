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
package ru.inforion.lab403.kopycat.cores.x86.instructions.cpu.system

import ru.inforion.lab403.common.extensions.uint
import ru.inforion.lab403.kopycat.cores.base.operands.AOperand
import ru.inforion.lab403.kopycat.cores.x86.hardware.systemdc.Prefixes
import ru.inforion.lab403.kopycat.cores.x86.instructions.cpu.string.AStringInstruction
import ru.inforion.lab403.kopycat.cores.x86.operands.x86Displacement
import ru.inforion.lab403.kopycat.cores.x86.operands.x86Register
import ru.inforion.lab403.kopycat.modules.cores.x86Core
import ru.inforion.lab403.kopycat.interfaces.*


class Outsw(core: x86Core, opcode: ByteArray, prefs: Prefixes, vararg operands: AOperand<x86Core>):
        AStringInstruction(core, opcode, prefs, false, *operands) {
    override val mnem = "outsw"

    override fun executeStringInstruction() {
        val dst = op1
        val src = op2 as x86Displacement

        val address = dst.value(core)
        val data = src.value(core)

        var pos = src.reg.value(core)
        if (!core.cpu.flags.df)
            pos += src.dtyp.bytes.uint
        else
            pos -= src.dtyp.bytes.uint

        src.reg.value(core, pos)

        core.ports.io.write(dst.dtyp, address, data)
    }
}