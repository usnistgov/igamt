package gov.nist.healthcare.tools.hl7.v2.igamt.lite.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.healthcare.nht.acmgt.dto.ResponseMessage;
import gov.nist.healthcare.nht.acmgt.dto.domain.Account;
import gov.nist.healthcare.nht.acmgt.repo.AccountRepository;
import gov.nist.healthcare.nht.acmgt.service.UserService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Case;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Component;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.CompositeMessage;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.CompositeMessages;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Constant;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Constant.SCOPE;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Constant.STATUS;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Datatype;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.DatatypeLibrary;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.DatatypeLink;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.DocumentMetaData;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ElementVerification;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ExportConfig;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ExportFontConfig;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Field;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Group;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.IGDocument;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.IGDocumentConfiguration;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.IGDocumentScope;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.IgDocumentComparator;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Mapping;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Message;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.MessageComparator;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Messages;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.PositionComparator;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Profile;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ProfileComponent;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ProfileComponentLibrary;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ProfileComponentLink;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ProfileMetaData;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Section;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.SectionMap;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Segment;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.SegmentLibrary;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.SegmentLink;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.SegmentRef;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.SegmentRefOrGroup;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ShareParticipantPermission;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Table;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.TableLibrary;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.TableLink;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.messageevents.MessageEvents;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.repo.MessageRepository;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.repo.ProfileComponentLibraryRepository;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.repo.ProfileComponentRepository;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.CompositeMessageService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.DatatypeLibraryService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.DatatypeService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.ExportConfigService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.ExportFontConfigService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.ExportFontService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.ExportService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.IGDocumentCreationService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.IGDocumentDeleteException;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.IGDocumentException;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.IGDocumentExportService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.IGDocumentListException;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.IGDocumentNotFoundException;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.IGDocumentService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.MessageService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.PhinvadsWSCallService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.ProfileComponentLibraryService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.ProfileComponentService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.ProfileException;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.ProfileNotFoundException;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.ProfileSerialization;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.ProfileService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.SegmentLibraryService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.SegmentService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.TableLibraryService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.TableService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.serialization.SerializationLayout;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.util.DateUtils;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.web.controller.wrappers.EventWrapper;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.web.controller.wrappers.IntegrationIGDocumentRequestWrapper;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.web.controller.wrappers.ScopesAndVersionWrapper;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.web.exception.GVTExportException;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.web.exception.NotFoundException;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.web.exception.OperationNotAllowException;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.web.exception.UserAccountNotFoundException;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.web.util.GVTClient;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.web.util.TimerTaskForPHINVADSValueSetDigger;

@RestController
@RequestMapping("/igdocuments")
public class IGDocumentController extends CommonController {


  Logger log = LoggerFactory.getLogger(IGDocumentController.class);

  @Autowired
  private IGDocumentService igDocumentService;

  @Autowired
  private IGDocumentExportService igDocumentExport;

  @Autowired
  private ExportService exportService;

  @Autowired
  private IGDocumentConfiguration igDocumentConfig;

  @Autowired
  UserService userService;
  @Autowired
  ProfileService profileService;
  @Autowired
  AccountRepository accountRepository;
  @Autowired
  ExportConfigService exportConfigService;
  @Autowired
  ExportFontConfigService exportFontConfigService;
  @Autowired
  ExportFontService exportFontService;
  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private IGDocumentCreationService igDocumentCreation;
  @Autowired
  private ProfileComponentService profileComponentService;
  @Autowired
  private ProfileComponentRepository profileComponentRepository;
  @Autowired
  private ProfileComponentLibraryRepository profileComponentLibraryRepository;
  @Autowired
  private ProfileComponentLibraryService profileComponentLibraryService;
  @Autowired
  private CompositeMessageService compositeMessageService;
  @Autowired
  private DatatypeLibraryService datatypeLibraryService;

  @Autowired
  private SegmentLibraryService segmentLibraryService;

  @Autowired
  private TableLibraryService tableLibraryService;

  @Autowired
  private DatatypeService datatypeService;

  @Autowired
  private SegmentService segmentService;

  @Autowired
  private TableService tableService;

  @Autowired
  private MessageService messageService;

  @Autowired
  private ProfileSerialization profileSerializationService;

  @Value("${server.email}")
  private String SERVER_EMAIL;

  @Value("${admin.email}")
  private String ADMIN_EMAIL;

  @Value("${gvt.url}")
  private String GVT_URL;


  @Value("${gvt.exportEndpoint}")
  private String GVT_EXPORT_ENDPOINT;



  @Autowired
  private MailSender mailSender;

  @Autowired
  private SimpleMailMessage templateMessage;

  public IGDocumentService getIgDocumentService() {
    return igDocumentService;
  }

  public void setIGDocumentService(IGDocumentService igDocumentService) {
    this.igDocumentService = igDocumentService;
  }

  /**
   * 
   * @param type
   * @return
   * @throws UserAccountNotFoundException
   * @throws IGDocumentException
   */
  @RequestMapping(method = RequestMethod.GET, produces = "application/json")
  public List<IGDocument> getIGDocumentListByType(@RequestParam("type") String type)
      throws UserAccountNotFoundException, IGDocumentListException {
    try {
      if ("PRELOADED".equalsIgnoreCase(type)) {
        return GetorderByposition(preloaded());
      } else if ("USER".equalsIgnoreCase(type)) {
        return GetorderByposition(userIGDocuments());
      } else if ("SHARED".equalsIgnoreCase(type)) {
        return GetorderByposition(sharedIGDocument());
      }
      throw new IGDocumentListException("Unknown IG document type");
    } catch (RuntimeException e) {
      throw new IGDocumentListException(e);
    } catch (Exception e) {
      throw new IGDocumentListException(e);
    }
  }

  @RequestMapping(value = "/findByScopesAndVersion", method = RequestMethod.POST,
      produces = "application/json")
  public List<IGDocument> findByScopesAndVersion(
      @RequestBody ScopesAndVersionWrapper scopesAndVersion) {
    log.info("Fetching the IG Document. scope=" + scopesAndVersion.getScopes() + " hl7Version="
        + scopesAndVersion.getHl7Version());
    List<IGDocument> igDocuments = new ArrayList<IGDocument>();
    try {
      User u = userService.getCurrentUser();
      Account account = accountRepository.findByTheAccountsUsername(u.getUsername());
      if (account == null) {
        throw new UserAccountNotFoundException();
      }

      if (scopesAndVersion.getScopes().get(0).toString() == "HL7STANDARD") {
        igDocuments.addAll(igDocumentService.findByScopesAndVersion(scopesAndVersion.getScopes(),
            scopesAndVersion.getHl7Version()));
      }
      if (scopesAndVersion.getScopes().get(0).toString() == "USER") {
        System.out.println("==================");
        igDocuments.addAll(igDocumentService.findByAccountIdAndScopesAndVersion(account.getId(),
            scopesAndVersion.getScopes(), scopesAndVersion.getHl7Version()));
      }

      if (igDocuments.isEmpty()) {
        throw new NotFoundException(
            "IG Document not found for scopesAndVersion=" + scopesAndVersion);
      }
    } catch (Exception e) {
      log.error("", e);
    }

    return igDocuments;
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
  public IGDocument getIgDocumentById(@PathVariable("id") String id) throws NotFoundException {
    log.info("Fetching igDocumentById..." + id);
    return findById(id);
  }

  public IGDocument findById(String id) throws NotFoundException {
    IGDocument result = igDocumentService.findById(id);
    if (result == null)
      throw new NotFoundException("igDocumentNotFound");
    return result;
  }

  /**
   * Return the list of pre-loaded profiles
   * 
   * @return
   */
  private List<IGDocument> preloaded() {
    log.info("Fetching all preloaded IGDocuments...");
    List<IGDocument> result = igDocumentService.findAllPreloaded();

    return result;
  }

  /**
   * @return
   * @throws UserAccountNotFoundException
   */
  private List<IGDocument> userIGDocuments() throws UserAccountNotFoundException {
    log.info("Fetching all custom IGDocuments...");
    User u = userService.getCurrentUser();
    Account account = accountRepository.findByTheAccountsUsername(u.getUsername());
    if (account == null) {
      throw new UserAccountNotFoundException();
    }
    return igDocumentService.findByAccountId(account.getId());
  }

  @RequestMapping(value = "/{id}/clone", method = RequestMethod.POST)
  public IGDocument clone(@PathVariable("id") String id)
      throws IGDocumentNotFoundException, UserAccountNotFoundException, IGDocumentException {
    try {
      log.info("Clone IGDocument with id=" + id);

      HashMap<String, String> segmentIdChangeMap = new HashMap<String, String>();
      HashMap<String, String> datatypeIdChangeMap = new HashMap<String, String>();
      HashMap<String, String> tableIdChangeMap = new HashMap<String, String>();

      User u = userService.getCurrentUser();
      Account account = accountRepository.findByTheAccountsUsername(u.getUsername());
      if (account == null)
        throw new UserAccountNotFoundException();
      IGDocument igDocument = this.findIGDocument(id);

      for (Message m : igDocument.getProfile().getMessages().getChildren()) {
        m.setId(null);
        if (m.getScope() == SCOPE.PRELOADED) {
          m.setScope(SCOPE.USER);
        }
        messageService.save(m);
      }
      for (CompositeMessage cm : igDocument.getProfile().getCompositeMessages().getChildren()) {
        cm.setId(null);
        compositeMessageService.save(cm);
      }

      DatatypeLibrary datatypeLibrary = igDocument.getProfile().getDatatypeLibrary();
      SegmentLibrary segmentLibrary = igDocument.getProfile().getSegmentLibrary();
      TableLibrary tableLibrary = igDocument.getProfile().getTableLibrary();
      ProfileComponentLibrary profileComponentLibrary =
          igDocument.getProfile().getProfileComponentLibrary();

      DatatypeLibrary clonedDatatypeLibrary = datatypeLibrary.clone();
      SegmentLibrary clonedSegmentLibrary = segmentLibrary.clone();
      TableLibrary clonedTableLibrary = tableLibrary.clone();
      ProfileComponentLibrary clonedProfileComponentLibrary = profileComponentLibrary.clone();

      clonedDatatypeLibrary.setChildren(new HashSet<DatatypeLink>());
      clonedSegmentLibrary.setChildren(new HashSet<SegmentLink>());
      clonedTableLibrary.setChildren(new HashSet<TableLink>());
      clonedProfileComponentLibrary.setChildren(new HashSet<ProfileComponentLink>());

      datatypeLibraryService.save(clonedDatatypeLibrary);
      segmentLibraryService.save(clonedSegmentLibrary);
      tableLibraryService.save(clonedTableLibrary);
      profileComponentLibraryService.save(clonedProfileComponentLibrary);

      List<ProfileComponent> profilecomponents =
          profileComponentLibraryService.findProfileComponentsById(profileComponentLibrary.getId());
      if (profilecomponents != null) {
        for (int i = 0; i < profilecomponents.size(); i++) {
          String oldPCId = null;
          ProfileComponent pc = profilecomponents.get(i);
          ProfileComponentLink pcL = profileComponentLibrary.findOne(pc.getId()).clone();
          pc.setId(null);
          profileComponentService.save(pc);
          pcL.setId(pc.getId());
          clonedProfileComponentLibrary.addProfileComponent(pcL);

        }
      }

      List<Datatype> datatypes = datatypeLibraryService.findDatatypesById(datatypeLibrary.getId());
      if (datatypes != null) {
        for (int i = 0; i < datatypes.size(); i++) {
          String oldDatatypeId = null;
          Datatype d = datatypes.get(i);
          DatatypeLink dl = datatypeLibrary.findOne(d.getId()).clone();
          if (d.getStatus().equals(STATUS.UNPUBLISHED) || d.getScope().equals(SCOPE.PRELOADED)) {
            oldDatatypeId = d.getId();
            d.setScope(SCOPE.USER);
            d.setCreatedFrom(oldDatatypeId);
            d.setId(null);
            d.setLibId(new HashSet<String>());
          }

          d.getLibIds().add(clonedDatatypeLibrary.getId());
          datatypeService.save(d);
          dl.setId(d.getId());
          clonedDatatypeLibrary.addDatatype(dl);
          if (oldDatatypeId != null) {
            datatypeIdChangeMap.put(oldDatatypeId, d.getId());
          }
        }
      }

      List<Segment> segments = segmentLibraryService.findSegmentsById(segmentLibrary.getId());
      if (segments != null) {
        for (int i = 0; i < segments.size(); i++) {
          String oldSegmentId = null;
          Segment s = segments.get(i);
          SegmentLink sl = segmentLibrary.findOne(s.getId()).clone();
          if (s.getScope().equals(SCOPE.USER) || s.getScope().equals(SCOPE.PRELOADED)) {
            oldSegmentId = s.getId();
            s.setScope(SCOPE.USER);
            s.setCreatedFrom(oldSegmentId);
            s.setId(null);
            s.setLibId(new HashSet<String>());
          }
          s.getLibIds().add(clonedSegmentLibrary.getId());
          segmentService.save(s);
          sl.setId(s.getId());
          clonedSegmentLibrary.addSegment(sl);
          if (oldSegmentId != null) {
            segmentIdChangeMap.put(oldSegmentId, s.getId());
          }
        }

      }

      List<Table> tables = tableLibraryService.findTablesByIds(tableLibrary.getId());
      if (tables != null) {
        for (int i = 0; i < tables.size(); i++) {
          String oldTableId = null;
          Table t = tables.get(i);
          TableLink tl = tableLibrary.findOneTableById(t.getId()).clone();
          if (t.getStatus().equals(STATUS.UNPUBLISHED) || t.getScope().equals(SCOPE.PRELOADED)) {
            oldTableId = t.getId();
            t.setScope(SCOPE.USER);
            t.setCreatedFrom(oldTableId);
            t.setId(null);
            t.setLibIds(new HashSet<String>());
          }
          t.getLibIds().add(clonedTableLibrary.getId());
          tableService.save(t);
          tl.setId(t.getId());
          clonedTableLibrary.addTable(tl);
          if (oldTableId != null) {
            tableIdChangeMap.put(oldTableId, t.getId());
          }
        }
      }

      datatypeLibraryService.save(clonedDatatypeLibrary);
      segmentLibraryService.save(clonedSegmentLibrary);
      tableLibraryService.save(clonedTableLibrary);
      profileComponentLibraryService.save(clonedProfileComponentLibrary);


      igDocument.getProfile().setMetaData(igDocument.getProfile().getMetaData().clone());
      igDocument.getProfile().setDatatypeLibrary(clonedDatatypeLibrary);
      igDocument.getProfile().setSegmentLibrary(clonedSegmentLibrary);
      igDocument.getProfile().setTableLibrary(clonedTableLibrary);
      igDocument.getProfile().setProfileComponentLibrary(clonedProfileComponentLibrary);

      updateModifiedId(tableIdChangeMap, datatypeIdChangeMap, segmentIdChangeMap,
          igDocument.getProfile());
      String sourceId = igDocument.getId();
      igDocument.setId(null);
      igDocument.getShareParticipantIds().clear();
      igDocument.setScope(IGDocumentScope.USER);
      igDocument.setAccountId(account.getId());
      igDocument.setCreatedFrom(sourceId);
      igDocument.getMetaData().setTitle(igDocument.getMetaData().getTitle() + "- Copy");
      return igDocumentService.save(igDocument);
    } catch (UserAccountNotFoundException e) {
      throw new IGDocumentException(e);
    } catch (CloneNotSupportedException e) {
      throw new IGDocumentException(e);
    }
  }

  private void updateModifiedId(HashMap<String, String> tableIdChangeMap,
      HashMap<String, String> datatypeIdChangeMap, HashMap<String, String> segmentIdChangeMap,
      Profile profile) {
    for (SegmentLink sl : profile.getSegmentLibrary().getChildren()) {
      Segment s = segmentService.findById(sl.getId());
      for (Field f : s.getFields()) {
        if (f.getDatatype() != null && f.getDatatype().getId() != null
            && datatypeIdChangeMap.containsKey(f.getDatatype().getId()))
          f.getDatatype().setId(datatypeIdChangeMap.get(f.getDatatype().getId()));
        if (f.getTables() != null && !f.getTables().isEmpty()) {
          for (TableLink tableLink : f.getTables()) {
            if (tableLink != null && tableLink.getId() != null
                && tableIdChangeMap.containsKey(tableLink.getId()))
              tableLink.setId(tableIdChangeMap.get(tableLink.getId()));
          }
        }
      }

      for (Mapping map : s.getDynamicMapping().getMappings()) {
        for (Case c : map.getCases()) {
          if (c.getDatatype() != null && datatypeIdChangeMap.containsKey(c.getDatatype()))
            c.setDatatype(datatypeIdChangeMap.get(c.getDatatype()));
        }
      }
      segmentService.save(s);
    }

    for (DatatypeLink dl : profile.getDatatypeLibrary().getChildren()) {
      Datatype d = datatypeService.findById(dl.getId());
      for (Component c : d.getComponents()) {
        if (c.getDatatype() != null && c.getDatatype().getId() != null
            && datatypeIdChangeMap.containsKey(c.getDatatype().getId()))
          c.getDatatype().setId(datatypeIdChangeMap.get(c.getDatatype().getId()));
        if (c.getTables() != null && !c.getTables().isEmpty()) {
          for (TableLink tableLink : c.getTables()) {
            if (tableLink != null && tableLink.getId() != null
                && tableIdChangeMap.containsKey(tableLink))
              tableLink.setId(tableIdChangeMap.get(tableLink.getId()));
          }
        }
      }
      datatypeService.save(d);
    }

    for (Message m : profile.getMessages().getChildren()) {
      for (SegmentRefOrGroup sog : m.getChildren()) {
        this.udateModifiedSegmentIdAndVisitChild(segmentIdChangeMap, sog);
      }
      messageService.save(m);
    }
  }

  private void udateModifiedSegmentIdAndVisitChild(HashMap<String, String> segmentIdChangeMap,
      SegmentRefOrGroup sog) {
    if (sog instanceof SegmentRef) {
      SegmentRef segmentRef = (SegmentRef) sog;
      if (segmentIdChangeMap.containsKey(segmentRef.getRef().getId()))
        segmentRef.getRef().setId(segmentIdChangeMap.get(segmentRef.getRef().getId()));
    }

    if (sog instanceof Group) {
      Group g = (Group) sog;

      for (SegmentRefOrGroup child : g.getChildren()) {
        this.udateModifiedSegmentIdAndVisitChild(segmentIdChangeMap, child);
      }
    }

  }

  @RequestMapping(value = "/{id}/", method = RequestMethod.GET)
  public IGDocument get(@PathVariable("id") String id) throws IGDocumentNotFoundException {
    try {
      log.info("Fetching profile with id=" + id);
      User u = userService.getCurrentUser();
      Account account = accountRepository.findByTheAccountsUsername(u.getUsername());
      if (account == null)
        throw new UserAccountNotFoundException();
      IGDocument d = findIGDocument(id);
      return d;
    } catch (RuntimeException e) {
      throw new IGDocumentNotFoundException(e);
    } catch (Exception e) {
      throw new IGDocumentNotFoundException(e);
    }
  }

  @RequestMapping(value = "/{id}/profile/profilecomponent/save", method = RequestMethod.POST)
  public ProfileComponent saveProfileComponent(@PathVariable("id") String id,
      @RequestBody ProfileComponent profileComponent, HttpServletRequest request,
      HttpServletResponse response)
      throws IOException, IGDocumentNotFoundException, IGDocumentException {
    IGDocument d = igDocumentService.findOne(id);
    if (d == null) {
      throw new IGDocumentNotFoundException(id);
    }

    profileComponent.setDateUpdated(new Date());
    if (d.getProfile().getProfileComponentLibrary() == null) {
      ProfileComponentLibrary profileComponentLibrary = new ProfileComponentLibrary();
      profileComponentService.create(profileComponent);
      ProfileComponentLink profileComponentLink = new ProfileComponentLink();
      profileComponentLink.setId(profileComponent.getId());
      profileComponentLink.setName(profileComponent.getName());
      profileComponentLibrary.addProfileComponent(profileComponentLink);
      profileComponentLibraryRepository.save(profileComponentLibrary);
      d.getProfile().setProfileComponentLibrary(profileComponentLibrary);
    } else {
      profileComponentService.create(profileComponent);
      ProfileComponentLink profileComponentLink = new ProfileComponentLink();
      profileComponentLink.setId(profileComponent.getId());
      profileComponentLink.setName(profileComponent.getName());
      d.getProfile().getProfileComponentLibrary().addProfileComponent(profileComponentLink);;
      profileComponentLibraryRepository.save(d.getProfile().getProfileComponentLibrary());
    }
    igDocumentService.save(d);
    return profileComponent;
  }

  @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
  public ResponseMessage delete(@PathVariable("id") String id) throws IGDocumentDeleteException {
    try {
      User u = userService.getCurrentUser();
      Account account = accountRepository.findByTheAccountsUsername(u.getUsername());
      if (account == null)
        throw new UserAccountNotFoundException();
      log.info("Delete IGDocument with id=" + id);
      IGDocument d = findIGDocument(id);
      if (d.getAccountId() == account.getId()) {
        deleteSegmentLibrary(d.getProfile().getSegmentLibrary());
        deleteTableLibrary(d.getProfile().getTableLibrary());
        deleteDatatypeLibrary(d.getProfile().getDatatypeLibrary());
        deleteProfileComponentLibrary(d.getProfile().getProfileComponentLibrary());
        deleteConformanceProfiles(d.getProfile().getMessages());
        deleteCompositeMessages(d.getProfile().getCompositeMessages());
        igDocumentService.delete(id);
        return new ResponseMessage(ResponseMessage.Type.success, "igDocumentDeletedSuccess", null);
      } else {
        throw new OperationNotAllowException("delete");
      }
    } catch (RuntimeException e) {
      throw new IGDocumentDeleteException(e);
    } catch (Exception e) {
      throw new IGDocumentDeleteException(e);
    }
  }

  private void deleteConformanceProfiles(Messages messages) {
    if (messages != null && messages.getChildren() != null) {
      for (Message m : messages.getChildren()) {
        if (m != null)
          messageService.delete(m);
      }
    }
  }

  private void deleteDatatypeLibrary(DatatypeLibrary datatypeLibrary) {
    List<Datatype> datatypes = datatypeLibraryService.findDatatypesById(datatypeLibrary.getId());
    List<Datatype> toremove = new ArrayList<Datatype>();
    if (datatypes != null) {
      for (int i = 0; i < datatypes.size(); i++) {
        Datatype d = datatypes.get(i);
        if (d.getStatus().equals(STATUS.UNPUBLISHED) && d.getScope().equals(SCOPE.USER)) {
          toremove.add(d);
        }
      }
      if (!toremove.isEmpty()) {
        datatypeService.delete(toremove);
      }
    }
    datatypeLibraryService.delete(datatypeLibrary);
  }

  private void deleteTableLibrary(TableLibrary tableLibrary) {
    List<Table> tables = tableLibraryService.findTablesByIds(tableLibrary.getId());
    List<Table> toremove = new ArrayList<Table>();

    if (tables != null) {
      for (int i = 0; i < tables.size(); i++) {
        Table t = tables.get(i);
        if (t.getStatus().equals(STATUS.UNPUBLISHED) && (t.getScope().equals(SCOPE.USER))) {
          toremove.add(t);
        }
      }
      if (!toremove.isEmpty()) {
        tableService.delete(toremove);
      }
    }
    tableLibraryService.delete(tableLibrary);
  }


  private void deleteSegmentLibrary(SegmentLibrary segmentLibrary) {
    List<Segment> segments = segmentLibraryService.findSegmentsById(segmentLibrary.getId());
    List<Segment> toremove = new ArrayList<Segment>();
    if (segments != null) {
      for (int i = 0; i < segments.size(); i++) {
        Segment t = segments.get(i);
        if (t.getScope().equals(SCOPE.USER)) {
          toremove.add(t);
        }
      }
      if (!toremove.isEmpty()) {
        segmentService.delete(toremove);
      }
    }
    segmentLibraryService.delete(segmentLibrary);
  }

  private void deleteProfileComponentLibrary(ProfileComponentLibrary profileComponentLibrary) {
    List<ProfileComponent> profilecomponents =
        profileComponentLibraryService.findProfileComponentsById(profileComponentLibrary.getId());
    profileComponentService.delete(profilecomponents);
    profileComponentLibraryRepository.delete(profileComponentLibrary);
  }

  private void deleteCompositeMessages(CompositeMessages messages) {
    if (messages != null && messages.getChildren() != null) {
      for (CompositeMessage m : messages.getChildren()) {
        if (m != null)
          compositeMessageService.delete(m.getId());
      }
    }
  }



  @RequestMapping(value = "/{id}/export/xml", method = RequestMethod.POST, produces = "text/xml",
      consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public void export(@PathVariable("id") String id, HttpServletRequest request,
      HttpServletResponse response) throws IOException, IGDocumentNotFoundException {
    log.info("Exporting as xml file IGDcoument with id=" + id);
    IGDocument d = this.findIGDocument(id);
    InputStream content = null;
    content = exportService.exportIGDocumentAsXml(d);
    response.setContentType("text/xml");
    response.setHeader("Content-disposition",
        "attachment;filename=" + updateFileName(d.getMetaData().getTitle()) + "-" + id + "_"
            + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xml");
    FileCopyUtils.copy(content, response.getOutputStream());
  }

  @RequestMapping(value = "/{id}/export/html/{type}", method = RequestMethod.POST,
      produces = "text/html", consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public void exportHtml(@PathVariable("id") String id, @PathVariable("type") String type,
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    log.info("Exporting as html file IGDcoument with id=" + id);
    IGDocument d = this.findIGDocument(id);
    InputStream content = null;
    SerializationLayout serializationLayout = identifyLayout(type);
    User u = userService.getCurrentUser();
    Account account = accountRepository.findByTheAccountsUsername(u.getUsername());
    if (account == null) {
      throw new UserAccountNotFoundException();
    }
    ExportConfig exportConfig =
        exportConfigService.findOneByTypeAndAccountId(identifyType(type), account.getId());
    if (exportConfig == null) {
      exportConfig = ExportConfig.getBasicExportConfig(type);
    }
    ExportFontConfig exportFontConfig = null;
    List<ExportFontConfig> existing = exportFontConfigService.findOneByAccountId(account.getId());
    if (!existing.isEmpty()) {
      exportFontConfig = existing.get(0);

    }
    if (exportFontConfig == null) {
      exportFontConfig = exportFontConfigService.getDefaultExportFontConfig();
    }
    content = exportService.exportIGDocumentAsHtml(d, serializationLayout, exportConfig,
        exportFontConfig);
    response.setContentType("text/html");
    response.setHeader("Content-disposition",
        "attachment;filename=" + updateFileName(d.getMetaData().getTitle()) + "-" + id + "_"
            + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".html");
    FileCopyUtils.copy(content, response.getOutputStream());
  }

  private SerializationLayout identifyLayout(String layout) {
    SerializationLayout serializationLayout;
    switch (layout) {
      case "IgDocument":
        serializationLayout = SerializationLayout.IGDOCUMENT;
        break;
      case "Profile":
        serializationLayout = SerializationLayout.PROFILE;
        break;
      case "Table":
        serializationLayout = SerializationLayout.TABLES;
        break;
      default:
        serializationLayout = SerializationLayout.IGDOCUMENT;
        break;
    }
    return serializationLayout;
  }

  private String identifyType(String layout) {
    String type;
    switch (layout) {
      case "IgDocument":
        type = "IG Style";
        break;
      case "Profile":
        type = "Profile Style";
        break;
      case "Table":
        type = "Table Style";
        break;
      default:
        type = "IG Style";
        break;
    }
    return type;
  }

  @RequestMapping(value = "/{id}/export/zip", method = RequestMethod.POST,
      produces = "application/zip", consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public void exportZip(@PathVariable("id") String id, HttpServletRequest request,
      HttpServletResponse response) throws IOException, IGDocumentNotFoundException {
    log.info("Exporting as xml file profile with id=" + id);
    IGDocument d = findIGDocument(id);
    InputStream content = null;
    content = igDocumentExport.exportAsZip(d);
    response.setContentType("application/zip");
    response.setHeader("Content-disposition",
        "attachment;filename=" + updateFileName(d.getMetaData().getTitle()) + "-" + id + "_"
            + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".zip");
    FileCopyUtils.copy(content, response.getOutputStream());
  }

  @RequestMapping(value = "/{id}/export/Validation/{mIds}", method = RequestMethod.POST,
      produces = "application/zip", consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public void exportValidationXMLByMessages(@PathVariable("id") String id,
      @PathVariable("mIds") String[] messageIds, HttpServletRequest request,
      HttpServletResponse response)
      throws IOException, IGDocumentNotFoundException, CloneNotSupportedException {
    log.info("Exporting as xml file profile with id=" + id + " for selected messages="
        + Arrays.toString(messageIds));
    IGDocument d = findIGDocument(id);
    InputStream content = igDocumentExport.exportAsValidationForSelectedMessages(d, messageIds);
    response.setContentType("application/zip");
    response.setHeader("Content-disposition",
        "attachment;filename=" + updateFileName(d.getMetaData().getTitle()) + "-" + id + "_"
            + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".zip");
    FileCopyUtils.copy(content, response.getOutputStream());
  }

  @RequestMapping(value = "/{id}/export/Gazelle/{mIds}", method = RequestMethod.POST,
      produces = "application/zip", consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public void exportGazelleXMLByMessages(@PathVariable("id") String id,
      @PathVariable("mIds") String[] messageIds, HttpServletRequest request,
      HttpServletResponse response)
      throws IOException, IGDocumentNotFoundException, CloneNotSupportedException {
    log.info(
        "Exporting as xml file profile with id=" + id + " for selected messages=" + messageIds);
    IGDocument d = findIGDocument(id);
    InputStream content = null;
    content = igDocumentExport.exportAsGazelleForSelectedMessages(d, messageIds);
    response.setContentType("application/zip");
    response.setHeader("Content-disposition",
        "attachment;filename=" + updateFileName(d.getMetaData().getTitle()) + "-" + id + "_"
            + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".zip");
    FileCopyUtils.copy(content, response.getOutputStream());
  }

  @RequestMapping(value = "/{id}/export/Display/{mIds}", method = RequestMethod.POST,
      produces = "application/zip", consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public void exportDisplayXMLByMessages(@PathVariable("id") String id,
      @PathVariable("mIds") String[] messageIds, HttpServletRequest request,
      HttpServletResponse response)
      throws IOException, IGDocumentNotFoundException, CloneNotSupportedException {
    IGDocument d = findIGDocument(id);
    InputStream content = null;
    content = igDocumentExport.exportAsDisplayForSelectedMessage(d, messageIds);
    response.setContentType("application/zip");
    response.setHeader("Content-disposition",
        "attachment;filename=" + updateFileName(d.getMetaData().getTitle()) + "-" + id + "_"
            + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".zip");
    FileCopyUtils.copy(content, response.getOutputStream());
  }

  @RequestMapping(value = "/{id}/export/pdf", method = RequestMethod.POST,
      produces = "application/pdf", consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public void exportPdfFromXsl(@PathVariable("id") String id, HttpServletRequest request,
      HttpServletResponse response) throws IOException, IGDocumentNotFoundException {
    log.info("Exporting as pdf file profile with id=" + id);
    IGDocument d = findIGDocument(id);
    InputStream content = null;
    content = igDocumentExport.exportAsPdf(d);
    response.setContentType("application/pdf");
    response.setHeader("Content-disposition",
        "attachment;filename=" + updateFileName(d.getMetaData().getTitle()) + "-"
            + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf");
    FileCopyUtils.copy(content, response.getOutputStream());
  }

  @RequestMapping(value = "/{id}/export/docx/{type}", method = RequestMethod.POST,
      produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
      consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public void exportDocx(@PathVariable("id") String id, @PathVariable("type") String type,
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    log.info("Exporting as docx file profile with id=" + id);
    IGDocument d = findIGDocument(id);
    InputStream content = null;
    SerializationLayout serializationLayout = identifyLayout(type);
    User u = userService.getCurrentUser();
    Account account = accountRepository.findByTheAccountsUsername(u.getUsername());
    if (account == null) {
      throw new UserAccountNotFoundException();
    }
    ExportConfig exportConfig =
        exportConfigService.findOneByTypeAndAccountId(identifyType(type), account.getId());
    if (exportConfig == null) {
      exportConfig = ExportConfig.getBasicExportConfig(type);
    }
    ExportFontConfig exportFontConfig = null;
    List<ExportFontConfig> existing = exportFontConfigService.findOneByAccountId(account.getId());

    if (existing.isEmpty()) {
      exportFontConfig = exportFontConfigService.getDefaultExportFontConfig();
    } else {

      exportFontConfig = existing.get(0);

    }
    content = exportService.exportIGDocumentAsDocx(d, serializationLayout, exportConfig,
        exportFontConfig);
    response
        .setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    response.setHeader("Content-disposition",
        "attachment;filename=" + updateFileName(d.getMetaData().getTitle()) + "-" + id + "_"
            + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".docx");
    FileCopyUtils.copy(content, response.getOutputStream());
  }

  @RequestMapping(value = "/{id}/delta/pdf", method = RequestMethod.POST,
      produces = "application/pdf", consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public void deltaPdf(@PathVariable("id") String id, HttpServletRequest request,
      HttpServletResponse response) throws IOException, IGDocumentNotFoundException {
    log.info("Exporting delta as pdf file IGDocument with id=" + id);
    IGDocument d = findIGDocument(id);
    InputStream content = null;

    // TODO need to implement igDocumentService.diffToPdf
    content = igDocumentService.diffToPdf(d);
    response.setContentType("application/pdf");
    response.setHeader("Content-disposition", "attachment;filename=" + d.getMetaData().getTitle()
        + "-Delta-" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf");
    FileCopyUtils.copy(content, response.getOutputStream());
  }

  private IGDocument findIGDocument(String documentId) throws IGDocumentNotFoundException {
    IGDocument d = igDocumentService.findOne(documentId);
    if (d == null) {
      throw new IGDocumentNotFoundException(documentId);
    }
    return d;
  }

  @RequestMapping(value = "/{id}/export/xslx", method = RequestMethod.POST,
      produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      consumes = "application/x-www-form-urlencoded; charset=UTF-8")
  public void exportXlsx(@PathVariable("id") String id, HttpServletRequest request,
      HttpServletResponse response) throws IOException, IGDocumentNotFoundException {
    log.info("Exporting as spreadsheet profile with id=" + id);
    InputStream content = null;
    IGDocument d = findIGDocument(id);
    content = igDocumentExport.exportAsXlsx(d);
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setHeader("Content-disposition",
        "attachment;filename=" + updateFileName(d.getMetaData().getTitle()) + "-" + id + "_"
            + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx");
    FileCopyUtils.copy(content, response.getOutputStream());
  }

  @RequestMapping(value = "/config", method = RequestMethod.GET)
  public IGDocumentConfiguration config() {
    return this.igDocumentConfig;
  }

  @RequestMapping(value = "/{id}/verify/segment/{sId}", method = RequestMethod.POST,
      produces = "application/json")
  public ElementVerification verifySegment(@PathVariable("id") String id,
      @PathVariable("sId") String sId, HttpServletRequest request, HttpServletResponse response)
      throws IOException, IGDocumentNotFoundException {
    log.info("Verifying segment " + sId + " from profile " + id);
    IGDocument d = igDocumentService.findOne(id);
    if (d == null) {
      throw new IGDocumentNotFoundException(id);
    }
    return igDocumentService.verifySegment(d, sId, "segment");
  }

  @RequestMapping(value = "/{id}/verify/datatype/{dtId}", method = RequestMethod.POST,
      produces = "application/json")
  public ElementVerification verifyDatatype(@PathVariable("id") String id,
      @PathVariable("dtId") String dtId, HttpServletRequest request, HttpServletResponse response)
      throws IOException, IGDocumentNotFoundException {
    log.info("Verifying datatype " + dtId + " from profile " + id);
    IGDocument d = igDocumentService.findOne(id);
    if (d == null) {
      throw new IGDocumentNotFoundException(id);
    }
    return igDocumentService.verifyDatatype(d, dtId, "datatype");
  }

  @RequestMapping(value = "/{id}/verify/valueset/{vsId}", method = RequestMethod.POST,
      produces = "application/json")
  public ElementVerification verifyValueSet(@PathVariable("id") String id,
      @PathVariable("vsId") String vsId, HttpServletRequest request, HttpServletResponse response)
      throws IOException, ProfileNotFoundException {
    log.info("Verifying segment " + vsId + " from profile " + id);
    IGDocument d = igDocumentService.findOne(id);
    if (d == null) {
      throw new ProfileNotFoundException(id);
    }
    return igDocumentService.verifyValueSet(d, vsId, "valueset");
  }

  @RequestMapping(value = "/findVersions", method = RequestMethod.GET,
      produces = "application/json")
  public List<String> findHl7Versions() {
    log.info("Fetching all HL7 versions.");
    List<String> result = igDocumentCreation.findHl7Versions();
    return result;
  }

  @RequestMapping(value = "/{hl7Version}/tables", method = RequestMethod.GET,
      produces = "application/json")
  public Set<Table> findHl7Tables(@PathVariable("hl7Version") String hl7Version) {
    log.info("Fetching all Tables for " + hl7Version);
    List<SCOPE> scopes = new ArrayList<SCOPE>();
    scopes.add(SCOPE.HL7STANDARD);

    return new HashSet<Table>(tableService.findByScopesAndVersion(scopes, hl7Version));
  }

  @RequestMapping(value = "/PHINVADS/tables", method = RequestMethod.GET,
      produces = "application/json")
  public List<Table> findAllPreloadedPHINVADSTables() throws MalformedURLException {
    log.info("Fetching all Tables for preloaded PHINVADS");
    return new TimerTaskForPHINVADSValueSetDigger().findAllpreloadedPHINVADSTables();
  }

  @RequestMapping(value = "/{searchText}/PHINVADS/tables", method = RequestMethod.GET,
      produces = "application/json")
  public Set<Table> findPHINVADSTables(@PathVariable("searchText") String searchText)
      throws MalformedURLException {
    log.info("Fetching all Tables for " + searchText);
    return new PhinvadsWSCallService().generateTableList(searchText);
  }

  // TODO Change to query as is but with $nin a list of messages that can be
  // empty.
  // @RequestMapping(value = "/hl7/messageListByVersion/{hl7Version:.*}",
  // method = RequestMethod.POST, produces = "application/json")
  // public List<String[]> getMessageListByVersion(@PathVariable("hl7Version")
  // String hl7Version, MessageByListCommand command) {
  @RequestMapping(value = "/messageListByVersion", method = RequestMethod.POST,
      consumes = "application/json", produces = "application/json")
  public List<MessageEvents> getMessageListByVersion(@RequestBody String hl7Version) {
    log.info("Fetching messages of version hl7Version=" + hl7Version);
    List<MessageEvents> messages = igDocumentCreation.findMessageEvents(hl7Version);
    return messages;
  }

  @RequestMapping(value = "/createIntegrationProfile", method = RequestMethod.POST,
      consumes = "application/json", produces = "application/json")
  public IGDocument createIG(@RequestBody IntegrationIGDocumentRequestWrapper idrw)
      throws IGDocumentException {
    log.info("Creation of IGDocument.");
    log.debug("idrw.getMsgEvts()=" + idrw.getMsgEvts());
    log.debug("idrw.getMsgEvts()=" + idrw.getMetaData());
    log.debug("idrw.getAccountId()=" + idrw.getAccountId());
    User u = userService.getCurrentUser();
    Account account = accountRepository.findByTheAccountsUsername(u.getUsername());
    IGDocument igDocument = igDocumentCreation.createIntegratedIGDocument(idrw.getMsgEvts(),
        idrw.getMetaData(), idrw.getHl7Version(), account.getId());
    return igDocument;
  }

  @RequestMapping(value = "/updateIntegrationProfile", method = RequestMethod.POST,
      consumes = "application/json", produces = "application/json")
  public IGDocument updateIG(@RequestBody IntegrationIGDocumentRequestWrapper idrw)
      throws IGDocumentException {
    log.info("Update profile with additional messages.");
    log.debug("getMsgEvts()" + idrw.getMsgEvts());
    log.debug("getIgdocument()" + idrw.getIgdocument());
    IGDocument igDocument =
        igDocumentCreation.updateIntegratedIGDocument(idrw.getMsgEvts(), idrw.getIgdocument());
    return igDocument;
  }

  @RequestMapping(value = "/{id}/deleteMessage/{messageId}", method = RequestMethod.POST,
      produces = "application/json")
  public Long deleteMessage(@PathVariable("id") String id,
      @PathVariable("messageId") String messageId, HttpServletRequest request,
      HttpServletResponse response)
      throws IOException, IGDocumentNotFoundException, IGDocumentException {
    IGDocument d = igDocumentService.findOne(id);
    if (d == null) {
      throw new IGDocumentNotFoundException(id);
    }
    Set<Message> messages = d.getProfile().getMessages().getChildren();
    Message found = null;
    for (Message message : messages) {
      if (message.getId().equals(messageId)) {
        found = message;
        break;
      }
    }
    if (found != null) {
      messages.remove(found);
      igDocumentService.save(d);
    }
    return d.getDateUpdated().getTime();
  }

  @RequestMapping(value = "/{id}/metadata/save", method = RequestMethod.POST)
  public Long saveIgDocMetadata(@PathVariable("id") String id,
      @RequestBody DocumentMetaData metaData, HttpServletRequest request,
      HttpServletResponse response)
      throws IOException, IGDocumentNotFoundException, IGDocumentException {
    IGDocument d = igDocumentService.findOne(id);
    if (d == null) {
      throw new IGDocumentNotFoundException(id);
    }
    d.setMetaData(metaData);
    igDocumentService.save(d);
    return d.getDateUpdated().getTime();
  }

  @RequestMapping(value = "/{id}/profile/metadata/save", method = RequestMethod.POST)
  public Long saveProfileMetadata(@PathVariable("id") String id,
      @RequestBody ProfileMetaData metaData, HttpServletRequest request,
      HttpServletResponse response)
      throws IOException, IGDocumentNotFoundException, IGDocumentException {
    IGDocument d = igDocumentService.findOne(id);
    if (d == null) {
      throw new IGDocumentNotFoundException(id);
    }
    d.getProfile().setMetaData(metaData);
    d.getProfile().setDateUpdated(DateUtils.getCurrentDate());
    igDocumentService.save(d);
    return d.getDateUpdated().getTime();
  }

  @RequestMapping(value = "/{id}/section/save", method = RequestMethod.POST)
  public Long saveSection(@PathVariable("id") String id, @RequestBody Section section,
      HttpServletRequest request, HttpServletResponse response)
      throws IOException, IGDocumentNotFoundException, IGDocumentException {
    IGDocument d = igDocumentService.findOne(id);
    if (d == null) {
      throw new IGDocumentNotFoundException(id);
    }
    Set<Section> newChildSection = d.getChildSections();
    section.setDateUpdated(DateUtils.getCurrentDate());
    newChildSection.add(section);
    d.setChildSections(newChildSection);
    igDocumentService.save(d, section.getDateUpdated());
    return d.getDateUpdated().getTime();
  }

  @RequestMapping(value = "/{id}/section/update", method = RequestMethod.POST)
  public Long updateSection(@PathVariable("id") String id, @RequestBody Section section,
      HttpServletRequest request, HttpServletResponse response)
      throws IOException, IGDocumentNotFoundException, IGDocumentException {
    IGDocument d = igDocumentService.findOne(id);
    if (d == null) {
      throw new IGDocumentNotFoundException(id);
    }
    String idSect = section.getId();
    section.setDateUpdated(new Date());
    // String msgInfraId= d.getProfile().getId();
    String conformaneId = d.getProfile().getMessages().getId();
    String dataTypesId = d.getProfile().getDatatypeLibrary().getId();
    String tableId = d.getProfile().getTableLibrary().getId();
    String segmentId = d.getProfile().getSegmentLibrary().getId();

    if (idSect.equalsIgnoreCase(conformaneId)) {
      d.getProfile().getMessages().setSectionContents(section.getSectionContents());
    } else if (idSect.equalsIgnoreCase(dataTypesId)) {
      d.getProfile().getDatatypeLibrary().setSectionContents(section.getSectionContents());
      datatypeLibraryService.save(d.getProfile().getDatatypeLibrary());
    } else if (idSect.equalsIgnoreCase(tableId)) {
      d.getProfile().getTableLibrary().setSectionContents(section.getSectionContents());
      tableLibraryService.save(d.getProfile().getTableLibrary());
    } else if (idSect.equalsIgnoreCase(segmentId)) {
      d.getProfile().getSegmentLibrary().setSectionContents(section.getSectionContents());
      segmentLibraryService.save(d.getProfile().getSegmentLibrary());
    } else {
      Section s = findSection(d, idSect);
      if (s == null)
        throw new IGDocumentException("Unknown Section");
      s.merge(section);
    }
    igDocumentService.save(d, section.getDateUpdated());
    return d.getDateUpdated().getTime();
  }

  @RequestMapping(value = "/{id}/section/{sectionId}/delete", method = RequestMethod.POST)
  public Long updateSection(@PathVariable("id") String id,
      @PathVariable("sectionId") String sectionId, HttpServletRequest request,
      HttpServletResponse response)
      throws IOException, IGDocumentNotFoundException, IGDocumentException {
    IGDocument d = igDocumentService.findOne(id);
    if (d == null) {
      throw new IGDocumentNotFoundException(id);
    }
    Section sect = findSection(d, sectionId);
    if (sect == null)
      throw new IGDocumentException("Unknown Section");
    if (d.getChildSections().contains(sect)) {
      d.getChildSections().remove(sect);
    } else {
      Section parent = findSectionParent(d, sectionId);
      if (parent == null)
        throw new IGDocumentException("Unknown Section");
      parent.getChildSections().remove(sect);
    }
    igDocumentService.save(d);
    return d.getDateUpdated().getTime();
  }

  private Section findSectionParent(IGDocument document, String sectionId) {
    for (Section section : document.getChildSections()) {
      Section s = findSectionParent(section, sectionId);
      if (s != null) {
        return s;
      }
    }
    return null;
  }

  private Section findSection(IGDocument document, String sectionId) {

    for (Section section : document.getChildSections()) {
      Section s = findSection(section, sectionId);
      if (s != null) {
        return s;
      }
    }
    return null;
  }

  private Section findSection(Section parent, String sectionId) {
    if (parent.getId().equals(sectionId))
      return parent;
    if (parent.getChildSections() != null)
      for (Section child : parent.getChildSections()) {

        Section section = child;
        Section f = findSection(section, sectionId);
        if (f != null) {
          return f;
        }
      }

    return null;
  }

  private Section findSectionParent(Section parent, String sectionId) {
    if (parent.getChildSections() != null)
      for (Object child : parent.getChildSections()) {
        if (child instanceof Section) {
          Section section = (Section) child;
          if (section.getId().equals(sectionId)) {
            return parent;
          }
          Section f = findSectionParent(section, sectionId);
          if (f != null) {
            return section;
          }
        }
      }
    return null;
  }

  private String updateFileName(String str) {
    return str.replaceAll(" ", "-").replaceAll("\\*", "-").replaceAll("\"", "-")
        .replaceAll(":", "-").replaceAll(";", "-").replaceAll("=", "-").replaceAll(",", "-");
  }

  @RequestMapping(value = "/{id}/updateChildSections", method = RequestMethod.POST)
  public Long updateChildSections(@PathVariable("id") String id,
      @RequestBody Set<Section> childSections)
      throws IOException, IGDocumentNotFoundException, IGDocumentException {
    IGDocument d = igDocumentService.findOne(id);
    if (d == null) {
      throw new IGDocumentNotFoundException(id);
    }
    d.setChildSections(childSections);
    igDocumentService.save(d);
    return d.getDateUpdated().getTime();
  }

  @RequestMapping(value = "/{id}/reorderMessages", method = RequestMethod.POST)
  public Long reorderMessages(@PathVariable("id") String id, @RequestBody Set<MessageMap> messages)
      throws IOException, IGDocumentNotFoundException, IGDocumentException, ProfileException {

    IGDocument d = igDocumentService.findOne(id);
    if (d == null) {
      throw new IGDocumentNotFoundException(id);
    }

    Date date = DateUtils.getCurrentDate();
    Profile p = d.getProfile();
    Messages msgs = p.getMessages();
    for (Message m : msgs.getChildren()) {
      for (MessageMap x : messages) {
        if (m.getId().equals(x.getId())) {
          m.setPosition(x.getPosition());
          messageService.save(m, date);
        }
      }
    }
    List<Message> sortedList = new ArrayList<Message>();
    sortedList.addAll(msgs.getChildren());
    MessageComparator comparator = new MessageComparator();
    Collections.sort(sortedList, comparator);
    Set<Message> sortedSet = new HashSet<Message>();
    sortedSet.addAll(sortedList);
    msgs.setChildren(sortedSet);
    p.setMessages(msgs);
    profileService.save(p, date);
    d.setProfile(p);
    igDocumentService.save(d, date);
    return date.getTime();
  }


  @RequestMapping(value = "/reorderIgs", method = RequestMethod.POST)
  public List<MessageMap> reorderIGS(@RequestBody List<MessageMap> igsMap)
      throws IOException, IGDocumentNotFoundException {


    for (MessageMap ig : igsMap) {

      IGDocument d = igDocumentService.findOne(ig.getId());
      if (d == null) {
        throw new IGDocumentNotFoundException(ig.getId());
      } else {
        igDocumentService.updatePosition(ig.getId(), ig.getPosition());
      }
    }
    return igsMap;


  }

  @RequestMapping(value = "/{id}/findAndAddMessages", method = RequestMethod.POST)
  public List<Message> findAndAddMessages(@PathVariable("id") String id,
      @RequestBody List<EventWrapper> eventWrapper) throws IOException, IGDocumentNotFoundException,
      IGDocumentException, CloneNotSupportedException {

    List<Message> newMessages = new ArrayList<Message>();
    IGDocument d = igDocumentService.findOne(id);
    if (d == null) {
      throw new IGDocumentNotFoundException(id);
    }

    Profile p = d.getProfile();
    Messages msgs = p.getMessages();

    List<Message> msgsToadd = new ArrayList<Message>();
    try {
      for (EventWrapper nands : eventWrapper) {
        Message newMessage = messageService.findByStructIdAndScopeAndVersion(
            nands.getParentStructId(), nands.getScope(), nands.getHl7Version());
        Message m1 = null;
        m1 = newMessage.clone();
        m1.setId(null);
        m1.setScope(Constant.SCOPE.USER);
        String name = m1.getMessageType() + "^" + nands.getName() + "^" + m1.getStructID();
        log.debug("Message.name=" + name);
        m1.setName(name);
        int position = messageService.findMaxPosition(msgs);
        m1.setPosition(++position);
        messageRepository.save(m1);
        msgsToadd.add(m1);
        msgs.addMessage(m1);
      }
      p.setMessages(msgs);
      d.setProfile(p);
      igDocumentService.save(d);
      if (newMessages.isEmpty()) {
        throw new NotFoundException("Message not found for event=" + eventWrapper.toString());
      }
    } catch (Exception e) {
      log.error("", e);
    }

    // for(Message m : newMessages){
    //
    // }

    return msgsToadd;
  }

  @RequestMapping(value = "/{id}/addMessages", method = RequestMethod.POST)
  public Long addMessages(@PathVariable("id") String id, @RequestBody Set<String> messageIds)
      throws IOException, IGDocumentNotFoundException, IGDocumentException,
      CloneNotSupportedException {

    IGDocument d = igDocumentService.findOne(id);
    if (d == null) {
      throw new IGDocumentNotFoundException(id);
    }

    Profile p = d.getProfile();
    Messages msgs = p.getMessages();
    List<Message> newMsgs = messageService.findByIds(messageIds);
    for (Message m : newMsgs) {
      Message m1 = null;
      m1 = m.clone();
      m1.setId(null);
      m1.setScope(Constant.SCOPE.USER);
      messageRepository.save(m1);
      msgs.addMessage(m1);
    }
    p.setMessages(msgs);
    d.setProfile(p);
    try {
      profileService.save(p);
    } catch (ProfileException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    igDocumentService.save(d);
    return d.getDateUpdated().getTime();
  }

  @RequestMapping(value = "/{id}/copyMessage", method = RequestMethod.POST)
  public Message addMessage(@PathVariable("id") String id, @RequestBody String messageId)
      throws IOException, IGDocumentNotFoundException, IGDocumentException,
      CloneNotSupportedException {

    IGDocument d = igDocumentService.findOne(id);
    if (d == null) {
      throw new IGDocumentNotFoundException(id);
    }

    Profile p = d.getProfile();
    Messages msgs = p.getMessages();
    Message newMsg = messageService.findById(messageId);
    newMsg.setId(ObjectId.get().toString());
    messageService.save(newMsg);
    newMsg.setPosition(msgs.getChildren().size());
    msgs.addMessage(newMsg);

    p.setMessages(msgs);
    d.setProfile(p);
    try {
      profileService.save(p);
      igDocumentService.save(d);
    } catch (ProfileException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    //
    return newMsg;
  }



  @RequestMapping(value = "/{id}/reorderChildSections", method = RequestMethod.POST)
  public Long reorderChildSections(@PathVariable("id") String id,
      @RequestBody Set<SectionMap> sections)
      throws IOException, IGDocumentNotFoundException, IGDocumentException {
    IGDocument d = igDocumentService.findOne(id);
    if (d == null) {
      throw new IGDocumentNotFoundException(id);
    }

    for (Section s : d.getChildSections()) {
      for (SectionMap x : sections) {
        if (s.getId().equals(x.getId())) {
          s.setSectionPosition(x.getSectionPosition());
        }
      }
    }
    List<Section> sortedList = new ArrayList<Section>();
    sortedList.addAll(d.getChildSections());

    PositionComparator comparator = new PositionComparator();
    Collections.sort(sortedList, comparator);

    Set<Section> sortedSet = new HashSet<Section>();
    sortedSet.addAll(sortedList);
    d.setChildSections(sortedSet);
    igDocumentService.save(d);
    return d.getDateUpdated().getTime();
  }

  /**
   * Share multiple participants
   * 
   * @param id
   * @param participants
   * @return
   * @throws IGDocumentException
   */
  @RequestMapping(value = "/{id}/share", method = RequestMethod.POST, produces = "application/json")
  public boolean shareIgDocument(@PathVariable("id") String id, @RequestBody Set<Long> participants)
      throws IGDocumentException {
    log.info("Sharing id document with id=" + id + " with partipants=" + participants);
    try {
      User u = userService.getCurrentUser();
      Account account = accountRepository.findByTheAccountsUsername(u.getUsername());
      if (account == null)
        throw new UserAccountNotFoundException();
      IGDocument d = this.findIGDocument(id);
      if (d.getAccountId() == null || !d.getAccountId().equals(account.getId())) {
        throw new IGDocumentException(
            "You do not have the right privilege to share this IG Document");
      }
      for (Long accountId : participants) {
        d.getShareParticipantIds().add(new ShareParticipantPermission(accountId));

        // Find the user
        Account acc = accountRepository.findOne(accountId);
        // Send confirmation email
        sendShareConfirmation(d, acc, account);
      }
      igDocumentService.save(d);
      return true;
    } catch (Exception e) {
      log.error("", e);
      throw new IGDocumentException("Failed to share IG Document \n" + e.getMessage());
    }
  }

  /**
   * Unshare one participant
   * 
   * @param id
   * @param shareParticipantId
   * @return
   * @throws IGDocumentException
   */
  @RequestMapping(value = "/{id}/unshare", method = RequestMethod.POST,
      produces = "application/json")
  public boolean unshareIgDocument(@PathVariable("id") String id,
      @RequestBody Long shareParticipantId) throws IGDocumentException {
    log.info("Unsharing id document with id=" + id + " with participant=" + shareParticipantId);
    try {
      User u = userService.getCurrentUser();
      Account account = accountRepository.findByTheAccountsUsername(u.getUsername());
      if (account == null)
        throw new UserAccountNotFoundException();
      IGDocument d = this.findIGDocument(id);
      // Cannot unshare owner
      if (d.getAccountId() != null && shareParticipantId != d.getAccountId()) {
        if (d.getAccountId().equals(account.getId())
            || account.getId().equals(shareParticipantId)) {
          d.getShareParticipantIds().remove(new ShareParticipantPermission(shareParticipantId));
          // Find the user
          Account acc = accountRepository.findOne(shareParticipantId);
          // Send unshare confirmation email
          sendUnshareEmail(d, acc, account);
        } else {
          throw new IGDocumentException("You do not have the right to share this ig document");
        }
      } else {
        throw new IGDocumentException("You do not have the right to share this ig document");
      }
      igDocumentService.save(d);
      return true;
    } catch (Exception e) {
      log.error("", e);
      throw new IGDocumentException("Failed to unshare IG Document \n" + e.getMessage());
    }
  }

  private List<IGDocument> sharedIGDocument() throws IGDocumentException {
    log.info("Getting List of shared Ig Documents");
    try {
      User u = userService.getCurrentUser();
      Account account = accountRepository.findByTheAccountsUsername(u.getUsername());
      if (account == null)
        throw new UserAccountNotFoundException();
      List<IGDocument> d = igDocumentService.findSharedIgDocuments(account.getId());
      return d;
    } catch (Exception e) {
      log.error("", e);
      throw new IGDocumentException("Failed to share IG Document \n" + e.getMessage());
    }
  }

  private void sendShareConfirmation(IGDocument doc, Account target, Account source) {

    SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);

    msg.setSubject("NIST IGAMT IGDocument Shared with you.");
    msg.setTo(target.getEmail());
    msg.setText("Dear " + target.getUsername() + ", \n\n" + source.getFullName() + "("
        + source.getUsername() + ")"
        + " wants to share the following Implementation Guide Document with you: \n" + "\n Title: "
        + doc.getMetaData().getTitle() + "\n Sub Title: " + doc.getMetaData().getSubTitle()
        + "\n Description:" + doc.getMetaData().getDescription() + "\n HL7 Version:"
        + doc.getMetaData().getHl7Version()
        + "\n If you wish to accept or reject the request please go to IGAMT tool under the 'Shared Implementation Guides' tab"
        + "\n\n" + "P.S: If you need help, contact us at '" + ADMIN_EMAIL + "'");
    try {
      this.mailSender.send(msg);
    } catch (MailException ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  private void sendUnshareEmail(IGDocument doc, Account target, Account source) {

    SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);

    msg.setSubject("NIST IGAMT IGDocument unshare");
    msg.setTo(target.getEmail());
    msg.setText("Dear " + target.getUsername() + " \n\n"
        + "This is an automatic email to let you know that " + source.getFullName() + "("
        + source.getUsername()
        + ") has stopped sharing the following Implementation Guide Document\n" + "\n Title: "
        + doc.getMetaData().getTitle() + "\n Sub Title: " + doc.getMetaData().getSubTitle()
        + "\n Description:" + doc.getMetaData().getDescription() + "\n HL7 Version:"
        + doc.getMetaData().getHl7Version() + "\n\n" + "P.S: If you need help, contact us at '"
        + ADMIN_EMAIL + "'");
    try {
      this.mailSender.send(msg);
    } catch (MailException ex) {
      log.error(ex.getMessage(), ex);
    }
  }

  /**
   * Update the IG document's date
   * 
   * @param id
   * @return
   * @throws IGDocumentException
   */
  @RequestMapping(value = "/{id}/updateDate", method = RequestMethod.POST,
      produces = "application/json")
  public Long updateDate(@PathVariable("id") String id) throws IGDocumentException {
    log.info("Updating date of ig document with id=" + id);
    try {
      User u = userService.getCurrentUser();
      if (accountRepository.findByTheAccountsUsername(u.getUsername()) == null)
        throw new UserAccountNotFoundException();
      Date date = igDocumentService.updateDate(id, DateUtils.getCurrentDate());
      return date.getTime();
    } catch (Exception e) {
      log.error("", e);
      throw new IGDocumentException("Failed to update IG Document's date \n" + e.getMessage());
    }
  }

  public List<IGDocument> GetorderByposition(List<IGDocument> toOrder) {
    // List<IGDocument>
    List<IGDocument> sortedList = new ArrayList<IGDocument>();
    sortedList.addAll(toOrder);
    IgDocumentComparator comparator = new IgDocumentComparator();
    Collections.sort(sortedList, comparator);

    return sortedList;
  }

  @RequestMapping(value = "/{id}/export/gvt", method = RequestMethod.POST,
      produces = "application/json")
  public Map<String, Object> exportToGVT(@PathVariable("id") String id,
      @RequestBody Set<String> messageIds, @RequestHeader("gvt-auth") String authorization,
      HttpServletRequest request, HttpServletResponse response) throws GVTExportException {
    try {
      log.info(
          "Exporting messages to GVT from IG Document with id=" + id + ", messages=" + messageIds);
      IGDocument d = findIGDocument(id);
      InputStream content = igDocumentExport.exportAsValidationForSelectedMessages(d,
          messageIds.toArray(new String[messageIds.size()]));
      ResponseEntity<?> rsp =
          new GVTClient().send(content, GVT_URL + GVT_EXPORT_ENDPOINT, authorization);
      Map<String, Object> res = (Map<String, Object>) rsp.getBody();
      return res;
    } catch (IGDocumentNotFoundException | IOException | CloneNotSupportedException e) {
      throw new GVTExportException(e);
    }
  }


}
