package co.loyyee.Omi.Drafter.service;

import co.loyyee.Omi.Drafter.service.file.FileHandler;

import java.io.File;
import java.io.IOException;

/**
 * Applying Dependency Inversion Principle
 * */
public class PromptContent{
	private FileHandler handler;
	private File resumeFile;
	private String company;
	private String title;
	private String description;
	
	public PromptContent(){}
	private PromptContent(FileHandler handler, File resumeFile, String company, String title, String description) {
		this.handler = handler;
		this.resumeFile = resumeFile;
		this.company = company;
		this.title = title;
		this.description = description;
	}
	
	public String setUserContent(FileHandler handler, File resumeFile, String company, String title, String description) throws IOException {
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
	
	private PromptContent setHandler(FileHandler handler) {
		this.handler = handler;
		return this;
	}
	
	private PromptContent setCompany(String company) {
		this.company = company;
		return this;
	}
	
	private PromptContent setTitle(String title) {
		this.title = title;
		return this;
	}
	
	private PromptContent setDescription(String description) {
		this.description = description;
		return this;
	}
	
	private PromptContent setResumeFile(File file) {
		this.resumeFile = file;
		return this;
	}
	
	private String build() throws IOException {
		String
			resume = handler.extract(resumeFile);
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
