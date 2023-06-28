module org.oefa.gob.pe.osigner {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.lang3;
    requires org.apache.logging.log4j;
    requires java.naming;
    requires org.bouncycastle.provider;
    requires MaterialFX;

    opens org.oefa.gob.pe.osigner to javafx.fxml;
    exports org.oefa.gob.pe.osigner;
    opens org.oefa.gob.pe.osigner.infra.input.adapter to javafx.fxml;
    exports org.oefa.gob.pe.osigner.infra.input.adapter;
}