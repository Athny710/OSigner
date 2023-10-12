package org.oefa.gob.pe.osigner.util;

import org.apache.logging.log4j.core.util.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CertificateUtilTest {

    @Test
    void loadCertificates() throws Exception {
        // GIVEN

        // WHEN
        LogUtil.setInfo("Iniciando carga de certificados", "[TEST]");
        CertificateUtil.loadCertificates(true);
        LogUtil.setInfo("Finalizando carga de certificados", "[TEST]");

        // THEN
        assert !CertificateUtil.certificateList.isEmpty();
    }

    @Test
    void reloadCertificateList() {
    }

    @Test
    void getUserCertificateList() {
    }
}