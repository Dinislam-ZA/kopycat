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
package ru.inforion.lab403.kopycat.cores.mips.instructions.cpu.memory

import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.kopycat.cores.base.enums.Datatype
import ru.inforion.lab403.kopycat.cores.mips.exceptions.MipsHardwareException
import ru.inforion.lab403.kopycat.cores.mips.instructions.AMipsInstruction
import ru.inforion.lab403.kopycat.cores.mips.operands.MipsRegister
import ru.inforion.lab403.kopycat.interfaces.read
import ru.inforion.lab403.kopycat.modules.cores.MipsCore

/**
 * From linux and disasm (no docs :C)
 *
 * ```
 * SPECIAL3 base   index  rd     LHUX   LX
 * 0111111  xxxxx  xxxxx  xxxxx  10100  001010
 * ```
 */
class lhux(
    core: MipsCore,
    data: ULong,
    private val rd: MipsRegister,
    private val index: MipsRegister,
    private val basereg: MipsRegister
) : AMipsInstruction(core, data, Type.VOID, rd, index, basereg) {

    override val mnem = "lhux"

    override fun execute() {
        val vAddr = index.value(core) + basereg.value(core)
        if (!core.dspExtension)
            throw MipsHardwareException.DSPDis(core.pc)
        if (vAddr[0] != 0uL && core.cop.regs.CvmCtl?.REPUN != true) {
            throw MipsHardwareException.AdEL(core.pc, vAddr)
        }

        // no signext
        val memword = core.read(Datatype.WORD, vAddr)
        rd.value(core, memword)
    }
}
