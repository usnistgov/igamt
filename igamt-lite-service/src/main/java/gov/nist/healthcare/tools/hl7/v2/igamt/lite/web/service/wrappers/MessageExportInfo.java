package gov.nist.healthcare.tools.hl7.v2.igamt.lite.web.service.wrappers;
public class MessageExportInfo {

    private boolean autoGenerated=false;
    private String originId,msgId,name,description,identifier, event;
    
	public MessageExportInfo() {
		super();
	}

	public boolean isAutoGenerated() {
		return autoGenerated;
	}

	public void setAutoGenerated(boolean autoGenerated) {
		this.autoGenerated = autoGenerated;
	}

	public String getOriginId() {
		return originId;
	}

	public void setOriginId(String originId) {
		this.originId = originId;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

  
}