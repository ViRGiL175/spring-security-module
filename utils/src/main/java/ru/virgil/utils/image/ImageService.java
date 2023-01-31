package ru.virgil.utils.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileSystemUtils;

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

@RequiredArgsConstructor
@CommonsLog
public abstract class ImageService<Owner extends IBaseEntity, PrivateImage extends IPrivateImage<Owner>> {

    public static final Path BASE_PRIVATE_IMAGE_PATH = Path.of("image", "private", "users");
    public static final Path BASE_PROTECTED_IMAGE_PATH = Path.of("image", "protected");
    public static final Path BASE_PUBLIC_IMAGE_PATH = Path.of("image", "public");
    protected final ResourceLoader resourceLoader;
    protected final IPrivateImageRepository<Owner, PrivateImage> privateImageRepository;
    protected final FileTypeService fileTypeService;

    public Resource getPrivate(Owner owner, UUID imageUuid) {
        IPrivateImage<Owner> privateImage = privateImageRepository.findByOwnerAndUuid(owner, imageUuid).orElseThrow();
        return new FileSystemResource(privateImage.getFileLocation());
    }

    public Resource getProtected(String imageName) {
        return new FileSystemResource(BASE_PROTECTED_IMAGE_PATH.resolve(imageName));
    }

    public Resource getPublic(String imageName) {
        return new FileSystemResource(BASE_PUBLIC_IMAGE_PATH.resolve(imageName));
    }

    public PrivateImage savePrivate(byte[] content, String imageName, Owner owner) throws IOException {
        Path imagePath = BASE_PRIVATE_IMAGE_PATH.resolve(owner.getUuid().toString());
        String mimeType = fileTypeService.getImageMimeType(content);
        PrivateImage privateImage = createEmptyPrivateImage();
        UUID uuid = privateImageRepository.save(privateImage).getUuid();
        String generatedName = "%s-%s.%s".formatted(imageName, uuid, mimeType.replace("image/", ""));
        Path imageFilePath = imagePath.resolve(generatedName).normalize();
        Files.createDirectories(imageFilePath.getParent());
        Files.write(imageFilePath, content);
        privateImage.setFileLocation(imageFilePath);
        privateImage.setOwner(owner);
        privateImage.setUuid(uuid);
        privateImage = privateImageRepository.save(privateImage);
        return privateImage;
    }

    protected abstract PrivateImage createEmptyPrivateImage();

    @PostConstruct
    public void preparePublicWorkDirectory() {
        copyInWorkPath(BASE_PUBLIC_IMAGE_PATH);
    }

    @PostConstruct
    public void prepareProtectedWorkDirectory() {
        copyInWorkPath(BASE_PROTECTED_IMAGE_PATH);
    }

    protected void compareDirectories(File source, File destination) throws IOException {
        List<String> listOfFilesInA = List.of(Optional.ofNullable(source.list())
                .orElseThrow(ImageException::new));
        List<String> listOfFilesInB = List.of(Optional.ofNullable(destination.list())
                .orElseThrow(ImageException::new));
        if (!new HashSet<>(listOfFilesInB).containsAll(listOfFilesInA)) {
            IOException ioException = new IOException("No files in working directory");
            throw new ImageException(ioException);
        }
    }

    protected void copyInWorkPath(Path workPath) {
        try {
            Path resourceClassPath = Paths.get("static").resolve(workPath);
            final Resource resource = resourceLoader.getResource("classpath:%s%s"
                    .formatted(resourceClassPath, File.separator));
            File source = resource.getFile();
            File destination = workPath.toFile();
            FileUtils.copyDirectory(source, destination);
            compareDirectories(source, destination);
        } catch (IOException e) {
            throw new ImageException(e);
        }
    }

    public void cleanFolders() throws IOException {
        FileSystemUtils.deleteRecursively(BASE_PRIVATE_IMAGE_PATH);
        FileSystemUtils.deleteRecursively(BASE_PROTECTED_IMAGE_PATH);
        FileSystemUtils.deleteRecursively(BASE_PUBLIC_IMAGE_PATH);
    }
}
