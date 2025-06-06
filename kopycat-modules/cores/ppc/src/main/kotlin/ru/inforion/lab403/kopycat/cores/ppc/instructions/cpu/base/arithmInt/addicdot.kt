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
package ru.inforion.lab403.kopycat.cores.ppc.instructions.cpu.base.arithmInt


import ru.inforion.lab403.common.extensions.signext
import ru.inforion.lab403.common.extensions.signextRenameMeAfter
import ru.inforion.lab403.kopycat.cores.base.enums.Datatype
import ru.inforion.lab403.kopycat.cores.base.operands.AOperand
import ru.inforion.lab403.kopycat.cores.ppc.hardware.flags.FlagProcessor
import ru.inforion.lab403.kopycat.cores.ppc.instructions.APPCInstruction
import ru.inforion.lab403.kopycat.cores.ppc.operands.PPCVariable
import ru.inforion.lab403.kopycat.modules.cores.PPCCore


//Add immediate carrying and record
class addicdot(core: PPCCore, val condRegField: ULong, val length: Boolean, val data: ULong, vararg operands: AOperand<PPCCore>):
        APPCInstruction(core, Type.VOID, *operands) {
    override val mnem = "addic."

    private val result = PPCVariable(Datatype.DWORD)

    override fun execute() {
        val extImm = data.signextRenameMeAfter(15) // Carry fix
        val rA = op2.value(core)
        result.value(core, rA + extImm)

        op1.value(core, result)

        FlagProcessor.processCarry(core, result)
        FlagProcessor.processCR0(core, result)
    }
}