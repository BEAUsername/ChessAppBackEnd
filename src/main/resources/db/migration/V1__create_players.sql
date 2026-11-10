CREATE TABLE players (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         username VARCHAR(12) NOT NULL UNIQUE,
                         creation_date DATE NOT NULL,
                         wins INT NOT NULL DEFAULT 0,
                         losses INT NOT NULL DEFAULT 0,
                         draws INT NOT NULL DEFAULT 0
);

INSERT INTO players (username, creation_date, wins, losses, draws) VALUES
                                                                       ('TestAcc001', '2026-01-15', 12, 3, 2),
                                                                       ('TestAcc002', '2026-03-02', 8, 5, 1),
                                                                       ('TestAcc003', '2026-05-20', 0, 0, 0),
                                                                       ('TestAcc004', '2026-05-08', 4, 6, 3);