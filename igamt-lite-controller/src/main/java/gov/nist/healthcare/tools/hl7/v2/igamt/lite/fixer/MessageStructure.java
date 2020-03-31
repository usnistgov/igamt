package gov.nist.healthcare.tools.hl7.v2.igamt.lite.fixer;

import java.util.ArrayList;
import java.util.List;

public class MessageStructure {

	 String identifier; // Message/@Identifier

	 String messageID;

	 String name; // Message/@Name

	 String messageType; // Message/@Type

	 String event; // Message/@Event

	 String structID; // Message/@StructID

	 String description; // Message/@Description
	
	List<SegOrGrp> children= new ArrayList<SegOrGrp>();
	
	
	
}
