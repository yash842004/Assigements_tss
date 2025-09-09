package com.tss.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tss.jpa.entity.ImageEntity;
import com.tss.jpa.service.CloudinaryService;
import com.tss.jpa.service.ImageService;

@RestController
@RequestMapping("/images")
public class ImageController {

	@Autowired
	private ImageService imageService;

	private final CloudinaryService cloudinaryService;

	public ImageController(CloudinaryService cloudinaryService) {
		this.cloudinaryService = cloudinaryService;
	}

	@PostMapping("/upload")
	public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws Exception {
		ImageEntity image = imageService.saveImage(file);
		return ResponseEntity.ok("Image uploaded successfully. ID: " + image.getId());
	}

	@GetMapping("/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
		ImageEntity image = imageService.getImage(id);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
				.contentType(MediaType.parseMediaType(image.getType())).body(image.getData());
	}

	@PostMapping(value = "/upload/cloud", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadImageCloud(@RequestParam("file") MultipartFile file) {
	    try {
	        String imageUrl = cloudinaryService.uploadFile(file);
	        return ResponseEntity.ok(imageUrl);
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body("Image upload failed: " + e.getMessage());
	    }
	}


}
