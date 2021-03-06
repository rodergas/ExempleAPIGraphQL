package serverGraphQL;

import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class GeographicalCoordinateRepository {
  private final ArrayList<GeographicalCoordinate> GeographicalCoordinates;

  private final Properties prop = new Properties();

  public GeographicalCoordinateRepository() {
    GeographicalCoordinates = new ArrayList<>();
    try{
    	 prop.load(getClass().getClassLoader().getResourceAsStream("../../config.properties"));
    	 VirtGraph graph = new VirtGraph (prop.getProperty("url_hostlist"), prop.getProperty("user"), prop.getProperty("password"));
    	 Query sparql = QueryFactory.create("Select ?subject FROM <"+ prop.getProperty("dbName") +"> WHERE {"
    	 + " ?subject <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.example.com/GeographicalCoordinate>."
    	 + "}");
     
    	 VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
    	 ResultSet res = vqe.execSelect();
    	 while(res.hasNext()){
    	 	 QuerySolution qs = res.next();
    	 	 String subject = qs.get("?subject").toString();
    	 	 GeographicalCoordinates.add(new GeographicalCoordinate(subject));
    	 }

    	 graph.close();
    }catch (IOException ex){
    }
  }

  public List<GeographicalCoordinate> getAllGeographicalCoordinates() {
    return GeographicalCoordinates;
  }

  public GeographicalCoordinate getGeographicalCoordinate(String id) {
    GeographicalCoordinate result = null; 
    try{
    	 prop.load(getClass().getClassLoader().getResourceAsStream("../../config.properties"));	 VirtGraph graph = new VirtGraph (prop.getProperty("url_hostlist"), prop.getProperty("user"), prop.getProperty("password"));
    	 Query sparql = QueryFactory.create("Select * FROM <"+ prop.getProperty("dbName") +"> WHERE {"
    	 + " {<"	 + id 	 + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?tipo.}"
    	 + "}" ); 
    	 VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
    	 ResultSet res = vqe.execSelect();
    	 String tipo = null;	 while(res.hasNext()){
    	 	 QuerySolution qs = res.next();
    	 	 if(qs.get("?tipo") != null) tipo = qs.get("?tipo").toString();
    	 }

    	 graph.close();
    	 if(tipo != null){
    	 	 if(tipo.equals("http://www.example.com/GeographicalCoordinate")) result =  new GeographicalCoordinate(id);
    	 } else { 
    	 	 result = null; 
    	 } 
    }catch (IOException ex){
    }
    return result; 
  }
}
