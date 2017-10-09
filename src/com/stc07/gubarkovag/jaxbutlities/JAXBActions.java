package com.stc07.gubarkovag.jaxbutlities;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JAXBActions {
    public static void jaxbMarshalling(Class<?> cls, String fileName, Object jaxbElement) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(cls);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Path path = Paths.get(fileName);
            Files.deleteIfExists(path);
            Files.createFile(path);

            marshaller.marshal(jaxbElement, path.toFile());
            marshaller.marshal(jaxbElement, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static Object jaxbUnmarshalling(Class<?> cls, String fileName) {
        Object unMarshalledObject = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(cls);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            Path path = Paths.get(fileName);
            if (!Files.exists(path)) {
                return null;
            }

            unMarshalledObject = unmarshaller.unmarshal(path.toFile());
            System.out.println(unMarshalledObject.toString());
        } catch (JAXBException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return unMarshalledObject;
    }
}
