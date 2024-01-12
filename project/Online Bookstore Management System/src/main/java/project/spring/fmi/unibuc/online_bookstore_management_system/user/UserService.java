package project.spring.fmi.unibuc.online_bookstore_management_system.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.spring.fmi.unibuc.online_bookstore_management_system.cart.CartService;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CartService cartService;

    @Autowired
    public UserService(UserRepository userRepository, CartService cartService) {
        this.userRepository = userRepository;
        this.cartService = cartService;
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

    public void deleteUser(Long userId) {
        UserEntity existsUser = userRepository.findById(userId).orElse(null);

        if (existsUser != null) {
            cartService.deleteCartAndCartItemsByUser(existsUser);
            if (UserEntity.signedInUser != null && UserEntity.signedInUser.getUserID().equals(userId))
                UserEntity.signedInUser = null;
            userRepository.delete(existsUser);
        }

    }
}
