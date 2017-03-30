
<form class="form-horizontal" action="${pageContext.request.contextPath}/addCategory"
	method="post">
	<fieldset>
		<legend>Add Category</legend>

		<div class="form-group">
			<label for="roles" class="col-lg-2 control-label">Category
				Type</label>
			<div class="col-lg-10">
				<select class="form-control" id="cat_type" name="type">
					<option></option>
					<option value="doc_cat">Documents</option>
					<option value="tools_ref">Tools/References</option>
					<option value="sub_cat">Sub Category</option>
				</select>
			</div>
		</div>

		<div class="form-group" id="parent-cat">
			<label for="roles" class="col-lg-2 control-label">Parent
				Category</label>
			<div class="col-lg-10">
				<select class="form-control" id="selection_cat" name="parentCat">
					<option></option>
				</select>
			</div>
		</div>

		<div class="form-group">
			<label for="Label" class="col-lg-2 control-label">Category
				Label</label>
			<div class="col-lg-10">
				<input type="text" class="form-control" id="Label" name="name"
					placeholder="Category Label" required />
			</div>
		</div>

		<div class="form-group">
			<label for="dirName" class="col-lg-2 control-label">Directory
				Name</label>
			<div class="col-lg-10">
				<input type="text" class="form-control" id="dirName" name="directoryName"
					placeholder="Directory Name" required />
			</div>
		</div>

		<div class="form-group">
			<div class="col-lg-8 col-lg-offset-2">
				<button type="reset" class="btn btn-default">Reset</button>
				<button type="submit" class="btn btn-primary" id="add_cat_btn">Add</button>
			</div>

		</div>
	</fieldset>
</form>

