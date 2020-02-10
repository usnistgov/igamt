package gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain;

import java.io.Serializable;


public class DateTimeComponentDefinition implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2526258830529516566L;
  private int position;
  private String name;
  private String description;
  private String format;
  private Usage usage;
  private DateTimePredicate dateTimePredicate;
 
  public DateTimeComponentDefinition(){
    super();
  }

  public DateTimeComponentDefinition(int position, String name, String format, Usage usage) {
    super();
    this.position = position;
    this.name = name;
    this.usage = usage;
    this.format = format;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Usage getUsage() {
    return usage;
  }

  public void setUsage(Usage usage) {
    this.usage = usage;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public DateTimePredicate getDateTimePredicate() {
    return dateTimePredicate;
  }

  public void setDateTimePredicate(DateTimePredicate dateTimePredicate) {
    this.dateTimePredicate = dateTimePredicate;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }


}
