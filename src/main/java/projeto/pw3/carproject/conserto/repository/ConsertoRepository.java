package projeto.pw3.carproject.conserto.repository;

import projeto.pw3.carproject.conserto.model.Conserto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ConsertoRepository extends JpaRepository<Conserto, Long> {

    @Query("SELECT c FROM Conserto c WHERE c.ativo = true")
    List<Conserto> findAllAtivos();

    @Query("SELECT c FROM Conserto c WHERE c.id = :id AND c.ativo = true")
    Optional<Conserto> findByIdAndAtivo(Long id);
}
