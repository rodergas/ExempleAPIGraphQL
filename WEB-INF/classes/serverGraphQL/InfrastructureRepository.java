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

public class InfrastructureRepository {
  private final ArrayList<IInfrastructure> Infrastructures;

  private final Properties prop = new Properties();

  public InfrastructureRepository() {
    Infrastructures = new ArrayList<>();
    try{
    	 prop.load(getClass().getClassLoader().getResourceAsStream("../../config.properties"));
    	 VirtGraph graph = new VirtGraph (prop.getProperty("url_hostlist"), prop.getProperty("user"), prop.getProperty("password"));
    	 Query sparql = QueryFactory.create("Select * FROM <"+ prop.getProperty("dbName") +"> WHERE {"
    	 + " {?subjectInfrastructure <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.example.com/Infrastructure>.}"
    	 + " UNION "
    	 + " {?subjectMetroAndBusStop <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.example.com/MetroAndBusStop>.}"
    	 + " UNION "
    	 + " {?subjectBicingStation <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.example.com/BicingStation>.}"
    	 + "}" ); 
    	 VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
    	 ResultSet res = vqe.execSelect();
    	 while(res.hasNext()){
    	 	 QuerySolution qs = res.next();
    	 	 String subjectInfrastructure = null;
    	 	 if(qs.get("?subjectInfrastructure") != null) subjectInfrastructure = qs.get("?subjectInfrastructure").toString();
    	 	 String subjectMetroAndBusStop = null;
    	 	 if(qs.get("?subjectMetroAndBusStop") != null) subjectMetroAndBusStop = qs.get("?subjectMetroAndBusStop").toString();
    	 	 String subjectBicingStation = null;
    	 	 if(qs.get("?subjectBicingStation") != null) subjectBicingStation = qs.get("?subjectBicingStation").toString();
    	 	 if(subjectInfrastructure != null)Infrastructures.add(new Infrastructure(subjectInfrastructure)); 
    	 	 else if(subjectMetroAndBusStop != null)Infrastructures.add(new MetroAndBusStop(subjectMetroAndBusStop)); 
    	 	 else if(subjectBicingStation != null)Infrastructures.add(new BicingStation(subjectBicingStation)); 
    	 }

    	 graph.close();
    }catch (IOException ex){
    }
  }

  public List<IInfrastructure> getAllInfrastructures() {
    return Infrastructures;
  }

  public IInfrastructure getInfrastructure(String id) {
    IInfrastructure result = null; 
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
    	 	 if(tipo.equals("http://www.example.com/Infrastructure")) result =  new Infrastructure(id);
    	 	 else if(tipo.equals("http://www.example.com/MetroAndBusStop")) result = new MetroAndBusStop(id);
    	 	 else if(tipo.equals("http://www.example.com/BicingStation")) result = new BicingStation(id);
    	 } else { 
    	 	 result = null; 
    	 } 
    }catch (IOException ex){
    }
    return result; 
  }
}
