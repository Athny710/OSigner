package org.oefa.gob.pe.osigner.commons;

import org.oefa.gob.pe.osigner.domain.SignCoordinates;

import java.text.SimpleDateFormat;
import java.util.List;

public class Constant {
    public static final byte[] TSA_PASS_HASH_KEY = new byte[] {50, 45, 113, 43, 56, 98, 106, 68, 35, 69, 103, 57, 46, 82, 45, 121 };
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm dd-MM-yyyy");
    public static final String OS = System.getProperty("os.name", "generic").toLowerCase();
    public static final String KEY_STORE = "Windows-MY";
    public static final String PROVIDER = "SunMSCAPI";
    public static final int TSA_TOKEN_SIZE = 4*8192;
    public static final int SIGNATURE_SIZE = 2*8192;
    public static final String HASH = "SHA-256";
    public static final String HASH_FORMAT_2 = "sha256";
    public static final String DOC_EXTENSION = ".DOC";
    public static final String DOCX_EXTENSION = ".DOCX";
    public static final String PDF_EXTENSION = ".pdf";
    public static final String TEMP_FILE_NAME_EXTENSION = "[TMP].pdf";
    public static final int GLOSA_SI = 1;
    public static final int FIRMA_POSICION_AUTOMATICA = 1;
    public static final int FIRMA_POSICION_RELATIVA = 2;
    public static final int FIRMA_ESTILO_INVISIBLE = 0;
    public static final int FIRMA_ESTILO_TEXTO = 1;
    public static final int FIRMA_ESTILO_TEXTO_IMAGEN = 2;
    public static final int FIRMA_TIPO_FIRMA = 1;
    public static final int FIRMA_TIPO_VISADO = 2;


    public static final class UbicacionFirma{

        public static final List<SignCoordinates> relativePosition = List.of(
                new SignCoordinates(1, 10, 742),
                new SignCoordinates(2, 198, 742),
                new SignCoordinates(3, 383, 742),
                new SignCoordinates(4, 10, 401),
                new SignCoordinates(5, 198, 401),
                new SignCoordinates(6, 383, 401),
                new SignCoordinates(7, 10, 10),
                new SignCoordinates(8, 198, 10),
                new SignCoordinates(9, 383, 10)
        );

        public static final List<SignCoordinates> visadoVerticalPosition = List.of(
                new SignCoordinates(1, 10, 682),
                new SignCoordinates(2, 10, 542),
                new SignCoordinates(3, 10, 401),
                new SignCoordinates(4, 10, 261),
                new SignCoordinates(5, 10, 120),
                new SignCoordinates(6, 10, 10),
                new SignCoordinates(7, 520, 682),
                new SignCoordinates(8, 520, 542),
                new SignCoordinates(9, 520, 401),
                new SignCoordinates(10, 520, 261),
                new SignCoordinates(11, 520, 120),
                new SignCoordinates(12, 520, 10)
        );

        public static final List<SignCoordinates> visadoHorizontalPosition = List.of(
                new SignCoordinates(1, 10, 440),
                new SignCoordinates(2, 10, 300),
                new SignCoordinates(3, 10, 160),
                new SignCoordinates(4, 10, 10),
                new SignCoordinates(5, 765, 440),
                new SignCoordinates(6, 765, 300),
                new SignCoordinates(7, 765, 160),
                new SignCoordinates(8, 765, 10)
        );

    }
}
