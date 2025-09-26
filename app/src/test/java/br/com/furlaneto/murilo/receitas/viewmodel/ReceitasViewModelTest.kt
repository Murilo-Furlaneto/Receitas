import br.com.furlaneto.murilo.receitas.data.model.IngredienteBase
import br.com.furlaneto.murilo.receitas.data.model.Receita
import br.com.furlaneto.murilo.receitas.data.repository.ReceitaRepository
import br.com.furlaneto.murilo.receitas.viewmodel.ReceitasViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ReceitasViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockReceitaRepository: ReceitaRepository

    private lateinit var viewModel: ReceitasViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ReceitasViewModel(mockReceitaRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `obterReceitasPorCategoria deve atualizar StateFlow e retornar lista de receitas com sucesso`() = runTest(testDispatcher) {
        val categoriaTeste = "salgado"
        val paginaTeste = 1

        val listaDeReceitasEsperada = listOf(
            Receita(
                id = 1,
                receita = "Torta de limao",
                ingredientes = "Ingredientes da torta",
                modoPreparo = "Modo de preparo da torta",
                linkImagem = "foto1.jpg",
                tipo = "salgado",
                createdAt = "",
                ingredientesBase = emptyList()
            ),
            Receita(
                id = 2,
                receita = "Bolo",
                ingredientes = "Ingredientes do bolo",
                modoPreparo = "Modo de preparo do bolo",
                linkImagem = "foto2.jpg",
                tipo = "doce",
                createdAt = "",
                ingredientesBase = listOf(
                    IngredienteBase(
                        id = 1,
                        nomesIngrediente = listOf("farinha", "ovo"),
                        receitaId = 2,
                        createdAt = ""
                    )
                )
            )
        )

        whenever(mockReceitaRepository.obterReceitasPorCategoria(categoriaTeste, paginaTeste))
            .thenReturn(listaDeReceitasEsperada)

        val resultado = viewModel.obterReceitasPorCategoria(categoriaTeste, paginaTeste)

        advanceUntilIdle()

        assertEquals(listaDeReceitasEsperada, resultado)
        val receitasNoStateFlow = viewModel.receitas.first()
        assertEquals(listaDeReceitasEsperada, receitasNoStateFlow)
    }

    @Test
    fun `obterReceitasPorCategoria deve retornar lista vazia se o repositorio retornar vazia`() = runTest(testDispatcher) {
        val categoriaTeste = "doce"
        val paginaTeste = 1
        val listaVaziaEsperada = emptyList<Receita>()

        whenever(mockReceitaRepository.obterReceitasPorCategoria(categoriaTeste, paginaTeste))
            .thenReturn(listaVaziaEsperada)

        val resultado = viewModel.obterReceitasPorCategoria(categoriaTeste, paginaTeste)
        advanceUntilIdle()

        assertEquals(listaVaziaEsperada, resultado)
        assertEquals(listaVaziaEsperada, viewModel.receitas.first())
    }
}
