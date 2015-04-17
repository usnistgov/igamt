/**
 * Created by haffo on 2/13/15.
 */

angular.module('igl')
    .controller('SegmentListCtrl', function ($scope, $rootScope, Restangular, ngTreetableParams, $filter, $http, $modal) {
        $scope.loading = false;
        $scope.loadingSelection = false;
        $scope.readonly = false;
        $scope.saved = false;
        $scope.message = false;
        $scope.params = null;
        $scope.tmpSegments =[];
        $scope.segmentCopy = null;
        $scope.init = function () {
            $scope.loading = true;
            $scope.params = new ngTreetableParams({
                getNodes: function (parent) {
                    return parent ? parent.fields ? parent.fields: parent.datatype ? $rootScope.datatypesMap[parent.datatype.id].components:parent.children : $rootScope.segment != null ? $rootScope.segment.fields:[];
                },
                getTemplate: function (node) {
                    return 'SegmentEditTree.html';
                }
//                ,
//                options: {
//                    initialState: 'expanded'
//                }
            });


            $scope.$watch(function () {
                return $rootScope.notifySegTreeUpdate;
            }, function (changeId) {
                if(changeId != 0) {
                    $scope.params.refresh();
                }
            });
            $scope.select($rootScope.segments[0]);
            $scope.loading = false;
        };
//
        $scope.select = function (segment) {
            if(segment) {
                $scope.loadingSelection = true;
                waitingDialog.show('Loading Segment ' + segment.name + "...", {dialogSize: 'sm', progressType: 'info'});
                $rootScope.segment = segment;
                $rootScope.segment["type"] = "segment";
                if ($scope.params)
                    $scope.params.refresh();
                $scope.loadingSelection = false;
                waitingDialog.hide();
            }
        };

        $scope.reset = function () {
//            $scope.loadingSelection = true;
//            $scope.message = "Segment " + $scope.segmentCopy.label + " reset successfully";
//            angular.extend($rootScope.segment, $scope.segmentCopy);
//             $scope.loadingSelection = false;
        };

        $scope.close = function(){
            $rootScope.segment = null;
            if ($scope.params)
                $scope.params.refresh();
            $scope.loadingSelection = false;
        };

        $scope.hasChildren = function(node){
            return node && node != null && ((node.fields && node.fields.length >0 ) || (node.datatype && $rootScope.datatypesMap[node.datatype.id].components && $rootScope.datatypesMap[node.datatype.id].components.length > 0));
        };

        $scope.validateLabel = function (label, name) {
            if(label && !label.startsWith(name)){
                return false;
            }
            return true;
        };

        $scope.onDatatypeChange = function(node){
//            $rootScope.recordChange(node,'datatype');
//            $rootScope.recordChangeForEdit2('field','edit',node.id,'datatype',node.id);
            $rootScope.recordChangeForEdit2('field','edit',node.id,'datatype',node.datatype);
            $scope.refreshTree();
        };

        $scope.refreshTree = function(){
            if ($scope.params)
                $scope.params.refresh();
        };
        
        $scope.goToTable = function(table){
        	$rootScope.table = table;
            $rootScope.notifyTableTreeUpdate = new Date().getTime();
            $rootScope.selectProfileTab(4);
        };

        $scope.goToDatatype = function(datatype){
            $rootScope.datatype = datatype;
            $rootScope.selectProfileTab(3);
            $rootScope.notifyDtTreeUpdate = new Date().getTime();
        };
        
        $scope.deleteTable = function (node) {
        	node.table = null;
        	$rootScope.recordChangeForEdit2('field','edit',node.id,'table',null);
         };
         
        $scope.mapTable = function(node) {
 			var modalInstance = $modal.open({
                 templateUrl: 'TableMappingSegmentCtrl.html',
                 controller: 'TableMappingSegmentCtrl',
                 windowClass: 'app-modal-window',
                 resolve: {
                 	selectedNode: function () {
                         return node;
                     }
                 }
             });
             modalInstance.result.then(function (node) {
                 $scope.selectedNode = node;
             }, function () {
             });
 		};

		$scope.findDTByComponentId = function(componentId){
            return $rootScope.parentsMap[componentId] ? $rootScope.parentsMap[componentId].datatype: null;
		};


        $scope.isSub = function(component){
            return  component.type === 'component' && $rootScope.parentsMap[component.id].type === 'component';
        };
		

		$scope.managePredicate = function(node){
			var modalInstance = $modal.open({
                templateUrl: 'PredicateSegmentCtrl.html',
                controller: 'PredicateSegmentCtrl',
                windowClass: 'app-modal-window',
                resolve: {
                	selectedNode: function () {
                        return node;
                    }
                }
            });
            modalInstance.result.then(function (node) {
                $scope.selectedNode = node;
            }, function () {
            });
 		};
		
		$scope.manageConformanceStatement = function(node){
			var modalInstance = $modal.open({
                templateUrl: 'ConformanceStatementSegmentCtrl.html',
                controller: 'ConformanceStatementSegmentCtrl',
                windowClass: 'app-modal-window',
                resolve: {
                	selectedNode: function () {
                        return node;
                    }
                }
            });
            modalInstance.result.then(function (node) {
                $scope.selectedNode = node;
            }, function () {
            });
		};
		
        $scope.show = function(segment){
            return true;
        };
		
		$scope.countConformanceStatements = function(position){
			var count = 0;
			if($rootScope.segment != null){
				for(var i=0, len1 = $rootScope.segment.conformanceStatements.length; i < len1; i ++){
					if($rootScope.segment.conformanceStatements[i].constraintTarget.indexOf(position + '[') === 0)
						count = count + 1;
				}	
			}
			return count;
		};
		
		$scope.countPredicate = function(position){
			if($rootScope.segment != null){
				for(var i=0, len1 = $rootScope.segment.predicates.length; i < len1; i ++){
					if($rootScope.segment.predicates[i].constraintTarget.indexOf(position + '[') === 0)
						return 1;
				}
			}
			return 0;
		};
     });

angular.module('igl')
    .controller('SegmentRowCtrl', function ($scope,$filter) {
        $scope.formName = "form_"+ new Date().getTime();
    });

angular.module('igl').controller('TableMappingSegmentCtrl', function ($scope, $modalInstance, selectedNode, $rootScope) {

    $scope.selectedNode = selectedNode;
	$scope.selectedTable = null;
	if(selectedNode.table != undefined){
		$scope.selectedTable = $rootScope.tablesMap[selectedNode.table.id];	
	}
	
	$scope.selectTable = function(table){
		$scope.selectedTable = table;
	};
	
	$scope.mappingTable = function(){
		$scope.selectedNode.table = {id:$scope.selectedTable.id};
		$rootScope.recordChangeForEdit2('field','edit',$scope.selectedNode.id,'table',$scope.selectedNode.table.id);
		$scope.ok();
	};

    $scope.ok = function () {
        $modalInstance.close($scope.selectedNode);
    };

});

angular.module('igl').controller('PredicateSegmentCtrl', function ($scope, $modalInstance, selectedNode, $rootScope) {
    $scope.selectedNode = selectedNode;
	$scope.newConstraint = angular.fromJson({
		segment : '',
		field_1 : null,
		component_1 : null,
		subComponent_1 : null,
		field_2 : null,
		component_2 : null,
		subComponent_2 : null,
		verb : null,
		contraintType : null,
		value : null,
		trueUsage : null,
		falseUsage : null
	});
	$scope.newConstraint.segment = $rootScope.segment.name;
    
	$scope.deletePredicate = function(predicate){
		$rootScope.segment.predicates.splice($rootScope.segment.predicates.indexOf(predicate),1);
		$rootScope.segmentPredicates.splice($rootScope.segmentPredicates.indexOf(predicate),1);
		if(!$scope.isNewCP(predicate.id)){
			$rootScope.recordChangeForEdit2('predicate', "delete", predicate.id,'id', predicate.id);
		}
	};
	
	$scope.isNewCP = function(id){
		if(id < 0) {
			if($rootScope.changes['predicate'] !== undefined && $rootScope.changes['predicate']['add'] !== undefined) {
    			for (var i = 0; i < $rootScope.changes['predicate']['add'].length; i++) {
        			var tmp = $rootScope.changes['predicate']['add'][i];
        			if (tmp.obj.id === id) {
                        $rootScope.changes['predicate']['add'].splice(i, 1);

                        if ($rootScope.changes["predicate"]["add"] && $rootScope.changes["predicate"]["add"].length === 0) {
                            delete  $rootScope.changes["predicate"]["add"];
                        }

                        if ($rootScope.changes["predicate"] && Object.getOwnPropertyNames($rootScope.changes["predicate"]).length === 0) {
                            delete  $rootScope.changes["predicate"];
                        }


                        return true;
                   }
        		}
    		}
			return true;
		}
		return false;
	};
    
	$scope.updateField_1 = function(){
		$scope.newConstraint.component_1 = null;
		$scope.newConstraint.subComponent_1 = null;
	};
	
	$scope.updateComponent_1 = function(){
		$scope.newConstraint.subComponent_1 = null;
	};
	
	$scope.updateField_2 = function(){
		$scope.newConstraint.component_2 = null;
		$scope.newConstraint.subComponent_2 = null;
	};
	
	$scope.updateComponent_2 = function(){
		$scope.newConstraint.subComponent_2 = null;
	};
	
	
	$scope.deletePredicateByTarget = function(){
		for(var i=0, len1 = $rootScope.segment.predicates.length; i < len1; i ++){
			if($rootScope.segment.predicates[i].constraintTarget.indexOf($scope.selectedNode.position + '[') === 0){
				$scope.deletePredicate($rootScope.segment.predicates[i]);
				return true;
			}
		}
		return false;
	};
	
	$scope.updatePredicate = function() {
		$rootScope.newPredicateFakeId = $rootScope.newPredicateFakeId - 1;
		$scope.deletePredicateByTarget();
		
		var position_1 = $scope.genPosition($scope.newConstraint.segment, $scope.newConstraint.field_1, $scope.newConstraint.component_1, $scope.newConstraint.subComponent_1);
		var position_2 = $scope.genPosition($scope.newConstraint.segment, $scope.newConstraint.field_2, $scope.newConstraint.component_2, $scope.newConstraint.subComponent_2);			
		var location_1 = $scope.genLocation($scope.newConstraint.field_1, $scope.newConstraint.component_1, $scope.newConstraint.subComponent_1);
		var location_2 = $scope.genLocation($scope.newConstraint.field_2, $scope.newConstraint.component_2, $scope.newConstraint.subComponent_2);	
		
		if(position_1 != null){
			if($scope.newConstraint.contraintType === 'valued'){
				var cp = {
					id : $rootScope.newPredicateFakeId,
					constraintId : $scope.newConstraint.segment + '-' + $scope.selectedNode.position,
					constraintTarget : $scope.selectedNode.position + '[1]',
					description : 'If ' + position_1 + ' ' +  $scope.newConstraint.verb + ' ' +  $scope.newConstraint.contraintType,
					trueUsage : $scope.newConstraint.trueUsage,
					falseUsage : $scope.newConstraint.falseUsage,
					assertion : '<Presence Path=\"' + location_1 + '\"/>'
				};
				$rootScope.segment.predicates.push(cp);
				$rootScope.segmentPredicates.push(cp);
				var newCPBlock = {targetType:'segment', targetId:$rootScope.segment.id, obj:cp};
		        $rootScope.recordChangeForEdit2('predicate', "add", null,'predicate', newCPBlock)
			}else if($scope.newConstraint.contraintType === 'a literal value'){
				var cp = {
					id : $rootScope.newPredicateFakeId,
					constraintId : $scope.newConstraint.segment + '-' + $scope.selectedNode.position,
					constraintTarget : $scope.selectedNode.position + '[1]',
					description : 'If the value of ' + position_1 + ' ' +  $scope.newConstraint.verb + ' \'' + $scope.newConstraint.value + '\'.',
					trueUsage : $scope.newConstraint.trueUsage,
					falseUsage : $scope.newConstraint.falseUsage,
					assertion : '<PlainText Path=\"' + location_1 + '\" Text=\"' + $scope.newConstraint.value + '\" IgnoreCase="false"/>'
				};
				$rootScope.segment.predicates.push(cp);
				$rootScope.segmentPredicates.push(cp);
				var newCPBlock = {targetType:'segment', targetId:$rootScope.segment.id, obj:cp};
		        $rootScope.recordChangeForEdit2('predicate', "add", null,'predicate', newCPBlock)
			}else if($scope.newConstraint.contraintType === 'one of list values'){
				var cp = {
					id : $rootScope.newPredicateFakeId,
					constraintId : $scope.newConstraint.segment + '-' + $scope.selectedNode.position,
					constraintTarget : $scope.selectedNode.position + '[1]',
					description : 'If the value of ' + position_1 + ' ' +  $scope.newConstraint.verb + ' ' +  $scope.newConstraint.contraintType + ': ' + $scope.newConstraint.value + '.',
					trueUsage : $scope.newConstraint.trueUsage,
					falseUsage : $scope.newConstraint.falseUsage,
					assertion : '<StringList Path=\"' + location_1 + '\" CSV=\"' + $scope.newConstraint.value + '\"/>'
				};
				$rootScope.segment.predicates.push(cp);
				$rootScope.segmentPredicates.push(cp);
				var newCPBlock = {targetType:'segment', targetId:$rootScope.segment.id, obj:cp};
		        $rootScope.recordChangeForEdit2('predicate', "add", null,'predicate', newCPBlock)
			}else if($scope.newConstraint.contraintType === 'formatted value'){
				var cp = {
					id : $rootScope.newPredicateFakeId,
					constraintId : $scope.newConstraint.segment + '-' + $scope.selectedNode.position,
					constraintTarget : $scope.selectedNode.position + '[1]',
					description : 'If the value of ' + position_1 + ' ' +  $scope.newConstraint.verb + ' valid in format: \'' + $scope.newConstraint.value + '\'.',
					trueUsage : $scope.newConstraint.trueUsage,
					falseUsage : $scope.newConstraint.falseUsage,
					assertion : '<Format Path=\"'+ location_1 + '\" Regex=\"' + $rootScope.genRegex($scope.newConstraint.value) + '\"/>'
				};
				$rootScope.segment.predicates.push(cp);
				$rootScope.segmentPredicates.push(cp);
				var newCPBlock = {targetType:'segment', targetId:$rootScope.segment.id, obj:cp};
		        $rootScope.recordChangeForEdit2('predicate', "add", null,'predicate', newCPBlock)
			}else if($scope.newConstraint.contraintType === 'identical to the another node'){
				var cp = {
					id : $rootScope.newPredicateFakeId,
					constraintId : $scope.newConstraint.segment + '-' + $scope.selectedNode.position,
					constraintTarget : $scope.selectedNode.position + '[1]',
					description : 'If the value of ' + position_1 + ' ' +  $scope.newConstraint.verb + ' identical to the value of ' + position_2 + '.',
					trueUsage : $scope.newConstraint.trueUsage,
					falseUsage : $scope.newConstraint.falseUsage,
					assertion : '<PathValue Path1=\"' + location_1 + '\" Operator="EQ" Path2=\"' + location_2 + '\"/>'
				};
				$rootScope.segment.predicates.push(cp);
				$rootScope.segmentPredicates.push(cp);
				var newCPBlock = {targetType:'segment', targetId:$rootScope.segment.id, obj:cp};
		        $rootScope.recordChangeForEdit2('predicate', "add", null,'predicate', newCPBlock)
			}
		}
	};
    
	 $scope.genPosition = function(segment, field, component, subComponent){
     	var position = null;
     	if(field != null && component == null && subComponent == null){
				position = segment + '-' + field.position;
			}else if(field != null && component != null && subComponent == null){
				position = segment + '-' + field.position + '.' + component.position;
			}else if(field != null && component != null && subComponent != null){
				position = segment + '-' + field.position + '.' + component.position + '.' + subComponent.position;
			}
     	
     	return position;
     };
     
     $scope.genLocation = function(field, component, subComponent){
     	var location = null;
     	if(field != null && component == null && subComponent == null){
				location = field.position + '[1]';
			}else if(field != null && component != null && subComponent == null){
				location = field.position + '[1].' + component.position + '[1]';
			}else if(field != null && component != null && subComponent != null){
				location = field.position + '[1].' + component.position + '[1].' + subComponent.position + '[1]';
			}
     	
     	return location;
     };
	
    $scope.ok = function () {
        $modalInstance.close($scope.selectedNode);
    };

});

angular.module('igl').controller('ConformanceStatementSegmentCtrl', function ($scope, $modalInstance, selectedNode, $rootScope) {
    $scope.selectedNode = selectedNode;
    
    $scope.newConstraint = angular.fromJson({
		segment : '',
		field_1 : null,
		component_1 : null,
		subComponent_1 : null,
		field_2 : null,
		component_2 : null,
		subComponent_2 : null,
		verb : null,
		constraintId : null,
		contraintType : null,
		value: null
	});
	$scope.newConstraint.segment = $rootScope.segment.name;
	
	$scope.deleteConformanceStatement = function(conformanceStatement){
		$rootScope.segment.conformanceStatements.splice($rootScope.segment.conformanceStatements.indexOf(conformanceStatement),1);
		$rootScope.segmentConformanceStatements.splice($rootScope.segmentConformanceStatements.indexOf(conformanceStatement),1);
		if(!$scope.isNewCS(conformanceStatement.id)){
			$rootScope.recordChangeForEdit2('conformanceStatement', "delete", conformanceStatement.id,'id', conformanceStatement.id);
		}
	};
	
	
	$scope.isNewCS = function(id){
		if(id < 0){
			if($rootScope.changes['conformanceStatement'] !== undefined && $rootScope.changes['conformanceStatement']['add'] !== undefined) {
    			for (var i = 0; i < $rootScope.changes['conformanceStatement']['add'].length; i++) {
        			var tmp = $rootScope.changes['conformanceStatement']['add'][i];
        			if (tmp.obj.id === id) {
                        $rootScope.changes['conformanceStatement']['add'].splice(i, 1);
                        if ($rootScope.changes["conformanceStatement"]["add"] && $rootScope.changes["conformanceStatement"]["add"].length === 0) {
                            delete  $rootScope.changes["conformanceStatement"]["add"];
                        }

                        if ($rootScope.changes["conformanceStatement"] && Object.getOwnPropertyNames($rootScope.changes["conformanceStatement"]).length === 0) {
                            delete  $rootScope.changes["conformanceStatement"];
                        }
                        return true;
                   }

        		}
    		}
			return true;
		}
		return false;
	};
    
	$scope.updateField_1 = function(){
		$scope.newConstraint.component_1 = null;
		$scope.newConstraint.subComponent_1 = null;
	};
	
	$scope.updateComponent_1 = function(){
		$scope.newConstraint.subComponent_1 = null;
	};
	
	$scope.updateField_2 = function(){
		$scope.newConstraint.component_2 = null;
		$scope.newConstraint.subComponent_2 = null;
	};
	
	$scope.updateComponent_2 = function(){
		$scope.newConstraint.subComponent_2 = null;
	};
    
	$scope.genPosition = function(segment, field, component, subComponent){
     	var position = null;
     	if(field != null && component == null && subComponent == null){
				position = segment + '-' + field.position;
			}else if(field != null && component != null && subComponent == null){
				position = segment + '-' + field.position + '.' + component.position;
			}else if(field != null && component != null && subComponent != null){
				position = segment + '-' + field.position + '.' + component.position + '.' + subComponent.position;
			}
     	
     	return position;
     };
     
    $scope.genLocation = function(field, component, subComponent){
     	var location = null;
     	if(field != null && component == null && subComponent == null){
				location = field.position + '[1]';
			}else if(field != null && component != null && subComponent == null){
				location = field.position + '[1].' + component.position + '[1]';
			}else if(field != null && component != null && subComponent != null){
				location = field.position + '[1].' + component.position + '[1].' + subComponent.position + '[1]';
			}
     	
     	return location;
    };
    
    $scope.addConformanceStatement = function() {
		$rootScope.newConformanceStatementFakeId = $rootScope.newConformanceStatementFakeId - 1;
		
		var position_1 = $scope.genPosition($scope.newConstraint.segment, $scope.newConstraint.field_1, $scope.newConstraint.component_1, $scope.newConstraint.subComponent_1);
		var position_2 = $scope.genPosition($scope.newConstraint.segment, $scope.newConstraint.field_2, $scope.newConstraint.component_2, $scope.newConstraint.subComponent_2);			
		var location_1 = $scope.genLocation($scope.newConstraint.field_1, $scope.newConstraint.component_1, $scope.newConstraint.subComponent_1);
		var location_2 = $scope.genLocation($scope.newConstraint.field_2, $scope.newConstraint.component_2, $scope.newConstraint.subComponent_2);	
		
		
		if(position_1 != null){
			if($scope.newConstraint.contraintType === 'valued'){
				var cs = {
					id : $rootScope.newConformanceStatementFakeId,
					constraintId : $scope.newConstraint.constraintId,
					constraintTarget : $scope.selectedNode.position + '[1]',
					description : position_1 + ' ' +  $scope.newConstraint.verb + ' ' +  $scope.newConstraint.contraintType + '.',
					assertion : '<Presence Path=\"' + location_1 + '\"/>'
				};
				$rootScope.segment.conformanceStatements.push(cs);
				$rootScope.segmentConformanceStatements.push(cs);
				var newCSBlock = {targetType:'segment', targetId:$rootScope.segment.id, obj:cs};
		        $rootScope.recordChangeForEdit2('conformanceStatement', "add", null,'conformanceStatement', newCSBlock);
			}else if($scope.newConstraint.contraintType === 'a literal value'){
				var cs = {
					id : $rootScope.newConformanceStatementFakeId,
					constraintId : $scope.newConstraint.constraintId,
					constraintTarget : $scope.selectedNode.position + '[1]',
					description : 'The value of ' + position_1 + ' ' +  $scope.newConstraint.verb + ' \'' + $scope.newConstraint.value + '\'.',
					assertion : '<PlainText Path=\"' + location_1 + '\" Text=\"' + $scope.newConstraint.value + '\" IgnoreCase="false"/>'
				};
				$rootScope.segment.conformanceStatements.push(cs);
				$rootScope.segmentConformanceStatements.push(cs);
				var newCSBlock = {targetType:'segment', targetId:$rootScope.segment.id, obj:cs};
		        $rootScope.recordChangeForEdit2('conformanceStatement', "add", null,'conformanceStatement', newCSBlock);
			}else if($scope.newConstraint.contraintType === 'one of list values'){
				var cs = {
					id : $rootScope.newConformanceStatementFakeId,
					constraintId : $scope.newConstraint.constraintId,
					constraintTarget : $scope.selectedNode.position + '[1]',
					description : 'The value of ' + position_1 + ' ' +  $scope.newConstraint.verb + ' ' +  $scope.newConstraint.contraintType + ': ' + $scope.newConstraint.value + '.',
					assertion : '<StringList Path=\"' + location_1 + '\" CSV=\"' + $scope.newConstraint.value + '\"/>'
				};
				$rootScope.segment.conformanceStatements.push(cs);
				$rootScope.segmentConformanceStatements.push(cs);
				var newCSBlock = {targetType:'segment', targetId:$rootScope.segment.id, obj:cs};
		        $rootScope.recordChangeForEdit2('conformanceStatement', "add", null,'conformanceStatement', newCSBlock);
			}else if($scope.newConstraint.contraintType === 'formatted value'){
				var cs = {
					id : $rootScope.newConformanceStatementFakeId,
					constraintId : $scope.newConstraint.constraintId,
					constraintTarget : $scope.selectedNode.position + '[1]',
					description : 'The value of ' + position_1 + ' ' +  $scope.newConstraint.verb + ' valid in format: \'' + $scope.newConstraint.value + '\'.',
					assertion : '<Format Path=\"'+ location_1 + '\" Regex=\"' + $rootScope.genRegex($scope.newConstraint.value) + '\"/>'
				};
				$rootScope.segment.conformanceStatements.push(cs);
				$rootScope.segmentConformanceStatements.push(cs);
				var newCSBlock = {targetType:'segment', targetId:$rootScope.segment.id, obj:cs};
		        $rootScope.recordChangeForEdit2('conformanceStatement', "add", null,'conformanceStatement', newCSBlock);
			}else if($scope.newConstraint.contraintType === 'identical to the another node'){
				var cs = {
					id : $rootScope.newConformanceStatementFakeId,
					constraintId : $scope.newConstraint.constraintId,
					constraintTarget : $scope.selectedNode.position + '[1]',
					description : 'The value of ' + position_1 + ' ' +  $scope.newConstraint.verb + ' identical to the value of ' + position_2 + '.',
					assertion : '<PathValue Path1=\"' + location_1 + '\" Operator="EQ" Path2=\"' + location_2 + '\"/>'
				};
				$rootScope.segment.conformanceStatements.push(cs);
				$rootScope.segmentConformanceStatements.push(cs);
				var newCSBlock = {targetType:'segment', targetId:$rootScope.segment.id, obj:cs};
		        $rootScope.recordChangeForEdit2('conformanceStatement', "add", null,'conformanceStatement', newCSBlock);
			}
		}
	};
	
    $scope.ok = function () {
        $modalInstance.close($scope.selectedNode);
    };

});
