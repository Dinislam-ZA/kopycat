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
package ru.inforion.lab403.kopycat.cores.arm.instructions.cpu.arithm.register

import ru.inforion.lab403.common.extensions.int
import ru.inforion.lab403.kopycat.cores.arm.AddWithCarry
import ru.inforion.lab403.kopycat.cores.arm.SRType
import ru.inforion.lab403.kopycat.cores.arm.Shift
import ru.inforion.lab403.kopycat.cores.arm.enums.Condition
import ru.inforion.lab403.kopycat.cores.arm.hardware.flags.FlagProcessor
import ru.inforion.lab403.kopycat.cores.arm.instructions.AARMInstruction
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMRegister
import ru.inforion.lab403.kopycat.cores.base.operands.Immediate
import ru.inforion.lab403.kopycat.modules.cores.AARMCore


class CMPr(cpu: AARMCore,
           opcode: ULong,
           cond: Condition,
           var setFlags: Boolean,
           var rd: ARMRegister,
           var rn: ARMRegister,
           var rm: ARMRegister,
           var shiftT: SRType,
           var shiftN: Int,
           size: Int): AARMInstruction(cpu, Type.VOID, cond, opcode, rn, rm, size = size) {

    override val mnem = "CMP$mcnd"

    override fun execute() {
        val shifted = Immediate<AARMCore>(Shift(rm.value(core), rm.dtyp.bits, shiftT, shiftN, core.cpu.flags.c.int))
        val (result, carry, overflow) = AddWithCarry(rn.dtyp.bits, rn.value(core), shifted.inv(core), 1)
        FlagProcessor.processArithmFlag(core, result, carry, overflow)
    }
}