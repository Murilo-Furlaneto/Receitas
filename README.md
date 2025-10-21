# App de Receitas

Um aplicativo de receitas moderno e funcional, desenvolvido em **Kotlin** utilizando as mais recentes tecnologias do ecossistema Android, como **Jetpack Compose**, **Retrofit** e **Hilt**.

## Características

-   Navegação por categorias de receitas.
-   Visualização detalhada de cada receita, incluindo imagem e modo de preparo.
-   Guia de preparo interativo com checkboxes para acompanhar cada passo.
-   Interface de usuário reativa e moderna construída inteiramente com Jetpack Compose.
-   Consumo de API REST para busca de dados de forma assíncrona.
-   Design limpo e intuitivo baseado no Material 3.

## Tecnologias Utilizadas

-   **Kotlin**: Linguagem principal, com foco em código conciso e seguro.
-   **Jetpack Compose**: Toolkit declarativo para a construção da UI.
-   **Retrofit & OkHttp**: Para realizar chamadas de rede à API de receitas.
-   **Hilt**: Para injeção de dependências, simplificando a arquitetura.
-   **Compose Navigation**: Para gerenciar a navegação entre as telas do app.
-   **Coroutines & StateFlow**: Para gerenciamento de operações assíncronas e estado da UI.
-   **Coil**: Para carregamento eficiente de imagens da internet.
-   **Android Architecture Components**: (ViewModel) para seguir as melhores práticas de arquitetura.

## Arquitetura

O projeto segue o padrão de arquitetura **MVVM (Model-View-ViewModel)**, garantindo uma separação clara de responsabilidades:

-   **UI (View)**: Telas construídas com Jetpack Compose (`CategoriesPage`, `DetailsPage`, etc.).
-   **ViewModel**: Contém a lógica de apresentação e gerencia o estado da UI usando `StateFlow`.
-   **Data (Model)**:
    -   **Repository**: Abstrai a fonte de dados (neste caso, a API remota).
    -   **Remote DataSource**: Usa o Retrofit para se comunicar com a API.

## Como Executar o Projeto

1.  Clone este repositório:
    ```bash
    git clone https://github.com/seu-usuario/nome-do-repositorio.git
    ```
2.  Abra o projeto no Android Studio (versão recomendada: Hedgehog ou mais recente).
3.  Aguarde a sincronização das dependências do Gradle.
4.  Conecte um dispositivo físico ou inicie um emulador Android.
5.  Execute o aplicativo.

## Próximos Passos para o Projeto

-   [ ] Implementar uma função de busca de receitas por nome ou ingredientes.
-   [ ] Adicionar uma tela de "Favoritos" com persistência local (usando Room Database).
-   [ ] Criar testes unitários para os ViewModels e Repositórios.
-   [ ] Adicionar animações para melhorar a experiência do usuário.
## Contribuição

Contribuições são muito bem-vindas! Sinta-se à vontade para abrir *issues*, sugerir melhorias ou enviar *pull requests*.
