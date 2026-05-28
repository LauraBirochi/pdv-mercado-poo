package br.com.pdvmercado.controller;

/**
 * SistemaController
 *
 * Camada CONTROLLER do padrão MVC.
 *
 * Responsável por:
 * - Inicializar todos os controllers do sistema
 * - Centralizar o acesso aos subsistemas
 * - Coordenar o fluxo geral da aplicação
 *
 * É o ponto central que conecta todas as partes do sistema.
 * A View interage com os controllers através desta classe.
 */
public class SistemaController {

    // Controllers dos subsistemas
    private LoginController loginController;
    private ProdutoController produtoController;
    private VendaController vendaController;

    // Construtor - inicializa todos os controllers
    public SistemaController() {
        this.loginController = new LoginController();
        this.produtoController = new ProdutoController();
        this.vendaController = new VendaController(produtoController);
    }

    // =============================================
    // ACESSO AOS CONTROLLERS (Facade)
    // =============================================

    /**
     * Retorna o controller de login/autenticação.
     * @return LoginController
     */
    public LoginController getLoginController() {
        return loginController;
    }

    /**
     * Retorna o controller de produtos.
     * @return ProdutoController
     */
    public ProdutoController getProdutoController() {
        return produtoController;
    }

    /**
     * Retorna o controller de vendas.
     * @return VendaController
     */
    public VendaController getVendaController() {
        return vendaController;
    }

    // =============================================
    // MÉTODOS DE CONVENIÊNCIA
    // =============================================

    /**
     * Verifica se o sistema tem um usuário autenticado.
     * @return true se há usuário logado
     */
    public boolean sistemaAutenticado() {
        return loginController.estaLogado();
    }

    /**
     * Retorna o nome do usuário logado.
     * @return nome do usuário ou "Nenhum"
     */
    public String getNomeUsuarioLogado() {
        if (!sistemaAutenticado()) return "Nenhum";
        return loginController.getUsuarioLogado().getNome();
    }

    /**
     * Retorna o perfil do usuário logado.
     * @return perfil ("CAIXA", "GERENTE") ou "N/A"
     */
    public String getPerfilUsuarioLogado() {
        if (!sistemaAutenticado()) return "N/A";
        return loginController.getUsuarioLogado().getPerfil();
    }

    /**
     * Verifica se o usuário logado é gerente.
     * @return true se for gerente
     */
    public boolean usuarioEhGerente() {
        if (!sistemaAutenticado()) return false;
        return "GERENTE".equals(loginController.getUsuarioLogado().getPerfil());
    }
}
