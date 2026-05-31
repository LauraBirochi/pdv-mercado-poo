package br.com.pdvmercado.view;

import br.com.pdvmercado.controller.SistemaController;
import br.com.pdvmercado.model.Caixa;
import br.com.pdvmercado.model.Gerente;
import br.com.pdvmercado.model.Usuario;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

/**
 * FuncionarioView
 *
 * Camada VIEW do padrão MVC.
 *
 * Tela de cadastro e gerenciamento de funcionários.
 * Acessível apenas por usuários com perfil GERENTE.
 * Demonstra o controle de permissões via herança (Caixa x Gerente).
 */
public class FuncionarioView extends JFrame {

    private SistemaController sistemaController;

    // Componentes
    private JTable tabelaFuncionarios;
    private DefaultTableModel modeloTabela;
    private JTextField campoNome, campoLogin, campoSenha, campoExtra;
    private JComboBox<String> comboPerfil;

    // Construtor
    public FuncionarioView(SistemaController sistemaController) {
        this.sistemaController = sistemaController;
        configurarJanela();
        criarComponentes();
        atualizarTabela();
    }

    private void configurarJanela() {
        setTitle("Gerenciamento de Funcionários (Gerente)");
        setSize(720, 530);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(25, 25, 38));
        setLayout(new BorderLayout(8, 8));
    }

    private void criarComponentes() {
        // --- Tabela ---
        String[] colunas = {"ID", "Nome", "Login", "Perfil", "Info Extra"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabelaFuncionarios = new JTable(modeloTabela);
        estilizarTabela(tabelaFuncionarios);
        JScrollPane scrollPane = new JScrollPane(tabelaFuncionarios);
        scrollPane.setBackground(new Color(25, 25, 38));
        scrollPane.getViewport().setBackground(new Color(25, 25, 38));
        
        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBackground(new Color(25, 25, 38));
        painelTabela.add(scrollPane, BorderLayout.CENTER);
        add(painelTabela, BorderLayout.CENTER);

        // --- Painel de cadastro ---
        JPanel painelCadastro = criarPainelCadastro();
        add(painelCadastro, BorderLayout.SOUTH);
    }

    private JPanel criarPainelCadastro() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(35, 35, 52));
        painel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 110)),
                "Cadastrar Funcionário",
                0, 0, new Font("Arial", Font.BOLD, 12), new Color(150, 150, 200)));

        JPanel campos = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        campos.setOpaque(false);

        campos.add(criarLabel("Nome:"));
        campoNome = new JTextField(12);
        estilizarCampo(campoNome);
        campos.add(campoNome);

        campos.add(criarLabel("Login:"));
        campoLogin = new JTextField(10);
        estilizarCampo(campoLogin);
        campos.add(campoLogin);

        campos.add(criarLabel("Senha:"));
        campoSenha = new JTextField(8);
        estilizarCampo(campoSenha);
        campos.add(campoSenha);

        campos.add(criarLabel("Perfil:"));
        comboPerfil = new JComboBox<>(new String[]{"CAIXA", "GERENTE"});
        estilizarComboBox(comboPerfil);
        campos.add(comboPerfil);

        campos.add(criarLabel("Nº Caixa / Depto:"));
        campoExtra = new JTextField(8);
        estilizarCampo(campoExtra);
        campos.add(campoExtra);

        painel.add(campos, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        botoes.setOpaque(false);

        JButton btnCadastrar = new JButton("Cadastrar");
        estilizarBotao(btnCadastrar, new Color(60, 160, 90));
        btnCadastrar.addActionListener(e -> cadastrarFuncionario());
        botoes.add(btnCadastrar);

        JButton btnRemover = new JButton("Remover");
        estilizarBotao(btnRemover, new Color(180, 60, 60));
        btnRemover.addActionListener(e -> removerFuncionario());
        botoes.add(btnRemover);

        painel.add(botoes, BorderLayout.EAST);
        return painel;
    }

    // =============================================
    // AÇÕES
    // =============================================

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        ArrayList<Usuario> lista = sistemaController.getLoginController().listarUsuarios();
        for (Usuario u : lista) {
            String infoExtra = "";
            if (u instanceof Caixa) {
                infoExtra = "Caixa nº " + ((Caixa) u).getNumeroCaixa();
            } else if (u instanceof Gerente) {
                infoExtra = "Depto: " + ((Gerente) u).getDepartamento();
            }
            modeloTabela.addRow(new Object[]{
                    u.getId(), u.getNome(), u.getLogin(), u.getPerfil(), infoExtra
            });
        }
    }

    private void cadastrarFuncionario() {
        try {
            String nome = campoNome.getText().trim();
            String login = campoLogin.getText().trim();
            String senha = campoSenha.getText().trim();
            String perfil = (String) comboPerfil.getSelectedItem();
            String extra = campoExtra.getText().trim();

            if (nome.isEmpty() || login.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.");
                return;
            }

            int novoId = sistemaController.getLoginController().gerarProximoId();
            Usuario novoUsuario;

            if ("CAIXA".equals(perfil)) {
                int numCaixa = extra.isEmpty() ? 1 : Integer.parseInt(extra);
                novoUsuario = new Caixa(novoId, nome, login, senha, numCaixa);
            } else {
                String depto = extra.isEmpty() ? "Geral" : extra;
                novoUsuario = new Gerente(novoId, nome, login, senha, depto);
            }

            boolean sucesso = sistemaController.getLoginController().cadastrarUsuario(novoUsuario);
            if (sucesso) {
                atualizarTabela();
                campoNome.setText("");
                campoLogin.setText("");
                campoSenha.setText("");
                campoExtra.setText("");
                JOptionPane.showMessageDialog(this, "Funcionário cadastrado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Login já existente. Escolha outro login.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Número do caixa inválido.");
        }
    }

    private void removerFuncionario() {
        int linha = tabelaFuncionarios.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário para remover.");
            return;
        }
        int id = (int) modeloTabela.getValueAt(linha, 0);
        // Não permite remover o próprio usuário logado
        if (sistemaController.getLoginController().getUsuarioLogado().getId() == id) {
            JOptionPane.showMessageDialog(this, "Você não pode remover o seu próprio usuário!");
            return;
        }
        int confirma = JOptionPane.showConfirmDialog(this,
                "Remover funcionário ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            sistemaController.getLoginController().removerUsuario(id);
            atualizarTabela();
        }
    }

    // =============================================
    // ESTILOS
    // =============================================

    private void estilizarTabela(JTable tabela) {
        tabela.setBackground(new Color(40, 40, 58));
        tabela.setForeground(Color.WHITE);
        tabela.setFont(new Font("Arial", Font.PLAIN, 13));
        tabela.setRowHeight(26);
        
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(50, 80, 130));
                c.setForeground(Color.WHITE);
                c.setFont(new Font("Arial", Font.BOLD, 12));
                return c;
            }
        };
        
        for (int i = 0; i < tabela.getColumnModel().getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        tabela.setGridColor(new Color(60, 60, 80));
    }

    private JLabel criarLabel(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial", Font.PLAIN, 12));
        return l;
    }

    private void estilizarCampo(JTextField c) {
        c.setBackground(new Color(55, 55, 75));
        c.setForeground(Color.WHITE);
        c.setCaretColor(Color.WHITE);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 100)),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
    }

    private void estilizarComboBox(JComboBox<String> combo) {
        combo.setUI(new BasicComboBoxUI());
        combo.setBackground(new Color(55, 55, 75));
        combo.setForeground(Color.WHITE);
        combo.setOpaque(true);
        
        // Estilizar o renderer do combo box
        DefaultListCellRenderer renderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    c.setBackground(new Color(60, 120, 200));
                } else {
                    c.setBackground(new Color(45, 45, 65));
                }
                c.setForeground(Color.WHITE);
                return c;
            }
        };
        combo.setRenderer(renderer);
    }

    private void estilizarBotao(JButton b, Color cor) {
        b.setUI(new BasicButtonUI());
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setBackground(cor);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setBorder(BorderFactory.createEmptyBorder(7, 12, 7, 12));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
