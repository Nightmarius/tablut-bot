package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.fullstack.hackathon.database.AdminData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AdminService {
    private JpaRepository<AdminData, Long> adminRepo;

    public Optional<AdminData> getAdminById(Long id){
        return adminRepo.findById(id);
    }
    public boolean login(String username, String password){
        AdminData admin = adminRepo.findAll().stream().filter(a-> a.getUsername().equals(username)).findFirst().orElse(null);
        if(admin == null) return false;
        return admin.getPassword().equals(password);
    }
}
