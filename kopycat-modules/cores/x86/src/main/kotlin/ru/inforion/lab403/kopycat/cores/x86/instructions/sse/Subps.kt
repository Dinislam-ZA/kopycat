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
import ru.inforion.lab403.kopycat.cores.x86.hardware.systemdc.Prefixes
import ru.inforion.lab403.kopycat.modules.cores.x86Core
import java.math.BigInteger

class Subps(core: x86Core, opcode: ByteArray, prefs: Prefixes, vararg operands: AOperand<x86Core>) :
    ASSEInstruction(core, opcode, prefs, *operands) {

    override val mnem = "subps"

    override fun executeSSEInstruction() {
        val a1 = op1.extValue(core)
        val a2 = op2.extValue(core)

        // DEST[31:0] := SRC1[31:0] - SRC2[31:0]
        // DEST[63:32] := SRC1[63:32] - SRC2[63:32]
        // DEST[95:64] := SRC1[95:64] - SRC2[95:64]
        // DEST[127:96] := SRC1[127:96] - SRC2[127:96]

        op1.extValue(
            core,
            (0 until 4).asIterable().fold(BigInteger.ZERO) { acc, i ->
                val range = 32 * (i + 1) - 1..32 * i
                acc.insert((a1[range].uint.ieee754() - a2[range].uint.ieee754()).ieee754AsUnsigned().ulong_z, range)
            },
        )
    }
}
