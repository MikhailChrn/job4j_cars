CREATE TABLE participates (
   id serial PRIMARY KEY,
   post_id int not null REFERENCES auto_posts(id),
   user_id int not null REFERENCES auto_users(id),
   UNIQUE (post_id, user_id)
);