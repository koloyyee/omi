package co.loyyee.Omi.Drafter.service;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PdfHandlerService implements FileHandler{
	private static final Logger log = LoggerFactory.getLogger(PdfHandlerService.class);
	
	@Override
	public String extract(File file) {
		try (PDDocument document = Loader.loadPDF(file)) {
			/* reference: https://mkyong.com/java/pdfbox-how-to-read-pdf-file-in-java/ */
			PDFTextStripperByArea stripperByArea = new PDFTextStripperByArea();
			stripperByArea.setSortByPosition(true);
			PDFTextStripper stripper = new PDFTextStripper();
			/* extract all text from the PDF */
			return stripper.getText(document);
    } catch (IOException e) {
      log.error("PDFBox extraction: " + e.getMessage());
			return "PDFBox extraction: " + e.getMessage();
		}
	}
}
