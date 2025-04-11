package br.com.jadersoncardoso.file.importer.impl;

import br.com.jadersoncardoso.data.dto.PersonDTO;
import br.com.jadersoncardoso.file.importer.contract.FileImporter;

import java.io.InputStream;
import java.util.List;

public class CsvImporter implements FileImporter {
    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {
        return null;
    }
}
