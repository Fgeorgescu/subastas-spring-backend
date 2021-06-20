-- Insert users in DB

insert into users
    (id, address, category, document, mail, password, phone, role, status, username, validation_code)
    VALUES (1, 'test 123', 'CLASSIC', 123456789, 'mailclassic@test.com', 'pass', '15123451', 'user', 'active', 'testclassic', '123');
insert into users
    (id, address, category, document, mail, password, phone, role, status, username, validation_code)
    VALUES (2, 'test 123', 'GOLD', 223456789, 'mailgold@test.com', 'pass', '15123452', 'user', 'active', 'testgold', '123');
insert into users
    (id, address, category, document, mail, password, phone, role, status, username, validation_code)
    VALUES (3, 'test 123', 'SILVER', 323456789, 'mailsilver@test.com', 'pass', '15123453', 'user', 'active', 'testsilver', '123');

-- Insert Items


-- Insert auctions