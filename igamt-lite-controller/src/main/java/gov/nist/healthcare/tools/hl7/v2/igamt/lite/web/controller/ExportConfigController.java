/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified. Abdelghani EL OUAKILI (NIST) Feb 6, 2017
 */
package gov.nist.healthcare.tools.hl7.v2.igamt.lite.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.nist.healthcare.nht.acmgt.dto.domain.Account;
import gov.nist.healthcare.nht.acmgt.repo.AccountRepository;
import gov.nist.healthcare.nht.acmgt.service.UserService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ExportConfig;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ExportFont;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.ExportFontConfig;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.ExportConfigService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.ExportFontConfigService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.ExportFontService;

/**
 * @author Abdelghani EL Ouakili (NIST)
 *
 */
@RestController
@RequestMapping("/ExportConfiguration")
public class ExportConfigController {


  @Autowired
  private ExportConfigService exportConfigService;

  @Autowired
  private UserService userService;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private ExportFontService exportFontService;

  @Autowired
  ExportFontConfigService exportFontConfigService;

  @Autowired
  static final private Logger logger = LoggerFactory.getLogger(ExportConfigController.class);

  @RequestMapping(value = "/override", method = RequestMethod.POST, produces = "application/json")
  public ExportConfig override(@RequestBody ExportConfig exportConfig) {
    ExportConfig currentConfig;
    User u = userService.getCurrentUser();
    try {
      Account account = accountRepository.findByTheAccountsUsername(u.getUsername());
      if (null != account) {
        currentConfig =
            exportConfigService.findOneByTypeAndAccountId(exportConfig.getType(), account.getId());
        if (null != currentConfig) {
          exportConfigService.delete(currentConfig);
        }
        exportConfig.setAccountId(account.getId());
        exportConfigService.save(exportConfig);
      }
    } catch (Exception e) {
      logger.error("Unable to save the config: " + e.getMessage());
    }
    return exportConfig;
  }

  @RequestMapping(value = "/restoreDefault", method = RequestMethod.POST,
      produces = "application/json")
  public ExportConfig restoreDefault(@RequestBody ExportConfig exportConfig) {
    ExportConfig currentConfig;
    User u = userService.getCurrentUser();
    try {
      Account account = accountRepository.findByTheAccountsUsername(u.getUsername());
      if (null != account) {
        currentConfig =
            exportConfigService.findOneByTypeAndAccountId(exportConfig.getType(), account.getId());
        if (null != currentConfig) {
          exportConfigService.delete(currentConfig);
        }
      }
    } catch (Exception e) {
      logger.warn("Unable to restore the default config: " + e.getMessage());
    }
    currentConfig = ExportConfig.getBasicExportConfig(exportConfig.getType());
    return currentConfig;

  }

  @RequestMapping(value = "/findCurrent", method = RequestMethod.POST,
      produces = "application/json")
  public ExportConfig findCurrent(@RequestBody String type) {
    ExportConfig currentConfig = null;
    User u = userService.getCurrentUser();
    try {
      Account account = accountRepository.findByTheAccountsUsername(u.getUsername());
      if (null != account) {
        currentConfig = exportConfigService.findOneByTypeAndAccountId(type, account.getId());
      }
    } catch (Exception e) {
      logger.warn("Unable to find the current config: " + e.getMessage());
    }
    if (null == currentConfig) {
      currentConfig = ExportConfig.getBasicExportConfig(type);
    }
    return currentConfig;
  }


  @RequestMapping(value = "/findFonts", method = RequestMethod.POST, produces = "application/json")
  public List<ExportFont> findFonts() {
    List<ExportFont> exportFonts = exportFontService.findAll();
    return exportFonts;
  }

  @RequestMapping(value = "/getUserExportFontConfig", method = RequestMethod.POST,
      produces = "application/json")
  public ExportFontConfig getUserExportFontConfig() throws Exception {
    ExportFontConfig exportFontConfig = null;
    User u = userService.getCurrentUser();
    try {
      Account account = accountRepository.findByTheAccountsUsername(u.getUsername());
      if (null != account) {
        List<ExportFontConfig> existing =
            exportFontConfigService.findOneByAccountId(account.getId());
        if (!existing.isEmpty()) {
          exportFontConfig = existing.get(0);
        } else {
          exportFontConfig = exportFontConfigService.getDefaultExportFontConfig();
        }
      }
    } catch (Exception e) {
      logger.warn("Unable to find the current config: " + e.getMessage());
    }

    return exportFontConfig;
  }

  @RequestMapping(value = "/saveExportFontConfig", method = RequestMethod.POST,
      produces = "application/json")
  public ExportFontConfig saveExportFontConfig(@RequestBody ExportFontConfig exportFontConfig) {
    User u = userService.getCurrentUser();
    try {
      Account account = accountRepository.findByTheAccountsUsername(u.getUsername());
      if (null != account) {
        List<ExportFontConfig> existing =
            exportFontConfigService.findOneByAccountId(account.getId());
        if (!existing.isEmpty()) {
          for (ExportFontConfig exp : existing) {
            exportFontConfigService.delete(exp);
          }
        }
        exportFontConfig.setAccountId(account.getId());
        exportFontConfig.setDefaultConfig(false);
        exportFontConfig.setId(null);
        exportFontConfigService.save(exportFontConfig);



      }
    } catch (Exception e) {
      logger.warn("Unable to find the current config: " + e.getMessage());
    }
    return exportFontConfig;
  }

  @RequestMapping(value = "/restoreDefaultExportFontConfig", method = RequestMethod.POST,
      produces = "application/json")
  public ExportFontConfig restoreDefaultExportFontConfig() throws Exception {
    ExportFontConfig exportFontConfig = null;
    User u = userService.getCurrentUser();
    try {
      Account account = accountRepository.findByTheAccountsUsername(u.getUsername());
      if (null != account) {
        List<ExportFontConfig> existing =
            exportFontConfigService.findOneByAccountId(account.getId());
        if (!existing.isEmpty()) {
          for (ExportFontConfig exp : existing) {
            exportFontConfigService.delete(exp);
          }
        }
      }
    } catch (Exception e) {
      logger.warn("Unable to delete the current config: " + e.getMessage());
    }
    exportFontConfig = exportFontConfigService.getDefaultExportFontConfig();
    return exportFontConfig;
  }


}
