package co.loyyee.Omi.Drafter.controller;

import co.loyyee.Omi.Drafter.service.TKOpenAiDrafterService;
import jakarta.validation.constraints.NotNull;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * <h3>FileUploadController</h3>
 * <hr>
 * <p>It is responsible for bridging between client and OpenAI API
 * not chat support but a prompt and get response from ChatGPT.
 * </p>
 * <br>
 * <p>The main is to draft a cover letter for the user with AI,
 * based on the job description and the user's resume.
 * </p>
 *
 * <br>
 * We will be handling both PDF and Docx file formats.
 * <p>{@link PDFTextStripper#getText(PDDocument)} will be used to extract text from PDF</p>
 * <p>{@link Loader#loadPDF(File)} will be used to load the PDF file</p>
 * <p>
 * <h3Workflow:</h3>
 * <p>
 * User first upload file processed by either
 * {@link #preloadPdf(MultipartFile)} or {@link #preloadDocx(MultipartFile)}.
 * </p>
 * <p>
 * Then after user submit the company,
 * job title, and job description
 * it will be processed by
 * {@link #generate(String, String, String)}
 * </p>
 */
@RestController
@RequestMapping("/drafter")
@CrossOrigin
public class FileUploadController {
    final private static Logger log = LoggerFactory.getLogger(FileUploadController.class);

    final private TKOpenAiDrafterService drafterService;

    public FileUploadController(TKOpenAiDrafterService drafterService) {
        this.drafterService = drafterService;
    }

    /**
     * @param mf Spring Boot file type MultipartFile from client post request
     *
     *           <p> It will convert file from MultipartFile to File parsing into Text</p>
     */
    private File convertToFile(@NotNull MultipartFile mf) {

        File file = null;
        try {
            file = new File(Objects.requireNonNull(mf.getOriginalFilename()));
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(mf.getBytes());
            fos.close();
        } catch (IOException e) {
            log.error("File conversion Error: " + e.getMessage());
        }
        return file;
    }

    /**
     * Preload PDF is a method preload the file before submitting
     * the job title, job description, and company.
     * <p>
     * Ideally this will speed up the generating time,
     * and reduce the chance of exceeding
     * the tokens quota in a
     * short period of time.
     * <p>
     * The uploaded pdf will be process first,
     * then after user input other information
     * they invoke "/generate"
     *
     * @param mf Multipart file - file uploaded from the frontend
     * @see #generate(String, String, String)
     **/
    @PostMapping(value = "/preload/pdf", consumes = "multipart/form-data")
    public void preloadPdf(@NotNull @RequestParam("resume") MultipartFile mf) {

        File file = convertToFile(mf);
        try (PDDocument document = Loader.loadPDF(file)) {
            /* reference: https://mkyong.com/java/pdfbox-how-to-read-pdf-file-in-java/ */
            PDFTextStripperByArea stripperByArea = new PDFTextStripperByArea();
            stripperByArea.setSortByPosition(true);
            PDFTextStripper stripper = new PDFTextStripper();
            /* extract all text from the PDF */
            String resume = stripper.getText(document);

            StringBuilder sb = new StringBuilder();
            var preloadPdf = sb.append("Record the following resume content.\n")
                    .append("I'll follow up with job title, company, and job description.\n")
                    .append("DO NOT output anything yet, wait for my command.\n")
                    .append(resume);
            this.drafterService.preload(preloadPdf.toString());

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            file.delete();
        }

    }

    /**
     * Preload Docx is a method preload the file before submitting
     * the job title, job description, and company.
     * <p>
     * Ideally this will speed up the generating time,
     * and reduce the chance of exceeding
     * the tokens quota in a
     * short period of time.
     * <p>
     * The uploaded docx will be process first
     * then after user input other information
     * they invoke "/generate"
     *
     * @param mf Multipart file - file uploaded from the frontend
     * @see #generate(String, String, String)
     **/
    @PostMapping(value = "/preload/docx", consumes = "multipart/form-data")
    public void preloadDocx(@NotNull @RequestParam("resume") MultipartFile mf) {

        File file = convertToFile(mf);
        try {
            XWPFDocument document = new XWPFDocument(new FileInputStream(file));
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            var resume = extractor.getText();

            String resp = this.drafterService.preload(resume);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            file.delete();
        }
    }

    /**
     * After preloading files to OpenAI
     * user will upload job title, company, and job description
     * then send it to OpenAI API.
     *
     * @param company     Company name of the user applying for the job
     * @param title       Title of the job the user is applying for
     * @param description The job description
     */
    @PostMapping(value = "/generate", consumes = "multipart/form-data")
    public ResponseEntity generate(
            @NotNull @RequestParam("company") String company,
            @NotNull @RequestParam("title") String title,
            @NotNull @RequestParam("description") String description) {

        StringBuilder userContent = new StringBuilder();
        userContent.append("Here is the company: ")
                .append(company)
                .append("The job title: ")
                .append(title + "\n")
                .append("The job description: ")
                .append(description + "\n");

        String resp = this.drafterService
                .setContent(userContent.toString())
                .ask();
        if (resp.contains("https://platform.openai.com/account/api-keys")) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(resp);
        }
    }

    /**
     * uploadPdf handles the PDF file uploaded from the client
     * we will extract with PDFBox and package it into a JSON object
     * and send it to OpenAI API.
     *
     * @param mf          MultipartFile from client, it will be in pdf format
     * @param company     Company name of the user applying for the job
     * @param title       Title of the job the user is applying for
     * @param description The job description
     */
    @PostMapping(value = "/upload/pdf", consumes = "multipart/form-data")
    public ResponseEntity uploadPdf(@NotNull @RequestParam("resume") MultipartFile mf,
                                    @NotNull @RequestParam("company") String company,
                                    @NotNull @RequestParam("title") String title,
                                    @NotNull @RequestParam("description") String description) {

        /* Multipart File conversion because PDDocument only take  File. */
        File file = convertToFile(mf);
        /**
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
            String resume = stripper.getText(document);

            StringBuilder sb = new StringBuilder();

            StringBuilder userContent = new StringBuilder();
            userContent.append("Here is the company: ")
                    .append(company)
                    .append("The job title: ")
                    .append(title + "\n")
                    .append("The job description: ")
                    .append(description + "\n")
                    .append("Here is my resume: \n")
                    .append(resume);

            String resp = this.drafterService
                    .setContent(userContent.toString())
                    .ask();
            if (resp.contains("https://platform.openai.com/account/api-keys")) {
                return ResponseEntity.badRequest().build();
            } else {
                return ResponseEntity.ok(resp);
            }
        } catch (IOException e) {
            log.error("PDFBox extraction: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        } finally {
            log.info("Deleting file: " + file.getName());
            file.delete();

        }
    }

    @PostMapping(value = "/upload/docx", consumes = "multipart/form-data")
    public ResponseEntity uploadDocx(
            @NotNull @RequestParam("resume") MultipartFile mf,
            @NotNull @RequestParam("company") String company,
            @NotNull @RequestParam("title") String title,
            @NotNull @RequestParam("description") String description
    ) {
        File file = convertToFile(mf);
        try {
            XWPFDocument document = new XWPFDocument(new FileInputStream(file));
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            var resume = extractor.getText();

            StringBuilder userContent = new StringBuilder();
            userContent.append("Here is the company: ")
                    .append(company)
                    .append("The job title: ")
                    .append(title + "\n")
                    .append("The job description: ")
                    .append(description + "\n")
                    .append("Here is my resume: \n")
                    .append(resume);

            String resp = this.drafterService
                    .setContent(userContent.toString())
                    .ask();
            if (resp.contains("https://platform.openai.com/account/api-keys")) {
                return ResponseEntity.badRequest().build();
            } else {
                return ResponseEntity.ok(resp);
            }
        } catch (IOException e) {
            log.error("PDFBox extraction: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        } finally {
            log.info("Deleting file: " + file.getName());
            file.delete();
        }
    }


}
