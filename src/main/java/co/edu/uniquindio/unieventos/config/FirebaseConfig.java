package co.edu.uniquindio.unieventos.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

	@Bean
	public FirebaseApp intializeFirebase() throws IOException {
		try (FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase-secret.json")) {

			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setStorageBucket("amaevents-0.appspot.com")
					.build();

			if (FirebaseApp.getApps().isEmpty()) {
				return FirebaseApp.initializeApp(options);
			}

			return FirebaseApp.getInstance();

		}
	}

}
