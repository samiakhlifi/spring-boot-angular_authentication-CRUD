package com.miniprojet.image;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class ImageService {

    @Autowired
    ImageRepository imageRepository;

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public String setFileName(String name) {

        String extension = name != null || name != "" ? "." + Optional.of(name).filter(f -> f.contains("."))
                .map(f -> f.substring(name.lastIndexOf(".") + 1)).orElse("") : "";
        return this.randomString(15) + extension;
    }

    public Set<ImageDTO> setMediaNames(Set<ImageDTO> images) {

        if (images == null || images.size() == 0)
            return new HashSet<>();

        for (ImageDTO image : images) {
            if (image.getFileName() != null && image.getFileName() != "" && image.getFile() != null)
                image.setFileName(setFileName(image.getFileName()));
            else
                images.remove(image);
        }

        return images;
    }

    public long uploadList(Set<ImageDTO> images, String path) {
        // String name = file.getOriginalFilename();
        if (images == null || images.size() == 0)
            return -1;
        try {
            for (ImageDTO image : images) {
                if (image.getFileName() != null && image.getFileName() != "" && image.getFile() != null)
                    upload(image.getFile(), image.getFileName(), path);
            }
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    public Image getById(Long id) {

        Image image = null;
        try {
            image = imageRepository.findById(id).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (image != null)
            return image;
        return null;
    }

    public void deleteById(long id) {

        Image image = getById(id);
        if (getById(id) != null) {
            imageRepository.delete(image);
        }
    }


    public long upload(byte[] file, String filename, String path) {
        // String name = file.getOriginalFilename();
        try {
            if (file != null) {

                byte[] bytes = file;
                File dir = new File(System.getProperty("user.dir") + "/" + path);
                if (!dir.exists())
                    dir.mkdirs();
                System.out.println("path is  " + dir.getAbsolutePath());
                File serverFile = new File(dir.getAbsolutePath() + "/" + filename);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
            }

            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    public Resource load(String dir, String filename) {
        try {
            Path root = Paths.get("uploads/" + dir);
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            System.out.println(filename);

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }

    }

    public boolean deleteFileByUrl(String url) {
        boolean res = false;
        if (url != null) {
            Path root = Paths.get(url.substring(0, url.lastIndexOf("/") + 1));
            Path file = root.resolve(url.substring(url.lastIndexOf("/") + 1, url.length()));
            try {
                Files.delete(file);
                res = true;
            } catch (IOException e) {
                throw new RuntimeException("Could not delete the files!: " + e.getMessage());
            }
        }

        return res;

    }
}
