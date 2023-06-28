module api {
    requires log4j;
    requires com.fasterxml.jackson.core;
    requires lombok;
    requires com.fasterxml.jackson.databind;
    requires java.persistence;
    requires json.smart;
    requires org.hibernate.orm.core;
    exports utils.converter;
    exports utils;
    exports entities.devices;
    exports entities.devices.ram;
    exports entities.devices.cpus;
    exports entities.devices.drives;
}