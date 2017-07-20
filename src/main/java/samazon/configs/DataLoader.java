package samazon.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import samazon.models.User;
import samazon.repositories.UserRepository;
@Component
public class DataLoader implements CommandLineRunner{
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public void run(String... strings) throws Exception {
        System.out.println("Loading data . . .");
        User user = new User("bob@bob.com","bob","Bob","Bobberson", true, "bob", "user");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        user = new User("jim@jim.com","jim","Jim","Jimmerson", true, "jim", "user");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        user = new User("admin@secure.com","password","Admin","User", true, "admin", "admin");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}