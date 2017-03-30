<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="includes/head.jsp" />
<script src="<c:url value ="/resources/js/library.js"/>"
	type="text/javascript"></script>
<div class="container-fluid main-container">
	<div class="row content">
		<div class="col-sm-1"></div>

		<div class="col-sm-10 text-left">
			<c:choose>
				<c:when test="${not empty (message)}">
					<div class="alert alert-dismissible alert-custom">
						<button type="button" class="close" data-dismiss="alert">&times;</button>
						<c:out value="${message}" />
					</div>
				</c:when>
			</c:choose>
			<div class="well bs-component">
				<form class="form-horizontal">
					<fieldset>
						<legend>Search Tool / Reference</legend>


						<div class="form-group">
							<label for="title_in" class="col-lg-2 control-label">Title</label>
							<div class="col-lg-8">
								<input type="text" class="form-control" id="title_in"
									name="title_in" placeholder="Title" />
							</div>
						</div>

						<div class="form-group">
							<label for="inputUploader" class="col-lg-2 control-label">Uploader</label>
							<div class="col-lg-8">
								<input type="text" class="form-control" id="inputUploader"
									placeholder="Uploader">
							</div>
						</div>
						<div class="form-group">
							<label for="tags" class="col-lg-2 control-label">Tags</label>
							<div class="col-lg-8">
								<input type="text" class="form-control" id="tags" name="tags"
									data-role="tagsinput" />
							</div>
						</div>
						<div class="form-group">
							<div class="col-lg-8 col-lg-offset-2">
								<a href="#advance-search" class="btn btn-info btn-xs"
									data-toggle="collapse">Advance Search</a>

							</div>
						</div>
						<div class="collapse" id="advance-search">

							<div class="form-group">
								<label for="datepicker" class="col-lg-2 control-label">Date
									Cascaded</label>
								<div class="col-lg-4 date input-group" data-provide="datepicker">
									<input type="text" class="form-control" id="datepicker"
										name="dateCascaded" placeholder="Date">
									<div class="input-group-addon">
										<span class="glyphicon glyphicon-calendar"></span>
									</div>
								</div>
							</div>

							<div class="form-group">
								<label for="addCat_in" class="col-lg-2 control-label">Category</label>
								<div class="col-lg-8">
									<select name="addCat_in" id="addCat_in"
										class="input-group bootstrap-tagsinput">
										<option></option>
									</select>
								</div>
							</div>

							<div class="form-group subcat-div">
								<label for="subCat" class="col-lg-2 control-label">Sub
									Category</label>
								<div class="col-lg-8">
									<select name="subCat_in" id="subCat_in"
										class="input-group bootstrap-tagsinput">
										<option></option>
									</select>
								</div>
							</div>

						</div>

						<div class="form-group">
							<div class="col-lg-8 col-lg-offset-2">
								<button type="reset" class="btn btn-default">Reset</button>
								<button type="submit" class="btn btn-primary"
									id="search_tool_btn">Search</button>
							</div>
						</div>
					</fieldset>
				</form>
			</div>
			<div class="well well-sm" id="search-well">
				<h4>Search Result</h4>
				<hr class="well-hr" />
				<div id="inqTableSpace">
					<table class="display" id="resultGrid"></table>
				</div>

				<input type="button" id="view_btn" value="View"
					class="btn btn-primary" data-toggle="modal" />
				<c:if test="${isUploader==true}">
					<input type="button" id="delete_btn" value="Delete"
						class="btn btn-info" />
				</c:if>
			</div>
		</div>
	</div>

	<!-- Modal -->
	<div class="modal fade" id="viewDocModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<!-- Modal Header -->
				<div class="modal-header btn-info">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span> <span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Modal title</h4>
				</div>

				<!-- Modal Body -->
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="inquiry-view-form"
						action="${pageContext.request.contextPath}/update" method="post"
						enctype="multipart/form-data">

						<c:if test="${isUploader==true}">
							<div class="form-group">
								<div class="update-doc-toggle">
									Update &nbsp;<input type="checkbox" id="updateDocument"
										name="my-checkbox" />
								</div>
							</div>
						</c:if>
						<div class="form-group">
							<label class="col-lg-3 control-label">Category</label>
							<div class="col-lg-9">
								<input type="text" id="viewDocCat" class="form-control" readonly />
								<input type="hidden" class="form-control" name="pageId"
									value="tools" readonly="readonly" />
							</div>
						</div>

						<div class="form-group">
							<label class="col-lg-3 control-label">Sub Category</label>
							<div class="col-lg-9">
								<input type="text" id="viewDocSubCat" class="form-control"
									readonly />
							</div>
						</div>

						<div class="form-group">
							<label for="subCategory" class="col-lg-3 control-label">Filename</label>
							<div class="col-lg-9">
								<input type="text" id="viewDocFilename" class="form-control"
									readonly />
							</div>
						</div>


						<div class="form-group">
							<label class="col-lg-3 control-label">Upload Date</label>
							<div class="col-lg-9">
								<input type="text" class="form-control" id="viewDocUploadDate"
									readonly />
							</div>
						</div>
						<div class="form-group">
							<label class="col-lg-3 control-label">Uploaded By</label>
							<div class="col-lg-9">
								<input type="text" class="form-control" id="viewDocUploadBy"
									readonly />
							</div>
						</div>
						<div class="form-group">
							<label class="col-lg-3 control-label">Last Updated Date</label>
							<div class="col-lg-9">
								<input type="text" class="form-control" id="viewDocUpdateDate"
									readonly />
							</div>
						</div>
						<div class="form-group">
							<label class="col-lg-3 control-label">Last Updated By</label>
							<div class="col-lg-9">
								<input type="text" class="form-control" id="viewDocUpdatedBy"
									readonly />
							</div>
						</div>
						<c:if test="${isUploader==true}">
							<div class="form-group" id="modal-selected-file">
								<label for="selectedFile" class="col-lg-3 control-label">Upload</label>
								<div class="col-lg-9">
									<span class="btn btn-file btn-info">Selected:<br /> <input
										type="file" name="selectedFile" class="btn btn-default"
										id="selectedFile" required /></span>

								</div>
								<div class="col-lg-9">
									<input type="hidden" class="form-control" id="fileName"
										name="fileName" placeholder="File name" readonly="readonly" />
									<input type="hidden" class="form-control" id="docId"
										name="docId" placeholder="File name" readonly="readonly" />
								</div>
							</div>
						</c:if>
						<div class="form-group">
							<label for="detailedInfo" class="col-lg-3 control-label">Detailed
								Info</label>
							<div class="col-lg-9">
								<textarea cols="40" rows="3" id="viewDocInfo"
									name="detailedInfo" class="form-control" placeholder="Info"></textarea>
							</div>
						</div>

						<div class="form-group">
							<label class="col-lg-3 control-label">Tags</label>
							<div class="col-lg-9">
								<input type="text" class="form-control" id="viewDocTags"
									name="tags" data-role="tagsinput" />
							</div>
						</div>
					</form>
				</div>

				<!-- Modal Footer -->
				<div class="modal-footer">

					<c:if test="${isUploader==true}">
						<div class="btn-group">
							<a href="#" class="btn btn-primary">Actions</a> <a href="#"
								class="btn btn-primary dropdown-toggle" data-toggle="dropdown"><span
								class="caret"></span></a>
							<ul class="dropdown-menu">
								<li><a href="#">Download</a></li>

								<li class="divider"></li>
								<li><a href="#">Archive</a></li>
								<li><a href="#">Delete</a></li>
							</ul>
						</div>
					</c:if>
					<button type="button" class="btn btn-default" data-dismiss="modal">Exit</button>
					<button id="downloadDoc" type="button" class="btn btn-primary">
						Download <span class="glyphicon glyphicon-download"></span>
					</button>

					<c:if test="${isUploader==true}">
						<button type="button" class="btn btn-info" id="modal-updateDoc"
							data-dismiss="modal">Update</button>
					</c:if>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="includes/foot.jsp" />