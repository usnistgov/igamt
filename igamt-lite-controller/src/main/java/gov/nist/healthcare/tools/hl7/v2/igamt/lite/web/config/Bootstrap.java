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

package gov.nist.healthcare.tools.hl7.v2.igamt.lite.web.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Code;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.CodeUsageConfig;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ColumnsConfig;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Component;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Constant;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Constant.SCOPE;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Constant.STATUS;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Datatype;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.DatatypeLibrary;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.DatatypeLink;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.DatatypeMatrix;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ExportConfig;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ExportFont;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ExportFontConfig;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Field;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Group;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.IGDocument;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.IGDocumentScope;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Message;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Messages;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.NameAndPositionAndPresence;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Profile;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Section;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Segment;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.SegmentLibrary;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.SegmentLink;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.SegmentRef;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.SegmentRefOrGroup;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Table;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.TableLibrary;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.TableLink;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.UnchangedDataType;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Usage;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.UsageConfig;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.constraints.ConformanceStatement;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.constraints.Predicate;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.repo.DatatypeLibraryRepository;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.repo.DatatypeMatrixRepository;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.repo.ExportConfigRepository;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.repo.TableLibraryRepository;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.repo.UnchangedDataRepository;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.DatatypeService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.DeltaService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.ExportFontConfigService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.ExportFontService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.IGDocumentException;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.IGDocumentService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.MessageService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.ProfileComponentLibraryService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.ProfileService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.SegmentService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.TableService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.impl.ProfileSerializationImpl;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.util.DataCorrectionSectionPosition;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.util.DateUtils;

@Service
public class Bootstrap implements InitializingBean {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final HashMap<String, ArrayList<List<String>>> DatatypeMap =
      new HashMap<String, ArrayList<List<String>>>();
  private HashMap<String, Integer> Visited = new HashMap<String, Integer>();


  @Autowired
  ExportConfigRepository exportConfig;

  @Autowired
  ExportFontService exportFontService;

  @Autowired
  ExportFontConfigService exportFontConfigService;

  @Autowired
  ProfileService profileService;
  @Autowired
  DatatypeMatrixRepository matrix;
  @Autowired
  IGDocumentService documentService;
  @Autowired
  UnchangedDataRepository unchangedData;


  @Autowired
  MessageService messageService;

  @Autowired
  SegmentService segmentService;
  @Autowired
  DatatypeService datatypeService;

  @Autowired
  TableService tableService;
  @Autowired
  DataCorrectionSectionPosition dataCorrectionSectionPosition;
  @Autowired
  private ProfileComponentLibraryService profileComponentLibraryService;

  @Autowired
  private TableLibraryRepository tableLibraryRepository;

  @Autowired
  private DatatypeLibraryRepository daatypeLibraryRepository;

  @Autowired
  private DeltaService deltaService;



  /*
   * 
   */
  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   */
  @Override
  public void afterPropertiesSet() throws Exception {

    // Carefully use this. It will delete all of existing IGDocuments and
    // make new ones converted from the "igdocumentPreLibHL7",
    // "igdocumentPreLibPRELOADED" , and ""igdocumentPreLibUSER"
    // new IGDocumentConverterFromOldToNew().convert();
    // new DataCorrection().updateSegment();
    // new DataCorrection().updateDatatype();
    // new DataCorrection().updateSegmentLibrary();
    // new DataCorrection().updateDatatypeLibrary();
    // new DataCorrection().updateTableLibrary();
    // new DataCorrection().updateMessage();
    //
    // dataCorrectionSectionPosition.resetSectionPositions();
    // new DataCorrection().updateValueSetForSegment();
    // new DataCorrection().updateValueSetsForDT();

    // addVersionAndScopetoPRELOADEDIG();
    // addVersionAndScopetoHL7IG();
    /** to be runned one Time **/
    // CreateCollectionOfUnchanged();
    // AddVersionsToDatatypes();
    // addVersionAndScopetoUSERIG();
    // addScopeUserToOldClonedPRELOADEDIG();
    // changeTabletoTablesInNewHl7();
    // modifyCodeUsage();
    // modifyFieldUsage();
    // modifyComponentUsage();
    // [NOTE from Woo] I have checked all of Usage B/W in the message, but nothing. So we don't need
    // to write a code for the message.


    // ===============Data Type Library=====================================

    // CreateCollectionOfUnchanged(); // group datatype by sets of versions
    // setDtsStatus();// sets the status of all the datatypes to published or unpublished
    // setTablesStatus(); // sets the status of all the tables to published or unpublished
    // Colorate(); // genenerates the datatypes evolution matrix.

    // CreateIntermediateFromUnchanged();
    // MergeComponents();
    // setSegmentStatus();
    // // ====================================================================*/
    // // this.modifyConstraint();
    // // this.modifyMSH2Constraint();
    // // createNewSectionIds();
    //
    //
    // correctProfileComp();
    // fixConfLengths();
    // fixUserPublishedData();
    // fixConstraints1();

    // createDefaultConfiguration("IG Style");
    // createDefaultConfiguration("Profile Style");
    // createDefaultConfiguration("Table Style");
    // createDefaultExportFonts();

    // createDefaultConfiguration("Datatype Library");


    // changeStatusofPHINVADSTables();

    // modifyCodeUsage();
    // fixMissingData();

    // new Master Datatype Generation

    // CreateCollectionOfUnchanged(); // group datatype by sets of versions
    // Colorate(); // genenerates the datatypes evolution matrix.

    // CreateIntermediateFromUnchanged();
    // MergeComponents();
    // fixDatatypeRecursion();
    // fixDuplicateValueSets();
    createDefaultExportFonts();
  }


  private void fixDatatypeRecursion(IGDocument document) throws IGDocumentException {
    DatatypeLibrary datatypeLibrary = document.getProfile().getDatatypeLibrary();
    Datatype withdrawn = getWithdrawnDatatype(document.getProfile().getMetaData().getHl7Version());
    DatatypeLink withdrawnLink =
        new DatatypeLink(withdrawn.getId(), withdrawn.getName(), withdrawn.getExt());
    Set<String> datatypeIds = new HashSet<String>();
    for (DatatypeLink datatypeLink : datatypeLibrary.getChildren()) {
      datatypeIds.add(datatypeLink.getId());
    }
    List<Datatype> datatypes = datatypeService.findByIds(datatypeIds);
    for (Datatype datatype : datatypes) {
      if (datatype != null) {
        List<Component> components = datatype.getComponents();
        if (components != null && !components.isEmpty()) {
          for (Component component : components) {
            DatatypeLink componentDatatypeLink = component.getDatatype();
            if (componentDatatypeLink != null && componentDatatypeLink.getId() != null
                && componentDatatypeLink.getId().equals(datatype.getId())) {
              component.setDatatype(withdrawnLink);
              if (!contains(withdrawnLink, datatypeLibrary)) {
                datatypeLibrary.addDatatype(withdrawnLink);
              }
            }
          }
        }
      }
    }
    datatypeService.save(datatypes);
    daatypeLibraryRepository.save(datatypeLibrary);
  }



  private Datatype getWithdrawnDatatype(String hl7Version) throws IGDocumentException {
    List<Datatype> datatypes =
        datatypeService.findByNameAndVersionAndScope("-", hl7Version, SCOPE.HL7STANDARD.toString());
    Datatype dt = datatypes != null && !datatypes.isEmpty() ? datatypes.get(0) : null;
    if (dt == null) {
      dt = new Datatype();
      dt.setName("-");
      dt.setDescription("withdrawn");
      dt.setHl7versions(Arrays.asList(new String[] {hl7Version}));
      dt.setHl7Version(hl7Version);
      dt.setScope(SCOPE.HL7STANDARD);
      dt.setDateUpdated(DateUtils.getCurrentDate());
      dt.setStatus(STATUS.PUBLISHED);
      dt.setPrecisionOfDTM(3);
      datatypeService.save(dt);
    }
    return dt;
  }



  private void fixDatatypeRecursion() throws IGDocumentException {
    List<IGDocument> igDocuments = documentService.findAll();
    for (IGDocument document : igDocuments) {
      fixDatatypeRecursion(document);
    }
  }



  private void fixDuplicateValueSets() throws IGDocumentException {
    List<IGDocument> igDocuments = documentService.findAllByScope(IGDocumentScope.USER);
    for (IGDocument document : igDocuments) {
      fixDuplicateValueSets(document);
    }
    igDocuments = documentService.findAllByScope(IGDocumentScope.PRELOADED);
    for (IGDocument document : igDocuments) {
      fixDuplicateValueSets(document);
    }
  }

  private void fixDuplicateValueSets(IGDocument document) throws IGDocumentException {
    DatatypeLibrary datatypeLibrary = document.getProfile().getDatatypeLibrary();
    SegmentLibrary segmentLibrary = document.getProfile().getSegmentLibrary();
    TableLibrary tableLibrary = document.getProfile().getTableLibrary();
    List<TableLink> unusedDuplicates =
        collectUnusedDuplicates(tableLibrary, datatypeLibrary, segmentLibrary);
    if (!unusedDuplicates.isEmpty()) {
      for (TableLink tableLink : unusedDuplicates) {
        TableLink found = findTableLink(tableLibrary.getChildren(), tableLink);
        if (found != null && found.getId() != null) {
          tableLibrary.getChildren().remove(found);
        }
      }
    }
    tableLibraryRepository.save(tableLibrary);
  }


  private List<TableLink> collectUnusedDuplicates(TableLibrary tableLibrary,
      DatatypeLibrary datatypeLibrary, SegmentLibrary segmentLibrary) {
    List<TableLink> unusedDuplicates = new ArrayList<TableLink>();
    if (tableLibrary.getChildren() != null && !tableLibrary.getChildren().isEmpty()) {
      List<TableLink> tableLinks = new ArrayList<TableLink>();
      tableLinks.addAll(tableLibrary.getChildren());
      Set<String> segmentIds = new HashSet<String>();
      for (SegmentLink segmentLink : segmentLibrary.getChildren()) {
        segmentIds.add(segmentLink.getId());
      }
      List<Segment> segments = segmentService.findByIds(segmentIds);
      Set<String> datatypeIds = new HashSet<String>();
      for (DatatypeLink datatypeLink : datatypeLibrary.getChildren()) {
        datatypeIds.add(datatypeLink.getId());
      }
      List<Datatype> datatypes = datatypeService.findByIds(datatypeIds);
      for (int i = 0; i < tableLinks.size(); i++) {
        if (isTableDuplicated(tableLinks.get(i), tableLinks)
            && !isTableUsed(segments, datatypes, tableLinks.get(i))) {
          unusedDuplicates.add(tableLinks.get(i));
        }
      }
    }
    return unusedDuplicates;
  }

  private TableLink findTableLink(Set<TableLink> tableLinks, TableLink link) {
    if (tableLinks != null && !tableLinks.isEmpty() && link != null && link.getId() != null) {
      Iterator<TableLink> it = tableLinks.iterator();
      while (it.hasNext()) {
        TableLink tableLink = it.next();
        if (tableLink.getId() != null && tableLink.getId().equals(link.getId())) {
          return tableLink;
        }
      }
    }
    return null;
  }



  private boolean isTableDuplicated(TableLink tableLink, List<TableLink> tableLinks) {
    if (tableLink != null && tableLink.getId() != null && tableLinks != null
        && !tableLinks.isEmpty()) {
      Table table = tableService.findOneShortById(tableLink.getId());
      if (table != null) {
        for (int i = 0; i < tableLinks.size(); i++) {
          TableLink link = tableLinks.get(i);
          if (link != null && link.getBindingIdentifier() != null
              && link.getBindingIdentifier().equals(tableLink.getBindingIdentifier())
              && !tableLink.getId().equals(link.getId()) && sameScope(table, link)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private boolean sameScope(Table table, TableLink link) {
    Table table2 = tableService.findOneShortById(link.getId());
    return table2 != null && table.getScope().equals(table2.getScope());
  }

  private boolean isTableUsed(List<Segment> segments, List<Datatype> datatypes,
      TableLink tableLink) {
    for (Segment segment : segments) {
      if (isTableUsed(segment, tableLink)) {
        return true;
      }
    }
    for (Datatype datatype : datatypes) {
      if (isTableUsed(datatype, tableLink)) {
        return true;
      }
    }
    return false;
  }



  private boolean isTableUsed(Segment segment, TableLink tableLink) {
    for (Field field : segment.getFields()) {
      if (field.getTables() != null && !field.getTables().isEmpty()) {
        for (TableLink t : field.getTables()) {
          if (t.getId().equals(tableLink.getId())) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private boolean isTableUsed(Datatype datatype, TableLink tableLink) {
    for (Component component : datatype.getComponents()) {
      if (component.getTables() != null && !component.getTables().isEmpty()) {
        for (TableLink t : component.getTables()) {
          if (t.getId().equals(tableLink.getId())) {
            return true;
          }
        }
      }
    }
    return false;
  }



  private void fixMissingData() {
    List<IGDocument> igDocuments = documentService.findAll();
    for (IGDocument document : igDocuments) {
      fixMissingData(document);
    }
  }

  private void fixMissingData(IGDocument document) {
    DatatypeLibrary datatypeLibrary = document.getProfile().getDatatypeLibrary();
    SegmentLibrary segmentLibrary = document.getProfile().getSegmentLibrary();
    TableLibrary tableLibrary = document.getProfile().getTableLibrary();

    for (SegmentLink segLink : segmentLibrary.getChildren()) {
      if (segLink.getId() != null) {
        Segment segment = segmentService.findById(segLink.getId());
        for (Field field : segment.getFields()) {
          fixMissingData(field.getTables(), tableLibrary);
          fixMissingData(field.getDatatype(), tableLibrary, datatypeLibrary);
        }
      }
    }
    daatypeLibraryRepository.save(datatypeLibrary);
    tableLibraryRepository.save(tableLibrary);
  }

  private void fixMissingData(DatatypeLink datatypeLink, TableLibrary tableLibrary,
      DatatypeLibrary dtLib) {
    if (datatypeLink.getId() != null) {
      if (!contains(datatypeLink, dtLib)) {
        dtLib.addDatatype(datatypeLink);
      }
      Datatype datatype = datatypeService.findById(datatypeLink.getId());
      if (datatype.getComponents() != null && !datatype.getComponents().isEmpty()) {
        for (Component c1 : datatype.getComponents()) {
          if (c1.getDatatype() != null && !datatypeLink.getId().equals(c1.getDatatype().getId())) {
            fixMissingData(c1.getDatatype(), tableLibrary, dtLib);
          }
          fixMissingData(c1.getTables(), tableLibrary);
        }
      }
    }
  }

  private void fixMissingData(List<TableLink> tableLinks, TableLibrary tableLibrary) {
    if (tableLinks != null && !tableLinks.isEmpty()) {
      for (TableLink tableLink : tableLinks) {
        if (tableLink != null && tableLink.getId() != null && !contains(tableLink, tableLibrary)) {
          tableLibrary.addTable(tableLink);
        }
      }
    }
  }

  private boolean contains(TableLink link, TableLibrary tableLibrary) {
    if (tableLibrary.getChildren() != null) {
      for (TableLink tableLink : tableLibrary.getChildren()) {
        if (tableLink.getId() != null && tableLink.getId().equals(link.getId())) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean contains(DatatypeLink link, DatatypeLibrary datatypeLibrary) {
    if (datatypeLibrary.getChildren() != null) {
      for (DatatypeLink datatypeLink : datatypeLibrary.getChildren()) {
        if (datatypeLink.getId() != null && datatypeLink.getId().equals(link.getId())) {
          return true;
        }
      }
    }
    return false;
  }



  /**
   * 
   */
  private void createDefaultConfiguration(String type) {
    ExportConfig defaultConfiguration = new ExportConfig();
    defaultConfiguration.setDefaultType(true);
    defaultConfiguration.setAccountId(null);
    defaultConfiguration.setType(type);
    // Default Usages
    UsageConfig displayAll = new UsageConfig();
    UsageConfig displaySelectives = new UsageConfig();
    displaySelectives.setC(true);
    displaySelectives.setX(false);
    displaySelectives.setO(false);
    displaySelectives.setR(true);
    displaySelectives.setRe(true);
    CodeUsageConfig codeUsageExport = new CodeUsageConfig();
    codeUsageExport.setE(false);
    codeUsageExport.setP(true);
    codeUsageExport.setR(true);

    displayAll.setC(true);
    displayAll.setRe(true);
    displayAll.setX(true);
    displayAll.setO(true);
    displayAll.setR(true);

    defaultConfiguration.setSegmentORGroupsExport(displayAll);

    defaultConfiguration.setComponentExport(displayAll);

    defaultConfiguration.setFieldsExport(displayAll);

    defaultConfiguration.setCodesExport(codeUsageExport);

    defaultConfiguration.setDatatypesExport(displaySelectives);
    defaultConfiguration.setSegmentsExport(displaySelectives);
    defaultConfiguration.setValueSetsExport(displaySelectives);

    // Default column
    ArrayList<NameAndPositionAndPresence> messageColumnsDefaultList =
        new ArrayList<NameAndPositionAndPresence>();

    messageColumnsDefaultList.add(new NameAndPositionAndPresence("Segment", 1, true, true));
    messageColumnsDefaultList.add(new NameAndPositionAndPresence("Flavor", 2, true, true));
    messageColumnsDefaultList.add(new NameAndPositionAndPresence("Element Name", 3, true, true));
    messageColumnsDefaultList.add(new NameAndPositionAndPresence("Cardinality", 4, true, false));
    messageColumnsDefaultList.add(new NameAndPositionAndPresence("Usage", 5, true, false));
    messageColumnsDefaultList.add(new NameAndPositionAndPresence("Comment", 1, true, false));

    ArrayList<NameAndPositionAndPresence> segmentColumnsDefaultList =
        new ArrayList<NameAndPositionAndPresence>();
    segmentColumnsDefaultList.add(new NameAndPositionAndPresence("Name", 1, true, true));
    segmentColumnsDefaultList
        .add(new NameAndPositionAndPresence("Conformance Length", 2, false, false));
    segmentColumnsDefaultList.add(new NameAndPositionAndPresence("Data Type", 3, true, false));
    segmentColumnsDefaultList.add(new NameAndPositionAndPresence("Usage", 4, true, false));
    segmentColumnsDefaultList.add(new NameAndPositionAndPresence("Length", 5, false, false));
    segmentColumnsDefaultList.add(new NameAndPositionAndPresence("Value Set", 6, true, false));
    segmentColumnsDefaultList.add(new NameAndPositionAndPresence("Comment", 7, true, false));



    ArrayList<NameAndPositionAndPresence> dataTypeColumnsDefaultList =
        new ArrayList<NameAndPositionAndPresence>();

    dataTypeColumnsDefaultList.add(new NameAndPositionAndPresence("Name", 1, true, true));
    dataTypeColumnsDefaultList
        .add(new NameAndPositionAndPresence("Conformance Length", 2, false, false));
    dataTypeColumnsDefaultList.add(new NameAndPositionAndPresence("Data Type", 3, true, false));
    dataTypeColumnsDefaultList.add(new NameAndPositionAndPresence("Usage", 4, true, false));
    dataTypeColumnsDefaultList.add(new NameAndPositionAndPresence("Length", 5, false, false));
    dataTypeColumnsDefaultList.add(new NameAndPositionAndPresence("Value Set", 6, true, false));
    dataTypeColumnsDefaultList.add(new NameAndPositionAndPresence("Comment", 7, true, false));



    defaultConfiguration.setDatatypeColumn(new ColumnsConfig(dataTypeColumnsDefaultList));
    defaultConfiguration.setSegmentColumn(new ColumnsConfig(segmentColumnsDefaultList));
    defaultConfiguration.setMessageColumn(new ColumnsConfig(messageColumnsDefaultList));

    ArrayList<NameAndPositionAndPresence> valueSetsDefaultList =
        new ArrayList<NameAndPositionAndPresence>();

    valueSetsDefaultList.add(new NameAndPositionAndPresence("Value", 1, true, true));
    valueSetsDefaultList.add(new NameAndPositionAndPresence("Code System", 2, true, true));
    valueSetsDefaultList.add(new NameAndPositionAndPresence("Usage", 3, false, false));
    valueSetsDefaultList.add(new NameAndPositionAndPresence("Description", 4, false, true));

    defaultConfiguration.setValueSetColumn(new ColumnsConfig(valueSetsDefaultList));

    exportConfig.save(defaultConfiguration);



  }

  private void createDefaultExportFonts() throws Exception {
    ExportFont exportFont =
        new ExportFont("'Arial Narrow',sans-serif", "'Arial Narrow',sans-serif;");
    exportFontService.save(exportFont);
    ExportFontConfig defaultExportFontConfig = exportFontConfigService.getDefaultExportFontConfig();
    if (defaultExportFontConfig != null) {
      exportFontConfigService.delete(defaultExportFontConfig);
    }
    defaultExportFontConfig = new ExportFontConfig(exportFont, 10, true);
    exportFontConfigService.save(defaultExportFontConfig);
    exportFont = new ExportFont("\"Palatino Linotype\", \"Book Antiqua\", Palatino, serif",
        "\"Palatino Linotype\", \"Book Antiqua\", Palatino, serif;");
    exportFontService.save(exportFont);
    exportFont =
        new ExportFont("\"Times New Roman\", Times, serif", "\"Times New Roman\", Times, serif;");
    exportFontService.save(exportFont);
    exportFont = new ExportFont("Georgia, serif", "Georgia, serif;");
    exportFontService.save(exportFont);
    exportFont = new ExportFont("\"Comic Sans MS\", cursive, sans-serif",
        "\"Comic Sans MS\", cursive, sans-serif;");
    exportFontService.save(exportFont);
    exportFont = new ExportFont("\"Lucida Sans Unicode\", \"Lucida Grande\", sans-serif",
        "\"Lucida Sans Unicode\", \"Lucida Grande\", sans-serif;");
    exportFontService.save(exportFont);
    exportFont = new ExportFont("Tahoma, Geneva, sans-serif", "Tahoma, Geneva, sans-serif;");
    exportFontService.save(exportFont);
    exportFont = new ExportFont("\"Trebuchet MS\", Helvetica, sans-serif",
        "\"Trebuchet MS\", Helvetica, sans-serif;");
    exportFontService.save(exportFont);
    exportFont = new ExportFont("Verdana, Geneva, sans-serif", "Verdana, Geneva, sans-serif;");
    exportFontService.save(exportFont);
    exportFont = new ExportFont("\"Courier New\", Courier, monospace",
        "\"Courier New\", Courier, monospace;");
    exportFontService.save(exportFont);
    exportFont = new ExportFont("\"Lucida Console\", Monaco, monospace",
        "\"Lucida Console\", Monaco, monospace;");
    exportFontService.save(exportFont);
  }

  private void changeStatusofPHINVADSTables() {
    List<Table> allTables = tableService.findAll();

    for (Table t : allTables) {
      if (null != t && null != t.getScope()) {
        if (t.getScope().equals(SCOPE.PHINVADS) && STATUS.UNPUBLISHED.equals(t.getStatus())) {
          tableService.updateStatus(t.getId(), STATUS.PUBLISHED);
        }
      }
    }
  }

  private void fixMissingCodes(String sourceTableLibId, String targetTableLibId) {
    TableLibrary sourceLib = tableLibraryRepository.findById(sourceTableLibId);
    TableLibrary tagertLib = tableLibraryRepository.findById(targetTableLibId);

    for (TableLink targetLink : tagertLib.getChildren()) {
      Table targetTable = tableService.findById(targetLink.getId());
      if (targetTable.getScope().equals(SCOPE.USER)
          && (targetTable.getCodes() == null || targetTable.getCodes().isEmpty())) {
        TableLink sourceLink = findTableLink(sourceLib, targetTable.getBindingIdentifier());
        if (sourceLink != null) {
          Table sourceTable = tableService.findById(sourceLink.getId());
          if (sourceTable != null) {
            targetTable.setCodes(sourceTable.getCodes());
            tableService.save(targetTable);
          }
        }
      }
    }
  }


  private TableLink findTableLink(TableLibrary library, String bindingIdentifier) {
    for (TableLink targetLink : library.getChildren()) {
      if (targetLink.getBindingIdentifier() != null
          && targetLink.getBindingIdentifier().equals(bindingIdentifier)) {
        return targetLink;
      }
    }
    return null;
  }

  private void fixConstraints1() {
    List<Datatype> allDts = datatypeService.findAll();
    for (Datatype d : allDts) {
      fixConstraint1(d.getConformanceStatements(), d.getPredicates());
      datatypeService.save(d);
    }
    List<Segment> segments = segmentService.findAll();
    for (Segment s : segments) {
      fixConstraint1(s.getConformanceStatements(), s.getPredicates());
      segmentService.save(s);
    }
  }

  private void fixConstraint1(List<ConformanceStatement> cs, List<Predicate> ps) {
    if (cs != null) {
      for (ConformanceStatement c : cs) {
        if (c.getAssertion() != null && c.getAssertion().startsWith("<Assertion><IFTHEN>")) {
          c.setAssertion(c.getAssertion().replaceAll(Pattern.quote("IFTHEN>"), "IMPLY>"));
        }
      }
    }
    if (ps != null) {
      for (Predicate p : ps) {
        if (p.getAssertion() != null && p.getAssertion().startsWith("<Assertion><IFTHEN>")) {
          p.setAssertion(p.getAssertion().replaceAll(Pattern.quote("IFTHEN>"), "IMPLY>"));
        }
      }
    }
  }

  private void fixUserPublishedData() {
    List<Table> allTables = tableService.findAll();
    for (Table t : allTables) {
      if (null != t && null != t.getScope()) {
        if (t.getScope().equals(SCOPE.USER) && STATUS.PUBLISHED.equals(t.getStatus())) {
          tableService.updateStatus(t.getId(), STATUS.UNPUBLISHED);
        }
      }
    }

    List<Datatype> allDts = datatypeService.findAll();
    for (Datatype d : allDts) {
      if (null != d && null != d.getScope()) {
        if (d.getScope().equals(SCOPE.USER) && STATUS.PUBLISHED.equals(d.getStatus())) {
          datatypeService.updateStatus(d.getId(), STATUS.UNPUBLISHED);
        }
      }
    }

    List<Segment> allsegs = segmentService.findAll();
    for (Segment s : allsegs) {
      if (null != s && null != s.getScope()) {
        if (s.getScope().equals(SCOPE.USER) && STATUS.PUBLISHED.equals(s.getStatus())) {
          segmentService.updateStatus(s.getId(), STATUS.UNPUBLISHED);
        }
      }
    }

    List<Message> allMessages = messageService.findAll();
    for (Message s : allMessages) {
      if (null != s && null != s.getScope()) {
        if (s.getScope().equals(SCOPE.USER) && STATUS.PUBLISHED.equals(s.getStatus())) {
          segmentService.updateStatus(s.getId(), STATUS.UNPUBLISHED);
        } else if (s.getScope().equals(SCOPE.HL7STANDARD)
            && STATUS.UNPUBLISHED.equals(s.getStatus())) {
          segmentService.updateStatus(s.getId(), STATUS.PUBLISHED);
        }
      }
    }
  }

  private void fixConfLengths() {
    List<Segment> segments = segmentService.findAll();
    for (Segment s : segments) {
      List<Field> fields = s.getFields();
      for (Field f : fields) {
        if ("-1".equals(f.getConfLength())) {
          f.setConfLength("");
        }
      }
    }
    segmentService.save(segments);

    List<Datatype> datatypes = datatypeService.findAll();
    for (Datatype s : datatypes) {
      List<Component> components = s.getComponents();
      for (Component f : components) {
        if ("-1".equals(f.getConfLength())) {
          f.setConfLength("");
        }
      }
    }
    datatypeService.save(datatypes);
  }


  private void modifyCodeUsage() {
    List<Table> allTables = tableService.findAll();

    for (Table t : allTables) {
      boolean isChanged = false;
      for (Code c : t.getCodes()) {
        if (c.getCodeUsage() == null) {
          c.setCodeUsage("P");
          isChanged = true;
        } else if (!c.getCodeUsage().equals("R") && !c.getCodeUsage().equals("P")
            && !c.getCodeUsage().equals("E")) {
          c.setCodeUsage("P");
          isChanged = true;
        }
      }
      if (isChanged) {
        tableService.save(t);
        logger.info("Table " + t.getId() + " has been updated by the codeusage issue.");
      }
    }
  }

  private void setTablesStatus() {
    List<Table> allTables = tableService.findAll();
    for (Table t : allTables) {
      if (null != t && null != t.getScope()) {
        if (t.getScope().equals(SCOPE.HL7STANDARD) || t.getScope().equals(SCOPE.PRELOADED)) {
          tableService.updateStatus(t.getId(), STATUS.PUBLISHED);
        } else if (!STATUS.PUBLISHED.equals(t.getStatus())) {
          tableService.updateStatus(t.getId(), STATUS.UNPUBLISHED);
        }
      }
    }
  }

  private void setDtsStatus() {
    List<Datatype> allDts = datatypeService.findAll();
    for (Datatype d : allDts) {
      if (null != d && null != d.getScope()) {
        if (d.getScope().equals(SCOPE.HL7STANDARD) || d.getScope().equals(SCOPE.PRELOADED)) {
          datatypeService.updateStatus(d.getId(), STATUS.PUBLISHED);
        } else if (!STATUS.PUBLISHED.equals(d.getStatus())) {
          datatypeService.updateStatus(d.getId(), STATUS.UNPUBLISHED);
        }
      }
    }
  }

  private void setSegmentStatus() {
    List<Segment> allsegs = segmentService.findAll();
    for (Segment s : allsegs) {
      if (null != s && null != s.getScope()) {
        if (s.getScope().equals(SCOPE.HL7STANDARD) || s.getScope().equals(SCOPE.PRELOADED)) {
          segmentService.updateStatus(s.getId(), STATUS.PUBLISHED);
        } else if (!STATUS.PUBLISHED.equals(s.getStatus())) {
          segmentService.updateStatus(s.getId(), STATUS.UNPUBLISHED);
        }
      }
    }
  }

  private void modifyFieldUsage() {
    List<Segment> allSegments = segmentService.findAll();

    for (Segment s : allSegments) {
      boolean isChanged = false;
      for (Field f : s.getFields()) {
        if (f.getUsage().equals(Usage.B) || f.getUsage().equals(Usage.W)) {
          f.setUsage(Usage.X);
          isChanged = true;
        }
      }
      if (isChanged) {
        segmentService.save(s);
        logger.info("Segment " + s.getId() + " has been updated by the usage W/B issue.");
      }
    }
  }

  private void createNewSectionIds() throws IGDocumentException {
    List<IGDocument> igs = documentService.findAll();
    for (IGDocument ig : igs) {
      if (ig.getChildSections() != null && !ig.getChildSections().isEmpty()) {
        for (Section s : ig.getChildSections()) {
        }
      }
      documentService.save(ig);
    }

    setUpdatedDates(); // Run only once.

  }



  // correctProfileComp(); }

  private void correctProfileComp() throws IGDocumentException {


    List<IGDocument> igDocuments = documentService.findAll();
    for (IGDocument igd : igDocuments) {
      Messages msgs = igd.getProfile().getMessages();
      if (igd.getProfile().getProfileComponentLibrary().getId() == null) {

        profileComponentLibraryService.save(igd.getProfile().getProfileComponentLibrary());
      }

    }
    documentService.save(igDocuments);
  }


  @SuppressWarnings("deprecation")
  private void setUpdatedDates() throws IGDocumentException {

    List<Datatype> datatypes = datatypeService.findAll();
    boolean changed = false;
    if (datatypes != null) {
      for (Datatype d : datatypes) {
        if (d.getScope() != null && !d.getScope().equals(SCOPE.HL7STANDARD)
            && d.getDate() != null) {
          try {
            Date dateUpdated = parseDate(d.getDate());
            datatypeService.updateDate(d.getId(), dateUpdated);
          } catch (ParseException e) {
            logger.info(
                "Failed to parse date of datatype with id=" + d.getId() + ", Date=" + d.getDate());
          }
        }
      }
    }

    changed = false;
    List<Segment> segments = segmentService.findAll();
    if (segments != null) {
      for (Segment d : segments) {
        if (d.getScope() != null && !d.getScope().equals(SCOPE.HL7STANDARD)
            && d.getDate() != null) {
          Date dateUpdated = null;
          try {
            dateUpdated = parseDate(d.getDate());
            segmentService.updateDate(d.getId(), dateUpdated);
          } catch (ParseException e) {
            logger.info(
                "Failed to parse date of segment with id=" + d.getId() + ", Date=" + d.getDate());
          }
        }
      }
    }

    changed = false;
    List<Table> tables = tableService.findAll();
    if (tables != null) {
      for (Table d : tables) {
        if (d.getScope() != null && !d.getScope().equals(SCOPE.HL7STANDARD)
            && d.getDate() != null) {
          Date dateUpdated = null;
          try {
            dateUpdated = parseDate(d.getDate());
            tableService.updateDate(d.getId(), dateUpdated);
          } catch (ParseException e) {
            logger.info(
                "Failed to parse date of table with id=" + d.getId() + ", Date=" + d.getDate());
          }
        }
      }
    }


    changed = false;
    List<Message> messages = messageService.findAll();
    if (messages != null) {
      for (Message d : messages) {
        if (d.getScope() != null && !d.getScope().equals(SCOPE.HL7STANDARD)
            && d.getDate() != null) {
          Date dateUpdated = null;
          try {
            dateUpdated = parseDate(d.getDate());
            messageService.updateDate(d.getId(), dateUpdated);
          } catch (ParseException e) {
            logger.info(
                "Failed to parse date of message with id=" + d.getId() + ", Date=" + d.getDate());
          }
        }
      }

    }

    changed = false;
    List<IGDocument> documents = documentService.findAll();
    if (documents != null) {
      for (IGDocument d : documents) {
        if (d.getScope() != null && !d.getScope().equals(SCOPE.HL7STANDARD)
            && d.getMetaData().getDate() != null) {
          Date dateUpdated = null;
          try {
            dateUpdated = parseDate(d.getMetaData().getDate());
            documentService.updateDate(d.getId(), dateUpdated);
          } catch (ParseException e) {
            logger.info("Failed to parse date of table with id=" + d.getId() + ", Date="
                + d.getMetaData().getDate());
          }
        }
      }
    }

  }

  private Date parseDate(String dateString) throws ParseException {
    Date dateUpdated = parseDate(dateString, DateUtils.FORMAT);

    if (dateUpdated == null) {
      dateUpdated = parseDate(dateString, "EEE MMM d HH:mm:ss zzz yyyy");
    }

    if (dateUpdated == null) {
      dateUpdated = parseDate(dateString, Constant.mdy.toPattern());
    }

    if (dateUpdated != null)
      return dateUpdated;

    throw new ParseException("", 1);
  }


  private Date parseDate(String dateString, String format) {
    Date dateUpdated = null;
    try {
      dateUpdated = new SimpleDateFormat(format).parse(dateString);
    } catch (ParseException e) {
      dateUpdated = null;
    }
    return dateUpdated;
  }



  //
  // private void modifyCodeUsage() {
  // List<Table> allTables = tableService.findAll();
  //
  // for (Table t : allTables) {
  // boolean isChanged = false;
  // for (Code c : t.getCodes()) {
  // if (c.getCodeUsage() == null) {
  // c.setCodeUsage("P");
  // isChanged = true;
  // } else if (!c.getCodeUsage().equals("R") && !c.getCodeUsage().equals("P")
  // && !c.getCodeUsage().equals("E")) {
  // c.setCodeUsage("P");
  // isChanged = true;
  // }
  // }
  // if (isChanged) {
  // tableService.save(t);
  // logger.info("Table " + t.getId() + " has been updated by the codeusage issue.");
  // }
  // }
  // }
  //
  // private void modifyFieldUsage() {
  // List<Segment> allSegments = segmentService.findAll();
  //
  // for (Segment s : allSegments) {
  // boolean isChanged = false;
  // for (Field f : s.getFields()) {
  // if (f.getUsage().equals(Usage.B) || f.getUsage().equals(Usage.W)) {
  // f.setUsage(Usage.X);
  // isChanged = true;
  // }
  // }
  // if (isChanged) {
  // segmentService.save(s);
  // logger.info("Segment " + s.getId() + " has been updated by the usage W/B issue.");
  // }
  // }
  // }
  // private void createNewSectionIds() throws IGDocumentException{
  // List<IGDocument> igs=documentService.findAll();
  // for(IGDocument ig: igs){
  // if(ig.getChildSections()!=null&& !ig.getChildSections().isEmpty()){
  // for(Section s : ig.getChildSections()){
  // ChangeIdInside(s);
  // }
  // }
  // documentService.save(ig);
  // }
  // }
  //
  // private void ChangeIdInside(Section s) {
  // s.setId(ObjectId.get().toString());
  // if(s.getChildSections()!=null&& !s.getChildSections().isEmpty()){
  // for(Section sub : s.getChildSections()){
  //
  // ChangeIdInside(sub);
  // }
  // }
  // }
  //
  // private void modifyMSH2Constraint(){
  // List<Segment> allSegments = segmentService.findAll();
  //
  // for (Segment s : allSegments) {
  // if(s.getName().equals("MSH")){
  // boolean isChanged = false;
  // for(ConformanceStatement cs: s.getConformanceStatements()){
  // if(cs.getConstraintTarget().equals("2[1]")){
  // cs.setDescription("The value of MSH.2 (Encoding Characters) SHALL be '^~\\&'.");
  // cs.setAssertion("<Assertion><PlainText IgnoreCase=\"false\" Path=\"2[1]\"
  // Text=\"^~\\&amp;\"/></Assertion>");
  // isChanged = true;
  // }
  // }
  //
  // if (isChanged) {
  // segmentService.save(s);
  // logger.info("Segment " + s.getId() + " has been updated by CS issue");
  // }
  // }
  // }
  //
  // }
  //
  // private void modifyConstraint(){
  // List<Segment> allSegments = segmentService.findAll();
  //
  // for (Segment s : allSegments) {
  // if(!s.getName().equals("MSH")){
  // boolean isChanged = false;
  // ConformanceStatement wrongCS = null;
  // for(ConformanceStatement cs: s.getConformanceStatements()){
  // if(cs.getConstraintTarget().equals("2[1]")){
  // if(cs.getDescription().startsWith("The value of MSH.2")){
  // wrongCS = cs;
  // isChanged = true;
  // }
  // }
  // }
  //
  // if (isChanged) {
  // s.getConformanceStatements().remove(wrongCS);
  // segmentService.save(s);
  // logger.info("Segment " + s.getId() + " has been updated by CS issue");
  // }
  // }
  // }
  //
  // }
  //
  private void modifyComponentUsage() {
    List<Datatype> allDatatypes = datatypeService.findAll();

    for (Datatype d : allDatatypes) {
      boolean isChanged = false;
      for (Component c : d.getComponents()) {
        if (c.getUsage().equals(Usage.B) || c.getUsage().equals(Usage.W)) {
          c.setUsage(Usage.X);
          isChanged = true;
        }
      }
      if (isChanged) {
        datatypeService.save(d);
        logger.info("Datatype " + d.getId() + " has been updated by the usage W/B issue.");
      }
    }
  }

  // private void changeTabletoTablesInNewHl7() {
  // List<String> hl7Versions = new ArrayList<String>();
  // hl7Versions.add("2.7.1");
  // hl7Versions.add("2.8");
  // hl7Versions.add("2.8.1");
  // hl7Versions.add("2.8.2");
  // List<IGDocument> igDocuments =
  // documentService.findByScopeAndVersionsInIg(IGDocumentScope.HL7STANDARD, hl7Versions);
  // for (IGDocument igd : igDocuments) {
  // Set<String> usedSegsId = new HashSet<String>();
  // SegmentLibrary segmentLib = igd.getProfile().getSegmentLibrary();
  // for (SegmentLink segLink : segmentLib.getChildren()) {
  // usedSegsId.add(segLink.getId());
  // }
  // List<Segment> usedSegs = segmentService.findByIds(usedSegsId);
  // for (Segment usedSeg : usedSegs) {
  // for (Field fld : usedSeg.getFields()) {
  // if (fld.getTable() != null) {
  // fld.getTables().add(fld.getTable());
  // System.out.println("Field Table Added=" + fld.getTable());
  // }
  // }
  // }
  // segmentService.save(usedSegs);
  // Set<String> usedDtsId = new HashSet<String>();
  // DatatypeLibrary datatypeLib = igd.getProfile().getDatatypeLibrary();
  // for (DatatypeLink dtLink : datatypeLib.getChildren()) {
  // usedSegsId.add(dtLink.getId());
  // }
  // List<Datatype> usedDts = datatypeService.findByIds(usedDtsId);
  // for (Datatype usedDt : usedDts) {
  // for (Component comp : usedDt.getComponents()) {
  // if (comp.getTable() != null) {
  // comp.getTables().add(comp.getTable());
  // System.out.println("Component Table Added=" + comp.getTable());
  // }
  // }
  // }
  // datatypeService.save(usedDts);
  // }
  //
  // }

  private void loadPreloadedIGDocuments() throws Exception {
    IGDocument d = new IGDocument();

    String p = IOUtils.toString(this.getClass().getResourceAsStream("/profiles/IZ_Profile.xml"));
    String v =
        IOUtils.toString(this.getClass().getResourceAsStream("/profiles/IZ_ValueSetLibrary.xml"));
    String c =
        IOUtils.toString(this.getClass().getResourceAsStream("/profiles/IZ_Constraints.xml"));
    Profile profile = new ProfileSerializationImpl().deserializeXMLToProfile(p, v, c);

    profile.setScope(IGDocumentScope.PRELOADED);

    d.addProfile(profile);

    boolean existPreloadedDocument = false;

    String documentID = d.getMetaData().getIdentifier();
    String documentVersion = d.getMetaData().getVersion();

    List<IGDocument> igDocuments = documentService.findAll();

    for (IGDocument igd : igDocuments) {
      if (igd.getScope().equals(IGDocumentScope.PRELOADED)
          && documentID.equals(igd.getMetaData().getIdentifier())
          && documentVersion.equals(igd.getMetaData().getVersion())) {
        existPreloadedDocument = true;
      }
    }
    if (!existPreloadedDocument)
      documentService.save(d);
  }

  private void initMAp() {
    List<SCOPE> scopes = new ArrayList<SCOPE>();
    scopes.add(SCOPE.HL7STANDARD);
    List<Datatype> dataInit = datatypeService.findByScopesAndVersion(scopes, "2.3.1");
    for (Datatype dt : dataInit) {
      ArrayList<List<String>> temp = new ArrayList<List<String>>();
      List<String> version1 = new ArrayList<String>();
      version1.add("2.3.1");
      temp.add(version1);
      DatatypeMap.put(dt.getName(), temp);

    }

  }

  private void AddVersiontoMap(String version) {
    Visited = new HashMap<String, Integer>();
    List<SCOPE> scopes = new ArrayList<SCOPE>();
    scopes.add(SCOPE.HL7STANDARD);
    List<Datatype> datatypesToAdd = datatypeService.findByScopesAndVersion(scopes, version);

    for (Datatype dt : datatypesToAdd) {
      ArrayList<List<String>> temp = new ArrayList<List<String>>();
      List<String> version2 = new ArrayList<String>();
      version2.add(version);
      temp.add(version2);
      // DatatypeMap.put(dt.getName(), temp);
      if (!DatatypeMap.containsKey(dt.getName())) {
        DatatypeMap.put(dt.getName(), temp);
      } else {
        for (int i = 0; i < DatatypeMap.get(dt.getName()).size(); i++) {
          List<Datatype> datatypes = datatypeService.findByNameAndVersionAndScope(dt.getName(),
              DatatypeMap.get(dt.getName()).get(i).get(0), "HL7STANDARD");
          Datatype d = datatypes != null && !datatypes.isEmpty() ? datatypes.get(0) : null;
          if (d != null && !Visited.containsKey(dt.getName())) {
            if (deltaService.CompareDatatypes(d, dt)) {
              DatatypeMap.get(dt.getName()).get(i).add(version);

              System.out.println("FOUND IDENTIQUE");
              Visited.put(dt.getName(), 1);
            }
          }
        }
        if (!Visited.containsKey(dt.getName())) {
          List<String> version2Add = new ArrayList<String>();
          version2Add.add(version);
          DatatypeMap.get(dt.getName()).add(version2Add);
          Visited.put(dt.getName(), 1);
        }
      }
    }
  }

  public void addAllVersions() {
    initMAp();
    String[] versions = {"2.4", "2.5", "2.5.1", "2.6", "2.7", "2.7.1", "2.8", "2.8.1", "2.8.2"};
    // String[] versions = {"2.2","2.3"};
    for (int i = 0; i < versions.length; i++) {
      AddVersiontoMap(versions[i].toString());
    }
  }

  public void CreateCollectionOfUnchanged() {
    addAllVersions();

    for (Entry<String, ArrayList<List<String>>> e : DatatypeMap.entrySet()) {
      String name = e.getKey();
      ArrayList<List<String>> values = e.getValue();
      for (List<String> versions : values) {
        UnchangedDataType unchanged = new UnchangedDataType();
        unchanged.setName(name);
        unchanged.setVersions(versions);
        unchangedData.insert(unchanged);

      }
    }
  }

  public void CreateIntermediateFromUnchanged() throws CloneNotSupportedException {
    List<UnchangedDataType> unchanged = unchangedData.findAll();
    for (UnchangedDataType dt : unchanged) {
      List<Datatype> datatypes = datatypeService.findByNameAndVersionAndScope(dt.getName(),
          dt.getVersions().get(dt.getVersions().size() - 1), SCOPE.HL7STANDARD.toString());
      Datatype d = datatypes != null && !datatypes.isEmpty() ? datatypes.get(0) : null;
      Datatype newDatatype = d.clone();
      newDatatype.setId(null);
      newDatatype.setHl7Version("*");
      newDatatype.setScope(SCOPE.INTERMASTER);
      newDatatype.setHl7versions(dt.getVersions());
      datatypeService.save(newDatatype);

    }


    // List<Datatype> Inter = datatypeService.findByScope("INTERMASTER");
    // for (Datatype d : Inter) {
    // if (d.getComponents().size() != 0) {
    // MergeComponent(d);
    // datatypeService.save(d);
    // }
    //
    // }

  }

  /**
   * @param d
   * @throws Exception
   */
  private void MergeComponents() throws Exception {
    // TODO Auto-generated method stub
    List<Datatype> BeforeMerge = datatypeService.findByScope("INTERMASTER");
    for (Datatype dt : BeforeMerge) {
      if (dt.getComponents().size() != 0) {
        for (Component c : dt.getComponents()) {
          if (c.getDatatype() != null) {
            Datatype current = datatypeService.findById(c.getDatatype().getId());
            Datatype temp = datatypeService.findByCompatibleVersion(current.getName(),
                current.getHl7Version(), "INTERMASTER");
            c.getDatatype().setId(temp.getId());

          }
        }
      }
      datatypeService.save(dt);
    }


  }

  public void Colorate() {
    addAllVersions();

    for (Entry<String, ArrayList<List<String>>> e : DatatypeMap.entrySet()) {
      String name = e.getKey();
      DatatypeMatrix dt = new DatatypeMatrix();
      dt.setName(name);
      HashMap<String, Integer> links = new HashMap<String, Integer>();

      ArrayList<List<String>> values = e.getValue();
      for (int i = 0; i < values.size(); i++) {
        for (String version : values.get(i)) {

          links.put(version.replace(".", ""), i);
        }
      }
      dt.setLinks(links);
      if (!dt.getName().equals("-")) {
        matrix.insert(dt);
      }
    }
  }



  // NOTE:ADD version to preloaded segs,dts,vs
  private void addVersionAndScopetoHL7IG() {
    List<String> hl7Versions = new ArrayList<String>();

    hl7Versions.add("2.3.1");
    hl7Versions.add("2.4");
    hl7Versions.add("2.5");
    hl7Versions.add("2.5.1");
    hl7Versions.add("2.6");
    hl7Versions.add("2.7");

    List<IGDocument> igDocuments =
        documentService.findByScopeAndVersions(IGDocumentScope.HL7STANDARD, hl7Versions);
    for (IGDocument igd : igDocuments) {
      Messages msgs = igd.getProfile().getMessages();
      System.out.println(msgs.getChildren().size());
      for (Message msg : msgs.getChildren()) {
        msg.setScope(SCOPE.HL7STANDARD);
        msg.setHl7Version(igd.getMetaData().getHl7Version());


      }
      messageService.save(msgs.getChildren());


    }
  }

  // private void addVersionAndScopetoPRELOADEDIG() {
  // List<String> hl7Versions = new ArrayList<String>();
  // hl7Versions.add("2.1");
  // hl7Versions.add("2.2");
  // hl7Versions.add("2.3");
  // hl7Versions.add("2.3.1");
  // hl7Versions.add("2.4");
  // hl7Versions.add("2.5");
  // hl7Versions.add("2.5.1");
  // hl7Versions.add("2.6");
  // hl7Versions.add("2.7");
  //
  // List<IGDocument> igDocuments =
  // documentService.findByScopeAndVersions(IGDocumentScope.PRELOADED, hl7Versions);
  // Set<String> segIds = new HashSet<String>();
  // for (IGDocument igd : igDocuments) {
  // Messages msgs = igd.getProfile().getMessages();
  // for (Message msg : msgs.getChildren()) {
  // msg.setScope(SCOPE.PRELOADED);
  // for (SegmentRefOrGroup segRef : msg.getChildren()) {
  //
  // if (segRef instanceof SegmentRef) {
  // segIds.add(((SegmentRef) segRef).getRef().getId());
  // } else if (segRef instanceof Group) {
  // segIds.addAll(processGrp((Group) segRef));
  // }
  // }
  // private void addScopeUserToOldClonedPRELOADEDIG() {
  // List<String> hl7Versions = new ArrayList<String>();
  // // hl7Versions.add("2.1");
  // // hl7Versions.add("2.2");
  // // hl7Versions.add("2.3");
  // // hl7Versions.add("2.3.1");
  // // hl7Versions.add("2.4");
  // // hl7Versions.add("2.5");
  // hl7Versions.add("2.5.1");
  // // hl7Versions.add("2.6");
  // // hl7Versions.add("2.7");
  //
  // List<IGDocument> igDocuments =
  // documentService.findByScopeAndVersionsInIg(IGDocumentScope.USER, hl7Versions);
  // for (IGDocument igd : igDocuments) {
  // Messages msgs = igd.getProfile().getMessages();
  // for (Message msg : msgs.getChildren()) {
  // if (SCOPE.USER.equals(msg.getScope()) || SCOPE.PRELOADED.equals(msg.getScope())) {
  // msg.setScope(SCOPE.USER);
  // }
  // List<Segment> preSegs = segmentService.findByIds(segIds);
  // Set<String> preDtsId = new HashSet<String>();
  // Set<String> preVssId = new HashSet<String>();
  // List<Segment> segToSave = new ArrayList<Segment>();
  // List<Datatype> dtToSave = new ArrayList<Datatype>();
  // List<Table> tableToSave = new ArrayList<Table>();
  // for (Segment seg : preSegs) {
  // if (seg.getScope() == SCOPE.USER) {
  // seg.setScope(SCOPE.PRELOADED);
  // for (Field fld : seg.getFields()) {
  // preDtsId.add(fld.getDatatype().getId());
  // for (TableLink t : fld.getTables()) {
  // preVssId.add(t.getId());
  // }
  // }
  // List<Datatype> preDts = datatypeService.findByIds(preDtsId);
  // // List<Table> preVss=tableService.findAllByIds(preVssId);
  // for (Datatype dt : preDts) {
  // if (dt.getScope() == SCOPE.USER) {
  // for (Component comp : dt.getComponents()) {
  // for (TableLink t : comp.getTables()) {
  // preVssId.add(t.getId());
  // }
  // }
  // dt.setScope(SCOPE.PRELOADED);
  // dtToSave.add(dt);
  //
  // }
  // }
  // Set<String> preDtsIdInComp = new HashSet<String>();
  // for (Datatype dtInComp : dtToSave) {
  // for (Component comp : dtInComp.getComponents()) {
  //
  // preDtsIdInComp.add(comp.getDatatype().getId());
  // }
  //
  // }
  // List<Datatype> preDtsInComp = datatypeService.findByIds(preDtsIdInComp);
  // for (Datatype dt : preDtsInComp) {
  // if (dt.getScope() == SCOPE.USER) {
  // for (Component comp : dt.getComponents()) {
  // for (TableLink t : comp.getTables()) {
  // preVssId.add(t.getId());
  // }
  // }
  // dt.setScope(SCOPE.PRELOADED);
  // dtToSave.add(dt);
  //
  // }
  // }
  // List<Table> preVs = tableService.findAllByIds(preVssId);
  //
  // for (Table preTable : preVs) {
  // if (preTable.getScope() == SCOPE.USER) {
  // preTable.setScope(SCOPE.PRELOADED);
  // tableToSave.add(preTable);
  // }
  // }
  // System.out.println(dtToSave);
  // segToSave.add(seg);
  //
  // }
  // Set<String> usedSegsId = new HashSet<String>();
  // SegmentLibrary segmentLib = igd.getProfile().getSegmentLibrary();
  // for (SegmentLink segLink : segmentLib.getChildren()) {
  // usedSegsId.add(segLink.getId());
  // }
  // List<Segment> usedSegs = segmentService.findByIds(usedSegsId);
  // for (Segment usedSeg : usedSegs) {
  // if (SCOPE.PRELOADED.equals(usedSeg.getScope())) {
  // usedSeg.setScope(SCOPE.USER);
  // }
  // segmentService.save(segToSave);
  // datatypeService.save(dtToSave);
  // tableService.save(tableToSave);
  //
  // msg.setHl7Version(igd.getMetaData().getHl7Version());
  // Set<String> usedDtsId = new HashSet<String>();
  // DatatypeLibrary datatypeLib = igd.getProfile().getDatatypeLibrary();
  // for (DatatypeLink dtLink : datatypeLib.getChildren()) {
  // usedDtsId.add(dtLink.getId());
  // }
  // List<Datatype> usedDts = datatypeService.findByIds(usedDtsId);
  // for (Datatype usedDt : usedDts) {
  // if (SCOPE.PRELOADED.equals((usedDt.getScope()))) {
  // usedDt.setScope(SCOPE.USER);
  // }
  // }
  // datatypeService.save(usedDts);
  //
  //
  // Set<String> usedTbsId = new HashSet<String>();
  // TableLibrary tableLib = igd.getProfile().getTableLibrary();
  // for (TableLink dtLink : tableLib.getChildren()) {
  // usedTbsId.add(dtLink.getId());
  // }
  // List<Table> usedTbs = tableService.findAllByIds(usedTbsId);
  // for (Table usedDt : usedTbs) {
  // if (SCOPE.PRELOADED.equals(usedDt.getScope())) {
  // usedDt.setScope(SCOPE.USER);
  // }
  //
  // }
  // messageService.save(msgs.getChildren());
  //
  //
  // }
  // }

  // private Set<String> SegIdsInMsg(Message msg, Set<String> result){
  // for(SegmentRefOrGroup segRef: msg.getChildren()){
  //
  // }
  // }
  private Set<String> processGrp(Group grp) {
    Set<String> result = new HashSet<String>();
    for (SegmentRefOrGroup segOrGrp : grp.getChildren()) {
      if (segOrGrp instanceof SegmentRef) {
        result.add(((SegmentRef) segOrGrp).getRef().getId());
      } else if (segOrGrp instanceof Group) {
        result.addAll(processGrp((Group) segOrGrp));
      }
    }
    return result;

  }

  private void checkTableNameForAllIGDocuments() throws IGDocumentException {

    List<IGDocument> igDocuments = documentService.findAll();

    for (IGDocument igd : igDocuments) {
      boolean ischanged = false;
      TableLibrary tables = igd.getProfile().getTableLibrary();

      for (TableLink tl : tables.getChildren()) {
        // if (t.getName() == null || t.getName().equals("")) {
        // if (t.getDescription() != null) {
        // t.setName(t.getDescription());
        // ischanged = true;
        // } else
        // t.setName("NONAME");
        // }
      }

      if (ischanged)
        documentService.apply(igd);
    }
  }

  private void AddVersionsToDatatypes() {
    List<Datatype> dts = datatypeService.findAll();
    for (Datatype d : dts) {
      if (d.getHl7versions() != null && d.getHl7versions().isEmpty()) {
        if (!d.getScope().equals(SCOPE.MASTER)) {

          d.getHl7versions().add(d.getHl7Version());
          datatypeService.save(d);

        }
      }
    }
  }


}
