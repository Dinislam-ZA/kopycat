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
package ru.inforion.lab403.kopycat.veos.ports.sysdep

import ru.inforion.lab403.common.extensions.int
import ru.inforion.lab403.kopycat.veos.kernel.System
import ru.inforion.lab403.kopycat.veos.ports.signal.sigaction
import ru.inforion.lab403.kopycat.veos.ports.stat.stat64_arm
import ru.inforion.lab403.kopycat.veos.ports.stat.stat_arm
import ru.inforion.lab403.kopycat.veos.ports.stat.stat

object ARM : ASystemDep(O_LARGEFILE = 0x20000 /* 0400000 */) {
    override fun toSigaction(sys: System, address: ULong) = sigaction(sys, address)

    override fun toStat(sys: System, address: ULong, stat: stat) = with(stat_arm(sys, address)) {
        st_dev = stat.st_dev
        st_ino = stat.st_ino.int
        st_mode = stat.st_mode.int
        st_nlink = stat.st_nlink.int
        st_uid = stat.st_uid.int
        st_gid = stat.st_gid.int
        st_rdev = stat.st_rdev
        st_size = stat.st_size.int
        st_blksize = stat.st_blksize.int
        st_blocks = stat.st_blocks.int
        st_atime = stat.st_atime.int
        st_mtime = stat.st_mtime.int
        st_ctime = stat.st_ctime.int
    }

    override fun toStat64(sys: System, address: ULong, stat: stat) = with(stat64_arm(sys, address)) {
        st_dev = stat.st_dev
        st_ino = stat.st_ino
        st_mode = stat.st_mode.int
        st_nlink = stat.st_nlink.int
        st_uid = stat.st_uid
        st_gid = stat.st_gid
        st_rdev = stat.st_rdev
        st_size = stat.st_size
        st_blksize = stat.st_blksize
        st_blocks = stat.st_blocks
        st_atime = stat.st_atime
        st_mtime = stat.st_mtime
        st_ctime = stat.st_ctime
    }
}