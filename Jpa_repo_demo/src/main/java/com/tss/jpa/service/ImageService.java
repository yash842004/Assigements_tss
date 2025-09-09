package com.tss.jpa.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tss.jpa.entity.ImageEntity;
import com.tss.jpa.Repositary.ImageRepository;

@Service
public class ImageService {

	@Autowired
	private ImageRepository imageRepository;

	public ImageEntity saveImage(MultipartFile file) throws IOException {
		ImageEntity image = new ImageEntity();
		image.setName(file.getOriginalFilename());
		image.setType(file.getContentType());
		image.setData(file.getBytes());
		return imageRepository.save(image);
	}

	public ImageEntity getImage(Long id) {
		return imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not found with id: " + id));
	}
}
