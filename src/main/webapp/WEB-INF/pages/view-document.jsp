<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="includes/head.jsp" />
<script>
	$(function() {
		disableEnterKey();
		$("#downloadDoc").click(function() {
			doDownload();
		});

		$("#home").click(function() {
			window.location = "${pageContext.request.contextPath}";
		});

		$("#action-download").on("click", function() {
			doDownload();
		});

		$("#action-delete").on(
				"click",
				function() {
					bootbox.confirm("Delete the selected document?", function(
							result) {
						if (result) {
							window.location = contextPath + "/delete?docId="
									+ $("#docId").val();
						}
					});

				});
		var isActive = "${document.enabled}";

		if (isActive == "false") {
			$("#action-archive").html("Undo Archive")
		} else {
			$("#action-archive").html("Archive")
		}

		$("#action-archive").on(
				"click",
				function() {
					if ($("#action-archive").html() == "Undo Archive") {
						bootbox.confirm("Undo archiving of selected document?",
								function(result) {
									if (result) {
										window.location = contextPath
												+ "/undoArchive?docId="
												+ $("#docId").val();
									}
								});
					} else {
						bootbox.confirm("Archive selected document?",
								function(result) {
									if (result) {
										window.location = contextPath
												+ "/archive?docId="
												+ $("#docId").val();

									}
								});
					}

				});

		var doDownload = function() {
			window.location = "${pageContext.request.contextPath}/doDownload?docId="
					+ $("#docId").val();
		}

	});
</script>
<div class="container-fluid text-center main-container">
	<div class="row content">
		<div class="col-sm-1"></div>
		<div class="col-sm-10 text-left">
			<c:choose>
				<c:when test="${not empty (message)}">
					<div class="alert alert-dismissible alert-warning">
						<button type="button" class="close" data-dismiss="alert">&times;</button>
						<c:out value="${message}" />
					</div>
				</c:when>
			</c:choose>

			<div class="well bs-component">

				<form class="form-horizontal" role="form">
					<fieldset>
						<legend>View Document</legend>
						<c:if test="${isRoleUploader==true}">
							<div class="btn-group" id="actions-group">
								<a href="#" class="btn btn-primary">Actions</a> <a href="#"
									class="btn btn-primary dropdown-toggle" data-toggle="dropdown"><span
									class="caret"></span></a>
								<ul class="dropdown-menu">
									<li><a href="#" id="action-download">Download</a></li>

									<li class="divider"></li>
									<li><a href="#" id="action-archive">Archive</a></li>
									<li><a href="#" id="action-delete">Delete</a></li>
								</ul>
							</div>

						</c:if>
						<div class="form-group">
							<label class="col-lg-2 control-label">Document Title:</label>
							<div class="col-lg-8">

								<input type="text" id="title" class="form-control" readonly
									value="<c:out value='${document.title}'/>" />
							</div>
						</div>

						<div class="form-group">
							<label class="col-lg-2 control-label">Line of Business</label>
							<div class="col-lg-8">

								<input type="text" id="line-of-business" class="form-control"
									readonly
									value="<c:out value='${document.lineOfBusiness.name}'/>" />
							</div>
						</div>


						<div class="form-group">
							<label class="col-lg-2 control-label">Category</label>
							<div class="col-lg-8">
								<input type="text" id="viewDocCat" class="form-control"
									value="<c:out value='${document.category.name}'/>" readonly />
								<input type="hidden" class="form-control" id="docId"
									value="<c:out value='${document.id}'/>" readonly="readonly" />
							</div>
						</div>

						<div class="form-group">
							<label class="col-lg-2 control-label">Sub Category</label>
							<div class="col-lg-8">
								<input type="text" id="viewDocSubCat" class="form-control"
									value="<c:out value='${document.subCategory.name}'/>" readonly />
							</div>
						</div>

						<div class="form-group">
							<label for="subCategory" class="col-lg-2 control-label">Filename</label>
							<div class="col-lg-8">
								<input type="text" id="viewDocFilename" class="form-control"
									value="<c:out value='${document.filename}'/>" readonly />
							</div>
						</div>


						<div class="form-group">
							<label class="col-lg-2 control-label">Upload Date</label>
							<div class="col-lg-8">
								<input type="text" class="form-control" id="viewDocUploadDate"
									value="<c:out value='${document.createDate}'/>" readonly />
							</div>
						</div>
						<div class="form-group">
							<label class="col-lg-2 control-label">Uploaded By</label>
							<div class="col-lg-8">
								<input type="text" class="form-control" id="viewDocUploadBy"
									value="<c:out value='${document.createUser.firstName} ${document.createUser.lastName}'/>"
									readonly />
							</div>
						</div>
						<div class="form-group">
							<label class="col-lg-2 control-label">Last Updated Date</label>
							<div class="col-lg-8">
								<input type="text" class="form-control" id="viewDocUpdateDate"
									value="<c:out value='${document.updateDate}'/>" readonly />
							</div>
						</div>
						<div class="form-group">
							<label class="col-lg-2 control-label">Last Updated By</label>
							<div class="col-lg-8">
								<input type="text" class="form-control" id="viewDocUpdatedBy"
									value="<c:out value='${document.updateUser.firstName} ${document.updateUser.lastName}'/>"
									readonly />
							</div>
						</div>

						<div class="form-group">
							<label for="detailedInfo" class="col-lg-2 control-label">Detailed
								Info</label>
							<div class="col-lg-8">
								<textarea cols="40" rows="3" id="viewDocInfo"
									name="detailedInfo" class="form-control" placeholder="Info"
									readonly><c:out value='${document.detailedInfo}' /></textarea>
							</div>
						</div>

						<div class="form-group">
							<label class="col-lg-2 control-label">Tags</label>
							<div class="col-lg-8">
								<input type="text" class="form-control" id="viewDocTags"
									name="tags" data-role="tagsinput"
									value="<c:out value='${document.tags}'/>" />
							</div>
						</div>

						<div class="form-group ">
							<div class="col-lg-offset-2 col-lg-8">
								<button id="home" type="button" class="btn btn-primary">
									Home</button>
								<button id="downloadDoc" type="button" class="btn btn-info">
									Download</button>
							</div>
						</div>

					</fieldset>
				</form>

			</div>
		</div>
	</div>
</div>

<jsp:include page="includes/foot.jsp" />