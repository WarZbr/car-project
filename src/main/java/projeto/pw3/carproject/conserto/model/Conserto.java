package projeto.pw3.carproject.conserto.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "consertos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conserto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_entrada")
    private String dataEntrada;

    @Column(name = "data_saida")
    private String dataSaida;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "marca", column = @Column(name = "veiculo_marca")),
            @AttributeOverride(name = "modelo", column = @Column(name = "veiculo_modelo")),
            @AttributeOverride(name = "ano", column = @Column(name = "veiculo_ano")),
            @AttributeOverride(name = "cor", column = @Column(name = "veiculo_cor"))
    })
    private Veiculo veiculo;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "nome", column = @Column(name = "mecanico_nome")),
            @AttributeOverride(name = "anosExperiencia", column = @Column(name = "mecanico_anos_experiencia"))
    })
    private Mecanico mecanico;

    @Column(name = "ativo")
    private Boolean ativo = true;

}
