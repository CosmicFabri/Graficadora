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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GraficadoraTheme {
                VistaPrincipal()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VistaPrincipal(modifier: Modifier = Modifier) {
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
                Graficador(modifier)
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
                EntradaUsuario(modifier)

                // Evaluar por punto o intervalo
                EvaluarPor(modifier)

                // Rango del dominio
                RangoDominio(modifier)

                SeleccionDominio(modifier)

                BotonGraficar(modifier)
            }
        }
    }
}

@Composable
fun Graficador(modifier: Modifier) {
    val graficadora = remember { Graficadora() }
    graficadora.Renderizar()
}

@Composable
fun EntradaUsuario(modifier: Modifier) {
    var funcion by remember { mutableStateOf("") }

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
            onValueChange = { funcion = it },
            modifier.width(320.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluarPor(modifier: Modifier) {
    val opcionesEvaluar = arrayOf(
        stringResource(R.string.selectEvaluarPorPunto),
        stringResource(R.string.selectEvaluarPorIntervalo))
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
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RangoDominio(modifier: Modifier) {
    var posicionSlider by remember { mutableStateOf(0f) }

    Row(
        modifier
            .padding(start = 20.dp)
            .fillMaxWidth(0.95f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = stringResource(R.string.labelMinimo))
        Slider(
            value = posicionSlider,
            onValueChange = { posicionSlider = it },
            modifier = Modifier
                .width(300.dp)
        )
        Text(text = stringResource(R.string.labelMaximo))
    }
}

@Composable
fun SeleccionDominio(modifier: Modifier) {
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
            SelectInicioIntervalo(modifier.width(74.dp))
            TextFieldIntervalo(modifier.width(60.dp))
            Text(text = ",")
            TextFieldIntervalo(modifier.width(60.dp))
            SelectFinalIntervalo(modifier.width(74.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectInicioIntervalo(modifier: Modifier) {
    val opcionesEvaluar = arrayOf(
        stringResource(R.string.selectIntervaloCerradoInicio),
        stringResource(R.string.selectIntervaloAbiertoInicio)
    )
    var menuExpandido by remember { mutableStateOf(false) }
    var opcionSeleccionada by remember { mutableStateOf(opcionesEvaluar[0]) }

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
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFinalIntervalo(modifier: Modifier) {
    val opcionesEvaluar = arrayOf(
        stringResource(R.string.selectIntervaloCerradoFinal),
        stringResource(R.string.selectIntervaloAbiertoFinal)
    )
    var menuExpandido by remember { mutableStateOf(false) }
    var opcionSeleccionada by remember { mutableStateOf(opcionesEvaluar[0]) }

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
                    }
                )
            }
        }
    }
}

@Composable
fun TextFieldIntervalo(modifier: Modifier) {
    var texto by remember { mutableStateOf("") }

    Box(modifier) {
        TextField(
            value = texto,
            onValueChange = { texto = it }
        )
    }
}

@Composable
fun BotonGraficar(modifier: Modifier) {
    Button(
        onClick = {},
        modifier
            .padding(start = 20.dp)
            .fillMaxWidth(0.95f)
    ) {
        Text(text = stringResource(R.string.btnGraficar))
    }
}