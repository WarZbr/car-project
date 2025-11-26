package projeto.pw3.carproject.conserto.controller;

import jakarta.validation.Valid;
import projeto.pw3.carproject.conserto.dto.ConsertoAtualizacaoDTO ;
import projeto.pw3.carproject.conserto.dto.ConsertoDTO;
import projeto.pw3.carproject.conserto.dto.ConsertoResumoDTO ;
import projeto.pw3.carproject.conserto.model.Conserto;
import projeto.pw3.carproject.conserto.service.ConsertoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/consertos")
@RequiredArgsConstructor
public class ConsertoController {

    private final ConsertoService consertoService;

    @PostMapping
    public ResponseEntity<Conserto> criarConserto(@Valid @RequestBody ConsertoDTO consertoDTO) {
        Conserto conserto = consertoService.salvarConserto(consertoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(conserto);
    }

    @GetMapping
    public ResponseEntity<Page<Conserto>> listarConsertos(Pageable pageable) {
        Page<Conserto> consertos = consertoService.listarTodosPaginado(pageable);
        return ResponseEntity.ok(consertos);
    }

    @GetMapping("/resumo")
    public ResponseEntity<List<ConsertoResumoDTO>> listarResumo() {
        List<ConsertoResumoDTO> resumo = consertoService.listarResumo();
        return ResponseEntity.ok(resumo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conserto> buscarPorId(@PathVariable UUID id) {
        Optional<Conserto> conserto = consertoService.buscarPorId(id);
        return conserto
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Conserto> atualizarConserto(
            @PathVariable UUID id,
            @Valid @RequestBody ConsertoAtualizacaoDTO dto) {
        Optional<Conserto> conserto = consertoService.atualizarConserto(id, dto);
        return conserto
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirConserto(@PathVariable UUID id) {
        return consertoService.excluirLogicamente(id)
            ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
            : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}