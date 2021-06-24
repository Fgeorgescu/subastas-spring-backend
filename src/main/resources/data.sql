-- Insert users in DB
INSERT INTO users
    VALUES (1, 'test 123', 'CLASSIC', 123456789, 'mailclassic@test.com', 'pass', '15123451', 'user', 'active', 'testclassic', '123');
INSERT INTO users
    VALUES (2, 'test 123', 'GOLD', 223456789, 'mailgold@test.com', 'pass', '15123452', 'user', 'active', 'testgold', '123');
INSERT INTO users
    VALUES (3, 'test 123', 'SILVER', 323456789, 'mailsilver@test.com', 'pass', '15123453', 'user', 'active', 'testsilver', '123');

-- Insert Items
INSERT INTO items (id, title, description, auction, status, owner)
    VALUES (1, 'Macbook Pro 2018', 'Macbook pro 2018 con poco uso y mucho cuidado', 1, 'PROCESSING', 1);

INSERT INTO items (id, title, description, auction, status, owner)
    VALUES (2, 'Calzado montaña', 'Calzado robusto para hacer montañismo', 2, 'PROCESSING', 2);

INSERT INTO items (id, title, description, auction, status, owner)
    VALUES (3, 'Calzado deportivo Nike', 'Botines para deportes varios en superficie de pasto', 2, 'PROCESSING', 3);

INSERT INTO items (id, title, description, auction, status, owner)
    VALUES (4, 'Zapatilla Basquet', 'Calzado deportivo para deportes como básquet o voley', 2, 'PROCESSING', 1);

INSERT INTO items (id, title, description, auction, status, owner)
    VALUES (5, 'Altas llantas', 'Las llantas más altas del mercado', 2, 'PROCESSING', 1);

INSERT INTO items (id, title, description, auction, status, owner)
    VALUES (6, 'Pelota clásica', 'Pelota clásica de futbol', 2, 'PROCESSING', 2);

INSERT INTO items (id, title, description, auction, status, owner)
    VALUES (7, 'Juego de te', 'Colección de tetera y copas + platos para tomar el te', 3, 'PROCESSING', 1);

INSERT INTO items (id, title, description, auction, status, owner)
    VALUES (8, 'Masajeador', 'Masajeador de hombros y espalda. Muy versátil', 3, 'PROCESSING', 3);

INSERT INTO items (id, title, description, auction, status, owner)
    VALUES (9, 'Plancha de pelo', 'Electrodomestico para planchado de pelo', 3, 'PROCESSING', 1);

INSERT INTO items (id, title, description, auction, status, owner)
    VALUES (10, 'Celular random', 'Celular en perfecto estado. Caracteristidas: \n probamos un \\n', 3, 'PROCESSING', 2);

-- Insert photos linked to item
INSERT INTO image_urls (registered_item_id, image_urls)
    values (1, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624218387/initial/macbook-pro_1.jpg');
INSERT INTO image_urls (registered_item_id, image_urls)
    values (1, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624218386/initial/macbook-pro_2.jpg');

INSERT INTO image_urls (registered_item_id, image_urls)
    values (2, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488419/initial/zapatilla_4.jpg');

INSERT INTO image_urls (registered_item_id, image_urls)
values (3, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488419/initial/zapatilla_2.jpg');

INSERT INTO image_urls (registered_item_id, image_urls)
values (4, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488419/initial/zapatilla_3.jpg');

INSERT INTO image_urls (registered_item_id, image_urls)
values (5, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488419/initial/zapatilla_1.jpg');

INSERT INTO image_urls (registered_item_id, image_urls)
values (6, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488442/initial/pelota-futbol.jpg');

INSERT INTO image_urls (registered_item_id, image_urls)
values (7, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488481/initial/juego-te_2.jpg');

INSERT INTO image_urls (registered_item_id, image_urls)
values (7, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488481/initial/juego-te_1.jpg');

INSERT INTO image_urls (registered_item_id, image_urls)
values (8, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488534/initial/masajeador.jpg');

INSERT INTO image_urls (registered_item_id, image_urls)
values (9, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488500/initial/plancha-pelo.jpg');

INSERT INTO image_urls (registered_item_id, image_urls)
values (10, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488549/initial/celular_2.jpg');

INSERT INTO image_urls (registered_item_id, image_urls)
values (10, 'https://res.cloudinary.com/dr4i78wvu/image/upload/v1624488562/initial/celular_1.jpg');

-- Insert auctions
INSERT INTO auction (id, title, category, status)
    VALUES (1, 'Subasta tecno', 'CLASSIC', 'PENDING');

INSERT INTO auction (id, title, category, status)
VALUES (2, 'Deportes', 'GOLD', 'PENDING');


INSERT INTO auction (id, title, category, status)
VALUES (3, 'Cosas para el hogar', 'SILVER', 'PENDING');