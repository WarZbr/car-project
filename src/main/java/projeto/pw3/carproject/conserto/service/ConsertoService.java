package projeto.pw3.carproject.conserto.service;

import projeto.pw3.carproject.conserto.model.Conserto;
import projeto.pw3.carproject.conserto.dto.ConsertoResumoDTO ;
import projeto.pw3.carproject.conserto.dto.ConsertoAtualizacaoDTO;
import projeto.pw3.carproject.conserto.repository.ConsertoRepository;
import projeto.pw3.carproject.conserto.model.Mecanico;
import projeto.pw3.carproject.conserto.model.Veiculo;
import projeto.pw3.carproject.conserto.dto.ConsertoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsertoService {

    private final ConsertoRepository consertoRepository;

    public Conserto salvarConserto(ConsertoDTO consertoDTO) {
        Veiculo veiculo = new Veiculo(
                consertoDTO.getVeiculoMarca(),
                consertoDTO.getVeiculoModelo(),
                consertoDTO.getVeiculoAno(),
                consertoDTO.getVeiculoCor()
        );

        Mecanico mecanico = new Mecanico(
                consertoDTO.getMecanicoNome(),
                consertoDTO.getMecanicoAnosExperiencia()
        );

        Conserto conserto = new Conserto();
        conserto.setDataEntrada(consertoDTO.getDataEntrada());
        conserto.setDataSaida(consertoDTO.getDataSaida());
        conserto.setVeiculo(veiculo);
        conserto.setMecanico(mecanico);
        conserto.setAtivo(true);

        return consertoRepository.save(conserto);
    }

    public Page<Conserto> listarTodosPaginado(Pageable pageable) {
        return consertoRepository.findAll(pageable);
    }

    public List<ConsertoResumoDTO> listarResumo() {
        List<Conserto> consertos = consertoRepository.findAllAtivos();
        return consertos.stream()
                .map(c -> new ConsertoResumoDTO(
                        c.getId(),
                        c.getDataEntrada(),
                        c.getDataSaida(),
                        c.getMecanico().getNome(),
                        c.getVeiculo().getMarca(),
                        c.getVeiculo().getModelo()
                ))
                .collect(Collectors.toList());
    }

    public List<Conserto> listarTodos() {
        return consertoRepository.findAll();
    }

    public Optional<Conserto> buscarPorId(UUID id) {
        return consertoRepository.findById(id);
    }

    public Optional<Conserto> atualizarConserto(UUID id, ConsertoAtualizacaoDTO dto) {
        Optional<Conserto> consertoOpt = consertoRepository.findByIdAndAtivo(id);

        if (consertoOpt.isPresent()) {
            Conserto conserto = consertoOpt.get();

            if (dto.getDataSaida() != null) {
                conserto.setDataSaida(dto.getDataSaida());
            }

            if (dto.getMecanicoNome() != null || dto.getMecanicoAnosExperiencia() != null) {
                Mecanico mecanico = conserto.getMecanico();
                if (dto.getMecanicoNome() != null) {
                    mecanico.setNome(dto.getMecanicoNome());
                }
                if (dto.getMecanicoAnosExperiencia() != null) {
                    mecanico.setAnosExperiencia(dto.getMecanicoAnosExperiencia());
                }
                conserto.setMecanico(mecanico);
            }

            return Optional.of(consertoRepository.save(conserto));
        }

        return Optional.empty();
    }

    public boolean excluirLogicamente(UUID id) {
        Optional<Conserto> consertoOpt = consertoRepository.findByIdAndAtivo(id);

        if (consertoOpt.isPresent()) {
            Conserto conserto = consertoOpt.get();
            conserto.setAtivo(false);
            consertoRepository.save(conserto);
            return true;
        }

        return false;
    }

}