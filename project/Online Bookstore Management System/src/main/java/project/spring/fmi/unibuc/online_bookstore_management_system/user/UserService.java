package project.spring.fmi.unibuc.online_bookstore_management_system.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(UserEntity user) {
        userRepository.save(user);
    }

    public boolean logInUser(UserEntity user) {
        UserEntity existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            UserEntity.signedInUser = existingUser;
            return true;
        }
        return false;
    }
}
