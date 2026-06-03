package br.com.pdvmercado.model;

/**
 * Subclasse Gerente - herda de Usuario
 */
public class Gerente extends Usuario {

    private String departamento;

    public Gerente(int id, String nome, String login, String senha, String departamento) {
        super(id, nome, login, senha, "GERENTE");
        this.departamento = departamento;
    }

    public boolean podeCadastrarProduto() {
        return true;
    }

    public boolean podeGerenciarFuncionarios() {
        return true;
    }


    public boolean podeVerRelatorios() {
        return true;
    }

    public boolean podeRegistrarVenda() {
        return true;
    }

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
