package br.edu.femass.dao;

import br.edu.femass.model.ItemCompra;
import br.edu.femass.model.ItemVenda;
import br.edu.femass.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDao extends DaoPostgres implements Dao<Produto>{
    @Override
    public List<Produto> listar() throws Exception {
        String sql = "select " +
                "produto.id as id, " +
                "produto.nome as nome, " +
                "produto.preco_venda as preco_venda, " +
                "produto.estoque as estoque, " +
                "order by produto.nome asc";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ResultSet rs = ps.executeQuery();

        List<Produto> produtos = new ArrayList<>();

        while (rs.next()) {
            Produto produto = new Produto();
            produto.setNome(rs.getString("nome"));
            produto.setId(rs.getLong("id"));
            produto.setEstoque(rs.getInt("estoque"));
            produtos.add(produto);
        }
        return produtos;
    }

    @Override
    public void gravar(Produto value) throws Exception {
        String sql = "INSERT INTO produto (nome, preco_venda, estoque) VALUES (?,?,?)";
        PreparedStatement ps = getPreparedStatement(sql, true);
        ps.setString(1, value.getNome());
        ps.setFloat(2, value.getPrecoVenda());
        ps.setInt(3, value.getEstoque());

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        value.setId(rs.getLong(1));

    }

    @Override
    public void alterar(Produto value) throws Exception {
        String sql = "update produto set nome = ?, preco_venda = ?, estoque = ?  where id = ?";
        PreparedStatement ps = getPreparedStatement(sql, false);
        ps.setString(1, value.getNome());
        ps.setFloat(2, value.getPrecoVenda());
        ps.setInt(3, value.getEstoque());
        ps.executeUpdate();
    }

    public void alterar(Object t, Produto value) throws Exception {
        String sql = "update produto set estoque = ? where id = ?";
        PreparedStatement ps = getPreparedStatement(sql, false);
        int estoque = value.getEstoque();
        if(t instanceof ItemVenda) {
            if(((ItemVenda) t).getQtd() > value.getEstoque()){
                return;
            }
            ps.setInt(1, (estoque - ((ItemVenda) t).getQtd()));
        }
        else {
            ps.setInt(1, (estoque + ((ItemCompra) t).getQtd()));
        }
        ps.executeUpdate();
    }

    @Override
    public void excluir(Produto value) throws Exception {
        Connection conexao = getConexao();
        conexao.setAutoCommit(false);
        try {
            String sql = "delete from produto where id = ?";
            PreparedStatement ps1 = conexao.prepareStatement(sql);
            ps1.setLong(1, value.getId());
            ps1.executeUpdate();
            conexao.commit();
        } catch (SQLException exception) {
            conexao.rollback();
            throw exception;
        }
    }
}
