package br.com.pdvmercado.model;

/**
 * Classe ItemVenda
 *
 * Representa um item dentro do carrinho de compras.
 * Associa um Produto a uma quantidade e calcula o subtotal.
 */
public class ItemVenda {

    private Produto produto;
    private int quantidade;

    // Construtor
    public ItemVenda(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    // =============================================
    // MÉTODO DE NEGÓCIO
    // =============================================

    /**
     * Calcula o subtotal deste item (preço x quantidade).
     * @return subtotal do item
     */
    public double calcularSubtotal() {
        return produto.getPreco() * quantidade;
    }

    // =============================================
    // GETTERS E SETTERS
    // =============================================

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return String.format("%s | Qtd: %d | Subtotal: R$ %.2f",
                produto.getNome(), quantidade, calcularSubtotal());
    }
}
