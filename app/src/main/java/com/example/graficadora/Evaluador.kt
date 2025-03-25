package com.example.graficadora
import com.example.graficadora.datastructures.Stack
import kotlin.math.pow

class Evaluador {
    val prioridad: Map<Char, Int> = mapOf(
        '^' to 3, '*' to 2, '/' to 2, '+' to 1, '-' to 1, '(' to 0,
    )

    fun infijaAPosfija(infija: Array<Char>): Array<Char> {
        val pila = Stack()
        val posfija: MutableList<Char> = mutableListOf()

        for (caracter in infija) {
            when {
                caracter.isDigit() || caracter == 'x' -> { // Es operando
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

        return posfija.toTypedArray()
    }

    fun evaluarPunto(x: Char, posfija: Array<Char>) : Int {
        val resultado = Stack()
        var a : Int
        var b : Int
        var z: Int

        for (caracter in posfija) {
            if (caracter.isDigit() || caracter == 'x') {
                if( caracter == 'x' ) {
                    resultado.push(x)
                }
                else {
                    resultado.push(caracter)
                }
            }
            else {
                b = resultado.pop()!!.code - '0'.code // Conversion de char a entero
                a = resultado.pop()!!.code - '0'.code

                when (caracter) {
                    '^' -> z = a.toFloat().pow(b).toInt()
                    '*' -> z = a * b
                    '/' -> z = a / b
                    '+' -> z = a + b
                    '-' -> z = a - b
                    else -> z = a + b // Else para que el compilador no se ponga de chillon
                }

                resultado.push((z + '0'.code).toChar())
            }
        }

        return resultado.pop()!!.code - '0'.code
    }

    fun evaluarMuchosPuntos(puntos: Array<Char>, posfija: Array<Char>): Array<Array<Int>> {
        val resultado = Array(puntos.size) { Array(2) {0} }

        for (i in 0 until puntos.size) {
            resultado[i][0] = puntos[i].code - '0'.code
            resultado[i][1] = evaluarPunto(puntos[i], posfija)
        }
        return resultado
    }
}

// Codigo de pruebas
fun main() {
    val evaluador = Evaluador()

    val infija: Array<Char> = arrayOf('x', '*', '2', '-', '1')
    val resultado = evaluador.infijaAPosfija(infija)
    println("Resultado 2: ${resultado.joinToString()}")  // Deberías ver la salida como "3 4 2 + * 1 -"

    val evaluado = evaluador.evaluarPunto('5', resultado)
    println("Evaluando el punto 5:  $evaluado")

    val puntos: Array<Char> = arrayOf('0', '1', '2', '3', '4', '5')
    val resultado2 = evaluador.evaluarMuchosPuntos(puntos, resultado)
    for (fila in resultado2) {
        println(fila.joinToString(", "))  // Une los elementos de cada fila con coma
    }
}