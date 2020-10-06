package gov.nist.healthcare.tools.hl7.v2.igamt.lite.fixer;

import java.util.ArrayList;
import java.util.List;

import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Usage;

public class Grp extends SegOrGrp {
	public Grp() {
		// TODO Auto-generated constructor stub
	}
	String name;
	int position;
	Usage usage;
	String version;
	List<SegOrGrp> children = new ArrayList<SegOrGrp>();
}
