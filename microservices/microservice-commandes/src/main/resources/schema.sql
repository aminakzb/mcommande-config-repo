CREATE TABLE commande
(
    id          INT AUTO_INCREMENT  PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    quantite    INT  NOT NULL,
    date        DATE NOT NULL,
    montant     float NOT NULL,
    produit_id  INT
);