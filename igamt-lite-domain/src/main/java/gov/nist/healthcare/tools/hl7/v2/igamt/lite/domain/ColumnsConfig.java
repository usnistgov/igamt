/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified. Abdelghani EL OUAKILI (NIST) Feb 2, 2017
 */
package gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain;

/**
 * @author Abdelghani EL Ouakili (NIST)
 *
 */
public class ColumnsConfig {


  protected boolean usage;
  protected boolean comments;

  /**
   * @return the name
   */
  /**
   * @return the usage
   */
  public boolean getUsage() {
    return usage;
  }

  /**
   * @param usage the usage to set
   */
  public void setUsage(boolean usage) {
    this.usage = usage;
  }

  /**
   * @return the comments
   */
  public boolean getComments() {
    return comments;
  }

  /**
   * @param comments the comments to set
   */
  public void setComments(boolean comments) {
    this.comments = comments;
  }

}
