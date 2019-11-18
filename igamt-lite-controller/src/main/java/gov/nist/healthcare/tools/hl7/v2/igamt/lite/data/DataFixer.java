package gov.nist.healthcare.tools.hl7.v2.igamt.lite.data;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.hibernate.engine.jdbc.spi.TypeSearchability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;

import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Constant.SCOPE;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Field;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Segment;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Table;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ValueSetBinding;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ValueSetBindingStrength;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ValueSetOrSingleCodeBinding;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.SegmentService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.TableService;

/**
 * @author Abdelghani El Ouakili
 *
 */
@Service
public class DataFixer {

  @Autowired
  SegmentService segmentsService;

  @Autowired
  TableService valueSetService;


  public void fixFromCSV() {
    String csvFile = "/Users/ena3/projects/hl7-igamt/bootstrap/src/main/resources/HL7tables-csv.csv";
    List<BindingInfo> locationIssues= new ArrayList<BindingInfo>();
    HashMap<String, Segment> segmentMap= new HashMap<String, Segment>();
    HashMap<String, String> valueSetIds= new HashMap<String, String>();

    CSVReader reader = null;
    try {
      reader = new CSVReader(new FileReader(csvFile));
      String[] line;
      while ((line = reader.readNext()) != null) {
        System.out.println("Country [id= " + line[0] + ", code= " + line[1] + " , name=" + line[2] + "]");
        if(line[8] !=null && line[8].equals("Frank") && line[9].equals("1")) {

          BindingInfo info = new BindingInfo(line[1], line[2], line[4], line[6], line[7]);
          fix(info);
          // locationIssues.add(new BindingInfo(line[1], line[2], line[4], line[6], line[7]));
        }
      }
      System.out.println(locationIssues.size());        
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * @param info
   * @throws ValidationException 
   */
  private void fix(BindingInfo info){
    // TODO Auto-generated method stub
	  Segment segment= this.segmentsService.findByNameAndVersionAndScope(info.name, info.version, SCOPE.HL7STANDARD.toString());
	  if( segment !=null ){
      fixLocation(segment,info.vs, info.position);
	  }
  }

  /**
   * @param s
   * @param vs
   * @param position
   * @throws ValidationException 
   */
  private void fixLocation(Segment s, String vs, String position) {
    // TODO Auto-generated method stub
    
	  Table t = valueSetService.findByScopeAndVersionAndBindingIdentifier(SCOPE.HL7STANDARD, s.getHl7Version(), vs);
	  if( t ==null){
		  System.out.println(vs+s.getHl7Version());
	  }
	  String vsID =  t.getId();

    		
    createBindingStructure(s, vsID, position);
    this.segmentsService.save(s);

  }


  /**
   * @param children
   * @param vs
   * @param position
   */
  private void createBindingStructure(Segment s, String vs, String position) {
    // TODO Auto-generated method stub 
    for(Field f: s.getFields()) {
      if( f.getPosition() ==Integer.valueOf(position)) {
    	  
    	  if(!containsBinding(s.getValueSetBindings(), vs, position)){
        	  ValueSetBinding vsb = new ValueSetBinding();
        	  vsb.setBindingStrength(ValueSetBindingStrength.R);
        	  vsb.setBindingLocation("1");
        	  vsb.setType("valueset");
        	  vsb.setTableId(vs);
        	  vsb.setLocation(position);	  
        	  s.getValueSetBindings().add(vsb);
    	  }
       break;
      }
    }
  }
  
  private boolean containsBinding(List<ValueSetOrSingleCodeBinding> bindings, String tableId, String position) {
	  return bindings.stream().anyMatch((x) -> x.getTableId().equals(tableId)&& x.getLocation().equals(position));
  }

}
