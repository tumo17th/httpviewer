package com.shibatats.httpviewer.printer;

import java.io.IOException;

public interface Printer {
	void printToFile(String content) throws IOException;
	void printToConsole(String content);
}
