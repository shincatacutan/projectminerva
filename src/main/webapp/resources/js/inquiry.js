/**
 * 
 */
var loadViewInquiries = function(getAll, gridId) {
	$.ajax({
			url : contextPath + "/getAllInquiry",
			type : "POST",
			accept : 'application/json',
			data : {
				'getAll' : getAll
			},
			success : function(data) {
				paintInqTable(data, gridId);
			},
			error : function(e) {
				// console.log(e);
			}

		});
}

var paintInqTable = function(data, gridId){
	if ($.fn.dataTable.isDataTable(gridId)) {
		var table = $(gridId).DataTable();
		table.clear();
		table.rows.add(data);
		table.draw();
	} else {
		var table = $(gridId)
				.dataTable(
						{
							"pageLength" : 10,
							"order" : [ [ 1, "asc" ] ],
							// "ordering": false,
							"data" : data,
							"columns" : [
									{
										"title" : "ID",
										"data" : "id",
										"class" : "dt-left",
										"visible" : false
									},

									{
										"title" : "Status",
										"data" : "statusString",
										"class" : "dt-left",
										"render" : function(obj) {
											if (obj == "New") {
												return "<span class='new-status'>*"
														+ obj
														+ "*</span>";
											}
											return obj
										}
									},
									{
										"title" : "Line of business",
										"data" : "lob",
										"class" : "dt-left"
									},
									{
										"title" : "Title",
										"data" : "title",
										"class" : "dt-left",
										"render" : function(
												data, type,
												full) {
											return '<a href='
													+ contextPath
													+ '/viewInquiry/'
													+ full['id']
													+ '>'
													+ data
													+ '</a>'
										}
									},
									{
										"title" : "Message",
										"data" : "body",
										"class" : "dt-left",
										"render" : function(str) {
											if (str.length > 40) {
												str = str
														.substring(
																0,
																40);
												str = str
														+ " ...";
											}
											return str
										}
									},
									{
										"title" : "Submitter",
										"data" : "createUser",
										"class" : "dt-left"
									},
									{
										"title" : "Create Timestamp",
										"data" : "createDate",
										"class" : "dt-left",
										"type" : "date"

									}, {
										"title" : "Status",
										"data" : "status",
										"class" : "dt-left",
										"visible" : false

									} , {
										"title" : "AssignedSME",
										"data" : "assignedSME",
										"class" : "dt-left",
										"visible" : false

									}]
						});

		$(gridId+' tbody').on('click', 'tr', function() {
			if ($(this).hasClass('selected')) {
				$(this).removeClass('selected');
			} else {
				table.$('tr.selected').removeClass('selected');
				$(this).addClass('selected');
			}
		});

		var dataLoad = function() {
			
			var newInstance = $(gridId).DataTable();
			var replyData = newInstance.rows('.selected')
					.data()[0];
			if (typeof replyData == 'undefined') {
				showNotifyMsg('Please select a row to view');
				$('#viewModal').modal('hide');
				return;
			}
			
			console.log(replyData)
			$("#myModalLabel").text(
					replyData.title + " ["
							+ replyData.statusString + "]");
			$("#author_pop").val(replyData.createUser);
			$("#date_pop").val(replyData.createDate);
			$("#inquiry_pop").val(replyData.body);
			$("#id_change").val(replyData.id);
			$("#inquiryId").val(replyData.id);
			$("#assignedSme").val(replyData.assignedSME);
			
			if (replyData.path != null) {
				$("#attach").attr(
						"href",
						contextPath + "/downloadAttach?inqId="
								+ replyData.id)
				$("#attach_grp").show();
			} else {
				$("#attach_grp").hide();
			}
			loadReplyHistory(replyData.id)
			$('#viewModal').modal('show');
			if (replyData.status == "2") {
				$("#save_inq_pop").hide();
				$(".reply-div").hide();
//				$("button[type='reset']").hide();
//				$("#replyattach").hide();
				return true;
			} else {
			
				if (isInquiryAdmin=="false") {
					$(".reply-div").hide();
					$("#save_inq_pop").hide();
				} else {
					$(".reply-div").show();
					$("#save_inq_pop").show();
				}

				if(replyData.createUser == loggedUser){
					$(".reply-div").show();
					$("#save_inq_pop").show();
				}
			
				return false;
			}

		}
		$("#close_inq_pop").hide();
		$('#view_btn_2').unbind();
		$('#view_btn_2').click(function() {
			dataLoad();
		});
		
		$('#view_btn').click(function() {
			dataLoad();
		});
		$('#reply_btn_2').unbind();
		$('#reply_btn_2').click(function() {
			if (dataLoad()) {
				$("#close_inq_pop").hide();
			} else {
				$("#close_inq_pop").show();
			}

		});
		$('#reply_btn').click(function() {
			if (dataLoad()) {
				$("#close_inq_pop").hide();
			} else {
				$("#close_inq_pop").show();
			}

		});
		
		$('#viewModal').on('hidden.bs.modal', function() {
			if(gridId == "#src_inquiry_grid"){
				searchByFilter();
			}else{
				loadViewInquiries(false, gridId);
			}
			
			$("#reply_pop").val("");
			$('#reply-table tbody tr').each(function() {
				$(this).remove();
			});
		})

		if(gridId == "#src_inquiry_grid"){
			$("#view_btn_2").show();
			$("#reply_btn_2").show();
			
		}else{
			$("#view_btn").show();
			$("#reply_btn").show();
			
			
		}
		

	}
}


var fetchedAll = false;
$(function() {

	disableEnterKey();
	loadViewInquiries(false, '#inquiry_grid');
	initAddInquiryReply();
	initCloseInquiry();
	
	$("#view_btn_2").hide();
	$("#reply_btn_2").hide();

	textAreaAction("#reply_pop");


	$("#action-setclosed").on("click", function() {
		bootbox.confirm("Close selected inquiry?", function(result) {
			if (result) {
				closeInquiry();
			}
		});
	});

	$("#action-delete").on("click", function() {
		bootbox.confirm("Delete selected inquiry?", function(result) {
			if (result) {
				deleteInquiry();
			}
		});
	});
	
	$("#action-reassign").on("click", function() {
		$("#changeLOB").modal("show");
		loadSMEs('#newLob');
	});
	
	
	$("#src_inq_btn").on("click", function(e){
		searchByFilter();	
		e.preventDefault();
	});

	disableEnterKey();
	$('#startDate').datepicker({
		autoclose : true,
		todayHighlight : true
	});
	$('#endDate').datepicker({
		autoclose : true,
		todayHighlight : true
	});
	loadLobs('#lineOfBus');
	
	$("#selectedFile").change(function() {
		$("#fileName").val($("#selectedFile")[0].files[0]['name']);
	});
});
