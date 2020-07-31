
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author ASHOK
 *
 */
public class FileWrite {

	private static final Logger logeer = LoggerFactory.getLogger(FileWrite.class);
	private String output = "output.txt";
	ClassLoader classLoader = this.getClass().getClassLoader();

	/**
	 * 
	 * @return
	 */
	public void writeFileFromResources(List<String> avgTimeList) {

		BufferedWriter writer;
		File avgTimeFiles = new File(classLoader.getResource(output).getFile());
		try {
			writer = new BufferedWriter(new FileWriter(avgTimeFiles, true));
			writer.newLine();
			for (String avgTime : avgTimeList) {
				writer.write(avgTime);
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			logeer.error(e.getMessage());
		}
	}
}
