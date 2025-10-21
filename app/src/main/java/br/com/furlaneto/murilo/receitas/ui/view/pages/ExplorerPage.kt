package br.com.furlaneto.murilo.receitas.ui.view.pages
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.furlaneto.murilo.receitas.data.model.Receita
import br.com.furlaneto.murilo.receitas.viewmodel.ReceitasViewModel
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import android.net.Uri
import androidx.compose.runtime.remember
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import br.com.furlaneto.murilo.receitas.ui.widgets.ReceitaCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplorerPage(
    modifier: Modifier = Modifier,
    categoria: String,
    navController: NavController
) {
    val viewModel: ReceitasViewModel = hiltViewModel()

    LaunchedEffect(categoria) {
        viewModel.obterReceitasPorCategoria(categoria.lowercase(), page = 1)
    }

    val receitasState = viewModel.receitas.collectAsState()

    val moshi = remember {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    val receitaAdapter = remember { moshi.adapter(Receita::class.java) }

    Scaffold(

    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding).padding(16.dp)) {

            items(receitasState.value) { receita ->
                ReceitaCard(receita = receita) {
                    val receitaJson = Uri.encode(receitaAdapter.toJson(receita))
                    navController.navigate("details/$receitaJson")
                }
            }
        }
    }
}
