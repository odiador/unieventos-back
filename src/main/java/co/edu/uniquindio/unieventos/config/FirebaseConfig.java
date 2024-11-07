package co.edu.uniquindio.unieventos.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

	@Bean
	public FirebaseApp intializeFirebase() throws IOException {
		
		try (InputStream is = getClass().getResourceAsStream("/firebase-secret.json");) {
			return getFirebaseApp(is);
		} catch (Exception e) {
			try (InputStream is = new FileInputStream(new File("/etc/secrets/firebase-secret.json"))) {
				return getFirebaseApp(is);
			} catch (Exception e2) {
				return null;
			}
		}
	}

	private FirebaseApp getFirebaseApp(InputStream is) throws IOException {
		FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(is))
				.setStorageBucket("amaevents-0.appspot.com")
				.build();

		if (FirebaseApp.getApps().isEmpty()) {
			return FirebaseApp.initializeApp(options);
		}

		return FirebaseApp.getInstance();
	}

}
