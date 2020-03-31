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
	
	public MessageStructure buildStructureOBR_R01(){
		MessageStructure structure =  new MessageStructure();
		structure.children = new ArrayList<SegOrGrp>();
		addSeg(structure.children , "MSH",Usage.R, 1, "1", 1, "2.3.1" );
		addSeg(structure.children , "PID",Usage.R, 1, "1", 2, "2.3.1" );
		addSeg(structure.children , "NK1",Usage.R, 1, "1", 3, "2.3.1" );
		addSeg(structure.children , "ORC",Usage.R, 1, "1", 4, "2.3.1" );
		addGroupORDER_OBSERVATION(structure.children);		
		return structure;
	} 
	private void addGroupORDER_OBSERVATION(List<SegOrGrp> children) {
		// TODO Auto-generated method stub
		Grp grp = new Grp();
		grp.name = "ORDER_OBSERVATION";
		grp.usage = Usage.R;
		grp.min=1;
		grp.max= "*";
		grp.position = 5;
		grp.children = new ArrayList<SegOrGrp>();
		addSeg(grp.children , "OBR",Usage.R, 1, "1", 1, "2.3.1" );
		addGroupOBSERVATION(grp.children);
		children.add(grp);
	}
	private void addGroupOBSERVATION(List<SegOrGrp> children) {
		Grp grp = new Grp();
		grp.name = "OBSERVATION";
		grp.usage = Usage.R;
		grp.min=1;
		grp.max= "*";
		grp.position = 2;
		grp.children = new ArrayList<SegOrGrp>();
		addSeg(grp.children , "OBX",Usage.O, 0, "1", 1, "2.3.1" );
		addSeg(grp.children , "NTE",Usage.O, 0, "*", 2, "2.3.1" );
		children.add(grp);

	}

	private void addSeg(List<SegOrGrp> children, String name, Usage usage, int min, String max, int position, String version) {
		// TODO Auto-generated method stub
		Seg seg = new Seg();
		seg.name=name;
		seg.usage=usage;
		seg.min=min;
		seg.max= max;
		seg.position=1;
		seg.version = version;
		children.add(seg); 
	}
	
	public void fixMessage(){
		Message message = this.message.findByStructIdAndScopeAndVersion("ORU_R01", SCOPE.HL7STANDARD.toString(), "2.3.1");
		MessageStructure newStructure = buildStructureOBR_R01();
		List<SegmentRefOrGroup> children = new ArrayList<SegmentRefOrGroup>();
		fixSturcture(newStructure.children, children);
		message.setChildren(children);
		this.message.save(message);
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
		}
		segRef.setRef(link);
		return segRef;
	}
}
