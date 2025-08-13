package com.oms.userservice.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oms.userservice.dao.UserRepository;
import com.oms.userservice.exception.ResourceNotFoundException;
import com.oms.userservice.model.User;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User getUserById(Long id) {
	    return userRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
	}


    public User saveUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        return userRepository.save(user);
    }

	public void deleteUser(Long id) {
		 userRepository.deleteById(id);
	}
}
