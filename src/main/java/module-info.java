module org.oefa.gob.pe.osigner {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.lang3;
    requires org.apache.logging.log4j;
    requires java.naming;
    requires org.bouncycastle.provider;
    requires MaterialFX;
    requires spring.web;
    requires com.google.gson;
    requires sign;
    requires kernel;
    requires io;

    opens org.oefa.gob.pe.osigner to javafx.fxml;
    exports org.oefa.gob.pe.osigner;

    opens org.oefa.gob.pe.osigner.infra.input.adapter to javafx.fxml;
    exports org.oefa.gob.pe.osigner.infra.input.adapter;

    opens org.oefa.gob.pe.osigner.domain.ws to com.google.gson;
    opens org.oefa.gob.pe.osigner.domain to com.google.gson;
}