package com.maltem.aiboo;;

import com.maltem.aiboo.utilities.TikaUtility;
import org.apache.tika.exception.TikaException;
        import org.apache.tika.metadata.Metadata;
        import org.junit.jupiter.api.Test;
        import org.xml.sax.SAXException;
        import static org.hamcrest.CoreMatchers.containsString;

        import java.io.IOException;
        import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TikaUtilityTest {

    @Test
    public void whenUsingDetector_thenDocumentTypeIsReturned()
            throws IOException {
        InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream("202210_Appel_Offre_Architecte_technique_ (1).pdf");
        String mediaType = TikaUtility.detectDocTypeUsingDetector(stream);
        assertEquals("application/pdf", mediaType);

        stream.close();
    }

    @Test
    public void whenUsingParser_thenPdfContentIsReturned()
            throws IOException, TikaException, SAXException {
        InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream("202210_Appel_Offre_Architecte_technique_ (1).pdf");
        String content = TikaUtility.extractContentUsingFacade(stream);
        System.out.println(content);

        assertThat(content,
                containsString("Appétence pour la technique"));
        assertThat(content,
                containsString("Rigueur"));

        stream.close();
    }

    @Test
    public void whenUsingParser_thenPdfContentWithTableIsReturned()
            throws IOException, TikaException, SAXException {
        InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream("DEP_PSB_PSB_GCR_221118-01.pdf");
        String content = TikaUtility.extractContentUsingFacade(stream);
        System.out.println(content);

        assertThat(content,
                containsString("Concepteur Développeur"));
        assertThat(content,
                containsString("Junior (0 à 3 ans d’XP)"));

        stream.close();
    }

    @Test
    public void whenUsingParser_thenDocxContentIsReturned()
            throws IOException, TikaException, SAXException {
        InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream("Fiche Mission_ SNCF_dev Back.docx");
        String content = TikaUtility.extractContentUsingFacade(stream);
        System.out.println(content);

        assertThat(content,
                containsString("Au sein de la Direction générale"));
        assertThat(content,
                containsString("la DGEX Solutions héberge une vingtaine de projets et activités"));

        stream.close();
    }

    @Test
    public void whenUsingParser_thenXlsxContentIsReturned()
            throws IOException, TikaException, SAXException {
        InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream("Enquête SATISFACTION FORMATION .xlsx");
        String content = TikaUtility.extractContentUsingFacade(stream);
        System.out.println(content);

        assertThat(content,
                containsString("Les supports pédagogiques"));
        assertThat(content,
                containsString("Evaluez la qualité"));

        stream.close();
    }

    @Test
    public void whenUsingParser_thenMetadataIsReturned()
            throws IOException, TikaException, SAXException {
        InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream("tika.xlsx");
        Metadata metadata = TikaUtility.extractMetadatatUsingFacade(stream);
        /*String[] metadaNames = metadata.names();
        Arrays.asList(metadaNames).forEach(metadataName -> {
            String metadaNameCopie= metadataName;
            System.out.println(metadataName+": "+metadata.get(metadaNameCopie));
        });*/


        assertEquals("org.apache.tika.parser.DefaultParser",
                metadata.get("X-Parsed-By"));
        assertEquals("UTF-8", metadata.get("Content-Encoding"));

        stream.close();
    }

    @Test
    public void whenUsingParser_thenXlsxContentLanguageIsReturned()
            throws IOException, TikaException, SAXException {
        InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream("Enquête SATISFACTION FORMATION .xlsx");
        String language = TikaUtility.extractContentLanguage(stream);
        System.out.println(language);

        assertEquals("fr", language);
        stream.close();
    }


}
