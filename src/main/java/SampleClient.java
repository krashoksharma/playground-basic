
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;

public class SampleClient {

	private static final Logger logeer = LoggerFactory.getLogger(SampleClient.class);
	CacheControlDirective cacheControlDirective = new CacheControlDirective();

	public static void main(String[] theArgs) {

		FhirContext fhirContext = FhirContext.forR4();
		IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
		client.registerInterceptor(new LoggingInterceptor(false));
		SampleClient sampleClient = new SampleClient();
		sampleClient.getResource(client);
	}

	public void getResource(IGenericClient iGenericClient) {
		PatientDetails patientDetails = new PatientDetails();
		FileWrite fileWrite = new FileWrite();
		FileReader fileReader = new FileReader();
		BufferedReader bufferedReader;
		String lastName;
		List<String> avengersTimeList = new ArrayList<String>();

		try {
			boolean isCacheDisabled = false; // TRUE means no caching
			for (int count = 1; count <= 3; count++) {
				bufferedReader = fileReader.readFile();
				InterceptorClass stopWatchInterceptor = new InterceptorClass();
				iGenericClient.registerInterceptor(stopWatchInterceptor);
				long avengersTime = 0;
				if (count == 3) {
					isCacheDisabled = true;
				} 
				while (bufferedReader.ready()) {
					lastName = bufferedReader.readLine();
					Bundle response = null;
					response = getResponse(response, iGenericClient, lastName, isCacheDisabled);
					avengersTime = avengersTime + stopWatchInterceptor.getStopWatch().getMillis();
					patientDetails.sortpatientList(patientDetails.getPatientList(response));					
				}
				avengersTimeList.add("Average time for execution "+count+" with cache disabled = "+isCacheDisabled+" is "+avengersTime / 20);
			}
			fileWrite.writeFileFromResources(avengersTimeList);
		} catch (IOException e) {
			logeer.error("error message:{}", e.getMessage());
		}
	}

	private Bundle getResponse(Bundle response, IGenericClient iGenericClient, String lastName, boolean val) {
		cacheControlDirective = cacheControlDirective.setNoCache(val);
		return response = iGenericClient.search().forResource("Patient").cacheControl(cacheControlDirective).sort()
				.ascending(Patient.NAME).where(Patient.FAMILY.matches().value(lastName)).returnBundle(Bundle.class)
				.execute();

	}
}
