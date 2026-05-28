package br.com.pdvmercado.controller;

import br.com.pdvmercado.model.Usuario;
import br.com.pdvmercado.model.Caixa;
import br.com.pdvmercado.model.Gerente;
import java.util.ArrayList;

/**
 * LoginController
 *
 * Camada CONTROLLER do padrão MVC.
 *
 * Responsável por:
 * - Gerenciar o processo de autenticação
 * - Manter a lista de usuários cadastrados (em memória, com ArrayList)
 * - Controlar qual usuário está logado
 *
 * O Controller recebe as ações da View e interage com o Model.
 */
public class LoginController {

    // Armazenamento em memória com ArrayList (sem banco de dados)
    private ArrayList<Usuario> usuarios;
    private Usuario usuarioLogado;

    // Construtor - inicializa com usuários padrão para teste
    public LoginController() {
        this.usuarios = new ArrayList<>();
        this.usuarioLogado = null;
        cadastrarUsuariosPadrao();
    }

    // =============================================
    // CADASTRO DE USUÁRIOS PADRÃO (dados de teste)
    // =============================================

    /**
     * Cadastra usuários iniciais para facilitar os testes.
     * Em produção, estes viriam de um arquivo ou banco de dados.
     */
    private void cadastrarUsuariosPadrao() {
        // Cadastra um gerente padrão
        Gerente gerente = new Gerente(1, "Admin Gerente", "admin", "admin123", "Geral");
        usuarios.add(gerente);

        // Cadastra um caixa padrão
        Caixa caixa = new Caixa(2, "João Caixa", "joao", "joao123", 1);
        usuarios.add(caixa);
    }

    // =============================================
    // MÉTODOS DE AUTENTICAÇÃO
    // =============================================

    /**
     * Realiza o login do usuário.
     * Busca o usuário pelo login e verifica a senha.
     *
     * @param login login digitado
     * @param senha senha digitada
     * @return true se autenticou com sucesso
     */
    public boolean realizarLogin(String login, String senha) {
        for (Usuario u : usuarios) {
            if (u.getLogin().equals(login) && u.autenticar(senha)) {
                this.usuarioLogado = u;
                return true;
            }
        }
        return false;
    }

    /**
     * Realiza o logout do usuário atual.
     */
    public void realizarLogout() {
        this.usuarioLogado = null;
    }

    /**
     * Verifica se há um usuário logado.
     * @return true se há usuário logado
     */
    public boolean estaLogado() {
        return usuarioLogado != null;
    }

    // =============================================
    // GERENCIAMENTO DE USUÁRIOS
    // =============================================

    /**
     * Cadastra um novo usuário no sistema.
     * @param usuario usuário a ser cadastrado
     * @return true se cadastrou com sucesso
     */
    public boolean cadastrarUsuario(Usuario usuario) {
        // Verifica se o login já existe
        for (Usuario u : usuarios) {
            if (u.getLogin().equals(usuario.getLogin())) {
                return false; // Login já existe
            }
        }
        usuarios.add(usuario);
        return true;
    }

    /**
     * Remove um usuário pelo ID.
     * @param id ID do usuário a remover
     * @return true se removeu com sucesso
     */
    public boolean removerUsuario(int id) {
        return usuarios.removeIf(u -> u.getId() == id);
    }

    /**
     * Retorna a lista de todos os usuários.
     * @return lista de usuários
     */
    public ArrayList<Usuario> listarUsuarios() {
        return usuarios;
    }

    /**
     * Gera o próximo ID disponível para um novo usuário.
     * @return próximo ID
     */
    public int gerarProximoId() {
        int maior = 0;
        for (Usuario u : usuarios) {
            if (u.getId() > maior) maior = u.getId();
        }
        return maior + 1;
    }

    // =============================================
    // GETTER DO USUÁRIO LOGADO
    // =============================================

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}
