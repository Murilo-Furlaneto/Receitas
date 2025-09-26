package br.com.furlaneto.murilo.receitas.viewmodel

import androidx.lifecycle.ViewModel
import br.com.furlaneto.murilo.receitas.data.model.Receita
import br.com.furlaneto.murilo.receitas.data.model.RespostaPaginada
import br.com.furlaneto.murilo.receitas.data.repository.ReceitaRepository // Alterado para a interface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class ReceitasViewModel @Inject constructor(
    private val receitaRepository : ReceitaRepository
)  : ViewModel() {

    private val _receitas = MutableStateFlow<List<Receita>>(emptyList())
    val receitas: StateFlow<List<Receita>> = _receitas

    private var currentPage = 1
    private var isLoading = false
    private var isLastPage = false

    suspend fun carregarMaisReceitas() {
        if (isLoading || isLastPage) return

        isLoading = true
        val resposta = receitaRepository.obterTodasReceitas(currentPage)
        if (resposta.items.isNotEmpty()) {
            _receitas.value = _receitas.value + resposta.items
            currentPage++
        } else {
            isLastPage = true
        }
        isLoading = false
    }

    suspend fun obterTodasReceitas(page: Int) : RespostaPaginada<Receita> {
        val resposta = receitaRepository.obterTodasReceitas(page)
        _receitas.value = resposta.items
        return resposta
    }

    suspend fun obterReceitasPorCategoria(categoria: String, page: Int): List<Receita> {
        val receitasRecebidas =
            receitaRepository.obterReceitasPorCategoria(categoria, page)
        _receitas.value = emptyList()
        _receitas.value = receitasRecebidas
        return receitasRecebidas
    }

}