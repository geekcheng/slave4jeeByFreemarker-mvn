package ${basepackage}.${controllerpackagename};

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import ${basepackage}.${servicepackagename}.${pojo}Service;



@Path("/${pojo}")
@Controller
public class ${pojo}BO {
	@Autowired
	private ${pojo}Service ${pojo}Service;


	@POST
	@Path("/findById")
	@Produces("application/json;charset=gbk")
	public String findById(@Context HttpServletRequest request, @Context HttpServletResponse response){
		System.out.println("findById()");
		String a = ${pojo}Service.findById();

		return a;
	}
		
}
