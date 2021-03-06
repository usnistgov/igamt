package gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.serialization;

import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.*;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.constraints.*;
import nu.xom.Attribute;
import nu.xom.Element;

import java.util.List;
import java.util.Map;

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
 * <p>
 * Created by Maxence Lefort on 12/9/16.
 */
public class SerializableSegment extends SerializableSection {

    private Segment segment;
    private String defPreText,defPostText,name,label,description,comment;
    private List<SerializableConstraint> constraints;
    private Map<Field,Datatype> fieldDatatypeMap;
    private Map<Field,List<Table>> fieldTableMap;
    private Map<CCValue,Table> coConstraintValueTableMap;
    private Boolean showConfLength;


    public SerializableSegment(String id, String prefix, String position, String headerLevel, String title,
        Segment segment, String name, String label, String description, String comment, String defPreText, String defPostText, List<SerializableConstraint> constraints, Map<Field,Datatype> fieldDatatypeMap,Map<Field,List<Table>> fieldTableMap, Map<CCValue,Table> coConstraintValueTableMap, Boolean showConfLength) {
        super(id, prefix, position, headerLevel, title);
        this.segment = segment;
        this.name = name;
        this.label = label;
        this.description = description;
        this.comment = comment;
        this.defPreText = defPreText;
        this.defPostText = defPostText;
        this.constraints = constraints;
        this.fieldDatatypeMap = fieldDatatypeMap;
        this.fieldTableMap = fieldTableMap;
        this.coConstraintValueTableMap = coConstraintValueTableMap;
        this.showConfLength = showConfLength;
    }



    @Override public Element serializeElement() {
        Element segmentElement = new Element("Segment");
        if (segment != null) {
            segmentElement.addAttribute(new Attribute("id", segment.getId()));
            segmentElement.addAttribute(new Attribute("Name", this.name));
            segmentElement.addAttribute(new Attribute("Label", this.label));
            segmentElement.addAttribute(new Attribute("Position", ""));
            segmentElement.addAttribute(new Attribute("Description", this.description));
            segmentElement.addAttribute(
                new Attribute("ShowConfLength", String.valueOf(showConfLength)));
            if (this.comment != null && !this.comment.isEmpty()) {
                segmentElement.addAttribute(new Attribute("Comment", this.comment));
            }

            if ((segment.getText1() != null && !segment.getText1().isEmpty()) || (
                segment.getText2() != null && !segment.getText2().isEmpty())) {
                if (this.defPreText != null && !this.defPreText.isEmpty()) {
                    segmentElement
                        .appendChild(this.createTextElement("DefPreText", this.defPreText));
                }
                if (this.defPostText != null && !this.defPostText.isEmpty()) {
                    segmentElement
                        .appendChild(this.createTextElement("DefPostText", this.defPostText));
                }
            }

            for (int i = 0; i < segment.getFields().size(); i++) {
                Field field = segment.getFields().get(i);
                Element fieldElement = new Element("Field");
                boolean isComplex = false;
                fieldElement.addAttribute(new Attribute("Name", field.getName()));
                fieldElement.addAttribute(new Attribute("Usage", getFullUsage(segment, i).toString()));
                Datatype datatype = fieldDatatypeMap.get(field);
                if (field.getDatatype()!=null&&datatype!=null) {
                    fieldElement.addAttribute(new Attribute("Datatype", datatype.getLabel()));
                        if (datatype.getComponents().size() > 0) {
                            isComplex = true;
                            fieldElement.addAttribute(new Attribute("ConfLength", ""));
                            fieldElement.addAttribute(new Attribute("MinLength", ""));
                            fieldElement.addAttribute(new Attribute("MaxLength", ""));
                        } else {
                            if(field.getConfLength()!=null && !"".equals(field.getConfLength())) {
                                fieldElement.addAttribute(
                                    new Attribute("ConfLength", field.getConfLength()));
                            }
                            fieldElement.addAttribute(
                                new Attribute("MinLength", String.valueOf(field.getMinLength())));
                            if (field.getMaxLength() != null && !field.getMaxLength().equals(""))
                                fieldElement
                                    .addAttribute(new Attribute("MaxLength", field.getMaxLength()));
                        }
                }
                fieldElement.addAttribute(new Attribute("complex",String.valueOf(isComplex)));
                fieldElement.addAttribute(new Attribute("Min", String.valueOf(field.getMin())));
                fieldElement.addAttribute(new Attribute("Max", field.getMax()));
                if (field.getTables() != null && !field.getTables().isEmpty()) {
                    List<Table> fieldTables = fieldTableMap.get(field);
                    String temp = "";
                    boolean isFirst = true;
                    if (fieldTables != null && fieldTables.size() > 0) {
                        for (Table table : fieldTables) {
                        	if(table != null){
                        		String bindingIdentifier = table.getBindingIdentifier();
                                if(!isFirst){
                                    temp += ",";
                                } else {
                                    isFirst = false;
                                }
                                if((bindingIdentifier != null && !bindingIdentifier.equals(""))){
                                    temp += bindingIdentifier;
                                } else {
                                    temp += " ! DEBUG: COULD NOT FIND binding identifier " + table
                                        .getBindingIdentifier();
                                }	
                        	}
                        }
                    }
                    fieldElement.addAttribute(new Attribute("Binding", temp));
                }
                if (field.getItemNo() != null && !field.getItemNo().equals(""))
                    fieldElement.addAttribute(new Attribute("ItemNo", field.getItemNo()));
                if (field.getComment() != null && !field.getComment().isEmpty())
                    fieldElement.addAttribute(new Attribute("Comment", field.getComment()));
                fieldElement.addAttribute(
                    new Attribute("Position", String.valueOf(field.getPosition())));

                if (field.getText() != null && !field.getText().isEmpty()) {
                    fieldElement.appendChild(this.createTextElement("Text", field.getText()));
                }
                segmentElement.appendChild(fieldElement);
            }

            if (!constraints.isEmpty()) {
                for (SerializableConstraint constraint : constraints) {
                    segmentElement.appendChild(constraint.serializeElement());
                }
            }

            CoConstraints coconstraints = segment.getCoConstraints();
            if (coconstraints.getConstraints().size() != 0) {
                //TODO refactor in a SerializableCoConstraint object and create the table in the XSLT
                Element coConstraintsElement = new Element("coconstraints");
                Element tableElement = new Element("table");
                tableElement.addAttribute(new Attribute("class", "contentTable"));

                Element thead = new Element("thead");
                thead.addAttribute(
                    new Attribute("class", "contentThead"));
                Element tr = new Element("tr");
                for (CoConstraintsColumn ccc : coconstraints.getColumnList()) {
                    Element th = new Element("th");
                    th.appendChild(segment.getName() + "-" + ccc.getField().getPosition());
                    tr.appendChild(th);
                }

                Element thd = new Element("th");
                thd.appendChild("Description");
                tr.appendChild(thd);

                Element thc = new Element("th");
                thc.appendChild("Comments");
                tr.appendChild(thc);
                thead.appendChild(tr);
                tableElement.appendChild(thead);

                Element tbody = new Element("tbody");
                for (CoConstraint coConstraint : coconstraints.getConstraints()) {

                    tr = new Element("tr");
                    for (CCValue coConstraintValue : coConstraint.getValues()) {
                        Element td = new Element("td");
                        if (coConstraintValue != null) {
                            if (coconstraints.getColumnList().get(coConstraint.getValues().indexOf(coConstraintValue))
                                .getConstraintType().equals("v")) {
                                td.appendChild(coConstraintValue.getValue());
                            } else {
                                if (coConstraintValue.getValue() != null && coConstraintValue.getValue().equals("")) {
                                    td.appendChild("N/A");
                                } else {
                                    Table table = coConstraintValueTableMap.get(coConstraintValue);
                                    if (table != null) {
                                        td.appendChild(table.getBindingIdentifier());
                                    } else {
                                        td.appendChild("");
                                    }
                                }
                            }
                        } else {
                            td.appendChild("");
                        }
                        tr.appendChild(td);
                    }
                    Element td = new Element("td");
                    td.appendChild(coConstraint.getDescription());
                    tr.appendChild(td);
                    td = new Element("td");
                    td.appendChild(coConstraint.getComments());
                    tr.appendChild(td);
                    tbody.appendChild(tr);
                }
                tableElement.appendChild(tbody);
                coConstraintsElement.appendChild(tableElement);
                segmentElement.appendChild(coConstraintsElement);
            }
        }

        return segmentElement;
    }

    private String getFullUsage(Segment segment, int i) {
        List<Predicate> predicates = super.findPredicate(i + 1, segment.getPredicates());
        if (predicates == null || predicates.isEmpty()) {
            return segment.getFields().get(i).getUsage().toString();
        } else {
            Predicate p = predicates.get(0);
            return segment.getFields().get(i).getUsage().toString() + "(" + p.getTrueUsage() + "/" + p.getFalseUsage() + ")";
        }
    }

    public List<SerializableConstraint> getConstraints() {
        return constraints;
    }

    public Segment getSegment() {
        return segment;
    }
}
