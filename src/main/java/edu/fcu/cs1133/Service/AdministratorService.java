package edu.fcu.cs1133.Service;

import edu.fcu.cs1133.model.Administrator;
import edu.fcu.cs1133.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministratorService {

    @Autowired
    private AdministratorRepository administratorRepository;

    public List<Administrator> getAllAdministrators() {
        return administratorRepository.findAll();
    }

    public Optional<Administrator> getAdministratorById(int id) {
        return administratorRepository.findById(id);
    }

    public Administrator createAdministrator(Administrator administrator) {
        return administratorRepository.save(administrator);
    }

    public Administrator updateAdministrator(int id, Administrator administratorDetails) {
        Administrator administrator = administratorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrator not found"));

        administrator.setName(administratorDetails.getName());
        administrator.setEmail(administratorDetails.getEmail());
        administrator.setPassword(administratorDetails.getPassword());
        administrator.setRole(administratorDetails.getRole());
        administrator.setUsername(administratorDetails.getUsername());

        return administratorRepository.save(administrator);
    }

    public void deleteAdministrator(int id) {
        administratorRepository.deleteById(id);
    }
}
