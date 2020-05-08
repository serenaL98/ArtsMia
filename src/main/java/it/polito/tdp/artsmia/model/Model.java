package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	private Map<Integer, ArtObject> idMapOpere;
	
	public Model() {
		
		this.idMapOpere = new HashMap<>();
		
	}
	
	//in modo che in un futuro posso cancellare e modificare questo grafo
	public void creaGrafo() {
		//importo il dao
		ArtsmiaDAO dao = new ArtsmiaDAO();
		//prendo le opere
		List<ArtObject> opere = dao.listObjects();
				
		//definisco il grafo: non orientato, pesato e semplice
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//riempio la mappa tale da non avere doppioni
		for(ArtObject a: opere) {
			idMapOpere.put(a.getId(), a);
		}
		
		//aggiungo VERTICI
		Graphs.addAllVertices(this.grafo, this.idMapOpere.values());
		
		//aggiungo ARCHI
		for(Collegamento c: dao.peso()) {
			if(c.getPeso()>0) {
				Graphs.addEdge(this.grafo, this.idMapOpere.get(c.getO1()), this.idMapOpere.get(c.getO2()), c.getPeso());
			}
		}
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
}
