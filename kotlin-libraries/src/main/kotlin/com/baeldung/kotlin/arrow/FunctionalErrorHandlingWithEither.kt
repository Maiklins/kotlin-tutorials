package com.baeldung.kotlin.arrow

import arrow.core.Either
import arrow.core.filterOrElse
import arrow.core.flatMap
import arrow.core.right
import kotlin.math.sqrt

class FunctionalErrorHandlingWithEither {

    sealed class ComputeProblem {
        object OddNumber : ComputeProblem()
        object NotANumber : ComputeProblem()
    }

    fun parseInput(s : String) : Either<ComputeProblem, Int> = if(s.toIntOrNull() != null) Either.Right(s.toInt()) else Either.Left(ComputeProblem.NotANumber)

    fun isEven(x : Int) : Boolean = x % 2 == 0

    fun biggestDivisor(x: Int) : Int = biggestDivisor(x, 2)

    fun biggestDivisor(x : Int, y : Int) : Int {
        if(x == y){
            return 1;
        }
        if(x % y == 0){
            return x / y;
        }
        return biggestDivisor(x, y+1)
    }

    fun isSquareNumber(x : Int) : Boolean {
        val sqrt: Double = sqrt(x.toDouble())
        return sqrt % 1.0 == 0.0
    }

    fun computeWithEither(input : String) : Either<ComputeProblem, Boolean> {
        return parseInput(input)
                .filterOrElse(::isEven) {->ComputeProblem.OddNumber}
                .map (::biggestDivisor)
                .map (::isSquareNumber)
    }

    fun computeWithEitherClient(input : String) {

        @Suppress("UNUSED_EXPRESSION")
        when(val computeWithEither = computeWithEither(input)){
            is Either.Right -> "The greatest divisor is square number: ${computeWithEither.value}"
            is Either.Left -> when(computeWithEither.value){
                is ComputeProblem.NotANumber -> "Wrong input! Not a number!"
                is ComputeProblem.OddNumber -> "It is an odd number!"
            }
        }
    }

}