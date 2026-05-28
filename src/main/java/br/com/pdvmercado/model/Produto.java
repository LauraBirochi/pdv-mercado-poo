package br.com.pdvmercado.model;

/**
 * Classe Produto
 *
 * Conceito de POO aplicado: ENCAPSULAMENTO
 * Os atributos são privados e só podem ser acessados/modificados
 * através dos métodos getters, setters e métodos de negócio.
 *
 * Isso garante que o estoque não seja alterado diretamente,
 * mantendo a integridade das regras do sistema.
 */
public class Produto {

    // Atributos privados - encapsulamento
    private int id;
    private String nome;
    private double preco;
    private int estoque;

    // Construtor completo
    public Produto(int id, String nome, double preco, int estoque) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    // =============================================
    // MÉTODOS DE NEGÓCIO (regras de estoque)
    // =============================================

    /**
     * Reduz o estoque ao realizar uma venda.
     * Lança exceção se a quantidade for insuficiente.
     *
     * @param quantidade quantidade a ser reduzida
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
     *
     * @param quantidade quantidade a ser adicionada
     */
    public void adicionarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade inválida.");
        }
        this.estoque += quantidade;
    }

    /**
     * Verifica se há estoque disponível para a quantidade solicitada.
     *
     * @param quantidade quantidade desejada
     * @return true se há estoque suficiente
     */
    public boolean temEstoque(int quantidade) {
        return this.estoque >= quantidade;
    }

    // =============================================
    // GETTERS E SETTERS
    // =============================================

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

    // Estoque não tem setter direto - só pode ser alterado pelos métodos de negócio

    @Override
    public String toString() {
        return String.format("[%d] %s - R$ %.2f (Estoque: %d)", id, nome, preco, estoque);
    }
}
