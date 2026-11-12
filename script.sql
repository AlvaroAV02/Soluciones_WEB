
CREATE DATABASE IF NOT EXISTS db_ecommerce_acostaalvaro
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE db_ecommerce_acostaalvaro;

CREATE TABLE IF NOT EXISTS productos (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre        VARCHAR(150)   NOT NULL,
  categoria     VARCHAR(100)   NOT NULL,
  precio_compra DECIMAL(10,2)  NOT NULL,
  precio_venta  DECIMAL(10,2)  NOT NULL,
  stock         INT            NOT NULL DEFAULT 0,
  marca         VARCHAR(100),
  codigo_barras VARCHAR(50)    NOT NULL UNIQUE,
  fecha_ingreso DATE           NOT NULL
);

CREATE TABLE IF NOT EXISTS usuarios (
  id       BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50)  NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  rol      VARCHAR(20)  NOT NULL
);
