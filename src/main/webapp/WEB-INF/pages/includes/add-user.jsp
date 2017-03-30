<form class="form-horizontal" action="${pageContext.request.contextPath}/addUser" method="post"
	enctype="multipart/form-data">
	<fieldset>
		<legend>Add User</legend>

		<div class="form-group">
			<label for="username" class="col-lg-2 control-label">Username</label>
			<div class="col-lg-8">
				<input type="text" class="form-control" id="username"
					name="username" placeholder="Username" required />
			</div>
		</div>
		<div class="form-group">
			<label for="empID" class="col-lg-2 control-label">Employee ID</label>
			<div class="col-lg-8">
				<input type="text" class="form-control" id="empID" name="empID"
					placeholder="Employee ID" required />
			</div>
		</div>
		<div class="form-group">
			<label for="lastname" class="col-lg-2 control-label">Last
				Name</label>
			<div class="col-lg-8">
				<input type="text" class="form-control" id="lastname"
					name="lastname" placeholder="Last Name" required />
			</div>
		</div>
		<div class="form-group">
			<label for="firstname" class="col-lg-2 control-label">First
				Name</label>
			<div class="col-lg-8">
				<input type="text" class="form-control" id="firstname"
					name="firstname" placeholder="First Name" required />
			</div>
		</div>

		<div class="form-group">
			<label for="email" class="col-lg-2 control-label">Email</label>
			<div class="col-lg-8">
				<input type="email" class="form-control" id="email" name="email"
					placeholder="Email" required />
			</div>
		</div>

		<div class="form-group subcat-div">
			<label for="roles" class="col-lg-2 control-label">Roles</label>
			<div class="col-lg-8">
				<select multiple="multiple" class="form-control" id="roles-selection" name="roles" required>
					<option>ROLE_USER</option>
					<option>ROLE_UPLOADER</option>
					<option>ROLE_INQUIRY_ADMIN</option>
					<option>ROLE_ADMIN</option>
				</select>
			</div>
		</div>

		<div class="form-group">
			<div class="col-lg-8 col-lg-offset-2">
				<button type="reset" class="btn btn-default">Reset</button>
				<button type="submit" class="btn btn-primary" id="add_user_btn">Add</button>
			</div>
		</div>
	</fieldset>
</form>