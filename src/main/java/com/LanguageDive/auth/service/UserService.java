package com.LanguageDive.auth.service;

import com.LanguageDive.auth.entity.User;
import com.LanguageDive.auth.repository.UserRepository;
import com.LanguageDive.common.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }
}
