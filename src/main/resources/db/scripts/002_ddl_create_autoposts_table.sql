create table auto_posts
(
    id               serial         primary key,
    description      varchar        not null,
    created          timestamp      not null,
    auto_user_id     int            not null
);