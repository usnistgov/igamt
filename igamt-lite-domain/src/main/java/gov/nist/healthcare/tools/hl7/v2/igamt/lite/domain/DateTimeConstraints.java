package gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DateTimeConstraints  implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -3513484929589802482L;
  private List<DateTimeComponentDefinition> dateTimeComponentDefinitions = new ArrayList<DateTimeComponentDefinition>();
  private String errorMessage;
  private String simplePattern;
  private String regex;
  
  
  public DateTimeConstraints() {
    super();
  }

  public DateTimeConstraints(List<DateTimeComponentDefinition> dateTimeComponentDefinitions) {
    super();
    this.dateTimeComponentDefinitions = dateTimeComponentDefinitions;
  }

  public List<DateTimeComponentDefinition> getDateTimeComponentDefinitions() {
    return dateTimeComponentDefinitions;
  }

  public void setDateTimeComponentDefinitions(List<DateTimeComponentDefinition> dateTimeComponentDefinitions) {
    this.dateTimeComponentDefinitions = dateTimeComponentDefinitions;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getSimplePattern() {
    return simplePattern;
  }

  public void setSimplePattern(String simplePattern) {
    this.simplePattern = simplePattern;
  }

  public String getRegex() {
    return regex;
  }

  public void setRegex(String regex) {
    this.regex = regex;
  }
  
  
  
  
}

