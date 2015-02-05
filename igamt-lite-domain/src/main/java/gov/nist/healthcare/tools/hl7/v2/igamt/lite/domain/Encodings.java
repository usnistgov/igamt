package gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;

@Embeddable
public class Encodings implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@ElementCollection
	@CollectionTable
	@Column(nullable = false)
	private Set<Encoding> encodings = new HashSet<Encoding>();

	public Set<Encoding> getEncodings() {
		return encodings;
	}

	public void setEncodings(Set<Encoding> encodings) {
		this.encodings = encodings;
	}

}
