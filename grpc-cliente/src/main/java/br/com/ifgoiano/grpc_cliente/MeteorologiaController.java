package br.com.ifgoiano.grpc_cliente;

import br.com.ifgoiano.*;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MeteorologiaController {

    @GrpcClient("mtol")
    private MeteorologiaServiceGrpc.MeteorologiaServiceBlockingStub stub;

    @PostMapping("/cidade")
    public ResponseEntity<String> cadastrar(@RequestBody String nome) {
        MensagemResponse response = stub.cadastrarCidade(CidadeRequest.newBuilder().setNome(nome).build());
        return ResponseEntity.ok(response.getMensagem());
    }

    @GetMapping("/cidades")
    public ResponseEntity<List<String>> listar() {
        return ResponseEntity.ok(stub.listarCidades(Vazio.newBuilder().build()).getNomesList());
    }

    @GetMapping("/temperatura")
    public ResponseEntity<Float> temperatura(@RequestParam String cidade) {
        return ResponseEntity.ok(stub.obterTemperaturaAtual(CidadeRequest.newBuilder().setNome(cidade).build()).getTemperatura());
    }

    @GetMapping("/previsao")
    public ResponseEntity<List<Float>> previsao(@RequestParam String cidade) {
        return ResponseEntity.ok(stub.previsaoCincoDias(CidadeRequest.newBuilder().setNome(cidade).build()).getTemperaturasList());
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<EstatisticasResponse> estatisticas(@RequestParam String cidade) {
        return ResponseEntity.ok(stub.estatisticasClimaticas(CidadeRequest.newBuilder().setNome(cidade).build()));
    }
}
