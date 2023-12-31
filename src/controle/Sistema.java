package controle;

import banco_de_dados.BD;
import banco_de_dados.BdException;

import java.sql.*;

public class Sistema {

    private Connection conn = null;
    private Statement st = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public Sistema () {
        conn = BD.abrirConexao();
    }

    public void addProduto(String nomeProduto, Double preco, int quantidade) {
        try {
            ps = conn.prepareStatement("insert into produtos values (NULL, ?, ?, ?);");
            ps.setString(1,nomeProduto);
            ps.setDouble(2, preco);
            ps.setInt(3, quantidade);

            ps.executeUpdate();
            System.out.println("Produto inserido com sucesso!");

        } catch (SQLException e) {
            throw new BdException(e.getMessage());
        } finally {
            BD.fecharStatementAndResultSet(st, rs);
        }
    }
    public void aumentarQuantidade(String nomeProduto, int quantidade) {
        try {
            int id = pegaId(nomeProduto);
            ps = conn.prepareStatement("update produtos set quantidade = quantidade + ? where id = ?");
            ps.setInt(1, quantidade);
            ps.setInt(2, id);

            ps.executeUpdate();
            System.out.println("Quantidade aumentada com sucesso!");

        } catch (SQLException e) {
            throw new BdException(e.getMessage());
        } finally {
            BD.fecharStatementAndResultSet(st, rs);
        }
    }

    private int pegaId (String nomeProduto) {
        try {
            int id = 0;
            st = conn.createStatement();
            rs = st.executeQuery("select id from produtos where nome = '" + nomeProduto + "';");

            while (rs.next()) {
                id = rs.getInt("id");
            }
            return id;
        } catch (SQLException e) {
            throw new BdException(e.getMessage());
        } finally {
            BD.fecharStatementAndResultSet(st, rs);
        }
    }

    public void diminuirQuantidade (String nomeProduto, int quantidadeComprada)  {
        try {
            int id = pegaId(nomeProduto);
            int novaQuantidade = pegaQuantidadeAtual(nomeProduto) - quantidadeComprada;

            if (novaQuantidade < 0) {
                throw new BdException("ERRO: QUANTIDADE NÃO PODE SER MENOR QUE ZERO!");

            } else {

                ps = conn.prepareStatement("update produtos set quantidade = quantidade - ? where id = ? ;");
                ps.setInt(1, quantidadeComprada);
                ps.setInt(2, id);

                st = conn.createStatement();
                rs = st.executeQuery("select quantidade from produtos where id = " + id);

                ps.executeUpdate();
                System.out.println("Quantidade diminuida com sucesso!");
            }

        } catch (SQLException e) {
            throw new BdException(e.getMessage());
        } finally {
            BD.fecharStatementAndResultSet(st, rs);
        }
    }

    private int pegaQuantidadeAtual(String nomeProduto) {
        try {
            int quantidadeAtual = 0;
            st = conn.createStatement();
            rs = st.executeQuery("select quantidade from produtos where nome = '" + nomeProduto + "';");

            while (rs.next()) {
                quantidadeAtual = rs.getInt("quantidade");
            }
            return quantidadeAtual;

        } catch (SQLException e) {
            throw new BdException(e.getMessage());
        } finally {
            BD.fecharStatementAndResultSet(st, rs);
        }
    }

    public void atualizarPreco (String nomeProduto, Double novoPreco)  {
        try {
            int id = pegaId(nomeProduto);

            if (novoPreco < 0.00) {
                throw new BdException("ERRO! PREÇO MENOR QUE ZERO!");

            } else {
                ps = conn.prepareStatement("update produtos set preco = ? where id = ? ;");
                ps.setDouble(1, novoPreco);
                ps.setInt(2, id);

                ps.executeUpdate();
                System.out.println("Preço atualizado com sucesso!");
            }

        } catch (SQLException e) {
            throw new BdException(e.getMessage());
        }  finally {
            BD.fecharStatementAndResultSet(st, rs);
        }
    }


    public void verProduto (String nomeProduto) {
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select * from produtos where nome = '" + nomeProduto + "';");

            while (rs.next()) {
                System.out.println("Id: " + rs.getInt("id") + " | Nome: " + rs.getString("nome")
                        + " | Preço: " + String.format("%.2f", rs.getDouble("preco")) + " | Quantidade: " + rs.getInt("quantidade"));
            }

        } catch (SQLException e) {
            throw new BdException(e.getMessage());
        } finally {
            BD.fecharStatementAndResultSet(st, rs);
        }
    }

    public void verTodosOsProdutos () {
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select * from produtos");

            while (rs.next()) {
                System.out.println("Id: " + rs.getInt("id") + " | Nome: " + rs.getString("nome")
                        + " | Preço: "  + String.format("%.2f", rs.getDouble("preco")) + " | Quantidade: " + rs.getInt("quantidade"));
            }

        } catch (SQLException e) {
            throw new BdException(e.getMessage());
        } finally {
            BD.fecharStatementAndResultSet(st, rs);
        }
    }

    public void deletarProduto(String nomeProduto) {
        try {
            int id = pegaId(nomeProduto);

            ps = conn.prepareStatement("delete from produtos where id = ?");
            ps.setInt(1, id);

            ps.executeUpdate();

            System.out.println("Produto deletado com sucesso!");

        } catch (SQLException e) {
            throw new BdException(e.getMessage());
        } finally {
            BD.fecharStatementAndResultSet(st, rs);
        }
    }



}
