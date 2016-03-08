angular
		.module('igl')
		.controller(
				'ToCCtl',
				[
						'$scope',
						'$rootScope',
						'ToCSvc',
						'ContextMenuSvc',
						'CloneDeleteSvc',
						function($scope, $rootScope, ToCSvc,
								ContextMenuSvc, CloneDeleteSvc) {
							var ctl = this;
							$scope.collapsed = [];
							$scope.yesDrop = false;
							$scope.noDrop = true;
							$scope.$watch('tocData', function(newValue,
									oldValue) {
								if (!oldValue && newValue) {
									_.each($scope.tocData, function(head) {
										$scope.collapsed[head] = false;
									});
								}
							});
							
							$scope.moved = function (index, leaf) {
								var idx = _.findLastIndex($scope.$parent.drop, function(leaf1) {
									return leaf.id === leaf1.id;
								});
							
								if (index === idx) {
									$scope.$parent.drop.splice(index + 1, 1);
								} else {
									$scope.$parent.drop.splice(index, 1);
								}
							};
							
							$scope.calcOffset = function(level) {
								return "margin-left : " + level + "em";
							};

							$scope.trackBy = function() {
								return new ObjectId().toString();
							}
							
							$scope.tocSelection = function(entry) {
								// TODO gcr: See about refactoring this to
								// eliminate the switch.
								// One could use entry.reference.type to assemble
								// the $emit string.
								// Doing so would require maintaining a sync
								// with the ProfileListController.
								entry.selected = true;
								ToCSvc.currentLeaf.selected = false;
								ToCSvc.currentLeaf = entry;
								console.log("entry=" + entry.reference.sectionTitle);
								switch (entry.type) {
								case "documentMetadata": {
									$scope.$emit('event:openDocumentMetadata',
											entry.reference);
									break;
								}
								case "profileMetadata": {
									$scope.$emit('event:openProfileMetadata',
											entry.reference);
									break;
								}
								case "message": {
									$scope.$emit('event:openMessage',
											entry.reference);
									break;
								}
								case "segment": {
									$scope.$emit('event:openSegment',
											entry.reference);
									break;
								}
								case "datatype": {
									$scope.$emit('event:openDatatype',
											entry.reference);
									break;
								}
								case "table": {
									$scope.$emit('event:openTable',
											entry.reference);
									break;
								}
								default: {
									$scope.$emit('event:openSection',
											entry.reference);
									break;
								}
								}
								return $scope.subview;
							};
							
							$scope.closedCtxSubMenu = function(node, $index) {
								var ctxMenuSelection = ContextMenuSvc.get();
								console.log("ctxMenuSelection=" + ctxMenuSelection);
								switch (ctxMenuSelection) {
								case "Add":
									console.log("Add==> node=" + node);
									break;
								case "Copy":
									console.log("Copy==> node=" + node + " node.reference.type=" + node.reference.type);
									if (node.reference.type === 'section') {
					        				CloneDeleteSvc.copySection(node);
									} else if (node.reference.type === 'segment') {
						        			CloneDeleteSvc.copySegment(node.reference);
									}  else if (node.reference.type === 'datatype') {
						        			CloneDeleteSvc.copyDatatype(node.reference);
									} else if (node.reference.type === 'table') {
										CloneDeleteSvc.copyTable(node.reference);
									} else if (node.reference.type === 'message') {
										CloneDeleteSvc.copyMessage(node.reference);
									}
									break;
								case "Delete":
									console.log("Copy==> node=" + node);
									if (node.reference.type === 'section') {
					        				CloneDeleteSvc.deleteSection(node);
									} else if (node.reference.type === 'segment') {
						        			CloneDeleteSvc.deleteSegment(node.reference);
									}  else if (node.reference.type === 'datatype') {
						        			CloneDeleteSvc.deleteDatatype(node.reference);
									} else if (node.reference.type === 'table') {
										CloneDeleteSvc.deleteValueSet(node.reference);
									} else if (node.reference.type === 'message') {
										CloneDeleteSvc.deleteMessage(node.reference);
									}
									break;
								default:
									console
											.log("Context menu defaulted with "
													+ ctxMenuSelection
													+ " Should be Add, Copy, or Delete.");
								}
								$rootScope.$broadcast('event:SetToC');	
							};
						} ])