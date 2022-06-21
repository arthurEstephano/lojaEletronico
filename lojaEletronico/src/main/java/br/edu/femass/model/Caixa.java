package br.edu.femass.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Caixa {
    private Long id;
    private LocalDateTime data;
    private List<Operacao> operacoes= new ArrayList<>();
    private Float total;


    public Caixa (LocalDateTime data){this.data = data; this.total = 0F;}

    public String fecharCaixa(LocalDateTime data){

        if(operacoes.isEmpty()){return "Esse dia não possui registro de operações.";}
        for (Operacao op : operacoes) {
            if(op.getTipo() == TipoOperacao.VENDA){
                this.total = this.total - op.getValor();
            }
            else{
                this.total = this.total + op.getValor();
            }
        }
        return "O caixa do dia " + this.getData().toString() + " fechou em: R$ " + this.getTotal().toString();
    }
}
