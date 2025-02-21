package com.example.epsi1

class Utils {
    companion object {
        /*génère un mot de passe de 20 caractères contenant de A à Z, de a à z et de 0 à 9*/
        fun generateur():String {
            val alphabet: List<Char> = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            return (1..20).map{ alphabet.random()}.joinToString("")
        }
    }
}