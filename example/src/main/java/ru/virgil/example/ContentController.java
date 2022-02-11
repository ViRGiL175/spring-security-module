package ru.virgil.example;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content")
public class ContentController {

    @GetMapping("/public")
    public String publicContent() {
        return "Public Content";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/user")
    public String userContent() {
        return "User Content";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    public String adminContent() {
        return "Admin Content";
    }
}
