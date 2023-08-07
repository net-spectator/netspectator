CREATE TABLE equipment_type (
	id bigserial NOT NULL,
	title varchar(255) NULL,
	CONSTRAINT equipment_type_pkey PRIMARY KEY (id)
);

CREATE TABLE sensors (
	id bigserial NOT NULL,
	title varchar(255) NULL,
	CONSTRAINT sensors_pkey PRIMARY KEY (id)
);

CREATE TABLE tracked_equipment (
	id bigserial NOT NULL,
	black_list int4 NULL DEFAULT 0,
	created_at timestamp NULL,
	ip varchar(255) NULL,
	mac_address varchar(255) NULL,
	online_status int4 NULL,
	title varchar(255) NULL,
	uuid varchar(255) NULL,
	server_log int4 NULL DEFAULT 0,
	updated_at timestamp NULL,
	"type" int8 NULL,
	CONSTRAINT tracked_equipment_pkey PRIMARY KEY (id),
	CONSTRAINT fk8lqro336h03wmqfnx5vp00656 FOREIGN KEY ("type") REFERENCES equipment_type(id)
);

CREATE TABLE tracked_equipment_sensors (
	id bigserial NOT NULL,
	sensor_id int8 NULL,
	tracked_equipment_id int8 NULL,
	CONSTRAINT tracked_equipment_sensors_pkey PRIMARY KEY (id),
	CONSTRAINT fkgg7eaqrivv4h9m36fkk6vg4b9 FOREIGN KEY (tracked_equipment_id) REFERENCES tracked_equipment(id),
	CONSTRAINT fktoi4veyl0l851pa99oh6h3l91 FOREIGN KEY (sensor_id) REFERENCES sensors(id)
);

INSERT INTO sensors (title) VALUES('CpuInfoCollector');
INSERT INTO sensors (title) VALUES('DriveInfoCollector');
INSERT INTO sensors (title) VALUES('RamInfoCollector');
