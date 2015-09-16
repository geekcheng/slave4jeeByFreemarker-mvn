package com.billy.jee.slavebyfreemarker.pojo;

import java.util.List;

//-- select table_name as name, comments as comment from user_tab_comments
//-- select * from user_col_comments where table_name='USER_INFO'

public class TableClass {
	private String classname;
	private String tablename;
	private String comment;
	private List<Field> fields;

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	

}
