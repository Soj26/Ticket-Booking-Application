INSERT INTO sec_user(name, email, encryptedPassword, enabled, balance)
VALUES('Loyd Alagao', 'alagao@sheridancollege.ca', '$2a$10$Nf3Iz5TbbGubtuvVOZCC.OI4yRNqil3yFhN10m6EJY0s4E2qMrEqy', TRUE, 1000.00);

INSERT INTO sec_user(name, email, encryptedPassword, enabled, balance)
VALUES('admin','admin@sheridancollege.ca','$2a$10$Nf3Iz5TbbGubtuvVOZCC.OI4yRNqil3yFhN10m6EJY0s4E2qMrEqy', TRUE, 1000.00);


INSERT INTO sec_role(roleName) VALUES ('ROLE_USER');
INSERT INTO sec_role(roleName) VALUES ('ROLE_ADMIN');

INSERT INTO user_role(userId, roleId) VALUES (1,1);
INSERT INTO user_role(userId, roleId) VALUES (2,2);


INSERT INTO tickets (userID, flightDetails, seatNumber, price, numberOfSeats, available)
VALUES (1, 'Flight 123, Toronto to Vancouver', '12A', 350.00, 1, TRUE);