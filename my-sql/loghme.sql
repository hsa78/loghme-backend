-- CREATE DATABASE loghme;
--
USE loghme;

ALTER DATABASE
    loghme
    CHARACTER SET = utf8
    COLLATE = utf8_persian_ci;


CREATE TABLE Location (
	x	int,
    y	int,
    primary key(x, y)
);

CREATE table User (
        firstName           VARCHAR(255) NOT NULL,
        lastName			VARCHAR(255) NOT NULL,
        email               VARCHAR(255) primary key NOT NULL,
        phone				VARCHAR(20) NOT NULL,
        credit				bigint NOT NULL default 0,
        x					int,
        y					int,
        password            VARCHAR(255),
        foreign key(x, y) references Location(x, y)
);


CREATE TABLE Restaurant (
	id		VARCHAR(255) PRIMARY KEY,
    name	VARCHAR(255) NOT NULL,
    logo	VARCHAR(255) NOT NULL,
    x		int,
    y		int,
    foreign key(x, y) references Location(x, y)
);

CREATE TABLE Food (
	name		VARCHAR(255) NOT NULL,
    restaurantId	VARCHAR(255) NOT NULL references Restaurant(id),
	image			VARCHAR(255) NOT NULL,
    price			bigint NOT NULL,
    oldPrice		bigint default NULL,
    type			CHAR(20) NOT NULL default 'ordinary',
    description		VARCHAR(255),
    popularity		float,
    count			int default null,
    unique (name, restaurantId, type),
    id				bigint auto_increment,
    active			bool default true,
    PRIMARY KEY		(id)
);

CREATE TABLE Cart (
	id				int auto_increment NOT NULL,
    userEmail		varchar(255) NOT NULL references User(email),
    restaurantId	VARCHAR(255) references Restaurant(id),
    restaurantName	VARCHAR(255),
    status			VARCHAR(255) default 'OnProgress',
    deliveryId				VARCHAR(255) references Delivery(id),
    PRIMARY KEY(id)
);

CREATE TABLE Delivery (
	id			VARCHAR(255) NOT NULL PRIMARY KEY,
    velocity 	int,
    timeToDest	float4 default 0,
    x			int,
    y			int,
    foreign key(x, y) references Location(x, y)
);

CREATE TABLE CartOrder(
	id			int auto_increment NOT NULL,
    cartId		int NOT NULL,
    count		int default 0,
    foodId		BIGINT,
    foreign key (foodId) references Food(id) on delete cascade,
    foreign key (cartId) references Cart(id),
    PRIMARY KEY(id)
);

SELECT * 
FROM Restaurant res;

SELECT *
FROM Location Loc;

SELECT count(*)
FROM Food food;

SELECT *
FROM User u;

SELECT *
FROM Delivery;

SELECT *
FROM Cart c;

SELECT * 
FROM CartOrder c
where c.cartId = 4;
