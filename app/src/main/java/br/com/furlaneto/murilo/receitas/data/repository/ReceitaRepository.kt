package br.com.furlaneto.murilo.receitas.data.repository

import br.com.furlaneto.murilo.receitas.data.model.Receita
import br.com.furlaneto.murilo.receitas.data.model.RespostaPaginada

interface ReceitaRepository {
    suspend fun obterTodasReceitas(page: Int): RespostaPaginada<Receita>
    suspend fun obterReceitasPorCategoria(categoria: String, page: Int): List<Receita>
    suspend fun obterReceitasPorNome(nome: String): RespostaPaginada<Receita>


}