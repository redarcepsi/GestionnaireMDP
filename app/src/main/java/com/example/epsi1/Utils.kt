package com.example.epsi1

class Utils {
    companion object {
        fun generateur():String {
            val alphabet: List<Char> = ('A'..'Z') + ('a'..'z') + ('0'..'9')

            return (1..20).map{ alphabet.random()}.joinToString("")
        }
    }
}