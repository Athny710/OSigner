package org.oefa.gob.pe.osigner.commons;

import java.text.SimpleDateFormat;

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
}
