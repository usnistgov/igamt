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
package gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.test.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.DatatypeLibrary;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.DatatypeLibraryService;

/**
 * @author gcr1
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceContext.class})
public class DatatypeLibraryServiceTest {
	
	@Autowired
	DatatypeLibraryService dtlService;
	
	@Test
	public void findAllTest() {
		List<DatatypeLibrary> dtl = dtlService.findAll();
		assertNotNull(dtl);
		assertEquals(1, dtl.size());
		assertEquals(91, dtl.get(0).getChildren().size());
	}
	
	@Test
	public void findByScopeTest() {
		List<DatatypeLibrary> dtl = dtlService.findByScope(DatatypeLibrary.SCOPE.HL7STANDARD);
		assertNotNull(dtl);
		assertEquals(1, dtl.size());
		assertEquals(91, dtl.get(0).getChildren().size());
	}
	
	@Test
	public void saveTest() {
		List<DatatypeLibrary> dtls = dtlService.findByScope(DatatypeLibrary.SCOPE.HL7STANDARD);
		DatatypeLibrary dtl = dtls.get(0);
		assertNotNull(dtl);
		dtl.setId(null);
		dtl.setScope(DatatypeLibrary.SCOPE.USER);
		dtlService.apply(dtl);
	}
}