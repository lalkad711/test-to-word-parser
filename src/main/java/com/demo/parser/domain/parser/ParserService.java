package com.demo.parser.domain.parser;

import java.nio.file.Path;

public interface ParserService {

    void parseAndCreateFiles(Path path);
}
