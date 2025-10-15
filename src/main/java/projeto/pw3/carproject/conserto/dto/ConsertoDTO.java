package projeto.pw3.carproject.conserto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsertoDTO {

    @NotBlank(message = "Data de entrada é obrigatória")
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Data de entrada deve estar no formato dd/mm/aaaa")
    private String dataEntrada;

    @NotBlank(message = "Data de saída é obrigatória")
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Data de saída deve estar no formato dd/mm/aaaa")
    private String dataSaida;

    @NotBlank(message = "Marca do veículo é obrigatória")
    private String veiculoMarca;

    @NotBlank(message = "Modelo do veículo é obrigatório")
    private String veiculoModelo;

    @NotBlank(message = "Ano do veículo é obrigatório")
    @Pattern(regexp = "\\d{4}", message = "Ano do veículo deve estar no formato aaaa")
    private String veiculoAno;

    private String veiculoCor;

    @NotBlank(message = "Nome do mecânico é obrigatório")
    private String mecanicoNome;

    private Integer mecanicoAnosExperiencia;

}

