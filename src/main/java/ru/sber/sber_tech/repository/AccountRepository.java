package ru.sber.sber_tech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sber.sber_tech.domain.Account;

/**
 * AccountRepository.
 *
 * @author Kiselev_Mikhail
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
