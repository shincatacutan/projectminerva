var textAreaAction = function(id) {
	$(id).on('change keyup paste', function() {
		var desc_in = $(id);
		var remarksLength = desc_in.val().length;
		var maxLength = 500;
		if (remarksLength > maxLength) {
			desc_in.val(desc_in.val().substring(0, maxLength));
		}
		$("#availableChar").html(maxLength - desc_in.val().length);
	});
}



var initAddInquiryReply = function() {
	$("#save_inq_pop").click(function() {
		hasDirtyBit = true;
		var selectedId;

		if ($("#inquiryId").val()) {
			selectedId = $("#inquiryId").val();
		} else {
			var newInstance = $('#inquiry_grid').DataTable();
			selectedId = newInstance.rows('.selected').data()[0].id;
		}

		var formData = new FormData($("form#inquiry-view-form")[0])
		$.ajax({
			url : contextPath + "/addReply",
			type : "POST",
			accept : 'application/json',
			data : formData,
			processData : false,
			contentType : false,
			success : function(data) {
				showNotifyMsg('reply was added successfully');
				$("#reply_pop").val("");
				loadReplyHistory(selectedId);
			},
			error : function(e) {
				$("#reply_pop").val("");
				loadReplyHistory(selectedId);
			}

		});
	});
}

var loadLobs = function(fieldId) {
	$.ajax({
		url : contextPath + "/getLoBList",
		type : "POST",
		accept : 'application/json',
		success : function(data) {
			$(fieldId).lineofbusiness(data);
		},
		error : function(e) {
			// console.log(e);
		}
	});
}

var loadSMEs = function(fieldId) {
	$.ajax({
		url : contextPath + "/getInquirySMEs",
		type : "POST",
		accept : 'application/json',
		success : function(data) {
			$(fieldId).smes(data);
		},
		error : function(e) {
			// console.log(e);
		}
	});
}

var closeInquiry = function() {
	hasDirtyBit = true;
	var selectedId;
	if ($("#inquiryId").val()) {

		selectedId = $("#inquiryId").val();
	} else {
		var newInstance = $('#inquiry_grid').DataTable();
		selectedId = newInstance.rows('.selected').data()[0].id;

	}
	$.ajax({
		url : contextPath + "/closeInquiry",
		type : "POST",
		accept : 'application/json',
		data : {
			'inquiryId' : selectedId
		},
		success : function(data) {
			if (undefined == $("#viewModal").data('bs.modal')) {
				window.location = selectedId;
			} else {
				$('#viewModal').modal('toggle');
			}

		},
		error : function(e) {
			showNotifyMsg('Closing inquiry failed');
		}
	});
}
var searchByFilter = function() {
	$.ajax({
		url : contextPath + "/searchInquiry",
		type : "POST",
		accept : 'application/json',
		data : {
			'startDate' : $("#startDate").val(),
			'endDate' : $("#endDate").val(),
			'lineOfBus' : $("#lineOfBus").val()
		},
		success : function(data) {
			paintInqTable(data, "#src_inquiry_grid");
		},
		error : function(e) {
			// console.log(e);
		}

	});
}

var deleteInquiry = function() {
	var selectedId;
	if ($("#inquiryId").val()) {
		selectedId = $("#inquiryId").val();
	} else {
		selectedId = $('#id_change').val()

	}
	$.ajax({
		url : contextPath + "/deleteInquiry",
		type : "POST",
		accept : 'application/json',
		data : {
			'inquiryId' : selectedId
		},
		success : function(data) {
			if (undefined == $("#viewModal").data('bs.modal')) {
				bootbox.alert("Inquiry was deleted successfully.", function(){
					window.location = contextPath + "/inquiry/view";
				})
				
			} else {
				$('#viewModal').modal('hide');
				bootbox.alert("Inquiry was deleted successfully.", function(){
					searchByFilter();
				});	
			}
		},
		error : function(e) {
			showNotifyMsg('Delete inquiry failed');
		}
	});
}

var initCloseInquiry = function() {
	$("#close_inq_pop").click(function() {
		bootbox.confirm("Close selected inquiry?", function(result) {
			if (result) {
				closeInquiry();
			}
		});

	});
	//

	$("#delete_inq_pop").click(function() {
		bootbox.confirm("Delete selected inquiry?", function(result) {
			if (result) {
				deleteInquiry();
			}
		});
	});
}

var loadReplyHistory = function(inq_id) {
	$
			.ajax({
				url : contextPath + "/fetchReplies",
				type : "POST",
				accept : 'application/json',
				data : {
					'inquiryId' : inq_id
				},
				success : function(data) {
					$('#reply-table tbody tr').each(function() {
						$(this).remove();
					});
					data
							.forEach(function(row) {

								var test = function(e) {
									if (e < 10) {
										e = "0" + e;
									}
									return e;
								}
								var day = test(row.createDate.dayOfMonth);
								var month = test(row.createDate.monthOfYear);
								var hour = test(row.createDate.hourOfDay);
								var min = test(row.createDate.minuteOfHour);
								var sec = test(row.createDate.secondOfMinute);

								
								var createDate = month + "/" + day + "/"
										+ row.createDate.year + " " + hour
										+ ":" + min + ":" + sec;
								var tr = "<tr><td>" + row.replyBody
										+ "</td><td>" + createDate
										+ "</td><td>"
										+ row.createUser.firstName + " "
										+ row.createUser.lastName + "</td>";
								if (row.path) {
									tr = tr
											+ "<td>"
											+ "<a href='"+contextPath+"/downloadRepAttach?repId="
											+ row.id
											+ "'><span class='glyphicon glyphicon-download-alt'></span></a></td></tr>";
								} else {
									tr = tr + "<td></td></tr>";
								}
								$("#reply-table tbody").append(tr);
							});

				},
				error : function(e) {
					// console.log(e);
				}

			});
}
