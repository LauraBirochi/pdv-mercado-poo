package br.com.pdvmercado.model;

/**
 * Classe Cliente
 *
 * Conceito de POO aplicado: ENCAPSULAMENTO
 * Os dados do cliente (nome e CPF) são privados.
 * A validação do CPF é feita antes de qualquer alteração,
 * protegendo a integridade dos dados.
 */
public class Cliente {

    // Atributos privados - encapsulamento
    private String nome;
    private String cpf;

    // Construtor
    public Cliente(String nome, String cpf) {
        this.nome = nome;
        // Valida o CPF antes de atribuir
        if (!validarCpf(cpf)) {
            throw new IllegalArgumentException("CPF inválido: " + cpf);
        }
        this.cpf = cpf;
    }

    // =============================================
    // MÉTODOS DE NEGÓCIO
    // =============================================

    /**
     * Valida se o CPF possui exatamente 11 dígitos numéricos.
     * (Validação simplificada para fins acadêmicos)
     *
     * @param cpf CPF a ser validado
     * @return true se o CPF for válido
     */
    public boolean validarCpf(String cpf) {
        if (cpf == null) return false;
        // Remove pontos e traços
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        return cpfLimpo.length() == 11;
    }

    /**
     * Atualiza o nome do cliente com validação.
     *
     * @param novoNome novo nome a ser atribuído
     */
    public void atualizarNome(String novoNome) {
        if (novoNome == null || novoNome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        this.nome = novoNome.trim();
    }

    // =============================================
    // GETTERS E SETTERS
    // =============================================

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    // CPF não pode ser alterado após criação (imutável)

    @Override
    public String toString() {
        return "Cliente: " + nome + " | CPF: " + cpf;
    }
}
