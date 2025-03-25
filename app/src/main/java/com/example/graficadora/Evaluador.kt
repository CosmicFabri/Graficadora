package com.example.graficadora
import com.example.graficadora.datastructures.Stack

class Evaluador {
    val prioridad: Map<Char, Int> = mapOf(
        '^' to 3, '*' to 2, '/' to 2, '+' to 1, '-' to 1, '(' to 0,
    )

    fun infijaAPosfija(infija: CharArray): CharArray {
        val pila = Stack()
        val posfija: MutableList<Char> = mutableListOf()

        for (caracter in infija) { // Es operando
            when {
                caracter.isDigit() -> {
                    posfija.add(caracter)
                }

                caracter == '(' -> {
                    pila.push(caracter)
                }

                caracter == ')' -> {
                    while(!pila.isEmpty() && pila.peek() != '(') {
                        pila.pop()?.let { posfija.add(it) }  // Esta comprobacion horrible la exige kotlin para no dar error por ser Null Safe
                    }
                    if (!pila.isEmpty()) {
                        pila.pop() // Eliminar '(' sin agregar a posfija
                    }
                }

                prioridad.containsKey(caracter) -> {
                    while(!pila.isEmpty() && prioridad.getOrElse(caracter, {0}) <= prioridad.getOrElse(pila.peek()?: ' ', {0})) { // Otra vez Kotlin y su null safety
                        pila.pop()?.let { posfija.add(it) }
                    }
                    pila.push(caracter)
                }
            }
        }

        while(!pila.isEmpty()) {
            pila.pop()?.let { posfija.add(it) }
        }

        return posfija.toCharArray()
    }

    fun evaluarPunto(x: Int, posfija: CharArray) {

    }

    fun evaluarMuchosPuntos() {

    }
}

fun main() {
    val evaluador = Evaluador()

    // Prueba 1
    val infija: CharArray = charArrayOf('3', '+', '5', '*', '(', '2', '-', '8', ')')
    val resultado = evaluador.infijaAPosfija(infija)
    println("Resultado: ${resultado.joinToString()}")  // Deberías ver la salida como "3 5 + 2 8 - *"

    // Prueba 2
    val infija2: CharArray = charArrayOf('3', '*', '2', '-', '1')
    val resultado2 = evaluador.infijaAPosfija(infija2)
    println("Resultado 2: ${resultado2.joinToString()}")  // Deberías ver la salida como "3 4 2 + * 1 -"
}