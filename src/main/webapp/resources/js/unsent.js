/**
 * 
 */

var paintTable = function(oData, page) {
	if ($.fn.dataTable.isDataTable('#unsentEmailGrid')) {

		var table = $('#unsentEmailGrid').DataTable();
		table.clear();
		table.rows.add(oData);
		table.columns.adjust().draw();
	} else {
		var table = $('#unsentEmailGrid').dataTable(
				{
					"pageLength" : 10,
					"data" : oData,
					"columns" : [
							{
								"title" : "Title",
								"data" : "title",
								"class" : "dt-left"
							},
							{
								"title" : "Body",
								"data" : "body",
								"class" : "dt-left",
								"render" : function(str) {
									if (str.length > 40) {
										str = str.substring(0, 40);
										str = str + " ...";
									}
									return str
								}
							},
							{
								"title" : "Recipient",
								"data" : "recipient",
								"class" : "dt-left",
								"render" : function(str) {
									if (str.length > 40) {
										str = str.substring(0, 40);
										str = str + " ...";
									}
									return str
								}
							},
							{
								"title" : "Status",
								"data" : "status",
								"class" : "dt-left"
							},
							{
								"title" : "ID",
								"data" : "id",
								"class" : "dt-left",
								"visible" : false
							},
							{
								"title" : "Sender",
								"data" : "sender",
								"class" : "dt-left"
							},
							{
								"title" : "Timestamp",
								"data" : "timeSent",
								"class" : "dt-left",
								"render" : function(obj) {
									var str;
									str = obj.values[1] + "/" + obj.values[2]
											+ "/" + obj.values[0] + " "
											+ obj.hourOfDay + ":"
											+ obj.minuteOfHour + ":"
											+ obj.secondOfMinute;
									return str
								}
							} ]
				});

		$('#unsentEmailGrid tbody').on('click', 'tr', function() {
			if ($(this).hasClass('selected')) {
				$(this).removeClass('selected');
			} else {
				table.$('tr.selected').removeClass('selected');
				$(this).addClass('selected');
			}
		});

		$('#sendEmail')
				.on(
						'click',
						function(e) {

							bootbox
									.dialog({
										title : "Confirm",
										message : "Proceed in resending email",
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
													var newInstance = $(
															"#unsentEmailGrid")
															.DataTable();
													var emailData = newInstance
															.rows('.selected')
															.data()[0];
													// sendFailedEmail
													$
															.ajax({
																url : contextPath
																		+ "/sendFailedEmail",
																type : "POST",
																accept : 'application/json',
																data : {
																	'emailId' : emailData.id
																},
																success : function(
																		data) {
																
																	if (data == "FAILED") {
																		showNotifyMsg("Resending email failed. Couldn't connect to email host. Please retry later.");
																	} else {
																		showNotifyMsg("email was send successfully");
																		if(page=="admin"){
																			getUnsentEmails();
																		}else{
																			getUnsentEmailsByUser();
																		}
																		
																	}

																},
																error : function(
																		e) {
																	// //console.log(e);
																}

															});
												}
											}

										},
										closeButton : true
									});
						});
	}

};

var getUnsentEmails = function() {
	$.ajax({
		url : contextPath + "/getUnsentEmail",
		type : "POST",
		accept : 'application/json',
		success : function(data) {
			paintTable(data,"admin")
		},
		error : function(e) {
			// console.log(e);
			console.log(e)
		}
	});
}

var getUnsentEmailsByUser = function() {
	$.ajax({
		url : contextPath + "/getUnsentEmailByUser",
		type : "POST",
		accept : 'application/json',
		success : function(data) {
			console.log(data)
			paintTable(data, "user")
		},
		error : function(e) {
			// console.log(e);
			console.log(e)
		}
	});
}
