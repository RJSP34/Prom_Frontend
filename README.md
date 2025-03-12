# Projeto de Programação de Aplicações Móveis
2022-2023

- João Lopes - 37133
- Rúben Pinto - 40115

## Descrição
Esta aplicação serve para um utilizador criar um backlog de filmes, ou seja, uma lista de filmes que o utilizador pretenda ver ou já viu. Este poderá ver uma lista de filmes presente na base de dados e adicioná-los para o seu backlog. No seu backlog pode indicar o estado do filme (Plan To Watch, Watching, Watched,...), a data de quando assistiu e uma nota pessoal de 0 a 10. Esta lista de filmes do seu backlog também poderá ser vista e é possível remover filmes.

## Requisitos funcionais

- UI standard design usado: Nav Drawer
- A app usa a biblioteca Room para implementar uma base de dados local com SQLite
- A app usa Activities e Fragments para o utilizador interagir com as diferentes páginas.
- A app usa um Intent Service para produzir um som em determinados botões
- A app comunica com um serviço de backend criado em Flask e os dados são armazenados numa base de dados MySQL

## Serviço backend
No link abaixo está o repositório do backend desta aplicação. Junto do código estão também os scripts usados para criar a base de dados em MySQL.
[Link para o repositório do Backend](https://github.com/40115/BackendProm)
