package com.miniprojet.image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ImageController {

    @Autowired
    ImageService imageService;

    @GetMapping("/uploads/{dir}/{filename}")
    public ResponseEntity<?> getFile(@PathVariable(name = "dir") String dir,
                                     @PathVariable(name = "filename") String filename) {
        Resource file = imageService.load(dir, filename);
        if (file != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } else {
            return new ResponseEntity<>("Could not read the file!", HttpStatus.NOT_FOUND);
        }

    }
}