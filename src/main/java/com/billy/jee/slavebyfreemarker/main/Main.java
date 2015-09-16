package com.billy.jee.slavebyfreemarker.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.billy.jee.slavebyfreemarker.pojo.TableClass;
import com.billy.jee.slavebyfreemarker.util.DBUtil;
import com.billy.jee.slavebyfreemarker.util.FileUtil;
import com.billy.jee.slavebyfreemarker.util.StringExUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class Main {

	public static void main(String[] args) throws Exception {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_21);

		cfg.setDirectoryForTemplateLoading(new File(
				Main.class.getClassLoader().getResource("").getPath() +
						"/com/billy/jee/slavebyfreemarker/templatefiles"));

		cfg.setDefaultEncoding("UTF-8");

		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("srcpath", "e:/newFolder");
		root.put("basepackage", "com.billy.jee.slave4jee");
		root.put("pojopackagename", "bean");
		root.put("controllerpackagename", "controller");
		root.put("servicepackagename", "service");
		root.put("daopackagename", "dao");
		root.put("mapperpackagename", "mapper");
		ensureDirectories(root);
		processAll(root, cfg);
		System.out.println("Done!");

	}

	/**
	 * 确保在生成java类文件前首先创建其所在的文件夹（包）。
	 *
	 * @param root
	 */
	private static void ensureDirectories(Map<String, Object> root) {
		String src = root.get("srcpath").toString().replace("\\", "/"); //"e:/com_slave"
		File file = new File(src);
		FileUtil.deleteDirectory(src);
		if(file != null && !file.exists()) {
			file.mkdirs();
		}
		String srcBasePkg = src + "/" + root.get("basepackage").toString().replace(".", "/");
		File fileBasePkg = new File(srcBasePkg);
		if(fileBasePkg != null && !fileBasePkg.exists()) {
			fileBasePkg.mkdirs();
		}
		String beanPkg = srcBasePkg + "/" + root.get("pojopackagename").toString();
		File fileBeanPkg = new File(beanPkg);
		if(fileBeanPkg != null && !fileBeanPkg.exists()) {
			fileBeanPkg.mkdirs();
		}
		String controllerPkg = srcBasePkg + "/" + root.get("controllerpackagename").toString();
		File fileControllerPkg = new File(controllerPkg);
		if(fileControllerPkg != null && !fileControllerPkg.exists()) {
			fileControllerPkg.mkdirs();
		}
		String servicePkg = srcBasePkg + "/" + root.get("servicepackagename").toString();
		File fileServicePkg = new File(servicePkg);
		if(fileServicePkg != null && !fileServicePkg.exists()) {
			fileServicePkg.mkdirs();
		}
		String daoPkg = srcBasePkg + "/" + root.get("daopackagename").toString();
		File fileDaoPkg = new File(daoPkg);
		if(fileDaoPkg != null && !fileDaoPkg.exists()) {
			fileDaoPkg.mkdirs();
		}
		String mapperPkg = srcBasePkg + "/" + root.get("mapperpackagename").toString();
		File fileMapperPkg = new File(mapperPkg);
		if(fileMapperPkg != null && !fileMapperPkg.exists()) {
			fileMapperPkg.mkdirs();
		}

	}

	/**
	 *  程序骨干
	 *
	 *  	1. 获取数据库表信息，字段信息
	 *  	2. 将表名及字段名转成java的驼峰样式
	 *  	        将数据库中表字段的类型转换为相应的java类型
	 *  	3. 根据每个表生成实体文件，控制层文件，业务层文件，Dao层文件，Mapper文件。
	 *
	 *
	 * @param root
	 * @param cfg
	 * @throws Exception
	 */
	private static void processAll(Map<String, Object> root, Configuration cfg) throws Exception {
		List<TableClass> list = DBUtil.getAllTableClasses(); // 获取所有表，放到TablaClass类中

		// 如下方法做了如下工作：
		//  0. 获取每个表的每个字段的名字，类型和注释
		//  1. 将数据库中表字段的类型转换为相应的java类型
		//  2. 将表名和字段名改成相应的驼峰样式
		DBUtil.paddingAllTableInfo(list);


		for(TableClass tc : list) {
			processPojo(root, cfg, tc);
			processController(root, cfg, tc);
			processService(root, cfg, tc);
			processDao(root, cfg, tc);
			processMapper(root, cfg, tc);
		}
	}



	private static void processController(Map<String, Object> root,
										  Configuration cfg, TableClass tc) throws Exception {
		root.put("pojo", tc.getClassname());
		Template template = cfg.getTemplate("controller.ftl");

		File file = new File(root.get("srcpath") + "/"
				+ root.get("basepackage").toString().replace(".", "/") + "/"
				+ root.get("controllerpackagename") + "/"
				+ tc.getClassname() + StringExUtil.capitaliseFirst(root.get("controllerpackagename").toString()) + ".java");
		FileWriter fileWriter = new FileWriter(file);
		BufferedWriter bufferedWritter = new BufferedWriter(fileWriter);
		template.process(root, bufferedWritter);
		bufferedWritter.flush();
		bufferedWritter.close();
		fileWriter.close();
	}

	private static void processService(Map<String, Object> root,
									   Configuration cfg, TableClass tc) {
	}

	private static void processDao(Map<String, Object> root, Configuration cfg,
								   TableClass tc) {
	}

	private static void processMapper(Map<String, Object> root,
									  Configuration cfg, TableClass tc) throws Exception {
		root.put("pojo", tc);
		Template template = cfg.getTemplate("mapper.ftl");

		File file = new File(root.get("srcpath") + "/" + root.get("basepackage").toString().replace(".", "/") + "/" + root.get("mapperpackagename") + "/" + tc.getClassname() + "Mapper.xml");
		FileWriter fileWriter = new FileWriter(file);
		BufferedWriter bufferedWritter = new BufferedWriter(fileWriter);
		template.process(root, bufferedWritter);
		bufferedWritter.flush();
		bufferedWritter.close();
		fileWriter.close();

	}

	private static void processPojo(
			Map<String, Object> root, Configuration cfg, TableClass tc)
			throws Exception {
		root.put("pojo", tc);
		Template template = cfg.getTemplate("pojo.ftl");

		File file = new File(root.get("srcpath") + "/"
				+ root.get("basepackage").toString().replace(".", "/") + "/"
				+ root.get("pojopackagename") + "/"
				+ tc.getClassname() + StringExUtil.capitaliseFirst(root.get("pojopackagename").toString()) + ".java");
		FileWriter fileWriter = new FileWriter(file);
		BufferedWriter bufferedWritter = new BufferedWriter(fileWriter);
		template.process(root, bufferedWritter);
		bufferedWritter.flush();
		bufferedWritter.close();
		fileWriter.close();
		
	/*	Writer out = new OutputStreamWriter(System.out);
		template.process(root, out);
		out.close();*/
	}
}
