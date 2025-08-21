package com.careconnect.repository;

import com.careconnect.model.entity.User;
import com.careconnect.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    boolean existsByCpf(String cpf);
    
    boolean existsByCnpj(String cnpj);
    
    List<User> findByTipo(UserRole tipo);
    
    List<User> findByAtivoTrue();
    
    Optional<User> findByEmailAndAtivoTrue(String email);
}