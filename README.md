# CIRCLE VIEW

**Circle View** é um projeto que oferece uma API que recebe solicitações com dados de JSON ou/e imagens, podendo assim, retornar resultados de treinos/avaliações no modelo de machine learning.

## Arquitetura

O projeto está dividido em três camadas:

**Camada de API (ml-api):** responsável por receber as solicitações HTTP e enviar as respostas correspondentes. Nesse caso, é implementado um servidor HTTP em Java que usa a biblioteca Spark para lidar com as solicitações e respostas.

**Camada de ML (ml-dl):** responsável por executar algortimos nos modelos de machine learning, implementado com Tensorflow e Keras, podendo assim, serem usados modelos de redes neurais convolucionais, recorrentes, etc.

**Camada de Front (ml-front):** responsável em receber as imagens e enviar para a API, podendo assim, retornar os resultados de treinos/avaliações no modelo de machine learning.

## Status

**Camada de API:** Em desenvolvimento.

**Camada de ML:** Em desenvolvimento.

**Camada de Front:** Em desenvolvimento.