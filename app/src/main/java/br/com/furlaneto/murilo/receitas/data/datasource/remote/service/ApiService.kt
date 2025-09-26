package br.com.furlaneto.murilo.receitas.data.datasource.remote.service
import br.com.furlaneto.murilo.receitas.data.model.Receita
import br.com.furlaneto.murilo.receitas.data.model.RespostaPaginada
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("receitas/todas")
    suspend fun obterTodasReceitas(@Query("page") page: Int): RespostaPaginada<Receita>

    @GET("receitas/tipo/{categoria}")
    suspend fun obterReceitasPorCategoria(
        @Path("categoria") categoria: String,
        @Query("page") page: Int
    ): List<Receita>
}
