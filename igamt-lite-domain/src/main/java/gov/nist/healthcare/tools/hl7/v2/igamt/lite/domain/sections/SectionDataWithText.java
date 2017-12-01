package gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.sections;


import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("withText")
public class SectionDataWithText extends SectionData{
	
	
	protected String sectionContent;
	protected String referenceType;
	protected String referenceId;
	protected  Integer position;
	
	public String getReferenceType() {
		return referenceType;
	}
	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public String getSectionContent() {
		return sectionContent;
	}
	public void setSectionContent(String sectionContent) {
		this.sectionContent = sectionContent;
	}
	public String getSectionTitle() {
		return sectionTitle;
	}
	public void setSectionTitle(String sectionTitle) {
		this.sectionTitle = sectionTitle;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	protected String sectionTitle;

	protected String section;
	public SectionDataWithText() {
		super();
	}
}
