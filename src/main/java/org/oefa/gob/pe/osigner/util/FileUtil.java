package org.oefa.gob.pe.osigner.util;

import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.core.component.ProgressComponent;
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
import java.util.zip.ZipOutputStream;

import com.aspose.words.*;
import org.oefa.gob.pe.osigner.domain.ws.wssfd.ArchivoFirmaMasiva;

public class FileUtil {

    private static final String OSIGNER_DIRECTORY = System.getProperty("user.home") + AppConfiguration.getKey("OSIGNER_FOLDER");
    private static final String TO_SIGN_FOLDER = AppConfiguration.getKey("POR_FIRMAR_FOLDER");
    private static final String SIGNED_FOLDER= AppConfiguration.getKey("POR_FIRMAR_FOLDER");

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

    public static void zipFiles(String zipName, List<FileModel> filesToZip) throws Exception{
        FileOutputStream fout = new FileOutputStream(OSIGNER_DIRECTORY + SIGNED_FOLDER + zipName);
        ZipOutputStream zipOut = new ZipOutputStream(fout);
        int index = 1;

        for(FileModel file : filesToZip){
            File f = new File(file.getLocation());
            FileInputStream fis = new FileInputStream(f);
            ZipEntry zipEntry = new ZipEntry(file.getName());

            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[4096];
            int r;
            long nread = 0L;
            while((r = fis.read(bytes)) != -1){
                zipOut.write(bytes, 0, r);
                nread += r;
                double progress = ((double) nread / f.length()) * ((double) index /filesToZip.size());
                ProgressComponent.updateProgress(0.4 + 0.4 * progress);
            }
            fis.close();

        }
        zipOut.close();
        fout.close();
    }

    public static String unzipFiles(String zipPath) throws Exception{
        File zipFile = new File(zipPath);
        ZipFile zip = new ZipFile(zipFile);
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();

        long zipSize = zipFile.length();
        long nread = 0L;

        while(entries.hasMoreElements()){
            ZipEntry entry = entries.nextElement();
            InputStream is = zip.getInputStream(entry);
            OutputStream os = new FileOutputStream(OSIGNER_DIRECTORY + TO_SIGN_FOLDER + entry.getName());

            byte[] buf = new byte[4096];
            int r;
            while((r = is.read(buf)) != -1){
                os.write(buf, 0, r);
                nread += r;
                double progress = (double) nread /zipSize;
                ProgressComponent.updateProgress(0.20 + 0.5 * progress);
            }
            os.close();
            is.close();
        }

        deleteFile(zipPath);

        return OSIGNER_DIRECTORY + TO_SIGN_FOLDER;

    }

    public static void deleteFile(String fileToDeletePath) {
        (new File(fileToDeletePath)).delete();

    }

}
