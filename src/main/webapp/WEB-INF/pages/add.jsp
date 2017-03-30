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
				<form class="form-horizontal" action="${pageContext.request.contextPath}/upload"
					method="post" enctype="multipart/form-data">
					<fieldset>
						<legend>Add Document</legend>

						<div class="form-group">
							<label for="title" class="col-lg-2 col-md-2 col-sm-2 control-label">Title</label>
							<div class="col-lg-8 col-md-8  col-sm-8 col-md-8  col-sm-8">
								<input type="text" class="form-control" id="add_title"
									name="title" placeholder="Title" required />
							</div>
						</div>
						
						<div class="form-group">
							<label for="addCat_in" class="col-lg-2 col-md-2 col-sm-2 control-label">Line of Business</label>
							<div class="col-lg-4 col-md-4 col-sm-4">
								<select name="lineOfBus" id="lineOfBus" required="required"
									class="input-group bootstrap-tagsinput form-control">
									<option></option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label for="categoryName" class="col-lg-2 col-md-2 col-sm-2 control-label">Category</label>
							<div class="col-lg-4 col-md-4 col-sm-4">
								<select name="categoryName" id="addCat_in"
									class="bootstrap-tagsinput form-control" required>
									<option></option>
								</select>
							</div>
						</div>

						<div class="form-group subcat-div">
							<label for="subCategory" class="col-lg-2 col-md-2 col-sm-2 control-label">Sub
								Category</label>
							<div class="col-lg-4 col-md-4 col-sm-4">
								<select name="subCategory" id="subCat_in"
									class="input-group bootstrap-tagsinput form-control">
									<option></option>
								</select>
							</div>
						</div>

						<div class="form-group">
							<label for="selectedFile" class="col-lg-2 col-md-2 col-sm-2 control-label">Upload</label>
							<div class="col-lg-8 col-md-8  col-sm-8 col-md-8  col-sm-8">
								<span class="btn btn-file btn-info">Selected:<br /> <input
									type="file" name="selectedFile" class="btn btn-default"
									id="selectedFile" required /></span>

							</div>
							<div class="col-lg-8 col-md-8  col-sm-8 col-md-8  col-sm-8">
								<input type="hidden" class="form-control" id="fileName"
									name="fileName" placeholder="File name" readonly="readonly" />
							</div>
						</div>

						<div class="form-group">
							<label for="detailedInfo" class="col-lg-2 col-md-2 col-sm-2 control-label">Detailed
								Info</label>
							<div class="col-lg-8 col-md-8  col-sm-8 col-md-8  col-sm-8">
								<textarea cols="40" rows="3" id="desc_in" name="detailedInfo"
									class="form-control" placeholder="Info" required></textarea>
									<span id="availableCharSpan"><span id="availableChar">500</span> characters remaining</span>
							</div>
						</div>

						<div class="form-group">
							<label for="tags" class="col-lg-2 col-md-2 col-sm-2 control-label">Tags</label>
							<div class="col-lg-8 col-md-8  col-sm-8">
								<input type="text" class="form-control" id="fileName"
									name="tags" data-role="tagsinput" />
							</div>
						</div>
						<!--
						<div class="form-group">
							<label for="datepicker_add" class="col-lg-2 col-md-2 col-sm-2 control-label">Date
								Cascaded</label>
							<div class="col-lg-4 col-md-4 col-sm-4 date input-group"
								data-provide="datepicker_add">
								<input type="text" class="form-control" id="datepicker_add"
									placeholder="Date" required>
								<div class="input-group-addon">
									<span class="glyphicon glyphicon-calendar"></span>
								</div>
							</div>
						</div> 

						<div class="form-group">
							<label for="author_in" class="col-lg-2 col-md-2 col-sm-2 control-label">Author</label>
							<div class="col-lg-10">
								<input type="text" class="form-control" id="author_in"
									name="author_in" placeholder="Author" required />
							</div>
						</div>
-->



						<div class="form-group">
							<div class="col-lg-10 col-lg-offset-2 col-md-10 col-md-offset-2 col-sm-10 col-sm-offset-2">
								<button type="reset" class="btn btn-default">Reset</button>
								<button type="submit" class="btn btn-primary" id="add_btn">Add</button>
							</div>
						</div>
					</fieldset>
				</form>
			</div>

		</div>
	</div>
</div>

<jsp:include page="includes/foot.jsp" />
