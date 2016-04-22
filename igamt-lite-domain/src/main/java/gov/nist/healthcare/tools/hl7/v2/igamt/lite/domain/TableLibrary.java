package gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "table-library")
public class TableLibrary extends TextbasedSectionModel implements java.io.Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private Long accountId;
	
	private String date;
	
	private String ext;

	private String valueSetLibraryIdentifier;

	private String status;

	private String valueSetLibraryVersion;

	private String organizationName;

	private String description;
	
	private String dateCreated;

	private String profileName = "";
	
	private TableLibraryMetaData metaData;
	
	private Constant.SCOPE scope;
	
	public TableLibrary() {
		super();
		this.id = ObjectId.get().toString();
	}

	private Set<String> children = new HashSet<String>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getValueSetLibraryIdentifier() {
		return valueSetLibraryIdentifier;
	}

	public void setValueSetLibraryIdentifier(String valueSetLibraryIdentifier) {
		this.valueSetLibraryIdentifier = valueSetLibraryIdentifier;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getValueSetLibraryVersion() {
		return valueSetLibraryVersion;
	}

	public void setValueSetLibraryVersion(String valueSetLibraryVersion) {
		this.valueSetLibraryVersion = valueSetLibraryVersion;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public Set<String> getChildren() {
		return children;
	}

	public void setChildren(Set<String> children) {
		this.children = children;
	}

	public Constant.SCOPE getScope() {
		return scope;
	}

	public void setScope(Constant.SCOPE scope) {
		this.scope = scope;
	}

	public void addTable(String t) {
		children.add(t);
	}

	public String save(String d) {
		if (!this.children.contains(d)) {
			children.add(d);
		}
		return d;
	}

	public void delete(String id) {
		String d = findOneTableById(id);
		if (d != null)
			this.children.remove(d);
	}
	
	public String findOneTableById(String id) {
		if (this.children != null) {
			for (String m : this.children) {
				if (m.equals(id)) {
					return m;
				}
			}
		}

		return null;
	}

	public TableLibrary clone(HashMap<String, Table> tabRecords)
			throws CloneNotSupportedException {
		TableLibrary clonedTables = new TableLibrary();
//		clonedTables.setChildren(new HashSet<Table>());
//		for (Table tab : this.children) {
//			if (tabRecords.containsKey(tab.getId())) {
//				clonedTables.addTable(tabRecords.get(tab.getId()));
//			} else {
//				Table clone = tab.clone();
//				clone.setId(tab.getId());
//				tabRecords.put(tab.getId(), clone);
//				clonedTables.addTable(clone);
//			}
//		}

		return clonedTables;
	}
	
	public void merge(TableLibrary tabs){
		this.getChildren().addAll(tabs.getChildren());
	}
	
//	@JsonIgnore
//	public Code getCode() {
//		//TODO Only byID constraints are considered; might want to consider byName
//		Constraints constraints = new Constraints();
//		Context tabContext = new Context();
//
//		Set<ByNameOrByID> byNameOrByIDs = new HashSet<ByNameOrByID>();
//		byNameOrByIDs = new HashSet<ByNameOrByID>();
//		for (Table d : this.getChildren()) {
//			ByID byID = new ByID();
//			byID.setByID(d.getLabel());
//			if (d.getConformanceStatements().size() > 0) {
//				byID.setConformanceStatements(d.getConformanceStatements());
//				byNameOrByIDs.add(byID);
//			}
//		}
//		tabContext.setByNameOrByIDs(byNameOrByIDs);
//
//		constraints.setDatatypes(tabContext);
//		return constraints;
//	}

	public TableLibraryMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(TableLibraryMetaData metaData) {
		this.metaData = metaData;
	}

	public Set<String> getTables() {
		return children;
	}

	public void setTables(Set<String> children) {
		this.children = children;
	}
}