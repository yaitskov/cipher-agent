package org.dan.cipher.demo;

import org.dan.cipher.Decrypt;
import org.dan.cipher.Encrypt;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Decrypt
@Encrypt
public class Pojo {
    private String id;
    private String data;

    @Decrypt
    @JsonIgnore
    public String getPlainData() {
        return data;
    }

    @Encrypt
    @JsonIgnore
    public void setPlainData(String data) {
        this.data = data;
    }
}
