# Jogo da Velha com Sockets UDP em Java

## Descrição

Este projeto implementa um Jogo da Velha utilizando sockets UDP em Java, desenvolvido para a disciplina de Redes de Computadores. A lógica do jogo é centralizada no servidor, enquanto os clientes são responsáveis por exibir o estado do jogo e enviar suas jogadas. Cada cliente deve ser executado em um computador diferente.

## Estrutura do Projeto

O projeto é dividido em dois componentes principais:

- **Servidor**: Responsável por gerenciar a lógica do jogo, processar as jogadas enviadas pelos clientes, e comunicar o estado atualizado do jogo para todos os clientes conectados.
- **Cliente**: Cada cliente representa um jogador que exibe o estado atual do jogo e envia as jogadas para o servidor.

## Protocolo de Comunicação

- **Protocolo**: UDP (User Datagram Protocol) foi escolhido para a comunicação entre o servidor e os clientes. Embora não garanta a entrega dos pacotes, o protocolo UDP é mais rápido e adequado para este tipo de aplicação onde o desempenho é prioritário.

## Como Executar

1. **Configurar e Iniciar o Servidor**:
   - Compile e execute o arquivo `Server.java` no terminal ou na sua IDE:
     ```bash
     javac Server.java
     java Server
     ```
   - O servidor ficará aguardando as conexões dos clientes. O IP e a porta do servidor devem ser conhecidos pelos clientes.

2. **Configurar e Iniciar os Clientes**:
   - Em máquinas diferentes (ou em terminais diferentes da mesma máquina), compile e execute o arquivo `Client.java`:
     ```bash
     javac Client.java
     java Client
     ```

## Regras do Jogo

- O Jogo da Velha é jogado em uma grade 3x3.
- Dois jogadores se alternam para colocar suas marcas (X ou O) em uma das células vazias.
- O primeiro jogador a alinhar três marcas consecutivas horizontalmente, verticalmente ou diagonalmente vence.
- Se todas as células forem preenchidas sem um vencedor, o jogo termina em empate.


## Autores

Este projeto foi desenvolvido por:

 %% [Wanessa Santana Ferreira](https://github.com/Wanessaa)
 %% [Eliane de Melo Cordeiro](https://github.com/ElianeCordeiro)
