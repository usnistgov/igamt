/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */
package gov.nist.healthcare.tools.hl7.v2.igamt.lite.web.controller;

/**
 * @author jungyubw
 *
 */
public class ReqId {
  private String[] mids;
  private String[] cids;
  public String[] getMids() {
    return mids;
  }
  public void setMids(String[] mids) {
    this.mids = mids;
  }
  public String[] getCids() {
    return cids;
  }
  public void setCids(String[] cids) {
    this.cids = cids;
  }
  
  

}
