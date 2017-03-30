/**
 * 
 */

var getLobList = function() {
	$.ajax({
		url : contextPath + "/getLoBList",
		type : "POST",
		accept : 'application/json',
		success : function(data) {
			$('#lineOfBus').lineofbusiness(data);
		},
		error : function(e) {
			// console.log(e);
		}
	});
}

var initCategories = function() {
	$(".subcat-div").hide();
	$.ajax({
		url : contextPath + "/getCategories",
		type : "POST",
		accept : 'application/json',
		success : function(data) {
			$('#searchCat_in').categories(data);
			$('#addCat_in').categories(data);
		},
		error : function(e) {
			// console.log(e);
		}
	});

	$("#addCat_in").change(function() {
		var catId = $("#addCat_in").children(":selected").attr("id");
		if (catId != "") {
			$.ajax({
				url : contextPath + "/getSubCategories",
				type : "POST",
				accept : 'application/json',
				data : {
					'cat' : catId
				},
				success : function(data) {
					if (data.length != 0) {
						$('#subCat_in').categories(data);
						$(".subcat-div").show();
					} else {
						$(".subcat-div").hide();
					}
				},
				error : function(e) {
					// console.log(e);
				}
			});
		} else {
			$(".subcat-div").hide();
		}
	});

}

var initHandleSearch = function() {

	$("#search_btn").click(function(event) {
		getDocuments(true);
		event.preventDefault();

	});
}

var getDocuments = function(isMainDocu) {

	var fields = [ "title_in", "inputFirstName", "inputLastName", "tags",
			"datepicker", "lineOfBus" ];

	var hasDirtyBit = false;

	$.each(fields, function(ind, data) {
		var field = $.trim($("#" + data).val());
		if (field.length > 0) {
			hasDirtyBit = true;
		}
	});

	var mainCat = $("#addCat_in").children(":selected").attr("id");
	mainCat = undefined == mainCat ? "" : mainCat;
	var subCat = $("#subCat_in").children(":selected").attr("id");
	subCat = undefined == subCat ? "" : subCat;

	if (mainCat.length > 0 || subCat.length > 0) {
		hasDirtyBit = true
	}

	var isArchived = $("#isArchived");
	var archivedBit = true;
	if (isArchived != null) {
		if (isArchived.prop("checked")) {
			hasDirtyBit = true;
			archivedBit = false;
		}
	}

	if (hasDirtyBit) {
		$.ajax({
			url : contextPath + "/searchDocument",
			type : "POST",
			data : {
				'isMainDoc' : isMainDocu,
				'title' : $("#title_in").val(),
				'firstName' : $("#inputFirstName").val(),
				'lastName' : $("#inputLastName").val(),
				'tags' : $("#tags").val(),
				'dateCascaded' : $("#datepicker").val(),
				'mainCat' : mainCat,
				'subCat' : subCat,
				'lineOfBus' : $("#lineOfBus").val(),
				'isArchived' : archivedBit
			},
			accept : 'application/json',
			success : function(data) {
				paintTable(data);
				var element_to_scroll_to = $('#search-well')[0];
				element_to_scroll_to.scrollIntoView();
			},
			error : function(e) {
				// console.log(e);
			}

		});
	} else {
		showNotifyMsg('Please fill out any search field')

	}

}

var initHandleSearchTool = function() {
	$("#search_tool_btn").click(function(event) {
		getDocuments(false);
		event.preventDefault();

	});
}

var paintTable = function(oData) {
	if ($.fn.dataTable.isDataTable('#resultGrid')) {
		var table = $('#resultGrid').DataTable();
		table.clear();
		table.rows.add(oData);
		table.columns.adjust().draw();
	} else {
		var table = $('#resultGrid')
				.dataTable(
						{
							"pageLength" : 10,
							"order" : [ [ 7, "desc" ] ],
							"data" : oData,
							"columns" : [
									{
										"title" : "Title",
										"data" : "title",
										"class" : "dt-left",
										"width" : "15%",
										"render" : function(data, type, full) {
											return '<a href=' + contextPath
													+ '/viewDocument/'
													+ full['docId'] + '>'
													+ data + '</a>'
										}
									},
									{
										"title" : "Description",
										"data" : "detailedInfo",
										"class" : "dt-left",
										"width" : "20%",
										"render" : function(str) {
											if (str.length > 40) {
												str = str.substring(0, 40);
												str = str + " ...";
											}
											return str
										}
									},
									{
										"title" : "Line of Business",
										"data" : "lob",
										"class" : "dt-left",
										"width" : "10%"
									},
									{
										"title" : "Category",
										"data" : "catName",
										"class" : "dt-left",
										"width" : "10%"
									},
									{
										"title" : "Sub Category",
										"data" : "subCatName",
										"class" : "dt-left",
										"width" : "10%"
									},
									{
										"title" : "Created By",
										"data" : "createUser",
										"class" : "dt-left",
										"width" : "10%"
									},
									{
										"title" : "Last Update By",
										"data" : "updateUser",
										"class" : "dt-left",
										"width" : "10%"
									},
									{
										"title" : "Last Update Date",
										"data" : "updateDate",
										"class" : "dt-left",
										"width" : "10%"
									},
									{
										"title" : "<span class='glyphicon glyphicon-download-alt'>",
										"data" : "docId",
										"class" : "dt-left",
										"width" : "5%",
										"render" : function(id) {
											return '<a href='
													+ contextPath
													+ '/doDownload?docId='
													+ id
													+ '><span class="glyphicon glyphicon-download-alt"></span></a>'
										}
									} ]
						});

		$('#resultGrid tbody').on('click', 'tr', function() {
			if ($(this).hasClass('selected')) {
				$(this).removeClass('selected');
			} else {
				table.$('tr.selected').removeClass('selected');
				$(this).addClass('selected');
			}
		});

		$('#view_btn').click(function() {
			dataLoad();
		});

		var dataLoad = function() {
			var newInstance = $('#resultGrid').DataTable();
			var docData = newInstance.rows('.selected').data()[0];
			if (undefined == docData) {
				showNotifyMsg('Please select a row to view');
				$('#viewDocModal').modal('hide');
				return;
			}
			if (!docData.isActive) {
				$("#action-archive").html("Undo Archive")
			} else {
				$("#action-archive").html("Archive")
			}
			$("#myModalLabel").text(docData.title);
			$("#viewDocCat").val(docData.catName);
			$("#viewDocSubCat").val(docData.subCatName);
			$("#viewDocFilename").val(docData.fileName);
			$("#viewDocInfo").val(docData.detailedInfo);
			$("#line-of-business").val(docData.lob);

			var tags = docData.tags.split(",");

			$.each(tags, function(data) {
				$('#viewDocTags').tagsinput('add', tags[data]);
			})

			$("#viewDocUploadDate").val(docData.createDate);
			$("#viewDocUploadBy").val(docData.createUser);
			$("#viewDocUpdateDate").val(docData.updateDate);
			$("#viewDocUpdatedBy").val(docData.updateUser);

			$('#viewDocModal').modal('show');

		}

		$('#delete_btn').click(function() {
			performDelete();
		});
	}
	initModalDownloadBtn();
	$("#search-well").show();
};

var performArchive = function() {
	var newInstance = $('#resultGrid').DataTable();
	var docData = newInstance.rows('.selected').data()[0];

	if ($("#action-archive").html() == "Undo Archive") {
		bootbox.confirm("Undo archiving of selected document?",
				function(result) {
					if (result) {
						window.location = contextPath + "/undoArchive?docId="
								+ docData.docId;
					}
				});
	} else {
		bootbox.confirm("Archive selected document?", function(result) {
			if (result) {
				window.location = contextPath + "/archive?docId="
						+ docData.docId;
			}
		});
	}

}

var performDelete = function() {
	var newInstance = $('#resultGrid').DataTable();
	var docData = newInstance.rows('.selected').data()[0];
	if (undefined == docData) {
		showNotifyMsg('Please select a row to delete');
		$('#viewDocModal').modal('hide');
		return;
	}

	bootbox.confirm("Delete selected document?", function(result) {
		if (result) {
			window.location = contextPath + "/delete?docId=" + docData.docId;
			$('#viewDocModal').modal('hide');
		}
	});
}

var downloadDoc = function() {
	var newInstance = $('#resultGrid').DataTable();
	var docid = newInstance.rows('.selected').data()[0].docId;

	window.location = contextPath + "/doDownload?docId=" + docid;
}
var initModalDownloadBtn = function() {
	$("#downloadDoc").click(function() {
		downloadDoc();
	});
}

$(function() {
	disableEnterKey();
	$("#selectedFile").change(function() {
		$("#fileName").val($("#selectedFile")[0].files[0]['name']);
	});

	initCategories();
	getLobList();
	initHandleSearch();
	initHandleSearchTool();
	$('#datepicker').datepicker({
		autoclose : true,
		todayHighlight : true
	});
	$('#datepicker_add').datepicker({
		autoclose : true,
		todayHighlight : true
	});
	$("#search-well").hide();

	$("#modal-selected-file").hide();
	$("#modal-updateDoc").hide();

	$("[name='my-checkbox']").bootstrapSwitch({
		'size' : 'small',
		'onText' : 'Yes',
		'offText' : 'No'
	});

	$('input[name="my-checkbox"]').on('switchChange.bootstrapSwitch',
			function(event, state) {
				if (true == state) {
					$("#modal-selected-file").show();
					$("#modal-updateDoc").show();
				} else {
					$("#modal-selected-file").hide();
					$("#modal-updateDoc").hide();
				}

			});

	$("#modal-updateDoc").on('click', function(event) {
		event.preventDefault();
		var newInstance = $('#resultGrid').DataTable();
		var docid = newInstance.rows('.selected').data()[0].docId;

		$("#docId").val(docid);
		$("#inquiry-view-form").submit();
	});

	$("#desc_in").on('change keyup paste', function() {
		var desc_in = $("#desc_in");
		var remarksLength = desc_in.val().length;
		var maxLength = 500;
		if (remarksLength > maxLength) {
			desc_in.val(desc_in.val().substring(0, maxLength));
		}
		$("#availableChar").html(maxLength - desc_in.val().length);
	});

	$("#restBtn").on("click", function() {
		$('#tags').tagsinput('removeAll');
	});

	$("#action-download").on("click", function() {
		downloadDoc();
	});

	$("#action-delete").on("click", function() {
		performDelete();
	});

	$("#action-archive").on("click", function() {
		performArchive();
	});
});
