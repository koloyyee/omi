package co.loyyee.Omi.Drafter.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.loyyee.Omi.Drafter.service.PromptContent;
import co.loyyee.Omi.Drafter.service.file.Convert;
import co.loyyee.Omi.Drafter.service.file.DocxHandlerService;
import co.loyyee.Omi.Drafter.service.file.PdfHandlerService;
import co.loyyee.Omi.Drafter.service.springai.SpringOpenAiService;
import jakarta.validation.constraints.NotNull;

/**
 *
 *
 * <h3>DraftController</h3>
 *
 * <hr>
 *
 * <p>
 * It is responsible for bridging between client and OpenAI API not chat support
 * but a prompt and get response from ChatGPT. <br>
 *
 * <p>
 * The main is to draft a cover letter for the user with AI, based on the job
 * description and the user's resume. <br>
 * We will be handling both PDF and Docx file formats.
 *
 * <h2> PDF Handler </h2>
 * Handle PDF uploads
 * <p>
 * {@link DraftPublicController#uploadPdf(MultipartFile, String, String, String)}
 * }</p>
 * <p>
 * {@link PDFTextStripper#getText(PDDocument)} will be used to extract text from
 * PDF
 * <p>
 * {@link Loader#loadPDF(File)} will be used to load the PDF file
 *
 * <h2> Docx Handler </h2>
 * Handle Window Office Docx files
 * <p>
 * {@link DraftPublicController#uploadDocx(MultipartFile, String, String, String)}</p>
 */
@RestController
@RequestMapping("/drafter/public")
@CrossOrigin
public class DraftPublicController {

    private static final Logger log = LoggerFactory.getLogger(DraftPublicController.class);

    private final SpringOpenAiService service;

    public DraftPublicController(SpringOpenAiService service) {
        this.service = service;
    }

    @GetMapping
    public String publicTest() {
        return "public is HERE!";
    }

    @GetMapping(path = "/test")
    public String test(Principal principal) {
        return principal.getName();
    }

    /**
     * uploadPdf handles the PDF file uploaded from the client we will extract
     * with PDFBox and package it into a JSON object and send it to OpenAI API.
     * <br>
     * <h2>PDFBox 3.0 </h2>
     * reference: <a href="https://pdfbox.apache.org"> Apache PDF Box</a>
     * <br>
     * <strong>Note: </strong>Replace PDDocument.load with Loader.loadPDF
     * <br>
     * reference: https://pdfbox.apache.org/3.0/migration.html check -
     * <strong>Use Loader to get a PDF document</strong>
     *
     * @param mf MultipartFile from client, it will be in pdf format
     * @param company Company name of the user applying for the job
     * @param title Title of the job the user is applying for
     * @param description The job description
     */
    @PostMapping(value = "/upload/pdf", consumes = "multipart/form-data")
    public String uploadPdf(
            @NotNull @RequestParam("resume") MultipartFile mf,
            @NotNull @RequestParam("company") String company,
            @NotNull @RequestParam("title") String title,
            @NotNull @RequestParam("description") String description) {
        /**
         * Deal with the issue of showing previous input value
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl("no-cache, no-store, must-revalidate");
        /* Multipart File conversion because PDDocument only take  File. */
        Convert convert = new Convert();
        File file = convert.toFile(mf);

        try {
            PromptContent promptContent = new PromptContent();
            var userContent = promptContent.setUserContent(new PdfHandlerService(), file, company, title, description);

            String resp = this.service.setContent(userContent).ask();
            return resp;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            log.info("Deleting file: " + file.getName());
            file.delete();
        }
    }

    /**
     * uploadDocx
     * <p>
     * handles Window Docx files with Apache {@link XWPFDocument}</p>
     * reference: <a href="https://poi.apache.org">Apache Poi </a>
     *
     * @param mf MultipartFile from client, it will be in pdf format
     * @param company Company name of the user applying for the job
     * @param title Title of the job the user is applying for
     * @param description The job description
   *
     */
    @PostMapping("/upload/docx")
    public String uploadDocx(
            @NotNull @RequestParam("resume") MultipartFile mf,
            @NotNull @RequestParam("company") String company,
            @NotNull @RequestParam("title") String title,
            @NotNull @RequestParam("description") String description) {
        Convert convert = new Convert();
        File file = convert.toFile(mf);

        try {

            PromptContent promptContent = new PromptContent();
            var userContent = promptContent.setUserContent(new DocxHandlerService(), file, company, title, description);
            String resp = this.service.setContent(userContent).ask();
            return resp;
        } catch (IOException e) {
            log.error("Docx extraction: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
            // return ResponseEntity.internalServerError().build();
        } finally {
            log.info("Deleting file: " + file.getName());
            file.delete();
        }
    }

//  @PostMapping("/update")
    public ResponseEntity updateDraft(@NotNull @RequestParam("changes") String changes) {

        return ResponseEntity.ok(null);
    }

}
