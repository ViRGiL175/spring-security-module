package ru.virgil.example.image;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.virgil.example.user.UserDetails;
import ru.virgil.example.user.UserDetailsService;
import ru.virgil.utils.image.FileTypeService;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final UserDetailsService userDetailsService;
    private final PrivateFileImageMapper privateFileImageMapper;
    private final FileTypeService fileTypeService;

    // todo: переменный Media Type
    @GetMapping("/public/{imageName}")
    public ResponseEntity<byte[]> getPublic(@PathVariable String imageName) throws IOException {
        Path filePath = Paths.get(imageService.getPublic(imageName).getURI());
        byte[] imageBytes = IOUtils.toByteArray(new FileSystemResource(filePath).getInputStream());
        String imageMime = fileTypeService.getImageMimeType(imageBytes);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(imageMime)).body(imageBytes);
    }

    @GetMapping("/protected/{imageName}")
    public ResponseEntity<byte[]> getProtected(@PathVariable String imageName) throws IOException {
        Path filePath = Paths.get(imageService.getProtected(imageName).getURI());
        byte[] imageBytes = IOUtils.toByteArray(new FileSystemResource(filePath).getInputStream());
        String imageMime = fileTypeService.getImageMimeType(imageBytes);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(imageMime)).body(imageBytes);
    }

    @GetMapping("/private/{imageUuid}")
    public ResponseEntity<byte[]> getPrivate(@PathVariable UUID imageUuid) throws IOException {
        UserDetails currentUser = userDetailsService.getCurrentUser();
        Path filePath = Paths.get(imageService.getPrivate(currentUser, imageUuid).getURI());
        byte[] imageBytes = IOUtils.toByteArray(new FileSystemResource(filePath).getInputStream());
        String imageMime = fileTypeService.getImageMimeType(imageBytes);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(imageMime)).body(imageBytes);
    }

    @PostMapping("/private")
    public PrivateImageFileDto postPrivate(@RequestParam MultipartFile image,
            @Nullable @RequestParam(required = false) String imageName) throws IOException {
        UserDetails currentUser = userDetailsService.getCurrentUser();
        PrivateImageFile privateFileImage = imageService.savePrivate(image.getBytes(),
                imageName == null ? "image" : imageName, currentUser);
        return privateFileImageMapper.toDto(privateFileImage);
    }
}
