<?xml version="1.0" encoding="UTF-8" ?>  
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${basepackage}.${daopackagename}.${pojo.classname}DaoMapper">
	
	<resultMap id="map${pojo.classname}${pojopackagename?cap_first}"
		type="${basepackage}.${pojopackagename}.${pojo.classname}${pojopackagename?cap_first}"> 
		<id column="ID" property="id" />
	<#list pojo.fields as field>
		<#if field.fieldname!='id'>
		<result column="${field.columnname}" property="${field.fieldname}"/> <!-- ${field.comment!''} -->
		</#if>
	</#list>
	</resultMap>

	
</mapper>
