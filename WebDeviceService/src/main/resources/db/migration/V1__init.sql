create table device_group
(
    id         bigserial primary key,
    title      varchar(255),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

insert into device_group(title)

values ('main');

create table device
(
    id           bigserial primary key,
    UUID         varchar(255),
    title        varchar(255),
    ip           varchar(255),
    description  varchar(1000),
    hdd_free_space numeric(8, 2),
    online_status numeric(1),
    group_id     bigint references device_group (id),
    created_at   timestamp default current_timestamp,
    updated_at   timestamp default current_timestamp
);

