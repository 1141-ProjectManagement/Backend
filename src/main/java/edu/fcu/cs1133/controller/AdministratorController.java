package edu.fcu.cs1133.controller;

import edu.fcu.cs1133.Service.AdministratorService;
import edu.fcu.cs1133.model.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administrators")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdministratorController {

    @Autowired
    private AdministratorService administratorService;

    @GetMapping
    public List<Administrator> getAllAdministrators() {
        return administratorService.getAllAdministrators();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Administrator> getAdministratorById(@PathVariable int id) {
        return administratorService.getAdministratorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Administrator createAdministrator(@RequestBody Administrator administrator) {
        return administratorService.createAdministrator(administrator);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Administrator> updateAdministrator(@PathVariable int id, @RequestBody Administrator administratorDetails) {
        Administrator updatedAdministrator = administratorService.updateAdministrator(id, administratorDetails);
        if (updatedAdministrator != null) {
            return ResponseEntity.ok(updatedAdministrator);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrator(@PathVariable int id) {
        administratorService.deleteAdministrator(id);
        return ResponseEntity.noContent().build();
    }
}
