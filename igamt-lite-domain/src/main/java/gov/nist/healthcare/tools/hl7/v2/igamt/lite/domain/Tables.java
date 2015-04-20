package gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tables")
public class Tables extends DataModel implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2904036105687742572L;

	@Id
	private String id;

	private String tableLibraryIdentifier;

	private String status;

	private String tableLibraryVersion;

	private String organizationName;

	private String name;

	private String description;

	private Set<Table> children = new HashSet<Table>();

	public Tables() {
		super();
		this.type = Constant.TABLELIBRARY;
		this.id = ObjectId.get().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTableLibraryIdentifier() {
		return tableLibraryIdentifier;
	}

	public void setTableLibraryIdentifier(String tableLibraryIdentifier) {
		this.tableLibraryIdentifier = tableLibraryIdentifier;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTableLibraryVersion() {
		return tableLibraryVersion;
	}

	public void setTableLibraryVersion(String tableLibraryVersion) {
		this.tableLibraryVersion = tableLibraryVersion;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Table> getChildren() {
		return children;
	}

	public void setChildren(Set<Table> children) {
		this.children = children;
	}

	public void addTable(Table t) {
		children.add(t);
	}

	public Table findOne(String id) {
		if (this.children != null)
			for (Table m : this.children) {
				if (m.getId().equals(id)) {
					return m;
				}
			}

		return null;
	}

	public Code findOneCode(String id) {
		if (this.children != null) {
			for (Table m : this.children) {
				Code c = m.findOneCode(id);
				if (c != null) {
					return c;
				}
			}
		}
		return null;
	}

	public void delete(String id) {
		Table t = findOne(id);
		if (t != null)
			this.children.remove(t);
	}

	public boolean deleteCode(String id) {
		if (this.children != null) {
			for (Table m : this.children) {
				Code c = m.findOneCode(id);
				if (c != null) {
					return m.deleteCode(c);
				}
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Tables [id=" + id + ", tableLibraryIdentifier="
				+ tableLibraryIdentifier + ", status=" + status
				+ ", tableLibraryVersion=" + tableLibraryVersion
				+ ", organizationName=" + organizationName + ", name=" + name
				+ ", description=" + description + "]";
	}

	public Tables clone(HashMap<String, Table> tableRecords)
			throws CloneNotSupportedException {
		Tables clonedTables = new Tables();
		clonedTables.setChildren(new HashSet<Table>());
		for (Table t : this.children) {
			clonedTables.addTable(t.clone());
			if (tableRecords.containsKey(t.getId())) {
				clonedTables.addTable(tableRecords.get(t.getId()));
			} else {
				Table clone = t.clone();
				tableRecords.put(t.getId(), clone);
				clonedTables.addTable(clone);
			}
		}

		clonedTables.setDescription(description);
		clonedTables.setName(name);
		clonedTables.setOrganizationName(organizationName);
		clonedTables.setStatus(status);
		clonedTables.setTableLibraryIdentifier(tableLibraryIdentifier);
		clonedTables.setTableLibraryVersion(tableLibraryVersion);

		return clonedTables;
	}
}
