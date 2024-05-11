insert into products (id, user_id, account, balance, product_type)
values
(1, 123, '1234567887654321', 12500, 1),
(2, 123, '9876543211234567', 500, 2),
(3, 125, '1233211233211235', 55000, 1),
(4, 125, '9876556789123431', 10, 2)
on conflict do nothing;
