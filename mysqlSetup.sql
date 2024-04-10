CREATE TABLE IF NOT EXISTS Movies (
    movie_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    release_year INT,
    genre VARCHAR(100),
    director VARCHAR(255),
    runtime_minutes INT,
    rating DECIMAL(3,1)
    );

INSERT INTO Movies (title, release_year, genre, director, runtime_minutes, rating)
VALUES
    ('The Godfather', 1972, 'Crime, Drama', 'Francis Ford Coppola', 175, 9.2),
    ('The Dark Knight', 2008, 'Action, Crime, Drama', 'Christopher Nolan', 152, 9.0),
    ('Pulp Fiction', 1994, 'Crime, Drama', 'Quentin Tarantino', 154, 8.9);