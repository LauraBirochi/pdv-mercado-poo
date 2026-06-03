package br.com.pdvmercado.controller;

import br.com.pdvmercado.model.Produto;
import java.util.ArrayList;

/**
 * Responsável por:
 * - CRUD de produtos (Create, Read, Update, Delete)
 * - Gerenciamento do estoque
 * - Busca e listagem de produtos
 *
 * Os dados são armazenados em memória usando ArrayList.
 */
public class ProdutoController {

    // Lista de produtos em memória (sem banco de dados)
    private ArrayList<Produto> produtos;
    private int proximoId;

    // Construtor - inicializa com produtos de exemplo
    public ProdutoController() {
        this.produtos = new ArrayList<>();
        this.proximoId = 1;
        cadastrarProdutosPadrao();
    }

    private void cadastrarProdutosPadrao() {
        cadastrarProduto(new Produto(proximoId++, "Arroz 5kg",      8.99,  50));
        cadastrarProduto(new Produto(proximoId++, "Feijão 1kg",     6.50,  40));
        cadastrarProduto(new Produto(proximoId++, "Macarrão 500g",  3.25,  60));
        cadastrarProduto(new Produto(proximoId++, "Óleo de Soja",   7.90,  30));
        cadastrarProduto(new Produto(proximoId++, "Leite Integral", 4.50,  80));
        cadastrarProduto(new Produto(proximoId++, "Café 500g",     12.90,  25));
        cadastrarProduto(new Produto(proximoId++, "Açúcar 1kg",     4.20,  70));
        cadastrarProduto(new Produto(proximoId++, "Sal 1kg",        2.00,  90));
    }

    public boolean cadastrarProduto(Produto produto) {
        // Verifica se já existe produto com mesmo ID
        for (Produto p : produtos) {
            if (p.getId() == produto.getId()) {
                return false;
            }
        }
        produtos.add(produto);
        return true;
    }

    /**
     * Cria e cadastra um novo produto com os dados fornecidos.
     */
    public Produto novoProduto(String nome, double preco, int estoque) {
        Produto p = new Produto(proximoId++, nome, preco, estoque);
        produtos.add(p);
        return p;
    }

    /**
     * Busca um produto pelo ID.
     */
    public Produto buscarPorId(int id) {
        for (Produto p : produtos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    /**
     * Busca produtos pelo nome (busca parcial, ignora maiúsculas/minúsculas).
     */
    public ArrayList<Produto> buscarPorNome(String nome) {
        ArrayList<Produto> resultado = new ArrayList<>();
        for (Produto p : produtos) {
            if (p.getNome().toLowerCase().contains(nome.toLowerCase())) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    /**
     * Atualiza os dados de um produto existente.
     */
    public boolean atualizarProduto(int id, String nome, double preco) {
        Produto p = buscarPorId(id);
        if (p == null) return false;
        p.setNome(nome);
        p.setPreco(preco);
        return true;
    }

    /**
     * Remove um produto pelo ID.
     */
    public boolean removerProduto(int id) {
        return produtos.removeIf(p -> p.getId() == id);
    }

    /**
     * Adiciona quantidade ao estoque de um produto.
     * @return true se operação foi realizada
     */
    public boolean reporEstoque(int id, int quantidade) {
        Produto p = buscarPorId(id);
        if (p == null) return false;
        p.adicionarEstoque(quantidade);
        return true;
    }

    /**
     * Lista todos os produtos cadastrados.
     */
    public ArrayList<Produto> listarTodos() {
        return produtos;
    }

    /**
     * Lista apenas produtos com estoque baixo
     */
    public ArrayList<Produto> listarEstoqueBaixo() {
        ArrayList<Produto> resultado = new ArrayList<>();
        for (Produto p : produtos) {
            if (p.getEstoque() < 10) {
                resultado.add(p);
            }
        }
        return resultado;
    }
}
