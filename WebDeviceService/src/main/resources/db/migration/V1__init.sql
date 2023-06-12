create table device_group
(
    id         bigserial primary key,
    title      varchar(255),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

insert into device_group(title)

values ('main');

create table tracked_equipment
(
    id            bigserial primary key,
    UUID          varchar(255),
    title         varchar(255),
    ip            varchar(255),
    online_status numeric(1),
    mac_address   varchar(255),
    black_list    numeric(1),
    group_id      bigint references device_group (id),
    created_at    timestamp default current_timestamp,
    updated_at    timestamp default current_timestamp
);

CREATE TABLE sensors
(
    id    bigserial primary key,
    title varchar(255)
);

CREATE TABLE routers
(
    id          bigserial primary key,
    ip_address  varchar(255),
    mac_address varchar(255),
    title       varchar(255)
);

CREATE TABLE tracked_equipment_sensors
(
    id                   bigserial primary key,
    sensor_id            bigint references sensors (id),
    tracked_equipment_id bigint references tracked_equipment (id)
);