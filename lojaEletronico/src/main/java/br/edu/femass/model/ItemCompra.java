package br.edu.femass.model;

import lombok.Data;


@Data
public class ItemCompra {

    private Long id;
    private int qtd;
    private Float PrecoCompra;
    private Produto produto;

    @Override
    public String toString() {return this.produto.getNome() + "; Preço compra: " + this.getPrecoCompra() +
            "; Quantidade: " + this.getQtd();}
}