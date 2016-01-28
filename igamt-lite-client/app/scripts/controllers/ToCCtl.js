angular
		.module('igl')
		.controller(
				'ToCCtl',
				[
						'$scope',
						'$rootScope',
						'$q',
						'ToCSvc',
						'ContextMenuSvc',
						'CloneDeleteSvc',
						function($scope, $rootScope, $q, ToCSvc,
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
							$scope.moved = function (index, leaf, branch) {
								var idx = _.findLastIndex(branch, function(leaf1) {
									return leaf.id === leaf1.id;
								});
							
								if (index === idx) {
									branch.splice(index + 1, 1);
								} else {
									branch.splice(index, 1);
								}
							}
							$scope.calcOffset = function(level) {
								return "margin-left : " + level + "em";
							}

							$scope.tocSelection = function(leaf) {
								// TODO gcr: See about refactoring this to
								// eliminate the switch.
								// One could use leaf.reference.type to assemble
								// the $emit string.
								// Doing so would require maintaining a sync
								// with the ProfileListController.
								leaf.selected = true;
								ToCSvc.currentLeaf.selected = false;
								ToCSvc.currentLeaf = leaf;
								switch (leaf.parent) {
								case "3.1": {
									$scope.$emit('event:openMessage',
											leaf.reference);
									break;
								}
								case "3.2": {
									$scope.$emit('event:openSegment',
											leaf.reference);
									break;
								}
								case "3.3": {
									$scope.$emit('event:openDatatype',
											leaf.reference);
									break;
								}
								case "3.4": {
									$scope.$emit('event:openTable',
											leaf.reference);
									break;
								}
								default: {
									$scope.$emit('event:openSection',
											leaf.reference);
									break;
								}
								}
								return $scope.subview;
							};
							
							$scope.closedCtxSubMenu = function(leaf, $index) {
								var ctxMenuSelection = ContextMenuSvc.get();
								switch (ctxMenuSelection) {
								case "Add flavor":
									console.log("Add flavor==> node=" + leaf);
									if (leaf.reference.type === 'datatype') {
						        			CloneDeleteSvc.cloneDatatypeFlavor(leaf.reference);
									} else if (leaf.reference.type === 'table') {
										CloneDeleteSvc.cloneTableFlavor(leaf.reference);
									}
									break;
								case "Clone":
									console.log("Clone==> node=" + leaf);
									CloneDeleteSvc.cloneMessage(
											$rootScope.igdocument, leaf);
									$rootScope.$broadcast('event:SetToC');
									break;
								case "Delete":
									console.log("Delete==> node=" + leaf.label);
									CloneDeleteSvc.deleteMessage(
											$rootScope.igdocument, leaf.reference);
									$rootScope.$broadcast('event:SetToC');
									break;
								default:
									console
											.log("Context menu defaulted with "
													+ ctxMenuSelection
													+ " Should be Add, clone, or Delete.");
								}
							};

						} ])