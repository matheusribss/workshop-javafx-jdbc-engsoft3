package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	
	//Injetando a depedencia usando o Padrao de projetos Factory
	private SellerDao dao = DaoFactory.createSellerDao();
	
	
	//RECUPERAR TODOS OS VENDEDORES
	public List<Seller> findAll(){
		
		return dao.findAll();
		
	}
	
	//INSERIR OU ATUALIZAR
	public void saveOrUpdate(Seller obj) {
		if (obj.getId()== null) {
			dao.insert(obj);
			
		}
		else {
			dao.update(obj);
		}
	}
	
	//REMOVER UM VENDEDOR DO BANCO DE DADOS
	public void remove (Seller obj) {
		dao.deleteById(obj.getId());
	}
}
