package gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.constraints;

import org.springframework.data.annotation.Id;

import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Usage;

public class CoConstraintColumnDefinition {

	@Id
	private String id;
	private String path;
	private String constraintPath;
	private String type; // field or component
	private String constraintType; //valueset or value
	private String name;
	private Usage usage;
	private String dtId;
	private boolean primitive;
	private boolean dMReference;

	public CoConstraintColumnDefinition() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getConstraintPath() {
		return constraintPath;
	}

	public void setConstraintPath(String constraintPath) {
		this.constraintPath = constraintPath;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Usage getUsage() {
		return usage;
	}

	public void setUsage(Usage usage) {
		this.usage = usage;
	}

	public String getDtId() {
		return dtId;
	}

	public void setDtId(String dtId) {
		this.dtId = dtId;
	}

	public String getConstraintType() {
		return constraintType;
	}

	public void setConstraintType(String constraintType) {
		this.constraintType = constraintType;
	}

	public boolean isPrimitive() {
		return primitive;
	}

	public void setPrimitive(boolean primitive) {
		this.primitive = primitive;
	}

	public boolean isdMReference() {
		return dMReference;
	}

	public void setdMReference(boolean dMReference) {
		this.dMReference = dMReference;
	}

}