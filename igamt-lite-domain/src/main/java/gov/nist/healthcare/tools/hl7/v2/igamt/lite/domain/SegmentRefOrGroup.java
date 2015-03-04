package gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "SEGMENTREFORGROUP")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class SegmentRefOrGroup extends DataModel implements
		java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.TABLE)
	protected Long id;

	@NotNull
	@Column(nullable = false, name = "USAGEE")
	@Enumerated(EnumType.STRING)
	protected Usage usage;

	@NotNull
	@Min(0)
	@Column(nullable = false, name = "MIN")
	protected Integer min;

	@NotNull
	@Column(nullable = false, name = "MAX")
	protected String max;

	@NotNull
	@Column(nullable = false, name = "SEGMENTREFORGROUP_POSITION")
	protected Integer position = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usage getUsage() {
		return usage;
	}

	public void setUsage(Usage usage) {
		this.usage = usage;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

}
