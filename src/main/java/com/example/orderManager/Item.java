package com.example.orderManager;

import javax.persistence.*;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String urlImagem;

    protected Item() {}

    public Item(String name, String urlImagem) {
        this.name = name;
        this.urlImagem = urlImagem;
    }

    public Long getIdItem() {
        return id;
    }

    public void setIdItem(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}
