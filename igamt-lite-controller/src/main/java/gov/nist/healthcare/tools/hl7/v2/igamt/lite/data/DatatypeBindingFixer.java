package gov.nist.healthcare.tools.hl7.v2.igamt.lite.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.opencsv.CSVReader;

import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Field;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Segment;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Table;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ValueSetBinding;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ValueSetBindingStrength;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ValueSetOrSingleCodeBinding;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Component;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Constant.SCOPE;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Datatype;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.DatatypeService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.TableService;

@Service
public class DatatypeBindingFixer {

	  @Autowired
	  DatatypeService datatypeService;

	  @Autowired
	  TableService valueSetService;


	  public void fixFromCSV() throws FileNotFoundException {
	    String csvFile = "/Users/ena3/projects/hl7-igamt/bootstrap/src/main/resources/HL7tables-csv.csv";
	   
		File file = ResourceUtils.getFile("classpath:bindings/datatype-binding-fix.csv");
		System.out.println(file.exists());
	    List<BindingInfo> locationIssues= new ArrayList<BindingInfo>();
	    CSVReader reader = null;
	    try {
	      reader = new CSVReader(new FileReader(file));
	      String[] line1= reader.readNext();
	      while ((line1 = reader.readNext()) != null) {
	          BindingInfo info = new BindingInfo(line1[0], line1[1], line1[2], line1[3]);
	          System.out.println(info);

	         fix(info);
	        
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
		  List<Datatype> dt= this.datatypeService.findByNameAndVersionAndScope(info.name, info.version, SCOPE.HL7STANDARD.toString());
		  if( dt !=null&&!dt.isEmpty()){
	      fixLocation(dt.get(0),info.vs, info.position);
		  }
	  }

	  /**
	   * @param s
	   * @param vs
	   * @param position
	   * @throws ValidationException 
	   */
	  private void fixLocation(Datatype s, String vs, String position) {
	    // TODO Auto-generated method stub
	    
		  Table t = valueSetService.findByScopeAndVersionAndBindingIdentifier(SCOPE.HL7STANDARD, s.getHl7Version(), vs.substring(3));
		  if( t ==null){
			  System.out.println(vs+s.getHl7Version());
		  }
		  String vsID =  t.getId();

	    		
	    createBindingStructure(s, vsID, position);
	    this.datatypeService.save(s);

	  }


	  /**
	   * @param children
	   * @param vs
	   * @param position
	   */
	  private void createBindingStructure(Datatype s, String vs, String position) {
	    // TODO Auto-generated method stub 
	    for(Component f: s.getComponents()) {
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
