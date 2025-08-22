package com.careconnect.service.user;

import com.careconnect.exception.UserNotFoundException;
import com.careconnect.model.entity.User;
import com.careconnect.model.enums.UserRole;
import com.careconnect.repository.UserRepository;
import com.careconnect.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }

        String rawPassword = user.getSenha();
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new RuntimeException("Senha é obrigatória");
        }
        if (!isBCryptHash(rawPassword)) {
            user.setSenha(passwordEncoder.encode(rawPassword));
        }

        validateAndNormalizeDocument(user);
        return userRepository.save(user);
    }

    private void validateAndNormalizeDocument(User user) {
        if (user.getTipo() == null) {
            throw new RuntimeException("Tipo de usuário é obrigatório");
        }

        if (user.getTipo() == UserRole.CLINICA) {
            String raw = user.getCnpj();
            if (raw == null || raw.isEmpty()) {
                throw new RuntimeException("CNPJ é obrigatório para clínicas");
            }
            if (!ValidationUtil.isValidCNPJ(raw)) {
                throw new RuntimeException("CNPJ inválido");
            }
            String cnpj = ValidationUtil.onlyDigits(raw);
            if (userRepository.existsByCnpj(cnpj)) {
                throw new RuntimeException("CNPJ já está em uso");
            }
            user.setCnpj(cnpj);
            user.setCpf(null);
        } else { // PACIENTE / ADMIN
            String raw = user.getCpf();
            if (raw == null || raw.isEmpty()) {
                throw new RuntimeException("CPF é obrigatório para pacientes e administradores");
            }
            if (!ValidationUtil.isValidCPF(raw)) {
                throw new RuntimeException("CPF inválido");
            }
            String cpf = ValidationUtil.onlyDigits(raw);
            if (userRepository.existsByCpf(cpf)) {
                throw new RuntimeException("CPF já está em uso");
            }
            user.setCpf(cpf);
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

        if (updatedUser.getSenha() != null && !updatedUser.getSenha().isBlank()) {
            String newRaw = updatedUser.getSenha();
            if (!isBCryptHash(newRaw)) {
                existingUser.setSenha(passwordEncoder.encode(newRaw));
            } else {
                existingUser.setSenha(newRaw);
            }
        }

        return userRepository.save(existingUser);
    }

    public void deactivateUser(Long id) {
        User user = findById(id);
        user.setAtivo(false);
        userRepository.save(user);
    }

    public boolean isValidLogin(String email, String senha) {
        return userRepository.findByEmailAndAtivoTrue(email)
                .map(u -> passwordEncoder.matches(senha, u.getSenha())) 
                .orElse(false);
    }

    public Optional<User> findActiveUserByEmail(String email) {
        return userRepository.findByEmailAndAtivoTrue(email);
    }

    private boolean isBCryptHash(String s) {
        return s != null && (s.startsWith("$2a$") || s.startsWith("$2b$") || s.startsWith("$2y$"));
    }
}
