package edu.gatech.i3l.fhir.jpa.dao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.jpa.dao.AbstractPredicateBuilder;
import ca.uhn.fhir.jpa.dao.BaseFhirResourceDao;
import ca.uhn.fhir.jpa.dao.PredicateBuilder;
import ca.uhn.fhir.model.dstu2.resource.MedicationPrescription;

@Transactional(propagation = Propagation.REQUIRED)
public class MedicationPrescriptionFhirResourceDao extends BaseFhirResourceDao<MedicationPrescription>{

	@Override
	public PredicateBuilder getPredicateBuilder() {
		return new AbstractPredicateBuilder() {
		};
	}

}
