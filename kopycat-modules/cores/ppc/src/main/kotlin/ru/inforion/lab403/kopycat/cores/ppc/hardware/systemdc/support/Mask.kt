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
package ru.inforion.lab403.kopycat.cores.ppc.hardware.systemdc.support

import ru.inforion.lab403.common.extensions.clr
import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.common.extensions.inv
import ru.inforion.lab403.common.extensions.set

class Mask private constructor(val pandm: ULong, val porm: ULong /*, val mandm: ULong, val morm: ULong*/) {
    companion object {
        private fun preprocess(raw: String): String = raw.filter { it.isDigit() || it == 'x' }.reversed()

        private fun m0(raw: String, offset: Int): ULong =
                preprocess(raw).foldIndexed(0x0000_0000uL) { k, r, c ->
                    if (c == '1') r set (k + offset) else r }

        private fun m1(raw: String, offset: Int): ULong =
                preprocess(raw).foldIndexed(0xFFFF_FFFFuL) { k, r, c ->
                    if (c == '0') r clr (k + offset) else r }

        private fun getPosPart(raw: String): String {
            val missingDelimiterValue = "?"
            val result = raw.substringBefore("not", missingDelimiterValue)
            return if (result != missingDelimiterValue) result.trim() else raw.trim()
        }

        private fun getNegPart(raw: String): String {
            val missingDelimiterValue = "?"
            val result = raw.substringAfter("not", missingDelimiterValue)
            return if (result != missingDelimiterValue) result.trim() else ""
        }

        private fun getRaw(andm: ULong, orm: ULong): String {
            return (31 downTo 0).joinToString("") { k ->
                val b1 = andm[k]
                val b2 = orm[k]
                if (b1 == b2) "$b1" else "x"
            }
        }

        fun create(raw: String, offset: Int): Mask {
            val pos = getPosPart(raw)
            val neg = getNegPart(raw)

            var m0p = 0x0000_0000uL
            var m1p = 0xFFFF_FFFFuL
            if (!pos.isBlank() && pos != "-" && pos.any { it != 'x' }) {
                m0p = m0(pos, offset)
                m1p = m1(pos, offset)
            }
            /*
            var m0n = 0xFFFF_FFFFL
            var m1n = 0x0000_0000L

            // neg.all { it == 'x' } = true for empty collections
            if (neg.isNotBlank()) {
                // throw out mask like "not -" or "not xxxxx"
                if (neg == "-" || neg.all { it == 'x' })
                    throw IllegalArgumentException("Wrong negative mask: $neg")

                m0n = m0(neg, offset)
                m1n = m1(neg, offset)
            }*/

            return Mask(m0p, m1p /*, m0n, m1n*/)
        }

        private fun makeOffsets(bits: Array<Any>): List<Int> {
            return bits.map {
                when (it) {
                    is Int -> it
                    is IntRange -> it.last
                    else -> throw IllegalArgumentException("Wrong bits argument (Int and IntRange): $it!")
                }
            }
        }

        fun fromPattern(pattern: String, bits: Array<Any>): Mask {
            val offsets = makeOffsets(bits)
            val masks = pattern
                    .split(',')
                    .zip(offsets)
                    .map { (raw, offset) -> if (raw.isBlank() || raw == "-")
                        Mask.any else Mask.create(raw, offset)
                    }

            return masks.fold(any) { acc, elm -> acc + elm }
        }

        val any = Mask()
    }

    //private val me = "${getRaw(pandm, porm)}|${getRaw(mandm, morm)}"

    private constructor() : this(
            0x0000_0000u,
            0xFFFF_FFFFu /*,
            0xFFFF_FFFF,
            0x0000_0000*/)

    fun suit(value: ULong): Boolean =
            ((value and pandm == pandm) && (value or porm == porm)) /*&&
                    ((value and mandm == mandm) && (value or morm == morm)).not()*/

    //fun isNegEmpty(): Boolean = mandm == 0xFFFF_FFFFL && morm == 0x0000_0000L
    fun isPosEmpty(): Boolean = pandm == 0x0000_0000uL && porm == 0xFFFF_FFFFuL
    fun isEmpty(): Boolean = isPosEmpty() //&& isNegEmpty()


    /* Если у диапазонов есть пересечение, то достаточно проORить and-маски и проANDить or-маски
    *  Если хотя бы в одном бите у результирующей and-маски стоит 1, а у or-маски - 0 (в той же позиции),
    *  то эти диапазоны не пересекаются
    *  Функция возвращает True, если диапазоны масок имеют пересечение
    * */
    fun intersect(other: Mask): Boolean {
        val nPandm = pandm or other.pandm
        val nPorm = porm and other.porm
        return (nPandm and inv(nPorm)) == 0uL
    }

    //Оператор плюс, так-то, делает то же самое, что и intersect, но возвращает маскуы
    operator fun plus(other: Mask): Mask {
        //val nMandm: ULong
        //val nMorm: ULong


        val nPandm = pandm or other.pandm
        val nPorm = porm and other.porm
        /*if (isNegEmpty() || other.isNegEmpty()) {
            nMandm = mandm and other.mandm
            nMorm = morm or other.morm
        } else {
            nMandm = mandm or other.mandm
            nMorm = morm and other.morm
        }*/
        return Mask(nPandm, nPorm /*, nMandm, nMorm*/)
    }

    override fun toString(): String = "[${getRaw(pandm, porm)}]"
}