package it.uniroma3.siw_techstore.Util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
	
	public static String salvaImmagine(MultipartFile image, String directory) {
        try {
            Date createdAt = new Date();
            String originalFilename = Paths.get(image.getOriginalFilename()).getFileName().toString();
            String storageFilename = createdAt.getTime() + "_" + originalFilename;

            Path uploadPath = Paths.get(directory);
            Files.createDirectories(uploadPath);

            try (InputStream inputStream = image.getInputStream()) {
                Path filePath = uploadPath.resolve(storageFilename);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            return storageFilename;
        } catch (IOException e) {
            throw new RuntimeException("Errore nel salvataggio immagine", e);
        }
    }
	
	public static void eliminaImmagine(String filename, String uploadDir) throws IOException{
		Path imagePath = Paths.get(uploadDir, filename);
        Files.deleteIfExists(imagePath);
	}

}
