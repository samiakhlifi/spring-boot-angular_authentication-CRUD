package com.miniprojet.image;


import java.io.Serializable;


import lombok.Data;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@ToString @Data
public class ImageDTO implements Serializable {

    private Long id;

    private String fileName;

    private byte[] file;

    private String mediaURL;

    public static ImageDTO mapToMediaDTO(Image image) {
        ModelMapper modelMapper = new ModelMapper();
        return (modelMapper.map(image, ImageDTO.class));
    }

}