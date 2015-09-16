package com.billy.jee.slavebyfreemarker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.billy.jee.slavebyfreemarker.pojo.Field;
import com.billy.jee.slavebyfreemarker.pojo.TableClass;

public class DBUtil {


	private static Map<String, String> oracleType2JavaType = new HashMap<String, String>();
	//private static Map<String, String> mysqlType2JavaType = new HashMap<String, String>(); 

	static {
		oracleType2JavaType.put("VARCHAR2", "String");
		oracleType2JavaType.put("VARCHAR", "String");
		oracleType2JavaType.put("NUMBER", "Integer");
		oracleType2JavaType.put("DATE", "String");
		oracleType2JavaType.put("BLOB", "String");
		oracleType2JavaType.put("CLOB", "String");
		oracleType2JavaType.put("TIMESTAMP", "String");
		//mysqlType2JavaType.put("", "");
	}
	/***************************************************************/

	private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String DB_URL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
	private static final String DB_USERNAME = "scott";
	private static final String DB_PASSWORD = "scott";

	private static Connection connection = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	/**
	 *
	 *
	 * @param oracleType
	 * @param dataScale dataScale中若是有数据就一定会是数字，若此时dataScale大于0就说明要返回Double类型，
	 * 					其它的就是简单的类型
	 * @return
	 */
	private static String getJavaTypeByOracleType(String oracleType, String dataScale) {
		if(dataScale == null || "".equals(dataScale)) {
			return oracleType2JavaType.get(oracleType.toUpperCase());
		}
		if(oracleType.equalsIgnoreCase("NUMBER") &&  Integer.parseInt(dataScale) > 0) {
			return "Double";
		}
		return oracleType2JavaType.get(oracleType.toUpperCase());
	}
	
	/*private static String getMysqlType2JavaType(String mysqlType) {
		
		return mysqlType2JavaType.get(mysqlType);
	}*/


	/**
	 * 获取所有表，放到TableClass类中
	 *
	 * @return
	 */
	public static List<TableClass> getAllTableClasses() {
		List<TableClass> list = new ArrayList<TableClass>();
		TableClass tc = null;

		try {
			String sql = " select table_name as t_name, comments as t_comment from user_tab_comments "
					+ " where table_name not like \'BIN$%\' ";
			//	System.out.println(sql);
			Class.forName(DB_DRIVER);
			connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				tc = new TableClass();
				tc.setTablename(resultSet.getString("t_name"));
				tc.setClassname(
						StringExUtil.capitaliseFirst(
								columnOrTableName2JavaCamelStyleString(
										resultSet.getString("t_name"))));
				tc.setComment(resultSet.getString("t_comment"));
				list.add(tc);
			}

			resultSet.close();
			preparedStatement.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return list;
	}

	/**
	 * 填充表信息
	 * 	填充表的字段集合
	 * 	转换成驼峰样式
	 *
	 * @param list
	 */
	public static void paddingAllTableInfo(List<TableClass> list) {
		for(TableClass tc : list) {
			try {
				String sql = " select distinct ucc.column_name as t_name, "
						+ " ucc.comments as t_comment,utc.data_type as t_type, utc.column_id as sortid, "
						+ " utc.data_scale as t_datascale "
						+ " from user_col_comments ucc left join user_tab_columns utc on ucc.column_name=utc.column_name "
						+ " where ucc.table_name=\'"+tc.getTablename()+"\' and utc.table_name=\'" + tc.getTablename() + "\' "
						+ " order by sortid ";
				//	System.out.println(sql);
				Class.forName(DB_DRIVER);
				connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery(sql);
				List<Field> fields = new ArrayList<Field>();

				Field field = null;
				while(resultSet.next()){
					field = new Field();
					field.setComment(resultSet.getString("t_comment"));
					field.setColumnname(resultSet.getString("t_name"));
					field.setColumntype(resultSet.getString("t_type"));
					field.setFieldname(columnOrTableName2JavaCamelStyleString(resultSet.getString("t_name")));
					field.setFieldtype(getJavaTypeByOracleType(resultSet.getString("t_type"), resultSet.getString("t_datascale")));
					fields.add(field);
				}

				tc.setFields(fields);

				resultSet.close();
				preparedStatement.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 表名或字段名转成驼峰型，并去掉下划线_
	 * 	注意：表名会首字母大写，字段名不会
	 *
	 * 	如  表名 USER_INFO  -->  UserInfo
	 *    字段名 create_time --> createTime
	 *
	 * @param columnName
	 * @return
	 */
	private static String columnOrTableName2JavaCamelStyleString(String columnName) {
		StringBuilder camelString = new StringBuilder(30);
		String[] split = columnName.toLowerCase().split("_");
		String s = null;
		for (int i = 0; i < split.length; i++) {
			s = split[i];
			if(i > 0) {
				s = StringExUtil.capitaliseFirst(s);
			}
			camelString.append(s);
		}

		return camelString.toString();
	}




}
