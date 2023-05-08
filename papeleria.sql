-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         10.5.12-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para papeleria
DROP DATABASE IF EXISTS `papeleria`;
CREATE DATABASE IF NOT EXISTS `papeleria` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `papeleria`;

-- Volcando estructura para tabla papeleria.alerta
DROP TABLE IF EXISTS `alerta`;
CREATE TABLE IF NOT EXISTS `alerta` (
  `IDProducto` int(10) unsigned DEFAULT NULL,
  `Aviso` varchar(100) DEFAULT NULL,
  `Fecha` date DEFAULT NULL,
  `Estado` varchar(10) DEFAULT NULL,
  KEY `FK__alerta_producto` (`IDProducto`),
  CONSTRAINT `FK__alerta_producto` FOREIGN KEY (`IDProducto`) REFERENCES `productos` (`IDProducto`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla papeleria.alerta: ~0 rows (aproximadamente)
DELETE FROM `alerta`;
/*!40000 ALTER TABLE `alerta` DISABLE KEYS */;
INSERT INTO `alerta` (`IDProducto`, `Aviso`, `Fecha`, `Estado`) VALUES
	(2, 'El producto "Pegamento en barra", ha alcanzado el stock mínimo.', '2023-05-02', 'NL');
/*!40000 ALTER TABLE `alerta` ENABLE KEYS */;

-- Volcando estructura para tabla papeleria.alerta_pedido
DROP TABLE IF EXISTS `alerta_pedido`;
CREATE TABLE IF NOT EXISTS `alerta_pedido` (
  `IDProducto` int(10) unsigned DEFAULT NULL,
  `Cantidad` int(11) DEFAULT NULL,
  `Razon` varchar(50) DEFAULT NULL,
  `Fecha` date DEFAULT NULL,
  `Estado` varchar(10) DEFAULT NULL,
  KEY `FK_alerta_pedido_productos` (`IDProducto`),
  CONSTRAINT `FK_alerta_pedido_productos` FOREIGN KEY (`IDProducto`) REFERENCES `productos` (`IDProducto`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla papeleria.alerta_pedido: ~0 rows (aproximadamente)
DELETE FROM `alerta_pedido`;
/*!40000 ALTER TABLE `alerta_pedido` DISABLE KEYS */;
/*!40000 ALTER TABLE `alerta_pedido` ENABLE KEYS */;

-- Volcando estructura para tabla papeleria.clientelocal
DROP TABLE IF EXISTS `clientelocal`;
CREATE TABLE IF NOT EXISTS `clientelocal` (
  `IDClienteLocal` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(30) DEFAULT NULL,
  `Apellidos` varchar(30) DEFAULT NULL,
  `Edad` int(11) DEFAULT NULL,
  `Correo` varchar(50) DEFAULT NULL,
  `Telefono` varchar(20) DEFAULT NULL,
  `RFC` varchar(20) DEFAULT NULL,
  `DomicilioF` varchar(70) DEFAULT NULL,
  `NomEmpresa` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`IDClienteLocal`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla papeleria.clientelocal: ~10 rows (aproximadamente)
DELETE FROM `clientelocal`;
/*!40000 ALTER TABLE `clientelocal` DISABLE KEYS */;
INSERT INTO `clientelocal` (`IDClienteLocal`, `Nombre`, `Apellidos`, `Edad`, `Correo`, `Telefono`, `RFC`, `DomicilioF`, `NomEmpresa`) VALUES
	(1, 'Pablo', 'Hernández Gómez', 21, 'hernandez.pablito@gmail.com', '2286365696', 'HEGP011205G90', 'Las Palmas #15, Xalapa, Ver.', 'Impulsarte'),
	(3, 'Mayde', 'Hernández Ortíz', 20, 'may.hernandez@gmail.com', '2283546080', 'HEOM011222DF3', 'Zaragoza #5, Xico, Ver.', 'Chedraui'),
	(5, 'Anilud', 'Hernández Ortíz', 19, 'ani.ortiz@gmail.com', '2282546055', 'HEOA020927JW6', 'Encinal #29, Xalapa, Ver.', 'Walmart'),
	(7, 'Milagros', 'Olmos Marín', 20, 'olmos.marin@gmail.com', '2281568070', 'OLMM0112141V8', 'Carriles #50, Coatepec, Ver.', 'Soriana'),
	(9, 'Alex', 'Ruíz Mendoza', 22, 'alex.ruiz@gmail.com', '2281789056', 'RUMA000421I82', 'Oaxaca #89, Xalapa, Ver.', 'Coppel'),
	(11, 'Lucía', 'Ortíz Guevara', 20, 'lucy.ortiz@gmail.com', '2281985630', 'ORGL010517T36', 'Jacarandas #3, Xalapa, Ver.', 'Oxxo'),
	(13, 'Servando', 'Hernández Galán', 22, 'servan.galan@gmail.com', '2281896350', 'HEGS991023TZ0', 'Arriaga #32, Xalapa, Ver.', 'Fasti'),
	(15, 'Eleazar', 'Ortíz Roque', 21, 'eleazar.ortiz@gmail.com', '2282509080', 'ORTE990823CV8', 'Covarrubias #90, Coatepec, Ver.', 'Famsa'),
	(17, 'Alicia', 'Martínez Hernández', 21, 'hernandez.alicia@gmail.com', '2282595085', 'MAFA980515HB2', 'Tesorería #12, Xalapa, Ver.', 'Starbucks'),
	(19, 'Jhonatan', 'Pale Colorado', 20, 'jhona.pale@gmail.com', '2281824459', 'PACJ0112293EA', '5 de mayo #20, Xalapa, Ver.', 'ITSX');
/*!40000 ALTER TABLE `clientelocal` ENABLE KEYS */;

-- Volcando estructura para tabla papeleria.clienteweb
DROP TABLE IF EXISTS `clienteweb`;
CREATE TABLE IF NOT EXISTS `clienteweb` (
  `IDClienteWeb` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(30) DEFAULT NULL,
  `Apellidos` varchar(30) DEFAULT NULL,
  `Edad` int(11) DEFAULT 0,
  `Correo` varchar(50) DEFAULT NULL,
  `Telefono` varchar(10) DEFAULT NULL,
  `RFC` varchar(20) DEFAULT NULL,
  `DomicilioF` varchar(70) DEFAULT NULL,
  `NomEmpresa` varchar(30) DEFAULT NULL,
  `Contrasena` varchar(50) DEFAULT NULL,
  `DatosBancarios` bigint(16) DEFAULT NULL,
  `CP` int(5) DEFAULT NULL,
  PRIMARY KEY (`IDClienteWeb`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla papeleria.clienteweb: ~10 rows (aproximadamente)
DELETE FROM `clienteweb`;
/*!40000 ALTER TABLE `clienteweb` DISABLE KEYS */;
INSERT INTO `clienteweb` (`IDClienteWeb`, `Nombre`, `Apellidos`, `Edad`, `Correo`, `Telefono`, `RFC`, `DomicilioF`, `NomEmpresa`, `Contrasena`, `DatosBancarios`, `CP`) VALUES
	(2, 'Daniela', 'Hernández Ortíz', 18, 'dani.hernandez@gmail.com', '2281824519', 'HEOD030818J29', 'Benito Juárez #29, Xico, Ver.', 'Coppel', 'dani123', NULL, NULL),
	(4, 'Fernando', 'Hernández Guevara', 23, 'fer.hernandez@gmail.com', '2289503125', 'HEGF980627NB9', 'Revolución #123, Xalapa, Ver.', 'Santander', 'fer123', NULL, NULL),
	(6, 'Zayra', 'Peñaloza Contreras', 22, 'peña.zay@gmail.com', '2285907180', 'PECZ000421MX7', 'Calvario #58, Xalapa, Ver.', 'Starbucks', 'zay123', NULL, NULL),
	(8, 'Ingrid', 'Guzmán Romero', 20, 'ingrid.romero@gmail.com', '2281598049', 'GURI011022H46', 'Ochoa #26, Xico, Ver.', 'INEGI', 'ing123', NULL, NULL),
	(10, 'Giovanni', 'Tlapa Pale', 21, 'giova.tlapa@gmail.com', '2283978560', '	TLPG000923GMA', 'Pino Suárez #16, Xalapa, Ver.', 'Coppel', 'gio123', NULL, NULL),
	(12, 'Abigail', 'Prado Caballero', 21, 'abi.prado@gmail.com', '2289705463', 'PACA0012155P2', 'Robles #19, Cosautlán, Ver.', 'UV', 'abi123', NULL, NULL),
	(14, 'Kalet', 'Vargas Hernández', 21, 'kalet.vargas@gmail.com', '2283154521', 'VAHK000426C91', 'Ciprés #12, Xalapa, Ver.', 'KFC', 'kal123', NULL, NULL),
	(16, 'Iván', 'Alba García', 21, 'ivan.alba@gmail.com', '2281789865', 'AAGI010930TY4', 'Briones #15, Xalapa, Ver.', 'ITSX', 'iva123', NULL, NULL),
	(18, 'Guadalupe', 'Gonzáles Landa', 22, 'lupita.landa@gmail.com', '2283564871', 'GOLG990505GA7', 'Loma Alta #4, Coatepec, Ver.', 'gua123', 'gon123', NULL, NULL),
	(20, 'Alejandra', 'Landa García', 21, 'ale.landa@gmail.com', '2281824563', 'LAGA001119CX9', 'San José #1, Xalapa, Ver.', 'BBVA', 'ale123', NULL, NULL);
/*!40000 ALTER TABLE `clienteweb` ENABLE KEYS */;

-- Volcando estructura para tabla papeleria.compra
DROP TABLE IF EXISTS `compra`;
CREATE TABLE IF NOT EXISTS `compra` (
  `IDCompra` int(11) NOT NULL AUTO_INCREMENT,
  `PrecioTotal` float DEFAULT NULL,
  `Fecha` date DEFAULT NULL,
  `CantProductos` int(11) DEFAULT NULL,
  PRIMARY KEY (`IDCompra`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla papeleria.compra: ~0 rows (aproximadamente)
DELETE FROM `compra`;
/*!40000 ALTER TABLE `compra` DISABLE KEYS */;
INSERT INTO `compra` (`IDCompra`, `PrecioTotal`, `Fecha`, `CantProductos`) VALUES
	(2, 736.5, '2022-05-22', 33);
/*!40000 ALTER TABLE `compra` ENABLE KEYS */;

-- Volcando estructura para tabla papeleria.devolucion
DROP TABLE IF EXISTS `devolucion`;
CREATE TABLE IF NOT EXISTS `devolucion` (
  `IDDevolucion` int(11) NOT NULL AUTO_INCREMENT,
  `IDVenta` int(11) DEFAULT NULL,
  `IDProducto` int(10) unsigned DEFAULT NULL,
  `IDProveedor` int(11) DEFAULT NULL,
  `DefectoP` varchar(30) DEFAULT NULL,
  `Fecha` date DEFAULT NULL,
  `TipoDev` enum('EF','CP') DEFAULT NULL,
  `Cantidad` int(11) DEFAULT NULL,
  PRIMARY KEY (`IDDevolucion`),
  KEY `FK__dev_productos` (`IDProducto`),
  KEY `FK__dev_proveedor` (`IDProveedor`),
  CONSTRAINT `FK__dev_productos` FOREIGN KEY (`IDProducto`) REFERENCES `productos` (`IDProducto`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK__dev_proveedor` FOREIGN KEY (`IDProveedor`) REFERENCES `proveedor` (`IDProveedor`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla papeleria.devolucion: ~15 rows (aproximadamente)
DELETE FROM `devolucion`;
/*!40000 ALTER TABLE `devolucion` DISABLE KEYS */;
INSERT INTO `devolucion` (`IDDevolucion`, `IDVenta`, `IDProducto`, `IDProveedor`, `DefectoP`, `Fecha`, `TipoDev`, `Cantidad`) VALUES
	(1, 4, 1, 3, 'Dañado', '2022-05-22', 'EF', 1),
	(2, 8, 20, 4, 'Dañado', '2022-05-22', 'CP', 1),
	(3, 14, 1, 3, 'Dañado', '2022-05-22', 'EF', 1),
	(4, 20, 1, 3, 'No deseado', '2023-04-30', 'CP', 1),
	(5, 22, 1, 3, 'Dañado', '2023-04-30', 'CP', 1),
	(6, 16, 2, 1, 'Dañado', '2023-04-30', 'CP', 1),
	(7, 20, 1, 3, 'No deseado', '2023-04-30', 'CP', 1),
	(8, 22, 1, 3, 'Dañado', '2023-04-30', 'CP', 1),
	(11, 24, 2, 1, 'Dañado', '2023-04-30', 'CP', 1),
	(12, 24, 8, 1, 'Dañado', '2023-05-01', 'CP', 1),
	(13, 24, 6, 4, 'No deseado', '2023-05-01', 'EF', 1),
	(14, 24, 6, 4, 'Dañado', '2023-05-01', 'EF', 1),
	(15, 20, 3, 2, 'No deseado', '2023-05-01', 'CP', 1),
	(16, 24, 8, 1, 'Dañado', '2023-05-01', 'CP', 1),
	(17, 24, 2, 1, 'No deseado', '2023-05-01', 'CP', 1),
	(18, 24, 2, 1, 'Dañado', '2023-05-01', 'CP', 1);
/*!40000 ALTER TABLE `devolucion` ENABLE KEYS */;

-- Volcando estructura para tabla papeleria.facturacion
DROP TABLE IF EXISTS `facturacion`;
CREATE TABLE IF NOT EXISTS `facturacion` (
  `NumFactura` int(11) NOT NULL AUTO_INCREMENT,
  `FechaExp` date DEFAULT NULL,
  `DescServicio` varchar(30) DEFAULT NULL,
  `PagoTotal` float DEFAULT NULL,
  `FormaPago` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`NumFactura`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla papeleria.facturacion: ~0 rows (aproximadamente)
DELETE FROM `facturacion`;
/*!40000 ALTER TABLE `facturacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `facturacion` ENABLE KEYS */;

-- Volcando estructura para tabla papeleria.gastos
DROP TABLE IF EXISTS `gastos`;
CREATE TABLE IF NOT EXISTS `gastos` (
  `IDGasto` int(11) NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(50) DEFAULT NULL,
  `Fecha` date DEFAULT NULL,
  `Monto` float DEFAULT NULL,
  `Tipo` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`IDGasto`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla papeleria.gastos: ~7 rows (aproximadamente)
DELETE FROM `gastos`;
/*!40000 ALTER TABLE `gastos` DISABLE KEYS */;
INSERT INTO `gastos` (`IDGasto`, `Nombre`, `Fecha`, `Monto`, `Tipo`) VALUES
	(1, 'Compra de mercancía', '2022-03-28', 77.5, 'Resurtido'),
	(3, 'Pago del recibo de luz', '2022-03-28', 850.3, 'Pago de servicio'),
	(5, 'Pago del recibo de agua', '2022-03-30', 350.5, 'Pago de servicio'),
	(7, 'Pago a empleados', '2022-03-31', 4500, 'Pago de nómina'),
	(9, 'Pago del recibo de internet', '2022-04-01', 470, 'Pago de servicio'),
	(11, 'Pago del recibo de luz', '2022-05-21', 850.3, 'Pago de servicio'),
	(13, 'Pago del recibo de agua', '2022-05-21', 340.2, 'Pago de servicio');
/*!40000 ALTER TABLE `gastos` ENABLE KEYS */;

-- Volcando estructura para tabla papeleria.notificacion
DROP TABLE IF EXISTS `notificacion`;
CREATE TABLE IF NOT EXISTS `notificacion` (
  `IDVentaWeb` int(11) DEFAULT NULL,
  `Mensaje` varchar(100) DEFAULT NULL,
  `Fecha` date DEFAULT NULL,
  KEY `FK_notificacion_ventaweb` (`IDVentaWeb`),
  CONSTRAINT `FK_notificacion_ventaweb` FOREIGN KEY (`IDVentaWeb`) REFERENCES `ventaweb` (`IDVentaWeb`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla papeleria.notificacion: ~1 rows (aproximadamente)
DELETE FROM `notificacion`;
/*!40000 ALTER TABLE `notificacion` DISABLE KEYS */;
INSERT INTO `notificacion` (`IDVentaWeb`, `Mensaje`, `Fecha`) VALUES
	(3, '¡Hay una nueva compra!', NULL);
/*!40000 ALTER TABLE `notificacion` ENABLE KEYS */;

-- Volcando estructura para tabla papeleria.productos
DROP TABLE IF EXISTS `productos`;
CREATE TABLE IF NOT EXISTS `productos` (
  `IDProducto` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `IDProveedor` int(11) DEFAULT NULL,
  `PrecioC` float unsigned DEFAULT NULL,
  `PrecioV` float unsigned DEFAULT NULL,
  `Marca` varchar(50) DEFAULT NULL,
  `Nombre` varchar(50) DEFAULT NULL,
  `CatProducto` varchar(30) DEFAULT NULL,
  `Disponibilidad` enum('D','ND') DEFAULT NULL,
  `Stock` int(11) DEFAULT NULL,
  `Limite` int(11) DEFAULT NULL,
  `Estado` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`IDProducto`),
  KEY `FK_productos_proveedor` (`IDProveedor`),
  CONSTRAINT `FK_productos_proveedor` FOREIGN KEY (`IDProveedor`) REFERENCES `proveedor` (`IDProveedor`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla papeleria.productos: ~27 rows (aproximadamente)
DELETE FROM `productos`;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` (`IDProducto`, `IDProveedor`, `PrecioC`, `PrecioV`, `Marca`, `Nombre`, `CatProducto`, `Disponibilidad`, `Stock`, `Limite`, `Estado`) VALUES
	(1, 3, 30, 48, 'NORMA', 'Libreta de cuadros', 'Manipulado del papel', 'D', 14, 5, 'NE'),
	(2, 1, 10.5, 16.5, 'PRITT', 'Pegamento en barra', 'Material escolar especializado', 'D', 0, 5, 'NE'),
	(3, 2, 7, 12, 'AZOR', 'Marcatextos amarillo', 'Artículo de escritura', 'D', 8, 5, 'NE'),
	(4, 5, 38, 52.5, 'JANEL', 'Cinta adhesiva', 'Material de embalaje', 'D', 6, 3, 'NE'),
	(5, 3, 70, 86.5, 'ACCO', 'Carpeta de vinil', 'Organización personal', 'D', 10, 3, 'NE'),
	(6, 4, 150, 175.5, 'CASIO', 'Calculadora de escritorio', 'Artículo de sobremesa', 'D', 9, 3, 'NE'),
	(7, 1, 85.5, 105, 'PILOT', 'Engrapadora', 'Artículo de sobremesa', 'D', 9, 3, 'NE'),
	(8, 1, 3.5, 6.5, 'COPAMEX', 'Cartulina blanca', 'Material escolar especializado', 'ND', 0, 5, 'NE'),
	(9, 3, 4.5, 7.5, 'COPAMEX', 'Cartulina amarilla', 'Material escolar especializado', 'D', 6, 5, 'NE'),
	(10, 3, 380.5, 449.5, 'CASIO', 'Calculadora científica Casio Fx-991E 552 ', 'Artículos de sobremesa', 'D', 30, 5, 'NE'),
	(11, 3, 18, 29.5, 'SCOTCH', 'Cinta adhesiva 550 Scotch 19Mmx', 'Material de embalaje', 'D', 150, 20, 'NE'),
	(12, 4, 8, 15.5, 'ACCO', 'Clip estándar Acco caja con 100 Piezas', 'Artículos de sobremesa', 'D', 110, 10, 'NE'),
	(13, 2, 130.8, 159.9, 'GRAPHER', 'Lapicero Grapher Prof 0.5mm ', 'Artículos de escritura', 'D', 90, 10, 'NE'),
	(14, 1, 35.9, 41.5, 'MAPPED', 'Lapices de grafito Triangulares 10 piezas Mapped', 'Artículos de escritura', 'D', 95, 20, 'NE'),
	(15, 4, 94.8, 109.5, 'XEROX', 'Paquete de hojas blancas Xerox 500 hojas ', 'Manipulados del papel', 'D', 147, 40, 'NE'),
	(16, 1, 48.8, 54.5, 'POST-IT', 'Mini cubo Post-it varios colores 400 hojas', 'Papeles y etiquetas', 'D', 76, 15, 'NE'),
	(17, 3, 187, 199.5, 'UNLINE', 'Tabla de aluminio con clip tamaño carta', 'Artículos de sobremesa', 'D', 94, 15, 'NE'),
	(18, 3, 24.8, 30.5, 'SCOTCH', 'Cinta de empaque transparente Scotch 48mmx50m', 'Material de embalaje', 'D', 85, 10, 'NE'),
	(19, 2, 189.9, 195.5, 'UNLINE', 'Sobre de solapa engomado manila tamaño oficio 40 p', 'Manipulados del papel', 'D', 154, 15, 'NE'),
	(20, 4, 223.9, 235.5, 'ACME', 'Engrapadora de golpe Acme', 'Artículos de sobre mesa', 'D', 74, 14, 'NE'),
	(21, 1, 18, 25.8, 'ACME', 'Desengrapadora ACME', 'Artículos de sobremesa', 'D', 65, 15, 'NE'),
	(22, 3, 68.9, 75.5, 'BACO', 'Broche Baco de 7cm Caja con 50 piezas', 'Artículos de sobremesa', 'D', 124, 30, 'NE'),
	(23, 3, 14.5, 20.5, 'JANEL', 'Etiqueta Blanca Econopack 50x100 - 72 peizas  Jane', 'Papeles y etiquetas', 'D', 8, 20, 'NE'),
	(24, 5, 20.4, 26.9, 'ACME', 'Perforadora de 2 orificios capacidad de 8 hojas', 'Artículos de sobremesa', 'D', 6, 30, 'NE'),
	(25, 1, 87.5, 96.9, 'BOSTITCH', 'Grapas de uso rudo BOSTITICH', 'Artículos de sobremesa', 'D', 2, 25, 'NE');
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;

-- Volcando estructura para tabla papeleria.productoscompra
DROP TABLE IF EXISTS `productoscompra`;
CREATE TABLE IF NOT EXISTS `productoscompra` (
  `IDCompra` int(11) DEFAULT NULL,
  `IDProducto` int(10) unsigned DEFAULT NULL,
  `CantProducto` int(11) DEFAULT NULL,
  `PrecioCompra` float DEFAULT NULL,
  KEY `FK__pc_compra` (`IDCompra`),
  KEY `FK__pc_productos` (`IDProducto`),
  CONSTRAINT `FK__pc_compra` FOREIGN KEY (`IDCompra`) REFERENCES `compra` (`IDCompra`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK__pc_productos` FOREIGN KEY (`IDProducto`) REFERENCES `productos` (`IDProducto`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla papeleria.productoscompra: ~2 rows (aproximadamente)
DELETE FROM `productoscompra`;
/*!40000 ALTER TABLE `productoscompra` DISABLE KEYS */;
INSERT INTO `productoscompra` (`IDCompra`, `IDProducto`, `CantProducto`, `PrecioCompra`) VALUES
	(2, 1, 20, 30),
	(2, 2, 13, 10.5);
/*!40000 ALTER TABLE `productoscompra` ENABLE KEYS */;

-- Volcando estructura para tabla papeleria.productoseliminados
DROP TABLE IF EXISTS `productoseliminados`;
CREATE TABLE IF NOT EXISTS `productoseliminados` (
  `IDProducto` int(11) DEFAULT NULL,
  `Nombre` varchar(50) DEFAULT NULL,
  `Cantidad` int(11) DEFAULT NULL,
  `Motivo` varchar(40) DEFAULT NULL,
  `Fecha` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla papeleria.productoseliminados: ~6 rows (aproximadamente)
DELETE FROM `productoseliminados`;
/*!40000 ALTER TABLE `productoseliminados` DISABLE KEYS */;
INSERT INTO `productoseliminados` (`IDProducto`, `Nombre`, `Cantidad`, `Motivo`, `Fecha`) VALUES
	(1, 'Libreta de cuadros', 1, 'Producto descontinuado', '2022-05-22'),
	(20, 'Engrapadora de golpe Acme', 2, 'Producto dañado', '2022-05-22'),
	(20, 'Engrapadora de golpe Acme', 2, 'Producto dañado', '2022-05-22'),
	(24, 'Perforadora de 2 orificios capacidad de 8 hojas', 160, 'Producto dañado', '2022-05-22'),
	(25, 'Grapas de uso rudo BOSTITICH', 150, 'Producto dañado', '2022-05-22'),
	(23, 'Etiqueta Blanca Econopack 50x100 - 72 peizas  Jane', 90, 'Producto dañado', '2022-05-22');
/*!40000 ALTER TABLE `productoseliminados` ENABLE KEYS */;

-- Volcando estructura para tabla papeleria.productospedido
DROP TABLE IF EXISTS `productospedido`;
CREATE TABLE IF NOT EXISTS `productospedido` (
  `IDPedido` int(11) DEFAULT NULL,
  `IDProducto` int(10) unsigned DEFAULT NULL,
  `Fecha` date DEFAULT NULL,
  `Estado` varchar(20) DEFAULT NULL,
  KEY `FK_productospedido_productos` (`IDProducto`),
  CONSTRAINT `FK_productospedido_productos` FOREIGN KEY (`IDProducto`) REFERENCES `productos` (`IDProducto`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla papeleria.productospedido: ~7 rows (aproximadamente)
DELETE FROM `productospedido`;
/*!40000 ALTER TABLE `productospedido` DISABLE KEYS */;
INSERT INTO `productospedido` (`IDPedido`, `IDProducto`, `Fecha`, `Estado`) VALUES
	(1, 1, '2022-05-22', 'Comprado'),
	(1, 2, '2022-05-22', 'Comprado'),
	(2, 24, '2022-05-22', 'Pendiente'),
	(3, 23, '2022-05-22', 'Pendiente'),
	(3, 25, '2022-05-22', 'Pendiente'),
	(4, 8, '2023-05-01', 'Pendiente'),
	(4, 1, '2023-05-01', 'Pendiente'),
	(5, 11, '2023-05-01', 'Pendiente');
/*!40000 ALTER TABLE `productospedido` ENABLE KEYS */;

-- Volcando estructura para tabla papeleria.productosventa
DROP TABLE IF EXISTS `productosventa`;
CREATE TABLE IF NOT EXISTS `productosventa` (
  `IDVenta` int(11) DEFAULT NULL,
  `IDProducto` int(10) unsigned DEFAULT NULL,
  `CantProducto` int(11) DEFAULT NULL,
  `PrecioVenta` float DEFAULT NULL,
  `PrecioCompra` float DEFAULT NULL,
  `EstadoP` varchar(20) DEFAULT NULL,
  `PrecioTotal` float DEFAULT NULL,
  KEY `Índice 1` (`IDProducto`),
  CONSTRAINT `FK_productosventa_productos` FOREIGN KEY (`IDProducto`) REFERENCES `productos` (`IDProducto`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla papeleria.productosventa: ~23 rows (aproximadamente)
DELETE FROM `productosventa`;
/*!40000 ALTER TABLE `productosventa` DISABLE KEYS */;
INSERT INTO `productosventa` (`IDVenta`, `IDProducto`, `CantProducto`, `PrecioVenta`, `PrecioCompra`, `EstadoP`, `PrecioTotal`) VALUES
	(2, 15, 1, 109.5, 94.8, 'Funcional', 109.5),
	(4, 1, 1, 48, 30, 'Dañado', 48),
	(6, 1, 1, 48, 30, 'Funcional', 48),
	(8, 20, 1, 235.5, 223.9, 'Dañado', 235.5),
	(10, 8, 1, 6.5, 3.5, 'Funcional', 6.5),
	(12, 17, 1, 199.5, 187, 'Funcional', 199.5),
	(12, 20, 1, 235.5, 223.9, 'Funcional', 235.5),
	(14, 1, 1, 48, 30, 'Funcional', 48),
	(14, 1, 1, 48, 30, 'Dañado', 48),
	(16, 2, 1, 16.5, 10.5, 'Funcional', 16.5),
	(18, 17, 2, 199.5, 187, 'Funcional', 399),
	(20, 3, 1, 12, 7, 'Funcional_Cambiado', 12),
	(20, 1, 1, 48, 30, 'Funcional_Cambiado', 48),
	(22, 1, 1, 48, 30, 'Dañado_Cambiado', 48),
	(16, 2, 1, 16.5, 10.5, 'Dañado_Cambiado', 16.5),
	(24, 2, 1, 16.5, 10.5, 'Funcional', 16.5),
	(24, 8, 2, 6.5, 3.5, 'Dañado_Cambiado', 13),
	(24, 6, 1, 175.5, 150, 'Funcional_Efectivo', 175.5),
	(24, 6, 1, 175.5, 150, 'Dañado_Efectivo', 175.5),
	(24, 2, 1, 16.5, 10.5, 'Funcional_Cambiado', 16.5),
	(24, 2, 1, 16.5, 10.5, 'Dañado_Cambiado', 16.5),
	(26, 4, 2, 52.5, 38, 'Funcional', 105),
	(26, 18, 2, 30.5, 24.8, 'Funcional', 61),
	(28, 2, 1, 16.5, 10.5, 'Funcional', 16.5);
/*!40000 ALTER TABLE `productosventa` ENABLE KEYS */;

-- Volcando estructura para tabla papeleria.proveedor
DROP TABLE IF EXISTS `proveedor`;
CREATE TABLE IF NOT EXISTS `proveedor` (
  `IDProveedor` int(11) NOT NULL AUTO_INCREMENT,
  `NombreProv` varchar(50) DEFAULT NULL,
  `NumPedidos` int(11) DEFAULT NULL,
  `Telefono` varchar(10) DEFAULT NULL,
  `Direccion` varchar(60) DEFAULT NULL,
  `Estado` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`IDProveedor`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla papeleria.proveedor: ~11 rows (aproximadamente)
DELETE FROM `proveedor`;
/*!40000 ALTER TABLE `proveedor` DISABLE KEYS */;
INSERT INTO `proveedor` (`IDProveedor`, `NombreProv`, `NumPedidos`, `Telefono`, `Direccion`, `Estado`) VALUES
	(1, 'Central Papelera', 4, '2281824585', 'Calle Este Ciudad A', 'NE'),
	(2, 'OficiMundo', 1, '2283514784', 'Calle Este Ciudad B', 'NE'),
	(3, 'Distribuidora Papelera', 4, '2289784510', 'Calle Sur Ciudad A', 'NE'),
	(4, 'Universal Papelerías', 1, '2288558519', 'Calle Este Ciudad C', 'NE'),
	(5, 'Cosmos Papelerías', 1, '2281809090', 'Calle Norte Ciudad A', 'NE'),
	(6, 'Distribuidora Central', 0, '2285663315', 'Calle Norte Ciudad G', 'NE'),
	(7, 'Distribuidora Universal', 0, '2266998986', 'Calle Norte Ciudad L', 'NE'),
	(8, 'Distribuidora Oeste', 0, '2122222626', 'Calle Oeste Ciudad A', 'NE'),
	(9, 'Distribuidora Norte', 0, '2282554560', 'Calle Sureste Ciudad P', 'NE'),
	(10, 'Prueba', 0, '1234567891', 'Direccion prueba', 'E'),
	(11, 'adadad', 0, '1234567891', 'sdasd', 'NE');
/*!40000 ALTER TABLE `proveedor` ENABLE KEYS */;

-- Volcando estructura para tabla papeleria.usuarios
DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE IF NOT EXISTS `usuarios` (
  `IDUsuario` int(11) NOT NULL,
  `Nombre` varchar(30) DEFAULT NULL,
  `Contraseña` varchar(20) DEFAULT NULL,
  `AreaT` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`IDUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla papeleria.usuarios: ~3 rows (aproximadamente)
DELETE FROM `usuarios`;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` (`IDUsuario`, `Nombre`, `Contraseña`, `AreaT`) VALUES
	(1, 'Ventas', '123456', 'Ventas'),
	(2, 'Compras', '123456', 'Compras'),
	(3, 'Finanzas', '123456', 'Finanzas');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;

-- Volcando estructura para tabla papeleria.ventalocal
DROP TABLE IF EXISTS `ventalocal`;
CREATE TABLE IF NOT EXISTS `ventalocal` (
  `IDVentaLocal` int(11) NOT NULL AUTO_INCREMENT,
  `IDClienteLocal` int(11) DEFAULT NULL,
  `NumFactura` int(11) DEFAULT NULL,
  `PrecioTotal` float DEFAULT NULL,
  `CantProductos` int(11) DEFAULT NULL,
  `Fecha` date DEFAULT NULL,
  `FormaPago` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`IDVentaLocal`),
  KEY `FK__vental_cliente` (`IDClienteLocal`),
  KEY `FK__vental_facturacion` (`NumFactura`),
  CONSTRAINT `FK__vental_cliente` FOREIGN KEY (`IDClienteLocal`) REFERENCES `clientelocal` (`IDClienteLocal`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK__vental_facturacion` FOREIGN KEY (`NumFactura`) REFERENCES `facturacion` (`NumFactura`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla papeleria.ventalocal: ~14 rows (aproximadamente)
DELETE FROM `ventalocal`;
/*!40000 ALTER TABLE `ventalocal` DISABLE KEYS */;
INSERT INTO `ventalocal` (`IDVentaLocal`, `IDClienteLocal`, `NumFactura`, `PrecioTotal`, `CantProductos`, `Fecha`, `FormaPago`) VALUES
	(2, NULL, NULL, 109.5, 1, '2022-05-21', 'Efectivo'),
	(4, NULL, NULL, NULL, NULL, '2022-05-21', 'PayPal'),
	(6, NULL, NULL, 48, 1, '2022-05-21', 'PayPal'),
	(8, NULL, NULL, NULL, NULL, '2022-05-21', 'PayPal'),
	(10, NULL, NULL, 6.5, 1, '2022-05-21', 'PayPal'),
	(12, NULL, NULL, 435, 2, '2022-05-22', 'Efectivo'),
	(14, NULL, NULL, 48, 1, '2022-05-22', 'Efectivo'),
	(16, NULL, NULL, 16.5, 1, '2023-05-02', 'Efectivo'),
	(18, NULL, NULL, 399, 2, '2023-02-13', 'Efectivo'),
	(20, NULL, NULL, 60, 3, '2023-04-25', 'Efectivo'),
	(22, NULL, NULL, NULL, NULL, '2023-04-25', 'Efectivo'),
	(24, NULL, NULL, 33, 2, '2023-04-30', 'Efectivo'),
	(26, NULL, NULL, 166, 4, '2023-05-01', 'Efectivo'),
	(28, NULL, NULL, 16.5, 1, '2023-05-01', 'Efectivo');
/*!40000 ALTER TABLE `ventalocal` ENABLE KEYS */;

-- Volcando estructura para tabla papeleria.ventaweb
DROP TABLE IF EXISTS `ventaweb`;
CREATE TABLE IF NOT EXISTS `ventaweb` (
  `IDVentaWeb` int(11) NOT NULL AUTO_INCREMENT,
  `IDClienteWeb` int(11) DEFAULT NULL,
  `NumFactura` int(11) DEFAULT NULL,
  `FechaCompra` date DEFAULT NULL,
  `FechaEntrega` date DEFAULT NULL,
  `PrecioTotal` float DEFAULT NULL,
  `CantProductos` int(11) DEFAULT NULL,
  `Estado` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`IDVentaWeb`) USING BTREE,
  KEY `FK__ventaw_cliente` (`IDClienteWeb`),
  KEY `FK__ventaw_facturacion` (`NumFactura`),
  CONSTRAINT `FK__ventaw_cliente` FOREIGN KEY (`IDClienteWeb`) REFERENCES `clienteweb` (`IDClienteWeb`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `FK__ventaw_facturacion` FOREIGN KEY (`NumFactura`) REFERENCES `facturacion` (`NumFactura`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Volcando datos para la tabla papeleria.ventaweb: ~2 rows (aproximadamente)
DELETE FROM `ventaweb`;
/*!40000 ALTER TABLE `ventaweb` DISABLE KEYS */;
INSERT INTO `ventaweb` (`IDVentaWeb`, `IDClienteWeb`, `NumFactura`, `FechaCompra`, `FechaEntrega`, `PrecioTotal`, `CantProductos`, `Estado`) VALUES
	(1, 4, NULL, '2022-05-25', '2022-05-25', 100, 100, 'Pendiente'),
	(3, 4, NULL, NULL, NULL, NULL, NULL, 'Pendiente');
/*!40000 ALTER TABLE `ventaweb` ENABLE KEYS */;

-- Volcando estructura para disparador papeleria.actAfTotalVentaL
DROP TRIGGER IF EXISTS `actAfTotalVentaL`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `actAfTotalVentaL` AFTER UPDATE ON `productosventa` FOR EACH ROW BEGIN
	DECLARE total FLOAT;
	DECLARE cant INT;
	
/*	SELECT SUM(PrecioTotal) INTO total FROM productosventa WHERE IDVenta = NEW.IDVenta AND EstadoP = 'Funcional';*/
	
	SELECT SUM(PrecioTotal) INTO total FROM (SELECT PrecioTotal FROM productosventa WHERE IDVenta = NEW.IDVenta AND EstadoP = 'Funcional' UNION ALL	SELECT PrecioTotal FROM productosventa WHERE IDVenta = NEW.IDVenta AND EstadoP = 'Funcional_Cambiado') AS totalT;
	
/*	SELECT SUM(CantProducto) INTO cant FROM productosventa WHERE IDVenta = NEW.IDVenta AND EstadoP = 'Funcional';*/
	
	SELECT SUM(CantProducto) INTO cant FROM (SELECT CantProducto FROM productosventa WHERE IDVenta = NEW.IDVenta AND EstadoP = 'Funcional' UNION ALL	SELECT CantProducto FROM productosventa WHERE IDVenta = NEW.IDVenta AND EstadoP = 'Funcional_Cambiado') AS totalT;
	
	UPDATE ventalocal SET PrecioTotal = total,CantProductos = cant WHERE IDVentaLocal = NEW.IDVenta;
	UPDATE ventaweb SET PrecioTotal = total,CantProductos = cant WHERE IDVentaWeb = NEW.IDVenta;
	
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador papeleria.actBeTotalVentaL
DROP TRIGGER IF EXISTS `actBeTotalVentaL`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER actBeTotalVentaL
BEFORE UPDATE ON productosventa
FOR EACH ROW
BEGIN
	SET NEW.PrecioTotal = NEW.CantProducto * NEW.PrecioVenta;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador papeleria.clientelocal_before_delete
DROP TRIGGER IF EXISTS `clientelocal_before_delete`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `clientelocal_before_delete` BEFORE DELETE ON `clientelocal` FOR EACH ROW BEGIN
	UPDATE ventalocal SET NumFactura = NULL WHERE IDCLienteLocal = OLD.IDClienteLocal;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador papeleria.compra_before_insert
DROP TRIGGER IF EXISTS `compra_before_insert`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `compra_before_insert` BEFORE INSERT ON `compra` FOR EACH ROW BEGIN
	SET NEW.IDCompra = (SELECT MAX(IDCompra) FROM compra) + 2;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador papeleria.devolucion_actProd
DROP TRIGGER IF EXISTS `devolucion_actProd`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `devolucion_actProd` AFTER INSERT ON `devolucion` FOR EACH ROW BEGIN
	DECLARE factura INT;
	
	if NEW.TipoDev = 'CP' AND NEW.DefectoP = 'Dañado' then BEGIN 
		UPDATE productos SET Stock = Stock - NEW.Cantidad WHERE IDProducto = NEW.IDProducto;
	END; END if;

	if NEW.TipoDev = 'EF' AND NEW.DefectoP = 'No deseado' then BEGIN 
		UPDATE productos SET Stock = Stock + NEW.Cantidad WHERE IDProducto = NEW.IDProducto;
	END; END if;

	SELECT NumFactura INTO factura FROM ventalocal WHERE IDVentaLocal = NEW.IDVenta;
	
	if factura IS NOT NULL then BEGIN
		DELETE FROM facturacion WHERE NumFactura = factura;
		UPDATE ventalocal SET IDClienteLocal = NULL WHERE IDVentaLocal = NEW.IDVenta;
	END; END if;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador papeleria.elimFacToActVenta
DROP TRIGGER IF EXISTS `elimFacToActVenta`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER elimFacToActVenta
BEFORE DELETE ON facturacion
FOR EACH ROW
BEGIN
	UPDATE ventalocal SET IDClienteLocal = NULL WHERE NumFactura = OLD.NumFactura;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador papeleria.elimVenLToElimProdV
DROP TRIGGER IF EXISTS `elimVenLToElimProdV`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `elimVenLToElimProdV` BEFORE DELETE ON `ventalocal` FOR EACH ROW BEGIN
	DELETE FROM productosventa WHERE IDVenta = OLD.IDVentaLocal;
	DELETE FROM devolucion WHERE IDVenta = OLD.IDVentaLocal;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador papeleria.gastos_before_insert
DROP TRIGGER IF EXISTS `gastos_before_insert`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `gastos_before_insert` BEFORE INSERT ON `gastos` FOR EACH ROW BEGIN
	SET NEW.IDGasto = (SELECT MAX(IDGasto) FROM gastos) + 2;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador papeleria.incClienteLocal
DROP TRIGGER IF EXISTS `incClienteLocal`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `incClienteLocal` BEFORE INSERT ON `clientelocal` FOR EACH ROW SET NEW.IDClienteLocal = (SELECT MAX(IDClienteLocal) + 2 FROM clientelocal)//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador papeleria.incClienteWeb
DROP TRIGGER IF EXISTS `incClienteWeb`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `incClienteWeb` BEFORE INSERT ON `clienteweb` FOR EACH ROW SET NEW.IDClienteWeb = (SELECT MAX(IDClienteWeb) + 2 FROM clienteweb)//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador papeleria.incVentalocal
DROP TRIGGER IF EXISTS `incVentalocal`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER incVentalocal BEFORE INSERT ON ventalocal
FOR EACH ROW 
SET NEW.IDVentaLocal = (SELECT MAX(IDVentaLocal) + 2 FROM ventalocal)//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador papeleria.incVentaWeb
DROP TRIGGER IF EXISTS `incVentaWeb`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER incVentaWeb BEFORE INSERT ON ventaWeb
FOR EACH ROW 
SET NEW.IDVentaWeb = (SELECT MAX(IDVentaWeb) + 2 FROM ventaWeb)//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador papeleria.insertAfTotalVentaL
DROP TRIGGER IF EXISTS `insertAfTotalVentaL`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `insertAfTotalVentaL` AFTER INSERT ON `productosventa` FOR EACH ROW BEGIN
	DECLARE total FLOAT;
	DECLARE cant INT;
	/*SELECT SUM(PrecioTotal) INTO total FROM productosventa WHERE IDVenta = NEW.IDVenta AND EstadoP = 'Funcional' OR EstadoP = 'Funcional_Cambiado';*/
	SELECT SUM(PrecioTotal) INTO total FROM (SELECT PrecioTotal FROM productosventa WHERE IDVenta = NEW.IDVenta AND EstadoP = 'Funcional' UNION ALL	SELECT PrecioTotal FROM productosventa WHERE IDVenta = NEW.IDVenta AND EstadoP = 'Funcional_Cambiado') AS totalT;
	/*SELECT SUM(CantProducto) INTO cant FROM productosventa WHERE IDVenta = NEW.IDVenta AND EstadoP = 'Funcional' OR EstadoP = 'Funcional_Cambiado';*/
	SELECT SUM(CantProducto) INTO cant FROM (SELECT CantProducto FROM productosventa WHERE IDVenta = NEW.IDVenta AND EstadoP = 'Funcional' UNION ALL	SELECT CantProducto FROM productosventa WHERE IDVenta = NEW.IDVenta AND EstadoP = 'Funcional_Cambiado') AS totalT;
	UPDATE ventalocal SET PrecioTotal = total,CantProductos = cant WHERE IDVentaLocal = NEW.IDVenta;
	UPDATE ventaweb SET PrecioTotal = total,CantProductos = cant WHERE IDVentaWeb = NEW.IDVenta;
	
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador papeleria.insertBeTotalVentaL
DROP TRIGGER IF EXISTS `insertBeTotalVentaL`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `insertBeTotalVentaL` BEFORE INSERT ON `productosventa` FOR EACH ROW BEGIN
	SET NEW.PrecioTotal = NEW.CantProducto * NEW.PrecioVenta;
	
	SELECT PrecioC INTO @p FROM productos WHERE IDProducto = NEW.IDProducto;
	SET NEW.PrecioCompra = @p;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador papeleria.productoscompra_after_insert
DROP TRIGGER IF EXISTS `productoscompra_after_insert`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `productoscompra_after_insert` AFTER INSERT ON `productoscompra` FOR EACH ROW BEGIN
	UPDATE productos SET Stock = Stock + NEW.CantProducto WHERE IDProducto = NEW.IDProducto;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador papeleria.productos_after_insert
DROP TRIGGER IF EXISTS `productos_after_insert`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `productos_after_insert` AFTER INSERT ON `productos` FOR EACH ROW BEGIN
	if NEW.Stock <= NEW.Limite then BEGIN 
		SELECT COUNT(*) INTO @total FROM alerta WHERE IDProducto = NEW.IDProducto;
		SELECT COUNT(*) INTO @total2 FROM productospedido WHERE IDProducto = NEW.IDProducto AND Estado = "Pendiente";
		
		if @total = 0 AND @total2 =0 AND NEW.Estado = 'NE' then begin
			SELECT CONCAT_WS('','El producto "',Nombre,'", ha alcanzado el stock mínimo.') INTO @NP FROM productos WHERE IDProducto = NEW.IDProducto;
			INSERT INTO alerta VALUES (NEW.IDProducto,@NP,CURDATE(),'NL');
		END; END if;
		
		if @total != 0 AND NEW.Estado = 'E' then BEGIN 
			DELETE FROM alerta WHERE IDProducto = NEW.IDProducto;
		END;END if;
		
	END; END if;
	
	if NEW.Estado = 'E' then BEGIN 
		DELETE FROM alerta WHERE IDProducto = NEW.IDProducto;
		DELETE FROM alerta_pedido WHERE IDProducto = NEW.IDProducto;
		DELETE FROM productospedido WHERE IDProducto = NEW.IDProducto;
		INSERT INTO productoseliminados VALUES (NEW.IDProducto,NEW.Nombre,0,'Eliminado',CURDATE());
	END;END if;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador papeleria.productos_after_update
DROP TRIGGER IF EXISTS `productos_after_update`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `productos_after_update` AFTER UPDATE ON `productos` FOR EACH ROW BEGIN
	if NEW.Stock <= NEW.Limite then BEGIN 
		SELECT COUNT(*) INTO @total FROM alerta WHERE IDProducto = NEW.IDProducto;
		SELECT COUNT(*) INTO @total2 FROM productospedido WHERE IDProducto = NEW.IDProducto AND Estado = "Pendiente";
		
		if @total = 0 AND @total2 =0 AND NEW.Estado = 'NE' then begin
			SELECT CONCAT_WS('','El producto "',Nombre,'", ha alcanzado el stock mínimo.') INTO @NP FROM productos WHERE IDProducto = NEW.IDProducto;
			INSERT INTO alerta VALUES (NEW.IDProducto,@NP,CURDATE(),'NL');
		END; END if;
		
		if @total != 0 AND NEW.Estado = 'E' then BEGIN 
			DELETE FROM alerta WHERE IDProducto = NEW.IDProducto;
		END;END if;
		
	END; END if;
	
	if NEW.Estado = 'E' then BEGIN 
		DELETE FROM alerta WHERE IDProducto = NEW.IDProducto;
		DELETE FROM alerta_pedido WHERE IDProducto = NEW.IDProducto;
		DELETE FROM productospedido WHERE IDProducto = NEW.IDProducto;
		INSERT INTO productoseliminados VALUES (NEW.IDProducto,NEW.Nombre,0,'Eliminado',CURDATE());
	END;END if;
	
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador papeleria.ventaweb_after_insert
DROP TRIGGER IF EXISTS `ventaweb_after_insert`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `ventaweb_after_insert` AFTER INSERT ON `ventaweb` FOR EACH ROW BEGIN
	if NEW.Estado = 'Pendiente' then BEGIN 
		SELECT COUNT(*) INTO @total FROM notificacion WHERE IDVentaWeb = NEW.IDVentaWeb;
		
		if @total = 0 then begin
			INSERT INTO notificacion VALUES (NEW.IDVentaWeb,'¡Hay una nueva compra!',NEW.FechaCompra);
		END; END if;
		
	END; END if;
	
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador papeleria.ventaweb_before_delete
DROP TRIGGER IF EXISTS `ventaweb_before_delete`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `ventaweb_before_delete` BEFORE DELETE ON `ventaweb` FOR EACH ROW BEGIN
	DELETE FROM devolucion WHERE IDVenta = OLD.IDVentaWeb;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador papeleria.ventaweb_before_update
DROP TRIGGER IF EXISTS `ventaweb_before_update`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `ventaweb_before_update` BEFORE UPDATE ON `ventaweb` FOR EACH ROW BEGIN
	if NEW.Estado = 'Entregado' then BEGIN 
		DELETE FROM notificacion WHERE IDVentaWeb = NEW.IDVentaWeb;
		SET NEW.FechaEntrega = CURDATE();
	END;END if;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
