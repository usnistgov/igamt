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

package gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.serialization.xml;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;

import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.DTComponent;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.IGDocumentConfiguration;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.SegmentField;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.VariesMapItem;

public class XMLConfig {

  @Bean
  public IGDocumentConfiguration igDocumentConfig() {
    IGDocumentConfiguration config = new IGDocumentConfiguration();
    config.setStatuses(toSet(new String[] {"Draft", "Active", "Superceded", "Withdrawn"}));
    config.setDomainVersions(toSet(new String[] {"2.1", "2.2", "2.3", "2.3.1", "2.4", "2.5",
        "2.5.1", "2.6", "2.7", "2.7.1", "2.8", "2.8.1", "2.8.2"}));
    config.setSchemaVersions(toSet(new String[] {"1.0", "1.5", "2.0", "2.5"}));
    config.setUsages(toSet(new String[] {"R", "RE", "O", "C", "X"}));
    config.setConditionalUsage(toSet(new String[] {"R", "RE", "O", "X"}));
    config.setCodeUsages(toSet(new String[] {"R", "P", "E"}));
    config.setCodeSources(toSet(new String[] {"HL7", "Local", "Redefined", "SDO"}));
    config.setTableStabilities(toSet(new String[] {"Static", "Dynamic", "Undefined"}));
    config.setTableContentDefinitions(
        toSet(new String[] {"Extensional", "Intensional", "Undefined"}));
    config.setTableExtensibilities(toSet(new String[] {"Open", "Closed", "Undefined"}));
    config.setConstraintVerbs(toSet(new String[] {"SHALL be", "SHALL NOT be", "should be",
        "should not be", "may be", "may not be", "is", "is not"}));
    config.setConditionalConstraintVerbs(toSet(new String[] {"is", "is not"}));
    config.setConstraintTypes(toSet(new String[] {"valued", "a literal value", "one of list values",
        "one of codes in ValueSet", "formatted value", "identical to another node",
        "equal to another node", "not-equal to another node", "greater than another node",
        "equal to or greater than another node", "less than another node",
        "equal to or less than another node", "equal to", "not-equal to", "greater than",
        "equal to or greater than", "less than", "equal to or less than",
        "valued sequentially starting with the value '1'", "custom"}));
    config.setPredefinedFormats(toSet(new String[] {"Positive Integer", "ISO-compliant OID",
        "Alphanumeric", "Regular expression"}));

    // ID, IS
    // CE, CF, CWE, CNE, CSU
    // HD, AUI, CK, CN, CNN, CX, EI, ERL, ELD, PLN, PPN, XCN
    
    /*
     * 
     (Pre) need to find why we did exclude multiple valuesets for coded elements DT.
CE, CF, CWE, CNE, CSU could have multiple Value Sets
2.1 ID, IS, HD, ST, NM should have a single value set
For each element Multiple Value Sets bindings should have a single binding location.
XML should be :
bindingIdentifier='A:B' bindingLocation="1:4"
bindingIdentifier='A:B' bindingLocation="1"
bindingIdentifier='A:B' bindingLocation="4"
     */

    config.setValueSetAllowedDTs(toSet(new String[] {"ID", "IS", "CE", "CF", "CWE", "CNE", "CSU","HD"}));
    
    //"AUI", "CK", "CN", "CNN", "CX", "EI", "ERL", "ELD", "PLN", "PPN", "XCN"  ==> go to valueSetAllowedComponents
    config.setCodedElementDTs(toSet(new String[] {"CE", "CF", "CWE", "CNE", "CSU"}));
    config.setSingleValueSetDTs(toSet(new String[] {"ID", "IS", "ST", "NM", "HD"})); // ST and NM are
                                                                               // partial
    Set<DTComponent> valueSetAllowedComponents = new HashSet<DTComponent>();
    valueSetAllowedComponents.add(new DTComponent("AD", 3));
    valueSetAllowedComponents.add(new DTComponent("AD", 4));
    valueSetAllowedComponents.add(new DTComponent("AD", 5));
    valueSetAllowedComponents.add(new DTComponent("CNS", 7));
    valueSetAllowedComponents.add(new DTComponent("CSU", 2));
    valueSetAllowedComponents.add(new DTComponent("CSU", 5));
    valueSetAllowedComponents.add(new DTComponent("CSU", 11));
    valueSetAllowedComponents.add(new DTComponent("CSU", 14));
    valueSetAllowedComponents.add(new DTComponent("LA2", 11));
    valueSetAllowedComponents.add(new DTComponent("LA2", 12));
    valueSetAllowedComponents.add(new DTComponent("LA2", 13));
    valueSetAllowedComponents.add(new DTComponent("OSD", 2));
    valueSetAllowedComponents.add(new DTComponent("OSD", 4));
    valueSetAllowedComponents.add(new DTComponent("XAD", 3));
    valueSetAllowedComponents.add(new DTComponent("XAD", 4));
    valueSetAllowedComponents.add(new DTComponent("XAD", 8));
    valueSetAllowedComponents.add(new DTComponent("XAD", 5));
    valueSetAllowedComponents.add(new DTComponent("XON", 3));
    valueSetAllowedComponents.add(new DTComponent("XON", 10));
    valueSetAllowedComponents.add(new DTComponent("AUI", 1));
    valueSetAllowedComponents.add(new DTComponent("CK", 1));
    valueSetAllowedComponents.add(new DTComponent("CN", 1));
    valueSetAllowedComponents.add(new DTComponent("CNN", 1));
    valueSetAllowedComponents.add(new DTComponent("CX", 1));
    valueSetAllowedComponents.add(new DTComponent("EI", 1));
    valueSetAllowedComponents.add(new DTComponent("ERL", 1));
    valueSetAllowedComponents.add(new DTComponent("ELD", 1));
    valueSetAllowedComponents.add(new DTComponent("PLN", 1));
    valueSetAllowedComponents.add(new DTComponent("PPN", 1));
    valueSetAllowedComponents.add(new DTComponent("XCN", 1));    
    config.setValueSetAllowedComponents(valueSetAllowedComponents);

    Set<SegmentField> valueSetAllowedFields = new HashSet<SegmentField>();
    valueSetAllowedFields.add(new SegmentField("PID", 23));
    config.setValueSetAllowedFields(valueSetAllowedFields);
    
    HashMap<String, Set<String>> bindingLocationListByHL7Version =
        new HashMap<String, Set<String>>();
    // "2.1", "2.2", "2.3", "2.3.1", "2.4", "2.5", "2.5.1", "2.6", "2.7", "2.7.1", "2.8", "2.8.1",
    // "2.8.2"
    bindingLocationListByHL7Version.put("2.1", toSet(new String[] {"1", "4", "1 or 4"}));
    bindingLocationListByHL7Version.put("2.2", toSet(new String[] {"1", "4", "1 or 4"}));
    bindingLocationListByHL7Version.put("2.3", toSet(new String[] {"1", "4", "1 or 4"}));
    bindingLocationListByHL7Version.put("2.3.1", toSet(new String[] {"1", "4", "1 or 4"}));
    bindingLocationListByHL7Version.put("2.4", toSet(new String[] {"1", "4", "1 or 4"}));
    bindingLocationListByHL7Version.put("2.5", toSet(new String[] {"1", "4", "1 or 4"}));
    bindingLocationListByHL7Version.put("2.5.1", toSet(new String[] {"1", "4", "1 or 4"}));
    bindingLocationListByHL7Version.put("2.6", toSet(new String[] {"1", "4", "1 or 4"}));
    bindingLocationListByHL7Version.put("2.7",
        toSet(new String[] {"1", "4", "10", "1 or 4", "1 or 4 or 10"}));
    bindingLocationListByHL7Version.put("2.7.1",
        toSet(new String[] {"1", "4", "10", "1 or 4", "1 or 4 or 10"}));
    bindingLocationListByHL7Version.put("2.8",
        toSet(new String[] {"1", "4", "10", "1 or 4", "1 or 4 or 10"}));
    bindingLocationListByHL7Version.put("2.8.1",
        toSet(new String[] {"1", "4", "10", "1 or 4", "1 or 4 or 10"}));
    bindingLocationListByHL7Version.put("2.8.2",
        toSet(new String[] {"1", "4", "10", "1 or 4", "1 or 4 or 10"}));
    config.setBindingLocationListByHL7Version(bindingLocationListByHL7Version);

    Set<VariesMapItem> variesMapItems = new HashSet<VariesMapItem>();
    variesMapItems.add(new VariesMapItem("2.8.2", "OBX", "5", "2", "0125"));
    variesMapItems.add(new VariesMapItem("2.8.2", "MFA", "5", "6", "0355"));
    variesMapItems.add(new VariesMapItem("2.8.2", "MFE", "4", "5", "0355"));
    variesMapItems.add(new VariesMapItem("2.8.1", "OBX", "5", "2", "0125"));
    variesMapItems.add(new VariesMapItem("2.8.1", "MFA", "5", "6", "0355"));
    variesMapItems.add(new VariesMapItem("2.8.1", "MFE", "4", "5", "0355"));
    variesMapItems.add(new VariesMapItem("2.8", "OBX", "5", "2", "0125"));
    variesMapItems.add(new VariesMapItem("2.8", "MFA", "5", "6", "0355"));
    variesMapItems.add(new VariesMapItem("2.8", "MFE", "4", "5", "0355"));
    variesMapItems.add(new VariesMapItem("2.7.1", "OBX", "5", "2", "0125"));
    variesMapItems.add(new VariesMapItem("2.7.1", "MFA", "5", "6", "0355"));
    variesMapItems.add(new VariesMapItem("2.7.1", "MFE", "4", "5", "0355"));
    variesMapItems.add(new VariesMapItem("2.7", "OBX", "5", "2", "0125"));
    variesMapItems.add(new VariesMapItem("2.7", "MFA", "5", "6", "0355"));
    variesMapItems.add(new VariesMapItem("2.7", "MFE", "4", "5", "0355"));
    variesMapItems.add(new VariesMapItem("2.6", "OBX", "5", "2", "0125"));
    variesMapItems.add(new VariesMapItem("2.6", "MFA", "5", "6", "0355"));
    variesMapItems.add(new VariesMapItem("2.6", "MFE", "4", "5", "0355"));
    variesMapItems.add(new VariesMapItem("2.5.1", "OBX", "5", "2", "0125"));
    variesMapItems.add(new VariesMapItem("2.5.1", "MFA", "5", "6", "0355"));
    variesMapItems.add(new VariesMapItem("2.5.1", "MFE", "4", "5", "0355"));
    variesMapItems.add(new VariesMapItem("2.5", "OBX", "5", "2", "0125"));
    variesMapItems.add(new VariesMapItem("2.5", "MFA", "5", "6", "0355"));
    variesMapItems.add(new VariesMapItem("2.5", "MFE", "4", "5", "0355"));
    variesMapItems.add(new VariesMapItem("2.4", "OBX", "5", "2", "0125"));
    variesMapItems.add(new VariesMapItem("2.4", "MFA", "5", "6", "0355"));
    variesMapItems.add(new VariesMapItem("2.4", "MFE", "4", "5", "0355"));
    variesMapItems.add(new VariesMapItem("2.3.1", "OBX", "5", "2", "0125"));
    variesMapItems.add(new VariesMapItem("2.3.1", "MFA", "5", "6", "0355"));
    variesMapItems.add(new VariesMapItem("2.3.1", "MFE", "4", "5", "0355"));
    variesMapItems.add(new VariesMapItem("2.3", "OBX", "5", "2", "0125"));
    variesMapItems.add(new VariesMapItem("2.3", "MFE", "4", "5", "0355"));
    variesMapItems.add(new VariesMapItem("2.2", "OBX", "5", "2", "0125"));
    config.setVariesMapItems(variesMapItems);

    HashMap<Integer, String> dtmRUsageRegexCodes = new HashMap<Integer, String>();
    dtmRUsageRegexCodes.put(1,
        "([0-9]{4})(((0[1-9])|(1[0-2]))(((0[1-9])|([1-2][0-9])|(3[0-1]))((([0-1][0-9])|(2[0-3]))(([0-5][0-9])(([0-5][0-9])(\\.[0-9]{1,4})?)?)?)?)?)?((\\+|\\-)[0-9]{4})?");
    dtmRUsageRegexCodes.put(2,
        "([0-9]{4})((0[1-9])|(1[0-2]))(((0[1-9])|([1-2][0-9])|(3[0-1]))((([0-1][0-9])|(2[0-3]))(([0-5][0-9])(([0-5][0-9])(\\.[0-9]{1,4})?)?)?)?)?((\\+|\\-)[0-9]{4})?");
    dtmRUsageRegexCodes.put(3,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))((([0-1][0-9])|(2[0-3]))(([0-5][0-9])(([0-5][0-9])(\\.[0-9]{1,4})?)?)?)?((\\+|\\-)[0-9]{4})?");
    dtmRUsageRegexCodes.put(4,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))(([0-5][0-9])(([0-5][0-9])(\\.[0-9]{1,4})?)?)?((\\+|\\-)[0-9]{4})?");
    dtmRUsageRegexCodes.put(5,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))([0-5][0-9])(([0-5][0-9])(\\.[0-9]{1,4})?)?((\\+|\\-)[0-9]{4})?");
    dtmRUsageRegexCodes.put(6,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))([0-5][0-9])([0-5][0-9])(\\.[0-9]{1,4})?((\\+|\\-)[0-9]{4})?");
    dtmRUsageRegexCodes.put(7,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))([0-5][0-9])([0-5][0-9])\\.[0-9]([0-9]([0-9]([0-9])?)?)?((\\+|\\-)[0-9]{4})?");
    dtmRUsageRegexCodes.put(8,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))([0-5][0-9])([0-5][0-9])\\.[0-9][0-9]([0-9]([0-9])?)?((\\+|\\-)[0-9]{4})?");
    dtmRUsageRegexCodes.put(9,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))([0-5][0-9])([0-5][0-9])\\.[0-9][0-9][0-9]([0-9])?((\\+|\\-)[0-9]{4})?");
    dtmRUsageRegexCodes.put(10,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))([0-5][0-9])([0-5][0-9])\\.[0-9][0-9][0-9][0-9]((\\+|\\-)[0-9]{4})?");
    dtmRUsageRegexCodes.put(11,
        "(([0-9]{4})(((0[1-9])|(1[0-2]))(((0[1-9])|([1-2][0-9])|(3[0-1]))((([0-1][0-9])|(2[0-3]))(([0-5][0-9])(([0-5][0-9])(\\.[0-9]{1,4})?)?)?)?)?)?)?(\\+|\\-)[0-9]{4}");
    config.setDtmRUsageRegexCodes(dtmRUsageRegexCodes);

    HashMap<Integer, String> dtmXUsageRegexCodes = new HashMap<Integer, String>();
    dtmXUsageRegexCodes.put(1, "((\\+|\\-)[0-9]{4})?");
    dtmXUsageRegexCodes.put(2, "([0-9]{4})?((\\+|\\-)[0-9]{4})?");
    dtmXUsageRegexCodes.put(3, "([0-9]{4})?((0[1-9])|(1[0-2]))?((\\+|\\-)[0-9]{4})?");
    dtmXUsageRegexCodes.put(4,
        "([0-9]{4})?((0[1-9])|(1[0-2]))?((0[1-9])|([1-2][0-9])|(3[0-1]))?((\\+|\\-)[0-9]{4})?");
    dtmXUsageRegexCodes.put(5,
        "([0-9]{4})?((0[1-9])|(1[0-2]))?((0[1-9])|([1-2][0-9])|(3[0-1]))?(([0-1][0-9])|(2[0-3]))?((\\+|\\-)[0-9]{4})?");
    dtmXUsageRegexCodes.put(6,
        "([0-9]{4})?((0[1-9])|(1[0-2]))?((0[1-9])|([1-2][0-9])|(3[0-1]))?(([0-1][0-9])|(2[0-3]))?([0-5][0-9])?((\\+|\\-)[0-9]{4})?");
    dtmXUsageRegexCodes.put(7,
        "([0-9]{4})?((0[1-9])|(1[0-2]))?((0[1-9])|([1-2][0-9])|(3[0-1]))?(([0-1][0-9])|(2[0-3]))?([0-5][0-9])?([0-5][0-9])?((\\+|\\-)[0-9]{4})?");
    dtmXUsageRegexCodes.put(8,
        "([0-9]{4})?((0[1-9])|(1[0-2]))?((0[1-9])|([1-2][0-9])|(3[0-1]))?(([0-1][0-9])|(2[0-3]))?([0-5][0-9])?([0-5][0-9])?(\\.[0-9])?((\\+|\\-)[0-9]{4})?");
    dtmXUsageRegexCodes.put(9,
        "([0-9]{4})?((0[1-9])|(1[0-2]))?((0[1-9])|([1-2][0-9])|(3[0-1]))?(([0-1][0-9])|(2[0-3]))?([0-5][0-9])?([0-5][0-9])?(\\.[0-9])?([0-9])?((\\+|\\-)[0-9]{4})?");
    dtmXUsageRegexCodes.put(10,
        "([0-9]{4})?((0[1-9])|(1[0-2]))?((0[1-9])|([1-2][0-9])|(3[0-1]))?(([0-1][0-9])|(2[0-3]))?([0-5][0-9])?([0-5][0-9])?(\\.[0-9])?([0-9])?([0-9])?((\\+|\\-)[0-9]{4})?");
    dtmXUsageRegexCodes.put(11,
        "([0-9]{4})?((0[1-9])|(1[0-2]))?((0[1-9])|([1-2][0-9])|(3[0-1]))?(([0-1][0-9])|(2[0-3]))?([0-5][0-9])?([0-5][0-9])?(\\.[0-9])?([0-9])?([0-9])?([0-9])?");
    config.setDtmXUsageRegexCodes(dtmXUsageRegexCodes);

    HashMap<Integer, String> dtmCUsageIsValuedRegexCodes = new HashMap<Integer, String>();
    dtmCUsageIsValuedRegexCodes.put(1,
        "([0-9]{4})(((0[1-9])|(1[0-2]))(((0[1-9])|([1-2][0-9])|(3[0-1]))((([0-1][0-9])|(2[0-3]))(([0-5][0-9])(([0-5][0-9])(\\.[0-9]{1,4})?)?)?)?)?)?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsValuedRegexCodes.put(2,
        "([0-9]{4})((0[1-9])|(1[0-2]))(((0[1-9])|([1-2][0-9])|(3[0-1]))((([0-1][0-9])|(2[0-3]))(([0-5][0-9])(([0-5][0-9])(\\.[0-9]{1,4})?)?)?)?)?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsValuedRegexCodes.put(3,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))((([0-1][0-9])|(2[0-3]))(([0-5][0-9])(([0-5][0-9])(\\.[0-9]{1,4})?)?)?)?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsValuedRegexCodes.put(4,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))(([0-5][0-9])(([0-5][0-9])(\\.[0-9]{1,4})?)?)?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsValuedRegexCodes.put(5,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))([0-5][0-9])(([0-5][0-9])(\\.[0-9]{1,4})?)?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsValuedRegexCodes.put(6,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))([0-5][0-9])([0-5][0-9])(\\.[0-9]{1,4})?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsValuedRegexCodes.put(7,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))([0-5][0-9])([0-5][0-9])\\.[0-9]([0-9]([0-9]([0-9])?)?)?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsValuedRegexCodes.put(8,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))([0-5][0-9])([0-5][0-9])\\.[0-9][0-9]([0-9]([0-9])?)?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsValuedRegexCodes.put(9,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))([0-5][0-9])([0-5][0-9])\\.[0-9][0-9][0-9]([0-9])?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsValuedRegexCodes.put(10,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))([0-5][0-9])([0-5][0-9])\\.[0-9][0-9][0-9][0-9]((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsValuedRegexCodes.put(11,
        "(([0-9]{4})(((0[1-9])|(1[0-2]))(((0[1-9])|([1-2][0-9])|(3[0-1]))((([0-1][0-9])|(2[0-3]))(([0-5][0-9])(([0-5][0-9])(\\.[0-9]{1,4})?)?)?)?)?)?)?(\\+|\\-)[0-9]{4}");
    config.setDtmCUsageIsValuedRegexCodes(dtmCUsageIsValuedRegexCodes);

    HashMap<Integer, String> dtmCUsageIsNOTValuedRegexCodes = new HashMap<Integer, String>();
    dtmCUsageIsNOTValuedRegexCodes.put(1, "((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsNOTValuedRegexCodes.put(2, "([0-9]{4})?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsNOTValuedRegexCodes.put(3, "([0-9]{4})?((0[1-9])|(1[0-2]))?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsNOTValuedRegexCodes.put(4,
        "([0-9]{4})?((0[1-9])|(1[0-2]))?((0[1-9])|([1-2][0-9])|(3[0-1]))?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsNOTValuedRegexCodes.put(5,
        "([0-9]{4})?((0[1-9])|(1[0-2]))?((0[1-9])|([1-2][0-9])|(3[0-1]))?(([0-1][0-9])|(2[0-3]))?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsNOTValuedRegexCodes.put(6,
        "([0-9]{4})?((0[1-9])|(1[0-2]))?((0[1-9])|([1-2][0-9])|(3[0-1]))?(([0-1][0-9])|(2[0-3]))?([0-5][0-9])?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsNOTValuedRegexCodes.put(7,
        "([0-9]{4})?((0[1-9])|(1[0-2]))?((0[1-9])|([1-2][0-9])|(3[0-1]))?(([0-1][0-9])|(2[0-3]))?([0-5][0-9])?([0-5][0-9])?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsNOTValuedRegexCodes.put(8,
        "([0-9]{4})?((0[1-9])|(1[0-2]))?((0[1-9])|([1-2][0-9])|(3[0-1]))?(([0-1][0-9])|(2[0-3]))?([0-5][0-9])?([0-5][0-9])?(\\.[0-9])?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsNOTValuedRegexCodes.put(9,
        "([0-9]{4})?((0[1-9])|(1[0-2]))?((0[1-9])|([1-2][0-9])|(3[0-1]))?(([0-1][0-9])|(2[0-3]))?([0-5][0-9])?([0-5][0-9])?(\\.[0-9])?([0-9])?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsNOTValuedRegexCodes.put(10,
        "([0-9]{4})?((0[1-9])|(1[0-2]))?((0[1-9])|([1-2][0-9])|(3[0-1]))?(([0-1][0-9])|(2[0-3]))?([0-5][0-9])?([0-5][0-9])?(\\.[0-9])?([0-9])?([0-9])?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsNOTValuedRegexCodes.put(11,
        "([0-9]{4})?((0[1-9])|(1[0-2]))?((0[1-9])|([1-2][0-9])|(3[0-1]))?(([0-1][0-9])|(2[0-3]))?([0-5][0-9])?([0-5][0-9])?(\\.[0-9])?([0-9])?([0-9])?([0-9])?");
    config.setDtmCUsageIsNOTValuedRegexCodes(dtmCUsageIsNOTValuedRegexCodes);

    HashMap<Integer, String> dtmCUsageIsLiteralValueRegexCodes = new HashMap<Integer, String>();
    dtmCUsageIsLiteralValueRegexCodes.put(1,
        "%(((0[1-9])|(1[0-2]))(((0[1-9])|([1-2][0-9])|(3[0-1]))((([0-1][0-9])|(2[0-3]))(([0-5][0-9])(([0-5][0-9])(\\.[0-9]{1,4})?)?)?)?)?)?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsLiteralValueRegexCodes.put(2,
        "([0-9]{4})%(((0[1-9])|([1-2][0-9])|(3[0-1]))((([0-1][0-9])|(2[0-3]))(([0-5][0-9])(([0-5][0-9])(\\.[0-9]{1,4})?)?)?)?)?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsLiteralValueRegexCodes.put(3,
        "([0-9]{4})((0[1-9])|(1[0-2]))%((([0-1][0-9])|(2[0-3]))(([0-5][0-9])(([0-5][0-9])(\\.[0-9]{1,4})?)?)?)?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsLiteralValueRegexCodes.put(4,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))%(([0-5][0-9])(([0-5][0-9])(\\.[0-9]{1,4})?)?)?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsLiteralValueRegexCodes.put(5,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))%(([0-5][0-9])(\\.[0-9]{1,4})?)?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsLiteralValueRegexCodes.put(6,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))([0-5][0-9])%(\\.[0-9]{1,4})?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsLiteralValueRegexCodes.put(7,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))([0-5][0-9])([0-5][0-9])\\.%([0-9]([0-9]([0-9])?)?)?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsLiteralValueRegexCodes.put(8,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))([0-5][0-9])([0-5][0-9])\\.[0-9]%([0-9]([0-9])?)?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsLiteralValueRegexCodes.put(9,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))([0-5][0-9])([0-5][0-9])\\.[0-9][0-9]%([0-9])?((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsLiteralValueRegexCodes.put(10,
        "([0-9]{4})((0[1-9])|(1[0-2]))((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))([0-5][0-9])([0-5][0-9])\\.[0-9][0-9][0-9]%((\\+|\\-)[0-9]{4})?");
    dtmCUsageIsLiteralValueRegexCodes.put(11,
        "(([0-9]{4})(((0[1-9])|(1[0-2]))(((0[1-9])|([1-2][0-9])|(3[0-1]))((([0-1][0-9])|(2[0-3]))(([0-5][0-9])(([0-5][0-9])(\\.[0-9]{1,4})?)?)?)?)?)?)?\\%");
    config.setDtmCUsageIsLiteralValueRegexCodes(dtmCUsageIsLiteralValueRegexCodes);

    HashMap<String, String> dateTimeCSV = new HashMap<String, String>();
    
    dateTimeCSV.put("DTM", 
        "1,0,X,YYYY[MM[DD[HH[mm[SS[.S1[S2[S3[S4]]]]]]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH[MM[SS[.S[S[S[S]]]]]]]]]'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})$\n" + 
        "1,1,X,YYYY[MM[DD[HH[mm[SS[.S1[S2[S3]]]]]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH[MM[SS[.S[S[S]]]]]]]]'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})$\n" + 
        "1,2,X,YYYY[MM[DD[HH[mm[SS[.S1[S2]]]]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH[MM[SS[.S[S]]]]]]]'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2})$\n" + 
        "1,3,X,YYYY[MM[DD[HH[mm[SS[.S1]]]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH[MM[SS[.S]]]]]]'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d)$\n" + 
        "1,4,X,YYYY[MM[DD[HH[mm[SS]]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH[MM[SS]]]]]'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14})$\n" + 
        "1,5,X,YYYY[MM[DD[HH[mm]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH[MM]]]]'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10}|\\d{12})$\n" + 
        "1,6,X,YYYY[MM[DD[HH]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH]]]'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10})$\n" + 
        "1,7,X,YYYY[MM[DD]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD]]'.,^(\\d{4}|\\d{6}|\\d{8})$\n" + 
        "1,8,X,YYYY[MM],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM]'.,^(\\d{4}|\\d{6})$\n" + 
        "1,9,X,YYYY,Element 'xxx' SHALL follow the Date/Time pattern 'YYYY'.,^(\\d{4})$\n" + 
        "2,0,X,YYYYMM[DD[HH[mm[SS[.S1[S2[S3[S4]]]]]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH[MM[SS[.S[S[S[S]]]]]]]]'.,^(\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})$\n" + 
        "2,1,X,YYYYMM[DD[HH[mm[SS[.S1[S2[S3]]]]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH[MM[SS[.S[S[S]]]]]]]'.,^(\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})$\n" + 
        "2,2,X,YYYYMM[DD[HH[mm[SS[.S1[S2]]]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH[MM[SS[.S[S]]]]]]'.,^(\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2})$\n" + 
        "2,3,X,YYYYMM[DD[HH[mm[SS[.S1]]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH[MM[SS[.S]]]]]'.,^(\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d)$\n" + 
        "2,4,X,YYYYMM[DD[HH[mm[SS]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH[MM[SS]]]]'.,^(\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14})$\n" + 
        "2,5,X,YYYYMM[DD[HH[mm]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH[MM]]]'.,^(\\d{6}|\\d{8}|\\d{10}|\\d{12})$\n" + 
        "2,6,X,YYYYMM[DD[HH]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH]]'.,^(\\d{6}|\\d{8}|\\d{10})$\n" + 
        "2,7,X,YYYYMM[DD],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD]'.,^(\\d{6}|\\d{8})$\n" + 
        "2,8,X,YYYYMM,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM'.,^(\\d{6})$\n" + 
        "3,0,X,YYYYMMDD[HH[mm[SS[.S1[S2[S3[S4]]]]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH[MM[SS[.S[S[S[S]]]]]]]'.,^(\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})$\n" + 
        "3,1,X,YYYYMMDD[HH[mm[SS[.S1[S2[S3]]]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH[MM[SS[.S[S[S]]]]]]'.,^(\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})$\n" + 
        "3,2,X,YYYYMMDD[HH[mm[SS[.S1[S2]]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH[MM[SS[.S[S]]]]]'.,^(\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2})$\n" + 
        "3,3,X,YYYYMMDD[HH[mm[SS[.S1]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH[MM[SS[.S]]]]'.,^(\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d)$\n" + 
        "3,4,X,YYYYMMDD[HH[mm[SS]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH[MM[SS]]]'.,^(\\d{8}|\\d{10}|\\d{12}|\\d{14})$\n" + 
        "3,5,X,YYYYMMDD[HH[mm]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH[MM]]'.,^(\\d{8}|\\d{10}|\\d{12})$\n" + 
        "3,6,X,YYYYMMDD[HH],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH]'.,^(\\d{8}|\\d{10})$\n" + 
        "3,7,X,YYYYMMDD,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD'.,^(\\d{8})$\n" + 
        "4,0,X,YYYYMMDDHH[mm[SS[.S1[S2[S3[S4]]]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH[MM[SS[.S[S[S[S]]]]]]'.,^(\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})$\n" + 
        "4,1,X,YYYYMMDDHH[mm[SS[.S1[S2[S3]]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH[MM[SS[.S[S[S]]]]]'.,^(\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})$\n" + 
        "4,2,X,YYYYMMDDHH[mm[SS[.S1[S2]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH[MM[SS[.S[S]]]]'.,^(\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2})$\n" + 
        "4,3,X,YYYYMMDDHH[mm[SS[.S1]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH[MM[SS[.S]]]'.,^(\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d)$\n" + 
        "4,4,X,YYYYMMDDHH[mm[SS]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH[MM[SS]]'.,^(\\d{10}|\\d{12}|\\d{14})$\n" + 
        "4,5,X,YYYYMMDDHH[mm],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH[MM]'.,^(\\d{10}|\\d{12})$\n" + 
        "4,6,X,YYYYMMDDHH,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH'.,^(\\d{10})$\n" + 
        "5,0,X,YYYYMMDDHHmm[SS[.S1[S2[S3[S4]]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMM[SS[.S[S[S[S]]]]]'.,^(\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})$\n" + 
        "5,1,X,YYYYMMDDHHmm[SS[.S1[S2[S3]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMM[SS[.S[S[S]]]]'.,^(\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})$\n" + 
        "5,2,X,YYYYMMDDHHmm[SS[.S1[S2]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMM[SS[.S[S]]]'.,^(\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2})$\n" + 
        "5,3,X,YYYYMMDDHHmm[SS[.S1]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMM[SS[.S]]'.,^(\\d{12}|\\d{14}|\\d{14}\\.\\d)$\n" + 
        "5,4,X,YYYYMMDDHHmm[SS],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMM[SS]'.,^(\\d{12}|\\d{14})$\n" + 
        "5,5,X,YYYYMMDDHHmm,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMM'.,^(\\d{12})$\n" + 
        "6,0,X,YYYYMMDDHHmmSS[.S1[S2[S3[S4]]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS[.S[S[S[S]]]]'.,^(\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})$\n" + 
        "6,1,X,YYYYMMDDHHmmSS[.S1[S2[S3]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS[.S[S[S]]]'.,^(\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})$\n" + 
        "6,2,X,YYYYMMDDHHmmSS[.S1[S2]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS[.S[S]]'.,^(\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2})$\n" + 
        "6,3,X,YYYYMMDDHHmmSS[.S1],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS[.S]'.,^(\\d{14}|\\d{14}\\.\\d)$\n" + 
        "6,4,X,YYYYMMDDHHmmSS,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS'.,^(\\d{14})$\n" + 
        "7,0,X,YYYYMMDDHHmmSS.S1[S2[S3[S4]]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.S[S[S[S]]]'.,^(\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})$\n" + 
        "7,1,X,YYYYMMDDHHmmSS.S1[S2[S3]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.S[S[S]]'.,^(\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})$\n" + 
        "7,2,X,YYYYMMDDHHmmSS.S1[S2],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.S[S]'.,^(\\d{14}\\.\\d|\\d{14}\\.\\d{2})$\n" + 
        "7,3,X,YYYYMMDDHHmmSS.S1,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.S'.,^(\\d{14}\\.\\d)$\n" + 
        "8,0,X,YYYYMMDDHHmmSS.S1S2[S3[S4]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.SS[S[S]]'.,^(\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})$\n" + 
        "8,1,X,YYYYMMDDHHmmSS.S1S2[S3],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.SS[S]'.,^(\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})$\n" + 
        "8,2,X,YYYYMMDDHHmmSS.S1S2,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.SS'.,^(\\d{14}\\.\\d{2})$\n" + 
        "9,0,X,YYYYMMDDHHmmSS.S1S2S3[S4],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.SSS[S]'.,^(\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})$\n" + 
        "9,1,X,YYYYMMDDHHmmSS.S1S2S3,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.SSS'.,^(\\d{14}\\.\\d{3})$\n" + 
        "10,0,X,YYYYMMDDHHmmSS.S1S2S3S4,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.SSSS'.,^(\\d{14}\\.\\d{4})$\n" + 
        "1,0,REO,YYYY[MM[DD[HH[mm[SS[.S1[S2[S3[S4]]]]]]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH[MM[SS[.S[S[S[S]]]]]]]]][+/-ZZZZ]'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})([+-]\\d{4})?$\n" + 
        "1,1,REO,YYYY[MM[DD[HH[mm[SS[.S1[S2[S3]]]]]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH[MM[SS[.S[S[S]]]]]]]][+/-ZZZZ]'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})([+-]\\d{4})?$\n" + 
        "1,2,REO,YYYY[MM[DD[HH[mm[SS[.S1[S2]]]]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH[MM[SS[.S[S]]]]]]][+/-ZZZZ]'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2})([+-]\\d{4})?$\n" + 
        "1,3,REO,YYYY[MM[DD[HH[mm[SS[.S1]]]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH[MM[SS[.S]]]]]][+/-ZZZZ]'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d)([+-]\\d{4})?$\n" + 
        "1,4,REO,YYYY[MM[DD[HH[mm[SS]]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH[MM[SS]]]]][+/-ZZZZ]'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14})([+-]\\d{4})?$\n" + 
        "1,5,REO,YYYY[MM[DD[HH[mm]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH[MM]]]][+/-ZZZZ]'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10}|\\d{12})([+-]\\d{4})?$\n" + 
        "1,6,REO,YYYY[MM[DD[HH]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH]]][+/-ZZZZ]'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10})([+-]\\d{4})?$\n" + 
        "1,7,REO,YYYY[MM[DD]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD]][+/-ZZZZ]'.,^(\\d{4}|\\d{6}|\\d{8})([+-]\\d{4})?$\n" + 
        "1,8,REO,YYYY[MM][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM][+/-ZZZZ]'.,^(\\d{4}|\\d{6})([+-]\\d{4})?$\n" + 
        "1,9,REO,YYYY[+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[+/-ZZZZ]'.,^(\\d{4})([+-]\\d{4})?$\n" + 
        "2,0,REO,YYYYMM[DD[HH[mm[SS[.S1[S2[S3[S4]]]]]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH[MM[SS[.S[S[S[S]]]]]]]][+/-ZZZZ]'.,^(\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})([+-]\\d{4})?$\n" + 
        "2,1,REO,YYYYMM[DD[HH[mm[SS[.S1[S2[S3]]]]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH[MM[SS[.S[S[S]]]]]]][+/-ZZZZ]'.,^(\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})([+-]\\d{4})?$\n" + 
        "2,2,REO,YYYYMM[DD[HH[mm[SS[.S1[S2]]]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH[MM[SS[.S[S]]]]]][+/-ZZZZ]'.,^(\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2})([+-]\\d{4})?$\n" + 
        "2,3,REO,YYYYMM[DD[HH[mm[SS[.S1]]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH[MM[SS[.S]]]]][+/-ZZZZ]'.,^(\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d)([+-]\\d{4})?$\n" + 
        "2,4,REO,YYYYMM[DD[HH[mm[SS]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH[MM[SS]]]][+/-ZZZZ]'.,^(\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14})([+-]\\d{4})?$\n" + 
        "2,5,REO,YYYYMM[DD[HH[mm]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH[MM]]][+/-ZZZZ]'.,^(\\d{6}|\\d{8}|\\d{10}|\\d{12})([+-]\\d{4})?$\n" + 
        "2,6,REO,YYYYMM[DD[HH]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH]][+/-ZZZZ]'.,^(\\d{6}|\\d{8}|\\d{10})([+-]\\d{4})?$\n" + 
        "2,7,REO,YYYYMM[DD][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD][+/-ZZZZ]'.,^(\\d{6}|\\d{8})([+-]\\d{4})?$\n" + 
        "2,8,REO,YYYYMM[+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[+/-ZZZZ]'.,^(\\d{6})([+-]\\d{4})?$\n" + 
        "3,0,REO,YYYYMMDD[HH[mm[SS[.S1[S2[S3[S4]]]]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH[MM[SS[.S[S[S[S]]]]]]][+/-ZZZZ]'.,^(\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})([+-]\\d{4})?$\n" + 
        "3,1,REO,YYYYMMDD[HH[mm[SS[.S1[S2[S3]]]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH[MM[SS[.S[S[S]]]]]][+/-ZZZZ]'.,^(\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})([+-]\\d{4})?$\n" + 
        "3,2,REO,YYYYMMDD[HH[mm[SS[.S1[S2]]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH[MM[SS[.S[S]]]]][+/-ZZZZ]'.,^(\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2})([+-]\\d{4})?$\n" + 
        "3,3,REO,YYYYMMDD[HH[mm[SS[.S1]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH[MM[SS[.S]]]][+/-ZZZZ]'.,^(\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d)([+-]\\d{4})?$\n" + 
        "3,4,REO,YYYYMMDD[HH[mm[SS]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH[MM[SS]]][+/-ZZZZ]'.,^(\\d{8}|\\d{10}|\\d{12}|\\d{14})([+-]\\d{4})?$\n" + 
        "3,5,REO,YYYYMMDD[HH[mm]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH[MM]][+/-ZZZZ]'.,^(\\d{8}|\\d{10}|\\d{12})([+-]\\d{4})?$\n" + 
        "3,6,REO,YYYYMMDD[HH][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH][+/-ZZZZ]'.,^(\\d{8}|\\d{10})([+-]\\d{4})?$\n" + 
        "3,7,REO,YYYYMMDD[+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[+/-ZZZZ]'.,^(\\d{8})([+-]\\d{4})?$\n" + 
        "4,0,REO,YYYYMMDDHH[mm[SS[.S1[S2[S3[S4]]]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH[MM[SS[.S[S[S[S]]]]]][+/-ZZZZ]'.,^(\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})([+-]\\d{4})?$\n" + 
        "4,1,REO,YYYYMMDDHH[mm[SS[.S1[S2[S3]]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH[MM[SS[.S[S[S]]]]][+/-ZZZZ]'.,^(\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})([+-]\\d{4})?$\n" + 
        "4,2,REO,YYYYMMDDHH[mm[SS[.S1[S2]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH[MM[SS[.S[S]]]][+/-ZZZZ]'.,^(\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2})([+-]\\d{4})?$\n" + 
        "4,3,REO,YYYYMMDDHH[mm[SS[.S1]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH[MM[SS[.S]]][+/-ZZZZ]'.,^(\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d)([+-]\\d{4})?$\n" + 
        "4,4,REO,YYYYMMDDHH[mm[SS]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH[MM[SS]][+/-ZZZZ]'.,^(\\d{10}|\\d{12}|\\d{14})([+-]\\d{4})?$\n" + 
        "4,5,REO,YYYYMMDDHH[mm][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH[MM][+/-ZZZZ]'.,^(\\d{10}|\\d{12})([+-]\\d{4})?$\n" + 
        "4,6,REO,YYYYMMDDHH[+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH[+/-ZZZZ]'.,^(\\d{10})([+-]\\d{4})?$\n" + 
        "5,0,REO,YYYYMMDDHHmm[SS[.S1[S2[S3[S4]]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMM[SS[.S[S[S[S]]]]][+/-ZZZZ]'.,^(\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})([+-]\\d{4})?$\n" + 
        "5,1,REO,YYYYMMDDHHmm[SS[.S1[S2[S3]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMM[SS[.S[S[S]]]][+/-ZZZZ]'.,^(\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})([+-]\\d{4})?$\n" + 
        "5,2,REO,YYYYMMDDHHmm[SS[.S1[S2]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMM[SS[.S[S]]][+/-ZZZZ]'.,^(\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2})([+-]\\d{4})?$\n" + 
        "5,3,REO,YYYYMMDDHHmm[SS[.S1]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMM[SS[.S]][+/-ZZZZ]'.,^(\\d{12}|\\d{14}|\\d{14}\\.\\d)([+-]\\d{4})?$\n" + 
        "5,4,REO,YYYYMMDDHHmm[SS][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMM[SS][+/-ZZZZ]'.,^(\\d{12}|\\d{14})([+-]\\d{4})?$\n" + 
        "5,5,REO,YYYYMMDDHHmm[+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMM[+/-ZZZZ]'.,^(\\d{12})([+-]\\d{4})?$\n" + 
        "6,0,REO,YYYYMMDDHHmmSS[.S1[S2[S3[S4]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS[.S[S[S[S]]]][+/-ZZZZ]'.,^(\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})([+-]\\d{4})?$\n" + 
        "6,1,REO,YYYYMMDDHHmmSS[.S1[S2[S3]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS[.S[S[S]]][+/-ZZZZ]'.,^(\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})([+-]\\d{4})?$\n" + 
        "6,2,REO,YYYYMMDDHHmmSS[.S1[S2]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS[.S[S]][+/-ZZZZ]'.,^(\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2})([+-]\\d{4})?$\n" + 
        "6,3,REO,YYYYMMDDHHmmSS[.S1][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS[.S][+/-ZZZZ]'.,^(\\d{14}|\\d{14}\\.\\d)([+-]\\d{4})?$\n" + 
        "6,4,REO,YYYYMMDDHHmmSS[+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS[+/-ZZZZ]'.,^(\\d{14})([+-]\\d{4})?$\n" + 
        "7,0,REO,YYYYMMDDHHmmSS.S1[S2[S3[S4]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.S[S[S[S]]][+/-ZZZZ]'.,^(\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})([+-]\\d{4})?$\n" + 
        "7,1,REO,YYYYMMDDHHmmSS.S1[S2[S3]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.S[S[S]][+/-ZZZZ]'.,^(\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})([+-]\\d{4})?$\n" + 
        "7,2,REO,YYYYMMDDHHmmSS.S1[S2][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.S[S][+/-ZZZZ]'.,^(\\d{14}\\.\\d|\\d{14}\\.\\d{2})([+-]\\d{4})?$\n" + 
        "7,3,REO,YYYYMMDDHHmmSS.S1[+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.S[+/-ZZZZ]'.,^(\\d{14}\\.\\d)([+-]\\d{4})?$\n" + 
        "8,0,REO,YYYYMMDDHHmmSS.S1S2[S3[S4]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.SS[S[S]][+/-ZZZZ]'.,^(\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})([+-]\\d{4})?$\n" + 
        "8,1,REO,YYYYMMDDHHmmSS.S1S2[S3][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.SS[S][+/-ZZZZ]'.,^(\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})([+-]\\d{4})?$\n" + 
        "8,2,REO,YYYYMMDDHHmmSS.S1S2[+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.SS[+/-ZZZZ]'.,^(\\d{14}\\.\\d{2})([+-]\\d{4})?$\n" + 
        "9,0,REO,YYYYMMDDHHmmSS.S1S2S3[S4][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.SSS[S][+/-ZZZZ]'.,^(\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})([+-]\\d{4})?$\n" + 
        "9,1,REO,YYYYMMDDHHmmSS.S1S2S3[+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.SSS[+/-ZZZZ]'.,^(\\d{14}\\.\\d{3})([+-]\\d{4})?$\n" + 
        "10,0,REO,YYYYMMDDHHmmSS.S1S2S3S4[+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.SSSS[+/-ZZZZ]'.,^(\\d{14}\\.\\d{4})([+-]\\d{4})?$\n" + 
        "1,0,R,YYYY[MM[DD[HH[mm[SS[.S1[S2[S3[S4]]]]]]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH[MM[SS[.S[S[S[S]]]]]]]]]+/-ZZZZ'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})([+-]\\d{4})$\n" + 
        "1,1,R,YYYY[MM[DD[HH[mm[SS[.S1[S2[S3]]]]]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH[MM[SS[.S[S[S]]]]]]]]+/-ZZZZ'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})([+-]\\d{4})$\n" + 
        "1,2,R,YYYY[MM[DD[HH[mm[SS[.S1[S2]]]]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH[MM[SS[.S[S]]]]]]]+/-ZZZZ'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2})([+-]\\d{4})$\n" + 
        "1,3,R,YYYY[MM[DD[HH[mm[SS[.S1]]]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH[MM[SS[.S]]]]]]+/-ZZZZ'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d)([+-]\\d{4})$\n" + 
        "1,4,R,YYYY[MM[DD[HH[mm[SS]]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH[MM[SS]]]]]+/-ZZZZ'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14})([+-]\\d{4})$\n" + 
        "1,5,R,YYYY[MM[DD[HH[mm]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH[MM]]]]+/-ZZZZ'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10}|\\d{12})([+-]\\d{4})$\n" + 
        "1,6,R,YYYY[MM[DD[HH]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD[HH]]]+/-ZZZZ'.,^(\\d{4}|\\d{6}|\\d{8}|\\d{10})([+-]\\d{4})$\n" + 
        "1,7,R,YYYY[MM[DD]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD]]+/-ZZZZ'.,^(\\d{4}|\\d{6}|\\d{8})([+-]\\d{4})$\n" + 
        "1,8,R,YYYY[MM]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM]+/-ZZZZ'.,^(\\d{4}|\\d{6})([+-]\\d{4})$\n" + 
        "1,9,R,YYYY+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYY+/-ZZZZ'.,^(\\d{4})([+-]\\d{4})$\n" + 
        "2,0,R,YYYYMM[DD[HH[mm[SS[.S1[S2[S3[S4]]]]]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH[MM[SS[.S[S[S[S]]]]]]]]+/-ZZZZ'.,^(\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})([+-]\\d{4})$\n" + 
        "2,1,R,YYYYMM[DD[HH[mm[SS[.S1[S2[S3]]]]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH[MM[SS[.S[S[S]]]]]]]+/-ZZZZ'.,^(\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})([+-]\\d{4})$\n" + 
        "2,2,R,YYYYMM[DD[HH[mm[SS[.S1[S2]]]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH[MM[SS[.S[S]]]]]]+/-ZZZZ'.,^(\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2})([+-]\\d{4})$\n" + 
        "2,3,R,YYYYMM[DD[HH[mm[SS[.S1]]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH[MM[SS[.S]]]]]+/-ZZZZ'.,^(\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d)([+-]\\d{4})$\n" + 
        "2,4,R,YYYYMM[DD[HH[mm[SS]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH[MM[SS]]]]+/-ZZZZ'.,^(\\d{6}|\\d{8}|\\d{10}|\\d{12}|\\d{14})([+-]\\d{4})$\n" + 
        "2,5,R,YYYYMM[DD[HH[mm]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH[MM]]]+/-ZZZZ'.,^(\\d{6}|\\d{8}|\\d{10}|\\d{12})([+-]\\d{4})$\n" + 
        "2,6,R,YYYYMM[DD[HH]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD[HH]]+/-ZZZZ'.,^(\\d{6}|\\d{8}|\\d{10})([+-]\\d{4})$\n" + 
        "2,7,R,YYYYMM[DD]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD]+/-ZZZZ'.,^(\\d{6}|\\d{8})([+-]\\d{4})$\n" + 
        "2,8,R,YYYYMM+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM+/-ZZZZ'.,^(\\d{6})([+-]\\d{4})$\n" + 
        "3,0,R,YYYYMMDD[HH[mm[SS[.S1[S2[S3[S4]]]]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH[MM[SS[.S[S[S[S]]]]]]]+/-ZZZZ'.,^(\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})([+-]\\d{4})$\n" + 
        "3,1,R,YYYYMMDD[HH[mm[SS[.S1[S2[S3]]]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH[MM[SS[.S[S[S]]]]]]+/-ZZZZ'.,^(\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})([+-]\\d{4})$\n" + 
        "3,2,R,YYYYMMDD[HH[mm[SS[.S1[S2]]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH[MM[SS[.S[S]]]]]+/-ZZZZ'.,^(\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2})([+-]\\d{4})$\n" + 
        "3,3,R,YYYYMMDD[HH[mm[SS[.S1]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH[MM[SS[.S]]]]+/-ZZZZ'.,^(\\d{8}|\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d)([+-]\\d{4})$\n" + 
        "3,4,R,YYYYMMDD[HH[mm[SS]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH[MM[SS]]]+/-ZZZZ'.,^(\\d{8}|\\d{10}|\\d{12}|\\d{14})([+-]\\d{4})$\n" + 
        "3,5,R,YYYYMMDD[HH[mm]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH[MM]]+/-ZZZZ'.,^(\\d{8}|\\d{10}|\\d{12})([+-]\\d{4})$\n" + 
        "3,6,R,YYYYMMDD[HH]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD[HH]+/-ZZZZ'.,^(\\d{8}|\\d{10})([+-]\\d{4})$\n" + 
        "3,7,R,YYYYMMDD+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD+/-ZZZZ'.,^(\\d{8})([+-]\\d{4})$\n" + 
        "4,0,R,YYYYMMDDHH[mm[SS[.S1[S2[S3[S4]]]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH[MM[SS[.S[S[S[S]]]]]]+/-ZZZZ'.,^(\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})([+-]\\d{4})$\n" + 
        "4,1,R,YYYYMMDDHH[mm[SS[.S1[S2[S3]]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH[MM[SS[.S[S[S]]]]]+/-ZZZZ'.,^(\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})([+-]\\d{4})$\n" + 
        "4,2,R,YYYYMMDDHH[mm[SS[.S1[S2]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH[MM[SS[.S[S]]]]+/-ZZZZ'.,^(\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2})([+-]\\d{4})$\n" + 
        "4,3,R,YYYYMMDDHH[mm[SS[.S1]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH[MM[SS[.S]]]+/-ZZZZ'.,^(\\d{10}|\\d{12}|\\d{14}|\\d{14}\\.\\d)([+-]\\d{4})$\n" + 
        "4,4,R,YYYYMMDDHH[mm[SS]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH[MM[SS]]+/-ZZZZ'.,^(\\d{10}|\\d{12}|\\d{14})([+-]\\d{4})$\n" + 
        "4,5,R,YYYYMMDDHH[mm]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH[MM]+/-ZZZZ'.,^(\\d{10}|\\d{12})([+-]\\d{4})$\n" + 
        "4,6,R,YYYYMMDDHH+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHH+/-ZZZZ'.,^(\\d{10})([+-]\\d{4})$\n" + 
        "5,0,R,YYYYMMDDHHmm[SS[.S1[S2[S3[S4]]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMM[SS[.S[S[S[S]]]]]+/-ZZZZ'.,^(\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})([+-]\\d{4})$\n" + 
        "5,1,R,YYYYMMDDHHmm[SS[.S1[S2[S3]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMM[SS[.S[S[S]]]]+/-ZZZZ'.,^(\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})([+-]\\d{4})$\n" + 
        "5,2,R,YYYYMMDDHHmm[SS[.S1[S2]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMM[SS[.S[S]]]+/-ZZZZ'.,^(\\d{12}|\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2})([+-]\\d{4})$\n" + 
        "5,3,R,YYYYMMDDHHmm[SS[.S1]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMM[SS[.S]]+/-ZZZZ'.,^(\\d{12}|\\d{14}|\\d{14}\\.\\d)([+-]\\d{4})$\n" + 
        "5,4,R,YYYYMMDDHHmm[SS]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMM[SS]+/-ZZZZ'.,^(\\d{12}|\\d{14})([+-]\\d{4})$\n" + 
        "5,5,R,YYYYMMDDHHmm+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMM+/-ZZZZ'.,^(\\d{12})([+-]\\d{4})$\n" + 
        "6,0,R,YYYYMMDDHHmmSS[.S1[S2[S3[S4]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS[.S[S[S[S]]]]+/-ZZZZ'.,^(\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})([+-]\\d{4})$\n" + 
        "6,1,R,YYYYMMDDHHmmSS[.S1[S2[S3]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS[.S[S[S]]]+/-ZZZZ'.,^(\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})([+-]\\d{4})$\n" + 
        "6,2,R,YYYYMMDDHHmmSS[.S1[S2]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS[.S[S]]+/-ZZZZ'.,^(\\d{14}|\\d{14}\\.\\d|\\d{14}\\.\\d{2})([+-]\\d{4})$\n" + 
        "6,3,R,YYYYMMDDHHmmSS[.S1]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS[.S]+/-ZZZZ'.,^(\\d{14}|\\d{14}\\.\\d)([+-]\\d{4})$\n" + 
        "6,4,R,YYYYMMDDHHmmSS+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS+/-ZZZZ'.,^(\\d{14})([+-]\\d{4})$\n" + 
        "7,0,R,YYYYMMDDHHmmSS.S1[S2[S3[S4]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.S[S[S[S]]]+/-ZZZZ'.,^(\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})([+-]\\d{4})$\n" + 
        "7,1,R,YYYYMMDDHHmmSS.S1[S2[S3]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.S[S[S]]+/-ZZZZ'.,^(\\d{14}\\.\\d|\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})([+-]\\d{4})$\n" + 
        "7,2,R,YYYYMMDDHHmmSS.S1[S2]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.S[S]+/-ZZZZ'.,^(\\d{14}\\.\\d|\\d{14}\\.\\d{2})([+-]\\d{4})$\n" + 
        "7,3,R,YYYYMMDDHHmmSS.S1+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.S+/-ZZZZ'.,^(\\d{14}\\.\\d)([+-]\\d{4})$\n" + 
        "8,0,R,YYYYMMDDHHmmSS.S1S2[S3[S4]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.SS[S[S]]+/-ZZZZ'.,^(\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})([+-]\\d{4})$\n" + 
        "8,1,R,YYYYMMDDHHmmSS.S1S2[S3]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.SS[S]+/-ZZZZ'.,^(\\d{14}\\.\\d{2}|\\d{14}\\.\\d{3})([+-]\\d{4})$\n" + 
        "8,2,R,YYYYMMDDHHmmSS.S1S2+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.SS+/-ZZZZ'.,^(\\d{14}\\.\\d{2})([+-]\\d{4})$\n" + 
        "9,0,R,YYYYMMDDHHmmSS.S1S2S3[S4]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.SSS[S]+/-ZZZZ'.,^(\\d{14}\\.\\d{3}|\\d{14}\\.\\d{4})([+-]\\d{4})$\n" + 
        "9,1,R,YYYYMMDDHHmmSS.S1S2S3+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.SSS+/-ZZZZ'.,^(\\d{14}\\.\\d{3})([+-]\\d{4})$\n" + 
        "10,0,R,YYYYMMDDHHmmSS.S1S2S3S4+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDDHHMMSS.SSSS+/-ZZZZ'.,^(\\d{14}\\.\\d{4})([+-]\\d{4})$");
    
    dateTimeCSV.put("DT", 
        "1,0,X,YYYY[MM[DD]],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM[DD]]'.,^(\\d{4}|\\d{6}|\\d{8})$\n" + 
        "1,1,X,YYYY[MM],Element 'xxx' SHALL follow the Date/Time pattern 'YYYY[MM]'.,^(\\d{4}|\\d{6})$\n" + 
        "1,2,X,YYYY,Element 'xxx' SHALL follow the Date/Time pattern 'YYYY'.,^(\\d{4})$\n" + 
        "2,0,X,YYYYMM[DD],Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM[DD]'.,^(\\d{6}|\\d{8})$\n" + 
        "2,1,X,YYYYMM,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMM'.,^(\\d{6})$\n" + 
        "3,0,X,YYYYMMDD,Element 'xxx' SHALL follow the Date/Time pattern 'YYYYMMDD'.,^(\\d{8})$");
    
    dateTimeCSV.put("TM", 
        "1,0,X,HH[mm[SS[.S1[S2[S3[S4]]]]]],Element 'xxx' SHALL follow the Date/Time pattern 'HH[MM[SS[.S[S[S[S]]]]]]'.,^(\\d{2}|\\d{4}|\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3}|\\d{6}\\.\\d{4})$\n" + 
        "1,1,X,HH[mm[SS[.S1[S2[S3]]]]],Element 'xxx' SHALL follow the Date/Time pattern 'HH[MM[SS[.S[S[S]]]]]'.,^(\\d{2}|\\d{4}|\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3})$\n" + 
        "1,2,X,HH[mm[SS[.S1[S2]]]],Element 'xxx' SHALL follow the Date/Time pattern 'HH[MM[SS[.S[S]]]]'.,^(\\d{2}|\\d{4}|\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2})$\n" + 
        "1,3,X,HH[mm[SS[.S1]]],Element 'xxx' SHALL follow the Date/Time pattern 'HH[MM[SS[.S]]]'.,^(\\d{2}|\\d{4}|\\d{6}|\\d{6}\\.\\d)$\n" + 
        "1,4,X,HH[mm[SS]],Element 'xxx' SHALL follow the Date/Time pattern 'HH[MM[SS]]'.,^(\\d{2}|\\d{4}|\\d{6})$\n" + 
        "1,5,X,HH[mm],Element 'xxx' SHALL follow the Date/Time pattern 'HH[MM]'.,^(\\d{2}|\\d{4})$\n" + 
        "1,6,X,HH,Element 'xxx' SHALL follow the Date/Time pattern 'HH'.,^(\\d{2})$\n" + 
        "2,0,X,HHmm[SS[.S1[S2[S3[S4]]]]],Element 'xxx' SHALL follow the Date/Time pattern 'HHMM[SS[.S[S[S[S]]]]]'.,^(\\d{4}|\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3}|\\d{6}\\.\\d{4})$\n" + 
        "2,1,X,HHmm[SS[.S1[S2[S3]]]],Element 'xxx' SHALL follow the Date/Time pattern 'HHMM[SS[.S[S[S]]]]'.,^(\\d{4}|\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3})$\n" + 
        "2,2,X,HHmm[SS[.S1[S2]]],Element 'xxx' SHALL follow the Date/Time pattern 'HHMM[SS[.S[S]]]'.,^(\\d{4}|\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2})$\n" + 
        "2,3,X,HHmm[SS[.S1]],Element 'xxx' SHALL follow the Date/Time pattern 'HHMM[SS[.S]]'.,^(\\d{4}|\\d{6}|\\d{6}\\.\\d)$\n" + 
        "2,4,X,HHmm[SS],Element 'xxx' SHALL follow the Date/Time pattern 'HHMM[SS]'.,^(\\d{4}|\\d{6})$\n" + 
        "2,5,X,HHmm,Element 'xxx' SHALL follow the Date/Time pattern 'HHMM'.,^(\\d{4})$\n" + 
        "3,0,X,HHmmSS[.S1[S2[S3[S4]]]],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS[.S[S[S[S]]]]'.,^(\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3}|\\d{6}\\.\\d{4})$\n" + 
        "3,1,X,HHmmSS[.S1[S2[S3]]],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS[.S[S[S]]]'.,^(\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3})$\n" + 
        "3,2,X,HHmmSS[.S1[S2]],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS[.S[S]]'.,^(\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2})$\n" + 
        "3,3,X,HHmmSS[.S1],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS[.S]'.,^(\\d{6}|\\d{6}\\.\\d)$\n" + 
        "3,4,X,HHmmSS,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS'.,^(\\d{6})$\n" + 
        "4,0,X,HHmmSS.S1[S2[S3[S4]]],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.S[S[S[S]]]'.,^(\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3}|\\d{6}\\.\\d{4})$\n" + 
        "4,1,X,HHmmSS.S1[S2[S3]],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.S[S[S]]'.,^(\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3})$\n" + 
        "4,2,X,HHmmSS.S1[S2],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.S[S]'.,^(\\d{6}\\.\\d|\\d{6}\\.\\d{2})$\n" + 
        "4,3,X,HHmmSS.S1,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.S'.,^(\\d{6}\\.\\d)$\n" + 
        "5,0,X,HHmmSS.S1S2[S3[S4]],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.SS[S[S]]'.,^(\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3}|\\d{6}\\.\\d{4})$\n" + 
        "5,1,X,HHmmSS.S1S2[S3],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.SS[S]'.,^(\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3})$\n" + 
        "5,2,X,HHmmSS.S1S2,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.SS'.,^(\\d{6}\\.\\d{2})$\n" + 
        "6,0,X,HHmmSS.S1S2S3[S4],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.SSS[S]'.,^(\\d{6}\\.\\d{3}|\\d{6}\\.\\d{4})$\n" + 
        "6,1,X,HHmmSS.S1S2S3,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.SSS'.,^(\\d{6}\\.\\d{3})$\n" + 
        "7,0,X,HHmmSS.S1S2S3S4,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.SSSS'.,^(\\d{6}\\.\\d{4})$\n" + 
        "1,0,REO,HH[mm[SS[.S1[S2[S3[S4]]]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HH[MM[SS[.S[S[S[S]]]]]][+/-ZZZZ]'.,^(\\d{2}|\\d{4}|\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3}|\\d{6}\\.\\d{4})([+-]\\d{4})?$\n" + 
        "1,1,REO,HH[mm[SS[.S1[S2[S3]]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HH[MM[SS[.S[S[S]]]]][+/-ZZZZ]'.,^(\\d{2}|\\d{4}|\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3})([+-]\\d{4})?$\n" + 
        "1,2,REO,HH[mm[SS[.S1[S2]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HH[MM[SS[.S[S]]]][+/-ZZZZ]'.,^(\\d{2}|\\d{4}|\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2})([+-]\\d{4})?$\n" + 
        "1,3,REO,HH[mm[SS[.S1]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HH[MM[SS[.S]]][+/-ZZZZ]'.,^(\\d{2}|\\d{4}|\\d{6}|\\d{6}\\.\\d)([+-]\\d{4})?$\n" + 
        "1,4,REO,HH[mm[SS]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HH[MM[SS]][+/-ZZZZ]'.,^(\\d{2}|\\d{4}|\\d{6})([+-]\\d{4})?$\n" + 
        "1,5,REO,HH[mm][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HH[MM][+/-ZZZZ]'.,^(\\d{2}|\\d{4})([+-]\\d{4})?$\n" + 
        "1,6,REO,HH[+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HH[+/-ZZZZ]'.,^(\\d{2})([+-]\\d{4})?$\n" + 
        "2,0,REO,HHmm[SS[.S1[S2[S3[S4]]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMM[SS[.S[S[S[S]]]]][+/-ZZZZ]'.,^(\\d{4}|\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3}|\\d{6}\\.\\d{4})([+-]\\d{4})?$\n" + 
        "2,1,REO,HHmm[SS[.S1[S2[S3]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMM[SS[.S[S[S]]]][+/-ZZZZ]'.,^(\\d{4}|\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3})([+-]\\d{4})?$\n" + 
        "2,2,REO,HHmm[SS[.S1[S2]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMM[SS[.S[S]]][+/-ZZZZ]'.,^(\\d{4}|\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2})([+-]\\d{4})?$\n" + 
        "2,3,REO,HHmm[SS[.S1]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMM[SS[.S]][+/-ZZZZ]'.,^(\\d{4}|\\d{6}|\\d{6}\\.\\d)([+-]\\d{4})?$\n" + 
        "2,4,REO,HHmm[SS][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMM[SS][+/-ZZZZ]'.,^(\\d{4}|\\d{6})([+-]\\d{4})?$\n" + 
        "2,5,REO,HHmm[+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMM[+/-ZZZZ]'.,^(\\d{4})([+-]\\d{4})?$\n" + 
        "3,0,REO,HHmmSS[.S1[S2[S3[S4]]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS[.S[S[S[S]]]][+/-ZZZZ]'.,^(\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3}|\\d{6}\\.\\d{4})([+-]\\d{4})?$\n" + 
        "3,1,REO,HHmmSS[.S1[S2[S3]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS[.S[S[S]]][+/-ZZZZ]'.,^(\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3})([+-]\\d{4})?$\n" + 
        "3,2,REO,HHmmSS[.S1[S2]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS[.S[S]][+/-ZZZZ]'.,^(\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2})([+-]\\d{4})?$\n" + 
        "3,3,REO,HHmmSS[.S1][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS[.S][+/-ZZZZ]'.,^(\\d{6}|\\d{6}\\.\\d)([+-]\\d{4})?$\n" + 
        "3,4,REO,HHmmSS[+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS[+/-ZZZZ]'.,^(\\d{6})([+-]\\d{4})?$\n" + 
        "4,0,REO,HHmmSS.S1[S2[S3[S4]]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.S[S[S[S]]][+/-ZZZZ]'.,^(\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3}|\\d{6}\\.\\d{4})([+-]\\d{4})?$\n" + 
        "4,1,REO,HHmmSS.S1[S2[S3]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.S[S[S]][+/-ZZZZ]'.,^(\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3})([+-]\\d{4})?$\n" + 
        "4,2,REO,HHmmSS.S1[S2][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.S[S][+/-ZZZZ]'.,^(\\d{6}\\.\\d|\\d{6}\\.\\d{2})([+-]\\d{4})?$\n" + 
        "4,3,REO,HHmmSS.S1[+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.S[+/-ZZZZ]'.,^(\\d{6}\\.\\d)([+-]\\d{4})?$\n" + 
        "5,0,REO,HHmmSS.S1S2[S3[S4]][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.SS[S[S]][+/-ZZZZ]'.,^(\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3}|\\d{6}\\.\\d{4})([+-]\\d{4})?$\n" + 
        "5,1,REO,HHmmSS.S1S2[S3][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.SS[S][+/-ZZZZ]'.,^(\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3})([+-]\\d{4})?$\n" + 
        "5,2,REO,HHmmSS.S1S2[+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.SS[+/-ZZZZ]'.,^(\\d{6}\\.\\d{2})([+-]\\d{4})?$\n" + 
        "6,0,REO,HHmmSS.S1S2S3[S4][+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.SSS[S][+/-ZZZZ]'.,^(\\d{6}\\.\\d{3}|\\d{6}\\.\\d{4})([+-]\\d{4})?$\n" + 
        "6,1,REO,HHmmSS.S1S2S3[+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.SSS[+/-ZZZZ]'.,^(\\d{6}\\.\\d{3})([+-]\\d{4})?$\n" + 
        "7,0,REO,HHmmSS.S1S2S3S4[+/-ZZZZ],Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.SSSS[+/-ZZZZ]'.,^(\\d{6}\\.\\d{4})([+-]\\d{4})?$\n" + 
        "1,0,R,HH[mm[SS[.S1[S2[S3[S4]]]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HH[MM[SS[.S[S[S[S]]]]]]+/-ZZZZ'.,^(\\d{2}|\\d{4}|\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3}|\\d{6}\\.\\d{4})([+-]\\d{4})$\n" + 
        "1,1,R,HH[mm[SS[.S1[S2[S3]]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HH[MM[SS[.S[S[S]]]]]+/-ZZZZ'.,^(\\d{2}|\\d{4}|\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3})([+-]\\d{4})$\n" + 
        "1,2,R,HH[mm[SS[.S1[S2]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HH[MM[SS[.S[S]]]]+/-ZZZZ'.,^(\\d{2}|\\d{4}|\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2})([+-]\\d{4})$\n" + 
        "1,3,R,HH[mm[SS[.S1]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HH[MM[SS[.S]]]+/-ZZZZ'.,^(\\d{2}|\\d{4}|\\d{6}|\\d{6}\\.\\d)([+-]\\d{4})$\n" + 
        "1,4,R,HH[mm[SS]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HH[MM[SS]]+/-ZZZZ'.,^(\\d{2}|\\d{4}|\\d{6})([+-]\\d{4})$\n" + 
        "1,5,R,HH[mm]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HH[MM]+/-ZZZZ'.,^(\\d{2}|\\d{4})([+-]\\d{4})$\n" + 
        "1,6,R,HH+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HH+/-ZZZZ'.,^(\\d{2})([+-]\\d{4})$\n" + 
        "2,0,R,HHmm[SS[.S1[S2[S3[S4]]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMM[SS[.S[S[S[S]]]]]+/-ZZZZ'.,^(\\d{4}|\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3}|\\d{6}\\.\\d{4})([+-]\\d{4})$\n" + 
        "2,1,R,HHmm[SS[.S1[S2[S3]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMM[SS[.S[S[S]]]]+/-ZZZZ'.,^(\\d{4}|\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3})([+-]\\d{4})$\n" + 
        "2,2,R,HHmm[SS[.S1[S2]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMM[SS[.S[S]]]+/-ZZZZ'.,^(\\d{4}|\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2})([+-]\\d{4})$\n" + 
        "2,3,R,HHmm[SS[.S1]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMM[SS[.S]]+/-ZZZZ'.,^(\\d{4}|\\d{6}|\\d{6}\\.\\d)([+-]\\d{4})$\n" + 
        "2,4,R,HHmm[SS]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMM[SS]+/-ZZZZ'.,^(\\d{4}|\\d{6})([+-]\\d{4})$\n" + 
        "2,5,R,HHmm+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMM+/-ZZZZ'.,^(\\d{4})([+-]\\d{4})$\n" + 
        "3,0,R,HHmmSS[.S1[S2[S3[S4]]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS[.S[S[S[S]]]]+/-ZZZZ'.,^(\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3}|\\d{6}\\.\\d{4})([+-]\\d{4})$\n" + 
        "3,1,R,HHmmSS[.S1[S2[S3]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS[.S[S[S]]]+/-ZZZZ'.,^(\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3})([+-]\\d{4})$\n" + 
        "3,2,R,HHmmSS[.S1[S2]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS[.S[S]]+/-ZZZZ'.,^(\\d{6}|\\d{6}\\.\\d|\\d{6}\\.\\d{2})([+-]\\d{4})$\n" + 
        "3,3,R,HHmmSS[.S1]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS[.S]+/-ZZZZ'.,^(\\d{6}|\\d{6}\\.\\d)([+-]\\d{4})$\n" + 
        "3,4,R,HHmmSS+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS+/-ZZZZ'.,^(\\d{6})([+-]\\d{4})$\n" + 
        "4,0,R,HHmmSS.S1[S2[S3[S4]]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.S[S[S[S]]]+/-ZZZZ'.,^(\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3}|\\d{6}\\.\\d{4})([+-]\\d{4})$\n" + 
        "4,1,R,HHmmSS.S1[S2[S3]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.S[S[S]]+/-ZZZZ'.,^(\\d{6}\\.\\d|\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3})([+-]\\d{4})$\n" + 
        "4,2,R,HHmmSS.S1[S2]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.S[S]+/-ZZZZ'.,^(\\d{6}\\.\\d|\\d{6}\\.\\d{2})([+-]\\d{4})$\n" + 
        "4,3,R,HHmmSS.S1+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.S+/-ZZZZ'.,^(\\d{6}\\.\\d)([+-]\\d{4})$\n" + 
        "5,0,R,HHmmSS.S1S2[S3[S4]]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.SS[S[S]]+/-ZZZZ'.,^(\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3}|\\d{6}\\.\\d{4})([+-]\\d{4})$\n" + 
        "5,1,R,HHmmSS.S1S2[S3]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.SS[S]+/-ZZZZ'.,^(\\d{6}\\.\\d{2}|\\d{6}\\.\\d{3})([+-]\\d{4})$\n" + 
        "5,2,R,HHmmSS.S1S2+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.SS+/-ZZZZ'.,^(\\d{6}\\.\\d{2})([+-]\\d{4})$\n" + 
        "6,0,R,HHmmSS.S1S2S3[S4]+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.SSS[S]+/-ZZZZ'.,^(\\d{6}\\.\\d{3}|\\d{6}\\.\\d{4})([+-]\\d{4})$\n" + 
        "6,1,R,HHmmSS.S1S2S3+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.SSS+/-ZZZZ'.,^(\\d{6}\\.\\d{3})([+-]\\d{4})$\n" + 
        "7,0,R,HHmmSS.S1S2S3S4+/-ZZZZ,Element 'xxx' SHALL follow the Date/Time pattern 'HHMMSS.SSSS+/-ZZZZ'.,^(\\d{6}\\.\\d{4})([+-]\\d{4})$");
    
    
    config.setDateTimeCSV(dateTimeCSV);
    
    return config;
  }

  private Set<String> toSet(String[] values) {
    Set<String> res = new HashSet<String>();
    for (String v : values) {
      res.add(v);
    }
    return res;
  }
}
