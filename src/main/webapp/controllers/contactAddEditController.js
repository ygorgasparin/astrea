var contactAddEditController = function($scope, $http, $stateParams, $state, contactService) {

	$scope.contact = $stateParams.contact || {};

	if(!$scope.contact.emails)
	    $scope.contact.emails = [''];

    if(!$scope.contact.phones)
        $scope.contact.phones = [''];

	$scope.submitted = false;
	
	$scope.save = function() {

		$scope.submitted = true;

		if ($scope.contact.name != null && $scope.contact.name != "") {
		    contactService.save($scope.contact).then(function(result) {
		        $state.go('main.contacts');
		    }, function(result) {
		        alert(result.data.message);
		        console.log(result.data);
		    });
		}

	};

	$scope.addToList = function(list) {
	    list.push('');
	}

	$scope.deleteFromList = function(list, index) {
        if (index > -1)
            list.splice(index, 1);

        if (list.length < 1)
            $scope.addToList(list);
	}

};

angular.module('avaliacandidatos').controller("contactAddEditController", contactAddEditController);