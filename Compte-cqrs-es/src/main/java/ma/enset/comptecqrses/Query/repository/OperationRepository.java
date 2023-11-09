package ma.enset.comptecqrses.Query.repository;


import ma.enset.comptecqrses.Query.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation,Long> {
}
