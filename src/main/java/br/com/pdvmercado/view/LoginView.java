package br.com.pdvmercado.view;

import br.com.pdvmercado.controller.SistemaController;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * LoginView
 * Apresenta campos de login e senha e valida com o LoginController.
*/
public class LoginView extends JFrame {

    // Referência ao controller central
    private SistemaController sistemaController;

    // Componentes da tela
    private JTextField campologin;
    private JPasswordField campoSenha;
    private JButton botaoEntrar;
    private JLabel labelMensagem;

    // Construtor
    public LoginView(SistemaController sistemaController) {
        this.sistemaController = sistemaController;
        configurarJanela();
        criarComponentes();
    }

    // CONFIGURAÇÃO DA JANELA

    private void configurarJanela() {
        setTitle("PDV Mercado - Login");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);    // Centraliza na tela
        setResizable(false);
        getContentPane().setBackground(new Color(30, 30, 45));
        setLayout(new BorderLayout());
    }

    // CRIAÇÃO DOS COMPONENTES

    private void criarComponentes() {
        // Painel do título
        JPanel painelTitulo = new JPanel();
        painelTitulo.setBackground(new Color(30, 30, 45));
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0));

        JLabel titulo = new JLabel("PDV MERCADO");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(new Color(100, 200, 120));
        painelTitulo.add(titulo);

        JLabel subtitulo = new JLabel("Sistema de Ponto de Venda");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitulo.setForeground(new Color(180, 180, 180));
        painelTitulo.setLayout(new BoxLayout(painelTitulo, BoxLayout.Y_AXIS));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelTitulo.add(subtitulo);

        // Painel central com campos 
        JPanel painelCentral = new JPanel(new GridBagLayout());
        painelCentral.setBackground(new Color(40, 40, 58));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label Login
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel labelLogin = new JLabel("Usuário:");
        labelLogin.setForeground(Color.WHITE);
        labelLogin.setFont(new Font("Arial", Font.PLAIN, 13));
        painelCentral.add(labelLogin, gbc);

        // Campo Login
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        campologin = new JTextField(15);
        campologin.setFont(new Font("Arial", Font.PLAIN, 13));
        estilizarCampo(campologin);
        painelCentral.add(campologin, gbc);

        // Label Senha
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setForeground(Color.WHITE);
        labelSenha.setFont(new Font("Arial", Font.PLAIN, 13));
        painelCentral.add(labelSenha, gbc);

        // Campo Senha
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        campoSenha = new JPasswordField(15);
        campoSenha.setFont(new Font("Arial", Font.PLAIN, 13));
        estilizarCampo(campoSenha);
        painelCentral.add(campoSenha, gbc);

        // Botão Entrar
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weightx = 0;
        botaoEntrar = new JButton("ENTRAR");
        estilizarBotao(botaoEntrar, new Color(100, 200, 120));
        botaoEntrar.addActionListener(this::realizarLogin);
        painelCentral.add(botaoEntrar, gbc);

        // Label de mensagem (erros/avisos)
        gbc.gridy = 3;
        labelMensagem = new JLabel(" ");
        labelMensagem.setForeground(new Color(220, 80, 80));
        labelMensagem.setFont(new Font("Arial", Font.PLAIN, 12));
        labelMensagem.setHorizontalAlignment(SwingConstants.CENTER);
        painelCentral.add(labelMensagem, gbc);

        // Dica de credenciais
        gbc.gridy = 4;
        JLabel dica = new JLabel("<html><center><font color='#888888'>Admin: admin / admin123<br>Caixa: joao / joao123</font></center></html>");
        dica.setFont(new Font("Arial", Font.PLAIN, 11));
        dica.setHorizontalAlignment(SwingConstants.CENTER);
        painelCentral.add(dica, gbc);

        // Monta o layout principal
        add(painelTitulo, BorderLayout.NORTH);
        add(painelCentral, BorderLayout.CENTER);

        // Enter no campo senha também realiza login
        campoSenha.addActionListener(this::realizarLogin);
    }

    // Botao de Login

    private void realizarLogin(ActionEvent e) {
        String login = campologin.getText().trim();
        String senha = new String(campoSenha.getPassword());

        if (login.isEmpty() || senha.isEmpty()) {
            labelMensagem.setText("Preencha todos os campos!");
            return;
        }

        // Delega para o Controller (padrão MVC)
        boolean sucesso = sistemaController.getLoginController().realizarLogin(login, senha);

        if (sucesso) {
            // Abre a tela principal
            labelMensagem.setText(" ");
            abrirTelaPrincipal();
        } else {
            labelMensagem.setText("Login ou senha incorretos!");
            campoSenha.setText("");
        }
    }

    /*
     * Fecha a tela de login e abre a tela principal do sistema.
     */
    private void abrirTelaPrincipal() {
        CaixaView caixaView = new CaixaView(sistemaController);
        caixaView.setVisible(true);
        this.dispose(); // Fecha a janela de login
    }

    // Estilo

    private void estilizarCampo(JTextField campo) {
        campo.setBackground(new Color(55, 55, 75));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(new Color(0, 0, 0));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 100)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
    }

    private void estilizarBotao(JButton botao, Color cor) {
        botao.setUI(new BasicButtonUI());
        botao.setOpaque(true);
        botao.setContentAreaFilled(true);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(new Font("Arial", Font.BOLD, 13));
        botao.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
