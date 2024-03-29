package gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.util;

import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ExportFont;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ExportFontConfig;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.NameAndPositionAndPresence;

import java.util.HashMap;
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
 * Created by Maxence Lefort on 10/5/16.
 */
public class ExportParameters {
    //Define parameters with a default value
    private boolean inlineConstraints = false;
    private boolean includeTOC = true;
    private String targetFormat = "html";
    private String documentTitle = "Implementation Guide";
    private String imageLogo;
    private List<NameAndPositionAndPresence> messageColumns;
    private List<NameAndPositionAndPresence> segmentsColumns;
    private List<NameAndPositionAndPresence> dataTypeColumns;
    private List<NameAndPositionAndPresence> valueSetColumns;
    private ExportFontConfig exportFontConfig;

    public ExportParameters(boolean inlineConstraints, boolean includeTOC, String targetFormat,
        String documentTitle) {
        this(inlineConstraints,includeTOC,targetFormat,documentTitle,null);
    }

    public ExportParameters(boolean inlineConstraints, boolean includeTOC, String targetFormat,
        String documentTitle,String imageLogo) {
        this(inlineConstraints,includeTOC,targetFormat,documentTitle,imageLogo,null,null,null,null,null);
    }

    public ExportParameters(boolean inlineConstraints, boolean includeTOC, String targetFormat,
        String documentTitle, String imageLogo, List<NameAndPositionAndPresence> messageColumns,
        List<NameAndPositionAndPresence> segmentsColumns,
        List<NameAndPositionAndPresence> dataTypeColumns,
        List<NameAndPositionAndPresence> valueSetColumns,
        ExportFontConfig exportFontConfig) {
        this.inlineConstraints = inlineConstraints;
        this.includeTOC = includeTOC;
        this.targetFormat = targetFormat;
        this.documentTitle = documentTitle;
        this.imageLogo = imageLogo;
        this.messageColumns = messageColumns;
        this.segmentsColumns = segmentsColumns;
        this.dataTypeColumns = dataTypeColumns;
        this.valueSetColumns = valueSetColumns;
        this.exportFontConfig = exportFontConfig;
    }

    public ExportParameters() {
    }

    public boolean isInlineConstraints() {
        return inlineConstraints;
    }

    public void setInlineConstraints(boolean inlineConstraints) {
        this.inlineConstraints = inlineConstraints;
    }

    public boolean isIncludeTOC() {
        return includeTOC;
    }

    public void setIncludeTOC(boolean includeTOC) {
        this.includeTOC = includeTOC;
    }

    public String getTargetFormat() {
        return targetFormat;
    }

    public void setTargetFormat(String targetFormat) {
        this.targetFormat = targetFormat;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public Map<String, String> toMap() {
        Map<String, String> params = new HashMap<>();
        params.put("includeTOC", String.valueOf(includeTOC));
        params.put("inlineConstraints", String.valueOf(inlineConstraints));
        params.put("targetFormat", targetFormat);
        params.put("documentTitle", documentTitle);
        if(imageLogo!=null) {
            params.put("imageLogo", imageLogo);
        }
        if(messageColumns!=null && !messageColumns.isEmpty()){
            String messageColumn = "messageColumn";
            for(NameAndPositionAndPresence currentColumn : messageColumns){
                params.put(messageColumn+currentColumn.getName().replace(" ",""),String.valueOf(currentColumn.isPresent()));
            }
        }
        if(dataTypeColumns!=null && !dataTypeColumns.isEmpty()){
            String dataTypeColumn = "dataTypeColumn";
            for(NameAndPositionAndPresence currentColumn : dataTypeColumns){
                params.put(dataTypeColumn+currentColumn.getName().replace(" ",""),String.valueOf(currentColumn.isPresent()));
            }
        }
        if(valueSetColumns!=null && !valueSetColumns.isEmpty()){
            String valueSetColumn = "valueSetColumn";
            for(NameAndPositionAndPresence currentColumn : valueSetColumns){
                params.put(valueSetColumn+currentColumn.getName().replace(" ",""),String.valueOf(currentColumn.isPresent()));
            }
        }
        if(segmentsColumns!=null && !segmentsColumns.isEmpty()){
            String segmentsColumn = "segmentColumn";
            for(NameAndPositionAndPresence currentColumn : segmentsColumns){
                params.put(segmentsColumn+currentColumn.getName().replace(" ",""),String.valueOf(currentColumn.isPresent()));
            }
        }
        if(exportFontConfig!=null) {
            params.put("userFontFamily", exportFontConfig.getExportFont().getValue());
            params.put("userFontSize", String.valueOf(exportFontConfig.getFontSize()));
        }
        return params;
    }

    public void setImageLogo(String imageLogo) {
        this.imageLogo = imageLogo;
    }

    public ExportFontConfig getExportFontConfig() {
        return exportFontConfig;
    }
}
