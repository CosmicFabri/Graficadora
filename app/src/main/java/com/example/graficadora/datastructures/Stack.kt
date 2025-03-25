package com.example.graficadora.datastructures

class Stack {
    private val list = mutableListOf<Char>()

    fun push(element: Char){
        list.add(element)
    }

    fun pop(): Char?{
        if(isEmpty()) return null
        return list.removeAt(list.size - 1)
    }

    fun peek(): Char? {
        if(isEmpty()) return null
        return list[list.size - 1]
    }

    fun isEmpty(): Boolean{
        return list.isEmpty()
    }

    fun size(): Int{
        return list.size
    }

    fun clear(){
        return list.clear()
    }
}