/**
 *  Copyright (c) 2017 Atos, and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  Contributors:
 *  Atos - initial implementation
 */
package eu.atos.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Zip {

    private static FileSystem fileSystem = FileSystems.getDefault();
    
    /**
     * Size of the buffer to read/write data
     */
    private static final int BUFFER_SIZE = 4096;

    public static void unzip(Path sourceZip, Path targetDirectory) throws IOException {
    
        try(ZipFile src = new ZipFile(sourceZip.toFile())) {
            
            Enumeration<? extends ZipEntry> entries = src.entries();
            
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                
                Path target = fileSystem.getPath(targetDirectory.toString(), entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(target);
                }
                else {
                    extractFile(src, entry, target);
                }
                
            }
        }
    }
    
    private static void extractFile(ZipFile src, ZipEntry entry, Path targetPath) throws IOException {
        
        try (
            BufferedInputStream is = new BufferedInputStream(src.getInputStream(entry));
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(targetPath.toFile())) ) 
        {
            
            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read = 0;
            while ((read = is.read(bytesIn)) != -1) {
                os.write(bytesIn, 0, read);
            }
        }
    }
    
}
