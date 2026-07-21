package com.LanguageDive.content.service;

import com.LanguageDive.common.exception.FileProcessingException;
import io.documentnode.epub4j.domain.Book;
import io.documentnode.epub4j.domain.Resource;
import io.documentnode.epub4j.domain.SpineReference;
import io.documentnode.epub4j.domain.TOCReference;
import io.documentnode.epub4j.epub.EpubReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.jsoup.Jsoup;

public class EpubParser {

    public record Sentence(int index, String text) {}
    public record Capitulo(String titulo, String contenido, int orden, List<Sentence> oraciones) {}
    public record Resultado(String titulo, String autor, String idioma, List<Capitulo> capitulos) {}

    public Resultado parsear(InputStream epubInputStream) {
        EpubReader reader = new EpubReader();
        Book book;

        try {
            book = reader.readEpub(epubInputStream);
        } catch (Exception e) {
            throw new FileProcessingException("No se pudo leer el archivo EPUB: " + e.getMessage(), e);
        }

        String tituloLibro = extraerTitulo(book);
        String autor = extraerAutor(book);
        String idioma = extraerIdioma(book);

        List<Capitulo> capitulos;
        try {
            capitulos = extraerCapitulos(book, idioma);
        } catch (Exception e) {
            throw new FileProcessingException("Error al extraer capítulos del EPUB: " + e.getMessage(), e);
        }

        return new Resultado(tituloLibro, autor, idioma, capitulos);
    }

    private String extraerTitulo(Book book) {
        var metadata = book.getMetadata();
        if (metadata.getTitles() != null && !metadata.getTitles().isEmpty()) {
            return metadata.getTitles().get(0);
        }
        return "Sin título";
    }

    private String extraerAutor(Book book) {
        var metadata = book.getMetadata();
        if (metadata.getAuthors() != null && !metadata.getAuthors().isEmpty()) {
            var author = metadata.getAuthors().get(0);
            String nombre = author.getFirstname();
            if (author.getLastname() != null) {
                nombre += " " + author.getLastname();
            }
            return nombre.strip();
        }
        return null;
    }

    private String extraerIdioma(Book book) {
        var metadata = book.getMetadata();
        String lang = metadata.getLanguage();
        return (lang != null && !lang.isBlank()) ? lang : null;
    }

    private List<Capitulo> extraerCapitulos(Book book, String idioma) throws Exception {
        List<TOCReference> toc = book.getTableOfContents().getTocReferences();
        if (toc != null && !toc.isEmpty()) {
            return capitulosDesdeToc(toc, idioma);
        }
        return capitulosDesdeSpine(book, idioma);
    }

    private List<Capitulo> capitulosDesdeToc(List<TOCReference> toc, String idioma) throws Exception {
        List<Capitulo> capitulos = new ArrayList<>();
        int orden = 0;

        for (TOCReference ref : toc) {
            List<TOCReference> hijos = ref.getChildren();
            if (hijos != null && !hijos.isEmpty()) {
                for (TOCReference hijo : hijos) {
                    procesarRef(hijo, capitulos, orden++, idioma);
                }
            } else {
                procesarRef(ref, capitulos, orden++, idioma);
            }
        }

        return capitulos;
    }

    private void procesarRef(TOCReference ref, List<Capitulo> capitulos, int orden, String idioma) throws Exception {
        String titulo = ref.getTitle();
        String contenido = extraerTexto(ref.getResource());
        if (contenido != null && !contenido.isBlank()) {
            List<Sentence> oraciones = dividirEnOraciones(contenido, idioma);
            capitulos.add(new Capitulo(titulo, contenido, orden, oraciones));
        }
    }

    private List<Capitulo> capitulosDesdeSpine(Book book, String idioma) throws Exception {
        List<SpineReference> spine = book.getSpine().getSpineReferences();
        List<Capitulo> capitulos = new ArrayList<>();

        for (int i = 0; i < spine.size(); i++) {
            Resource resource = spine.get(i).getResource();
            String contenido = extraerTexto(resource);
            if (contenido != null && !contenido.isBlank()) {
                List<Sentence> oraciones = dividirEnOraciones(contenido, idioma);
                String titulo = "Capítulo " + (i + 1);
                capitulos.add(new Capitulo(titulo, contenido, i, oraciones));
            }
        }

        return capitulos;
    }

    private String extraerTexto(Resource resource) throws Exception {
        if (resource == null) return null;
        byte[] data = resource.getData();
        if (data == null) return null;
        String html = new String(data, StandardCharsets.UTF_8);
        return Jsoup.parse(html).body().wholeText().strip();
    }

    static List<Sentence> dividirEnOraciones(String text, String idioma) {
        List<Sentence> oraciones = new ArrayList<>();
        if (text == null || text.isBlank()) return oraciones;

        Locale locale = (idioma != null) ? Locale.forLanguageTag(idioma) : Locale.ENGLISH;
        BreakIterator boundary = BreakIterator.getSentenceInstance(locale);
        boundary.setText(text);

        int start = boundary.first();
        int idx = 0;
        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
            String sentence = text.substring(start, end).strip();
            if (!sentence.isBlank()) {
                oraciones.add(new Sentence(idx++, sentence));
            }
        }

        return oraciones;
    }
}
