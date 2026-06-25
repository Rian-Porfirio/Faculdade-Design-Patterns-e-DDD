-- Plans (Subscription Context)
INSERT INTO plans (id, name, price_amount, price_currency, duration_days) VALUES
 (1, 'Free',    0.00,  'BRL', 30),
 (2, 'Premium', 19.90, 'BRL', 30),
 (3, 'Family',  34.90, 'BRL', 30);

-- Users (Identity Context)
INSERT INTO users (id, name, email) VALUES
 (1, 'Alice Johnson', 'alice@stream.io'),
 (2, 'Bruno Souza',   'bruno@stream.io');

-- Cards (Billing Context)
INSERT INTO cards (id, card_number, holder, expiration, cvv, status) VALUES
 (1, '4111111111111111', 'Alice Johnson', '12/2030', '123', 'ACTIVE'),
 (2, '5555444433332222', 'Bruno Souza',   '11/2029', '456', 'INACTIVE');

-- Songs (Music Context)
INSERT INTO songs (id, title, artist, album, duration_seconds) VALUES
 (1, 'Bohemian Rhapsody', 'Queen',        'A Night at the Opera', 354),
 (2, 'Imagine',           'John Lennon',  'Imagine',              183),
 (3, 'Smells Like Teen Spirit', 'Nirvana','Nevermind',           301),
 (4, 'Billie Jean',       'Michael Jackson', 'Thriller',         294),
 (5, 'Hotel California',   'Eagles',      'Hotel California',     390);

-- Favorites (Identity Context aggregate state)
INSERT INTO user_favorites (user_id, song_id) VALUES
 (1, 1),
 (1, 4);

-- Playlists (Music Context)
INSERT INTO playlists (id, user_id, name) VALUES
 (1, 1, 'Morning Vibes');

INSERT INTO playlist_songs (playlist_id, position, song_id) VALUES
 (1, 0, 2),
 (1, 1, 5);

-- Subscriptions (Subscription Context)
INSERT INTO subscriptions (id, user_id, plan_id, status, start_date, end_date) VALUES
 (1, 1, 2, 'ACTIVE', DATE '2025-01-01', DATE '2025-01-31');

-- Transactions (Billing Context)
INSERT INTO transactions (id, card_id, amount_value, amount_currency, merchant, date_time, status) VALUES
 (1, 1, 19.90, 'BRL', 'STREAMING-SUBSCRIPTION', TIMESTAMP '2025-01-01 10:00:00', 'AUTHORIZED');

-- Restart identity sequences so JPA-generated IDs do not collide with seeds
ALTER TABLE plans         ALTER COLUMN id RESTART WITH 100;
ALTER TABLE users         ALTER COLUMN id RESTART WITH 100;
ALTER TABLE cards         ALTER COLUMN id RESTART WITH 100;
ALTER TABLE songs         ALTER COLUMN id RESTART WITH 100;
ALTER TABLE playlists     ALTER COLUMN id RESTART WITH 100;
ALTER TABLE subscriptions ALTER COLUMN id RESTART WITH 100;
ALTER TABLE transactions  ALTER COLUMN id RESTART WITH 100;
