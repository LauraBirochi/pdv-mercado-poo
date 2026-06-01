package br.com.pdvmercado.model;

public class PagamentoDinheiro extends Pagamento {

    private double valorRecebido;

    public PagamentoDinheiro(double valor, double valorRecebido) {
        super(valor, "Pagamento em Dinheiro");
        this.valorRecebido = valorRecebido;
    }

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
