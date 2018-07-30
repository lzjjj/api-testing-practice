package exercises;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class RestAssuredExercises2Test {

    private static RequestSpecification requestSpec;

    static Stream <Arguments> circuitDataProvider() {
        return Stream.of(
                Arguments.of( "monza", "monza" )
        );
    }

    static Stream <Arguments> circuitDataProvider_1() {
        return Stream.of(
                Arguments.of( "monza", "Italy" )
        );
    }

    @BeforeAll
    public static void createRequestSpecification() {

        requestSpec = new RequestSpecBuilder().
                setBaseUri( "http://localhost" ).
                setPort( 9876 ).
                setBasePath( "/api/f1" ).
                build();
    }

    /*******************************************************
     * Use junit-jupiter-params for @ParameterizedTest that
     * specifies in which country
     * a specific circuit can be found (specify that Monza
     * is in Italy, for example)
     ******************************************************/

    @ParameterizedTest
    @MethodSource("circuitDataProvider_1")
    public void checkCountryForItaly(String circuitName, String country) {

        given()
                .pathParam( "country", circuitName )
                .spec( requestSpec )
                .when()
                .get( "/circuits/{country}.json" )
                .then()
                .assertThat()
                .body( "MRData.CircuitTable.Circuits[0].Location.country", equalTo( country ) );
    }

    /*******************************************************
     * Use junit-jupiter-params for @ParameterizedTest that specifies for all races
     * (adding the first four suffices) in 2015 how many
     * pit stops Max Verstappen made
     * (race 1 = 1 pitstop, 2 = 3, 3 = 2, 4 = 2)
     ******************************************************/

    //todo

    /*******************************************************
     * Request data for a specific circuit (for Monza this
     * is /circuits/monza.json)
     * and check the country this circuit can be found in
     ******************************************************/
    @ParameterizedTest
    @MethodSource("circuitDataProvider")
    public void checkCountryForCircuit(String circuitName, String circuitId) {

        given()
                .pathParam( "country", circuitName )
                .spec( requestSpec )
                .when()
                .get( "/circuits/{country}.json" )
                .then()
                .assertThat()
                .body( "MRData.CircuitTable.Circuits[0].circuitId", equalTo( circuitId ) );
    }

    /*******************************************************
     * Request the pitstop data for the first four races in
     * 2015 for Max Verstappen (for race 1 this is
     * /2015/1/drivers/max_verstappen/pitstops.json)
     * and verify the number of pit stops made
     ******************************************************/

    @Test
    public void checkNumberOfPitstopsForMaxVerstappenIn2015() {

        given().
                spec( requestSpec ).
                when()
                .get( "/2015/1/drivers/max_verstappen/pitstops.json" )
                .then()
                .assertThat()
                .body( "MRData.RaceTable.Races[0].PitStops.size()", equalTo( 1 ) );
    }
}