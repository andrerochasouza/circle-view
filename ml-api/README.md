# CIRCLE VIEW

**Circle View** é um projeto que oferece uma API em Java que recebe solicitações com dados de entrada em formato JSON e retorna resultados de treinos/avaliações no modelo de machine learning. 
## Arquitetura
O projeto está dividido em três camadas:

**Camada de API:** responsável por receber as solicitações HTTP e enviar as respostas correspondentes. Nesse caso, é implementado um servidor HTTP em Java que usa a biblioteca Spark para lidar com as solicitações e respostas.

**Camada de ML:** responsável por executar o feedforward e backpropagation nos modelos de machine learning treinados. Nesse caso, é implementado um modelo de rede neural artificial simples com uma camada de entrada, uma camada oculta e uma camada de saída (Camadas Ocultas/Saidas limitados a 10 nós).

**Camada de dados:** responsável por armazenar os modelos de machine learning treinados e os dados de entrada. Nesse caso, os dados de entrada são passados diretamente para a API em formato JSON. Os modelos treinados são armazenados em disco e carregados na memória quando a API é iniciada.

## Como usar
Para executar a API, é necessário ter o Java 11 ou superior instalado na máquina.

**Clone o repositório do projeto:**
```bash
git clone https://github.com/seu-usuario/circle-view.git
```

**Acesse a pasta do projeto:**

```bash
cd circle-view
```

**Compile o projeto usando o Maven:**
```bash
mvn clean package
```

**Execute o servidor HTTP:**
```bash
java -jar target/circle-view-1.0.0.jar
```

**Agora a API está em execução na porta 4567.** 

Você pode testar as solicitações usando um cliente HTTP, como o cURL ou o Postman.

## GET /healthcheck
Retorna um status HTTP 200 OK se o servidor estiver em execução e saudável.

Exemplo de resposta:

```bash
Status: 200 OK
Body: {
  "status": "OK"
}
```

## POST /feedforward
Executa uma passagem de **feedforward** no modelo de machine learning e retorna as saídas correspondentes. O corpo da solicitação deve incluir os dados de entrada em formato JSON.

**Exemplo de solicitação:**

```bash
POST /feedforward HTTP/1.1
Content-Type: application/json

{
  "inputs": [0.5, 0.3, 0.2, 0.1, 0.4, 0.6, 0.7, 0.8, 0.9]
}
```

**Exemplo de resposta:**

```bash
Status: 200 OK
Body: {
  "outputs": [0.1, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 0.2, 0.3]
}
```

## POST /train
Executa um ciclo de **treinamento** no modelo de machine learning com os dados de entrada fornecidos e retorna o erro de treinamento correspondente. O corpo da solicitação deve incluir os dados de entrada e saída esperados em formato JSON.

**Exemplo de solicitação:**

```bash
POST /train HTTP/1.1
Content-Type: application/json

{
    "inputs": [0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9],
    "outputs": [0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1]
}
```

**Exemplo de resposta:**

```bash
Status: 200 OK
Body: {
  "error": 0.0123456789
}
```

## Licença
Este projeto está licenciado sob a licença MIT. Consulte o arquivo LICENSE para obter mais informações.