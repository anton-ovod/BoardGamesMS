CREATE TABLE IF NOT EXISTS boardgames (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    min_players INT NOT NULL,
    max_players INT NOT NULL,
    recommended_age INT NOT NULL,
    playing_time_minutes INT NOT NULL,
    publisher VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    rating DOUBLE NOT NULL
    );

CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
    );
