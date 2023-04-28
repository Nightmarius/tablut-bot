package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.fullstack.hackathon.database.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {
    private JpaRepository<Admin, String> adminRepo;

    public Optional<Admin> getAdmin(String name) {
        return adminRepo.findById(name);
    }

    public boolean login(String username, String password) {
        Admin admin = adminRepo.findAll().stream().filter(a -> a.getUsername().equals(username)).findFirst().orElse(null);
        if (admin == null) return false;
        return admin.getPassword().equals(password);
    }
}
