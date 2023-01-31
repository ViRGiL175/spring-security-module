package ru.virgil.security.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

@EnableConfigurationProperties(FirebaseSecurityProperties.class)
@RequiredArgsConstructor
@Service
public class FirebaseService {

    private final FirebaseSecurityProperties firebaseSecurityProperties;
    @Nullable
    @Getter
    private String webCredentialsJson;

    @PostConstruct
    private void initFirebaseFromConfig() throws IOException {
        try {
            FirebaseApp.getInstance();
        } catch (IllegalStateException e) {
            String adminCredentialsFilePath = "classpath:%s".formatted(
                    firebaseSecurityProperties.adminCredentialsFilePath());
            File credentialsFile = ResourceUtils.getFile(adminCredentialsFilePath);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new FileInputStream(credentialsFile)))
                    .build();
            FirebaseApp.initializeApp(options);
        }
    }

    @PostConstruct
    private void initWebCredentialsFromFile() throws IOException {
        String webCredentialsFilePath = "classpath:%s".formatted(firebaseSecurityProperties.webCredentialsFilePath());
        File credentialsFile = ResourceUtils.getFile(webCredentialsFilePath);
        webCredentialsJson = Files.readString(credentialsFile.toPath());
    }

    public FirebaseToken decodeToken(String rawFirebaseToken) throws FirebaseAuthException, IllegalArgumentException {
        return FirebaseAuth.getInstance().verifyIdToken(rawFirebaseToken);
    }
}
