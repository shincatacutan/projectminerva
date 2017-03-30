/**
 * 
 */

var loadAppSettings = function() {
	$.ajax({
		url : contextPath + "/getProperties",
		type : "POST",
		accept : 'application/json',
		success : function(data) {

			$.each(data, function(i, obj) {
				switch (obj.id) {
				case "minerva.default.password":
					$("#defaultPassword").val(obj.value);
					break;
				case "minerva.inquiry.email.recipient":
					var tags = obj.value.split(",");

					$.each(tags, function(e) {
						$('#recipients').tagsinput('add', tags[e]);
					});
					break;
				case "minerva.docs.email.recipient":
					var tags = obj.value.split(",");

					$.each(tags, function(e) {
						$('#docrecipients').tagsinput('add', tags[e]);
					});
					break;
				case "minerva.appname":
					$("#appName").val(obj.value);
					break;
				case "minerva.hostname":
					$("#hostName").val(obj.value);
					break;
				case "minerva.server.port":
					$("#serverPort").val(obj.value);
					break;
				case "minerva.email.sender":
					$("#emailSender").val(obj.value);
					break;
				case "minerva.email.identity":
					$("#emailIdentity").val(obj.value);
					break;
				default:
					break;
				}

			});
		},
		error : function(e) {
			// //console.log(e);
		}

	});
}

var loadAllUsernames = function() {

	$.ajax({
		url : contextPath + "/getAllUsernames",
		type : "POST",
		accept : 'application/json',
		success : function(data) {
			$('#user-autocomplete').typeahead({
				source : data,
				onSelect : function(item) {
					loadUserInfo(item.value);

				}
			});
		},
		error : function(e) {
			// //console.log(e);
		}

	});
}

var loadUserInfo = function(username) {
	if (username != "-21") {
		$.ajax({
			url : contextPath + "/getUserInfo",
			type : "POST",
			data : {
				'username' : username
			},
			accept : 'application/json',
			success : function(data) {
				// console.log(data);
				$("#username_up").val(data.username);
				$("#empID_up").val(data.empid);
				$("#lastname_up").val(data.lastName);
				$("#firstname_up").val(data.firstName);
				$("#email_up").val(data.email);
				var roles = new Array();
				$.each(data.userRole, function(item, value) {
					// console.log(value.role)
					roles.push(value.role);
				});

				if (!data.enabled) {
					$("#unlock_user_btn").show();
					$("#deactvt_user_btn").hide();

					$("#unlock_user_btn").click(function(event) {
						var mapForm = document.createElement("form");
						mapForm.method = "POST";
						mapForm.action = contextPath + "/unlockUser";

						var mapInput = document.createElement("input");
						mapInput.type = "hidden";
						mapInput.name = "username";
						mapInput.value = $("#username_up").val();
						mapForm.appendChild(mapInput);
						document.body.appendChild(mapForm);

						mapForm.submit();

						// Remove mapForm
						document.body.removeChild(mapForm);

						event.preventDefault();
					});
				} else {
					$("#deactvt_user_btn").show();
					$("#unlock_user_btn").hide();
				}

				// console.log(roles)
				$("#roles-selection_up").val(roles);
				$("#populate-div").show();
			},
			error : function(e) {
				// //console.log(e);
			}
		});
	}

}

$(function() {

	disableEnterKey();
	loadAllUsernames();
	$("#populate-div").hide();
	$("#unlock_user_btn").hide();
	$('#archive-date').datepicker({
		autoclose : true,
		todayHighlight : true
	});

	$("#delete_user_btn").click(function(event) {
		var mapForm = document.createElement("form");
		mapForm.method = "POST";
		mapForm.action = contextPath + "/deleteUser";

		var mapInput = document.createElement("input");
		mapInput.type = "hidden";
		mapInput.name = "username";
		mapInput.value = $("#username_up").val();
		mapForm.appendChild(mapInput);
		document.body.appendChild(mapForm);

		mapForm.submit();

		// Remove mapForm
		document.body.removeChild(mapForm);

		event.preventDefault();
	});

	$('#deactvt_user_btn').on('click', function(e) {

		bootbox.dialog({
			title : "Confirm",
			message : "Proceed in deactivating the user",
			buttons : {
				"Cancel" : {
					className : "btn-default",
					callback : function() {

					}
				},
				success : {
					label : "OK",
					className : "btn-primary",
					callback : function() {
						var mapForm = document.createElement("form");
						mapForm.method = "POST";
						mapForm.action = contextPath + "/deactivateUser";

						var mapInput = document.createElement("input");
						mapInput.type = "hidden";
						mapInput.name = "username";
						mapInput.value = $("#username_up").val();
						mapForm.appendChild(mapInput);
						document.body.appendChild(mapForm);

						mapForm.submit();

						// Remove mapForm
						document.body.removeChild(mapForm);
					}
				}

			},
			closeButton : true
		});
	});

	$("#parent-cat").hide();
	$("#cat_type").change(function() {
		var type = $("#cat_type").val();
		if ("sub_cat" == type) {
			$.ajax({
				url : contextPath + "/getCategories",
				type : "POST",
				accept : 'application/json',
				success : function(data) {
					$("#selection_cat").categories(data);
				},
				error : function(e) {
					// console.log(e);
				}
			});
			$("#parent-cat").show();
		} else {
			$("#parent-cat").hide();

		}
	});

	loadAppSettings();
	initCategories();
	getUnsentEmails();
});

var initCategories = function() {
	$.ajax({
		url : contextPath + "/getCategories",
		type : "POST",
		accept : 'application/json',
		success : function(data) {
			$('#selection_cat_del').categories(data);
		},
		error : function(e) {
			// console.log(e);
		}
	});

	$("#selection_cat_del").change(function() {
		var catId = $("#selection_cat_del").val();
		if (catId != "") {
			$.ajax({
				url : contextPath + "/getSubCategories",
				type : "POST",
				accept : 'application/json',
				data : {
					'cat' : catId
				},
				success : function(data) {
					$('#subCat_in').categories(data);
				},
				error : function(e) {
					// console.log(e);
				}
			});
		}
	});
}
jQuery.extend(jQuery.fn, {
	categories : function(data) {
		var input = $(this);
		input.find('option').remove().end();
		input.append($('<option>', {
			text : "Select",
			value : "-2"
		}));
		$.each(data, function(i, data) {
			input.append($('<option>', {
				text : data.name,
				value : data.id
			}));
		});
	}
});
