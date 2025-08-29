package edu.fcu.cs1133.repository;

import edu.fcu.cs1133.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
    Optional<Administrator> findByUsername(String username);
}
