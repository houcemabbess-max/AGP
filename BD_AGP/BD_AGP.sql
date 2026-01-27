
DROP DATABASE IF EXISTS agp;

-- 1) Base
CREATE DATABASE agp
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE agp;

-- 2) Tables
CREATE TABLE island (
  id   INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_island_name (name)
) ENGINE=InnoDB;

CREATE TABLE site (
  id          INT NOT NULL,
  name        VARCHAR(150) NOT NULL,
  entry_price DOUBLE NOT NULL DEFAULT 0,
  duration    DOUBLE NOT NULL DEFAULT 0,      -- ex: heures
  latitude    DOUBLE DEFAULT NULL,
  longitude   DOUBLE DEFAULT NULL,
  type        ENUM('HISTORIQUE','NATURE','LOISIR','CULTURE') NOT NULL,
  island_id   INT NOT NULL,
  PRIMARY KEY (id),
  KEY idx_site_island (island_id),
  KEY idx_site_type (type),
  CONSTRAINT fk_site_island
    FOREIGN KEY (island_id) REFERENCES island(id)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE hotel (
  id            INT NOT NULL,
  name          VARCHAR(150) NOT NULL,
  price_per_day DOUBLE NOT NULL,
  stars         TINYINT UNSIGNED NOT NULL,
  latitude      DOUBLE DEFAULT NULL,
  longitude     DOUBLE DEFAULT NULL,
  island_id     INT NOT NULL,
  PRIMARY KEY (id),
  KEY idx_hotel_island (island_id),
  KEY idx_hotel_price (price_per_day),
  CONSTRAINT chk_hotel_stars CHECK (stars BETWEEN 1 AND 5),
  CONSTRAINT fk_hotel_island
    FOREIGN KEY (island_id) REFERENCES island(id)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- 3) Données
INSERT INTO island (id, name) VALUES

(2, 'Îlet à Cordes'),
(3, 'Îlet des Salazes'),
(4, 'Îlet à Guillaume'),
(5, 'Cirque de Mafate'),
(6, 'Cirque de Cilaos'),
(7, 'Cirque de Salazie');

INSERT INTO site (id, name, entry_price, duration, latitude, longitude, type, island_id) VALUES
(101, 'Piton de la Fournaise', 0, 5,   -21.2444, 55.7081, 'NATURE',      1),
(102, 'Voile de la Mariée',    0, 2,   -21.0325, 55.5446, 'NATURE',      7),
(103, 'Maison Folio',          6, 1.5, -21.1365, 55.4726, 'HISTORIQUE',  6),
(104, 'Sentier Scout Mafate',  0, 6,   -21.0750, 55.4450, 'LOISIR',      5);

INSERT INTO hotel (id, name, price_per_day, stars, latitude, longitude, island_id) VALUES
-- La Réunion (côte Ouest / Nord / Sud / Est)
(201, 'Hotel Le Boucan Canot',      170, 4, -20.9969, 55.2356, 1),
(202, 'Hotel Saint-Gilles Lagoon',  210, 4, -21.0513, 55.2297, 1),
(203, 'Hotel Saint-Denis Centre',   140, 3, -20.8789, 55.4481, 1),
(204, 'Hotel Saint-Pierre Beach',   160, 3, -21.3401, 55.4781, 1),
(205, 'Eco-Lodge Sainte-Rose',      120, 3, -21.1268, 55.7996, 1),

-- Cirque de Salazie (zone 7)
(206, 'Gite Salazie - Hell Bourg',   95, 2, -21.0604, 55.5220, 7),

-- Cirque de Cilaos (zone 6)
(207, 'Hotel Cilaos Thermal',       130, 3, -21.1369, 55.4719, 6),

-- Cirque de Mafate (zone 5)
(208, 'Gite Mafate - La Nouvelle',   85, 2, -21.0740, 55.4440, 5),

-- Îlets
(209, 'Gite Ilet a Cordes',          80, 2, -21.2800, 55.5200, 2),
(210, 'Gite Ilet des Salazes',       78, 2, -21.1100, 55.4600, 3),
(211, 'Gite Ilet a Guillaume',       82, 2, -21.0400, 55.4600, 4);

