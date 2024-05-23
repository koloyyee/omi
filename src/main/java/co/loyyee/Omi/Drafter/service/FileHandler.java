package co.loyyee.Omi.Drafter.service;

import jakarta.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;

/**
 * This is implementing Dependency Inversion with FileHandler <br>
 * it will be implemented by PdfHandlerService and DocxHandler <br>
 * PromptContent will be the intermediate service to convert the file to resume <br>
 * and combining other information to create a UserContent for prompting
 *
 * */
public interface FileHandler {
	String extract(@NotNull File file) throws IOException;
}
