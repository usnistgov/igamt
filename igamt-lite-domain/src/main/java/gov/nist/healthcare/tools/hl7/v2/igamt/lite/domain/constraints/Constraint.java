package gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.constraints;

import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.constraints.assertion.Assertion;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

@Entity
public class Constraint implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5723342171557075960L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected long id;
	
	@NotNull
	@Column(nullable = false)
	protected String constraintId;
	
	protected String constraintTag;
	
	protected Reference reference;
	
	@NotNull
	@Column(nullable = false)
	protected String description;
	
	@NotNull
	@Column(nullable = false)
	@Lob
	protected Assertion assertion;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getConstraintId() {
		return constraintId;
	}

	public void setConstraintId(String constraintId) {
		this.constraintId = constraintId;
	}

	public String getConstraintTag() {
		return constraintTag;
	}

	public void setConstraintTag(String constraintTag) {
		this.constraintTag = constraintTag;
	}

	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Assertion getAssertion() {
		return assertion;
	}

	public void setAssertion(Assertion assertion) {
		this.assertion = assertion;
	}

	@Override
	public String toString() {
		return "Constraint [id=" + id + ", constraintId=" + constraintId
				+ ", constraintTag=" + constraintTag + ", reference="
				+ reference + ", description=" + description + ", assertion="
				+ assertion + "]";
	}
	
	
	
	
	
}