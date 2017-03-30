$(function() {
	$("#change_password").click(function(event) {
		$('#viewModal').modal('show');
		event.preventDefault();
	});
	
	$("#save_pass").click(function(event){
		var newPassForm = $("#new-pass-form");
		newPassForm.submit();
	})
});
