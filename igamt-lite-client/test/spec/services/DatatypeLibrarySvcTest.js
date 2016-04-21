'use strict';

describe("datatype library service", function () {
	// gcr This test is not ready for IGDocument.
	var DatatypeLibrarySvc;
  var froala;
	var datatypesAsString;
	var library;

	beforeEach(function() {
		module('igl');
		inject(function (_DatatypeLibrarySvc_, $injector, $rootScope, $controller, _froala_) {
			DatatypeLibrarySvc = _DatatypeLibrarySvc_;
      froala = _froala_;

// Don't ask me why, but the following fixtures path MUST have "base/" prepended or it won't work.
// Also, see the "pattern" thing, which is the last element of the files array in test/karma.conf.js.
/* 		 	jasmine.getJSONFixtures().fixturesPath='base/test/fixtures/datatypes/';
		 	var jsonFixture = getJSONFixture('datatypes-MASTER.json');
		 	datatypesAsString = JSON.stringify(jsonFixture);
		 	expect($rootScope).toBeDefined();
		 	expect(datatypesAsString).toBeDefined(); */
		});
		// We want a pristine document for each test so state changes from one test don't pollute
		// the others.
		library = JSON.parse(datatypesAsString);
	});

	it("Can we get HL7 versions?", function () {
		var hl7Versions = DatatypeLibrarySvc.getHL7Versions();
    expect(hl7Versions).toBeDefined();
    expect(hl7Versions.length > 0).toBe(true);
	});
});
