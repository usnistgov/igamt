package gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Transient;

public class DynamicMappingDefinition implements java.io.Serializable, Cloneable {

  /**
   * 
   */
  private static final long serialVersionUID = -2599483305053533142L;
  
  private VariesMapItem mappingStructure = new VariesMapItem();
  private List<DynamicMappingItem> dynamicMappingItems = new ArrayList<DynamicMappingItem>();
  @Transient
  protected boolean temporary =false;

  public static long getSerialversionuid() {
	return serialVersionUID;
}

public boolean isTemporary() {
	return temporary;
}

public DynamicMappingDefinition() {
    super();
  }

  public VariesMapItem getMappingStructure() {
    return mappingStructure;
  }

  public void setMappingStructure(VariesMapItem mappingStructure) {
    this.mappingStructure = mappingStructure;
  }

  public List<DynamicMappingItem> getDynamicMappingItems() {
    return dynamicMappingItems;
  }

  public void setDynamicMappingItems(List<DynamicMappingItem> dynamicMappingItems) {
    this.dynamicMappingItems = dynamicMappingItems;
  }
  
  public void addDynamicMappingItem(DynamicMappingItem item){
    if(this.checkDuplicated(item)){
      this.dynamicMappingItems.add(item);
    }
  }

  private boolean checkDuplicated(DynamicMappingItem item) {
    for(DynamicMappingItem entry:this.dynamicMappingItems){
      if(entry.getFirstReferenceValue().equals(item.getFirstReferenceValue())) {
        if(entry.getSecondReferenceValue() != null && entry.getSecondReferenceValue().equals(item.getSecondReferenceValue())) return false;
      }
    }
    return true;
  }

  @Override
  public DynamicMappingDefinition clone() throws CloneNotSupportedException {
    DynamicMappingDefinition cloned = new DynamicMappingDefinition();
    cloned.setDynamicMappingItems(new ArrayList<DynamicMappingItem>());
    for(DynamicMappingItem item:dynamicMappingItems){
      if(item != null) cloned.getDynamicMappingItems().add(item.clone()); 
    }
   
    if(mappingStructure != null) cloned.setMappingStructure(this.mappingStructure.clone());
    return cloned;
  }

public void setTemporary(boolean b) {
	// TODO Auto-generated method stub
	
}



}
