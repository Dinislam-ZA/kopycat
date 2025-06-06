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
package ru.inforion.lab403.gradle.buildConfig.creator.scriptgen

/**
 * Generates powershell KOPYCAT startup script
 */
class PowerShellScriptGenerator(
    val data: ScriptGeneratorData
) : IScriptGenerator {
    override val name: String = data.name
    override val description: String = data.description
    override val projectDir: String = data.projectDir
    override val starterClass: String = data.starterClass

    override val classpathStr by lazy { data.classpath.joinToString(";") }

    override val arguments = linkedMapOf<String, String?>()

    override fun generate(): String {
        val argumentsStr = arguments.map { (key, value) ->
            value?.let { "  $key \"$it\"" } ?: "  $key"
        }.joinToString(" `\n")

        return """
# This is autogenerated file
# Do not edit it

💲PROJECT_DIR="${data.projectDir}"
Write-Host ([string]::Format("Configuration: {0}", "$name"))
Write-Host ([string]::Format("{0}", "$description"))

Write-Host ([string]::Format("Project dir: {0}", "💲PROJECT_DIR"))
Set-Location "💲PROJECT_DIR"

.\gradlew ${data.gradleBuildTask}
if (💲LastExitCode -ne 0) {
    Write-Host "Build failed"
    exit 1
}

java `
  -server `
  `
  -Xms2G `
  -Xmx8G `
  -XX:MaxMetaspaceSize=256m `
  -XX:+UseParallelGC `
  -XX:SurvivorRatio=6 `
  -XX:-UseGCOverheadLimit `
  -classpath "$classpathStr" `
  `
  "$starterClass" `
  `
$argumentsStr `
  💲args
""".replace("💲", "${'$'}")
    }

    override fun fileName(): String = "${data.kcPackageName}-$name.ps1"
    override fun dirName(): String = "powershell"
}