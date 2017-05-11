/**
 * Copyright 2017 Atos
 * Contact: Atos <roman.sosa@atos.net>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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
