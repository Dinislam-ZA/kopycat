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
package ru.inforion.lab403.kopycat.cores.x86.instructions.sse

import ru.inforion.lab403.common.extensions.*
import ru.inforion.lab403.kopycat.cores.base.operands.AOperand
import ru.inforion.lab403.kopycat.cores.x86.exceptions.x86HardwareException
import ru.inforion.lab403.kopycat.cores.x86.hardware.systemdc.Prefixes
import ru.inforion.lab403.kopycat.modules.cores.x86Core

class Divsd(core: x86Core, opcode: ByteArray, prefs: Prefixes, vararg operands: AOperand<x86Core>) :
    ASSEInstruction(core, opcode, prefs, *operands) {

    override val mnem = "divsd"

    override fun executeSSEInstruction() {
        // TODO: MXCSR

        val a1 = op1.extValue(core)
        val a2 = op2.extValue(core)

        val v1 = a1.ulong.ieee754()
        val v2 = a2.ulong.ieee754()

        if (v2 == 0.0) {
            core.config.mxcsr = core.config.mxcsr set 2
            if (core.config.mxcsr and 0x0200uL == 0uL) {
                // 'Invalid' exception
                throw x86HardwareException.FpuException(core.pc)
            }
        }

        op1.extValue(core, a1.insert((v1 / v2).ieee754AsUnsigned(), 63..0))
    }
}
