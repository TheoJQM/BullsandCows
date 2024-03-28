package bullscows

import kotlin.random.Random

const val grade = "Grade: %s"
const val codeCharacters = "0123456789abcdefghijklmnopqrstuvwxyz"

class BullsAndCows {
    private var code = ""
    private var bulls = 0
    private var cows = 0
    private var nbTurns = 0

    fun play() {
        val lengthCode = println("Please, enter the secret code's length:").run { readln() }
        if (checkLengthCode(lengthCode)) return
        val nbSymbols = println("Input the number of possible symbols in the code:").run { readln().toInt() }
        if (checkNbSymbols(nbSymbols)) return


        if (!createSecretCode(lengthCode, nbSymbols)) return

        var preparationCodeString = "The secret is prepared: ${"*".repeat(lengthCode.toInt())} "
        preparationCodeString += if (nbSymbols > 10) "(0-9,a-${codeCharacters[nbSymbols - 1]})." else "(0-${codeCharacters[nbSymbols - 1]})."
        println(preparationCodeString)

        println("Okay, let's start a game!")

        while (bulls != code.length) {
            bulls = 0
            cows = 0
            nbTurns++
            println("Turn $nbTurns:")
            gradeGuess()
        }
        println("Congratulations! You guessed the secret code.")
    }

    private fun checkLengthCode(input: String): Boolean {
        return if (!Regex("""[1-9][0-9]*""").matches(input))  {
            println("Error: \"$input\" isn't a valid number.")
            true
        } else {
            false
        }
    }

    private fun checkNbSymbols(input: Int): Boolean {
        return if(input > 36)  {
            println("Error: it's not possible to generate a code with more than ${codeCharacters.length} unique symbols.")
            true
        } else {
            false
        }
    }

    private fun createSecretCode(size: String, symbols: Int): Boolean {
        return when {
            size.toInt() > symbols -> {
                println("Error: it's not possible to generate a code with a length of $size with $symbols unique symbols.")
                false
            }
            else -> {
                var randomNumber = List(size.toInt()) { codeCharacters[Random.nextInt(0, symbols)]  }.joinToString("")
                while (true) {
                    if (checkCode(randomNumber, symbols)) {
                        code = randomNumber
                        break
                    }
                    randomNumber = List(size.toInt()) { codeCharacters[Random.nextInt(0, symbols)]  }.joinToString("")
                }
                true
            }
        }
    }

    private fun checkCode(code: String, symbols: Int): Boolean {
        for (i in codeCharacters.substring(0, symbols)) {
            val isNotUnique = code.filter { it.toString() == i.toString() }.length > 1
            if (isNotUnique) return false
        }
        return true
    }

    private fun gradeGuess() {
        val guess = readln()
        for (i in guess.indices) if (guess[i] == code[i]) bulls++ else if (code.contains(guess[i])) cows++

        val solution = when {
            (bulls > 0 && cows > 0) -> grade.format("$bulls bull${if (bulls >1) "s" else ""} and $cows cow${if (cows >1) "s" else ""}")
            bulls > 0 -> grade.format("$bulls bull${if (bulls >1) "s" else ""}")
            cows > 0 -> grade.format("$cows cows${if (cows >1) "s" else ""}")
            else -> grade.format("None")
        }
        println(solution)
    }
}

fun main() {
    val game = BullsAndCows()
    game.play()
}