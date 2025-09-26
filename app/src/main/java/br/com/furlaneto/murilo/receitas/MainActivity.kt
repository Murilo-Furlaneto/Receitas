package br.com.furlaneto.murilo.receitas

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.furlaneto.murilo.receitas.ui.theme.ReceitasTheme
import br.com.furlaneto.murilo.receitas.ui.view.pages.ExplorerPage
import br.com.furlaneto.murilo.receitas.ui.view.pages.HomePage
import br.com.furlaneto.murilo.receitas.viewmodel.ReceitasViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import br.com.furlaneto.murilo.receitas.data.model.Receita
import br.com.furlaneto.murilo.receitas.ui.view.pages.CategoriesPage
import br.com.furlaneto.murilo.receitas.ui.view.pages.DetailsPage
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.serialization.decodeFromString


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReceitasTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ReceitasTheme {
        Greeting("Android")
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val receitasViewModel: ReceitasViewModel = hiltViewModel()


    val moshi = remember {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    val receitaAdapter = remember {
        moshi.adapter(Receita::class.java)
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomePage(
                modifier = Modifier,
                navController = navController
            )
        }
        composable(
            route = "explorer/{categoria}",
            arguments = listOf(navArgument("categoria") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoria = backStackEntry.arguments?.getString("categoria") ?: ""
            ExplorerPage(
                categoria = categoria,
                modifier = Modifier,
                navController = navController
            )
        }
        composable(
            route = "details/{receitaJson}",
            arguments = listOf(navArgument("receitaJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val receitaJsonEncoded = backStackEntry.arguments?.getString("receitaJson") ?: ""
            val receitaJson = Uri.decode(receitaJsonEncoded)
            val receita = receitaAdapter.fromJson(receitaJson)

            if (receita != null) {
                DetailsPage(
                    modifier = Modifier,
                    receita = receita
                )
            } else {
                Text("Erro ao carregar detalhes da receita")
            }
        }
        composable(
             "categories"
        ){
            CategoriesPage(
                modifier = Modifier,
                navController = navController
            )
        }
    }
}
