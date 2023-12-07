INSERT INTO sec_user(name, email, encryptedPassword, enabled, balance)
VALUES('Loyd Alagao', 'alagao@sheridancollege.ca', '$2a$10$Nf3Iz5TbbGubtuvVOZCC.OI4yRNqil3yFhN10m6EJY0s4E2qMrEqy', TRUE, 1000.00);

INSERT INTO sec_user(name, email, encryptedPassword, enabled, balance)
VALUES('admin','admin@sheridancollege.ca','$2a$10$Nf3Iz5TbbGubtuvVOZCC.OI4yRNqil3yFhN10m6EJY0s4E2qMrEqy', TRUE, 1000.00);


INSERT INTO sec_role(roleName) VALUES ('ROLE_USER');
INSERT INTO sec_role(roleName) VALUES ('ROLE_ADMIN');

INSERT INTO user_role(userId, roleId) VALUES (1,1);
INSERT INTO user_role(userId, roleId) VALUES (2,2);


INSERT INTO tickets (ticketTitle, timestamp, userID, flightDetails, seatNumber, price, numberOfSeats, available)
VALUES ('Morning Flight', '2023-01-01 08:00:00', 1, 'Toronto to Vancouver', '12A', 350.00, 1, TRUE),                                                                                                                     ('Evening Flight', '2023-01-02 18:00:00', 2, 'Vancouver to Toronto', '14C', 450.00, 2, TRUE);
