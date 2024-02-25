package co.loyyee.Omi.Drafter.controller;

import jakarta.validation.constraints.NotNull;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * FileUploadController
 * It is responsible for bridging between client and OpenAI API
 * not chat support but a prompt and get response from ChatGPT.
 *
 * The main is to draft a cover letter for the user with AI,
 * based on the job description and the user's resume.
 *
 * We will be handling both PDF and Docx file formats.
 * */
@RestController
@RequestMapping("/api/drafter")
public class FileUploadController {
	final private static Logger log = LoggerFactory.getLogger(FileUploadController.class);

	/**
	 * uploadPdf handles the PDF file uploaded from the client
	 * we will extract with PDFBox and package it into a JSON object
	 * and send it to OpenAI API.
	 *
	 * @param mf MultipartFile from client, it will be in pdf format
	 * @param company Company name of the user applying for the job
	 * @param title Title of the job the user is applying for
	 * @param description The job description
	 * */
	@PostMapping("/upload/pdf")
	public void uploadPdf(@NotNull @RequestParam("file") MultipartFile mf,
						   @RequestParam("company") String company,
						   @RequestParam("title") String title,
						   @RequestParam("description") String description) {

		/* Multipart File conversion because PDDocument only take  File. */
		File file = null;
		try {
			file = new File(mf.getOriginalFilename());
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(mf.getBytes());
			fos.close();
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		try (PDDocument document = Loader.loadPDF(file)) {

			PDFTextStripperByArea stripperByArea = new PDFTextStripperByArea();
			stripperByArea.setSortByPosition(true);
			PDFTextStripper stripper = new PDFTextStripper();
			/* extract all text from the PDF */
			String resume= stripper.getText(document);
			log.info("company: " + company);
			log.info("title: " + title);
			log.info("description: " + description);
			log.info("resume: " + resume);
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			file.delete();
		 }

	}
}
