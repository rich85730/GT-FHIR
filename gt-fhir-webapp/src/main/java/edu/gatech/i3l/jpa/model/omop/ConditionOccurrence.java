/**
 * 
 */
package edu.gatech.i3l.jpa.model.omop;

import java.sql.Date;
import java.util.Collection;


import ca.uhn.fhir.jpa.entity.BaseTag;
import ca.uhn.fhir.jpa.entity.TagDefinition;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;

/**
 * @author MC142
 *
 */
public class ConditionOccurrence extends BaseResourceTable {

	private Long id;
	private Person person;
	private Concept conditionConcept;
	private Date startDate;
	private Date endDate;
	private Concept conditionTypeConcept;
	private String stopReason;
	private Long providerIdFix;
	private Long encounterFix;
	private String sourceValue;
	
	public ConditionOccurrence() {
		super();
	}
	
	public ConditionOccurrence(Long id, Person person, Concept conditionConcept,
			Date startDate, Date endDate, Concept conditionTypeConcept, String stopReason,
			Long providerIdFix, Long encounterFix, String sourceValue) {
		super();
		
		this.id = id;
		this.person = person;
		this.conditionConcept = conditionConcept;
		this.startDate = startDate;
		this.endDate = endDate;
		this.conditionTypeConcept = conditionTypeConcept;
		this.stopReason = stopReason;
		this.providerIdFix = providerIdFix;
		this.encounterFix = encounterFix;
		this.sourceValue = sourceValue;
	}
	
	/* (non-Javadoc)
	 * @see edu.gatech.i3l.jpa.model.omop.IResourceTable#getRelatedResource()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Condition getRelatedResource() {
		Condition condition = new Condition();

		// Populate condition parameters. 
		// Refer to 4.3.3 at http://hl7-fhir.github.io/condition.html 
		
		// set Identifier
		condition.setId(getIdDt());

		// Set patient reference to Patient (note: in dstu1, this was subject.)
		ResourceReferenceDt patientReference = new ResourceReferenceDt(person.getIdDt());
		condition.setPatient(patientReference);

		// Set encounter if exists. 
		if (encounterFix != null && encounterFix > 0) {
			// FIXME: encounter resource not yet implemented.
			//        we just create this reference resource manually. When encounter is implemented, we
			//        will get it from visit_occurrence class.
			ResourceReferenceDt encounterReference = new ResourceReferenceDt("Encounter/"+encounterFix);
			condition.setEncounter(encounterReference);
		}
		
		// Set asserter if exists
		// This can be either Patient or Practitioner. 
		if (providerIdFix != null && providerIdFix > 0) {
			// FIXME: Practitioner resource not yet implemented.
			ResourceReferenceDt practitionerReference = new ResourceReferenceDt("Practitioner/"+providerIdFix);
			condition.setAsserter(practitionerReference);
		}

		// Set Code
		System.out.println("ConceptID:"+this.getConditionConcept().getId().toString());
		System.out.println("ConceptName:"+this.getConditionConcept().getName());

		Vocabulary myVoc = this.getConditionConcept().getVocabulary();
		
		System.out.println("VocabularyID:"+myVoc.getId());
		System.out.println("VocabularyName:"+myVoc.getName());

		String theSystem = this.getConditionConcept().getVocabulary().getSystemUri();
		String theCode = this.getConditionConcept().getConceptCode();
		
		CodeableConceptDt conditionCodeConcept = new CodeableConceptDt();
		if (theSystem != "") {
			// Create coding here. We have one coding in this condition as OMOP allows one coding concept per condition.
			// In the future, if we want to allow multiple coding concepts here, we need to do it here.
			CodingDt coding = new CodingDt(theSystem, theCode);
			coding.setDisplay(conditionConcept.getName());
			conditionCodeConcept.addCoding(coding);
		}

		// FHIR does not require the coding. If our System URI is not mappable from
		// OMOP database, then coding would be empty. Set Text here. Even text is not
		// required in FHIR. But, then no reason to have this condition, I think...
		String theText = conditionConcept.getName() + ", "
				+ conditionConcept.getVocabulary().getName() + ", "
				+ conditionConcept.getConceptCode();
		
		conditionCodeConcept.setText(theText);
		condition.setCode(conditionCodeConcept);
		
		// Set clinicalStatus
		// We have clinicalStatus information in the FHIR extended table. This will
		// be set in the extended class. 
		
		// Set severity
		// We have this as well in the FHIR exteded table.

		// Set onset[x]
		// We may have only one date if this condition did not end. If ended, we have
		// a period. First, check if endDate is available.
		DateTimeDt startDateDt = new DateTimeDt(startDate);
		if (endDate == null) {
			// Date
			condition.setOnset(startDateDt);
		} else {
			// Period
			DateTimeDt endDateDt = new DateTimeDt(endDate);
			PeriodDt periodDt = new PeriodDt();
			periodDt.setStart(startDateDt);
			periodDt.setEnd(endDateDt);
			condition.setOnset(periodDt);
		}
		
		return condition;
	}

	/* (non-Javadoc)
	 * @see edu.gatech.i3l.jpa.model.omop.IResourceTable#getRelatedResourceType()
	 */
	@Override
	public Class<? extends IResource> getRelatedResourceType() {
		return Condition.class;
	}

	/* (non-Javadoc)
	 * @see ca.uhn.fhir.jpa.entity.BaseHasResource#addTag(ca.uhn.fhir.jpa.entity.TagDefinition)
	 */
	@Override
	public BaseTag addTag(TagDefinition arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.uhn.fhir.jpa.entity.BaseHasResource#getId()
	 */
	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see ca.uhn.fhir.jpa.entity.BaseHasResource#getIdDt()
	 */
	@Override
	public IdDt getIdDt() {
		return new IdDt(getResourceType(), id);
	}

	/* (non-Javadoc)
	 * @see ca.uhn.fhir.jpa.entity.BaseHasResource#getResourceType()
	 */
	@Override
	public String getResourceType() {
		return "Condition";
	}

	/* (non-Javadoc)
	 * @see ca.uhn.fhir.jpa.entity.BaseHasResource#getTags()
	 */
	@Override
	public Collection<? extends BaseTag> getTags() {
		return null;
	}

	/* (non-Javadoc)
	 * @see ca.uhn.fhir.jpa.entity.BaseHasResource#getVersion()
	 */
	@Override
	public long getVersion() {
		return 0;
	}

	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public Concept getConditionConcept() {
		return conditionConcept;
	}
	
	public void setConditionConcept(Concept conditionConcept) {
		this.conditionConcept = conditionConcept;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Concept getConditionTypeConcept() {
		return conditionTypeConcept;
	}
	
	public void setConditionTypeConcept(Concept conditionTypeConcept) {
		this.conditionTypeConcept = conditionTypeConcept;
	}
	
	public String getStopReason() {
		return stopReason;
	}
	
	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
	}
	
	public Long getProviderIdFix() {
		return providerIdFix;
	}
	
	// FIXME Provider and Encounter need to created. Revisit this after Provider and Encounter
	//       are implmented.
	public void setProviderIdFix(Long providerIdFix) {
		this.providerIdFix = providerIdFix;
	}
	
	public Long getEncounterFix() {
		return encounterFix;
	}
	
	public void setEncounterFix(Long encounterFix) {
		this.encounterFix = encounterFix;
	}
	
	public String getSourceValue() {
		return sourceValue;
	}
	
	public void setSourceValue(String sourceValue) {
		this.sourceValue = sourceValue;
	}
}