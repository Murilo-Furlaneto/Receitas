package br.com.furlaneto.murilo.receitas.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class  RespostaPaginada<T>(
    val items: List<Receita>,
)




