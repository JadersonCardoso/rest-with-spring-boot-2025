package br.com.jadersoncardoso.integrationtests.dto.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class WrapperPersonDTO implements Serializable {

    private static final long serialLVersionUID = 1L;

    @JsonProperty("_embedded")
    private PersonEmbeddedDTO embedded;

    public WrapperPersonDTO() {
    }

    public PersonEmbeddedDTO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(PersonEmbeddedDTO embedded) {
        this.embedded = embedded;
    }
}
