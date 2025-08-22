package com.careconnect.service.user;

import com.careconnect.model.entity.User;           
import com.careconnect.repository.UserRepository;   
import com.careconnect.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final UserRepository userRepository;    

    @Autowired
    public PatientService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {          
        if (!ValidationUtil.isValidCPF(user.getCpf())) {
            throw new IllegalArgumentException("CPF inválido!");
        }
        return userRepository.save(user);
    }
}
