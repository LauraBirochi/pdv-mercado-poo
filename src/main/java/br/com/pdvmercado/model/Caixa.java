package br.com.pdvmercado.model;


public class Caixa extends Usuario {

    private int numeroCaixa;

    public Caixa(int id, String nome, String login, String senha, int numeroCaixa) {
        super(id, nome, login, senha, "CAIXA");
        this.numeroCaixa = numeroCaixa;
    }


    public String abrirCaixa() {
        return "Caixa " + numeroCaixa + " aberto pelo operador: " + getNome();
    }


    public boolean podeRegistrarVenda() {
        return true;
    }


    public boolean podeCadastrarProduto() {
        return false;
    }


    public int getNumeroCaixa() {
        return numeroCaixa;
    }

    public void setNumeroCaixa(int numeroCaixa) {
        this.numeroCaixa = numeroCaixa;
    }

    @Override
    public String toString() {
        return super.toString() + " | Caixa nº " + numeroCaixa;
    }
}
