package com.mpazpro3.app.ui.screen.docente

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mpazpro3.app.navigation.Rutas
import com.mpazpro3.app.ui.components.BarraSuperior
import com.mpazpro3.app.viewmodel.MpazProViewModel

@Composable
fun ResultadosCursoScreen(navController: NavHostController, viewModel: MpazProViewModel) {
    val usuario by viewModel.usuarioActivo.collectAsState()
    val estudiantes by viewModel.estudiantes.collectAsState()
    val curso = usuario?.curso.orEmpty()
    val context = LocalContext.current

    Scaffold(topBar = { BarraSuperior("Resultados del curso", navController) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val archivoGenerado = viewModel.descargarResultados(context, curso)
                    if (archivoGenerado != null && archivoGenerado.exists()) {
                        Toast.makeText(
                            context,
                            "Archivo guardado: ${archivoGenerado.name}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "No se pudo crear el archivo de resultados",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Text("Descargar resultados del curso")
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(estudiantes) { estudiante ->
                    val avance = viewModel.progresoGeneral(estudiante.id)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.seleccionarEstudiante(estudiante.id)
                                navController.navigate(Rutas.DOCENTE_INFORME)
                            }
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(estudiante.nombreFicticio, style = MaterialTheme.typography.titleSmall)
                            LinearProgressIndicator(
                                progress = { avance },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                "Avance: ${(avance * 100).toInt()}% · Toca para ver e informar",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
}