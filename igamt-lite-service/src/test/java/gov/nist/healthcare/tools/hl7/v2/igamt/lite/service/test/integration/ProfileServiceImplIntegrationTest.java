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
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Datatype;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Field;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Group;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Message;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Profile;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Segment;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.SegmentRef;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.repo.ProfileRepository;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.ProfileSaveException;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.converters.ComponentWriteConverter;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.converters.FieldWriteConverter;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.converters.ProfileReadConverter;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.converters.SegmentRefWriteConverter;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.impl.ProfileSerializationImpl;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.impl.ProfileServiceImpl;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.util.ProfilePropertySaveError;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ProfileServiceImplIntegrationTest {

	@Autowired
	ProfileRepository profileRepository;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	ProfileServiceImpl profileService;

	@BeforeClass
	public static void setup() {
		try {
			Properties p = new Properties();
			InputStream log4jFile = ProfileServiceImplIntegrationTest.class
					.getResourceAsStream("/igl-test-log4j.properties");
			p.load(log4jFile);
			PropertyConfigurator.configure(p);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testApplyProfileMetaDataChanges() throws IOException {
		String p = IOUtils.toString(this.getClass().getResourceAsStream(
				"/vxuTest/Profile.xml"));
		String v = IOUtils.toString(this.getClass().getResourceAsStream(
				"/vxuTest/ValueSets_all.xml"));
		String c = IOUtils.toString(this.getClass().getResourceAsStream(
				"/vxuTest/Constraints.xml"));
		Profile p1 = new ProfileSerializationImpl().deserializeXMLToProfile(p,
				v, c);
		String jsonChanges = "\r\n\r\n {\r\n  \"profile\": {\r\n    \"edit\": [\r\n      {\r\n        \"id\": \"552014603004d0a9f09caf16\",\r\n        \"identifier\": \"ddddddddddddd\",\r\n        \"subTitle\": \"ddddddddd\",\r\n        \"orgName\": \"NISTddddd\",\r\n        \"name\": \"VXU_V04ddddd\"\r\n      }\r\n    ]\r\n  }\r\n}\r\n                ";
		try {
			p1 = profileService.apply(jsonChanges, p1);
			assertEquals("ddddddddddddd", p1.getMetaData().getIdentifier());
			assertEquals("ddddddddd", p1.getMetaData().getSubTitle());
			assertEquals("NISTddddd", p1.getMetaData().getOrgName());
			assertEquals("VXU_V04ddddd", p1.getMetaData().getName());
			// assertEquals(jsonChanges, p1.getChanges());
		} catch (ProfileSaveException e) {
			if (e.getErrors() != null && !e.getErrors().isEmpty()) {
				for (ProfilePropertySaveError error : e.getErrors()) {
					System.out.println(error);
				}
			}
		}

	}

	@Test
	public void testApplyMessageChanges() throws IOException {
		String p = IOUtils.toString(this.getClass().getResourceAsStream(
				"/vxuTest/Profile.xml"));
		String v = IOUtils.toString(this.getClass().getResourceAsStream(
				"/vxuTest/ValueSets_all.xml"));
		String c = IOUtils.toString(this.getClass().getResourceAsStream(
				"/vxuTest/Constraints.xml"));
		Profile p1 = new ProfileSerializationImpl().deserializeXMLToProfile(p,
				v, c);

		Message message = p1.getMessages().getChildren()
				.toArray(new Message[] {})[0];
		SegmentRef segmentRef = (SegmentRef) message.getChildren().get(0);
		Group group = (Group) message.getChildren().get(5);
		Segment segment = p1.getSegments().findOne(segmentRef.getRef());
		Field field = segment.getFields().get(0);
		Datatype datatype = p1.getDatatypes().getChildren()
				.toArray(new Datatype[] {})[0];
		String jsonChanges = "{\r\n  \"segmentRef\": {\r\n    \"edit\": [\r\n      {\r\n        \"id\": \""
				+ segmentRef.getId()
				+ "\",\r\n        \"usage\": \"X\",\r\n        \"min\": \"100\",\r\n        \"max\": \"100\"\r\n      }\r\n    ]\r\n  },\r\n  \"message\": {\r\n    \"edit\": [\r\n      {\r\n        \"id\": \""
				+ message.getId()
				+ "\",\r\n        \"identifier\": \"identifier\",\r\n        \"version\": \"version\",\r\n        \"oid\": \"oid\",\r\n        \"date\": \"today\",\r\n        \"comment\": \"comment\"\r\n      }\r\n    ]\r\n  }\r\n,  \"group\": {\r\n    \"edit\": [\r\n      {\r\n        \"id\": \""
				+ message.getChildren().get(5).getId()
				+ "\",\r\n        \"usage\": \"W\"\r\n      }\r\n    ]\r\n  }, \"segment\": {\r\n    \"edit\": [\r\n      {\r\n        \"id\": \""
				+ segment.getId()
				+ "\",\r\n        \"label\": \"label\",\r\n        \"description\": \"desc\",\r\n        \"comment\": \"comment\",\r\n        \"text2\": \"posttest\",\r\n        \"text1\": \"pretest\"\r\n      }\r\n    ]\r\n  },\r\n  \"field\": {\r\n    \"edit\": [\r\n      {\r\n        \"id\": \""
				+ field.getId()
				+ "\",\r\n        \"usage\": \"X\",\r\n        \"min\": \"10\",\r\n        \"max\": \"10\",\r\n        \"minLength\": \"10\",\r\n        \"maxLength\": \"10\",\r\n        \"confLength\": \"10\",\r\n        \"datatype\": {\r\n          \"id\": \""
				+ datatype.getId()
				+ "\"\r\n        }\r\n      }\r\n    ]\r\n  }, "
				+ "\"datatype\": {\n    \"add\": [\n      {\n        \"type\": \"datatype\",\n        \"id\": -3821077,\n        \"label\": \"HD_IZ_3821077\",\n        \"components\": [\n          {\n            \"type\": \"component\",\n            \"name\": \"Namespace ID\",\n            \"usage\": \"C\",\n            \"minLength\": 1,\n            \"maxLength\": \"20\",\n            \"confLength\": \"\",\n            \"table\": {\n              \"id\": \"552014603004d0a9f09ca2b3\"\n            },\n            \"bindingStrength\": \"\",\n            \"bindingLocation\": \"\",\n            \"datatype\": {\n              \"id\": \"552014603004d0a9f09cab27\"\n            },\n            \"position\": 1,\n            \"comment\": null,\n            \"id\": -4399911,\n            \"path\": \"MSH.3.1\"\n          },\n          {\n            \"type\": \"component\",\n            \"name\": \"Universal ID\",\n            \"usage\": \"C\",\n            \"minLength\": 1,\n            \"maxLength\": \"199\",\n            \"confLength\": \"\",\n            \"table\": null,\n            \"bindingStrength\": \"\",\n            \"bindingLocation\": \"\",\n            \"datatype\": {\n              \"id\": \"552014603004d0a9f09cab29\"\n            },\n            \"position\": 2,\n            \"comment\": null,\n            \"id\": -740343,\n            \"path\": \"MSH.3.2\"\n          },\n          {\n            \"type\": \"component\",\n            \"name\": \"Universal ID Type\",\n            \"usage\": \"C\",\n            \"minLength\": 1,\n            \"maxLength\": \"6\",\n            \"confLength\": \"\",\n            \"table\": {\n              \"id\": \"552014603004d0a9f09ca25e\"\n            },\n            \"bindingStrength\": \"\",\n            \"bindingLocation\": \"\",\n            \"datatype\": {\n              \"id\": \"552014603004d0a9f09cab2e\"\n            },\n            \"position\": 3,\n            \"comment\": null,\n            \"id\": -7086103,\n            \"path\": \"MSH.3.3\"\n          }\n        ],\n        \"name\": \"HD\",\n        \"description\": \"Hierarchic Designator\",\n        \"predicates\": [\n          {\n            \"id\": -1307454,\n            \"constraintId\": \"[HD_IZ]1[1]\",\n            \"constraintTarget\": \"1[1]\",\n            \"reference\": null,\n            \"description\": \"If HD.2 (Universal ID) is not valued.\",\n            \"assertion\": \"<Condition>\\n <NOT>\\n <Presence Path=\\\"2[1]\\\"/>\\n </NOT>\\n </Condition>\",\n            \"trueUsage\": \"R\",\n            \"falseUsage\": \"O\"\n          },\n          {\n            \"id\": -3885632,\n            \"constraintId\": \"[HD_IZ]2[1]\",\n            \"constraintTarget\": \"2[1]\",\n            \"reference\": null,\n            \"description\": \"If HD.1 (Namespace ID) is not valued.\",\n            \"assertion\": \"<Condition>\\n <NOT>\\n <Presence Path=\\\"1[1]\\\"/>\\n </NOT>\\n </Condition>\",\n            \"trueUsage\": \"R\",\n            \"falseUsage\": \"O\"\n          },\n          {\n            \"id\": -5889559,\n            \"constraintId\": \"[HD_IZ]3[1]\",\n            \"constraintTarget\": \"3[1]\",\n            \"reference\": null,\n            \"description\": \"If HD.2 (Universal ID) is valued.\",\n            \"assertion\": \"<Condition>\\n <Presence Path=\\\"2[1]\\\"/>\\n </Condition>\",\n            \"trueUsage\": \"R\",\n            \"falseUsage\": \"X\"\n          }\n        ],\n        \"conformanceStatements\": [\n          {\n            \"id\": -7578659,\n            \"constraintId\": \"IZ-5\",\n            \"constraintTarget\": \"2[1]\",\n            \"reference\": null,\n            \"description\": \"The value of HD.2 (Universal ID) SHALL be formatted with ISO-compliant OID.\",\n            \"assertion\": \"<Assertion>\\n <Format Path=\\\"2[1]\\\" Regex=\\\"[0-2](\\\\.(0|[1-9][0-9]*))*\\\"/>\\n </Assertion>\"\n          },\n          {\n            \"id\": -3467488,\n            \"constraintId\": \"IZ-6\",\n            \"constraintTarget\": \"3[1]\",\n            \"reference\": null,\n            \"description\": \"The value of HD.3 (Universal ID Type) SHALL be 'ISO'.\",\n            \"assertion\": \"<Assertion>\\n <PlainText IgnoreCase=\\\"false\\\" Path=\\\"3[1]\\\" Text=\\\"ISO\\\"/>\\n </Assertion>\"\n          }\n        ],\n        \"comment\": null,\n        \"usageNote\": null\n      }\n    ]\n  }"
				+ " }\r\n    ";
		try {
			p1 = profileService.apply(jsonChanges, p1);
			assertEquals("identifier", message.getIdentifier());
			assertEquals("version", message.getVersion());
			assertEquals("oid", message.getOid());
			assertEquals("today", message.getDate());
			assertEquals("comment", message.getComment());

			assertEquals("X", segmentRef.getUsage().toString());
			assertEquals("100", segmentRef.getMin() + "");

			assertEquals("W", group.getUsage().toString());

			assertEquals("label", segment.getLabel());
			assertEquals("comment", segment.getComment());
			assertEquals("pretest", segment.getText1());
			assertEquals("posttest", segment.getText2());
			assertEquals("desc", segment.getDescription());

			assertEquals(datatype.getId(), field.getDatatype());

			assertEquals("X", field.getUsage().value());
			assertEquals(new Integer(10), field.getMin());
			assertEquals("10", field.getMax());
			assertEquals(new Integer(10), field.getMinLength());
			assertEquals("10", field.getMaxLength());
			assertEquals("10", field.getConfLength());
		} catch (ProfileSaveException e) {
			if (e.getErrors() != null && !e.getErrors().isEmpty()) {
				for (ProfilePropertySaveError error : e.getErrors()) {
					System.out.println(error);
				}
			}
		}

		//
	}

	@Configuration
	@EnableMongoRepositories(basePackages = "gov.nist.healthcare.tools")
	@ComponentScan(basePackages = "gov.nist.healthcare.tools")
	static class ProfileTestConfiguration extends AbstractMongoConfiguration {

		@Override
		public Mongo mongo() {
			// uses fongo for in-memory tests
			return new Fongo("igl_test").getMongo();
		}

		@Override
		@Bean
		public CustomConversions customConversions() {
			List<Converter<?, ?>> converterList = new ArrayList<Converter<?, ?>>();
			converterList.add(new FieldWriteConverter());
			converterList.add(new ComponentWriteConverter());
			converterList.add(new SegmentRefWriteConverter());
			converterList.add(new ProfileReadConverter());
			return new CustomConversions(converterList);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.springframework.data.mongodb.config.AbstractMongoConfiguration#
		 * getDatabaseName()
		 */
		@Override
		protected String getDatabaseName() {
			return "igl_test";
		}

		@Override
		public String getMappingBasePackage() {
			return "gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain";
		}
	}

}