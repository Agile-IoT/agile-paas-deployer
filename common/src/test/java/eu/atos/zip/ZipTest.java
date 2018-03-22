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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.testng.annotations.Test;

public class ZipTest {

    @Test
    public void unzip() throws IOException {

        Path from = new File(ZipTest.class.getResource("/samplezip.zip").getFile()).toPath();
        Path to = Files.createTempDirectory("pul");
        
        System.out.println("unzipping to " + to);
        Zip.unzip(from, to);
    }
}
