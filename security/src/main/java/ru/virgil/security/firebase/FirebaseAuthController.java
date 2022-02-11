package ru.virgil.security.firebase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.virgil.security.SecurityConfig;
import ru.virgil.security.entity.AuthRecord;
import ru.virgil.security.header.AuthorizationHeader;
import ru.virgil.security.header.WwwAuthenticateHeader;
import ru.virgil.security.service.FirebaseAuthRecordsService;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static ru.virgil.security.entity.AuthMethods.FIREBASE;
import static ru.virgil.security.service.FirebaseAuthRecordsService.AUTH_RECORD_UUID_CREDENTIAL;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/firebase")
public class FirebaseAuthController {

    private final FirebaseAuthRecordsService firebaseAuthRecordsService;
    private final FirebaseService firebaseService;
    private final ObjectMapper jackson;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ClassCastException.class, IllegalArgumentException.class})
    public void handleBadRequestExceptions(HttpServletResponse response, Exception exception) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, exception.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NullPointerException.class})
    public void handleNotFoundExceptions(HttpServletResponse response, Exception exception) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, exception.getLocalizedMessage());
    }

    @GetMapping("/page")
    public ModelAndView refactoredAuthPage(@Deprecated @RequestParam(required = false) @Nullable UUID authRecordUuid,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader)
            throws JsonProcessingException {
        // todo: если нет хедера и параметра, то проводится веб-авторизация
        UUID recordUuid = authRecordUuid != null ? authRecordUuid
                : firebaseAuthRecordsService.extractAuthRecordUuid(authorizationHeader);
        AuthorizationHeader putCallbackAuthorizationHeader = new AuthorizationHeader(FIREBASE,
                Map.of(AUTH_RECORD_UUID_CREDENTIAL, recordUuid.toString()));
        String authRecordUpdatePath = "/auth/firebase/page/record";
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModelMap().addAttribute("auth_record_callback_header",
                jackson.writeValueAsString(putCallbackAuthorizationHeader));
        modelAndView.getModelMap().addAttribute("firebase_credentials_json",
                firebaseService.getWebCredentialsJson());
        modelAndView.getModelMap().addAttribute("auth_record_callback_path", authRecordUpdatePath);
        modelAndView.setViewName("firebase_auth_page");
        return modelAndView;
    }

    @GetMapping("/page/record")
    public AuthRecord getAuthRecord(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        UUID authRecordUuid = firebaseAuthRecordsService.extractAuthRecordUuid(authorizationHeader);
        return firebaseAuthRecordsService.getAuthRecord(authRecordUuid);
    }

    @PostMapping("/page/record")
    public void createAuthRecord(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UUID authRecord = firebaseAuthRecordsService.createAuthRecord();
        String exampleHeader = jackson.writeValueAsString(new AuthorizationHeader(FIREBASE,
                Map.of(AUTH_RECORD_UUID_CREDENTIAL, "value")));
        Map<String, String> challenges = Map.of(AUTH_RECORD_UUID_CREDENTIAL, authRecord.toString(),
                "info", "Send %s Header to GET %s, pass authentication and then GET Firebase Token from /page/record"
                        .formatted(HttpHeaders.AUTHORIZATION, SecurityConfig.AUTH_PAGE_PATHS.get(FIREBASE)),
                "auth_header_example", exampleHeader
        );
        WwwAuthenticateHeader wwwAuthenticateHeader = new WwwAuthenticateHeader(FIREBASE, challenges);
        response.addHeader(HttpHeaders.WWW_AUTHENTICATE, jackson.writeValueAsString(wwwAuthenticateHeader));
    }

    @PutMapping("/page/record")
    public void editAuthRecord(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody AuthRecord authRecord) {
        UUID authRecordUuid = firebaseAuthRecordsService.extractAuthRecordUuid(authorizationHeader);
        firebaseAuthRecordsService.editAuthRecord(authRecordUuid, authRecord);
    }

    @DeleteMapping("/page/record")
    public void deleteAuthRecord(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        UUID authRecordUuid = firebaseAuthRecordsService.extractAuthRecordUuid(authorizationHeader);
        firebaseAuthRecordsService.deleteAuthRecord(authRecordUuid);
    }
}
