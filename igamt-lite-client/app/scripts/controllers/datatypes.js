/**
 * Created by haffo on 2/13/15.
 */
angular.module('igl')
    .controller('DatatypeListCtrl', function($scope, $rootScope, Restangular, ngTreetableParams, $filter, $http, $q, $modal, $timeout, CloneDeleteSvc, ViewSettings, DatatypeService, ComponentService, MastermapSvc, FilteringSvc, DatatypeLibrarySvc, TableLibrarySvc, MessageService, TableService, blockUI, SegmentService, VersionAndUseService, CompareService, ValidationService) {
        $scope.accordStatus = {
            isCustomHeaderOpen: false,
            isFirstOpen: true,
            isSecondOpen: true,
            isThirdOpen: true,
            isFirstDisabled: false
        };
        $scope.tabStatus = {
            active: 1
        };
        $scope.availbleVersionOfDt = [];
        $scope.editableDT = '';
        $scope.editableVS = '';
        $scope.readonly = false;
        $scope.saved = false;
        $scope.message = false;
        $scope.datatypeCopy = null;
        $scope.viewSettings = ViewSettings;
        $scope.selectedChildren = [];
        $scope.saving = false;
        $scope.init = function() {

            $scope.accordStatus = {
                isCustomHeaderOpen: false,
                isFirstOpen: true,
                isSecondOpen: false,
                isThirdOpen: false,
                isFirstDisabled: false
            };

            $scope.tabStatus = {
                active: 1
            };

        };

        $scope.deleteConformanceStatementFromList = function(c) {
            $rootScope.datatype.conformanceStatements.splice($rootScope.datatype.conformanceStatements.indexOf(c), 1);

            $scope.setDirty();
        };

        $scope.deletePredicateFromList = function(p) {
            $rootScope.datatype.predicates.splice($rootScope.datatype.predicates.indexOf(p), 1);

            $scope.setDirty();
        };
        $scope.validateDatatype = function() {
            console.log($rootScope.datatype);
            ValidationService.validatedatatype($rootScope.datatype, $rootScope.igdocument.profile.metaData.hl7Version).then(function(result) {
                $rootScope.validationMap = {};
                $rootScope.childValidationMap = {};
                $rootScope.showDtErrorNotification = true;
                $rootScope.validationResult = result;
                console.log($rootScope.validationResult);
                $rootScope.buildValidationMap($rootScope.validationResult);
                console.log($rootScope.validationMap);
                console.log($rootScope.childValidationMap);

            });
        };
         $scope.isDatatypeValidated = function() {
        if ($rootScope.datatype && ($rootScope.validationResult.targetId === $rootScope.datatype.id || $rootScope.childValidationMap[$rootScope.datatype.id])) {
            return true;
        } else {
            return false;
        }
    };
        $scope.setErrorNotification = function() {
            $rootScope.showDtErrorNotification = !$rootScope.showDtErrorNotification;
        };




        $scope.changeDatatypeLink = function(datatypeLink) {
            datatypeLink.isChanged = true;

            var t = $rootScope.datatypesMap[datatypeLink.id];

            if (t == null) {
                datatypeLink.name = null;
                datatypeLink.ext = null;
                datatypeLink.label = null;
            } else {
                datatypeLink.name = t.name;
                datatypeLink.ext = t.ext;
                datatypeLink.label = t.label;
            }
            console.log(datatypeLink);
        };

        $scope.dtmSliderOptions = {
            ceil: 7,
            floor: 0,
            showSelectionBar: true,
            onChange: function(id) {
                $scope.setDirty();
            },
            showTicks: true,
            getTickColor: function(value) {
                if (value < 3)
                    return 'red';
                if (value < 6)
                    return 'orange';
                if (value < 8)
                    return 'yellow';
                return '#2AE02A';
            }
        };

        $scope.refreshSlider = function() {


            setTimeout(function() {
                $scope.$broadcast('reCalcViewDimensions');
                console.log("refreshed Slider!!");
            }, 1000);
        };

        $scope.deleteComponent = function(componentToDelete, datatype) {
            var modalInstance = $modal.open({
                templateUrl: 'DeleteComponent.html',
                controller: 'DeleteComponentCtrl',
                size: 'md',
                resolve: {
                    componentToDelete: function() {
                        return componentToDelete;
                    },
                    datatype: function() {
                        return datatype;
                    }


                }
            });
            modalInstance.result.then(function() {

                $scope.setDirty();
                try {
                    if ($scope.datatypesParams)
                        $scope.datatypesParams.refresh();
                } catch (e) {

                }
            });
        };
        $scope.testCall = function() {
            console.log($rootScope.references);
        };

        $scope.deletePredicate = function(position, datatype) {
            var modalInstance = $modal.open({
                templateUrl: 'DeleteDatatypePredicate.html',
                controller: 'DeleteDatatypePredicateCtrl',
                size: 'md',
                resolve: {
                    position: function() {
                        return position;
                    },
                    datatype: function() {
                        return datatype;
                    }
                }
            });
            modalInstance.result.then(function() {
                $scope.setDirty();
            });
        };

        $scope.openPredicateDialog = function(node) {
            if (node.usage == 'C') $scope.managePredicate(node);
        };
        $scope.alerts = [
            { type: 'warning', msg: ' Warning: This Datatype is being deprecated, there are new versions availables' },

        ];



        $scope.closeAlert = function(index) {
            $scope.alerts.splice(index, 1);
        };

        $scope.OtoX = function(message) {
            console.log(message);
            var modalInstance = $modal.open({
                templateUrl: 'OtoX.html',
                controller: 'OtoXCtrl',
                size: 'md',
                resolve: {
                    message: function() {
                        return message;
                    }
                }
            });
            modalInstance.result.then(function() {
                $scope.setDirty();
                try {
                    if ($scope.datatypesParams)
                        $scope.datatypesParams.refresh();
                } catch (e) {

                }
            });
        };

        $scope.getAllVersionsOfDT = function(id) {
            $scope.checked = {};
            var ancestors = [];
            if (!$rootScope.versionAndUseMap[id]) {
                return "";
            }
            if ($rootScope.versionAndUseMap[id].ancestors && $rootScope.versionAndUseMap[id].ancestors.length > 0) {
                var ancestors = $rootScope.versionAndUseMap[id].ancestors;

            }
            ancestors.push($rootScope.versionAndUseMap[id].id);
            var derived = $rootScope.versionAndUseMap[id].derived;
            angular.forEach(ancestors, function(ancestor) {
                derived.push(ancestor);
            });
            var all = derived;
            console.log(all);
            VersionAndUseService.findAllByIds(all).then(function(result) {
                console.log("==========Adding Datatypes from their IDS============");
                //$rootScope.datatypes = result;
                console.log(result);
                $scope.availbleVersionOfDt = result;
                if ($scope.dynamicDt_Evolution) {

                }

            }, function(error) {
                $rootScope.msg().text = "DatatypesLoadFailed";
                $rootScope.msg().type = "danger";
                $rootScope.msg().show = true;
                delay.reject(false);

            });

        }

        $scope.dynamicDt_Evolution = new ngTreetableParams({
            getNodes: function(parent) {
                if ($scope.dataList !== undefined) {

                    //return parent ? parent.fields : $scope.test;
                    if (parent) {
                        if (parent.fields) {
                            return parent.fields;
                        } else if (parent.components) {
                            return parent.components;
                        } else if (parent.segments) {
                            return parent.segments;
                        } else if (parent.codes) {
                            return parent.codes;
                        }

                    } else {
                        return $scope.dataList;
                    }

                }
            },
            getTemplate: function(node) {
                return 'tree_node';
            }
        });
        $scope.compareWithCurrent = function(id) {
            $scope.checked = id;
            $rootScope.clearChanges();
            $scope.cleanState();

            DatatypeService.getOne(id).then(function(result) {
                $scope.dtChanged = false;
                $scope.vsTemplate = false;
                console.log("========dddddd");
                console.log($rootScope.datatype);
                console.log(result);
                $scope.dataList = CompareService.cmpDatatype(JSON.stringify($rootScope.datatype), JSON.stringify(result), [], [], [], []);
                console.log("$scope.dataList");
                console.log($scope.dataList);
                $scope.hideEvolution = false;
                $rootScope.clearChanges();
                $scope.cleanState();
                $scope.loadingSelection = false;
                if ($scope.dynamicDt_Evolution) {
                    $scope.dynamicDt_Evolution.refresh();
                }
            });
        };

        $scope.editableComp = '';
        $scope.editComponent = function(component) {
            $scope.editableComp = component.id;
            $scope.compName = component.name;

        };

        $scope.backComp = function() {
            $scope.editableComp = '';
        };
        $scope.applyComp = function(datatype, component, name, position) {
            blockUI.start();
            $scope.editableComp = '';
            if (component) {
                component.name = name;


            }
            if (position) {
                MessageService.updatePosition(datatype.components, component.position - 1, position - 1);
            }
            $scope.setDirty();

            if ($scope.datatypesParams)
                $scope.datatypesParams.refresh();
            $scope.Posselected = false;
            blockUI.stop();

        };
        $scope.selectPos = function() {

            $scope.Posselected = true;
        };





        $scope.selectDT = function(field, datatype) {
            if (datatype) {
                $scope.DTselected = true;
                blockUI.start();
                field.datatype.ext = JSON.parse(datatype).ext;
                field.datatype.id = JSON.parse(datatype).id;
                field.datatype.label = JSON.parse(datatype).label;
                field.datatype.name = JSON.parse(datatype).name;
                console.log(field);
                $scope.setDirty();
                // $rootScope.processElement(field);

                if ($scope.datatypesParams)
                    $scope.datatypesParams.refresh();
                $scope.editableDT = '';
                $scope.DTselected = false;
                blockUI.stop();
            } else {
                $scope.otherDT(field);
            }



        };
        $scope.otherDT = function(field) {
            var modalInstance = $modal.open({
                templateUrl: 'otherDTModal.html',
                controller: 'otherDTCtrl',
                windowClass: 'edit-VS-modal',
                resolve: {

                    datatypes: function() {
                        return $rootScope.datatypes;
                    },

                    field: function() {
                        return field;
                    }

                }
            });
            modalInstance.result.then(function(field) {
                $scope.setDirty();
                $scope.editableDT = '';
                if ($scope.datatypesParams) {
                    $scope.datatypesParams.refresh();
                }
            });

        };
        $scope.redirectSeg = function(segmentRef) {
            SegmentService.get(segmentRef.id).then(function(segment) {
                var modalInstance = $modal.open({
                    templateUrl: 'redirectCtrl.html',
                    controller: 'redirectCtrl',
                    size: 'md',
                    resolve: {
                        destination: function() {
                            return segment;
                        }
                    }



                });
                modalInstance.result.then(function() {
                    $rootScope.editSeg(segment);
                });



            });
        };
        $scope.redirectDT = function(datatype) {
            console.log(datatype);
            DatatypeService.getOne(datatype.id).then(function(datatype) {
                var modalInstance = $modal.open({
                    templateUrl: 'redirectCtrl.html',
                    controller: 'redirectCtrl',
                    size: 'md',
                    resolve: {
                        destination: function() {
                            return datatype;
                        }
                    }



                });
                modalInstance.result.then(function() {
                    $rootScope.editDatatype(datatype);
                });

            });
        };


        $scope.editDT = function(field) {
            $scope.editableDT = field.id;

            $scope.results = [];
            angular.forEach($rootScope.datatypeLibrary.children, function(dtLink) {
                if (dtLink.name && dtLink.name === field.datatype.name) {

                    $scope.results.push(dtLink);
                }
            });
        };



        $scope.backDT = function() {
            $scope.editableDT = '';
        };

        $scope.getTableLabel = function(tableLink) {
            var table = $rootScope.tablesMap[tableLink.id];
            console.log("TABLE FOUND");
            if (table && table.bindingIdentifier) {
                return $scope.getLabel(table.bindingIdentifier, table.ext);
            }
            return "";
        };
        $scope.getLabel = function(name, ext) {
            var label = name;
            if (ext && ext !== null && ext !== "") {
                label = label + "_" + ext;
            }
            return label;
        };

        $scope.editVSModal = function(component) {
            var modalInstance = $modal.open({
                templateUrl: 'editVSModal.html',
                controller: 'EditVSCtrl',
                windowClass: 'edit-VS-modal',
                resolve: {

                    valueSets: function() {
                        return $rootScope.tables;
                    },

                    field: function() {
                        return component;
                    }

                }
            });
            modalInstance.result.then(function(datatype) {
                $scope.setDirty();
                if ($scope.segmentsParams) {
                    $scope.segmentsParams.refresh();
                }
            });

        };

        $scope.editVS = function(field) {
            $scope.editableVS = field.id;
            if (field.table !== null) {
                $scope.VSselected = true;
                $scope.selectedValueSet = field.table;
                console.log($scope.selectedValueSet);

            } else {
                $scope.VSselected = false;

            }
        };
        $scope.backVS = function() {
            $scope.editableVS = '';
        };

        $scope.selectVS = function(field, valueSet) {
            $scope.selectedValueSet = valueSet;
            $scope.VSselected = true;
            $scope.editableVS = '';
            if (field.table === null) {
                field.table = {
                    id: '',
                    bindingIdentifier: ''

                };
                console.log(field);

            }

            field.table.id = $scope.selectedValueSet.id;
            field.table.bindingIdentifier = $scope.selectedValueSet.bindingIdentifier;
            $scope.setDirty();
            $scope.VSselected = false;



        };
     
        $scope.ContainUnpublished = function(element) {

            if (element && element.type && element.type === "datatype") {

                angular.forEach(element.components, function(component) {
                    component.location = element.name + "_" + element.ext + "." + component.position
                    $scope.ContainUnpublished(component);
                });


            } else if (element && element.type && element.type === "component") {

                if (element.tables && element.tables != null) {
                    angular.forEach(element.tables, function(table) {
                        if ($rootScope.tablesMap[table.id] && $rootScope.tablesMap[table.id]) {
                            if ($rootScope.tablesMap[table.id].scope !== "HL7STANDARD" && $rootScope.tablesMap[table.id].status !== "PUBLISHED") {
                                $scope.containUnpublished = true;
                                $scope.unpublishedTables.push({ table: table, location: element.location });
                                console.log($scope.unpublishedTables);
                            }
                        }
                    });
                }
                if (element.datatype !== null || element.datatype !== undefined) {


                    if ($rootScope.datatypesMap[element.datatype.id] && $rootScope.datatypesMap[element.datatype.id]) {
                        if ($rootScope.datatypesMap[element.datatype.id].status !== "PUBLISHED" && $rootScope.datatypesMap[element.datatype.id].scope !== "HL7STANDARD") {
                            console.log("Found Unpublished");
                            console.log($scope.containUnpublished);
                            $scope.containUnpublished = true;
                            $scope.unpublishedDatatypes.push({ datatype: element.datatype, location: element.location });
                        }
                    }


                }
            }
        };

        $scope.confirmPublish = function(datatypeCopy) {
            var modalInstance = $modal.open({
                templateUrl: 'ConfirmDatatypePublishCtl.html',
                controller: 'ConfirmDatatypePublishCtl',
                resolve: {
                    datatypeToPublish: function() {
                        return datatypeCopy;
                    }
                }
            });
            modalInstance.result.then(function(datatypetoPublish) {
                console.log("Saving");
                console.log($rootScope.datatype);
                console.log("IN LIBRARY");
                console.log($rootScope.datatypeLibrary);

                var ext = $rootScope.datatype.ext;

                DatatypeService.publish($rootScope.datatype).then(function(result) {
                    console.log("returning");
                    console.log(result);
                    console.log("In the map")

                    console.log($rootScope.datatypesMap[result.id])
                    var oldLink = DatatypeLibrarySvc.findOneChild(result.id, $rootScope.datatypeLibrary.children);
                    var newLink = DatatypeService.getDatatypeLink(result);
                    newLink.ext = ext;
                    DatatypeLibrarySvc.updateChild($rootScope.datatypeLibrary.id, newLink).then(function(link) {
                        DatatypeService.merge($rootScope.datatypesMap[result.id], result);
                        console.log("After Merge")
                        console.log($rootScope.datatypesMap[result.id]);


                        $rootScope.datatypesMap[result.id].status = "PUBLISHED";
                        $rootScope.datatype.status = "PUBLISHED";

                        if ($scope.editForm) {
                            console.log("Cleeaning");
                            $scope.editForm.$setPristine();
                            $scope.editForm.$dirty = false;
                            $scope.editForm.$invalid = false;

                        }
                        $rootScope.clearChanges();
                        DatatypeService.merge($rootScope.datatype, result);
                        if ($scope.datatypesParams) {
                            $scope.datatypesParams.refresh();
                        }
                        VersionAndUseService.findById(result.id).then(function(inf) {
                            $rootScope.versionAndUseMap[inf.id] = inf;
                            if ($rootScope.versionAndUseMap[inf.sourceId]) {
                                $rootScope.versionAndUseMap[inf.sourceId].deprecated = true;
                            }

                        });
                        oldLink.ext = newLink.ext;
                        oldLink.name = newLink.name;
                        $scope.saving = false;
                    }, function(error) {
                        $scope.saving = false;
                        $rootScope.msg().text = "Sorry an error occured. Please try again";
                        $rootScope.msg().type = "danger";
                        $rootScope.msg().show = true;
                    });
                }, function(error) {
                    $scope.saving = false;
                    $rootScope.msg().text = "Sorry an error occured. Please try again";
                    $rootScope.msg().type = "danger";
                    $rootScope.msg().show = true;
                });
            });
        };

        $scope.abortPublish = function(datatype) {
            var modalInstance = $modal.open({
                templateUrl: 'AbortPublishCtl.html',
                controller: 'AbortPublishCtl',
                resolve: {
                    datatypeToPublish: function() {
                        return datatype;
                    },
                    unpublishedDatatypes: function() {
                        return $scope.unpublishedDatatypes;
                    },
                    unpublishedTables: function() {
                        return $scope.unpublishedTables;
                    }

                }
            });

        };




        $scope.publishDatatype = function(datatype) {

            $scope.containUnpublished = false;
            $scope.unpublishedTables = [];
            $scope.unpublishedDatatypes = [];
            $scope.ContainUnpublished(datatype);

            if ($scope.containUnpublished) {
                $scope.abortPublish(datatype);
                datatype.status = "UNPUBLISHED";
            } else {
                $scope.confirmPublish(datatype);

            }
        };

        $scope.redirectVS = function(valueSet) {
            TableService.getOne(valueSet.id).then(function(valueSet) {
                var modalInstance = $modal.open({
                    templateUrl: 'redirectCtrl.html',
                    controller: 'redirectCtrl',
                    size: 'md',
                    resolve: {
                        destination: function() {
                            return valueSet;
                        }
                    }



                });
                modalInstance.result.then(function() {
                    if (!$rootScope.SharingScope) {
                        $rootScope.editTable(valueSet);
                    } else {
                        $scope.editTable(valueSet);
                    }

                });



            });
        };


        $scope.selectedVS = function() {
            return ($scope.selectedValueSet !== undefined);
        };
        $scope.unselectVS = function() {
            $scope.selectedValueSet = undefined;
            $scope.VSselected = false;
            };
        $scope.isVSActive = function(id) {
            if ($scope.selectedValueSet) {
                return $scope.selectedValueSet.id === id;
            } else {
                return false;
            }

        };





        $scope.addComponentModal = function(datatype) {
            console.log(datatype);
            var modalInstance = $modal.open({
                templateUrl: 'AddComponentModal.html',
                controller: 'AddComponentCtrl',
                windowClass: 'app-modal-window',
                resolve: {

                    valueSets: function() {
                        return $rootScope.tables;
                    },
                    datatypes: function() {
                        return $rootScope.datatypes;
                    },
                    datatype: function() {
                        return datatype;
                    },
                    messageTree: function() {
                        return $rootScope.messageTree;
                    }

                }
            });
            modalInstance.result.then(function(datatype) {
                $scope.setDirty();

                if ($scope.datatypesParams)
                    $scope.datatypesParams.refresh();
            });
        };

        $scope.copy = function(datatype) {
            CloneDeleteSvc.copyDatatype(datatype);
        };

        $scope.reset = function() {
            blockUI.start();
            DatatypeService.reset();
            $scope.cleanState();
            $rootScope.datatype = angular.copy($rootScope.datatypesMap[$rootScope.datatype.id]);

            $rootScope.references = [];
            angular.forEach($rootScope.segments, function(segment) {
                $rootScope.findDatatypeRefs($rootScope.datatype, segment, $rootScope.getSegmentLabel(segment), segment);
            });
            angular.forEach($rootScope.datatypes, function(dt) {
                $rootScope.findDatatypeRefs($rootScope.datatype, dt, $rootScope.getDatatypeLabel(dt), dt);
            });

            blockUI.stop();
        };

        $scope.recordDatatypeChange = function(type, command, id, valueType, value) {
            var datatypeFromChanges = $rootScope.findObjectInChanges("datatype", "add", $rootScope.datatype.id);
            if (datatypeFromChanges === undefined) {
                $rootScope.recordChangeForEdit2(type, command, id, valueType, value);
            }
        };

        $scope.close = function() {
            $rootScope.datatype = null;
            $scope.refreshTree();
            $scope.loadingSelection = false;
        };

        $scope.delete = function(datatype) {
            CloneDeleteSvc.deleteDatatype(datatype);
        };

        $scope.hasChildren = function(node) {
            return node && node != null && node.datatype && $rootScope.getDatatype(node.datatype.id) != undefined && $rootScope.getDatatype(node.datatype.id).components != null && $rootScope.getDatatype(node.datatype.id).components.length > 0;
        };

        $scope.validateLabel = function(label, name) {
            if (label && !label.startsWith(name)) {
                return false;
            }
            return true;
        };

        $scope.onDatatypeChange = function(node) {
            $rootScope.recordChangeForEdit2('component', 'edit', node.id, 'datatype', node.datatype);
            $scope.refreshTree(); // TODO: Refresh only the node
        };

        $scope.refreshTree = function() {
            if ($scope.datatypesParams)
                $scope.datatypesParams.refresh();
        };

        $scope.goToTable = function(table) {
            $scope.$emit('event:openTable', table);
        };

        $scope.deleteTable = function(node) {
            node.table = null;
            $rootScope.recordChangeForEdit2('component', 'edit', node.id, 'table', null);
        };

        $scope.mapTable = function(node) {
            var modalInstance = $modal.open({
                templateUrl: 'TableMappingDatatypeCtrl.html',
                controller: 'TableMappingDatatypeCtrl',
                windowClass: 'app-modal-window',
                resolve: {
                    selectedNode: function() {
                        return node;
                    }
                }
            });
            modalInstance.result.then(function(node) {
                $scope.selectedNode = node;
                $scope.setDirty();
            }, function() {});
        };

        $scope.managePredicate = function(node) {
            var modalInstance = $modal.open({
                templateUrl: 'PredicateDatatypeCtrl.html',
                controller: 'PredicateDatatypeCtrl',
                windowClass: 'app-modal-window',
                resolve: {
                    selectedNode: function() {
                        return node;
                    }
                }
            });
            modalInstance.result.then(function(dt) {
                if (dt) {
                    $rootScope.datatype.predicates = dt.predicates;
                    $scope.setDirty();
                }
            }, function() {});
        };

        $scope.manageConformanceStatement = function() {
            var modalInstance = $modal.open({
                templateUrl: 'ConformanceStatementDatatypeCtrl.html',
                controller: 'ConformanceStatementDatatypeCtrl',
                windowClass: 'app-modal-window',
                resolve: {}
            });
            modalInstance.result.then(function(dt) {
                if (dt) {
                    $scope.setDirty();
                }
            }, function() {});
        };

        $scope.isSubDT = function(component) {
            if ($rootScope.datatype != null) {
                for (var i = 0, len = $rootScope.datatype.components.length; i < len; i++) {
                    if ($rootScope.datatype.components[i].id === component.id)
                        return false;
                }
            }
            return true;
        };

        $scope.findDTByComponentId = function(componentId) {
            return $rootScope.parentsMap[componentId] ? $rootScope.parentsMap[componentId] : null;
        };

        $scope.countConformanceStatements = function(position) {
            var count = 0;
            if ($rootScope.datatype != null)
                for (var i = 0, len1 = $rootScope.datatype.conformanceStatements.length; i < len1; i++) {
                    if ($rootScope.datatype.conformanceStatements[i].constraintTarget.indexOf(position + '[') === 0)
                        count = count + 1;
                }

            return count;
        };

        $scope.countPredicate = function(position) {
            var count = 0;
            if ($rootScope.datatype != null)
                for (var i = 0, len1 = $rootScope.datatype.predicates.length; i < len1; i++) {
                    if ($rootScope.datatype.predicates[i].constraintTarget.indexOf(position + '[') === 0)
                        count = count + 1;
                }

            return count;
        };

        $scope.countPredicateOnSubComponent = function(position, componentId) {
            var dt = $scope.findDTByComponentId(componentId);
            if (dt != null)
                for (var i = 0, len1 = dt.predicates.length; i < len1; i++) {
                    if (dt.predicates[i].constraintTarget.indexOf(position + '[') === 0)
                        return 1;
                }

            return 0;
        };


        $scope.isRelevant = function(node) {
            return DatatypeService.isRelevant(node);
        };

        $scope.isBranch = function(node) {
            return DatatypeService.isBranch(node);
        };


        $scope.isVisible = function(node) {
            return DatatypeService.isVisible(node);
        };

        $scope.children = function(node) {
            return DatatypeService.getNodes(node);
        };

        $scope.getParent = function(node) {
            return DatatypeService.getParent(node);
        };

        $scope.getDatatypeLevelConfStatements = function(element) {
            return DatatypeService.getDatatypeLevelConfStatements(element);
        };

        $scope.getDatatypeLevelPredicates = function(element) {
            return DatatypeService.getDatatypeLevelPredicates(element);
        };

        $scope.isChildSelected = function(component) {
            return $scope.selectedChildren.indexOf(component) >= 0;
        };

        $scope.isChildNew = function(component) {
            return component && component != null && component.status === 'DRAFT';
        };


        $scope.selectChild = function($event, child) {
            var checkbox = $event.target;
            var action = (checkbox.checked ? 'add' : 'remove');
            updateSelected(action, child);
        };


        $scope.selectAllChildren = function($event) {
            var checkbox = $event.target;
            var action = (checkbox.checked ? 'add' : 'remove');
            for (var i = 0; i < $rootScope.datatype.components.length; i++) {
                var component = $rootScope.datatype.components[i];
                updateSelected(action, component);
            }
        };

        var updateSelected = function(action, child) {
            if (action === 'add' && !$scope.isChildSelected(child)) {
                $scope.selectedChildren.push(child);
            }
            if (action === 'remove' && $scope.isChildSelected(child)) {
                $scope.selectedChildren.splice($scope.selectedChildren.indexOf(child), 1);
            }
        };

        //something extra I couldn't resist adding :)
        $scope.isSelectedAllChildren = function() {
            return $rootScope.datatype && $rootScope.datatype != null && $rootScope.datatype.components && $scope.selectedChildren.length === $rootScope.datatype.components.length;
        };


        /**
         * TODO: update master map
         */
        $scope.createNewComponent = function() {
            if ($rootScope.datatype != null) {
                if (!$rootScope.datatype.components || $rootScope.datatype.components === null)
                    $rootScope.datatype.components = [];
                var child = ComponentService.create($rootScope.datatype.components.length + 1);
                $rootScope.datatype.components.push(child);
                //TODO update master map
                //MastermapSvc.addDatatypeObject($rootScope.datatype, [[$rootScope.igdocument.id, "ig"], [$rootScope.igdocument.profile.id, "profile"]]);
                //TODO:remove as legacy code
                $rootScope.parentsMap[child.id] = $rootScope.datatype;
                if ($scope.datatypesParams)
                    $scope.datatypesParams.refresh();
            }
        };

        /**
         * TODO: update master map
         */
        $scope.deleteComponents = function() {
            if ($rootScope.datatype != null && $scope.selectedChildren != null && $scope.selectedChildren.length > 0) {
                ComponentService.deleteList($scope.selectedChildren, $rootScope.datatype);
                //TODO update master map
                //TODO:remove as legacy code
                angular.forEach($scope.selectedChildren, function(child) {
                    delete $rootScope.parentsMap[child.id];
                });
                $scope.selectedChildren = [];
                if ($scope.datatypesParams)
                    $scope.datatypesParams.refresh();
            }
        };


        $scope.cleanState = function() {
            $scope.selectedChildren = [];
            $rootScope.addedDatatypes = [];
            $rootScope.addedTables = [];
            if ($scope.editForm) {
                $scope.editForm.$setPristine();
                $scope.editForm.$dirty = false;
            }
            $rootScope.clearChanges();
            if ($scope.datatypesParams)
                $scope.datatypesParams.refresh();
        };
        $scope.callDTDelta = function() {

            $rootScope.$emit("event:openDTDelta");
        };
        $scope.AddBindingForDatatype = function(datatype) {
            var modalInstance = $modal.open({
                templateUrl: 'AddBindingForDatatype.html',
                controller: 'AddBindingForDatatype',
                windowClass: 'conformance-profiles-modal',
                resolve: {
                    datatype: function() {
                        return datatype;
                    }
                }
            });
            modalInstance.result.then(function() {
                $scope.setDirty();
            });
        };

        $scope.saveDatatype = function() {
            console.log("Saving");
            console.log($rootScope.datatype);
            console.log("IN LIBRARY");
            console.log($rootScope.datatypeLibrary);

            var ext = $rootScope.datatype.ext;

            DatatypeService.save($rootScope.datatype).then(function(result) {
                var oldLink = DatatypeLibrarySvc.findOneChild(result.id, $rootScope.datatypeLibrary.children);
                var newLink = DatatypeService.getDatatypeLink(result);
                newLink.ext = ext;
                DatatypeLibrarySvc.updateChild($rootScope.datatypeLibrary.id, newLink).then(function(link) {
                    DatatypeService.merge($rootScope.datatypesMap[result.id], result);
                    DatatypeService.merge($rootScope.datatype, result);
                    console.log("datatype.components");
                    console.log($scope.datatypesParams);
                    console.log($rootScope.datatype.components);
                    //  if ($scope.datatypesParams){
                    //      $scope.datatypesParams
                    //         $scope.datatypesParams.refresh();
                    //     }
                    $rootScope.clearChanges();
                    if ($scope.datatypesParams) {
                        // $scope.datatypesParams.refresh();   	
                    }
                    $rootScope.datatype.dateUpdated = result.dateUpdated;
                    $rootScope.$emit("event:updateIgDate");
                    DatatypeLibrarySvc.updateChild($rootScope.datatypeLibrary.id, newLink).then(function(link) {
                        DatatypeService.saveNewElements().then(function() {


                            oldLink.ext = newLink.ext;
                            oldLink.name = newLink.name;
                            $scope.saving = false;
                            $scope.cleanState();
                        }, function(error) {
                            $scope.saving = false;
                            $rootScope.msg().text = "Sorry an error occured. Please try again";
                            $rootScope.msg().type = "danger";
                            $rootScope.msg().show = true;
                        });
                    }, function(error) {
                        $scope.saving = false;
                        $rootScope.msg().text = "Sorry an error occured. Please try again";
                        $rootScope.msg().type = "danger";
                        $rootScope.msg().show = true;
                    });

                }, function(error) {
                    $scope.saving = false;
                    $rootScope.msg().text = error.data.text;
                    $rootScope.msg().type = error.data.type;
                    $rootScope.msg().show = true;
                });
                $rootScope.saveBindingForDatatype();
            });
        }
        $scope.cancel = function() {
            //TODO: remove changes from master ma
            angular.forEach($rootScope.datatype.components, function(child) {
                if ($scope.isChildNew(child.status)) {
                    delete $rootScope.parentsMap[child.id];
                }
            });
            $rootScope.datatype = null;
            $scope.selectedChildren = [];
            $rootScope.clearChanges();
        };

        var searchById = function(id) {
            var children = $rootScope.datatypeLibrary.children;
            for (var i = 0; i < $rootScope.datatypeLibrary.children; i++) {
                if (children[i].id === id) {
                    return children[i];
                }
            }
            return null;
        };

        var indexIn = function(id, collection) {
            for (var i = 0; i < collection.length; i++) {
                if (collection[i].id === id) {
                    return i;
                }
            }
            return -1;
        };


        $scope.showSelectDatatypeFlavorDlg = function(component) {
            var modalInstance = $modal.open({
                templateUrl: 'SelectDatatypeFlavor.html',
                controller: 'SelectDatatypeFlavorCtrl',
                windowClass: 'app-modal-window',
                resolve: {
                    currentDatatype: function() {
                        return $rootScope.datatypesMap[component.datatype.id];
                    },

                    hl7Version: function() {
                        return $rootScope.igdocument.profile.metaData.hl7Version;
                    },
                    datatypeLibrary: function() {
                        return $rootScope.datatypeLibrary;
                    }
                }
            });
            modalInstance.result.then(function(datatype, ext) {
                //                MastermapSvc.deleteElementChildren(component.datatype.id, "datatype", component.id, component.type);
                //                MastermapSvc.addDatatypeObject(datatype, [[component.id, component.type]]);
                component.datatype.id = datatype.id;
                component.datatype.name = datatype.name;
                component.datatype.ext = datatype.ext;
                $rootScope.processElement(component);
                $scope.setDirty();
                if ($scope.datatypesParams)
                    $scope.datatypesParams.refresh();

            });

        };

        $scope.shareModal = function(datatype) {


            $http.get('api/usernames').then(function(response) {
                var userList = response.data;
                var filteredUserList = userList.filter(function(user) {
                    // Add accountId var
                    user.accountId = user.id;
                    var isPresent = false;
                    if (datatype.shareParticipantIds) {
                        for (var i = 0; i < datatype.shareParticipantIds.length; i++) {
                            if (datatype.shareParticipantIds[i].accountId == user.id) {
                                isPresent = true;
                            }
                        }
                    }
                    if (!isPresent) return user;
                });

                var modalTemplate = "ShareDatatypeErrorModal.html";
                if (datatype.status === "PUBLISHED") {
                    modalTemplate = "ShareDatatypeModal.html";
                }
                var modalInstance = $modal.open({
                    templateUrl: modalTemplate,
                    controller: 'ShareDatatypeCtrl',
                    size: 'lg',
                    resolve: {
                        igdocumentSelected: function() {
                            return datatype;
                        },
                        userList: function() {
                            return _.filter(filteredUserList, function(user) {
                                return user.id != $rootScope.accountId && datatype.shareParticipantIds && datatype.shareParticipantIds != null && datatype.shareParticipantIds.indexOf(user.id) == -1;
                            });
                        }
                    }
                });

                modalInstance.result.then(function(result) {
                    $scope.saveDatatypeAfterShare();
                }, function() {
                    if (modalTemplate === 'ShareDatatypeModal.html') {
                        $scope.saveDatatypeAfterShare();
                    }
                });

            }, function(error) {
            });
        };

        $scope.saveDatatypeAfterShare = function() {

            var ext = $rootScope.datatype.ext;

            DatatypeService.save($rootScope.datatype).then(function(result) {
                var oldLink = DatatypeLibrarySvc.findOneChild(result.id, $rootScope.datatypeLibrary.children);
                var newLink = DatatypeService.getDatatypeLink(result);
                newLink.ext = ext;
                DatatypeLibrarySvc.updateChild($rootScope.datatypeLibrary.id, newLink).then(function(link) {
                    DatatypeService.merge($rootScope.datatype, result);

                    DatatypeService.saveNewElements(true).then(function() {


                        oldLink.ext = newLink.ext;
                        oldLink.name = newLink.name;
                        $scope.saving = false;
                        $scope.cleanState();
                    }, function(error) {
                        $scope.saving = false;
                    });
                }, function(error) {
                    $scope.saving = false;
                });

            }, function(error) {
                $scope.saving = false;
            });
            $rootScope.saveBindingForDatatype();
        };

    });


angular.module('igl')
    .controller('DatatypeRowCtrl', function($scope, $filter) {

        $scope.init = function(node) {
            $scope.node = node;
        }

        $scope.formName = "form_" + new Date().getTime();
    });

angular.module('igl')
    .controller('SelectDatatypeFlavorCtrl', function($scope, $filter, $modalInstance, $rootScope, $http, currentDatatype, DatatypeService, $rootScope, hl7Version, ngTreetableParams, ViewSettings, DatatypeLibrarySvc, $q, datatypeLibrary, TableService) {
        $scope.resultsError = null;
        $scope.viewSettings = ViewSettings;
        $scope.resultsLoading = null;
        $scope.results = [];
        $scope.tmpResults = [].concat($scope.results);
        $scope.datatypeLibrary = datatypeLibrary;

        $scope.currentDatatype = angular.copy(currentDatatype);
        $scope.selection = { library: null, scope: null, hl7Version: hl7Version, datatype: null, name: $scope.currentDatatype != null && $scope.currentDatatype ? $scope.currentDatatype.name : null, selected: null };
        $scope.dataypesMap = {};
        $scope.datatypeFlavorParams = new ngTreetableParams({
            getNodes: function(parent) {
                return DatatypeService.getNodes(parent, $scope.selection.datatype);
            },
            getTemplate: function(node) {
                return DatatypeService.getReadTemplate(node, $scope.selection.datatype);
            }
        });

        $scope.isRelevant = function(node) {
            var rel = DatatypeService.isRelevant(node);
            return rel;
        };

        $scope.isBranch = function(node) {
            var isBran = DatatypeService.isBranch(node);
            return isBran;
        };


        $scope.isVisible = function(node) {
            var isVis = DatatypeService.isVisible(node);
            return isVis;
        };

        $scope.children = function(node) {
            var chil = DatatypeService.getNodes(node);
            return chil;
        };

        $scope.getParent = function(node) {
            var par = DatatypeService.getParent(node);
            return par;
        };

        $scope.isChildSelected = function(component) {
            return $scope.selectedChildren.indexOf(component) >= 0;
        };

        $scope.isChildNew = function(component) {
            return component && component != null && component.status === 'DRAFT';
        };


        $scope.hasChildren = function(node) {
            console.log("hasChildren==============================================================");
            console.log(node);
            return node && node != null && node.datatype && $rootScope.getDatatype(node.datatype.id) != undefined && $rootScope.getDatatype(node.datatype.id).components != null && $rootScope.getDatatype(node.datatype.id).components.length > 0;
        };

        $scope.validateLabel = function(label, name) {
            if (label && !label.startsWith(name)) {
                return false;
            }
            return true;
        };

        $scope.loadLibrariesByFlavorName = function() {
            var delay = $q.defer();
            $scope.selection.datatype = null;
            $scope.selection.selected = null;
            $scope.ext = null;
            $scope.results = [];
            $scope.tmpResults = [];
            $scope.results = $scope.results.concat(filterFlavors(datatypeLibrary, $scope.selection.name));
            $scope.tmpResults = [].concat($scope.results);
            DatatypeLibrarySvc.findLibrariesByFlavorName($scope.selection.name, 'HL7STANDARD', $scope.selection.hl7Version).then(function(libraries) {
                if (libraries != null) {
                    _.each(libraries, function(library) {
                        $scope.results = $scope.results.concat(filterFlavors(library, $scope.selection.name));
                    });
                }

                $scope.results = _.uniq($scope.results, function(item, key, a) {
                    return item.id;
                });
                $scope.tmpResults = [].concat($scope.results);

                delay.resolve(true);
            }, function(error) {
                $rootScope.msg().text = "Sorry could not load the data types";
                $rootScope.msg().type = error.data.type;
                $rootScope.msg().show = true;
                delay.reject(error);
            });
            return delay.promise;
        };

        var filterFlavors = function(library, name) {
            var results = [];
            _.each(library.children, function(link) {
                if (link.name === name) {
                    link.libraryName = library.metaData.name;
                    link.hl7Version = library.metaData.hl7Version;
                    results.push(link);
                }
            });
            return results;
        };

        $scope.isDatatypeSubDT = function(component) {
            return DatatypeService.isDatatypeSubDT(component, $scope.selection.datatype);
        };

        $scope.isSelectedDatatype = function(datatype) {
            return $scope.selection.selected != null && datatype != null && $scope.selection.selected == datatype.id;
        };

        $scope.isSelectedLibrary = function(library) {
            return $scope.selection.library != null && library != null && $scope.selection.library.id == library.id;
        };

        $scope.showSelectedDetails = function(datatype) {
            if (datatype && datatype != null) {
                $scope.selection.datatype = datatype;
                $scope.selection.datatype["type"] = "datatype";
            }
        };

        var indexIn = function(id, collection) {
            for (var i = 0; i < collection.length; i++) {
                if (collection[i].id === id) {
                    return i;
                }
            }
            return -1;
        };


        var collectNewDatatypesAndTables = function(root, datatypes) {
            $rootScope.datatypesMap[root.id] = root;
            if (indexIn(root.id, $rootScope.addedDatatypes) < 0) {
                $rootScope.addedDatatypes.push(root);
                console.log(child);
            }
            var tmpTables = [];
            angular.forEach(datatypes, function(child) {
                $rootScope.datatypesMap[child.id] = child;
                if (indexIn(child.id, $rootScope.addedDatatypes) < 0) {
                    $rootScope.addedDatatypes.push(child);
                    console.log(child);
                    $rootScope.filteredDatatypesList.push(child);
                }

                if (indexIn(child.table.id, $rootScope.addedTables) < 0) {
                    tmpTables.push(child.table.id);

                }
            });

            if (tmpTables.length > 0) {
                TableService.findAllByIds(tmpTables).then(function(tables) {
                    $rootScope.addedTables = $rootScope.addedTables.concat(tables);
                    angular.forEach(tables, function(table) {
                        $rootScope.tablesMap[table.id] = table;
                    });
                    $modalInstance.close($scope.selection.datatype);
                }, function(error) {
                    $rootScope.msg().text = "Sorry an error occured. Please try again";
                    $rootScope.msg().type = "danger";
                    $rootScope.msg().show = true;
                });
            } else {
                $modalInstance.close($scope.selection.datatype);
            }
        };


        $scope.submit = function() {
            var indexFromLibrary = indexIn($scope.selection.datatype.id, $scope.datatypeLibrary.children);
            var indexFromCollection = indexIn($scope.selection.datatype.id, $rootScope.datatypes);
            var indexFromMap = $rootScope.datatypesMap[$scope.selection.datatype.id] != undefined && $rootScope.datatypesMap[$scope.selection.datatype.id] != null ? 100 : -1;

            if (indexFromLibrary < 0 | indexFromCollection < 0 | indexFromMap < 0) {
                DatatypeService.getOne($scope.selection.datatype.id).then(function(full) {
                    DatatypeService.collectDatatypes(full.id).then(function(datatypes) {
                        $rootScope.processSegmentsTree($rootScope.segment, null);
                        $scope.ext = full.ext;
                        $scope.selection.datatype = full;
                        $scope.selection.datatype["type"] = "datatype";
                        collectNewDatatypesAndTables($scope.selection.datatype, datatypes);
                    }, function(error) {
                        $scope.loadingSelection = false;
                        $rootScope.msg().text = "Sorry could not load the data type";
                        $rootScope.msg().type = "danger";
                        $rootScope.msg().show = true;
                    });
                }, function(error) {
                    $scope.resultsLoading = false;
                    $rootScope.msg().text = "Sorry could not load the data type";
                    $rootScope.msg().type = "danger";
                    $rootScope.msg().show = true;
                });
            } else {
                $modalInstance.close($scope.selection.datatype);
            }
        };


        $scope.cancel = function() {
            $scope.resetMap();
            $modalInstance.dismiss('cancel');
        };

        $scope.getLocalDatatypeLabel = function(link) {
            return link != null ? $rootScope.getLabel(link.name, link.ext) : null;
        };


        $scope.resetMap = function() {
            if ($rootScope.addedDatatypes = null) {
                angular.forEach($rootScope.addedDatatypes, function(child) {
                    var dt = $rootScope.datatypesMap[child];
                    if (dt.id !== $scope.currentDatatype.id) {
                        delete $rootScope.datatypesMap[child];
                    }
                });
            }
        };

        $scope.loadLibrariesByFlavorName().then(function(done) {
            $scope.selection.selected = $scope.currentDatatype.id;
            $scope.showSelectedDetails($scope.currentDatatype);
        });

    });


angular.module('igl').controller('ConfirmDatatypeDeleteCtrl', function($scope, $modalInstance, dtToDelete, $rootScope, DatatypeLibrarySvc, DatatypeService, MastermapSvc, CloneDeleteSvc) {
    $scope.dtToDelete = dtToDelete;
    $scope.loading = false;
    $scope.delete = function() {
        $scope.loading = true;
        if ($scope.dtToDelete.scope === 'USER' && $scope.dtToDelete.status === 'UNPUBLISHED') {
            CloneDeleteSvc.deleteDatatypeAndDatatypeLink($scope.dtToDelete);
        } else {
            console.log("deleting");
            CloneDeleteSvc.deleteDatatypeLink($scope.dtToDelete);
        }

        $modalInstance.close($scope.dtToDelete);
        $scope.loading = false;
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
});


angular.module('igl').controller('DatatypeReferencesCtrl', function($scope, $modalInstance, dtToDelete) {

    $scope.dtToDelete = dtToDelete;

    $scope.ok = function() {
        $modalInstance.close($scope.dtToDelete);
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
});

angular.module('igl').controller('TableMappingDatatypeCtrl', function($scope, $modalInstance, selectedNode, $rootScope) {
    $scope.changed = false;
    $scope.selectedNode = selectedNode;
    $scope.selectedTable = null;
    if (selectedNode.table != undefined) {
        $scope.selectedTable = $rootScope.tablesMap[selectedNode.table.id];
    }

    $scope.selectTable = function(table) {
        $scope.changed = true;
        $scope.selectedTable = table;
    };


    $scope.mappingTable = function() {
        if ($scope.selectedNode.table == null || $scope.selectedNode.table == undefined) $scope.selectedNode.table = {};

        $scope.selectedNode.table.id = $scope.selectedTable.id;
        $scope.selectedNode.table.bindingIdentifier = $scope.selectedTable.bindingIdentifier;
        $rootScope.recordChanged();
        $scope.ok();
    };

    $scope.ok = function() {
        $modalInstance.close($scope.selectedNode);
    };

});


angular.module('igl').controller('ConformanceStatementDatatypeCtrl', function($scope, $modalInstance, $rootScope, $q) {
    $scope.constraintType = 'Plain';
    $scope.constraints = [];
    $scope.firstConstraint = null;
    $scope.secondConstraint = null;
    $scope.compositeType = null;
    $scope.complexConstraint = null;
    $scope.ext = null;
    if ($rootScope.igdocument) $scope.ext = $rootScope.igdocument.metaData.ext;
    $scope.newComplexConstraintId = $rootScope.calNextCSID($scope.ext, $rootScope.datatype.name + "_" + $rootScope.datatype.ext);
    $scope.newComplexConstraint = [];
    $scope.firstNodeData = null;
    $scope.secondNodeData = null;
    $scope.changed = false;
    $scope.tempComformanceStatements = [];
    angular.copy($rootScope.datatype.conformanceStatements, $scope.tempComformanceStatements);

    $scope.treeDataForContext = [];
    $scope.treeDataForContext.push(angular.copy($rootScope.datatype));
    $scope.treeDataForContext[0].pathInfoSet = [];
    $scope.generatePathInfo = function(current, positionNumber, locationName, instanceNumber, isInstanceNumberEditable, nodeName) {
        var pathInfo = {};
        pathInfo.positionNumber = positionNumber;
        pathInfo.locationName = locationName;
        pathInfo.nodeName = nodeName;
        pathInfo.instanceNumber = instanceNumber;
        pathInfo.isInstanceNumberEditable = isInstanceNumberEditable;
        current.pathInfoSet.push(pathInfo);

        if (current.type == 'datatype') {
            var dt = current;
            for (var i in dt.components) {
                var c = dt.components[i];
                c.pathInfoSet = angular.copy(current.pathInfoSet);

                var childPositionNumber = c.position;
                var childLocationName = c.position;
                var childNodeName = c.name;
                var childInstanceNumber = "1";
                var childisInstanceNumberEditable = false;
                var child = angular.copy($rootScope.datatypesMap[c.datatype.id]);
                child.id = new ObjectId().toString();
                c.child = child;
                $scope.generatePathInfo(c, childPositionNumber, childLocationName, childInstanceNumber, childisInstanceNumberEditable, childNodeName);
            }
        } else if (current.type == 'component') {
            var dt = current.child;
            for (var i in dt.components) {
                var c = dt.components[i];
                c.pathInfoSet = angular.copy(current.pathInfoSet);
                var childPositionNumber = c.position;
                var childLocationName = c.position;
                var childNodeName = c.name;
                var childInstanceNumber = "1";
                var childisInstanceNumberEditable = false;
                var child = angular.copy($rootScope.datatypesMap[c.datatype.id]);
                child.id = new ObjectId().toString();
                c.child = child;
                $scope.generatePathInfo(c, childPositionNumber, childLocationName, childInstanceNumber, childisInstanceNumberEditable, childNodeName);
            }
        }
    };

    $scope.generatePathInfo($scope.treeDataForContext[0], ".", ".", "1", false);

    $scope.setChanged = function() {
        $scope.changed = true;
    };

    $scope.toggleChildren = function(data) {
        data.childrenVisible = !data.childrenVisible;
        data.folderClass = data.childrenVisible ? "fa-minus" : "fa-plus";
    };

    $scope.beforeComponentDrop = function() {
        var deferred = $q.defer();

        if ($scope.draggingStatus === 'ContextTreeNodeDragging') {
            deferred.resolve();
        } else {
            deferred.reject();
        }
        return deferred.promise;
    };

    $scope.beforeNodeDrop = function() {
        var deferred = $q.defer();
        deferred.resolve();
        return deferred.promise;
    };

    $scope.afterNodeDrop = function() {
        $scope.draggingStatus = null;
        $scope.newConstraint.pathInfoSet_1 = $scope.firstNodeData.pathInfoSet;
        $scope.generateFirstPositionAndLocationPath();
    };

    $scope.afterSecondNodeDrop = function() {
        $scope.draggingStatus = null;
        $scope.newConstraint.pathInfoSet_2 = $scope.secondNodeData.pathInfoSet;
        $scope.generateSecondPositionAndLocationPath();
    };

    $scope.draggingNodeFromContextTree = function(event, ui, data) {
        $scope.draggingStatus = 'ContextTreeNodeDragging';
    };

    $scope.initConformanceStatement = function() {
        $scope.newConstraint = angular.fromJson({
            pathInfoSet_1: null,
            pathInfoSet_2: null,
            position_1: null,
            position_2: null,
            location_1: null,
            location_2: null,
            freeText: null,
            verb: null,
            ignoreCase: false,
            constraintId: $rootScope.calNextCSID($scope.ext, $rootScope.datatype.name + "_" + $rootScope.datatype.ext),
            contraintType: null,
            value: null,
            value2: null,
            valueSetId: null,
            bindingStrength: 'R',
            bindingLocation: '1'
        });
    };

    $scope.initComplexStatement = function() {
        $scope.constraints = [];
        $scope.firstConstraint = null;
        $scope.secondConstraint = null;
        $scope.compositeType = null;
        $scope.newComplexConstraintId = $rootScope.calNextCSID($scope.ext, $rootScope.datatype.name + "_" + $rootScope.datatype.ext);
    };

    $scope.initConformanceStatement();

    $scope.generateFirstPositionAndLocationPath = function() {
        if ($scope.newConstraint.pathInfoSet_1) {
            var positionPath = '';
            var locationPath = '';
            for (var i in $scope.newConstraint.pathInfoSet_1) {
                if (i > 0) {
                    var pathInfo = $scope.newConstraint.pathInfoSet_1[i];
                    positionPath = positionPath + "." + pathInfo.positionNumber + "[" + pathInfo.instanceNumber + "]";
                    locationPath = locationPath + "." + pathInfo.locationName + "[" + pathInfo.instanceNumber + "]";

                    if (i == $scope.newConstraint.pathInfoSet_1.length - 1) {
                        locationPath = locationPath + " (" + pathInfo.nodeName + ")";
                    }
                }
            }
            $scope.newConstraint.position_1 = positionPath.substr(1);
            $scope.newConstraint.location_1 = $rootScope.datatype.name + '-' + locationPath.substr(1);
        }
    };

    $scope.generateSecondPositionAndLocationPath = function() {
        if ($scope.newConstraint.pathInfoSet_2) {
            var positionPath = '';
            var locationPath = '';
            for (var i in $scope.newConstraint.pathInfoSet_2) {
                if (i > 0) {
                    var pathInfo = $scope.newConstraint.pathInfoSet_2[i];
                    positionPath = positionPath + "." + pathInfo.positionNumber + "[" + pathInfo.instanceNumber + "]";
                    locationPath = locationPath + "." + pathInfo.locationName + "[" + pathInfo.instanceNumber + "]";

                    if (i == $scope.newConstraint.pathInfoSet_2.length - 1) {
                        locationPath = locationPath + " (" + pathInfo.nodeName + ")";
                    }
                }
            }
            $scope.newConstraint.position_2 = positionPath.substr(1);
            $scope.newConstraint.location_2 = $rootScope.datatype.name + '-' + locationPath.substr(1);
        }
    };

    $scope.deleteConformanceStatement = function(conformanceStatement) {
        $scope.tempComformanceStatements.splice($scope.tempComformanceStatements.indexOf(conformanceStatement), 1);
        $scope.changed = true;
    };

    $scope.addComplexConformanceStatement = function() {
        $scope.complexConstraint = $rootScope.generateCompositeConformanceStatement($scope.compositeType, $scope.firstConstraint, $scope.secondConstraint, $scope.constraints);
        $scope.tempComformanceStatements.push($scope.complexConstraint);
        $scope.initComplexStatement();
        $scope.changed = true;
    };

    $scope.addFreeTextConformanceStatement = function() {
        var cs = $rootScope.generateFreeTextConformanceStatement($scope.newConstraint);
        $scope.tempComformanceStatements.push(cs);
        $scope.changed = true;
        $scope.initConformanceStatement();
    };

    $scope.addConformanceStatement = function() {
        var cs = $rootScope.generateConformanceStatement($scope.newConstraint);
        $scope.tempComformanceStatements.push(cs);
        $scope.changed = true;
        $scope.initConformanceStatement();
    };

    $scope.ok = function() {
        $modalInstance.close();
    };

    $scope.saveclose = function() {
        angular.copy($scope.tempComformanceStatements, $rootScope.datatype.conformanceStatements);
        $rootScope.recordChanged();
        $modalInstance.close($rootScope.datatype);
    };
});


angular.module('igl').controller('PredicateDatatypeCtrl', function($scope, $modalInstance, selectedNode, $rootScope, $q) {
    $scope.selectedNode = selectedNode;
    $scope.constraintType = 'Plain';
    $scope.constraints = [];
    $scope.firstConstraint = null;
    $scope.secondConstraint = null;
    $scope.compositeType = null;
    $scope.complexConstraint = null;
    $scope.changed = false;
    $scope.existingPredicate = null;
    $scope.tempPredicates = [];
    $scope.selectedDatatype = angular.copy($rootScope.datatype);
    $scope.predicateData = null;

    $scope.treeDataForContext = [];
    $scope.treeDataForContext.push($scope.selectedDatatype);
    $scope.treeDataForContext[0].pathInfoSet = [];
    $scope.generatePathInfo = function(current, positionNumber, locationName, instanceNumber, isInstanceNumberEditable, nodeName) {
        var pathInfo = {};
        pathInfo.positionNumber = positionNumber;
        pathInfo.locationName = locationName;
        pathInfo.nodeName = nodeName;
        pathInfo.instanceNumber = instanceNumber;
        pathInfo.isInstanceNumberEditable = isInstanceNumberEditable;
        current.pathInfoSet.push(pathInfo);

        if (current.type == 'datatype') {
            var dt = current;
            for (var i in dt.components) {
                var c = dt.components[i];
                c.pathInfoSet = angular.copy(current.pathInfoSet);

                var childPositionNumber = c.position;
                var childLocationName = c.position;
                var childNodeName = c.name;
                var childInstanceNumber = "1";
                var childisInstanceNumberEditable = false;
                var child = angular.copy($rootScope.datatypesMap[c.datatype.id]);
                child.id = new ObjectId().toString();
                c.child = child;
                $scope.generatePathInfo(c, childPositionNumber, childLocationName, childInstanceNumber, childisInstanceNumberEditable, childNodeName);
            }
        } else if (current.type == 'component') {
            var dt = current.child;
            for (var i in dt.components) {
                var c = dt.components[i];
                c.pathInfoSet = angular.copy(current.pathInfoSet);
                var childPositionNumber = c.position;
                var childLocationName = c.position;
                var childNodeName = c.name;
                var childInstanceNumber = "1";
                var childisInstanceNumberEditable = false;
                var child = angular.copy($rootScope.datatypesMap[c.datatype.id]);
                child.id = new ObjectId().toString();
                c.child = child;
                $scope.generatePathInfo(c, childPositionNumber, childLocationName, childInstanceNumber, childisInstanceNumberEditable, childNodeName);
            }
        }
    };

    $scope.generatePathInfo($scope.treeDataForContext[0], ".", ".", "1", false);

    $scope.toggleChildren = function(data) {
        data.childrenVisible = !data.childrenVisible;
        data.folderClass = data.childrenVisible ? "fa-minus" : "fa-plus";
    };

    $scope.beforeNodeDrop = function() {
        var deferred = $q.defer();
        deferred.resolve();
        return deferred.promise;
    };

    $scope.afterNodeDrop = function() {
        $scope.draggingStatus = null;
        $scope.newConstraint.pathInfoSet_1 = $scope.firstNodeData.pathInfoSet;
        $scope.generateFirstPositionAndLocationPath();
    };

    $scope.afterSecondNodeDrop = function() {
        $scope.draggingStatus = null;
        $scope.newConstraint.pathInfoSet_2 = $scope.secondNodeData.pathInfoSet;
        $scope.generateSecondPositionAndLocationPath();
    };

    $scope.beforePredicateDrop = function() {
        var deferred = $q.defer();

        if ($scope.draggingStatus === 'PredicateDragging') {
            $scope.predicateData = null;
            deferred.resolve();
        } else {
            deferred.reject();
        }
        return deferred.promise;
    };

    $scope.afterPredicateDrop = function() {
        $scope.draggingStatus = null;
        $scope.existingPredicate = $scope.predicateData;
        $scope.existingPredicate.constraintTarget = $scope.selectedNode.position + '[1]';
    };



    $scope.draggingPredicate = function(event, ui, nodeData) {
        $scope.draggingStatus = 'PredicateDragging';
    };

    $scope.draggingNodeFromContextTree = function(event, ui, data) {
        $scope.draggingStatus = 'ContextTreeNodeDragging';
    };

    $scope.setChanged = function() {
        $scope.changed = true;
    }

    $scope.initPredicate = function() {
        $scope.newConstraint = angular.fromJson({
            pathInfoSet_1: null,
            pathInfoSet_2: null,
            position_1: null,
            position_2: null,
            location_1: null,
            location_2: null,
            freeText: null,
            verb: null,
            ignoreCase: false,
            contraintType: null,
            value: null,
            value2: null,
            valueSetId: null,
            bindingStrength: 'R',
            bindingLocation: '1'
        });
    };

    $scope.initComplexPredicate = function() {
        $scope.constraints = [];
        $scope.firstConstraint = null;
        $scope.secondConstraint = null;
        $scope.compositeType = null;
    };

    $scope.generateFirstPositionAndLocationPath = function() {
        if ($scope.newConstraint.pathInfoSet_1) {
            var positionPath = '';
            var locationPath = '';
            for (var i in $scope.newConstraint.pathInfoSet_1) {
                if (i > 0) {
                    var pathInfo = $scope.newConstraint.pathInfoSet_1[i];
                    positionPath = positionPath + "." + pathInfo.positionNumber + "[" + pathInfo.instanceNumber + "]";
                    locationPath = locationPath + "." + pathInfo.locationName + "[" + pathInfo.instanceNumber + "]";

                    if (i == $scope.newConstraint.pathInfoSet_1.length - 1) {
                        locationPath = locationPath + " (" + pathInfo.nodeName + ")";
                    }
                }
            }

            $scope.newConstraint.position_1 = positionPath.substr(1);
            $scope.newConstraint.location_1 = $rootScope.datatype.name + '.' + locationPath.substr(1);
        }
    };

    $scope.generateSecondPositionAndLocationPath = function() {
        if ($scope.newConstraint.pathInfoSet_2) {
            var positionPath = '';
            var locationPath = '';
            for (var i in $scope.newConstraint.pathInfoSet_2) {
                if (i > 0) {
                    var pathInfo = $scope.newConstraint.pathInfoSet_2[i];
                    positionPath = positionPath + "." + pathInfo.positionNumber + "[" + pathInfo.instanceNumber + "]";
                    locationPath = locationPath + "." + pathInfo.locationName + "[" + pathInfo.instanceNumber + "]";

                    if (i == $scope.newConstraint.pathInfoSet_2.length - 1) {
                        locationPath = locationPath + " (" + pathInfo.nodeName + ")";
                    }
                }
            }

            $scope.newConstraint.position_2 = positionPath.substr(1);
            $scope.newConstraint.location_2 = $rootScope.datatype.name + '.' + locationPath.substr(1);
        }
    };

    $scope.findExistingPredicate = function() {
        for (var i = 0, len1 = $scope.selectedDatatype.predicates.length; i < len1; i++) {
            if ($scope.selectedDatatype.predicates[i].constraintTarget.indexOf($scope.selectedNode.position + '[') === 0)
                return $scope.selectedDatatype.predicates[i];
        }
    };

    $scope.deletePredicate = function() {
        $scope.existingPredicate = null;
        $scope.setChanged();
    };

    $scope.deleteTempPredicate = function(predicate) {
        $scope.tempPredicates.splice($scope.tempPredicates.indexOf(predicate), 1);
    };

    $scope.deletePredicateByTarget = function() {
        for (var i = 0, len1 = $scope.selectedDatatype.predicates.length; i < len1; i++) {
            if ($scope.selectedDatatype.predicates[i].constraintTarget.indexOf($scope.selectedNode.position + '[') === 0) {
                $scope.selectedDatatype.predicates.splice($scope.selectedDatatype.predicates.indexOf($scope.selectedDatatype.predicates[i]), 1);
                return true;
            }
        }
        return false;
    };

    $scope.addComplexPredicate = function() {
        $scope.complexConstraint = $rootScope.generateCompositePredicate($scope.compositeType, $scope.firstConstraint, $scope.secondConstraint, $scope.constraints);
        $scope.complexConstraint.constraintId = $scope.newConstraint.segment + '-' + $scope.selectedNode.position;
        $scope.tempPredicates.push($scope.complexConstraint);
        $scope.initComplexPredicate();
        $scope.changed = true;
    };

    $scope.addFreeTextPredicate = function() {
        var cp = $rootScope.generateFreeTextPredicate($scope.selectedNode.position + '[1]', $scope.newConstraint);
        $scope.tempPredicates.push(cp);
        $scope.changed = true;
        $scope.initPredicate();
    };

    $scope.addPredicate = function() {
        var cp = $rootScope.generatePredicate($scope.selectedNode.position + '[1]', $scope.newConstraint);
        $scope.tempPredicates.push(cp);
        $scope.changed = true;
        $scope.initPredicate();
    };

    $scope.ok = function() {
        $modalInstance.close();
    };

    $scope.saveclose = function() {
        $scope.deletePredicateByTarget();
        $scope.selectedDatatype.predicates.push($scope.existingPredicate);
        $rootScope.recordChanged();
        $modalInstance.close($scope.selectedDatatype);
    };

    $scope.initPredicate();
    $scope.initComplexPredicate();
    $scope.existingPredicate = $scope.findExistingPredicate();
});

angular.module('igl').controller('AddComponentCtrl', function($scope, $modalInstance, datatypes, datatype, valueSets, $rootScope, $http, ngTreetableParams, SegmentService, DatatypeLibrarySvc, MessageService, blockUI) {

    $scope.valueSets = valueSets;
    $scope.datatypes = datatypes;


    $scope.newComponent = {
        comment: "",
        confLength: "",
        datatype: {
            ext: null,
            id: "",
            label: "",
            name: "",
        },
        hide: false,
        id: "",
        maxLength: "",
        minLength: "",
        name: "",
        position: "",
        table: {
            bindingIdentifier: "",
            bindingLocation: null,
            bindingStrength: null,
            id: ""
        },
        text: "",
        type: "component",
        usage: ""


    };

    $scope.$watch('DT', function() {
        if ($scope.DT) {
            $scope.newComponent.datatype.ext = $scope.DT.ext;
            $scope.newComponent.datatype.id = $scope.DT.id;
            $scope.newComponent.datatype.name = $scope.DT.name;
            $scope.newComponent.datatype.label = $scope.DT.label;


        }
        console.log($scope.DT);

    }, true);

    $scope.$watch('VS', function() {
        if ($scope.VS) {
            $scope.newComponent.table.bindingIdentifier = $scope.VS.bindingIdentifier;
            $scope.newComponent.table.id = $scope.VS.id;


        }

    }, true);


    $scope.selectDT = function(datatype) {
        $scope.DT = datatype;
    };
    $scope.selectedDT = function() {
        return ($scope.DT !== undefined);
    };
    $scope.unselectDT = function() {
        $scope.DT = undefined;
    };
    $scope.isDTActive = function(id) {
        if ($scope.DT) {
            return $scope.DT.id === id;
        } else {
            return false;
        }

    };
    $scope.selectUsage = function(usage) {
        console.log(usage);
        if (usage === 'X' || usage === 'W') {
            $scope.newComponent.max = 0;
            $scope.newComponent.min = 0;
            $scope.disableMin = true;
            $scope.disableMax = true;

        } else if (usage === 'R') {
            $scope.newComponent.min = 1;

            $scope.disableMin = true;
            $scope.disableMax = false;
        } else if (usage === 'RE' || usage === 'O') {
            $scope.newComponent.min = 0;

            $scope.disableMin = true;
            $scope.disableMax = false;

        } else {
            $scope.disableMin = false;
            $scope.disableMax = false;

        }

    };


    $scope.selectVS = function(valueSet) {
        $scope.VS = valueSet;
    };
    $scope.selectedVS = function() {
        return ($scope.VS !== undefined);
    };
    $scope.unselectVS = function() {
        $scope.VS = undefined;
    };
    $scope.isVSActive = function(id) {
        if ($scope.VS) {
            return $scope.VS.id === id;
        } else {
            return false;
        }

    };


    $scope.addComponent = function() {
        blockUI.start();
        if ($rootScope.datatype.components.length !== 0) {
            $scope.newComponent.position = $rootScope.datatype.components[$rootScope.datatype.components.length - 1].position + 1;

        } else {
            $scope.newComponent.position = 1;
        }

        $scope.newComponent.id = new ObjectId().toString();

        if ($rootScope.datatype != null) {
            if (!$rootScope.datatype.components || $rootScope.datatype.components === null)
                $rootScope.datatype.components = [];
            $rootScope.datatype.components.push($scope.newComponent);
            MessageService.updatePosition(datatype.components, $scope.newComponent.position - 1, $scope.position - 1);





        }
        blockUI.stop();
        $modalInstance.close();

    };


    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };


});

angular.module('igl').controller('DeleteComponentCtrl', function($scope, $modalInstance, componentToDelete, datatype, $rootScope, SegmentService, blockUI) {
    $scope.componentToDelete = componentToDelete;
    $scope.loading = false;
    console.log(datatype);
    console.log($scope.componentToDelete);
    $scope.updatePosition = function(node) {
        angular.forEach(node.components, function(component) {
            component.position = node.components.indexOf(component) + 1;

        })

    };
    $scope.delete = function() {
        blockUI.start();
        $scope.loading = true;
        datatype.components.splice(componentToDelete.position - 1, 1);


        $rootScope.msg().text = "ComponentDeleteSuccess";

        $rootScope.msg().type = "success";
        $rootScope.msg().show = true;
        $rootScope.manualHandle = true;
        $scope.loading = false;
        $scope.updatePosition(datatype);
        $modalInstance.close($scope.componentToDelete);
        blockUI.stop();

    };


    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };


});

angular.module('igl').controller('cmpDatatypeCtrl', function($scope, $modal, ObjectDiff, orderByFilter, $rootScope, $q, $interval, ngTreetableParams, $http, StorageService, userInfoService, IgDocumentService, SegmentService, DatatypeService, SegmentLibrarySvc, DatatypeLibrarySvc, TableLibrarySvc, CompareService) {
    var ctrl = this;
    this.datatypeId = -1;
    $scope.dtChanged = false;
    $scope.isDeltaCalled = false;
    $scope.setDeltaToF = function() {
        console.log("HEEEEEERREEEEE");
        $scope.isDeltaCalled = false;
    }



    $scope.scopes = [{
        name: "USER",
        alias: "My IG"
    }, {
        name: "HL7STANDARD",
        alias: "Base HL7"
    }];

    $scope.getLabel = function(element) {
        if (element) {
            if (element.ext !== null && element.ext !== "") {
                return element.name + "_" + element.ext;
            } else {
                return element.name;
            }
        }

    };
    var listHL7Versions = function() {
        return $http.get('api/igdocuments/findVersions', {
            timeout: 60000
        }).then(function(response) {
            var hl7Versions = [];
            var length = response.data.length;
            for (var i = 0; i < length; i++) {
                hl7Versions.push(response.data[i]);
            }
            return hl7Versions;
        });
    };
    $scope.status = {
        isCustomHeaderOpen: false,
        isFirstOpen: true,
        isSecondOpen: true,
        isFirstDisabled: false
    };

    $scope.initt = function() {
        $scope.isDeltaCalled = true;
        $scope.dataList = [];
        listHL7Versions().then(function(versions) {
            $scope.versions = versions;
            if ($rootScope.igdocument && $rootScope.igdocument != null) {
                $scope.datatype1 = angular.copy($rootScope.datatype);

                $scope.version1 = angular.copy($scope.datatype1.hl7Version);
                $scope.scope1 = "USER";
                $scope.ig1 = angular.copy($rootScope.igdocument.metaData.title);
                ctrl.datatypeId = -1;
                $scope.variable = !$scope.variable;


                $scope.segments2 = null;
                //$scope.setIG2($scope.ig2);
                $scope.segList1 = angular.copy($rootScope.segments);
                $scope.dtList1 = angular.copy($rootScope.datatypes);
                $scope.version2 = angular.copy($scope.version1);
                console.log($scope.scopes);
                console.log($scope.scopes[1]);
            }
            //$scope.status.isFirstOpen = true;
            $scope.scope2 = "HL7STANDARD";
            if ($scope.dynamicDt_params) {
                $scope.showDelta = false;
                $scope.status.isFirstOpen = true;
                $scope.dynamicDt_params.refresh();
            }

        });



    };

    $scope.$on('event:loginConfirmed', function(event) {
        $scope.initt();
    });

    //$scope.initt();

    $rootScope.$on('event:initDatatype', function(event) {
        console.log("$scope.isDeltaCalled");
        $scope.getAllVersionsOfDT($rootScope.datatype.id);
        console.log($scope.isDeltaCalled);
        if ($scope.isDeltaCalled) {
            $scope.initt();
        }
    });
    $rootScope.$on('event:openDTDelta', function(event) {
        $scope.initt();
    });



    $scope.setVersion2 = function(vr) {
        $scope.version2 = vr;

    };
    $scope.setScope2 = function(scope) {

        $scope.scope2 = scope;
    };

    $scope.$watchGroup(['datatype1', 'datatype2'], function() {
        $scope.dtChanged = true;
        //$scope.segment1 = angular.copy($rootScope.activeSegment);
    }, true);
    $scope.$watchGroup(['version2', 'scope2', 'variable'], function() {
        $scope.igList2 = [];
        $scope.segments2 = [];
        $scope.ig2 = "";
        if ($scope.scope2 && $scope.version2) {
            IgDocumentService.getIgDocumentsByScopesAndVersion([$scope.scope2], $scope.version2).then(function(result) {
                if (result) {
                    if ($scope.scope2 === "HL7STANDARD") {
                        $scope.igDisabled2 = true;
                        $scope.ig2 = {
                            id: result[0].id,
                            title: result[0].metaData.title
                        };
                        $scope.igList2.push($scope.ig2);

                        $scope.setIG2($scope.ig2);
                    } else {
                        $scope.igDisabled2 = false;
                        for (var i = 0; i < result.length; i++) {
                            $scope.igList2.push({
                                id: result[i].id,
                                title: result[i].metaData.title,
                            });
                        }
                    }
                }
            });

        }

    }, true);
    $scope.setDatatype2 = function(datatype) {
        if (datatype === -1) {
            $scope.datatype2 = {};
        } else {
            $scope.datatype2 = $scope.datatypes2[datatype];

        }
    };
    $scope.setIG2 = function(ig) {
        if (ig) {
            IgDocumentService.getOne(ig.id).then(function(igDoc) {
                SegmentLibrarySvc.getSegmentsByLibrary(igDoc.profile.segmentLibrary.id).then(function(segments) {
                    DatatypeLibrarySvc.getDatatypesByLibrary(igDoc.profile.datatypeLibrary.id).then(function(datatypes) {
                        TableLibrarySvc.getTablesByLibrary(igDoc.profile.tableLibrary.id).then(function(tables) {
                            $scope.segments2 = [];
                            $scope.segment2 = "";
                            if (igDoc) {
                                $scope.segList2 = angular.copy(segments);
                                $scope.dtList2 = angular.copy(datatypes);
                                $scope.tableList2 = angular.copy(tables);

                                $scope.datatypes2 = orderByFilter(datatypes, 'name');
                            }
                        });
                    });
                });

            });


        }

    };

    $scope.hideDT = function(dt1, dt2) {

        if (dt2) {
            return !(dt1.name === dt2.name);
        } else {
            return false;
        }
    };
    $scope.disableDT = function(dt1, dt2) {

        if (dt2) {
            return (dt1.id === dt2.id);
        } else {
            return false;
        }
    };




    $scope.dynamicDt_params = new ngTreetableParams({
        getNodes: function(parent) {
            if ($scope.dataList !== undefined) {

                //return parent ? parent.fields : $scope.test;
                if (parent) {
                    if (parent.fields) {
                        return parent.fields;
                    } else if (parent.components) {
                        return parent.components;
                    } else if (parent.segments) {
                        return parent.segments;
                    } else if (parent.codes) {
                        return parent.codes;
                    }

                } else {
                    return $scope.dataList;
                }

            }
        },
        getTemplate: function(node) {
            return 'tree_node';
        }
    });
    $scope.cmpDatatype = function(datatype1, datatype2) {

        $scope.loadingSelection = true;
        $scope.dtChanged = false;
        $scope.vsTemplate = false;
        $scope.dataList = CompareService.cmpDatatype(JSON.stringify(datatype1), JSON.stringify(datatype2), $scope.dtList1, $scope.dtList2, $scope.segList1, $scope.segList2);
        console.log("hg==========");
        console.log($scope.dataList);
        $scope.loadingSelection = false;
        if ($scope.dynamicDt_params) {
            console.log($scope.dataList);
            $scope.showDelta = true;
            $scope.status.isSecondOpen = true;
            $scope.dynamicDt_params.refresh();
        }

    };
});

angular.module('igl').controller('DeleteDatatypePredicateCtrl', function($scope, $modalInstance, position, datatype, $rootScope) {
    $scope.selectedDatatype = datatype;
    $scope.position = position;
    $scope.delete = function() {
        for (var i = 0, len1 = $scope.selectedDatatype.predicates.length; i < len1; i++) {
            if ($scope.selectedDatatype.predicates[i].constraintTarget.indexOf($scope.position + '[') === 0) {
                $scope.selectedDatatype.predicates.splice($scope.selectedDatatype.predicates.indexOf($scope.selectedDatatype.predicates[i]), 1);
                $modalInstance.close();
                return;
            }
        }
        $modalInstance.close();
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
});


angular.module('igl').controller('AddBindingForDatatype', function($scope, $modalInstance, $rootScope, datatype) {
    console.log($rootScope.references);
    $scope.datatype = datatype;
    $scope.selectedSegmentForBinding = null;
    $scope.selectedFieldForBinding = null;
    $scope.selectedDatatypeForBinding = null;
    $scope.selectedComponentForBinding = null;

    $scope.pathForBinding = null;
    $scope.bindingTargetType = 'DATATYPE';

    $scope.init = function() {
        $scope.selectedSegmentForBinding = null;
        $scope.selectedFieldForBinding = null;
        $scope.selectedDatatypeForBinding = null;
        $scope.selectedComponentForBinding = null;
        $scope.pathForBinding = null;
        $scope.currentField = null;
        $scope.currentComp = null;

    };

    $scope.checkDuplicated = function(path) {
        for (var i = 0; i < $rootScope.references.length; i++) {
            var ref = $rootScope.references[i];
            if (ref.path==path) return true;
        }
        return false;
    };

    $scope.selectSegment = function() {
        $scope.selectedFieldForBinding = null;
        $scope.currentField = null;
    };
    $scope.selectField = function() {
        console.log($scope.selectedFieldForBinding);
        if ($scope.selectedFieldForBinding) {
            $scope.currentField = JSON.parse($scope.selectedFieldForBinding);
            console.log($rootScope.datatypesMap[$scope.currentField.datatype.id]);

        }
    };
    $scope.selectComp = function() {
        if ($scope.selectedComponentForBinding) {
            $scope.currentComp = JSON.parse($scope.selectedComponentForBinding);

        }
    };


    $scope.selectDatatype = function() {
        $scope.selectedComponentForBinding = null;
        $scope.currentComp = null;
    };

    $scope.save = function(bindingTargetType) {
        var datatypeLink = {};
        datatypeLink.id = $scope.datatype.id;
        datatypeLink.name = $scope.datatype.bindingIdentifier;
        datatypeLink.ext = $scope.datatype.ext;
        datatypeLink.label = $scope.datatype.label;
        datatypeLink.isChanged = true;
        datatypeLink.isNew = true;

        if (bindingTargetType == 'SEGMENT') {
            $scope.selectedFieldForBinding = JSON.parse($scope.selectedFieldForBinding);
            $scope.pathForBinding = $rootScope.getSegmentLabel($scope.selectedSegmentForBinding) + '-' + $scope.selectedFieldForBinding.position;

            var ref = angular.copy($scope.selectedFieldForBinding);
            ref.path = $scope.pathForBinding;
            ref.target = angular.copy($scope.selectedSegmentForBinding);
            ref.datatypeLink = angular.copy(datatypeLink);
            $rootScope.references.push(ref);
        } else {
            $scope.selectedComponentForBinding = JSON.parse($scope.selectedComponentForBinding);
            $scope.pathForBinding = $rootScope.getDatatypeLabel($scope.selectedDatatypeForBinding) + '-' + $scope.selectedComponentForBinding.position;

            var ref = angular.copy($scope.selectedComponentForBinding);
            ref.path = $scope.pathForBinding;
            ref.target = angular.copy($scope.selectedDatatypeForBinding);
            ref.datatypeLink = angular.copy(datatypeLink);
            $rootScope.references.push(ref);
        }

        $modalInstance.close();
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
});

angular.module('igl').controller('ShareDatatypeCtrl', function($scope, $modalInstance, $http, igdocumentSelected, userList, DatatypeService, $rootScope) {

    $scope.igdocumentSelected = igdocumentSelected;

    // Add participants username and fullname
    // Find share participants
    if ($scope.igdocumentSelected.shareParticipantIds && $scope.igdocumentSelected.shareParticipantIds.length > 0) {
        $scope.igdocumentSelected.shareParticipantIds.forEach(function(participant) {
            $http.get('api/shareparticipant', { params: { id: participant.accountId } })
                .then(
                    function(response) {
                        participant.username = response.data.username;
                        participant.fullname = response.data.fullname;
                    },
                    function(error) {
                        console.log(error);
                    }
                );
        });
    }
    $scope.userList = userList;
    $scope.error = "";
    $scope.tags = [];
    $scope.ok = function() {
        var idsTab = $scope.tags.map(function(user) {
            return user.accountId;
        });

        DatatypeService.share($scope.igdocumentSelected.id, idsTab, $rootScope.accountId).then(function(result) {
            // Add participants for direct view
            $scope.igdocumentSelected.shareParticipantIds = $scope.igdocumentSelected.shareParticipantIds || [];
            $scope.tags.forEach(function(tag) {
                tag.permission = $scope.selectedItem.selected;
                tag.pendingApproval = true;
                $scope.igdocumentSelected.shareParticipantIds.push(tag);
            });
            $rootScope.msg().text = "dtSharedSuccessfully";
            $rootScope.msg().type = "success";
            $rootScope.msg().show = true;
            $modalInstance.close();
        }, function(error) {
            $scope.error = error.data;
            console.log(error);
        });
    };
    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };

    $scope.selectedItem = {
        selected: "VIEW"
    };
    $scope.itemArray = ["VIEW"];

    $scope.loadUsernames = function($query) {
        return userList.filter(function(user) {
            return user.username.toLowerCase().indexOf($query.toLowerCase()) != -1;
        });
    };

    $scope.unshare = function(shareParticipant) {
        $scope.loading = false;
        DatatypeService.unshare($scope.igdocumentSelected.id, shareParticipant.accountId).then(function(res) {
            var indexOfId = $scope.igdocumentSelected.shareParticipantIds.indexOf(shareParticipant.accountId);
            if (indexOfId > -1) {
                $scope.igdocumentSelected.shareParticipantIds.splice(indexOfId, 1);
            }
            var participantIndex = -1;
            for (var i = 0; i < $scope.igdocumentSelected.shareParticipantIds.length; i++) {
                if ($scope.igdocumentSelected.shareParticipantIds[i].accountId === shareParticipant.accountId) {
                    participantIndex = i;
                    $scope.userList.push($scope.igdocumentSelected.shareParticipantIds[i]);
                    break;
                }
            }
            if (participantIndex > -1) {
                $scope.igdocumentSelected.shareParticipantIds.splice(participantIndex, 1);
            }
            $scope.loading = false;
            $rootScope.msg().text = "dtUnSharedSuccessfully";
            $rootScope.msg().type = "success";
            $rootScope.msg().show = true;
        }, function(error) {
            $rootScope.msg().text = error.data.text;
            $rootScope.msg().type = error.data.type;
            $rootScope.msg().show = true;
            $scope.loading = false;
        });
    };


});
angular.module('igl').controller('ConfirmDatatypePublishCtl', function($scope, $rootScope, $http, $modalInstance, datatypeToPublish) {

    $scope.datatypeToPublish = datatypeToPublish;
    $scope.loading = false;

    $scope.delete = function() {
        $modalInstance.close($scope.datatypeToPublish);
    };

    $scope.cancel = function() {
        $scope.datatypeToPublish.status = 'UNPUBLISHED';
        $rootScope.clearChanges();
        $modalInstance.dismiss('cancel');
    };
});

angular.module('igl').controller('AbortPublishCtl', function($scope, $rootScope, $http, $modalInstance, datatypeToPublish, unpublishedDatatypes, unpublishedTables) {

    $scope.datatypeToPublish = datatypeToPublish;
    $scope.loading = false;
    $scope.unpublishedDatatypes = unpublishedDatatypes;
    $scope.unpublishedTables = unpublishedTables;

    $scope.delete = function() {
        $modalInstance.close($scope.datatypeToPublish);
    };

    $scope.cancel = function() {
        //$scope.datatypeToPublish.status = "'UNPUBLISHED'";
        $modalInstance.dismiss('cancel');
    };
});