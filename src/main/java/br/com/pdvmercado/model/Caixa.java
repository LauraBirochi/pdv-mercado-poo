package br.com.pdvmercado.model;

/**
 * Subclasse Caixa - herda de Usuario
 *
 * Conceito de POO aplicado: HERANÇA
 * O Caixa é um tipo de Usuario com permissões limitadas.
 * Pode registrar vendas, mas não tem acesso ao cadastro de produtos.
 */
public class Caixa extends Usuario {

    // Atributo específico da subclasse
    private int numeroCaixa;

    // Construtor - chama o construtor da superclasse com super()
    public Caixa(int id, String nome, String login, String senha, int numeroCaixa) {
        super(id, nome, login, senha, "CAIXA");
        this.numeroCaixa = numeroCaixa;
    }

    // =============================================
    // COMPORTAMENTO ESPECÍFICO DO CAIXA
    // =============================================

    /**
     * Abre o caixa para o turno.
     * Comportamento exclusivo da subclasse Caixa.
     */
    public String abrirCaixa() {
        return "Caixa " + numeroCaixa + " aberto pelo operador: " + getNome();
    }

    /**
     * Verifica se o perfil permite registrar vendas.
     * @return true - caixas sempre podem registrar vendas
     */
    public boolean podeRegistrarVenda() {
        return true;
    }

    /**
     * Caixas NÃO podem cadastrar produtos.
     * @return false
     */
    public boolean podeCadastrarProduto() {
        return false;
    }

    // =============================================
    // GETTER ESPECÍFICO
    // =============================================

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
