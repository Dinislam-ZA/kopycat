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
package ru.inforion.lab403.kopycat.cores.x86.instructions.cpu.stack

import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.common.extensions.insert
import ru.inforion.lab403.kopycat.cores.base.enums.Datatype
import ru.inforion.lab403.kopycat.cores.base.exceptions.GeneralException
import ru.inforion.lab403.kopycat.cores.x86.exceptions.x86HardwareException
import ru.inforion.lab403.kopycat.cores.x86.hardware.processors.x86CPU
import ru.inforion.lab403.kopycat.cores.x86.hardware.systemdc.Prefixes
import ru.inforion.lab403.kopycat.cores.x86.instructions.AX86Instruction
import ru.inforion.lab403.kopycat.cores.x86.x86utils
import ru.inforion.lab403.kopycat.modules.cores.x86Core


class Pushf(core: x86Core, opcode: ByteArray, prefs: Prefixes):
        AX86Instruction(core, Type.VOID, opcode, prefs) {
    override val mnem = "pushf"

    override fun execute() {
        val pe = core.cpu.cregs.cr0.pe
        val vm = core.cpu.flags.vm
        val iopl = core.cpu.flags.iopl
        val eflags = core.cpu.flags.eflags.value or 0x0002uL // Reserved, always 1 in EFLAGS

        // Default 64-bit operand size
        // All instructions, except far branches, that implicitly reference the RSP
        if (core.is64bit)
            prefs.rexW = true

        // Real-Address Mode, Protected mode, or Virtual-8086 mode with IOPL equal to 3 (merged with 64-bit mode)
        if (!pe || /*pe &&*/ (!vm || (vm && iopl == 3uL)) || core.is64bit) {
            if (prefs.opsize != Datatype.WORD)
                // VM and RF bits are cleared in image stored on the stack
                x86utils.push(core, eflags and 0xFCFFFFu, prefs.opsize, prefs)
            else
                x86utils.push(core, eflags[15..0], Datatype.WORD, prefs)
        } /*else if (core.is64bit) { // In 64-bit Mode
            // Default 64-bit operand size
            // All instructions, except far branches, that implicitly reference the RSP
            if (prefs.opsize == Datatype.QWORD)
                // VM and RF bits are cleared in image stored on the stack
                x86utils.push(core, eflags and 0xFCFFFFu, Datatype.QWORD, prefs)
            else
                x86utils.push(core, eflags[15..0], Datatype.WORD, prefs)
        }*/ else { // In Virtual-8086 Mode with IOPL less than 3
            if (!core.cpu.cregs.cr4.vme || prefs.opsize == Datatype.DWORD)
                throw x86HardwareException.GeneralProtectionFault(core.pc, 0u) // Trap to virtual-8086 monitor
            val tempFlags = eflags[15..0]
                .insert(core.cpu.flags.eflags.vif, 9) // VIF replaces IF
                .insert(0b11u, 13..12)          // IOPL is set to 3 in image stored on the stack
            x86utils.push(core, tempFlags, Datatype.WORD, prefs)
        }
    }
}