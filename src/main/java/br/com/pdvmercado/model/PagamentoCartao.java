package br.com.pdvmercado.model;

/**
 * Subclasse PagamentoCartao - herda de Pagamento
 *
 * Conceito de POO aplicado: HERANÇA + POLIMORFISMO
 * Implementa o processamento específico do pagamento com cartão,
 * incluindo suporte a parcelamento.
 */
public class PagamentoCartao extends Pagamento {

    // Atributos específicos do pagamento com cartão
    private String tipoCartao;   // "DEBITO" ou "CREDITO"
    private int numeroParcelas;
    private String ultimos4Digitos;

    // Construtor
    public PagamentoCartao(double valor, String tipoCartao, int numeroParcelas, String ultimos4Digitos) {
        super(valor, "Pagamento com Cartão");
        this.tipoCartao = tipoCartao.toUpperCase();
        this.numeroParcelas = numeroParcelas;
        this.ultimos4Digitos = ultimos4Digitos;
    }

    // =============================================
    // IMPLEMENTAÇÃO DOS MÉTODOS ABSTRATOS
    // =============================================

    /**
     * Simula o processamento do cartão.
     * Implementação específica do pagamento com cartão.
     */
    @Override
    public String processar() {
        if (tipoCartao.equals("DEBITO")) {
            return String.format("Cartão de DÉBITO aprovado! Final **** %s | R$ %.2f",
                    ultimos4Digitos, getValor());
        } else {
            double valorParcela = getValor() / numeroParcelas;
            return String.format("Cartão de CRÉDITO aprovado! Final **** %s | %dx de R$ %.2f",
                    ultimos4Digitos, numeroParcelas, valorParcela);
        }
    }

    @Override
    public String getTipo() {
        return "Cartão (" + tipoCartao + ")";
    }

    // =============================================
    // MÉTODOS ESPECÍFICOS
    // =============================================

    /**
     * Calcula o valor de cada parcela (apenas crédito).
     * @return valor da parcela
     */
    public double calcularParcela() {
        if (numeroParcelas <= 0) return getValor();
        return getValor() / numeroParcelas;
    }

    public String getTipoCartao() {
        return tipoCartao;
    }

    public void setTipoCartao(String tipoCartao) {
        this.tipoCartao = tipoCartao.toUpperCase();
    }

    public int getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(int numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }

    public String getUltimos4Digitos() {
        return ultimos4Digitos;
    }

    public void setUltimos4Digitos(String ultimos4Digitos) {
        this.ultimos4Digitos = ultimos4Digitos;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Tipo: %s | Parcelas: %d", tipoCartao, numeroParcelas);
    }
}
