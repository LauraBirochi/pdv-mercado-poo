package br.com.pdvmercado.model;

/**
 * Subclasse PagamentoPix - herda de Pagamento
 *
 * Conceito de POO aplicado: HERANÇA + POLIMORFISMO
 * Implementa o processamento específico do pagamento via Pix,
 * com geração de chave e confirmação.
 */
public class PagamentoPix extends Pagamento {

    // Atributos específicos do Pix
    private String chavePix;
    private String codigoPix; // Código QR simulado

    // Construtor
    public PagamentoPix(double valor, String chavePix) {
        super(valor, "Pagamento via Pix");
        this.chavePix = chavePix;
        this.codigoPix = gerarCodigoPix();
    }

    // =============================================
    // IMPLEMENTAÇÃO DOS MÉTODOS ABSTRATOS
    // =============================================

    /**
     * Simula o processamento do Pix.
     * Implementação específica do pagamento via Pix.
     */
    @Override
    public String processar() {
        return String.format("Pix aprovado! Chave: %s | Cód: %s | R$ %.2f",
                chavePix, codigoPix, getValor());
    }

    @Override
    public String getTipo() {
        return "Pix";
    }

    // =============================================
    // MÉTODOS ESPECÍFICOS
    // =============================================

    /**
     * Gera um código Pix simulado (para fins acadêmicos).
     * @return código Pix simplificado
     */
    private String gerarCodigoPix() {
        // Simulação simplificada de um código Pix
        return "PIX" + System.currentTimeMillis();
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
        this.codigoPix = gerarCodigoPix(); // Regenera o código ao trocar a chave
    }

    public String getCodigoPix() {
        return codigoPix;
    }

    @Override
    public String toString() {
        return super.toString() + " | Chave: " + chavePix;
    }
}
