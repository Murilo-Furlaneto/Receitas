package br.com.furlaneto.murilo.receitas.data.repository

import br.com.furlaneto.murilo.receitas.data.datasource.remote.service.ApiService
import br.com.furlaneto.murilo.receitas.data.model.Receita
import br.com.furlaneto.murilo.receitas.data.model.RespostaPaginada
import jakarta.inject.Inject

    class ReceitaRepositoryImpl @Inject constructor
        (private val apiService: ApiService) : ReceitaRepository {

        override suspend fun obterTodasReceitas(page: Int): RespostaPaginada<Receita> {
            return apiService.obterTodasReceitas(page)
        }

        override suspend fun obterReceitasPorCategoria(categoria: String, page: Int): List<Receita> {
            return apiService.obterReceitasPorCategoria(categoria, page)
        }

        override suspend fun obterReceitasPorNome(nome: String): RespostaPaginada<Receita> {
            return apiService.obterReceitasPorNome(nome)
        }
    }


