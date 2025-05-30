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

import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.common.extensions.insert
import ru.inforion.lab403.kopycat.cores.base.operands.AOperand
import ru.inforion.lab403.kopycat.cores.x86.hardware.systemdc.Prefixes
import ru.inforion.lab403.kopycat.modules.cores.x86Core
import java.math.BigInteger

class Shufps(core: x86Core, opcode: ByteArray, prefs: Prefixes, vararg operands: AOperand<x86Core>) :
    ASSEInstruction(core, opcode, prefs, *operands) {

    override val mnem = "shufps"

    private fun select4(src: BigInteger, control: ULong) = when (control) {
        0uL -> src[31..0]
        1uL -> src[63..32]
        2uL -> src[95..64]
        else -> src[127..96]
    }

    override fun executeSSEInstruction() {
        val a1 = op1.extValue(core)
        val a2 = op2.extValue(core)
        val a3 = op3.value(core)

        val result = BigInteger.ZERO
            .insert(select4(a1, a3[1..0]), 31..0)
            .insert(select4(a1, a3[3..2]), 63..32)
            .insert(select4(a2, a3[5..4]), 95..64)
            .insert(select4(a2, a3[7..6]), 127..96)

        op1.extValue(core, result)
    }
}
