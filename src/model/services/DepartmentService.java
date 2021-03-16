package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	//Injetando a depedencia usando o Padrao de projetos Factory
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll(){
		
		return dao.findAll();
		
		
		/*
		//MOCKANDO DADOS = RETORNA DADOS DE MENTIRINHA
		List<Department> list = new ArrayList<>();
		list.add(new Department(1,"Livros"));
		list.add(new Department(2,"Computadores"));
		list.add(new Department(3,"Eletronicos"));
		return list;*/
	}
}
