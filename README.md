# 🌤️ Sistema de Previsão do Tempo com Spring Boot e gRPC

O sistema é composto por duas aplicações Spring Boot: uma que atua como servidor gRPC, oferecendo serviços meteorológicos simulados;e outra como cliente REST, que converte chamadas HTTP em chamadas gRPC.
O cliente usa o @GrpcClient do Spring para se comunicar via MeteorologiaServiceGrpc.MeteorologiaServiceBlockingStub, conectando-se ao servidor gRPC que escuta na porta 9090. 
As operações disponíveis incluem cadastro e listagem de cidades, previsão de temperatura e estatísticas climáticas.

---

## 📦 Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Spring Boot gRPC
- Protocol Buffers (`.proto`)
- Maven

---

## ⚙️ Estrutura do Sistema

### 🔹 Servidor gRPC

- Implementa o serviço `MeteorologiaService` definido no arquivo `.proto`.
- Simula um "banco de dados" com um `ConcurrentHashMap<String, List<Float>>` para armazenar cidades e suas temperaturas.
- Endpoints gRPC disponíveis:

| RPC                        | Descrição                                      |
|---------------------------|------------------------------------------------|
| `CadastrarCidade`         | Cadastra uma nova cidade com 5 temperaturas aleatórias |
| `ListarCidades`           | Lista todas as cidades cadastradas             |
| `ObterTemperaturaAtual`   | Retorna a primeira temperatura da lista        |
| `PrevisaoCincoDias`       | Retorna as 5 temperaturas associadas à cidade  |
| `EstatisticasClimaticas`  | Calcula média, mínima e máxima da cidade       |

#### Configuração (application.properties):
```properties
spring.application.name=grpc-servidor
spring.grpc.server.port=9090
```

#### Endpoints 
- Cadastrar Cidade
- POST
- http://localhost:9000/cidade
- Exemplo de requisição raw-JSON: "Goiânia"
- Resposta esperada: "Cidade Goiânia cadastrada com sucesso."
-------------------------------------------------------------
- Listar Cidades
- GET
- http://localhost:9000/cidades
- Resposta esperada: ["Goiânia", "São Paulo"]
-------------------------------------------------------------
- Temperatura atual
- GET
- http://localhost:9000/temperatura?cidade=Goi%C3%A2nia
- Resposta esperada: 28.3
-------------------------------------------------------------
- Previsão para 5 dias
- GET
- http://localhost:9000/previsao?cidade=Goiânia
- Resposta esperada: [28.3, 30.1, 29.5, 31.2, 27.8]
-------------------------------------------------------------
- Estatísticas Climáticas
- GET
- http://localhost:9000/estatisticas?cidade=Goiânia
- Resposta esperada: {
  "media": 29.38,
  "minima": 27.8,
  "maxima": 31.2
}




