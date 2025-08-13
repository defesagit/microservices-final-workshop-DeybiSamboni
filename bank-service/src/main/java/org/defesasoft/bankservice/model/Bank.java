package org.defesasoft.bankservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("banks")
public class Bank {

    @Id
    private Long id;
    private String name;

    public Bank(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public Bank() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
