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
package ru.inforion.lab403.kopycat.modules.cores

import ru.inforion.lab403.common.extensions.*
import ru.inforion.lab403.common.logging.INFO
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.kopycat.cores.base.common.Debugger
import ru.inforion.lab403.kopycat.cores.base.common.Module
import ru.inforion.lab403.kopycat.cores.ppc.enums.eOEA
import ru.inforion.lab403.kopycat.cores.ppc.enums.eUISA
import java.math.BigInteger


class PPCDebugger(parent: Module, name: String): Debugger(parent, name) {
    companion object {
        @Transient val log = logger(INFO)
    }

    override fun ident() = "ppc"

    override fun registers(): MutableList<BigInteger> {
        val core = core as PPCCore
        val result = mutableListOf<BigInteger>().apply {
            addAll(Array(eUISA.GPR31.id + 1) { k -> regRead(eUISA.GPR0.id + k) })
            addAll(Array(64) { (it.ulong_z or 0x80000000uL).bigint })
            add(regRead(eUISA.PC.id))
            add(core.cpu.oeaRegs.readIntern(eOEA.MSR.id).bigint)
            add(regRead(eUISA.CR.id))
            add(regRead(eUISA.LR.id))
            add(regRead(eUISA.CTR.id))
            add(regRead(eUISA.XER.id))
            addAll(Array(32) { (it.ulong_z or 0xFF000000uL).bigint })


            /*//addAll(Array(32) { k -> readRegister(eUISA.FPR0.id + k) })
            //addAll(Array(32) { k -> readRegister(eUISA.FPR0.id + k) })
            addAll(Array(32) { k -> -1L })
            */
            /*addAll(Array(32) { k -> readRegister(eUISA.GPR0.id + k) })
            add(1)
            add(2)
            add(3)
            add(3)
            add(3)
            add(3)
            add(3)
            add(3)
            add(3)
            add(3)
            add(3)
            add(3)
            add(3)
            add(3)
            add(3)*/

        }.map { it.swap32() }.toMutableList()
        return result
    }

    override fun regWrite(index: Int, value: BigInteger) {
        val core = core as PPCCore
        val data = value.swap32().ulong
        if ((index >= eUISA.GPR0.id) && (index <= eUISA.GPR31.id)) {
            core.cpu.regs.writeIntern(index, data)
        }
        /*else if ((index >= 32) || (index <= 95)) {
            log.severe { "Write to register $index = ${value.hex8}! Register has no definition!!!" }
        }*/
        else when(index) {
            64 -> core.cpu.regs.writeIntern(eUISA.PC.id, data)
            65 -> core.cpu.oeaRegs.writeIntern(eOEA.MSR.id, data)
            66 -> core.cpu.regs.writeIntern(eUISA.CR.id, data)
            67 -> core.cpu.regs.writeIntern(eUISA.LR.id, data)
            68 -> core.cpu.regs.writeIntern(eUISA.CTR.id, data)
            69 -> core.cpu.regs.writeIntern(eUISA.XER.id, data)
            70 -> core.cpu.regs.writeIntern(eUISA.FPSCR.id, data)
            71, 72 -> log.severe { "Write to register $index = ${data.hex8}! Register has no definition!!!" }
        }
        log.info { "Register write: $index = ${data.hex8}" }
    }
}
