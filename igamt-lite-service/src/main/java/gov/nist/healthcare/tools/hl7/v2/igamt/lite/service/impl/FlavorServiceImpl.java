/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified. Ismail Mellouli (NIST) Mar 14, 2017
 */

package gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.impl;

import java.util.HashMap;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Component;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Constant.SCOPE;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.DataModel;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Datatype;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Field;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Group;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.PathGroup;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Segment;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.SegmentRef;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.SubProfileComponentAttributes;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.DatatypeService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.FlavorService;
import gov.nist.healthcare.tools.hl7.v2.igamt.lite.service.QueryService;

@Service

public class FlavorServiceImpl implements FlavorService {

	@Autowired
	QueryService queryService;
	@Autowired
	DatatypeService datatypeService;

	@Override
	public DataModel createFlavor(String ext, DataModel dm, List<SubProfileComponentAttributes> attributes,
			List<PathGroup> pathGroups, HashMap<String, Integer> namesMap) {
		if (dm instanceof Group) {
			Group grp = (Group) dm;

			if (attributes != null && !attributes.isEmpty()) {
				for (SubProfileComponentAttributes attr : attributes) {

					if (attr.getMax() != null) {
						grp.setMax(attr.getMax());
						grp.setTemporary(true);

					}
					if (attr.getMin() != null) {
						grp.setMin(attr.getMin());
						grp.setTemporary(true);

					}
					if (attr.getUsage() != null) {
						grp.setUsage(attr.getUsage());
						grp.setTemporary(true);

					}
					if (attr.getConformanceStatements() != null && attr.getConformanceStatements().size() > 0) {
						grp.setConformanceStatements(attr.getConformanceStatements());
						grp.setTemporary(true);

					}
				}
			}
			return grp;
		} else if (dm instanceof SegmentRef) {
			SegmentRef segRef = (SegmentRef) dm;
			if (attributes != null && !attributes.isEmpty()) {
				for (SubProfileComponentAttributes attr : attributes) {
					// if (attr.getComment() != null) {
					// segRef.setComment(attr.getComment());
					// }
					if (attr.getMax() != null) {
						segRef.setMax(attr.getMax());
						segRef.setTemporary(true);

					}
					if (attr.getMin() != null) {
						segRef.setMin(attr.getMin());
						segRef.setTemporary(true);

					}
					if (attr.getUsage() != null) {
						segRef.setUsage(attr.getUsage());
						segRef.setTemporary(true);

					}
				}
			}
			if (!pathGroups.isEmpty()) {
				Segment originalSeg = queryService.getSegmentsMap().get(segRef.getRef().getId());
				try {
					Segment segmentFlavor = originalSeg.clone();
					segmentFlavor.setExt(createExtension(ext, originalSeg.getExt(), originalSeg.getId(), namesMap));
					segmentFlavor.setId(ObjectId.get().toString());
					segmentFlavor.setTemporary(true);
					segmentFlavor.setScope(SCOPE.USER);

					if (attributes != null) {
						for (SubProfileComponentAttributes attr : attributes) {
							if (attr.getConformanceStatements() != null && attr.getConformanceStatements().size() > 0) {
								segmentFlavor.setConformanceStatements(attr.getConformanceStatements());

							}
							if (attr.getDynamicMappingDefinition() != null
									&& attr.getDynamicMappingDefinition().getDynamicMappingItems().size() > 0) {
								segmentFlavor.setDynamicMappingDefinition(attr.getDynamicMappingDefinition());
							}
							if (attr.getCoConstraintsTable() != null && attr.getCoConstraintsTable().getRowSize() > 0) {
								segmentFlavor.setCoConstraintsTable(attr.getCoConstraintsTable());
							}
							if (attr.getComments() != null && !attr.getComments().isEmpty()) {
								segmentFlavor.setComments(attr.getComments());
							}
						}
					}

					queryService.getSegmentsMap().put(segmentFlavor.getId(), segmentFlavor);
					segRef.getRef().setId(segmentFlavor.getId());
					segRef.getRef().setExt(segmentFlavor.getExt());

				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				if (attributes != null) {
					for (SubProfileComponentAttributes attr : attributes) {
						if (attr.getConformanceStatements() != null && attr.getConformanceStatements().size() > 0) {
							Segment originalSeg = queryService.getSegmentsMap().get(segRef.getRef().getId());
							try {
								Segment segmentFlavor = originalSeg.clone();
								segmentFlavor.setExt(createExtension(ext, originalSeg.getExt(), originalSeg.getId(), namesMap));
								segmentFlavor.setId(ObjectId.get().toString());
								segmentFlavor.setScope(SCOPE.USER);
								segmentFlavor.setTemporary(true);
								segmentFlavor.setConformanceStatements(attr.getConformanceStatements());
								queryService.getSegmentsMap().put(segmentFlavor.getId(), segmentFlavor);
								segRef.getRef().setId(segmentFlavor.getId());
								segRef.getRef().setExt(segmentFlavor.getExt());

							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
					for (SubProfileComponentAttributes attr : attributes) {

						if (attr.getDynamicMappingDefinition() != null
								&& attr.getDynamicMappingDefinition().getDynamicMappingItems().size() > 0) {
							Segment originalSeg = queryService.getSegmentsMap().get(segRef.getRef().getId());
							try {
								Segment segmentFlavor = originalSeg.clone();
								segmentFlavor.setExt(createExtension(ext, originalSeg.getExt(), originalSeg.getId(), namesMap));
								segmentFlavor.setId(ObjectId.get().toString());
								segmentFlavor.setTemporary(true);
								segmentFlavor.setScope(SCOPE.USER);
								attr.getDynamicMappingDefinition().setTemporary(true);

								segmentFlavor.setDynamicMappingDefinition(attr.getDynamicMappingDefinition());

								// System.out.println(
								// attr.getDynamicMappingDefinition().getMappingStructure().getSegmentName());
								queryService.getSegmentsMap().put(segmentFlavor.getId(), segmentFlavor);
								segRef.getRef().setId(segmentFlavor.getId());
								segRef.getRef().setExt(segmentFlavor.getExt());

							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if (attr.getCoConstraintsTable() != null && attr.getCoConstraintsTable().getRowSize() > 0) {
							Segment originalSeg = queryService.getSegmentsMap().get(segRef.getRef().getId());
							try {
								Segment segmentFlavor = originalSeg.clone();
								segmentFlavor.setExt(createExtension(ext, originalSeg.getExt(), originalSeg.getId(), namesMap));
								segmentFlavor.setId(ObjectId.get().toString());
								segmentFlavor.setTemporary(true);
								segmentFlavor.setScope(SCOPE.USER);
								attr.getCoConstraintsTable().setTemporary(true);
								segmentFlavor.setCoConstraintsTable(attr.getCoConstraintsTable());

								queryService.getSegmentsMap().put(segmentFlavor.getId(), segmentFlavor);
								segRef.getRef().setId(segmentFlavor.getId());
								segRef.getRef().setExt(segmentFlavor.getExt());

							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if (attr.getComments() != null && !attr.getComments().isEmpty()) {
							Segment originalSeg = queryService.getSegmentsMap().get(segRef.getRef().getId());
							try {
								Segment segmentFlavor = originalSeg.clone();
								segmentFlavor.setExt(createExtension(ext, segmentFlavor.getExt(), originalSeg.getId(), namesMap));
								segmentFlavor.setId(ObjectId.get().toString());
								segmentFlavor.setTemporary(true);
								segmentFlavor.setScope(SCOPE.USER);
								segmentFlavor.setComments(attr.getComments());
								segRef.getRef().setId(segmentFlavor.getId());
								segRef.getRef().setExt(segmentFlavor.getExt());
								segRef.setTemporary(true);
								queryService.getSegmentsMap().put(segmentFlavor.getId(), segmentFlavor);

							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}

				}
			}
			return segRef;
		} else if (dm instanceof Field) {
			Field field = (Field) dm;
			System.out.println("FIELD : " + field.getName());
			if (attributes != null && !attributes.isEmpty()) {
				for (SubProfileComponentAttributes attr : attributes) {
					// if (attr.getComment() != null) {
					// field.setComment(attr.getComment());
					// }
					if (attr.getMax() != null) {
						field.setMax(attr.getMax());
						field.setTemporary(true);

					}
					if (attr.getText() != null) {
						field.setText(attr.getText());
						field.setTemporary(true);

					}
					if (attr.getMin() != null) {
						field.setMin(attr.getMin());
						field.setTemporary(true);

					}
					if (attr.getUsage() != null) {
						field.setUsage(attr.getUsage());
						field.setTemporary(true);

					}
					if (attr.getConfLength() != null) {
						field.setConfLength(attr.getConfLength());
						field.setTemporary(true);

					}
					if (attr.getMaxLength() != null) {
						field.setMaxLength(attr.getMaxLength());
						field.setTemporary(true);

					}
					if (attr.getMinLength() != null) {
						field.setMinLength(attr.getMinLength());
						field.setTemporary(true);

					}
					if (attr.getDatatype() != null) {
						if (!queryService.getDatatypesMap().containsKey(attr.getDatatype().getId())) {
							Datatype d = datatypeService.findById(attr.getDatatype().getId());
							queryService.getDatatypesMap().put(d.getId(), d);
						}
						field.setDatatype(attr.getDatatype());
						field.setTemporary(true);

					}
				}
			}
			if (!pathGroups.isEmpty()) {
				Datatype originalDt = queryService.getDatatypesMap().get(field.getDatatype().getId());
				try {
					Datatype datatypeFlavor = originalDt.clone();
					datatypeFlavor.setExt(createExtension(ext, originalDt.getExt(), originalDt.getId(), namesMap));
					datatypeFlavor.setId(ObjectId.get().toString());
					datatypeFlavor.setTemporary(true);
					datatypeFlavor.setScope(SCOPE.USER);
					field.getDatatype().setId(datatypeFlavor.getId());
					field.getDatatype().setExt(datatypeFlavor.getExt());
					field.setTemporary(true);
					queryService.getDatatypesMap().put(datatypeFlavor.getId(), datatypeFlavor);

				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return field;

		} else if (dm instanceof Component) {

			Component component = (Component) dm;
			System.out.println("COMPONENT: " + component.getName());
			if (attributes != null && !attributes.isEmpty()) {
				for (SubProfileComponentAttributes attr : attributes) {
					// if (attr.getComment() != null) {
					// component.setComment(attr.getComment());
					// }
					if (attr.getUsage() != null) {
						component.setUsage(attr.getUsage());
						component.setTemporary(true);

					}
					if (attr.getConfLength() != null) {
						component.setConfLength(attr.getConfLength());
						component.setTemporary(true);

					}
					if (attr.getText() != null) {
						component.setText(attr.getText());
						component.setTemporary(true);

					}
					if (attr.getMaxLength() != null) {
						component.setMaxLength(attr.getMaxLength());
						component.setTemporary(true);

					}
					if (attr.getMinLength() != null) {
						component.setMinLength(attr.getMinLength());
						component.setTemporary(true);

					}
					if (attr.getDatatype() != null) {
						if (!queryService.getDatatypesMap().containsKey(attr.getDatatype().getId())) {
							Datatype d = datatypeService.findById(attr.getDatatype().getId());
							queryService.getDatatypesMap().put(d.getId(), d);
						}
						component.setDatatype(attr.getDatatype());
						component.setTemporary(true);

					}
				}
			}
			if (!pathGroups.isEmpty()) {
				Datatype originalDt = queryService.getDatatypesMap().get(component.getDatatype().getId());
				try {
					Datatype datatypeFlavor = originalDt.clone();
					datatypeFlavor.setExt(createExtension(ext, originalDt.getExt(), originalDt.getId(), namesMap));
					datatypeFlavor.setId(ObjectId.get().toString());
					datatypeFlavor.setTemporary(true);
					datatypeFlavor.setScope(SCOPE.USER);
					component.getDatatype().setId(datatypeFlavor.getId());
					component.getDatatype().setExt(datatypeFlavor.getExt());
					queryService.getDatatypesMap().put(datatypeFlavor.getId(), datatypeFlavor);

				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return component;

		}

		return dm;
	}

	private String createExtension(String compositeExtension, String flavorExtension, String id,
			HashMap<String, Integer> namesMap) {
		// TODO Auto-generated method stub
		String ext = "";
		if (flavorExtension != null && !flavorExtension.isEmpty()){
			ext = flavorExtension + "_" + compositeExtension + '-';
		}else{
			ext = compositeExtension + '-';
		}
		if (namesMap.containsKey(id)) {
			ext += namesMap.get(id)+1;
			namesMap.put(id, namesMap.get(id)+1);
		}else{
			ext += 1;
			namesMap.put(id, 1);
		}
		return ext;
	}

	// private boolean needsGeneration(List<SubProfileComponentAttributes>
	// attributes){
	// if(attributes!=null){
	// for (SubProfileComponentAttributes attr:attributes ){
	// if (attr.getUsage() != null) {
	// return true;
	// }
	// if (attr.getConfLength() != null) {
	// return true;
	// }
	// if (attr.getText() != null) {
	// return true;
	// }
	// if (attr.getMaxLength() != null) {
	// return true;
	// }
	// if (attr.getMinLength() != null) {
	// return true;
	// }
	// if (attr.getDatatype() != null) {
	// return true;
	// }if(attr.getMax() !=null){
	// return true;
	// }
	// if(attr.getMax() !=null){
	// return true;
	// }
	// if(attr.getCoConstraintsTable()!=null){
	// return true;
	// }
	// if(attr.getDynamicMappingDefinition() !=null){
	// return true;
	// }
	// }
	// }
	// return false;
	//
	// }
}
