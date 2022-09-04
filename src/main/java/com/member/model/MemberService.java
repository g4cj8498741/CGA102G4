package com.member.model;
import java.util.List;

public class MemberService {
	
	private MemberDAO dao;
	
	public MemberService() {
		dao = new MemberDAOImpl();
	}
	
	public boolean insert(MemberVO MemberVO) {
		return dao.Insert(MemberVO);
	}
	
	public void update(MemberVO MemberVO) {
		dao.Update(MemberVO);
	}
	
	public void imgUpdate(MemberVO memberVO) {
		dao.ImgUpdate(memberVO);
	}
	
	public List<MemberVO> getAll(){
		return dao.GetAll();
	}
	
	public MemberVO login(String username, String password) {
		return dao.Login(username, password);
	}
	
	public MemberVO findByPrimaryKey(String meb_no) {
		return dao.FindByPrimaryKey(meb_no);
	}
	
	public boolean Register(String email) {
		return dao.Register(email);	
	}
	
	
	
}
