import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseSetUp {

    private static final String URL = "jdbc:mysql://localhost/";
    private String dbname = "ca5_joseph_byrne";
    //private String username = "root";
    //private String password = "";

    private static DatabaseSetUp instance;

    private DatabaseSetUp()
    {

    }

    public static DatabaseSetUp getInstance()
    {
        if(instance == null)
            instance = new DatabaseSetUp();

        return instance;
    }

    public Connection getConnection()
    {
        try{
            Connection conn = DriverManager.getConnection
                    (URL+dbname);
            return conn;
        }
        catch (SQLException e){
            System.out.println("Unable to connect to database");
            return null;
        }
    }

    public List<Movie> getAllMovies() throws SQLException
    {
        List<Movie> movies = new ArrayList<>();
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("Select * from Movies");

        while (results.next())
        {
            Movie movie = new Movie();
            movie.setMovie_id(results.getInt("movie_id"));
            movie.setTitle(results.getString("title"));
            movies.add(movie);
        }

        conn.close();
        return movies;
    }

    public void insertMovie(Movie movie) throws SQLException {
        Connection conn = getConnection();
        String query = "Insert Into Movies VALUES (null, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, movie.getMovie_id());
            preparedStatement.setString(2, movie.getTitle());
            preparedStatement.setInt(3, movie.getRelease_year());
            preparedStatement.setString(4, movie.getGenre());
            preparedStatement.setString(5, movie.getDirector());
            preparedStatement.setInt(6, movie.getRuntime_minutes());
            preparedStatement.setDouble(7, movie.getRating());
            preparedStatement.executeUpdate(); //Will insert a new row
        } finally {
            conn.close();
        }
    }


}
