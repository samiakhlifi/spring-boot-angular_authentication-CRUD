package com.miniprojet.image;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;


import lombok.Data;
import org.modelmapper.ModelMapper;

@Entity
@Data
public class Image implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fileName;

    public Image(@NotBlank String fileName) {
        super();
        this.fileName = fileName;
    }

    public Image() {
        super();
    }

    @Override
    public Object clone() {
        try {
            return (Image) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static Image mapToMedia(ImageDTO imageDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return (modelMapper.map(imageDTO, Image.class));
    }


}
