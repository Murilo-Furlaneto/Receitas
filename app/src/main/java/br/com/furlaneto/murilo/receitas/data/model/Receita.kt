package br.com.furlaneto.murilo.receitas.data.model
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Receita(
    val id: Int,
    val receita: String,
    val ingredientes: String,
    @Json(name = "modo_preparo")
    val modoPreparo: String,
    @Json(name = "link_imagem")
    val linkImagem: String,
    val tipo: String,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "IngredientesBase")
    val ingredientesBase: List<IngredienteBase>
)


@JsonClass(generateAdapter = true)
data class IngredienteBase(
    val id: Int,
    val nomesIngrediente: List<String>,
    @Json(name = "receita_id")
    val receitaId: Int,
    @Json(name = "created_at")
    val createdAt: String
)

