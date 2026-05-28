package br.com.pdvmercado.model;

/**
 * Classe Abstrata Pagamento
 *
 * Conceito de POO aplicado: HERANÇA + POLIMORFISMO + ABSTRAÇÃO
 *
 * Esta classe define o contrato comum para todos os tipos de pagamento.
 * O método "processar()" é abstrato, ou seja, cada subclasse DEVE
 * implementar seu próprio comportamento de processamento.
 *
 * Justificativa: "Aplicamos herança e polimorfismo no sistema de pagamentos
 * porque cada forma de pagamento possui comportamento específico,
 * mas compartilha características em comum."
 */
public abstract class Pagamento {

    // Atributos comuns a todos os pagamentos
    private double valor;
    private String descricao;

    // Construtor da classe abstrata
    public Pagamento(double valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    // =============================================
    // MÉTODO ABSTRATO - POLIMORFISMO
    // Cada subclasse implementa sua própria lógica
    // =============================================

    /**
     * Processa o pagamento conforme a forma escolhida.
     * Cada subclasse implementa este método de forma diferente.
     *
     * @return mensagem de confirmação do pagamento
     */
    public abstract String processar();

    /**
     * Retorna o tipo de pagamento (nome da forma).
     * Cada subclasse retorna seu próprio tipo.
     *
     * @return nome da forma de pagamento
     */
    public abstract String getTipo();

    // =============================================
    // GETTERS E SETTERS (atributos compartilhados)
    // =============================================

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
