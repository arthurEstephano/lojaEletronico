package br.edu.femass.dao;

import br.edu.femass.model.*;
import br.edu.femass.model.Compra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraDao extends DaoPostgres implements Dao<Compra>{
    @Override
    public List<Compra> listar() throws Exception {
        String sql = "select " +
                "compra.id as id, " +
                "compra.valor_total as valor_total, " +
                "compra.data as data, " +
                "order by cliente.data asc";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ResultSet rs = ps.executeQuery();

        List<Compra> compras = new ArrayList<>();

        while (rs.next()) {
            Compra compra = new Compra();
            compra.setValorTotal(rs.getFloat("valor_total"));
            compra.setId(rs.getLong("id"));
            compra.setData(rs.getDate("data"));
            compras.add(compra);
        }
        return compras;
    }

    public Float consultarValor (java.util.Date data) throws Exception {
        String sql = "SELECT" +
                "compra.valor_total AS valor_total, " +
                "WHERE compra.data BETWEEN ' " + data + " 00:00:00 " +
                "AND " + data + "23:59:59";

        PreparedStatement ps = getPreparedStatement(sql, true);
        ResultSet rs = ps.executeQuery();

        Compra compra = new Compra();
        compra.setValorTotal(rs.getFloat("valor_total"));

        return compra.getValorTotal();
    }
    @Override
    public void gravar(Compra value) throws Exception {
        Connection conexao = getConexao();
        conexao.setAutoCommit(false);
        try {
            String sql = "INSERT INTO compra (data, valor_total, id_fornecedor) VALUES (?,?,?)";
            PreparedStatement ps = getPreparedStatement(sql, true);
            ps.setDate(1, (Date) value.getData());
            ps.setFloat(2, value.getValorTotal());
            ps.setLong(3, value.getFornecedor().getId());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            value.setId(rs.getLong(1));

            for (ItemCompra itemComprado: value.getItensComprados()) {
                sql = "INSERT INTO item_compra (qtd, preco_venda, id_compra, id_produto) VALUES (?,?,?,?)";
                PreparedStatement ps2 = getPreparedStatement(sql, true);
                ps2.setInt(1, itemComprado.getQtd());
                ps2.setFloat(2, itemComprado.getPrecoCompra());
                ps2.setLong(3, value.getId());
                ps2.setLong(4, itemComprado.getProduto().getId());

                ps2.executeUpdate();

                ResultSet rs2 = ps.getGeneratedKeys();
                rs2.next();
                itemComprado.setId(rs2.getLong(1));

                ProdutoDao produtoDao = new ProdutoDao();
                produtoDao.alterar(itemComprado.getQtd(), itemComprado.getProduto());

            }
        } catch (SQLException exception) {
            conexao.rollback();
            throw exception;
        }
    }

    @Override
    public void alterar(Compra value) throws Exception {
        //Este conjunto de dados não possui alteração
    }

    @Override
    public void excluir(Compra value) throws Exception {
        //Este conjunto de dados não possui exclusão
    }
}
