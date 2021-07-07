-- Insert users in DB
INSERT INTO users
VALUES (1, 'test 123', 0, 123456789, 'mailclassic@test.com', 'pass', '15123451', 'user', 'active', 'testclassic', '123');
INSERT INTO users
VALUES (2, 'test 123', 2, 223456789, 'mailgold@test.com', 'pass', '15123452', 'user', 'active', 'testgold', '123');
INSERT INTO users
VALUES (3, 'test 123', 1, 323456789, 'mailsilver@test.com', 'pass', '15123453', 'user', 'active', 'testsilver', '123');

-- Insert Items
INSERT INTO items (id, title, description, auction, status, owner, base_price, current_price, winner_id)
VALUES (1, 'Macbook Pro 2018', 'Macbook pro 2018 con poco uso y mucho cuidado', 1, 'FINISHED', 1, 15000.0, 25000.0, 1);

INSERT INTO items (id, title, description, auction, status, owner, base_price, current_price)
VALUES (2, 'Calzado montaña', 'Calzado robusto para hacer montañismo', 2, 'PENDING', 2, 500.0, 500.0);

INSERT INTO items (id, title, description, auction, status, owner, base_price, current_price)
VALUES (3, 'Calzado deportivo Nike', 'Botines para deportes varios en superficie de pasto', 2, 'PENDING', 3, 750.0, 750.0);

INSERT INTO items (id, title, description, auction, status, owner, base_price, current_price)
VALUES (4, 'Zapatilla Basquet', 'Calzado deportivo para deportes como básquet o voley', 2, 'PENDING', 1, 600.0, 600.0);

INSERT INTO items (id, title, description, auction, status, owner, base_price, current_price)
VALUES (5, 'Altas llantas', 'Las llantas más altas del mercado', 2, 'PENDING', 1, 350.0, 350.0);

INSERT INTO items (id, title, description, auction, status, owner, base_price, current_price)
VALUES (6, 'Pelota clásica', 'Pelota clásica de futbol', 2, 'PENDING', 2, 400.0, 400.0);

INSERT INTO items (id, title, description, auction, status, owner, base_price, current_price)
VALUES (7, 'Juego de te', 'Colección de tetera y copas + platos para tomar el te', 3, 'PENDING', 1, 2500.0, 2500.0);

INSERT INTO items (id, title, description, auction, status, owner, base_price, current_price)
VALUES (8, 'Masajeador', 'Masajeador de hombros y espalda. Muy versátil', 3, 'PENDING', 3, 4000.0, 4000.0);

INSERT INTO items (id, title, description, auction, status, owner, base_price, current_price)
VALUES (9, 'Plancha de pelo', 'Electrodomestico para planchado de pelo', 3, 'PENDING', 1, 2300.0, 2300.0);

INSERT INTO items (id, title, description, auction, status, owner, base_price, current_price, currency)
VALUES (10, 'Celular random', 'Celular en perfecto estado. Caracteristidas: \n probamos un \\n', 3, 'PENDING', 2, 17000.0, 17000.0, 1);

INSERT INTO items (id, title, description, auction, status, owner, base_price, current_price, currency)
VALUES (11, 'Nintendo switch', 'Consola portatil y para conectar a la televisión', 4, 'PENDING', 1, 350.0, 350.0, 1);

INSERT INTO items (id, title, description, auction, status, owner, base_price, current_price, currency)
VALUES (12, 'Play Station 5 XL', 'Consola de videojuegos.', 4, 'PENDING', 2, 499.0, 499.0, 1);

INSERT INTO items (id, title, description, auction, status, owner, base_price, current_price, currency)
VALUES (13, 'Xbox', 'Poco uso, incluye juegos', 4, 'PENDING', 1, 450.0, 450.0, 1);

INSERT INTO items (id, title, description, auction, status, owner, base_price, current_price, currency)
VALUES (14, 'PC Gamer', 'Computadora de escritorio. Caracteristicas: \n Placa de video potente \n RAM increible \n etc', 4, 'PENDING', 3, 700.0, 700.0, 1);

INSERT INTO items (id, title, description, auction, status, owner, base_price, current_price, currency)
VALUES (15, 'Silla de escritorio - Streamer', 'Silla DXRacer de cuero, soporte lumbar. Altamente ergonómica', 4, 'PENDING', 3, 199.0, 199.0, 1);

-- Insert photos linked to item
INSERT INTO image_urls (item_id, image_urls)
values (1, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624218387/initial/macbook-pro_1.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (1, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624218386/initial/macbook-pro_2.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (2, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488419/initial/zapatilla_4.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (3, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488419/initial/zapatilla_2.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (4, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488419/initial/zapatilla_3.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (5, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488419/initial/zapatilla_1.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (6, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488442/initial/pelota-futbol.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (7, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488481/initial/juego-te_2.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (7, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488481/initial/juego-te_1.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (8, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488534/initial/masajeador.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (9, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488500/initial/plancha-pelo.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (10, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488549/initial/celular_2.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (10, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488562/initial/celular_1.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (11, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488562/initial/switch_1.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (11, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488562/initial/switch_2.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (11, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488562/initial/switch_3.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (12, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488562/initial/ps5.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (12, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488562/initial/ps5_2.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (13, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488562/initial/xbox.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (13, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488562/initial/xbox_2.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (14, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488562/initial/pc.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (14, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488562/initial/pc_1.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (14, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488562/initial/pc_3.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (15, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488562/initial/silla_1.jpg');

INSERT INTO image_urls (item_id, image_urls)
values (15, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488562/initial/silla_2.jpg');


-- Insert auctions
-- Para agregar fechas futuras dinámicamente: DATEADD('MINUTE',30, CURRENT_TIMESTAMP())
INSERT INTO auction (id, title, category, status)
VALUES (1, 'Subasta tecno', 0, 'FINISHED');

INSERT INTO auction (id, title, category, status, start_time)
VALUES (2, 'Deportes', 2, 'PENDING', DATEADD('MINUTE',30, CURRENT_TIMESTAMP()));

INSERT INTO auction (id, title, category, status)
VALUES (3, 'Cosas para el hogar', 1, 'PENDING');

INSERT INTO auction (id, title, category, status, currency)
VALUES (4, 'Subasta en dolares', 0, 'PENDING', 1);


-- Insert payment method -> 0 es Credit card y 1 es CBU

INSERT INTO payment_method (id, type, name, owner, data, status)
VALUES (1, 0, 'Tarjeta de prueba 1', 1,'{"primeros_4":1234}', 'APPROVED');

INSERT INTO payment_method (id, type, name, owner, data, status)
VALUES (4, 1, 'CBU aun no validado', 1,'{"alias":"test.test.test"}', 'PENDING');

INSERT INTO payment_method (id, type, name, owner, data, status)
VALUES (8, 1, 'CBU validado en pesos', 1,'{"alias":"test.test.test"}', 'APPROVED');

INSERT INTO payment_method (id, type, name, owner, data, status, currency)
VALUES (5, 1, 'CBU validado en dolares', 1,'{"alias":"test.test.test"}', 'APPROVED', 1);

INSERT INTO payment_method (id, type, name, owner, data, status)
VALUES (2, 0, 'Tarjeta de prueba 2', 2,'{"primeros_4":1234}', 'APPROVED');

INSERT INTO payment_method (id, type, name, owner, data, status)
VALUES (6, 1, 'CBU de prueba válido 3', 2,'{"alias":"pedro.maria.juan"}', 'APPROVED');

INSERT INTO payment_method (id, type, name, owner, data, status)
VALUES (7, 0, 'Tarjeta de prueba 4', 2,'{"primeros_4":4321}', 'PENDING');

INSERT INTO payment_method (id, type, name, owner, data, status)
VALUES (3, 0, 'Tarjeta de prueba 1', 3, '{"primeros_4":1234}', 'APPROVED');

-- Users registrados en subastas

insert into users_auctions (users_id, auctions_id)
values (1,1);

insert into users_auctions (users_id, auctions_id)
values (2,1);

insert into users_auctions (users_id, auctions_id)
values (3,1);

insert into users_auctions (users_id, auctions_id)
values (1,2);

insert into users_auctions (users_id, auctions_id)
values (2,2);


-- Agregamos pujas por el primer item
insert into bids (id, bid, bid_increase, bidder_id, item_id, payment_id)
values (1, 17500.0, 2500, 1, 1, 1);

insert into bids (id, bid, bid_increase, bidder_id, item_id, payment_id)
values (2, 23000.0, 5500, 2, 1, 2);

insert into bids (id, bid, bid_increase, bidder_id, item_id, payment_id)
values (3, 25000.0, 2000, 1, 1, 1);