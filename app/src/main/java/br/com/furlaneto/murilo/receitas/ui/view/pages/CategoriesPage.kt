package br.com.furlaneto.murilo.receitas.ui.view.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.furlaneto.murilo.receitas.data.enum.EnumTipoReceita
import br.com.furlaneto.murilo.receitas.ui.widgets.BottomMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesPage(
    modifier: Modifier = Modifier,
    navController: NavController
){
    var selectedItem by remember { mutableStateOf("categories") }

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Categorias") },
                modifier = modifier.background(MaterialTheme.colorScheme.primary)
            )
        },
        bottomBar = {
            BottomMenu(
                selectedItem = selectedItem,
                onItemSelected = {item ->
                    selectedItem = item
                    when(item){
                        "home" -> navController.navigate("home")
                        "categories" -> navController.navigate("categories")
                    }
                }
            )
        }
    ){
            innerPadding ->
        val tiposReceita = EnumTipoReceita.values()
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            tiposReceita.forEach { tipoReceita ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                        .clickable {
                            navController.navigate("explorer/${tipoReceita.name}")
                        },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.LightGray,
                                        Color.White
                                    )
                                )
                            )
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tipoReceita.name,
                            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

