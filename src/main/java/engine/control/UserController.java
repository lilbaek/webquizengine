package engine.control;

import engine.model.UserDbEntry;
import engine.model.UserDto;
import engine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api")
@Validated
public class UserController {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> create(@RequestBody @Valid UserDto dto) {
        if (userRepository.findByEmailIgnoreCase(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }
        if(dto.getPassword().trim().length() < 5) {
            System.out.println("Password too short: " + dto.getPassword().length());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password too short");
        }
        UserDbEntry save = new UserDbEntry();
        save.setEmail(dto.getEmail());
        save.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(save);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}