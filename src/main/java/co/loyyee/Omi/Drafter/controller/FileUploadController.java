package co.loyyee.Omi.Drafter.controller;

import co.loyyee.Omi.Drafter.service.DrafterService;
import co.loyyee.Omi.config.DrafterConfigurationProperties;
import jakarta.validation.constraints.NotNull;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <h3>FileUploadController</h3>
 * <hr>
 *<p>It is responsible for bridging between client and OpenAI API
 * not chat support but a prompt and get response from ChatGPT.
 *</p>
 * <br>
 * <p>The main is to draft a cover letter for the user with AI,
 * based on the job description and the user's resume.
 *</p>
 *
 * <br>
 * We will be handling both PDF and Docx file formats.
 * <p>{@link PDFTextStripper#getText(PDDocument)} will be used to extract text from PDF</p>
 *<p>{@link Loader#loadPDF(File)} will be used to load the PDF file</p>
 * */
@RestController
@RequestMapping("/api/drafter")
@CrossOrigin(origins = "http://localhost:5173")
public class FileUploadController {
	final private static Logger log = LoggerFactory.getLogger(FileUploadController.class);
	final private DrafterService service;
	final private DrafterConfigurationProperties config;

	public FileUploadController(DrafterService service, DrafterConfigurationProperties config) {
		this.service = service;
		this.config = config;
	}

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
	@PostMapping(value = "/upload/pdf", consumes = "multipart/form-data")
	public ResponseEntity uploadPdf(@NotNull @RequestParam("resume") MultipartFile mf,
									@NotNull @RequestParam("company") String company,
									@NotNull	@RequestParam("title") String title,
									@NotNull @RequestParam("description") String description) {

		/* Multipart File conversion because PDDocument only take  File. */
		File file = null;
		try {
			file = new File(mf.getOriginalFilename());
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(mf.getBytes());
			fos.close();
		} catch (IOException e) {
			log.error("File conversion: " + e.getMessage());
		}
		/*
		* PDFBox 3.0 has replaced PDDocument.load with Loader.loadPDF
		 * reference: https://pdfbox.apache.org/3.0/migration.html
		 * check - <strong>Use Loader to get a PDF document</strong>
		*
		* */
		try (PDDocument document = Loader.loadPDF(file)) {
			/* reference: https://mkyong.com/java/pdfbox-how-to-read-pdf-file-in-java/ */
			PDFTextStripperByArea stripperByArea = new PDFTextStripperByArea();
			stripperByArea.setSortByPosition(true);
			PDFTextStripper stripper = new PDFTextStripper();
			/* extract all text from the PDF */
			String resume= stripper.getText(document);

			StringBuilder userContent = new StringBuilder();
			userContent.append("Here is the company: ")
				.append(company)
				.append( "The job title: ")
				.append(title + "\n")
				.append("The job description: ")
				.append(description  + "\n" )
				.append("Here is my resume: \n")
				.append(resume);
			log.info("Submitting ✈️: " + userContent.toString());
			String resp = this.service
				.setContent(userContent.toString())
				.ask();
			log.info("receiving 📦: " + resp);

			return new ResponseEntity( resp, HttpStatus.OK);
		} catch (IOException e) {
			log.error("PDFBox extraction: " + e.getMessage());
			return new ResponseEntity( HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			log.info("Deleting file: " + file.getName());
			file.delete();
		 }
	}
}
