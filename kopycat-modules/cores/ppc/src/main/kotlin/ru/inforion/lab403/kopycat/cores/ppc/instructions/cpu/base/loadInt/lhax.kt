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
package ru.inforion.lab403.kopycat.cores.ppc.instructions.cpu.base.loadInt

import ru.inforion.lab403.common.extensions.signext
import ru.inforion.lab403.common.extensions.signextRenameMeAfter
import ru.inforion.lab403.kopycat.cores.ppc.enums.eUISA
import ru.inforion.lab403.kopycat.cores.ppc.instructions.APPCInstruction
import ru.inforion.lab403.kopycat.cores.ppc.operands.PPCRegister
import ru.inforion.lab403.kopycat.modules.cores.PPCCore
import ru.inforion.lab403.kopycat.interfaces.*


//Load halfword algebraic indexed
class lhax(core: PPCCore, val fieldA: Int, val fieldB: Int, val fieldC: Int, val flag: Boolean):
        APPCInstruction(core, Type.VOID) {
    override val mnem = "lhax"

    val rt = PPCRegister.gpr(fieldA)
    val ra = PPCRegister.gpr(fieldB)
    val rb = PPCRegister.gpr(fieldC)

    override fun execute() {
        val b = if ((ra as PPCRegister).reg == eUISA.GPR0.id) 0uL else ra.value(core)
        val ea = b + rb.value(core)
        val mem = core.inw(ea).signextRenameMeAfter(15)
        rt.value(core, mem)
    }
}