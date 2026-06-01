package br.com.pdvmercado.model;

public class PagamentoCartao extends Pagamento {

    private String tipoCartao;  
    private int numeroParcelas;
    private String ultimos4Digitos;

    public PagamentoCartao(double valor, String tipoCartao, int numeroParcelas, String ultimos4Digitos) {
        super(valor, "Pagamento com Cartão");
        this.tipoCartao = tipoCartao.toUpperCase();
        this.numeroParcelas = numeroParcelas;
        
        // A validação agora fica centralizada no método set
        setUltimos4Digitos(ultimos4Digitos);
    }

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
        if (ultimos4Digitos == null || !ultimos4Digitos.matches("\\d{4}") || ultimos4Digitos.equals("0000")) {
            throw new IllegalArgumentException("Os últimos 4 dígitos do cartão devem conter exatamente 4 números e não podem ser 0000.");
        }
        this.ultimos4Digitos = ultimos4Digitos;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Tipo: %s | Parcelas: %d", tipoCartao, numeroParcelas);
    }
}
