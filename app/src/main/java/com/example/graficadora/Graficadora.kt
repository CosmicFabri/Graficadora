package com.example.graficadora

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class Graficadora {
    // Usamos SnapshotStateList para que Compose detecte los cambios

    //Escritura
    private val _puntos = mutableStateListOf<Offset>()
    //Lectura
    val puntos: List<Offset> get() = _puntos

    // Dimensiones del canvas
    private var anchoCanvas: Float = 0f
    private var altoCanvas: Float = 0f

    // Configuración de márgenes
    private var margenHorizontal: Float = 16f
    private var margenVertical: Float = 16f

    fun configurarMargenes(horizontal: Float, vertical: Float) {
        this.margenHorizontal = horizontal
        this.margenVertical = vertical
    }

    fun agregarPunto(x: Float, y: Float) {
        // Esperamos a tener las dimensiones del canvas
        if (anchoCanvas > 0 && altoCanvas > 0) {
            _puntos.add(Offset(anchoCanvas/2 + x, altoCanvas/2 - y))
        }
    }

    fun limpiarPuntos() {
        _puntos.clear()
    }

    @Composable
    fun Renderizar() {
        Box {
            Column {
                Canvas(
                    modifier = Modifier
                        .padding(margenHorizontal.dp, margenVertical.dp)
                        .aspectRatio(3 / 2f)
                ) {
                    anchoCanvas = size.width
                    altoCanvas = size.height

                    dibujarArea()
                    dibujarPuntos(_puntos)
                }
            }
        }
    }
    //Define el contorno y las lineas de guia
    private fun DrawScope.dibujarArea() {
        val contorno = 1.dp.toPx()
        drawRect(
            color = Color(0, 0, 0, 255),
            style = Stroke(contorno)
        )
        //Definimos la linea de guia vertical
        val verticalLineGuide = 1
        val verticalSize = anchoCanvas / (verticalLineGuide + 1)
        repeat(verticalLineGuide){ i ->
            val startX = verticalSize *(i+1)
            drawLine(
                color = Color(185, 185, 185, 255),
                start = Offset(startX, 0f),
                end = Offset(startX,altoCanvas),
                strokeWidth = contorno
            )
        }
        //Definimos la linea de guia horizontal
        val horizontalLineGuide = 1
        val horizontalSize = altoCanvas / (horizontalLineGuide + 1)
        repeat(horizontalLineGuide){ i ->
            val startY = horizontalSize *(i+1)
            drawLine(
                color = Color(185, 185, 185, 255),
                start = Offset(0F, startY),
                end = Offset(anchoCanvas,startY),
                strokeWidth = contorno
            )
        }
    }
    //Función que dibuja puntos
    private fun DrawScope.dibujarPuntos(puntos: List<Offset>) {
        drawPoints(
            points = puntos,
            pointMode = PointMode.Points,
            color = Color.Red,
            strokeWidth = 5.dp.toPx()
        )
    }
}