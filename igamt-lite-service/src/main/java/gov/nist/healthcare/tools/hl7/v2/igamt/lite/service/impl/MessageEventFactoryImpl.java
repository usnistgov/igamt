package gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Code;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Constant.SCOPE;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Message;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Messages;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Table;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.messageevents.Event;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.messageevents.MessageEvents;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.repo.TableRepository;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.MessageEventFactory;

@Service
public class MessageEventFactoryImpl implements MessageEventFactory {

  private static Logger log = LoggerFactory.getLogger(MessageEventFactory.class);

  @Autowired
  private TableRepository tableRepository;

  public TableRepository getTableRepository() {
    return tableRepository;
  }

  public void setTableRepository(TableRepository tableRepository) {
    this.tableRepository = tableRepository;
  }

  @Override
  public List<MessageEvents> createMessageEvents(List<Message> msgs, String hl7Version) {

    Table tableO354 = getTable(hl7Version, "0354");
    HashMap<String, String> descriptionMap = this.getDescriptionMap(hl7Version, "0003");
    List<MessageEvents> ret = new ArrayList<MessageEvents>();
    if (tableO354 != null) {
      for (Message msg : msgs) {
        String id = msg.getId();
        String structID = msg.getStructID();
        List<Event> events = findEvents(structID, tableO354, descriptionMap, id);
        Collections.sort(events);
        String description = msg.getDescription();
        ret.add(new MessageEvents(id, structID, events, description));
      }
    }
    return ret;
  }

  public List<Event> findEvents(String structID, Table tableO354, HashMap<String, String> descriptionMap, String messageId) {
    List<String> eventsNames = new ArrayList<String>();
    List<Event> ret = new ArrayList<Event>();
    String structID1 = fixUnderscore(structID);
    Code code = tableO354.findOneCodeByValue(structID1);
    if (code != null) {
      String label = code.getLabel();
      label = label == null ? "Varies" : label;
      String[] ss = label.split(","); 
      Collections.addAll(eventsNames, ss);
      for(String s :eventsNames){
    	  Event ev = new Event(messageId, s.trim(), structID, null);
    	  if(descriptionMap.containsKey(s.toLowerCase().trim())){
    		  ev.setDescription(descriptionMap.get(s.toLowerCase().trim()));
    	  }
    	 ret.add(ev);
    	 
      }
    } else {
      log.error("No code found for structID=" + structID1);
    }
    
    return ret;
  }

  public String fixUnderscore(String structID) {
    if (structID.endsWith("_")) {
      int pos = structID.length();
      return structID.substring(0, pos - 1);
    } else {
      return structID;
    }
  }

  public Table getTable(String hl7Version, String s) {
    return tableRepository.findByBindingIdentifierAndHL7VersionAndScope(s, hl7Version,
        SCOPE.HL7STANDARD);
  }
  public HashMap<String, String> getDescriptionMap(String hl7Version, String s) {
	HashMap<String, String> descriptionMap = new HashMap<String, String>();
	Table table= getTable(hl7Version, s);
	if(table != null) {
		for(Code c: table.getCodes()){
			if(c.getValue() !=null){
				descriptionMap.put(c.getValue().toLowerCase().trim(), c.getLabel());
			}
		}
	}
	return descriptionMap;
  } 
}
