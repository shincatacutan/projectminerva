<form class="form-horizontal"
	action="${pageContext.request.contextPath}/deleteCategory"
	method="post">
	<fieldset>
		<legend>Delete Category</legend>

		<div class="form-group" id="parent-cat-delete">
			<label for="roles" class="col-lg-2 control-label">Parent
				Category</label>
			<div class="col-lg-4">
				<select class="form-control" id="selection_cat_del" name="parentCat" required="required">
					<option value="-2">Select</option>
				</select>
			</div>
		</div>

		<div class="form-group">
			<label for="subCat" class="col-lg-2 control-label">Sub
				Category</label>
			<div class="col-lg-4">
				<select name="subCat" id="subCat_in"
					class="input-group bootstrap-tagsinput form-control">
					<option value="-2">Select</option>
				</select>
			</div>
		</div>

		<div class="form-group">
			<div class="col-lg-8 col-lg-offset-2">
				<button type="reset" class="btn btn-default">Reset</button>
				<button type="submit" class="btn btn-primary" id="add_cat_btn">Delete</button>
			</div>
		</div>
	</fieldset>
</form>