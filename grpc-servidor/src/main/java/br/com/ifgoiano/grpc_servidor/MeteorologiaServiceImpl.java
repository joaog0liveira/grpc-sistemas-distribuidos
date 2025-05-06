package br.com.ifgoiano.grpc_servidor;

import br.com.ifgoiano.*;
import br.com.ifgoiano.CidadeRequest;
import br.com.ifgoiano.CidadesResponse;
import br.com.ifgoiano.EstatisticasResponse;
import br.com.ifgoiano.MensagemResponse;
import br.com.ifgoiano.MeteorologiaServiceGrpc;
import br.com.ifgoiano.PrevisaoResponse;
import br.com.ifgoiano.TemperaturaResponse;
import br.com.ifgoiano.Vazio;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class MeteorologiaServiceImpl extends MeteorologiaServiceGrpc.MeteorologiaServiceImplBase {

    private final Map<String, List<Float>> bancoSimulado = new ConcurrentHashMap<>();

    @Override
    public void cadastrarCidade(CidadeRequest request, StreamObserver<MensagemResponse> responseObserver) {
        String cidade = request.getNome();
        List<Float> temperaturas = new Random().doubles(5, 15, 35)
                .mapToObj(d -> (float) d).collect(Collectors.toList());
        bancoSimulado.put(cidade, temperaturas);
        responseObserver.onNext(MensagemResponse.newBuilder()
                .setMensagem("Cidade " + cidade + " cadastrada com sucesso.")
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void listarCidades(Vazio request, StreamObserver<CidadesResponse> responseObserver) {
        responseObserver.onNext(CidadesResponse.newBuilder()
                .addAllNomes(bancoSimulado.keySet())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void obterTemperaturaAtual(CidadeRequest request, StreamObserver<TemperaturaResponse> responseObserver) {
        List<Float> temps = bancoSimulado.get(request.getNome());
        Float temp = (temps != null && !temps.isEmpty()) ? temps.get(0) : 0.0f;
        responseObserver.onNext(TemperaturaResponse.newBuilder().setTemperatura(temp).build());
        responseObserver.onCompleted();
    }

    @Override
    public void previsaoCincoDias(CidadeRequest request, StreamObserver<PrevisaoResponse> responseObserver) {
        List<Float> temps = bancoSimulado.getOrDefault(request.getNome(), List.of());
        responseObserver.onNext(PrevisaoResponse.newBuilder().addAllTemperaturas(temps).build());
        responseObserver.onCompleted();
    }

    @Override
    public void estatisticasClimaticas(CidadeRequest request, StreamObserver<EstatisticasResponse> responseObserver) {
        List<Float> temps = bancoSimulado.getOrDefault(request.getNome(), List.of());
        float media = (float) temps.stream().mapToDouble(f -> f).average().orElse(0);
        float min = temps.stream().min(Float::compare).orElse(0f);
        float max = temps.stream().max(Float::compare).orElse(0f);
        responseObserver.onNext(EstatisticasResponse.newBuilder()
                .setMedia(media).setMinima(min).setMaxima(max).build());
        responseObserver.onCompleted();
    }
}
