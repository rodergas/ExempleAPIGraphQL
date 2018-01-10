package serverGraphQL;

import java.lang.String;
import java.util.ArrayList;

public interface IInfrastructure {
  String InfrastructureType();

  String idInstance();

  GeographicalCoordinate locatedIn();

  ArrayList<IInfrastructure> nearByInfrastructure();
}
