BEGIN TRANSACTION;

DROP TABLE IF EXISTS person_lift;
DROP TABLE IF EXISTS person;
DROP TABLE IF EXISTS lift;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
	user_id SERIAL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);

CREATE TABLE person (
    person_id INT NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    birthdate DATE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,

    CONSTRAINT PK_person PRIMARY KEY (person_id),
    CONSTRAINT FK_person_users FOREIGN KEY (person_id) REFERENCES users(user_id)

);

CREATE TABLE lift (
    lift_id SERIAL,
    lift_name VARCHAR(100) NOT NULL UNIQUE,

    CONSTRAINT PK_lift PRIMARY KEY (lift_id)
);

CREATE TABLE person_lift (
    workout_id SERIAL,
    lift_id INT NOT NULL,
    person_id INT NOT NULL,
    date DATE NOT NULL,
    sets INT NOT NULL CHECK (sets >= 0),
    reps INT NOT NULL CHECK (reps >= 0),
    max_weight INT CHECK (max_weight >= 0),
    notes TEXT,

    CONSTRAINT PK_workout_id PRIMARY KEY (workout_id),
    CONSTRAINT unique_person_lift UNIQUE (lift_id, person_id, date),
    CONSTRAINT FK_person_lift_lift FOREIGN KEY (lift_id) REFERENCES lift(lift_id),
    CONSTRAINT FK_person_lift_person FOREIGN KEY (person_id) REFERENCES person(person_id)
);

COMMIT TRANSACTION;