-- Insert users in DB
INSERT INTO users
    VALUES (1, 'test 123', 'CLASSIC', 123456789, 'mailclassic@test.com', 'pass', '15123451', 'user', 'active', 'testclassic', '123');
INSERT INTO users
    VALUES (2, 'test 123', 'GOLD', 223456789, 'mailgold@test.com', 'pass', '15123452', 'user', 'active', 'testgold', '123');
INSERT INTO users
    VALUES (3, 'test 123', 'SILVER', 323456789, 'mailsilver@test.com', 'pass', '15123453', 'user', 'active', 'testsilver', '123');

-- Insert Items
INSERT INTO items (id, title, description, auction)
    VALUES (1, 'Macbook Pro 2018', 'Macbook pro 2018 con poco uso y mucho cuidado', 1);

-- Insert photos linked to item
INSERT INTO image_urls (registered_item_id, image_urls)
values (1, 'link1');
INSERT INTO image_urls (registered_item_id, image_urls)
values (1, 'link2');

-- Insert auctions
INSERT INTO auction
    VALUES (1, 'Subasta tecno', 'CLASSIC', 'PENDING');
