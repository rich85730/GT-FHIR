package edu.gatech.i3l.fhir.jpa.dao;

import ca.uhn.fhir.jpa.dao.BaseFhirResourceDao;
import ca.uhn.fhir.jpa.query.AbstractPredicateBuilder;
import ca.uhn.fhir.jpa.query.PredicateBuilder;
import ca.uhn.fhir.model.dstu2.resource.Location;

public class LocationFhirResourceDao extends BaseFhirResourceDao<Location>{

	public LocationFhirResourceDao() {
		super();
		setResourceEntity(edu.gatech.i3l.fhir.dstu2.entities.Location.class);
		setValidateBean(true);
	}

	@Override
	public PredicateBuilder getPredicateBuilder() {
		return new AbstractPredicateBuilder(){};
	}
}