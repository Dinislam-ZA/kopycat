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
package ru.inforion.lab403.kopycat.cores.arm.instructions.cpu.exceptions

import ru.inforion.lab403.common.extensions.uint
import ru.inforion.lab403.common.extensions.unaryMinus
import ru.inforion.lab403.kopycat.cores.arm.enums.Condition
import ru.inforion.lab403.kopycat.cores.arm.exceptions.ARMHardwareException
import ru.inforion.lab403.kopycat.cores.arm.instructions.AARMInstruction
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMRegister
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMRegisterList
import ru.inforion.lab403.kopycat.cores.base.enums.Datatype
import ru.inforion.lab403.kopycat.cores.base.like
import ru.inforion.lab403.kopycat.modules.cores.AARMCore
import ru.inforion.lab403.kopycat.interfaces.*


// LDM (exception return), see B9.3.5
class LDMer(cpu: AARMCore,
            opcode: ULong,
            cond: Condition,
            val wback: Boolean,
            val increment: Boolean,
            val wordhigher: Boolean,
            val rn: ARMRegister,
            val registers: ARMRegisterList,
            size: Int):
        AARMInstruction(cpu, Type.VOID, cond, opcode, rn, registers, size = size) {
    // TODO: not correct mnem
    override val mnem = "LDM$mcnd"

    override fun execute() {
        if (core.cpu.CurrentModeIsHyp())
            throw ARMHardwareException.Undefined
        else if (core.cpu.CurrentModeIsUserOrSystem() || core.cpu.CurrentInstrSet() == AARMCore.InstructionSet.THUMB_EE)
            throw ARMHardwareException.Unpredictable
        else {
            val length = 4u * registers.count.uint + 4u
            var address = if (increment) rn.value(core) else rn.value(core) - length
            if (wordhigher) address += 4u

            // There is difference from datasheet (all registers save in common loop) -> no LoadWritePC called

            registers.forEachIndexed { _, reg ->
                reg.value(core, core.inl(address like Datatype.DWORD))
                address += 4u
            }

            val newPCValue = core.inl(address like Datatype.DWORD)

            val hasRn = rn in registers
            val value = rn.value(core) + if (increment) length else -length

            if (wback && !hasRn) rn.value(core, value)
            if (wback && hasRn) rn.value(core, /*UNKNOWN*/ 0uL)

            core.cpu.CPSRWriteByInstr(core.cpu.sregs.spsr.value, 0b1111, true)

            if (core.cpu.sregs.cpsr.m == 0b11010uL && core.cpu.sregs.cpsr.j && core.cpu.sregs.cpsr.t)
                throw ARMHardwareException.Unpredictable
            else
                core.cpu.BranchWritePC(newPCValue)
        }
    }
}