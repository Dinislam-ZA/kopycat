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
package ru.inforion.lab403.elfloader2.processors.mips.enums


enum class MipsFlags(val id: UInt) {
    EF_MIPS_NOREORDER           (0x00000001u),  // At least one .noreorder assembly directive appeared in a source contributing to the object
    EF_MIPS_PIC                 (0x00000002u),  // This file contains position-independent code
    EF_MIPS_CPIC                (0x00000004u),  // This file's code follows standard conventions for calling position-independent code.
    EF_MIPS_XGOT                (0x00000008u),  // This file contains large (32-bit) GOT
    EF_MIPS_UCODE               (0x00000010u),  // This file contains UCODE (obsolete)
    EF_MIPS_64BIT_WHIRL         (0x00000010u),  // This file contains WHIRL intermediate relocation language code (SGI/Open64)
    EF_MIPS_ABI2                (0x00000020u),  // This file follows the n32 abi
    EF_MIPS_ABI_ON32            (0x00000040u),  // (obsolete)
    EF_MIPS_OPTIONS_FIRST       (0x00000080u),  // The .MIPS.options section in this file contains one or more descriptors, which should be processed first by ld.
    EF_MIPS_32BITMODE           (0x00000100u),  // binaries compiled for a 32bit ABI, but a 64bit ISA, have this flag set, as the kernel will refuse to execute 64bt code
    E_MIPS_FP64                 (0x00000200u),  // 32-bit machine but FP registers are 64 bit (-mfp64).
    E_MIPS_NAN2008              (0x00000400u),  // Code in file uses the IEEE 754-2008 NaN encoding convention.
    E_MIPS_ABI_O32              (0x00001000u),  // This file follows the first MIPS 32 bit ABI (UCODE). Unknown if this flag is actually used.
    E_MIPS_ABI_O64              (0x00002000u),  // This file follows the UCODE MIPS 64 bit ABI (obsolete)
    E_MIPS_ABI_EABI32           (0x00003000u),  // Embedded Application Binary Interface for 32-bit
    E_MIPS_ABI_EABI64           (0x00004000u),  // Embedded Application Binary Interface for 64-bit
    EF_MIPS_ARCH_ASE_MDMX       (0x08000000u),  // Uses MDMX multimedia extensions
    EF_MIPS_ARCH_ASE_M16        (0x04000000u),  // Uses MIPS-16 ISA extensions
    EF_MIPS_ARCH_ASE_MICROMIPS  (0x02000000u),  // Uses MicroMips. Actually not an extension, but a full architecture
    EF_MIPS_ARCH_1              (0x00000000u),  // Contains MIPS I instruction set
    EF_MIPS_ARCH_2              (0x10000000u),  // Contains MIPS II instruction set
    EF_MIPS_ARCH_3              (0x20000000u),  // Contains MIPS III instruction set
    EF_MIPS_ARCH_4              (0x30000000u),  // MIPS IV is the fourth version of the architecture. A superset of MIPS III compatible with all existing versions of MIPS.
    EF_MIPS_ARCH_5              (0x40000000u),  // Never introduced
    EF_MIPS_ARCH_32             (0x50000000u),  // This file will run on a machine with the architecture describe for Mips32 Revision 1
    EF_MIPS_ARCH_64             (0x60000000u),  // This file will run on a machine with the architecture describe for Mips64 Revision 1
    EF_MIPS_ARCH_32R2           (0x70000000u),  // This file will run on a machine with the architecture describe for Mips32 Revision 2
    EF_MIPS_ARCH_64R2           (0x80000000u),  // This file will run on a machine with the architecture describe for Mips64 Revision 2

    EF_MIPS_ABI         (0x0000f000u),       // Mask used to isolate 4 bit ABI field
    EF_MIPS_ARCH_ASE    (0x0f000000u),       // Mask used to isolate the Application Specific Extensions used by this object file
    EF_MIPS_ARCH        (0xf0000000u);   // Mask used to isolate the 4-bit field for Architecture in this file

}