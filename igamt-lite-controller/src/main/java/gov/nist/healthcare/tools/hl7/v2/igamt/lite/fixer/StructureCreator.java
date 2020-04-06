package gov.nist.healthcare.tools.hl7.v2.igamt.lite.fixer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Constant.SCOPE;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Group;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Message;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Segment;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.SegmentLink;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.SegmentRef;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.SegmentRefOrGroup;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Usage;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.repo.MessageRepository;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.SegmentService;

@Service
public class StructureCreator {
	@Autowired
	MessageRepository message;
	@Autowired
	SegmentService seg;
	
	public MessageStructure buildStructureOBR_R01(String version){
		MessageStructure structure =  new MessageStructure();
		structure.children = new ArrayList<SegOrGrp>();
		addSeg(structure.children , "MSH",Usage.R, 1, "1", 1, version );
		addSeg(structure.children , "PID",Usage.R, 1, "1", 2, version );
		if(!version.equals("2.3")){
		addSeg(structure.children , "NK1",Usage.R, 1, "1", structure.children.size()+1, version );
		}
		addSeg(structure.children , "ORC",Usage.R, 1, "1", structure.children.size()+1, version );
		addGroupORDER_OBSERVATION(structure.children, version);		
		return structure;
	}
	
	public MessageStructure buildStructureOBR_R01_Standard(String version){
		MessageStructure structure =  new MessageStructure();
		structure.children = new ArrayList<SegOrGrp>();
		addSeg(structure.children , "MSH",Usage.R, 1, "1", structure.children.size()+1, version );
		addGroup_PATIENT_RESULT(structure.children,version);	
		if(!version.equals("2.3")){
		addSeg(structure.children , "DSC",Usage.O, 0, "1",structure.children.size()+1, version );
		}
		return structure;
	} 
	
	private void addGroup_PATIENT_RESULT(List<SegOrGrp> children, String version) {
		// TODO Auto-generated method stub
		
		Grp grp = new Grp();
		grp.name = "PATIENT_RESULT";
		grp.usage = Usage.R;
		grp.min=1;
		grp.max= "*";
		grp.position = children.size()+1;
		grp.children = new ArrayList<SegOrGrp>();
		addGroupPATIENT(grp.children,version);
		addGroupORDER_OBSERVATION(grp.children, version);

		children.add(grp);
		
	}

	private void addGroupPATIENT(List<SegOrGrp> children, String version) {
		// TODO Auto-generated method stub
		Grp grp = new Grp();
		grp.name = "PATIENT";
		grp.usage = Usage.O;
		grp.min=0;
		grp.max= "1";
		grp.position =  children.size()+1;
		grp.children = new ArrayList<SegOrGrp>();
		addSeg(grp.children , "PID",Usage.R, 1, "1", grp.children.size()+1, version );
		addSeg(grp.children , "PD1",Usage.O, 0, "1", grp.children.size()+1, version );
		if(!version.equals("2.3")){
		addSeg(grp.children , "NK1",Usage.O, 0, "*", grp.children.size()+1, version );
		}
		addSeg(grp.children , "NTE",Usage.O, 0, "*", grp.children.size()+1, version );
		
		addGroupVISIT(grp.children,version);

		children.add(grp);
		
	}

	private void addGroupVISIT(List<SegOrGrp> children, String version) {
		// TODO Auto-generated method stub
		Grp grp = new Grp();
		grp.name = "VISIT";
		grp.usage = Usage.O;
		grp.min=0;
		grp.max= "1";
		grp.position = children.size()+1;
		grp.children = new ArrayList<SegOrGrp>();
		addSeg(grp.children , "PV1",Usage.R, 1, "1", grp.children.size()+1, version );
		addSeg(grp.children , "PV2",Usage.O, 0, "1", grp.children.size()+1, version );
				
		children.add(grp);
	}

	private void addGroupORDER_OBSERVATION(List<SegOrGrp> children, String version) {
		// TODO Auto-generated method stub
		Grp grp = new Grp();
		grp.name = "ORDER_OBSERVATION";
		grp.usage = Usage.R;
		grp.min=1;
		grp.max= "*";
		grp.position = children.size()+1;
		grp.children = new ArrayList<SegOrGrp>();
		addSeg(grp.children , "ORC",Usage.O, 0, "1", grp.children.size()+1, version );
		addSeg(grp.children , "OBR",Usage.R, 1, "1", grp.children.size()+1, version );
		addSeg(grp.children , "NTE",Usage.O, 0, "*", grp.children.size()+1, version );

		addGroupOBSERVATION(grp.children, version);
		addSeg(grp.children , "CTI",Usage.O, 0, "*", grp.children.size()+1, version );

		children.add(grp);
	}
	private void addGroupOBSERVATION(List<SegOrGrp> children, String version) {
		Grp grp = new Grp();
		grp.name = "OBSERVATION";
		grp.usage = Usage.R;
		grp.min=1;
		grp.max= "*";
		grp.position = children.size()+1;
		grp.children = new ArrayList<SegOrGrp>();
		addSeg(grp.children , "OBX",Usage.O, 0, "1", grp.children.size()+1, version );
		addSeg(grp.children , "NTE",Usage.O, 0, "*", grp.children.size()+1, version );
		children.add(grp);
	}

	private void addSeg(List<SegOrGrp> children, String name, Usage usage, int min, String max, int position, String version) {
		// TODO Auto-generated method stub
		Seg seg = new Seg();
		seg.name=name;
		seg.usage=usage;
		seg.min=min;
		seg.max= max;
		seg.position=position;
		seg.version = version;
		children.add(seg); 
	}
	
	public void fixMessage(String version){
		MessageStructure newStructure = buildStructureOBR_R01_Standard(version);

		Message message = this.message.findByStructIdAndScopeAndVersion("ORU_R01", SCOPE.HL7STANDARD.toString(), version);
		fixSingleMessage(message, newStructure);
		
//		List<Message> users = this.message.findByStructureIdAndScopeAndVersion("ORU_R01", SCOPE.USER.toString(), "2.3.1");
//		for(Message msg: users) {
//		fixSingleMessage(msg, newStructure);
//		}
	}
	
	public void fixSingleMessage(Message m, MessageStructure newStructure){
		List<SegmentRefOrGroup> children = new ArrayList<SegmentRefOrGroup>();
		fixSturcture(newStructure.children, children);
		m.setChildren(children);
		this.message.save(m);
	}
	private void fixSturcture(List<SegOrGrp> children, List<SegmentRefOrGroup> messageChildren) {
		// TODO Auto-generated method stub
		for(SegOrGrp child: children){
			messageChildren.add(processSegmentOrGroup(child));		
		}
		
	}
	
	
	private SegmentRefOrGroup processSegmentOrGroup(SegOrGrp child) {

		if(child instanceof Seg){
			return processSegment((Seg)child);
		}else if(child instanceof Grp) {
			return processGRoup((Grp)child);
		}
		return null;
		
	}
	private SegmentRefOrGroup processGRoup(Grp child) {
		Group grp= new Group();
		grp.setUsage(child.usage);
		grp.setMin(child.min);
		grp.setMax(child.max);
		grp.setPosition(child.position);
		grp.setName(child.name);
		List<SegmentRefOrGroup> children  = new ArrayList<SegmentRefOrGroup>();
		grp.setChildren(children);
		if(child.children !=null ){
			for(SegOrGrp subChild: child.children){
				grp.getChildren().add(processSegmentOrGroup(subChild));
			}
		}
		// TODO Auto-generated method stub
		return grp;
	}
	private SegmentRefOrGroup processSegment(Seg child) {
		// TODO Auto-generated method stub
		SegmentRef segRef= new SegmentRef();
		segRef.setUsage(child.usage);
		segRef.setMin(child.min);
		segRef.setMax(child.max);
		segRef.setPosition(child.position);
		SegmentLink link = new SegmentLink();
		Segment s = seg.findByNameAndVersionAndScope(child.name, child.version, SCOPE.HL7STANDARD.toString());
		if(s == null){
			System.out.println(child.name+ "Not found");
		}else {
			link.setId(s.getId());
			link.setName(s.getName());
		}
		segRef.setRef(link);
		return segRef;
	}
}
