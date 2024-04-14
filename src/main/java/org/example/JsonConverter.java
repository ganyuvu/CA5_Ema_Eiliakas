package org.example;
/**
 * Main author: Julius
 *
 */

import org.example.DTOs.Movie;
import com.google.gson.Gson;

import java.util.List;

public class JsonConverter
{
    public static final Gson gsonParser = new Gson();

    public static String moviesListToJson (List<Movie> list) {
        return gsonParser.toJson(list);
    }

    public static String singleMovieToJson(String json)
    {
        Movie movie = gsonParser.fromJson(json, Movie.class);

        return gsonParser.toJson(movie);
    }
}
