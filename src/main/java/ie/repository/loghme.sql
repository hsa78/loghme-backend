CREATE DATABASE loghme;

USE loghme;

CREATE table User (
        id                  INT AUTO_INCREMENT PRIMARY KEY,
        firstName           VARCHAR(255) NOT NULL,
        lastName			VARCHAR(255) NOT NULL,
        email               VARCHAR(255) UNIQUE NOT NULL,
        phone				VARCHAR(20) NOT NULL,
        credit				bigint NOT NULL default 0,
        x					int,
        y					int,
        password            VARCHAR(255),
        foreign key(x, y) references Location(x, y)
);

CREATE TABLE Location (
	x	int,
    y	int,
    primary key(x, y)
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
	foodName			VARCHAR(255) NOT NULL,
    restaurantId	VARCHAR(255) NOT NULL references Restaurant(id),
	image			VARCHAR(255) NOT NULL,
    price			bigint NOT NULL,
    oldPrice		bigint default 0,
    type			CHAR(20) NOT NULL default 'ordinary',
    description		VARCHAR(255),
    popularity		float,
    count			int default 0,
    PRIMARY KEY		(foodName, restaurantId)
);

CREATE TABLE Cart (
	id				int NOT NULL,
    userId			int NOT NULL references User(id),
    totalPrice		bigint default 0,
    restaurantId	VARCHAR(255) references Restaurant(id),
    restaurantName	VARCHAR(255),
    status			VARCHAR(255) default 'OnProgress',
    deliveryId				VARCHAR(255) references Delivery(id),
    PRIMARY KEY(id, userId)
);

CREATE TABLE Delivery (
	id			VARCHAR(255) NOT NULL PRIMARY KEY,
    velocity 	int,
    timeToDest	float4,
    x			int,
    y			int,
    foreign key(x, y) references Location(x, y)
);

CREATE TABLE cartOrder(
	id			int NOT NULL,
    userId		int,
    cartId		int,
    count		int default 0,
    price		bigint default 0,
    foodName	VARCHAR(255),
    restaurantId VARCHAR(255),
    foreign key (foodName, restaurantId) references Food(foodName, restaurantId),
    foreign key (cartId, userId) references Cart(id, userId),
    PRIMARY KEY(id, userId, cartId)
);





