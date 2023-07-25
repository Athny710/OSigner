package org.oefa.gob.pe.osigner.util;

import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.domain.FileModel;

import java.io.*;
import java.net.URI;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import com.aspose.words.*;
import org.oefa.gob.pe.osigner.domain.ws.wssfd.ArchivoFirmaMasiva;

public class FileUtil {

    private static final String OSIGNER_DIRECTORY = System.getProperty("user.home") + AppConfiguration.getKey("OSIGNER_FOLDER");
    private static final String TO_SIGN_FOLDER = AppConfiguration.getKey("POR_FIRMAR_FOLDER");

    public static String saveFileBytes(FileModel file) throws Exception{
        LogUtil.setInfo("Guardando el archivo: " + file.getName(), FileUtil.class.getName());
        String path = OSIGNER_DIRECTORY + TO_SIGN_FOLDER + file.getName();

        FileOutputStream fos = new FileOutputStream(path);
        fos.write(file.getBytes());
        fos.close();

        return path;

    }

    public static String saveFileFromUrl(String url, String zipName) throws Exception{
        ReadableByteChannel readableByteChannel = Channels.newChannel(URI.create(url).toURL().openStream());
        String path = OSIGNER_DIRECTORY + TO_SIGN_FOLDER + zipName;

        FileOutputStream fileOutputStream = new FileOutputStream(path);
        FileChannel fileChannel = fileOutputStream.getChannel();
        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

        return path;

    }

    public static String unzipFiles(String zipPath) throws Exception{
        ZipFile zip = new ZipFile(new File(zipPath));
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();

        while(entries.hasMoreElements()){
            ZipEntry entry = entries.nextElement();
            InputStream is = zip.getInputStream(entry);
            OutputStream os = new FileOutputStream(OSIGNER_DIRECTORY + TO_SIGN_FOLDER + entry.getName());

            byte[] buf = new byte[4096];
            int r;
            while((r = is.read(buf)) != -1){
                os.write(buf, 0, r);
            }
            os.close();
            is.close();
        }

        return OSIGNER_DIRECTORY + TO_SIGN_FOLDER;

    }


}
