package br.com.pdvmercado.model;

/**
 * Superclasse Usuario
 *
 * Conceito de POO aplicado: HERANÇA
 * Esta classe representa a base para todos os tipos de usuário do sistema.
 * As subclasses (Caixa e Gerente) herdam seus atributos e comportamentos,
 * adicionando características específicas de cada perfil.
 *
 * Justificativa: "Aplicamos herança nos usuários para representar diferentes
 * permissões dentro do sistema."
 */
public class Usuario {

    // Atributos privados - encapsulamento
    private int id;
    private String nome;
    private String login;
    private String senha;
    private String perfil; // "CAIXA" ou "GERENTE"

    // Construtor
    public Usuario(int id, String nome, String login, String senha, String perfil) {
        this.id = id;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    // =============================================
    // MÉTODO DE AUTENTICAÇÃO
    // =============================================

    /**
     * Verifica se a senha informada corresponde à senha do usuário.
     *
     * @param senhaInformada senha digitada pelo usuário
     * @return true se a senha for correta
     */
    public boolean autenticar(String senhaInformada) {
        return this.senha.equals(senhaInformada);
    }

    // =============================================
    // GETTERS E SETTERS
    // =============================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPerfil() {
        return perfil;
    }

    protected void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    // Senha não tem getter por segurança
    protected void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s | Login: %s | Perfil: %s", id, nome, login, perfil);
    }
}
