angular.module('igl')
 .directive('trunk', function () {
	  console.log("trunk");

	  var template = "<div><branch ng-repeat='branch in trunk track by $index' branch='branch'></branch></div>";

  return {
      restrict: "E",
      replace: true,
      scope: {
          trunk: '='
      },
      template: template,
  }
})
.directive("branch", function ($compile) {
	  console.log("branch");
	  
	  var trunkTemplate = "<trunk trunk='branch.children'></trunk>";
	  var branchTemplate = "<div ng-class=\"{'yesyes':yesDrop, 'nono':noDrop}\"" + 
								" drop-container accepts='{{branch.drop}}'" + 
								" on-drag-enter='onBranchEnter(branch)' on-drag-leave='onBranchLeave(branch)' on-drop='onBranchDrop(branch)'>" +
								"<a class='point'" +
								"ng-click='collapsed[branch.label] = !collapsed[branch.label]'>" +
								"<span class='fa' ng-class=\"{'fa-caret-right': collapsed[branch.label],'fa-caret-down': !collapsed[branch.label]}\">" +
								"<b>{{branch.label}}</b></span>" + 
								"</a>" +
								"<div collapse='collapsed[branch.label]' ng-show='!collapsed[branch.label]' class='panel-body'><leaf ng-repeat='leaf in branch.children track by $index' leaf='leaf'></leaf></div>" +
								"</div>";
	  var branchMessageTemplate = "<div ng-class=\"{'yesyes':yesDrop, 'nono':noDrop}\"" + 
		" context-menu context-menu-close='closedCtxMenu(branch)' data-target='messageHeadContextDiv.html'" + 
		" drop-container accepts='{{branch.drop}}'" + 
		" on-drag-enter='onBranchEnter(branch)' on-drag-leave='onBranchLeave(branch)' on-drop='onBranchDrop(branch)'>" +
		"<a class='point'" +
		"ng-click='collapsed[branch.label] = !collapsed[branch.label]'>" +
		"<span class='fa' ng-class=\"{'fa-caret-right': collapsed[branch.label],'fa-caret-down': !collapsed[branch.label]}\">" +
		"<b>{{branch.label}}</b></span>" + 
		"</a>" +
		"<div collapse='collapsed[branch.label]' ng-show='!collapsed[branch.label]' class='panel-body'><leaf ng-repeat='leaf in branch.children track by $index' leaf='leaf'></leaf></div>" +
		"</div>";
// ng-show='branch.children && branch.children.length > 0'
	  var linker = function(scope, element, attrs) {
		  if (angular.isArray(scope.branch.children)) {
              // We must add the branchTemplate before we add the trunkTemplate.
			  if (scope.branch.label === "Messages") {
		             element.append(branchMessageTemplate).show();
				  
			  } else {
				  element.append(branchTemplate).show();
			  }
              $compile(element.contents())(scope);
              
              element.append(trunkTemplate);
              $compile(element.contents())(scope); 
		  }
	    }
	  
  return {
      restrict: "E",
      replace: true,
      controller: "ToCCtl",
      scope: {
          branch: '='
      	},
      link: linker
      }
})
.directive("leaf", function($compile) {
//	  var leafMetadata = "<li drag-container on-drag-start='onStart(leaf)' on-drag-stop='onStop(leaf)' on-drag-enter='onEnter(leaf)' on-drop='onDrop(leaf)' mime-type='{{leaf.drag}}'>{{leaf.label}}</li>";

	  var leafMessage = "<div" +
	  						" context-menu context-menu-close='closedCtxSubMenu(leaf)' data-target='messageContextDiv.html'" + 
	  						" drag-container mime-type='{{leaf.drag}}'" + 
	  						" on-drag-start='onLeafStart(leaf)' on-drag-stop='onLeafStop(leaf)' on-drag-enter='onLeafEnter(leaf)' on-drop='onLeafDrop(leaf)'" + 
	  						" ng-click='tocSelection(leaf)'>" +
						    "<a class='point txt'>" +
							"<span class='fa' ng-class=\"{'': !collapsed[leaf.label]}\">{{leaf.label}}</span>" +
	      				    "</a>" +
	  					"</div>";
 
	  var leafDefault = "<div" + 
	  						" drag-container mime-type='{{leaf.drag}}'" + 
	  						" on-drag-start='onLeafStart(leaf)' on-drag-stop='onLeafStop(leaf)' on-drag-enter='onLeafEnter(leaf)' on-drop='onLeafDrop(leaf)'" + 
	  						" ng-click='tocSelection(leaf)'>" +
						    "<a class='point txt'>" +
							"<span class='fa' ng-class=\"{'': !collapsed[leaf.label]}\">{{leaf.label}}</span>" +
	      				    "</a>" +
	  					"</div>";
	  
	  var linker = function(scope, element, attrs) {
	        if (scope.leaf.drag === "Messages") {
		        element.html(leafMessage).show();	        	
	        } else {
		        element.html(leafDefault).show();
	        }
	        $compile(element.contents())(scope);
	    }
	  
	  return {
	      restrict: "E",
	      replace: true,
	      controller: "ToCCtl",
	      scope: {
	          leaf: '='
	      	},
	      link: linker
	      }
})
