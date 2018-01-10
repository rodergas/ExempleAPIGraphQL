package serverGraphQL;

import com.coxautodev.graphql.tools.SchemaParser;
import graphql.servlet.SimpleGraphQLServlet;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndPoint extends SimpleGraphQLServlet {
  public GraphQLEndPoint() {
    super(SchemaParser.newParser()
    .schemaString("type MetroAndBusStop implements IInfrastructure{\n"
        + "\tInfrastructureType: String!\n"
        + "\tstopName : String!\n"
        + "\tstopPhone : Int\n"
        + "\tstopAddress : String\n"
        + "\tlocatedIn : GeographicalCoordinate!\n"
        + "\tnearByInfrastructure : [IInfrastructure]\n"
        + "\tidInstance : String!\n"
        + "}\n"
        + "\n"
        + "type Suburb {\n"
        + "\tbelongsTo : District!\n"
        + "\tprovidesStop : [MetroAndBusStop]\n"
        + "\tsuburbName : String!\n"
        + "\tidInstance : String!\n"
        + "}\n"
        + "\n"
        + "type GeographicalCoordinate {\n"
        + "\tlatitude : Float!\n"
        + "\tlongitude : Float!\n"
        + "\tidInstance : String!\n"
        + "}\n"
        + "\n"
        + "type BicingStation implements IInfrastructure{\n"
        + "\tInfrastructureType: String!\n"
        + "\tstationBikesNumber : Int!\n"
        + "\tnearByStation : [BicingStation]\n"
        + "\tstationStreetNumber : Int!\n"
        + "\tstationType : String!\n"
        + "\tstationStatus : String!\n"
        + "\tstationSlotsNumber : Int!\n"
        + "\tstationID : ID!\n"
        + "\tstationAltitude : Float!\n"
        + "\tstationStreetName : String!\n"
        + "\tlocatedIn : GeographicalCoordinate!\n"
        + "\tnearByInfrastructure : [IInfrastructure]\n"
        + "\tidInstance : String!\n"
        + "}\n"
        + "\n"
        + "type District {\n"
        + "\tdistrictNestedList : String\n"
        + "\tdistrictName : String!\n"
        + "\tdistrictNumber : Int!\n"
        + "\tidInstance : String!\n"
        + "}\n"
        + "\n"
        + "interface IInfrastructure {\n"
        + "\tInfrastructureType: String!\n"
        + "\tidInstance : String!\n"
        + "\tlocatedIn : GeographicalCoordinate!\n"
        + "\tnearByInfrastructure : [IInfrastructure]\n"
        + "}\n"
        + "\n"
        + "type Infrastructure implements IInfrastructure {\n"
        + "\tInfrastructureType: String!\n"
        + "\tlocatedIn : GeographicalCoordinate!\n"
        + "\tnearByInfrastructure : [IInfrastructure]\n"
        + "\tidInstance : String!\n"
        + "}\n"
        + "\n"
        + "type Query {\n"
        + "\tallMetroAndBusStops: [MetroAndBusStop]\n"
        + "\tgetMetroAndBusStop(id: String!): MetroAndBusStop\n"
        + "\tallSuburbs: [Suburb]\n"
        + "\tgetSuburb(id: String!): Suburb\n"
        + "\tallGeographicalCoordinates: [GeographicalCoordinate]\n"
        + "\tgetGeographicalCoordinate(id: String!): GeographicalCoordinate\n"
        + "\tallBicingStations: [BicingStation]\n"
        + "\tgetBicingStation(id: String!): BicingStation\n"
        + "\tallDistricts: [District]\n"
        + "\tgetDistrict(id: String!): District\n"
        + "\tallInfrastructures: [IInfrastructure]\n"
        + "\tgetInfrastructure(id: String!): IInfrastructure\n"
        + "}\n"
        + "schema {\n"
        + "\tquery: Query\n"
        + "}\n"
        + "\n")
    .resolvers(new Query(new MetroAndBusStopRepository(), new SuburbRepository(), new GeographicalCoordinateRepository(), new BicingStationRepository(), new DistrictRepository(), new InfrastructureRepository()))
    .dictionary( MetroAndBusStop.class)
    .dictionary( Suburb.class)
    .dictionary( GeographicalCoordinate.class)
    .dictionary( BicingStation.class)
    .dictionary( District.class)
    .dictionary( Infrastructure.class)
    .build()
    .makeExecutableSchema());}
}
