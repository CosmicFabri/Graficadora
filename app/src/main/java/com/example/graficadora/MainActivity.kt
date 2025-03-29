package com.example.graficadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.graficadora.ui.theme.GraficadoraTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import kotlin.math.roundToInt


class MainActivity : ComponentActivity() {
    lateinit var graficadora: Graficadora

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        graficadora = Graficadora()

        enableEdgeToEdge()
        setContent {
            GraficadoraTheme {
                VistaPrincipal(graficadora)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VistaPrincipal(graficadora: Graficadora, modifier: Modifier = Modifier) {
    val opcionesEvaluar = arrayOf(
        stringResource(R.string.selectEvaluarPorPunto),
        stringResource(R.string.selectEvaluarPorIntervalo))

    // Punto o intervalo
    var opcionSeleccionada by remember { mutableStateOf(opcionesEvaluar[0]) }

    // La función "y" que ingresa el usuario
    var funcion by remember { mutableStateOf("") }

    // Este es el punto x que se va a evaluar en la función "y"
    var posicionSlider by remember { mutableFloatStateOf(0f) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(text = stringResource(R.string.topAppBarText))
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.bottomAppBarText)
                )
            }
        }
    ) { contentPadding ->
        Column(
            modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            // Plano cartesiano
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Graficador(graficadora)
            }

            // Formulario
            Column(
                modifier
                    .padding(top = 10.dp, bottom = 0.dp)
                    .fillMaxWidth()
                    .weight(1.5f),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // Entrada del usuario
                EntradaUsuario(
                    modifier = modifier,
                    funcion = funcion,
                    onFuncionChange = { newValue -> funcion = newValue }
                )

                // Evaluar por punto o intervalo
                EvaluarPor(modifier, opcionesEvaluar) { nuevaOpcion ->
                    opcionSeleccionada = nuevaOpcion
                }

                // Seleccionar punto a evaluar
                SeleccionPunto(
                    modifier = modifier,
                    opcionSeleccionada = opcionSeleccionada,
                    posicionSlider = posicionSlider,
                    onPosicionSliderChange = { newValue ->
                        posicionSlider = newValue
                    }
                )

                // Seleccionar un dominio a evaluar
                SeleccionDominio(modifier, opcionSeleccionada)

                // Graficar la función
                BotonGraficar(
                    graficadora = graficadora,
                    funcion = funcion,
                    punto = posicionSlider,
                    opcion = opcionSeleccionada,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun Graficador(graficadora: Graficadora) {
    // val graficadora = remember { Graficadora() }
    graficadora.Renderizar()
}

@Composable
fun EntradaUsuario(
    modifier: Modifier,
    funcion: String,
    onFuncionChange: (String) -> Unit 
) {
    Row(
        modifier
            .padding(start = 20.dp)
            .fillMaxWidth(0.95f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = stringResource(R.string.labelY))
        TextField(
            value = funcion,
            onValueChange = onFuncionChange,  // Usamos el callback
            modifier = Modifier.width(320.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluarPor(modifier: Modifier, opcionesEvaluar: Array<String>, onSelectionChange: (String) -> Unit) {
    var menuExpandido by remember { mutableStateOf(false) }
    var opcionSeleccionada by remember { mutableStateOf(opcionesEvaluar[0]) }

    Row(
        modifier
            .padding(start = 20.dp)
            .fillMaxWidth(0.95f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = stringResource(R.string.labelEvaluarPor))
        ExposedDropdownMenuBox(
            expanded = menuExpandido,
            onExpandedChange = {
                menuExpandido = !menuExpandido
            },
            modifier.width(250.dp)
        ) {
            TextField(
                value = opcionSeleccionada,
                onValueChange = { },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpandido) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = menuExpandido,
                onDismissRequest = { menuExpandido = false }
            ) {
                opcionesEvaluar.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            opcionSeleccionada = item
                            menuExpandido = false
                            onSelectionChange(item)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SeleccionPunto(
    modifier: Modifier,
    opcionSeleccionada: String,
    posicionSlider: Float,
    onPosicionSliderChange: (Float) -> Unit
) {
    Row(
        modifier
            .padding(start = 20.dp)
            .fillMaxWidth(0.95f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = stringResource(R.string.labelMinimo))
        Slider(
            modifier = Modifier
                .width(300.dp),
            value = posicionSlider,
            onValueChange = onPosicionSliderChange,
            enabled = opcionSeleccionada.equals("Punto"),
            steps = 17,
            valueRange = -9f..9f
        )
        Text(text = stringResource(R.string.labelMaximo))
    }
    Text(
        text = posicionSlider.roundToInt().toString(),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun SeleccionDominio(modifier: Modifier, opcionSeleccionada: String) {
    Row(
        modifier
            .padding(start = 20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.labelDominio))
        Row(
            modifier
                .padding(start = 5.dp, end = 5.dp)
                .fillMaxWidth(0.95f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            SelectInicioIntervalo(modifier.width(74.dp), opcionSeleccionada)
            TextFieldIntervalo(modifier.width(60.dp), opcionSeleccionada)
            Text(text = ",")
            TextFieldIntervalo(modifier.width(60.dp), opcionSeleccionada)
            SelectFinalIntervalo(modifier.width(74.dp), opcionSeleccionada)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectInicioIntervalo(modifier: Modifier, opcionSeleccionada: String) {
    val opcionesEvaluar = arrayOf(
        stringResource(R.string.selectIntervaloCerradoInicio),
        stringResource(R.string.selectIntervaloAbiertoInicio)
    )
    var menuExpandido by remember { mutableStateOf(false) }
    var intervaloSeleccionado by remember { mutableStateOf(opcionesEvaluar[0]) }

    ExposedDropdownMenuBox(
        expanded = menuExpandido,
        onExpandedChange = {
            if (opcionSeleccionada.equals("Intervalo"))
                menuExpandido = !menuExpandido
        },
        modifier.width(250.dp)
    ) {
        TextField(
            value = intervaloSeleccionado,
            onValueChange = { },
            readOnly = true,
            enabled = opcionSeleccionada.equals("Intervalo"),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpandido) },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = menuExpandido,
            onDismissRequest = { menuExpandido = false }
        ) {
            opcionesEvaluar.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        intervaloSeleccionado = item
                        menuExpandido = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFinalIntervalo(modifier: Modifier, opcionSeleccionada: String) {
    val opcionesEvaluar = arrayOf(
        stringResource(R.string.selectIntervaloCerradoFinal),
        stringResource(R.string.selectIntervaloAbiertoFinal)
    )
    var menuExpandido by remember { mutableStateOf(false) }
    var intervaloSeleccionado by remember { mutableStateOf(opcionesEvaluar[0]) }

    ExposedDropdownMenuBox(
        expanded = menuExpandido,
        onExpandedChange = {
            if (opcionSeleccionada.equals("Intervalo"))
                menuExpandido = !menuExpandido
        },
        modifier.width(250.dp)
    ) {
        TextField(
            value = intervaloSeleccionado,
            onValueChange = { },
            readOnly = true,
            enabled = opcionSeleccionada.equals("Intervalo"),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpandido) },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = menuExpandido,
            onDismissRequest = { menuExpandido = false }
        ) {
            opcionesEvaluar.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        intervaloSeleccionado = item
                        menuExpandido = false
                    }
                )
            }
        }
    }
}

@Composable
fun TextFieldIntervalo(modifier: Modifier, opcionSeleccionada: String) {
    var texto by remember { mutableStateOf("") }

    Box(modifier) {
        TextField(
            enabled = opcionSeleccionada.equals("Intervalo"),
            value = texto,
            onValueChange = { texto = it }
        )
    }
}

@Composable
fun BotonGraficar(
    graficadora: Graficadora,
    funcion: String,
    punto: Float,
    opcion: String,
    modifier: Modifier
) {
    Button(
        onClick = {
            when (opcion) {
                "Punto" -> {
                    // Graficar solo el par de puntos
                    // graficadora.dibujarParPuntos(funcion, punto)
                    // Ver las medidas del canva
                    graficadora.agregarPunto(100f, 0f)
                    graficadora.agregarPunto(200f, 0f)
                    graficadora.agregarPunto(300f, 0f)
                    graficadora.agregarPunto(400f, 0f)
                    graficadora.agregarPunto(500f, 0f)

                    graficadora.agregarPunto(0f, 100f)
                    graficadora.agregarPunto(0f, 200f)
                    graficadora.agregarPunto(0f, 300f)
                    graficadora.agregarPunto(0f, 400f)
                }
                "Intervalo" -> {

                }
            }
        },
        modifier
            .padding(start = 20.dp)
            .fillMaxWidth(0.95f)
    ) {
        Text(text = stringResource(R.string.btnGraficar))
    }
}