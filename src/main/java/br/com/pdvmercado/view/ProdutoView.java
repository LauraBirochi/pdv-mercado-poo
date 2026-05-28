package br.com.pdvmercado.view;

import br.com.pdvmercado.controller.SistemaController;
import br.com.pdvmercado.model.Produto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * ProdutoView
 *
 * Camada VIEW do padrão MVC.
 *
 * Tela de cadastro e gerenciamento de produtos.
 * Apenas gerentes podem cadastrar/remover produtos.
 * Caixas podem apenas visualizar.
 */
public class ProdutoView extends JFrame {

    private SistemaController sistemaController;

    // Componentes
    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;
    private JTextField campoNome, campoPreco, campoEstoque, campoBusca;
    private boolean ehGerente;

    // Construtor
    public ProdutoView(SistemaController sistemaController) {
        this.sistemaController = sistemaController;
        this.ehGerente = sistemaController.usuarioEhGerente();
        configurarJanela();
        criarComponentes();
        atualizarTabela();
    }

    private void configurarJanela() {
        setTitle("Gerenciamento de Produtos" + (ehGerente ? "" : " (Somente Leitura)"));
        setSize(750, 550);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(28, 28, 42));
        setLayout(new BorderLayout(8, 8));
    }

    private void criarComponentes() {
        // --- Painel de busca ---
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelBusca.setBackground(new Color(35, 35, 52));

        painelBusca.add(criarLabel("🔍 Buscar:"));
        campoBusca = new JTextField(20);
        estilizarCampo(campoBusca);
        painelBusca.add(campoBusca);

        JButton btnBuscar = new JButton("Buscar");
        estilizarBotao(btnBuscar, new Color(60, 120, 200));
        btnBuscar.addActionListener(e -> buscarProdutos());
        painelBusca.add(btnBuscar);

        JButton btnListarTodos = new JButton("Listar Todos");
        estilizarBotao(btnListarTodos, new Color(80, 80, 110));
        btnListarTodos.addActionListener(e -> atualizarTabela());
        painelBusca.add(btnListarTodos);

        add(painelBusca, BorderLayout.NORTH);

        // --- Tabela de produtos ---
        String[] colunas = {"ID", "Nome", "Preço", "Estoque"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabelaProdutos = new JTable(modeloTabela);
        estilizarTabela(tabelaProdutos);
        add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);

        // --- Painel de cadastro (só visível para gerentes) ---
        if (ehGerente) {
            JPanel painelCadastro = criarPainelCadastro();
            add(painelCadastro, BorderLayout.SOUTH);
        } else {
            JLabel aviso = new JLabel("ℹ️ Apenas gerentes podem cadastrar ou remover produtos.",
                    SwingConstants.CENTER);
            aviso.setForeground(new Color(200, 160, 60));
            aviso.setFont(new Font("Arial", Font.ITALIC, 12));
            aviso.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            add(aviso, BorderLayout.SOUTH);
        }
    }

    private JPanel criarPainelCadastro() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(new Color(35, 35, 52));
        painel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 110)),
                "➕ Cadastrar Produto",
                0, 0, new Font("Arial", Font.BOLD, 12), new Color(150, 150, 200)));

        JPanel campos = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        campos.setOpaque(false);

        campos.add(criarLabel("Nome:"));
        campoNome = new JTextField(14);
        estilizarCampo(campoNome);
        campos.add(campoNome);

        campos.add(criarLabel("Preço:"));
        campoPreco = new JTextField(7);
        estilizarCampo(campoPreco);
        campos.add(campoPreco);

        campos.add(criarLabel("Estoque:"));
        campoEstoque = new JTextField(5);
        estilizarCampo(campoEstoque);
        campos.add(campoEstoque);

        painel.add(campos, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        botoes.setOpaque(false);

        JButton btnCadastrar = new JButton("💾 Cadastrar");
        estilizarBotao(btnCadastrar, new Color(60, 160, 90));
        btnCadastrar.addActionListener(e -> cadastrarProduto());
        botoes.add(btnCadastrar);

        JButton btnRemover = new JButton("🗑️ Remover Selecionado");
        estilizarBotao(btnRemover, new Color(180, 60, 60));
        btnRemover.addActionListener(e -> removerProduto());
        botoes.add(btnRemover);

        JButton btnReporEstoque = new JButton("📦 Repor Estoque");
        estilizarBotao(btnReporEstoque, new Color(80, 80, 180));
        btnReporEstoque.addActionListener(e -> reporEstoque());
        botoes.add(btnReporEstoque);

        painel.add(botoes, BorderLayout.EAST);
        return painel;
    }

    // =============================================
    // AÇÕES
    // =============================================

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        ArrayList<Produto> lista = sistemaController.getProdutoController().listarTodos();
        for (Produto p : lista) {
            modeloTabela.addRow(new Object[]{
                    p.getId(),
                    p.getNome(),
                    String.format("R$ %.2f", p.getPreco()),
                    p.getEstoque()
            });
        }
    }

    private void buscarProdutos() {
        String termo = campoBusca.getText().trim();
        modeloTabela.setRowCount(0);
        ArrayList<Produto> lista = sistemaController.getProdutoController().buscarPorNome(termo);
        for (Produto p : lista) {
            modeloTabela.addRow(new Object[]{
                    p.getId(), p.getNome(),
                    String.format("R$ %.2f", p.getPreco()), p.getEstoque()
            });
        }
    }

    private void cadastrarProduto() {
        try {
            String nome = campoNome.getText().trim();
            double preco = Double.parseDouble(campoPreco.getText().replace(",", "."));
            int estoque = Integer.parseInt(campoEstoque.getText().trim());

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o nome do produto.");
                return;
            }

            sistemaController.getProdutoController().novoProduto(nome, preco, estoque);
            atualizarTabela();
            campoNome.setText("");
            campoPreco.setText("");
            campoEstoque.setText("");
            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço e estoque devem ser números válidos.");
        }
    }

    private void removerProduto() {
        int linha = tabelaProdutos.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para remover.");
            return;
        }
        int id = (int) modeloTabela.getValueAt(linha, 0);
        int confirma = JOptionPane.showConfirmDialog(this,
                "Remover produto ID " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            sistemaController.getProdutoController().removerProduto(id);
            atualizarTabela();
        }
    }

    private void reporEstoque() {
        int linha = tabelaProdutos.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para repor o estoque.");
            return;
        }
        int id = (int) modeloTabela.getValueAt(linha, 0);
        String qtdStr = JOptionPane.showInputDialog(this, "Quantidade a adicionar ao estoque:");
        if (qtdStr != null && !qtdStr.isEmpty()) {
            try {
                int qtd = Integer.parseInt(qtdStr);
                sistemaController.getProdutoController().reporEstoque(id, qtd);
                atualizarTabela();
                JOptionPane.showMessageDialog(this, "Estoque atualizado!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Quantidade inválida.");
            }
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
        tabela.getTableHeader().setBackground(new Color(50, 80, 130));
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
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

    private void estilizarBotao(JButton b, Color cor) {
        b.setBackground(cor);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setBorder(BorderFactory.createEmptyBorder(7, 12, 7, 12));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setFocusPainted(false);
    }
}
