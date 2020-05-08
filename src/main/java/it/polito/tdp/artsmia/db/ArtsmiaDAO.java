package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Collegamento;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/*
	public int calcoloPeso(ArtObject a1, ArtObject a2) {
		final String sql = "SELECT count(DISTINCT e1.object_id) AS peso FROM exhibition_objects e1, exhibition_objects e2" + 
				"WHERE e1.exhibition_id = e2.exhibition_id AND e1.object_id = ? AND e2.object_id = ? ";
		int peso = 0;
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, a1.getId());
			ps.setInt(2, a2.getId());
			ResultSet res = ps.executeQuery();
			
			while(res.next()) {
				peso = res.getInt("peso");
			}
			
			conn.close();
			
			return peso;
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
		
	}*/
	
	public List<Collegamento> peso() {
		
		final String sql = "SELECT e2.object_id AS o2, e1.object_id AS o1, COUNT(*) AS peso"+
							" FROM exhibition_objects e1, exhibition_objects e2"+
							" WHERE e1.exhibition_id = e2.exhibition_id AND e1.object_id > e2.object_id"+
							" GROUP BY e1.object_id, e2.object_id";
		List<Collegamento> lista = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet res = ps.executeQuery();
			
			while(res.next()) {
				Collegamento coll = new Collegamento(res.getInt("o1"), res.getInt("o2"), res.getInt("peso") );
				lista.add(coll);
			}
			
			conn.close();
			
			return lista;
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
