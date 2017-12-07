package com.commit451.youtubeextractor

/**
 * Runs JavaScripty things
 */
internal object JavaScriptUtil {

    private const val DECRYPTION_FUNC_NAME = "decrypt"

    fun loadDecryptionCode(playerCode: String): String {
        val decryptionFuncName: String = Util.matchGroup("([\"\\'])signature\\1\\s*,\\s*([a-zA-Z0-9$]+)\\(", playerCode, 2)
        var callerFunc = "function $DECRYPTION_FUNC_NAME(a){return %%(a);}"

        val functionPattern = ("("
                + decryptionFuncName.replace("$", "\\$")
                + "=function\\([a-zA-Z0-9_]+\\)\\{.+?\\})")

        val decryptionFunc = "var " + Util.matchGroup(functionPattern, playerCode, 1) + ";"

        val helperObjectName = Util
                .matchGroup(";([A-Za-z0-9_\\$]{2})\\...\\(", decryptionFunc, 1)

        val helperPattern = "(var " + helperObjectName.replace("$", "\\$") + "=\\{.+?\\}\\};)"
        val helperObject = Util.matchGroup(helperPattern, playerCode, 1)

        callerFunc = callerFunc.replace("%%", decryptionFuncName)
        return helperObject + decryptionFunc + callerFunc
    }
}