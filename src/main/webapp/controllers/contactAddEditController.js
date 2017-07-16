var contactAddEditController;

contactAddEditController = function($scope, $http, $stateParams, $state) {

    //TODO: obter request pelo id

	$scope.contact = $stateParams.contact || {};

	if(!$scope.contact.emails)
	    $scope.contact.emails = [''];

    if(!$scope.contact.phones)
        $scope.contact.phones = [''];

	$scope.submitted = false;
	
	$scope.save = function() {

		$scope.submitted = true;

		if ($scope.contact.name != null && $scope.contact.name != "") {
		    $http.post('/contacts', $scope.contact).then(function(result) {
		        $state.go('main.contacts');
		    }, function(result) {
		        alert(result.data.message);
		        console.log(result.data);
		    });
		}

	};

	$scope.addMorePhones = function() {
		$scope.contact.phones.push('');
	}; 

	$scope.addMoreEmails = function() {
		$scope.contact.emails.push('');
	};

	$scope.deletePhone = function(index){
		if (index > -1) {
    		$scope.contact.phones.splice(index, 1);
		}

		if ($scope.contact.phones.length < 1){
			$scope.addMorePhones();
		}
	};

	$scope.deleteEmail = function(index){
		if (index > -1) {
    		$scope.contact.emails.splice(index, 1);
		}

		if ($scope.contact.emails.length < 1){
			$scope.addMoreEmails();
		}
	};

};

angular.module('avaliacandidatos').controller("contactAddEditController", contactAddEditController);