CREATE TABLE users_posts (
   id SERIAL PRIMARY KEY,
   user_id INT NOT NULL REFERENCES users(id),
   post_id INT NOT NULL REFERENCES posts(id),
   UNIQUE (post_id, user_id)
);