package br.com.pdvmercado.model;

public abstract class Pagamento {
    
    private double valor;
    private String descricao;

    public Pagamento(double valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public abstract String processar();

    public abstract String getTipo();

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return String.format("Pagamento [%s] - R$ %.2f", getTipo(), valor);
    }
}
