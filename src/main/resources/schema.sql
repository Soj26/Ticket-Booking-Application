-- User Table
CREATE TABLE sec_user (
                          userID BIGINT AUTO_INCREMENT PRIMARY KEY,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          encryptedPassword VARCHAR(255) NOT NULL,
                          enabled BOOLEAN NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          balance NUMERIC(10, 2) DEFAULT 0.00,
                          purchaseCount INT DEFAULT 0
);

-- Role Table
CREATE TABLE sec_role (
                          roleId BIGINT AUTO_INCREMENT PRIMARY KEY,
                          roleName VARCHAR(30) NOT NULL
);

-- User Role Junction Table
CREATE TABLE user_role (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           userID BIGINT NOT NULL,
                           roleId BIGINT NOT NULL,
                           CONSTRAINT user_role_uk UNIQUE (userID, roleId),
                           CONSTRAINT user_role_fk1 FOREIGN KEY (userID) REFERENCES sec_user(userID),
                           CONSTRAINT user_role_fk2 FOREIGN KEY (roleId) REFERENCES sec_role(roleId)
);

-- Tickets Table
CREATE TABLE tickets (
                         ticketID BIGINT AUTO_INCREMENT PRIMARY KEY,
                         ticketTitle VARCHAR(255) NOT NULL,
                         timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         userID BIGINT NOT NULL,
                         flightDetails VARCHAR(255) NOT NULL,
                         seatNumber VARCHAR(255) NOT NULL,
                         price NUMERIC(10, 2) NOT NULL,
                         numberOfSeats INT NOT NULL,
                         available BOOLEAN NOT NULL DEFAULT TRUE,
                         CONSTRAINT fk_ticket_user FOREIGN KEY (userID) REFERENCES sec_user(userID)
);
