package ${basepackage}.${pojopackagename};

import java.io.Serializable;

/**
 * ${pojo.comment!''}
 *
 */
public class ${pojo.classname}${pojopackagename?cap_first} implements Serializable{
	
<#list pojo.fields as field>
	private ${field.fieldtype} ${field.fieldname}; /* ${field.comment!''} */
</#list>
	
	
<#list pojo.fields as field>
	public ${field.fieldtype} get${field.fieldname?cap_first}(){
		return ${field.fieldname};
	}
	public void set${field.fieldname?cap_first}(${field.fieldtype} ${field.fieldname}){
		this.${field.fieldname} = ${field.fieldname};
	}
</#list>

}
