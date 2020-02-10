package gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain;


public class DateTimePredicate {
  public enum PredicateType {
    PRESENCE, NOTPRESENCE, EQUAL, NOTEQUAL
  }

  private Usage trueUsage;
  private Usage falseUsage;
  private DateTimeComponentDefinition target;
  private PredicateType predicateType;
  private String value;


  public DateTimePredicate(Usage trueUsage, Usage falseUsage, DateTimeComponentDefinition target,
      PredicateType predicateType, String value) {
    super();
    this.trueUsage = trueUsage;
    this.falseUsage = falseUsage;
    this.target = target;
    this.predicateType = predicateType;
    this.value = value;
  }

  public Usage getTrueUsage() {
    return trueUsage;
  }

  public void setTrueUsage(Usage trueUsage) {
    this.trueUsage = trueUsage;
  }

  public Usage getFalseUsage() {
    return falseUsage;
  }

  public void setFalseUsage(Usage falseUsage) {
    this.falseUsage = falseUsage;
  }

  public DateTimeComponentDefinition getTarget() {
    return target;
  }

  public void setTarget(DateTimeComponentDefinition target) {
    this.target = target;
  }

  public PredicateType getPredicateType() {
    return predicateType;
  }

  public void setPredicateType(PredicateType predicateType) {
    this.predicateType = predicateType;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


}
