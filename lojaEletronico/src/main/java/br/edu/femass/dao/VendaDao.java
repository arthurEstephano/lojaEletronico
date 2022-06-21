package br.edu.femass.dao;


import br.edu.femass.model.Venda;
import br.edu.femass.model.ItemVenda;
import br.edu.femass.model.Produto;
import br.edu.femass.model.Venda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendaDao extends DaoPostgres implements Dao<Venda>{
    @Override
    public List<Venda> listar() throws Exception {
        String sql = "select " +
                "venda.id as id, " +
                "venda.valor_total as valor_total, " +
                "venda.data as data, " +
                "order by cliente.data asc";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ResultSet rs = ps.executeQuery();

        List<Venda> vendas = new ArrayList<>();

        while (rs.next()) {
            Venda venda = new Venda();
            venda.setValorTotal(rs.getFloat("valor_total"));
            venda.setId(rs.getLong("id"));
            venda.setData(rs.getDate("data"));
            vendas.add(venda);
        }
        return vendas;
    }

    @Override
    public void gravar(Venda value) throws Exception {
        Connection conexao = getConexao();
        conexao.setAutoCommit(false);
        try {
            String sql = "INSERT INTO venda (data, valor_total, id_cliente) VALUES (?,?,?)";
            PreparedStatement ps = getPreparedStatement(sql, true);
            ps.setDate(1, (Date) value.getData());
            ps.setFloat(2, value.getValorTotal());
            ps.setLong(3, value.getCliente().getId());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            value.setId(rs.getLong(1));


            for (ItemVenda itemVendido: value.getItensVendidos()) {
                sql = "INSERT INTO item_venda (qtd, preco_venda, id_venda, id_produto) VALUES (?,?,?,?)";
                PreparedStatement ps2 = getPreparedStatement(sql, true);
                ps2.setInt(1, itemVendido.getQtd());
                ps2.setFloat(2, itemVendido.getPrecoVenda());
                ps2.setLong(3, value.getId());
                ps2.setLong(4, itemVendido.getProduto().getId());

                ps2.executeUpdate();

                ResultSet rs2 = ps.getGeneratedKeys();
                rs2.next();
                itemVendido.setId(rs2.getLong(1));

                ProdutoDao produtoDao = new ProdutoDao();
                produtoDao.alterar(itemVendido.getQtd(), itemVendido.getProduto());

                }
        } catch (SQLException exception) {
            conexao.rollback();
            throw exception;
        }
    }

    @Override
    public void alterar(Venda value) throws Exception {
        //Este conjunto de dados não possui alteração
    }

    @Override
    public void excluir(Venda value) throws Exception {
        //Este conjunto de dados não possui exclusão
    }
}
