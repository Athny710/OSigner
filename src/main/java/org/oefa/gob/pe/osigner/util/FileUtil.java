package org.oefa.gob.pe.osigner.util;

import org.oefa.gob.pe.osigner.Configuration.AppConfiguration;
import org.oefa.gob.pe.osigner.commons.Constant;
import org.oefa.gob.pe.osigner.core.NotificationFX;
import org.oefa.gob.pe.osigner.domain.FileModel;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


public class FileUtil {


    private static final String OSIGNER_DIRECTORY = System.getProperty("user.home") + AppConfiguration.getKey("OSIGNER_FOLDER");
    private static final String TEMP_FOLDER = AppConfiguration.getKey("TEMP_FOLDER");
    private static final String SIGNED_FOLDER= AppConfiguration.getKey("SIGNED_FOLDER");


    public static String saveFileBytes(FileModel file) throws Exception{
        String path = getTempFolder();

        deleteFile(path + file.getName());

        FileOutputStream fos = new FileOutputStream(path + file.getName());
        fos.write(file.getBytes());
        fos.close();

        return path;

    }


    public static void loadFilesBytes(List<FileModel> filesList) throws IOException {
        for(FileModel fileToLoad : filesList){
            String path = fileToLoad.getLocation() + fileToLoad.getName();
            fileToLoad.setBytes(
                    Files.readAllBytes(Path.of(path))
            );
        }
    }


    /**
     * Función que permite descargar y conocer el progreso de descarga.
     * @param urlStr URL del archivo que se desea descargar.
     * @param fileName Nombre con el que se guardará el archivo.
     * @throws Exception Excepción al descargar el archivo.
     */
    public static void saveFileFromUrlWithProgress(String urlStr, String fileName) throws Exception{
        String path = getTempFolder() + fileName;
        URL url = new URL(urlStr);
        long fileSize = getSizeOfUrlFile(urlStr);

        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(path);

        byte[] buffer = new byte[4096];
        int count;
        long nread = 0L;
        while((count = bis.read(buffer)) != -1) {
            fis.write(buffer, 0, count);
            nread += count;

            double progress = (double) nread / fileSize;
            NotificationFX.updateProgressNotification(
                    Constant.PROGRESS_VALUE_DOWNLOAD.getInitialValue(),
                    Constant.PROGRESS_VALUE_DOWNLOAD.getPartialValue()  * progress
            );
        }
        fis.close();
        bis.close();

    }


    public static void saveFileFromUrl(String urlStr, String fileName) throws Exception {
        String path = getTempFolder() + fileName;
        URL url = new URL(urlStr);

        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(path);

        FileChannel fileChannel = fileOutputStream.getChannel();
        fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

    }

    /**
     * Función que permite comprimir archivos en un archivo .zip
     * @param folderPath Ruta del folder donde se encuentran los archivos
     * @param zipName Nombre del archivo .zip en el que se comprimirán los archivos.
     * @param filesToZip Lista de archivos que se desea comprimir.
     * @throws Exception Excepción al momento de comprimir los archivos.
     */
    public static void zipFiles(String folderPath, String zipName, List<FileModel> filesToZip) throws Exception{
        FileOutputStream fout = new FileOutputStream(folderPath + zipName);
        ZipOutputStream zipOut = new ZipOutputStream(fout);
        int index = 1;

        for(FileModel file : filesToZip){
            File f = new File(file.getLocation() + file.getName());
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
                NotificationFX.updateProgressNotification(
                        Constant.PROGRESS_VALUE_ZIP.getInitialValue(),
                        Constant.PROGRESS_VALUE_ZIP.getPartialValue() * progress
                );
            }
            fis.close();

        }
        zipOut.close();
        fout.close();
    }

    /**
     * Función que permite descomprimir un archivo tipo .zip
     * @param zipName Nombre del archivo .zip
     * @throws Exception Excepción al descomprimir el archivo
     */
    public static void unzipFiles(String zipName) throws Exception{
        File zipFile = new File(getTempFolder() + zipName);
        ZipFile zip = new ZipFile(zipFile);
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();

        long zipSize = zipFile.length();
        long nread = 0L;

        while(entries.hasMoreElements()){
            ZipEntry entry = entries.nextElement();
            InputStream is = zip.getInputStream(entry);
            OutputStream os = new FileOutputStream(getTempFolder() + entry.getName());

            byte[] buf = new byte[4096];
            int r;
            while((r = is.read(buf)) != -1){
                os.write(buf, 0, r);
                nread += r;
                double percent = (double) nread /zipSize;
                NotificationFX.updateProgressNotification(
                        Constant.PROGRESS_VALUE_UNZIP.getInitialValue(),
                        Constant.PROGRESS_VALUE_UNZIP.getPartialValue() * percent
                );
            }
            os.close();
            is.close();

        }
        zip.close();
        deleteFile(getTempFolder() + zipName);

    }

    public static void deleteFile(String fileToDeletePath) {
        (new File(fileToDeletePath)).delete();

    }

    private static long getSizeOfUrlFile(String urlStr) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept-Encoding", "identity");
            return conn.getContentLengthLong();
        } catch (IOException e) {
            return 0;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static String getSignedFolder(){
        return OSIGNER_DIRECTORY + SIGNED_FOLDER;
    }

    public static String getTempFolder(){
        return OSIGNER_DIRECTORY + TEMP_FOLDER;
    }

}
