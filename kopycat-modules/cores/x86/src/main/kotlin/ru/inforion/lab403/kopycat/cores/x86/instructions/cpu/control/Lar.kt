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
package ru.inforion.lab403.kopycat.cores.x86.instructions.cpu.control

import ru.inforion.lab403.kopycat.cores.base.operands.AOperand
import ru.inforion.lab403.kopycat.cores.x86.hardware.processors.x86CPU
import ru.inforion.lab403.kopycat.cores.x86.hardware.systemdc.Prefixes
import ru.inforion.lab403.kopycat.cores.x86.instructions.AX86Instruction
import ru.inforion.lab403.kopycat.modules.cores.x86Core



class Lar(core: x86Core, opcode: ByteArray, prefs: Prefixes, vararg operands: AOperand<x86Core>): AX86Instruction(core, Type.VOID, opcode, prefs, *operands) {
    override val mnem = "lar"

    override fun execute() {
        val ss = op2.value(core)
        // TODO: Whether we should cache take into account?
        if (core.cpu.mode == x86CPU.Mode.R64)
            TODO("LAR for IA-32e isn't implemented yet")
        val desc = core.mmu.readSegmentDescriptor32(ss)
        val result = if(prefs.is16BitOperandMode){
            TODO()
        } else {
            desc.data1 and 0xF0FF00uL
        }
        op1.value(core, result)
        core.cpu.flags.zf = true
    }
}