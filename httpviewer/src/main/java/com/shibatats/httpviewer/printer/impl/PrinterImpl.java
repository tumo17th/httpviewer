package com.shibatats.httpviewer.printer.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.shibatats.httpviewer.printer.Printer;

@Component
public class PrinterImpl implements Printer {

	private static final String FILE_PATH = "result";
	private static final String FILE_NAME_PREFIX = "response_";
	private static final String FS = File.separator;

	@Override
	public void printToFile(String content) throws IOException {
		final DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String path = FILE_PATH + FS + FILE_NAME_PREFIX + df.format(new Date()) + ".txt";

		try (BufferedWriter out = new BufferedWriter(new FileWriter(path))) {
			out.write(content);
		}
	}

	@Override
	public void printToConsole(String content) {
		System.out.println(content);
	}

}
