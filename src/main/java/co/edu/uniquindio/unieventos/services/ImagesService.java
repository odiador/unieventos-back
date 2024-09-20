package co.edu.uniquindio.unieventos.services;

import org.springframework.web.multipart.MultipartFile;

import co.edu.uniquindio.unieventos.exceptions.DeleteFileException;
import co.edu.uniquindio.unieventos.exceptions.UploadFileException;

public interface ImagesService {
	String uploadImage(MultipartFile image) throws UploadFileException;

	void deleteImage(String imageName) throws DeleteFileException;
}
