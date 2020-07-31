
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author ASHOK
 *
 */
public class FileReader {

	private static final Logger logeer = LoggerFactory.getLogger(FileReader.class);
	private String fileName = "input.txt";
	ClassLoader classLoader = this.getClass().getClassLoader();

	/**
	 * 
	 * @return
	 */
	public BufferedReader readFile() {

		logeer.info("file name: {}", "patientLastName.txt");
		FileInputStream inputStream = getFileFromResources();
		logeer.debug("inputStream: {}", inputStream);
		return new BufferedReader(new InputStreamReader(inputStream));
	}

	/**
	 * 
	 * @return
	 */
	private FileInputStream getFileFromResources() {

		logeer.info("getting ClassLoader object");
		logeer.info("getting resource(file) from class loader");
		File configFile = new File(classLoader.getResource(fileName).getFile());
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(configFile);
			return inputStream;
		} catch (FileNotFoundException e) {
			logeer.error("error message:{}", e.getMessage());
		}
		return inputStream;

	}
}
