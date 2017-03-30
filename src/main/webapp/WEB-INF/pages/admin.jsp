
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="includes/head.jsp" />
<script src="<c:url value ="/resources/js/unsent.js"/>"
	type="text/javascript"></script>
<script src="<c:url value ="/resources/js/admin.js"/>"
	type="text/javascript"></script>
<div class="container-fluid text-center main-container">
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
				<h3 class="well-title">Settings</h3>
				<hr class="well-hr" />
				<ul class="nav nav-tabs">
					<li class="active"><a href="#adduser" data-toggle="tab"
						aria-expanded="true">Add User</a></li>
					<li class=""><a href="#updateuser" data-toggle="tab"
						aria-expanded="false">Update User</a></li>
					<li class=""><a href="#categories" data-toggle="tab"
						aria-expanded="false">Add Category</a></li>
					<li class=""><a href="#deletecategory" data-toggle="tab"
						aria-expanded="false">Delete Category</a></li>
					<li class=""><a href="#properties" data-toggle="tab"
						aria-expanded="false">App Properties</a></li>
					<li class=""><a href="#unsent" data-toggle="tab"
						aria-expanded="false">Unsent Emails</a></li>
					
				</ul>
				<div id="myTabContent" class="tab-content">
					<div class="tab-pane fade active in" id="adduser">
						<jsp:include page="includes/add-user.jsp" />
					</div>
					<div class="tab-pane fade" id="updateuser">
						<jsp:include page="includes/update-user.jsp" />
					</div>
					<div class="tab-pane fade" id="categories">
						<jsp:include page="includes/manage-categories.jsp" />
					</div>
					<div class="tab-pane fade" id="deletecategory">
						<jsp:include page="includes/delete-category.jsp" />
					</div>

					<div class="tab-pane fade" id="properties">
						<jsp:include page="includes/application-properties.jsp" />
					</div>
					
					<div class="tab-pane fade" id="unsent">
						<jsp:include page="includes/unsent-emails.jsp" />
					</div>
					<%--<div class="tab-pane fade" id="storage">
						<h3>Storage</h3>
						<p>Trust fund seitan letterpress, keytar raw denim keffiyeh
							etsy art party before they sold out master cleanse gluten-free
							squid scenester freegan cosby sweater. Fanny pack portland seitan
							DIY, art party locavore wolf cliche high life echo park Austin.
							Cred vinyl keffiyeh DIY salvia PBR, banh mi before they sold out
							farm-to-table VHS viral locavore cosby sweater.</p>
					</div> --%>
				</div>
			</div>
		</div>
	</div>
</div>
<jsp:include page="includes/foot.jsp" />