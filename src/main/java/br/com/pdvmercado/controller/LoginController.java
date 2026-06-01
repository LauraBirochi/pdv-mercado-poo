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

 */
public class LoginController {

    // Armazenamento em memória com ArrayList
    private ArrayList<Usuario> usuarios;
    private Usuario usuarioLogado;

    public LoginController() {
        this.usuarios = new ArrayList<>();
        this.usuarioLogado = null;
        cadastrarUsuariosPadrao();
    }

    private void cadastrarUsuariosPadrao() {
        Gerente gerente = new Gerente(1, "Admin Gerente", "admin", "admin123", "Geral");
        usuarios.add(gerente);

        Caixa caixa = new Caixa(2, "Carol Caixa", "Carol", "123", 2);
        usuarios.add(caixa);
        
        Caixa caixa2 = new Caixa(3, "Gabi Caixa", "Gabi", "123", 5);
        usuarios.add(caixa2);
        
        Caixa caixa3 = new Caixa(4, "Heitor Caixa", "Heitor", "123", 4);
        usuarios.add(caixa3);
    }

    public boolean realizarLogin(String login, String senha) {
        for (Usuario u : usuarios) {
            if (u.getLogin().equals(login) && u.autenticar(senha)) {
                this.usuarioLogado = u;
                return true;
            }
        }
        return false;
    }

    public void realizarLogout() {
        this.usuarioLogado = null;
    }

    public boolean estaLogado() {
        return usuarioLogado != null;
    }

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


    public boolean removerUsuario(int id) {
        return usuarios.removeIf(u -> u.getId() == id);
    }


    public ArrayList<Usuario> listarUsuarios() {
        return usuarios;
    }

    public int gerarProximoId() {
        int maior = 0;
        for (Usuario u : usuarios) {
            if (u.getId() > maior) maior = u.getId();
        }
        return maior + 1;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}
