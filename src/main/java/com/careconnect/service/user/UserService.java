package com.careconnect.service.user;

import com.careconnect.model.entity.User;
import com.careconnect.model.enums.UserRole;
import com.careconnect.repository.UserRepository;
import com.careconnect.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }
        
        validateDocument(user);
        
        return userRepository.save(user);
    }

    private void validateDocument(User user) {
        if (user.getTipo() == UserRole.CLINICA) {
            if (user.getCnpj() == null || user.getCnpj().isEmpty()) {
                throw new RuntimeException("CNPJ é obrigatório para clínicas");
            }
            if (userRepository.existsByCnpj(user.getCnpj())) {
                throw new RuntimeException("CNPJ já está em uso");
            }
            user.setCpf(null);
        } else {
            if (user.getCpf() == null || user.getCpf().isEmpty()) {
                throw new RuntimeException("CPF é obrigatório para pacientes e administradores");
            }
            if (userRepository.existsByCpf(user.getCpf())) {
                throw new RuntimeException("CPF já está em uso");
            }
            user.setCnpj(null);
        }
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com ID: " + id));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findByTipo(UserRole tipo) {
        return userRepository.findByTipo(tipo);
    }

    public List<User> findActiveUsers() {
        return userRepository.findByAtivoTrue();
    }

    public User updateUser(Long id, User updatedUser) {
        User existingUser = findById(id);
        
        existingUser.setNome(updatedUser.getNome());
        existingUser.setTelefone(updatedUser.getTelefone());
        
        return userRepository.save(existingUser);
    }

    public void deactivateUser(Long id) {
        User user = findById(id);
        user.setAtivo(false);
        userRepository.save(user);
    }

    public boolean isValidLogin(String email, String senha) {
        Optional<User> user = userRepository.findByEmailAndAtivoTrue(email);
        return user.isPresent() && user.get().getSenha().equals(senha);
    }

    public Optional<User> findActiveUserByEmail(String email) {
        return userRepository.findByEmailAndAtivoTrue(email);
    }
}