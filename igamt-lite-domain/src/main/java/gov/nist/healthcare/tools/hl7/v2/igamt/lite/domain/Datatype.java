package gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain;

import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.constraints.ConformanceStatement;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.constraints.Predicate;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "DATATYPE")
public class Datatype extends DataModel implements java.io.Serializable,
		Cloneable {

	private static final long serialVersionUID = 1L;

	public Datatype() {
		super();
		this.type = Constant.DATATYPE;
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(nullable = false, name = "LABEL")
	private String label;

	@JsonProperty("children")
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, orphanRemoval = true)
	@javax.persistence.JoinTable(name = "DATATYPE_COMPONENT", joinColumns = @JoinColumn(name = "DATATYPE_ID"), inverseJoinColumns = @JoinColumn(name = "COMPONENT_ID", unique = false))
	// @org.hibernate.annotations.OrderBy(clause = "position asc")
	protected Set<Component> components = new LinkedHashSet<Component>();

	@NotNull
	@Column(nullable = false, name = "DATATYPE_NAME")
	private String name;

	@Column(nullable = true, name = "DATATYPE_DESC")
	private String description;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, orphanRemoval = true)
	@javax.persistence.JoinTable(name = "DATATYPE_PREDICATE", joinColumns = @JoinColumn(name = "DATATYPE_ID"), inverseJoinColumns = @JoinColumn(name = "PREDICATE_ID"))
	protected Set<Predicate> predicates = new HashSet<Predicate>();

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, orphanRemoval = true)
	@javax.persistence.JoinTable(name = "DATATYPE_CONFSTATEMENT", joinColumns = @JoinColumn(name = "DATATYPE_ID"), inverseJoinColumns = @JoinColumn(name = "CONFSTATEMENT_ID", unique = false))
	protected Set<ConformanceStatement> conformanceStatements = new HashSet<ConformanceStatement>();

	@com.fasterxml.jackson.annotation.JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DATATYPES_ID")
	private Datatypes datatypes;

	@Column(name = "COMMENT", columnDefinition = "TEXT")
	protected String comment;

	@Column(name = "USAGE_NOTE", columnDefinition = "TEXT")
	protected String usageNote;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Set<Component> getComponents() {
		return components;
	}

	public void setComponents(Set<Component> components) {
		if (components != null) {
			this.components.clear();
			Iterator<Component> it = components.iterator();
			while (it.hasNext()) {
				addComponent(it.next());
			}
		} else {
			this.components = null;
		}
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

	public Datatypes getDatatypes() {
		return datatypes;
	}

	public void setDatatypes(Datatypes datatypes) {
		this.datatypes = datatypes;
	}

	public Set<Predicate> getPredicates() {
		return predicates;
	}

	public void setPredicates(Set<Predicate> predicates) {
		if (predicates != null) {
			this.predicates.clear();
			Iterator<Predicate> it = predicates.iterator();
			while (it.hasNext()) {
				addPredicate(it.next());
			}
		} else {
			this.predicates = null;
		}
	}

	public void setConformanceStatements(
			Set<ConformanceStatement> conformanceStatements) {
		if (conformanceStatements != null) {
			this.conformanceStatements.clear();
			Iterator<ConformanceStatement> it = conformanceStatements
					.iterator();
			while (it.hasNext()) {
				addConformanceStatement(it.next());
			}
		} else {
			this.conformanceStatements = null;
		}
	}

	public Set<ConformanceStatement> getConformanceStatements() {
		return conformanceStatements;
	}

	public void addPredicate(Predicate p) {
		predicates.add(p);
	}

	public void addConformanceStatement(ConformanceStatement cs) {
		conformanceStatements.add(cs);
	}

	public void addComponent(Component c) {
		c.setPosition(components.size() + 1);
		components.add(c);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUsageNote() {
		return usageNote;
	}

	public void setUsageNote(String usageNote) {
		this.usageNote = usageNote;
	}

	@Override
	public String toString() {
		return "Datatype [id=" + id + ", label=" + label + ", name=" + name
				+ ", description=" + description + "]";
	}

	@Override
	public Datatype clone() throws CloneNotSupportedException {
		Datatype clonedDT = (Datatype) super.clone();
		clonedDT.setId(null);

		clonedDT.setDatatypes(null);
		this.datatypes.addDatatype(clonedDT);

		clonedDT.setConformanceStatements(new HashSet<ConformanceStatement>());
		for (ConformanceStatement cs : this.conformanceStatements) {
			clonedDT.addConformanceStatement(cs.clone());
		}

		clonedDT.setPredicates(new HashSet<Predicate>());
		for (Predicate cp : this.predicates) {
			clonedDT.addPredicate(cp.clone());
		}

		clonedDT.setComponents(new LinkedHashSet<Component>());
		for (Component c : this.components) {
			clonedDT.addComponent(c.clone());
		}

		return clonedDT;
	}

}
