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
package ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.arm.dataproc

import ru.inforion.lab403.common.extensions.find
import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.common.extensions.int
import ru.inforion.lab403.kopycat.cores.arm.ARMExpandImm
import ru.inforion.lab403.kopycat.cores.arm.enums.Condition
import ru.inforion.lab403.kopycat.cores.arm.enums.Condition.AL
import ru.inforion.lab403.kopycat.cores.arm.hardware.systemdc.decoders.ADecoder
import ru.inforion.lab403.kopycat.cores.arm.instructions.AARMInstruction
import ru.inforion.lab403.kopycat.cores.arm.instructions.cpu.special.ADR
import ru.inforion.lab403.kopycat.modules.cores.AARMCore

object ArmAdrDecoder {
    abstract class Common(cpu: AARMCore, val add: Boolean) : ADecoder<AARMInstruction>(cpu) {
        override fun decode(data: ULong): AARMInstruction {
            val cond = find { it.opcode == data[31..28].int } ?: AL
            val rd = gpr(data[15..12].int)
            val imm = imm(ARMExpandImm(data[11..0]), true)
            return ADR(core, data, cond, add, rd, imm, 4)
        }
    }

    class A1(cpu: AARMCore) : Common(cpu, true)
    class A2(cpu: AARMCore) : Common(cpu, false)
}