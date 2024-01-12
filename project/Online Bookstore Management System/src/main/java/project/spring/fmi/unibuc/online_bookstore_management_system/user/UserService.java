package project.spring.fmi.unibuc.online_bookstore_management_system.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        if (UserEntity.signedInUser != null && UserEntity.signedInUser.getUserID().equals(user.getUserID())){
            UserEntity.signedInUser = user;
        }
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

    public UserEntity findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean deleteUser(Long userId) {
        UserEntity existsUser = userRepository.findById(userId).orElse(null);

        if (existsUser != null) {
            if (UserEntity.signedInUser != null && UserEntity.signedInUser.getUserID().equals(userId))
                UserEntity.signedInUser = null;
            userRepository.delete(existsUser);
            return true;
        }

        return false;
    }
}
