package repository;

import model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByDni(String dni);
    Optional<Customer> findByDni(String dni);
    Optional<Customer> findByMail(String mail);
    List<Customer> findByName(String nombre);
}
