package br.edu.femass.model;


import lombok.*;

@Data
public class Produto {
    private Long id;
    private String nome;
    private Float precoVenda;
    private int estoque;

    @Override
    public String toString(){return this.getNome() + "; Estoque: " + this.getEstoque();}


}