package br.com.furlaneto.murilo.receitas.ui.view.pages

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.furlaneto.murilo.receitas.data.model.Receita
import br.com.furlaneto.murilo.receitas.ui.widgets.BottomMenu
import br.com.furlaneto.murilo.receitas.ui.widgets.ReceitaCard
import br.com.furlaneto.murilo.receitas.viewmodel.ReceitasViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    var selectedItem by remember { mutableStateOf("home") }
    var searchText by remember { mutableStateOf("") }
    val viewModel: ReceitasViewModel = hiltViewModel()
    val receitas by viewModel.receitas.collectAsState()
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val moshi = remember {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    val receitaAdapter = remember { moshi.adapter(Receita::class.java) }

    LaunchedEffect(listState) {
        viewModel.obterTodasReceitas(1)
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { index ->
                if (searchText.isBlank() && index != null && receitas.isNotEmpty() && index >= receitas.lastIndex - 2) {
                    viewModel.carregarMaisReceitas()
                }
            }
    }

    LaunchedEffect(searchText) {
        if (searchText.isNotBlank()) {
            delay(500L)
            viewModel.obterReceitasPorNome(searchText)
        } else {
            viewModel.obterTodasReceitas(1)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Todas as Receitas") }
            )
        },
        bottomBar = {
            BottomMenu(
                selectedItem = selectedItem,
                onItemSelected = { item ->
                    selectedItem = item
                    if (navController.currentDestination?.route != item) {
                        when (item) {
                            "home" -> navController.navigate("home")
                            "categories" -> navController.navigate("categories")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Pesquisar receita") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.obterReceitasPorNome(searchText)
                        keyboardController?.hide()
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Blue,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color(0xFFF0F0F0),
                    unfocusedContainerColor = Color(0xFFF0F0F0),
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
            )

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                if (receitas.isEmpty() && viewModel.isLoading.value) {
                    item {
                        Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                } else if (receitas.isEmpty()) {
                    item {
                        Text(
                            text = "Nenhuma receita encontrada",
                            modifier = Modifier
                                .fillParentMaxSize()
                                .wrapContentSize(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    items(receitas.size) { index ->
                        val receita = receitas[index]
                        ReceitaCard(receita = receita) {
                            val receitaJson = Uri.encode(receitaAdapter.toJson(receita))
                            navController.navigate("details/$receitaJson")
                        }
                    }
                }

                if (viewModel.isLoading.value && receitas.isNotEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}