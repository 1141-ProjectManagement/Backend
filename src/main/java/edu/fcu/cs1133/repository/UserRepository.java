package edu.fcu.cs1133.repository;

import edu.fcu.cs1133.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import edu.fcu.cs1133.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByOfficialId(String officialId);

    Boolean existsByOfficialId(String officialId);

    Boolean existsByEmail(String email);

    List<User> findByRole(Role role);
}