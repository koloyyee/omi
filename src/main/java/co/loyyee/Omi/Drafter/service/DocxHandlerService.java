package co.loyyee.Omi.Drafter.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class DocxHandlerService implements FileHandler {
	@Override
	public String extract(File file) throws IOException {
		
		XWPFDocument document = new XWPFDocument(new FileInputStream(file));
		XWPFWordExtractor extractor = new XWPFWordExtractor(document);
		return extractor.getText();
	}
}
