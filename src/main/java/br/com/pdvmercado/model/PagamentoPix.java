package br.com.pdvmercado.model;

public class PagamentoPix extends Pagamento {

    private String chavePix;
    private String codigoPix; // Código QR simulado

    public PagamentoPix(double valor, String chavePix) {
        super(valor, "Pagamento via Pix");
        this.chavePix = chavePix;
        this.codigoPix = gerarCodigoPix();
    }

    @Override
    public String processar() {
        return String.format("Pix aprovado! Chave: %s | Cód: %s | R$ %.2f",
                chavePix, codigoPix, getValor());
    }

    @Override
    public String getTipo() {
        return "Pix";
    }

    private String gerarCodigoPix() {
        return "PIX" + System.currentTimeMillis();
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
        this.codigoPix = gerarCodigoPix(); 
    }

    public String getCodigoPix() {
        return codigoPix;
    }

    @Override
    public String toString() {
        return super.toString() + " | Chave: " + chavePix;
    }
}
