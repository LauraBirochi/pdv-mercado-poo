package br.com.pdvmercado.model;

/**
 * Subclasse Gerente - herda de Usuario
 *
 * Conceito de POO aplicado: HERANÇA
 * O Gerente é um tipo de Usuario com permissões completas.
 * Pode cadastrar produtos, gerenciar funcionários e ver relatórios.
 */
public class Gerente extends Usuario {

    // Atributo específico do Gerente
    private String departamento;

    // Construtor - chama o construtor da superclasse com super()
    public Gerente(int id, String nome, String login, String senha, String departamento) {
        super(id, nome, login, senha, "GERENTE");
        this.departamento = departamento;
    }

    // =============================================
    // COMPORTAMENTOS ESPECÍFICOS DO GERENTE
    // =============================================

    /**
     * Gerentes podem cadastrar produtos.
     * @return true
     */
    public boolean podeCadastrarProduto() {
        return true;
    }

    /**
     * Gerentes podem gerenciar funcionários.
     * @return true
     */
    public boolean podeGerenciarFuncionarios() {
        return true;
    }

    /**
     * Gerentes podem ver relatórios de vendas.
     * @return true
     */
    public boolean podeVerRelatorios() {
        return true;
    }

    /**
     * Gerentes podem também registrar vendas.
     * @return true
     */
    public boolean podeRegistrarVenda() {
        return true;
    }

    // =============================================
    // GETTER E SETTER ESPECÍFICO
    // =============================================

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    @Override
    public String toString() {
        return super.toString() + " | Depto: " + departamento;
    }
}
