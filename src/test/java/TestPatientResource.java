

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;

/**
 * 
 * @author ASHOK
 *
 */
public class TestPatientResource {

	private IGenericClient iGenericClient = null;
	PatientDetails patientResource = new PatientDetails();
	String line;
	FileReader fileReaderClass = new FileReader();
	BufferedReader bufferedReader = fileReaderClass.readFile();
	Bundle response = null;

	@Before
	public void RestfulGenericClient() {

		FhirContext fhirContext = FhirContext.forR4();
		iGenericClient = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
		iGenericClient.registerInterceptor(new LoggingInterceptor(false));
	}

	@Test
	public void iGenericClientTest() {
		assertNotNull(iGenericClient);
		assertNotNull(patientResource);
	}

	@Test
	public void cacheControlResourceTest() {

		assertNotNull(bufferedReader);
		try {
			line = bufferedReader.readLine();
			assertNotNull(line);
			line = "Smith";
			assertEquals("Smith", line);

			InterceptorClass stopWatchInterceptor = new InterceptorClass();
			iGenericClient.registerInterceptor(stopWatchInterceptor);
			assertNotNull(stopWatchInterceptor);
			assertNotNull(iGenericClient);

			for (int i = 1; i <= 3; i++) {
				response = iGenericClient.search().forResource("Patient").where(Patient.FAMILY.matches().value(line))
						.returnBundle(Bundle.class).execute();

				if (i == 2) {
					response = iGenericClient.search().forResource("Patient")
							.cacheControl(new CacheControlDirective().setNoCache(true)).sort().ascending(Patient.NAME)
							.where(Patient.FAMILY.matches().value(line)).returnBundle(Bundle.class).execute();
				}

				assertNotNull(stopWatchInterceptor.getDuration());
				assertNotNull(response);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
