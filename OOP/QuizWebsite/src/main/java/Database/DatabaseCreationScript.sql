CREATE DATABASE IF NOT EXISTS tank_database;

USE tank_database;

CREATE TABLE IF NOT EXISTS users
(
    id       INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(30)  NOT NULL,
    password VARCHAR(150) NOT NULL,
    is_admin BOOLEAN      NOT NULL
);

INSERT INTO users (username, password, is_admin)
VALUES ('admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', true);

CREATE TABLE IF NOT EXISTS quizzes
(
    id                      INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name                    TEXT  NOT NULL,
    description             TEXT NOT NULL,
    creator_id              INT,
    has_random_questions    BOOLEAN,
    is_one_page             BOOLEAN,
    is_immediate_correction BOOLEAN,
    has_practice_mode       BOOLEAN,
    creation_date_time      TIMESTAMP,
    submission_count        INT,
    FOREIGN KEY (creator_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS question_types
(
    type_id INT          NOT NULL primary key,
    type    VARCHAR(150) NOT NULL
);

INSERT INTO question_types (type_id, type)
VALUES (0, 'Question-Response'),
       (1, 'Fill in the Blank'),
       (2, 'Multiple Choice'),
       (3, 'Picture-Response Questions');


CREATE TABLE IF NOT EXISTS questions
(
    quiz_id       INT          NOT NULL,
    question_id   INT          NOT NULL AUTO_INCREMENT primary key,
    question_type INT          NOT NULL,
    content       TEXT NOT NULL,
    FOREIGN KEY (question_type) REFERENCES question_types(type_id)
);

CREATE TABLE IF NOT EXISTS answers
(
    question_id INT          NOT NULL,
    answer_id   INT          NOT NULL AUTO_INCREMENT primary key,
    content     VARCHAR(150) NOT NULL,
    is_correct  BOOLEAN,
    FOREIGN KEY (question_id) REFERENCES questions(question_id)
);

CREATE TABLE IF NOT EXISTS mails
(
    Id              serial AUTO_INCREMENT primary key,
    from_Id         INT,
    to_Id           INT,
    message_type    varchar(5),
    message_title   varchar(200),
    message_content varchar(1000),
    send_date       date,
    FOREIGN KEY (from_Id) REFERENCES users(id),
    FOREIGN KEY (to_Id) REFERENCES users(id)
);
CREATE TABLE IF NOT EXISTS friends
(
    first_person  INT NOT NULL,
    second_person INT NOT NULL,
    FOREIGN KEY (first_person) REFERENCES users(id),
    FOREIGN KEY (second_person) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS announcements
(
    admin_id        INT           NOT NULL,
    announcement_id INT           NOT NULL AUTO_INCREMENT primary key,
    announcement    VARCHAR(1000) NOT NULL,
    FOREIGN KEY (admin_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS quiz_results
(
    quiz_id       INT NOT NULL,
    user_id       INT NOT NULL,
    result_id     serial AUTO_INCREMENT primary key,
    score         INT NOT NULL,
    percent       INT NOT NULL,
    quiz_duration INT NOT NULL,
    finish_time   TIMESTAMP,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS achievements
(
    achievement_id   INT PRIMARY KEY AUTO_INCREMENT,
    achievement_name VARCHAR(100) NOT NULL,
    requirement      VARCHAR(200) NOT NULL
);

INSERT INTO achievements (achievement_name, requirement)
VALUES ('Amateur Author', 'Created 1 quiz'),
       ('Prolific Author', 'Created 5 quizzes'),
       ('Prodigious Author', 'Created 10 quizzes'),
       ('Quiz Machine', 'Took 10 quizzes'),
       ('I am the Greatest', 'Scored the highest in a quiz');

CREATE TABLE IF NOT EXISTS user_achievements
(
    user_id        INT NOT NULL,
    achievement_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (achievement_id) REFERENCES achievements(achievement_id)
);