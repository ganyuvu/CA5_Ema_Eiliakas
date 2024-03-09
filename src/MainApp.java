import java.sql.*;
import java.util.Scanner;

/**
 * Main author: Joseph Byrne
 * Other contributors: Ema Eiliakas, Julius Odeyami, Brandon...
 *
 */
public class MainApp {
    public static void main(String[] args) throws SQLException {

        Scanner keyboard = new Scanner(System.in);
        DatabaseSetUp databaseSetUp = DatabaseSetUp.getInstance();

        System.out.println("Hello there and welcome to our movie database");

        int choice;
        do {
            System.out.println("1. View all movies.");
            System.out.println("2.Insert new movie.");

            choice = keyboard.nextInt();
            keyboard.nextLine();

            switch (choice) {
                case 1:

                    break;
                case 2:
                    Movie newMovie = insertNewMovie(keyboard); //creating new movie and calling method
                    DatabaseSetUp.getInstance().insertMovie(newMovie); //inserts new movie into database
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
        } while (choice != 3); //Exits Loop
    }

    private static Movie insertNewMovie(Scanner keyboard) {
        Movie newMovie = new Movie();
        System.out.println("Enter Movie title:");
        newMovie.setTitle(keyboard.nextLine());
        System.out.println("Enter release year:");
        newMovie.setRelease_year(keyboard.nextInt());
        System.out.println("Enter genre:");
        newMovie.setGenre(keyboard.nextLine());
        System.out.println("Enter director:");
        newMovie.setDirector(keyboard.nextLine());
        System.out.println("Enter runtime in minutes:");
        newMovie.setRuntime_minutes(keyboard.nextInt());
        System.out.println("Enter rating:");
        newMovie.setRating(keyboard.nextDouble());
        return newMovie;
    }

}

    //CODE TO TEST INSERT
    //Movie movie = new Movie();
    //movie.setTitle("Spooderman");
    //movie.setRelease_year(2022);
    //movie.setGenre("Action");
    //movie.setDirector("Tom holland");
    //movie.setRuntime_minutes(120);
    //movie.setRating(8);
    //
    //DatabaseSetUp.getInstance().insertMovie(movie);

