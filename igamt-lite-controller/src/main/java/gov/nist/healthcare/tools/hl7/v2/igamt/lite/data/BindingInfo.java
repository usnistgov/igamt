package gov.nist.healthcare.tools.hl7.v2.igamt.lite.data;

/**
 * @author Abdelghani El Ouakili
 *
 */
public class BindingInfo {
  String name;
  String position;
  String dtName;
  String vs;
  String version;
  /**
   * @param name
   * @param position
   * @param dtName
   * @param vs
   * @param version
   */
  public BindingInfo(String name, String position, String dtName, String vs, String version) {
    super();
    this.name = name;
    this.position = position;
    this.dtName = dtName;
    this.vs = vs;
    this.version = version;
  }
  public BindingInfo(String version, String name, String position, String vs) {
	    super();
	    this.name = name;
	    this.position = position;
	    this.vs = vs;
	    this.version = version;
	  }
@Override
public String toString() {
	return "BindingInfo [name=" + name + ", position=" + position + ", dtName=" + dtName + ", vs=" + vs + ", version="
			+ version + "]";
}

}
