package ru.virgil.example.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user_details")
public class UserDetailsController {

    private final UserDetailsService userDetailsService;
    private final UserDetailsMapper userDetailsMapper;

    @GetMapping
    public UserDetailsDto get() {
        UserDetails currentUser = userDetailsService.getCurrentUser();
        return userDetailsMapper.toDto(currentUser);
    }

}
