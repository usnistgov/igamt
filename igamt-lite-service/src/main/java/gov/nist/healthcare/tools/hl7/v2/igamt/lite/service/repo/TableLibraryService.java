/**
 * This software was developed at the National Institute of Standards and Technology by employees
 * of the Federal Government in the course of their official duties. Pursuant to title 17 Section 105 of the
 * United States Code this software is not subject to copyright protection and is in the public domain.
 * This is an experimental system. NIST assumes no responsibility whatsoever for its use by other parties,
 * and makes no guarantees, expressed or implied, about its quality, reliability, or any other characteristic.
 * We would appreciate acknowledgement if the software is used. This software can be redistributed and/or
 * modified freely provided that any derivative works bear some notice that they are derived from it, and any
 * modified versions bear some notice that they have been modified.
 */

/**
 * 
 * @author Olivier MARIE-ROSE
 * 
 */

package gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.repo;

import java.util.Iterator;

import org.hibernate.mapping.Set;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Component;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Datatype;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Datatypes;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.tables.TableLibrary;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.repo.*;


@Service
public class TableLibraryService {

	@Autowired
	private TableLibraryRepository tableLibraryRepository;


	/**
	 * 
	 * @param p
	 * @return
	 */
	@Transactional()
	public TableLibrary save(TableLibrary t) {
		tableLibraryRepository.saveAndFlush(t);
		return t;
	}

	/**
	 * 
	 * @param id
	 */
	public void delete(Long id) {
		tableLibraryRepository.delete(id);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public TableLibrary findOne(Long id) {
		return tableLibraryRepository.findOne(id);
	}

}