package co.edu.uniquindio.unieventos.services.impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;

import co.edu.uniquindio.unieventos.exceptions.DeleteFileException;
import co.edu.uniquindio.unieventos.exceptions.UploadFileException;
import co.edu.uniquindio.unieventos.services.ImagesService;

@Service
public class ImagesServiceImpl implements ImagesService {

	@Override
	public String uploadImage(MultipartFile image) throws UploadFileException {
		Bucket bucket = StorageClient.getInstance().bucket();
		String fileName = String.format("%s-%s", UUID.randomUUID().toString(), image.getOriginalFilename());
		Blob blob;
		try {
			blob = bucket.create(fileName, image.getInputStream(), image.getContentType());
		} catch (IOException e) {
			throw new UploadFileException(e.getMessage());
		}

		return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", bucket.getName(),
				blob.getName());
	}

	@Override
	public void deleteImage(String imageName) throws DeleteFileException {
		Bucket bucket = StorageClient.getInstance().bucket();
		Blob blob = bucket.get(imageName);
		blob.delete();

	}
}
