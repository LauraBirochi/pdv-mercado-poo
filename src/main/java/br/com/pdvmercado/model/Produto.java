package br.com.pdvmercado.model;

public class Produto {

    private int id;
    private String nome;
    private double preco;
    private int estoque;

    public Produto(int id, String nome, double preco, int estoque) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    /**
     * Reduz o estoque ao realizar uma venda.
     */
    public void reduzirEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade inválida.");
        }
        if (this.estoque < quantidade) {
            throw new IllegalStateException("Estoque insuficiente para o produto: " + this.nome);
        }
        this.estoque -= quantidade;
    }

    /**
     * Adiciona ao estoque (reposição de produto).
     */
    public void adicionarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade inválida.");
        }
        this.estoque += quantidade;
    }

    /**
     * Verifica se há estoque disponível para a quantidade solicitada.
     */
    public boolean temEstoque(int quantidade) {
        return this.estoque >= quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo.");
        }
        this.preco = preco;
    }

    public int getEstoque() {
        return estoque;
    }

    // Estoque não tem setter direto

    @Override
    public String toString() {
        return String.format("[%d] %s - R$ %.2f (Estoque: %d)", id, nome, preco, estoque);
    }
}
