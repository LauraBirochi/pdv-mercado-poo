package br.com.pdvmercado.controller;

/**
 * Responsável por(Fachada):
 * - Inicializar todos os controllers do sistema
 * - Centralizar o acesso aos subsistemas
 * - Coordenar o fluxo geral da aplicação
 *
 * eh o ponto central que conecta todas as partes do sistema.
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

    // ACESSO AOS CONTROLLERS 

    /**
     * Retorna o controller de login/autenticação.
     * @return LoginController
     */
    public LoginController getLoginController() {
        return loginController;
    }

    /**
     * Retorna o controller de produtos.
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

    // MÉTODOS DE CONVENIÊNCIA = operacoes atalho

    /**
     * Verifica se o sistema tem um usuário autenticado.
     */
    public boolean sistemaAutenticado() {
        return loginController.estaLogado();
    }

    /**
     * Retorna o nome do usuário logado.
     */
    public String getNomeUsuarioLogado() {
        if (!sistemaAutenticado()) return "Nenhum";
        return loginController.getUsuarioLogado().getNome();
    }

    /**
     * Retorna o perfil do usuário logado.
     */
    public String getPerfilUsuarioLogado() {
        if (!sistemaAutenticado()) return "N/A";
        return loginController.getUsuarioLogado().getPerfil();
    }

    /**
     * Verifica se o usuário logado é gerente.
     */
    public boolean usuarioEhGerente() {
        if (!sistemaAutenticado()) return false;
        return "GERENTE".equals(loginController.getUsuarioLogado().getPerfil());
    }
}
