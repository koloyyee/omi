package co.loyyee.Omi.Drafter.util.helpers;

import jakarta.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class Convert {
	
	private static final Logger log = LoggerFactory.getLogger(Convert.class);
	/**
	 * @param mf Spring Boot file type MultipartFile from client post request
	 *     <p>It will convert file from MultipartFile to File parsing into Text
	 */
	public static File toFile(@NotNull MultipartFile mf) {
		
		File file = null;
		try {
			file = new File(Objects.requireNonNull(mf.getOriginalFilename()));
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(mf.getBytes());
			fos.close();
		} catch (IOException e) {
			log.error("File conversion Error: " + e.getMessage());
		}
		return file;
	}
}
