package co.loyyee.Omi.Drafter.service;

import java.io.File;
import java.io.IOException;


public class PromptContent{
	
	public static String setUserContent(FileHandler handler, File resumeFile, String company, String title, String description) throws IOException {
		String resume = handler.extract(resumeFile);
		StringBuilder userContent = new StringBuilder();
		userContent
				.append("Here is the company: ")
				.append(company)
				.append("The job title: ")
				.append(title + "\n")
				.append("The job description: ")
				.append(description + "\n")
				.append("Here is my resume: \n")
				.append(resume);
		return userContent.toString();
	}
}
