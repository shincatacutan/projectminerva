
<form class="form-horizontal" action="${pageContext.request.contextPath}/updateUser"
	method="post" enctype="multipart/form-data">
	<fieldset>
		<legend>Update User</legend>

		<div class="form-group search-user">
			<label for="username" class="col-lg-4 col-md-4 col-sm-4 control-label">Search</label>
			<div class="input-group">
				<input type="text" class="form-control typeahead"
					id="user-autocomplete" placeholder="Username"><span
					class="input-group-addon"><span
					class="glyphicon glyphicon-search"></span></span>
			</div>
		</div>
		<div id="populate-div">
			<div class="form-group">
				<label for="username" class="col-lg-2 control-label">Username</label>
				<div class="col-lg-10">
					<input type="text" class="form-control" id="username_up"
						name="username" placeholder="Username" readonly />
				</div>
			</div>
			<div class="form-group">
				<label for="empID" class="col-lg-2 control-label">Employee
					ID</label>
				<div class="col-lg-10">
					<input type="text" class="form-control" id="empID_up" name="empID"
						placeholder="Employee ID" readonly />
				</div>
			</div>
			<div class="form-group">
				<label for="lastname" class="col-lg-2 control-label">Last
					Name</label>
				<div class="col-lg-10">
					<input type="text" class="form-control" id="lastname_up"
						name="lastname" placeholder="Last Name" />
				</div>
			</div>
			<div class="form-group">
				<label for="firstname" class="col-lg-2 control-label">First
					Name</label>
				<div class="col-lg-10">
					<input type="text" class="form-control" id="firstname_up"
						name="firstname" placeholder="First Name" />
				</div>
			</div>

			<div class="form-group">
				<label for="email" class="col-lg-2 control-label">Email</label>
				<div class="col-lg-10">
					<input type="email" class="form-control" id="email_up" name="email"
						placeholder="Email" />
				</div>
			</div>

			<div class="form-group subcat-div">
				<label for="roles" class="col-lg-2 control-label">Roles</label>
				<div class="col-lg-10">
					<select multiple="multiple" class="form-control"
						id="roles-selection_up" name="roles">
						<option>ROLE_USER</option>
						<option>ROLE_UPLOADER</option>
						<option>ROLE_INQUIRY_ADMIN</option>
						<option>ROLE_ADMIN</option>
					</select>
				</div>
			</div>

			<div class="form-group">
				<div class="col-lg-8 col-lg-offset-2">
					<input type="button" class="btn btn-primary" id="deactvt_user_btn"
						value="Deactivate" />
					<!-- <input type="button" class="btn btn-primary" id="delete_user_btn" value="Delete" hidden="true"/> -->
					<button type="submit" class="btn btn-info" id="update_user_btn">Update</button>
					<input type="button" class="btn btn-success" id="unlock_user_btn"
						value="Unlock" />
				</div>

			</div>

			<div id="confirm" class="modal hide fade">
				<div class="modal-body">Proceed deactivating user?</div>
				<div class="modal-footer">
					<button type="button" data-dismiss="modal" class="btn btn-primary"
						id="deactivt">Delete</button>
					<button type="button" data-dismiss="modal" class="btn">Cancel</button>
				</div>
			</div>
		</div>
	</fieldset>
</form>