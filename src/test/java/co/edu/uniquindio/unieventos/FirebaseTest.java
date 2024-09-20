package co.edu.uniquindio.unieventos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import co.edu.uniquindio.unieventos.exceptions.UploadFileException;
import co.edu.uniquindio.unieventos.services.impl.ImagesServiceImpl;

@SpringBootTest
public class FirebaseTest {

	@Test
	public void uploadFileTest() throws UploadFileException {
		// TODO cambiar por controllers
		ImagesServiceImpl isi = new ImagesServiceImpl();
		MockMultipartFile mockFile = new MockMultipartFile("image", 
				"real-test-image.jpg",
				MediaType.IMAGE_JPEG_VALUE,
				"Contenido de la imagen de prueba".getBytes() 
		);
		System.out.println(isi.uploadImage(mockFile));
	}
}
