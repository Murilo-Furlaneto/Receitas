/*package br.com.furlaneto.murilo.receitas.data.datasource.remote.service

import br.com.furlaneto.murilo.receitas.data.model.IngredienteBase
import br.com.furlaneto.murilo.receitas.data.model.Receita
import br.com.furlaneto.murilo.receitas.data.model.RespostaPaginada
import br.com.furlaneto.murilo.receitas.data.model.Meta
import br.com.furlaneto.murilo.receitas.data.model.Links

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.get
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test
import java.util.Date

class ApiServiceTest {

    private val mockJson = Json { ignoreUnknownKeys = true; prettyPrint = true }

    private fun createMockClient(
        responseContent: String,
        responseStatus: HttpStatusCode
    ): HttpClient {
        return HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    if (request.url.toString().startsWith("https://api-receitas-pi.vercel.app/receitas/")) {
                        respond(
                            content = responseContent,
                            status = responseStatus,
                            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        )
                    } else {
                        error("Unhandled request: ${request.url}")
                    }
                }
            }
            install(ContentNegotiation) {
                json(mockJson)
            }
        }
    }

    @Test
    fun `obterTodasReceitas DEVE retornar RespostaPaginada QUANDO a API retornar sucesso (200)`() = runTest {
        // Arrange
        val paginaEsperada = 1
        val timestampAtual = Date().toInstant().toString()

        val mockIngredienteBase = IngredienteBase(
            id = 101,
            nomesIngrediente = listOf("Farinha de Trigo", "Trigo"),
            receitaId = 1,
            createdAt = timestampAtual
        )

        val mockReceita = Receita(
            id = 1,
            receita = "Pão Caseiro",
            ingredientes = "500g de Farinha de Trigo\n10g de Fermento Biológico...",
            modoPreparo = "1. Misture os ingredientes secos...",
            linkImagem = "https://example.com/pao.jpg",
            tipo = "Pães",
            createdAt = timestampAtual,
            ingredientesBase = listOf(mockIngredienteBase)
        )

        val mockMeta = Meta(
            page = paginaEsperada,
            limit = 10,
            itemCount = 1,
            pageCount = 1,
            hasPreviousPage = false,
            hasNextPage = false
        )

        val mockLinks = Links(
            first = "https://api-receitas-pi.vercel.app/receitas/todas?page=1",
            previous = null,
            next = null,
            last = "https://api-receitas-pi.vercel.app/receitas/todas?page=3"
        )

        val mockRespostaPaginada = RespostaPaginada(
            items = listOf(mockReceita),
            meta = mockMeta,
            links = mockLinks
        )
        val mockJsonResponse = mockJson.encodeToString(mockRespostaPaginada)
        println("JSON Esperado:\n$mockJsonResponse")

        val mockHttpClient = createMockClient(mockJsonResponse, HttpStatusCode.OK)
        val apiService = ApiServiceForTest(mockHttpClient)

        // Act
        val resultado = apiService.obterTodasReceitas(paginaEsperada)

        // Assert
        assertNotNull(resultado)
        assertEquals(paginaEsperada, resultado.meta.page)
        assertEquals(1, resultado.meta.itemCount)
        assertEquals(1, resultado.items.size)
        val receitaResultado = resultado.items[0]
        assertEquals("Pão Caseiro", receitaResultado.receita)
        assertEquals("Pães", receitaResultado.tipo)
        assertTrue(receitaResultado.ingredientes.contains("Farinha de Trigo"))
        assertEquals(1, receitaResultado.ingredientesBase.size)
        assertEquals("Farinha de Trigo", receitaResultado.ingredientesBase[0].nomesIngrediente[0])
        assertEquals("https://api-receitas-pi.vercel.app/receitas/todas?page=1", resultado.links.first)
    }

    @Test
    fun `obterTodasReceitas DEVE lançar exceção QUANDO a API retornar erro (não 200)`() = runTest {
        // Arrange
        val paginaEsperada = 1
        val mockHttpClient = createMockClient("Erro no servidor", HttpStatusCode.InternalServerError)
        val apiService = ApiServiceForTest(mockHttpClient)

        // Act & Assert
        val exception = assertThrows(Exception::class.java) {
            runTest {
                apiService.obterTodasReceitas(paginaEsperada)
            }
        }
        assertEquals("Erro ao obter receitas. Status: 500 Internal Server Error", exception.message)
    }

    @Test
    fun `obterTodasReceitas DEVE incluir o parâmetro page na URL da requisição`() = runTest {
        // Arrange
        val paginaParaTestar = 5
        // Ajustar o mockRespostaPaginada para o teste de URL, se necessário, ou usar um mock simples.
        val mockMeta = Meta(paginaParaTestar, 10, 0, 1, false, false)
        val mockLinks = Links("first", null, null, "last")
        val mockRespostaPaginada = RespostaPaginada<Receita>(emptyList(), mockMeta, mockLinks)
        val mockJsonResponse = mockJson.encodeToString(mockRespostaPaginada)
        var requisicaoUrl = ""

        val mockEngine = MockEngine { request ->
            requisicaoUrl = request.url.toString()
            respond(
                content = mockJsonResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val mockHttpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) { json(mockJson) }
        }
        val apiService = ApiServiceForTest(mockHttpClient)

        // Act
        apiService.obterTodasReceitas(paginaParaTestar)

        // Assert
        assertTrue("A URL deve conter '?page=$paginaParaTestar'", requisicaoUrl.contains("?page=$paginaParaTestar"))
        assertEquals("https://api-receitas-pi.vercel.app/receitas/todas?page=$paginaParaTestar", requisicaoUrl)
    }
}

// Classe auxiliar ApiServiceForTest (permanece a mesma da resposta anterior,
// mas lembre-se das sugestões para refatorar ApiService para melhor testabilidade)
private class ApiServiceForTest(private val mockClient: HttpClient) {
    private val realApiServiceForBaseUrl = ApiService()
    private val baseUrl: String

    init {
        val field = ApiService::class.java.getDeclaredField("baseUrl")
        field.isAccessible = true
        baseUrl = field.get(realApiServiceForBaseUrl) as String
    }

    val client: HttpClient = mockClient

    suspend fun obterTodasReceitas(page: Int): RespostaPaginada<Receita> {
        val response: HttpResponse = this.client.get("$baseUrl/receitas/todas?page=$page")
        if (response.status.value == 200) {
            val jsonString = response.bodyAsText()
            val json = Json { ignoreUnknownKeys = true }
            return json.decodeFromString<RespostaPaginada<Receita>>(jsonString)
        } else {
            throw Exception("Erro ao obter receitas. Status: ${response.status}")
        }
    }
}
*/