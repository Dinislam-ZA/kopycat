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
package ru.inforion.lab403.kopycat.cores.arm.instructions.cpu.logic.registerShiftedRegister

import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.common.extensions.int
import ru.inforion.lab403.kopycat.cores.arm.SRType
import ru.inforion.lab403.kopycat.cores.arm.Shift_C
import ru.inforion.lab403.kopycat.cores.arm.enums.Condition
import ru.inforion.lab403.kopycat.cores.arm.hardware.flags.FlagProcessor
import ru.inforion.lab403.kopycat.cores.arm.instructions.AARMInstruction
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMRegister
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMVariable
import ru.inforion.lab403.kopycat.cores.base.enums.Datatype
import ru.inforion.lab403.kopycat.cores.base.operands.Immediate
import ru.inforion.lab403.kopycat.modules.cores.AARMCore


class TEQrsr(cpu: AARMCore,
             opcode: ULong,
             cond: Condition,
             val setFlags: Boolean,
             val rd: ARMRegister,
             val rn: ARMRegister,
             val rm: ARMRegister,
             val rs: ARMRegister,
             val shiftT: SRType):
        AARMInstruction(cpu, Type.VOID, cond, opcode, rd, rn) {

    override val mnem = "TEQ$mcnd"

    private var result = ARMVariable(Datatype.DWORD)

    override fun execute() {
        val shiftN = rs.value(core)[7..0].int
        val (shifted, carry) = Shift_C(rm.value(core), rm.dtyp.bits, shiftT, shiftN, core.cpu.flags.c.int)
        result.xor(core, rn, Immediate(shifted))
        FlagProcessor.processLogicFlag(core, result, carry == 1)
    }
}