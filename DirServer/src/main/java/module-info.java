module DirServer {
    requires io.netty.all;
    requires log4j;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.transaction;
    requires api;
    requires com.fasterxml.jackson.databind;
    requires lombok;
    requires com.sun.istack.runtime;
    requires java.naming;
}