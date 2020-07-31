
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatientDetails {

	private static final Logger logeer = LoggerFactory.getLogger(PatientDetails.class);
	FileWrite fileWrite = new FileWrite();

	public List<Patient> getPatientList(Bundle response) {

		logeer.info("store Patient information in a Patient list");
		List<Patient> patientList = new ArrayList<>();
		for (BundleEntryComponent entry : response.getEntry()) {
			Resource res = entry.getResource();
			if (res instanceof Patient) {
				patientList.add((Patient) res);
			}
		}
		return patientList;
	}

	public void sortpatientList(List<Patient> patientLists) {
		Collections.sort(patientLists, new ComparatorClass());
		for (Patient patient : patientLists) {
			logeer.info("Birth Date:{} -- First Name:{} -- Last Name:{}", patient.getBirthDate(),
					patient.getName().get(0).getGivenAsSingleString(), patient.getName().get(0).getFamily());
		}

	}

	class ComparatorClass implements Comparator<Patient> {

		@Override
		public int compare(Patient patient1, Patient patient2) {

			if (patient2.getName().get(0).getGivenAsSingleString() == null) {
				return (patient1.getName().get(0).getGivenAsSingleString() == null) ? 0 : -1;
			}
			if (patient1.getName().get(0).getGivenAsSingleString() == null) {
				return 1;
			}
			return patient1.getName().get(0).getGivenAsSingleString()
					.compareTo(patient2.getName().get(0).getGivenAsSingleString());
		}

	}

}
