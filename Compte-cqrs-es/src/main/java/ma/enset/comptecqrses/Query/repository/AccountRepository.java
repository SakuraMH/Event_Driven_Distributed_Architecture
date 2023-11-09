package ma.enset.comptecqrses.Query.repository;

import ma.enset.comptecqrses.Query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {
}
