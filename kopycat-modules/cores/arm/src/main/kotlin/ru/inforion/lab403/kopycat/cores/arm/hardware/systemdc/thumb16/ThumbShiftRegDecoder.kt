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
package ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.thumb16

import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.common.extensions.int
import ru.inforion.lab403.kopycat.cores.arm.enums.Condition
import ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.decoders.ADecoder
import ru.inforion.lab403.kopycat.cores.arm.instructions.AARMInstruction
import ru.inforion.lab403.kopycat.cores.arm.operands.ARMRegister
import ru.inforion.lab403.kopycat.modules.cores.AARMCore

class ThumbShiftRegDecoder(
        cpu: AARMCore,
        val constructor: (
                cpu: AARMCore,
                opcode: ULong,
                cond: Condition,
                setFlags: Boolean,
                rd: ARMRegister,
                rn: ARMRegister,
                rm: ARMRegister,
                size: Int) -> AARMInstruction) : ADecoder<AARMInstruction>(cpu) {
    override fun decode(data: ULong): AARMInstruction {
        val rd = gpr(data[2..0].int)
        val rn = gpr(data[2..0].int)
        val rm = gpr(data[5..3].int)
        val setFlag = !core.cpu.InITBlock()

        return constructor(core, data, Condition.AL, setFlag, rd, rn, rm, 2)
    }
}