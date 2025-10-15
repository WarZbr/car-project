package projeto.pw3.carproject.conserto.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsertoAtualizacaoDTO {

    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Data deve estar no formato dd/mm/aaaa")
    private String dataSaida;

    private String mecanicoNome;

    private Integer mecanicoAnosExperiencia;

}
