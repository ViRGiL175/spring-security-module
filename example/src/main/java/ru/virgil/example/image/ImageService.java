package ru.virgil.example.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import ru.virgil.example.user.UserDetails;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@CommonsLog
public class ImageService {

    // todo: в утилиты
    public static final Path BASE_PRIVATE_IMAGE_PATH = Path.of("images", "private", "users");
    public static final Path BASE_PROTECTED_IMAGE_PATH = Path.of("images", "protected");
    public static final Path BASE_PUBLIC_IMAGE_PATH = Path.of("images", "public");
    private final ResourceLoader resourceLoader;
    private final PrivateFileImageRepository privateFileImageRepository;
    private final FileTypeService fileTypeService;

    public Resource getPrivate(UUID imageUuid, UserDetails owner) {
        PrivateFileImage privateFileImage = privateFileImageRepository.findByOwnerAndUuid(owner, imageUuid)
                .orElseThrow();
        return new FileSystemResource(privateFileImage.getFileLocation());
    }

    public Resource getProtected(String imageName) {
        return new FileSystemResource(BASE_PROTECTED_IMAGE_PATH.resolve(imageName));
    }

    public Resource getPublic(String imageName) {
        return new FileSystemResource(BASE_PUBLIC_IMAGE_PATH.resolve(imageName));
    }

    public PrivateFileImage savePrivate(byte[] content, String imageName, UserDetails owner) throws IOException {
        Path imagePath = BASE_PRIVATE_IMAGE_PATH.resolve(owner.getUuid().toString());
        String mimeType = fileTypeService.checkIfImage(content);
        PrivateFileImage privateFileImage = new PrivateFileImage();
        UUID uuid = privateFileImageRepository.save(privateFileImage).getUuid();
        String generatedName = "%s-%s.%s".formatted(imageName, uuid, mimeType.replace("image/", ""));
        Path imageFilePath = imagePath.resolve(generatedName).normalize();
        Files.createDirectories(imageFilePath.getParent());
        Files.write(imageFilePath, content);
        privateFileImage.setFileLocation(imageFilePath);
        privateFileImage.setOwner(owner);
        privateFileImage.setUuid(uuid);
        privateFileImage = privateFileImageRepository.save(privateFileImage);
        return privateFileImage;
    }

    @PostConstruct
    public void preparePublicWorkDirectory() {
        copyInWorkPath(BASE_PUBLIC_IMAGE_PATH);
    }

    @PostConstruct
    public void prepareProtectedWorkDirectory() {
        copyInWorkPath(BASE_PROTECTED_IMAGE_PATH);
    }

    private void compareDirectories(File source, File destination) throws IOException {
        List<String> listOfFilesInA = List.of(Optional.ofNullable(source.list()).orElseThrow(MockImageException::new));
        List<String> listOfFilesInB = List.of(Optional.ofNullable(destination.list()).orElseThrow(MockImageException::new));
        if (!new HashSet<>(listOfFilesInB).containsAll(listOfFilesInA)) {
            throw new IOException("No files in working directory");
        }
    }

    private void copyInWorkPath(Path workPath) {
        try {
            Path resourceClassPath = Paths.get("static").resolve(workPath);
            final Resource resource = resourceLoader.getResource("classpath:" + resourceClassPath + File.separator);
            File source = resource.getFile();
            File destination = workPath.toFile();
            FileUtils.copyDirectory(source, destination);
            compareDirectories(source, destination);
        } catch (IOException e) {
            throw new MockImageException(e);
        }
    }

    public void cleanFolders() {
        FileSystemUtils.deleteRecursively(BASE_PRIVATE_IMAGE_PATH.toFile());
        FileSystemUtils.deleteRecursively(BASE_PROTECTED_IMAGE_PATH.toFile());
        FileSystemUtils.deleteRecursively(BASE_PUBLIC_IMAGE_PATH.toFile());
    }
}
