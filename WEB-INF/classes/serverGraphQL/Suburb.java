package serverGraphQL;

import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class Suburb {
  private Properties prop = new Properties();

  private String idTurtle;

  public Suburb(String idTurtle) {
    this.idTurtle = idTurtle;
  }

  public String idInstance() {
    return idTurtle;
  }

  public District belongsTo() {
    ArrayList<String> result = connectVirtuoso("http://www.example.com/belongsTo");
    if(result.size() == 0) return null;
    else return new District(result.get(0));
  }

  public ArrayList<MetroAndBusStop> providesStop() {
    ArrayList<String> providesStop = connectVirtuoso("http://www.example.com/providesStop");
    ArrayList<MetroAndBusStop> providesStops = new ArrayList<>();
    for(String id:providesStop) providesStops.add(new MetroAndBusStop(id));
    if(providesStops.size() == 0) return null;
    else return providesStops;
  }

  public String suburbName() {
    ArrayList<String> result = connectVirtuoso("http://www.example.com/suburbName") ;
    if(result.size() == 0) return null;
    else return modifyScalarValue(result.get(0));
  }

  public ArrayList<String> connectVirtuoso(String value, String id) {
    ArrayList<String> valor = new ArrayList<>();

    try{
    	 prop.load(getClass().getClassLoader().getResourceAsStream("../../config.properties")); 
    	 VirtGraph graph = new VirtGraph (prop.getProperty("url_hostlist"), prop.getProperty("user"), prop.getProperty("password"));
    	 Query sparql = QueryFactory.create("Select ?valor FROM <"+ prop.getProperty("dbName") +"> WHERE {"
    	 + " <"+ id +"> <"+  value + "> ?valor."
    	 + "}");
     
    	 VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
    	 ResultSet res = vqe.execSelect();
    	 while(res.hasNext()){
    	 	 QuerySolution qs = res.next();
    	 	 valor.add(qs.get("?valor").toString());
    	 }

    	graph.close();
    }catch (IOException ex){
    }
    return valor;
  }

  public ArrayList<String> connectVirtuoso(String value) {
    ArrayList<String> valor = new ArrayList<>();

    try{
    	 prop.load(getClass().getClassLoader().getResourceAsStream("../../config.properties")); 
    	 VirtGraph graph = new VirtGraph (prop.getProperty("url_hostlist"), prop.getProperty("user"), prop.getProperty("password"));
    	 Query sparql = QueryFactory.create("Select ?valor FROM <"+ prop.getProperty("dbName") +"> WHERE {"
    	 + " <"+ this.idTurtle +"> <"+  value + "> ?valor."
    	 + "}");
     
    	 VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, graph);
    	 ResultSet res = vqe.execSelect();
    	 while(res.hasNext()){
    	 	QuerySolution qs = res.next();
    	 	valor.add(qs.get("?valor").toString());
    	 }

    	graph.close();
    }catch (IOException ex){
    }
    return valor;
  }

  public String modifyScalarValue(String value) {
    int index = value.toString().indexOf("^");
    String resultat =  value.toString().substring(0, index);
    return resultat;
  }
}
