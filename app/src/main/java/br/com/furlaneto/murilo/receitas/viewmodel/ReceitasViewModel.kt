package br.com.furlaneto.murilo.receitas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.furlaneto.murilo.receitas.data.model.Receita
import br.com.furlaneto.murilo.receitas.data.repository.ReceitaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.URLEncoder

@HiltViewModel
class ReceitasViewModel @Inject constructor(
    private val receitaRepository: ReceitaRepository
) : ViewModel() {

    private val _receitas = MutableStateFlow<List<Receita>>(emptyList())
    val receitas: StateFlow<List<Receita>> = _receitas
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var currentPage = 1
    private var isLastPage = false

    fun carregarMaisReceitas() {
        if (_isLoading.value || isLastPage) return

        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resposta = receitaRepository.obterTodasReceitas(currentPage)
                if (resposta.items.isNotEmpty()) {
                    _receitas.value = _receitas.value + resposta.items
                    currentPage++
                } else {
                    isLastPage = true
                }
            } catch (e: Exception) {
                Log.e("ReceitasViewModel", "Erro ao carregar receitas", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun obterTodasReceitas(page: Int) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resposta = receitaRepository.obterTodasReceitas(page)
                _receitas.value = resposta.items
            } catch (e: Exception) {
                Log.e("ReceitasViewModel", "Erro ao obter todas as receitas", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun obterReceitasPorCategoria(categoria: String, page: Int) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val receitasRecebidas =
                    receitaRepository.obterReceitasPorCategoria(categoria, page)
                _receitas.value = receitasRecebidas
            } catch (e: Exception) {
                Log.e("ReceitasViewModel", "Erro ao obter receitas por categoria", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun obterReceitasPorNome(nome: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO){
            try{
                val receitasRecebidas = receitaRepository.obterReceitasPorNome(nome)
                _receitas.value = receitasRecebidas.items
            } catch (e: Exception){
                Log.e("ReceitasViewModel", "Erro ao obter receitas por nome", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
