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
package ru.inforion.lab403.kopycat.cores.msp430.instructions.cpu.branch

import ru.inforion.lab403.kopycat.cores.base.operands.AOperand
import ru.inforion.lab403.kopycat.cores.msp430.instructions.AMSP430Instruction
import ru.inforion.lab403.kopycat.modules.cores.MSP430Core
import ru.inforion.lab403.kopycat.interfaces.*




class Reti(core: MSP430Core, size: Int, vararg operands: AOperand<MSP430Core>):
        AMSP430Instruction(core, Type.VOID, size, *operands) {
    override val mnem = "reti"

    override fun execute() {
        val stack = core.cpu.regs.r1StackPointer
        core.cpu.regs.r2StatusRegister = core.inw(stack + 0u)
        core.cpu.regs.r0ProgramCounter = core.inw(stack + 2u)
        core.cpu.regs.r1StackPointer = stack + 4u
    }
}