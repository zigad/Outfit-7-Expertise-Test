package si.deisinger.Services;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.IOException;

@SpringBootApplication
public class Fun7Application {
	public static void main(String[] args) {
		//Only for development
		try {
			authExplicit();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SpringApplication.run(Fun7Application.class, args);
	}

	static void authExplicit() throws IOException {
		// You can specify a credential file by providing a path to GoogleCredentials.
		// Otherwise credentials are read from the GOOGLE_APPLICATION_CREDENTIALS environment variable.
		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/mimetic-planet-341421-ab8a1fe5ad1b.json"))
				.createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
		Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

		System.out.println("Buckets:");
		Page<Bucket> buckets = storage.list();
		for (Bucket bucket : buckets.iterateAll()) {
			System.out.println(bucket.toString());
		}
	}
}


