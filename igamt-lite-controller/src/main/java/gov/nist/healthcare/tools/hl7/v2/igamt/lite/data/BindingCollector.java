package gov.nist.healthcare.tools.hl7.v2.igamt.lite.data;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.AppInfo;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Component;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Constant.SCOPE;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Datatype;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Field;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Segment;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Table;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ValueSetBinding;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ValueSetOrSingleCodeBinding;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.DatatypeService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.SegmentService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.TableService;


/**
 * @author Abdelghani El Ouakili
 *
 */
@Service
public class BindingCollector {


	@Autowired
	TableService valueSetService;
	@Autowired
	SegmentService segmentService;
	@Autowired
	DatatypeService datatypeService;

	@Autowired
	Environment env;
	@Autowired
	AppInfo appInfo;

	public void collect() throws FileNotFoundException {
		// TODO Auto-generated constructor stub
		for(String s : appInfo.getHl7Versions()) {
			List<Table> vs= valueSetService.findByScopeAndVersion(SCOPE.HL7STANDARD.toString(),s);
			Map<String, String> valueSetsNames = vs.stream().collect(
					Collectors.toMap(Table::getId, Table::getBindingIdentifier));
			List<Datatype> datatypes= datatypeService.findByScopeAndVersion(SCOPE.HL7STANDARD.toString(), s);
			Map<String, String> datatypesNames = datatypes.stream().collect(
					Collectors.toMap(Datatype::getId, Datatype::getName));
			processSegment(s, valueSetsNames, datatypesNames);
			// processDatatypes(s,valueSetsNames, datatypesNames);
		}
	}



	/**
	 * @param s
	 * @param names
	 * @param datatypesNames 
	 * @throws FileNotFoundException 
	 */
	private void processSegment(String version, Map<String, String> valueSetsNames, Map<String, String> datatypesNames) throws FileNotFoundException {
		// TODO Auto-generated method stub
		List<Segment> segments= this.segmentService.findByScopeAndVersion(SCOPE.HL7STANDARD.toString(), version);
		gerneratefile("segments"+ version, segments, valueSetsNames,datatypesNames );
	}

	private void processDatatypes(String version, Map<String, String> valueSetsNames, Map<String, String> datatypesNames) throws FileNotFoundException {
		// TODO Auto-generated method stub
		List<Datatype> datatypes= this.datatypeService.findByScopeAndVersion(SCOPE.HL7STANDARD.toString(), version);
		gerneratefileDatatypesBindings("datatypes"+ version, datatypes, valueSetsNames,datatypesNames );
	}



	/**
	 * @param string
	 * @param datatypes
	 * @param valueSetsNames
	 * @param datatypesNames
	 * @throws FileNotFoundException 
	 */
	private void gerneratefileDatatypesBindings(String name, List<Datatype> datatypes,
			Map<String, String> valueSetsNames, Map<String, String> datatypesNames) throws FileNotFoundException {
		// TODO Auto-generated method stub

		File csvOutputFile = new File(name+ ".csv");

		List<String> dataLines = new ArrayList<String>();
		dataLines.add("Data Type, Location, Referenced Data type, Value Set");

		for(Datatype d: datatypes) {
			if(d.getComponents() !=null&& !d.getComponents().isEmpty()){
				dataLines.addAll(processDatatypeBinding(d,valueSetsNames,datatypesNames ));
			}
		}

		try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
			dataLines.stream()
			.forEach(pw::println);
		}
		assertTrue(csvOutputFile.exists());

	}



	/**
	 * @param string
	 * @param version
	 * @param segments
	 * @throws FileNotFoundException 
	 */
	private void gerneratefile(String name, List<Segment> segments,   Map<String, String> valueSetsNames, Map<String, String> datatypesNames) throws FileNotFoundException {
		// TODO Auto-generated method stub
		File csvOutputFile = new File("/Users/ena3/projects/igamt/igamt-lite-controller/data-binding-segment"+File.separator+ name+".csv");

		List<String> dataLines = new ArrayList<String>();
		dataLines.add("Segments, Location, Referenced Data type, Value Set");

		for(Segment s: segments) {
			dataLines.addAll(processBinding(s,valueSetsNames,datatypesNames ));
		}

		try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
			dataLines.stream()
			.forEach(pw::println);
		}
		assertTrue(csvOutputFile.exists());
	}

	private List<String> processBinding(Segment s,  Map<String, String> valueSetsNames, Map<String, String> datatypesNames) {
		List<String> lines= new ArrayList<String>();
		HashMap<Integer, String> usedDtNames= new HashMap<Integer, String> ();
		for( Field f : s.getFields()) {
			if(f.getDatatype() !=null && f.getDatatype().getId() !=null) {
				usedDtNames.put(f.getPosition(), datatypesNames.get(f.getDatatype().getId()));
			}
		}
		if(s.getValueSetBindings() !=null && !s.getValueSetBindings().isEmpty()) {

			for(ValueSetOrSingleCodeBinding binding: s.getValueSetBindings()) {
		
				if(binding instanceof ValueSetBinding){
					if(binding.getTableId() !=null){
						lines.add(constructBindingLine(s.getName(), binding,usedDtNames, valueSetsNames ));
					}
				}
			}
		}
		return lines;

	}


	private List<String> processDatatypeBinding(Datatype d,  Map<String, String> valueSetsNames, Map<String, String> datatypesNames) {
		List<String> lines= new ArrayList<String>();
		HashMap<Integer, String> usedDtNames= new HashMap<Integer, String> ();
		for( Component c : d.getComponents()) {
			if(c.getDatatype() !=null && c.getDatatype().getId() !=null) {
				usedDtNames.put(c.getPosition(), datatypesNames.get(c.getDatatype().getId()));
			}
		}
		if(d.getValueSetBindings() !=null && !d.getValueSetBindings().isEmpty()) {

			for(ValueSetOrSingleCodeBinding binding: d.getValueSetBindings()) {
				if(binding.getTableId() !=null){
					lines.add(constructBindingLine(d.getName(), binding,usedDtNames, valueSetsNames ));
				}
			}
		}
		return lines;

	}

	/**
	 * @param binding
	 * @param usedDtNames
	 * @param valueSetsNames
	 * @return
	 */
	private String constructBindingLine(String parent, ValueSetOrSingleCodeBinding binding,
			HashMap<Integer, String> usedDtNames, Map<String, String> valueSetsNames) {
		// TODO Auto-generated method stub
		String ret = parent + ',' + binding.getLocation() + ','+ usedDtNames.get(Integer.valueOf(binding.getLocation().trim()));

		if(binding.getTableId() !=null){
			ret = ret + ",HL7" + valueSetsNames.get(binding.getTableId()); 
		}
		return ret; 
	}


}
