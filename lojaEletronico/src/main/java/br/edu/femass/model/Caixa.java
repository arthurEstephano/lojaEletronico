package br.edu.femass.model;

import br.edu.femass.dao.CompraDao;
import br.edu.femass.dao.VendaDao;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Caixa {
    private Date data;
    private List<Operacao> operacoes= new ArrayList<>();
    private Float total;


    public Caixa (Date data){this.data = data; this.total = 0F;}

    public String fecharCaixa(Date data){

        Operacao opCompra = new Operacao();
        opCompra.setTipo(TipoOperacao.COMPRA);
        Operacao opVenda = new Operacao();
        opVenda.setTipo(TipoOperacao.VENDA);
        CompraDao compraDao = new CompraDao();
        VendaDao vendaDao = new VendaDao();

        try {
            opCompra.setValor(compraDao.consultarValor(data));
            opVenda.setValor(vendaDao.consultarValor(data));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.operacoes.add(opCompra);
        this.operacoes.add(opVenda);

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
