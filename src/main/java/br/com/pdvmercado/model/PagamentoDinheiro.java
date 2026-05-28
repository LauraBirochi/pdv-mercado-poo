package br.com.pdvmercado.model;

/**
 * Subclasse PagamentoDinheiro - herda de Pagamento
 *
 * Conceito de POO aplicado: HERANÇA + POLIMORFISMO
 * Implementa o processamento específico do pagamento em dinheiro,
 * incluindo o cálculo do troco.
 */
public class PagamentoDinheiro extends Pagamento {

    // Atributo específico do pagamento em dinheiro
    private double valorRecebido;

    // Construtor
    public PagamentoDinheiro(double valor, double valorRecebido) {
        super(valor, "Pagamento em Dinheiro");
        this.valorRecebido = valorRecebido;
    }

    // =============================================
    // IMPLEMENTAÇÃO DOS MÉTODOS ABSTRATOS
    // =============================================

    /**
     * Calcula o troco e confirma o pagamento.
     * Implementação específica do pagamento em dinheiro.
     */
    @Override
    public String processar() {
        double troco = valorRecebido - getValor();
        if (troco < 0) {
            return "ERRO: Valor recebido insuficiente! Falta R$ " + String.format("%.2f", Math.abs(troco));
        }
        return String.format("Pagamento em dinheiro efetuado! Troco: R$ %.2f", troco);
    }

    @Override
    public String getTipo() {
        return "Dinheiro";
    }

    // =============================================
    // MÉTODOS ESPECÍFICOS
    // =============================================

    /**
     * Calcula o troco a ser devolvido.
     * @return valor do troco
     */
    public double calcularTroco() {
        return valorRecebido - getValor();
    }

    public double getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(double valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Recebido: R$ %.2f | Troco: R$ %.2f",
                valorRecebido, calcularTroco());
    }
}
